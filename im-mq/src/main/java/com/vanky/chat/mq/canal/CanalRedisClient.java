package com.vanky.chat.mq.canal;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.google.common.base.CaseFormat;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry.*;
import com.alibaba.otter.canal.protocol.Message;
import com.vanky.chat.common.bo.CanalModel;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * canal 客户端
 */
@Component
@Slf4j
public class CanalRedisClient {

    @Resource
    @Lazy
    private CanalRedisProxy canalRedisProxy;

    @Value("${canal.address}")
    private String canalAddress;

    @Value("${canal.port}")
    private int canalPort;

    @Value("${canal.destination}")
    private String canalDestination;

    @PostConstruct
    public void canalExample() {
        // 创建链接
        CompletableFuture.runAsync(() -> {
            CanalConnector connector = CanalConnectors
                    .newSingleConnector(new InetSocketAddress(canalAddress,
                            canalPort), canalDestination, "", "");

            log.info("连接 canal 成功！");
            int batchSize = 1000;
            int emptyCount = 0;

            try {
                connector.connect();
                connector.subscribe(".*\\..*");
                connector.rollback();
                int totalEmptyCount = 3600;
                while (emptyCount < totalEmptyCount) {
                    Message message = connector.getWithoutAck(batchSize); // 获取指定数量的数据
                    long batchId = message.getId();
                    int size = message.getEntries().size();
                    if (batchId == -1 || size == 0) {
                        emptyCount++;
                        //log.info("empty count : {}", emptyCount);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                        }
                    } else {
                        emptyCount = 0;
                        //把修改消息进行打包
                        toJsonString(message.getEntries());
                    }

                    connector.ack(batchId); // 提交确认
                }

                log.warn("empty too many times, exit");
            } finally {
                connector.disconnect();
            }
        });

    }

    /**
     * 一行数据封装为一个 Canal 类
     * @param entries
     */
    private void toJsonString(List<Entry> entries){
        for (Entry entry : entries) {
            if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN || entry.getEntryType() == EntryType.TRANSACTIONEND) {
                continue;
            }

            RowChange rowChage = null;
            try {
                rowChage = RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(),
                        e);
            }

            CanalModel canalModel = new CanalModel();

            canalModel.setDbName(entry.getHeader().getSchemaName());
            canalModel.setTableName(entry.getHeader().getTableName());
            canalModel.setEventType(rowChage.getEventType().toString());

            for (RowData rowData : rowChage.getRowDatasList()) {
                String dataId = getDataId(rowData.getBeforeColumnsList());
                if (StringUtils.hasText(dataId)){
                    canalModel.setDataId(dataId);
                }else {
                    canalModel.setDataId(getDataId(rowData.getAfterColumnsList()));
                }

                canalModel.setBefore(columnToJsonString(rowData.getBeforeColumnsList()));
                canalModel.setAfter(columnToJsonString(rowData.getAfterColumnsList()));

                //把jsonString 放入rabbitmq中
                canalRedisProxy.putMQ(canalModel);
            }
        }
    }

    /**
     * 获取数据的id，用于 redis 的 存储key
     * @param columns
     * @return
     */
    private String getDataId(List<Column> columns){
        for (Column column : columns){
            if ("id".equals(column.getName())){
                return column.getValue();
            }
        }

        return "";
    }

    /**
     * 把表中的行转换为json格式，用于存储到redis
     * @param columns
     * @return
     */
    private String columnToJsonString(List<Column> columns){
        HashMap<String, String> map = new HashMap<>();
        for (Column column : columns) {
            String columnName = column.getName();
            String resultStr = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, columnName);

            map.put(resultStr, column.getValue());
        }

        String jsonString = JSONObject.toJSONString(map);

        return jsonString;
    }
}