package com.vanky.chat.server;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.vanky.chat.common.bo.BaseMsgBo;
import com.vanky.chat.common.bo.OfflineMsgDetailBo;
import com.vanky.chat.common.bo.UserBo;
import com.vanky.chat.common.protobuf.BaseMsgProto;
import com.vanky.chat.common.utils.RedisUtil;
import com.vanky.chat.server.pojo.BaseMsg;
import jakarta.annotation.Resource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.math.BigInteger;
import java.util.*;

/**
 * @author vanky
 * @create 2024/3/29 15:52
 */
@SpringBootTest(classes = ServerApplication.class)
public class Test {

    @Resource
    private RedisTemplate<Object, Object> redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @org.junit.jupiter.api.Test
    public void test(){
        //初始化数据
        BaseMsgProto.BaseMsg.Builder demo = BaseMsgProto.BaseMsg.newBuilder();
        demo.setId(1L)
                .setChatType(2)
                //.setContent("你好啊哥")
                .setUniqueId(1241212L)
                .setFromUserId(3L)
                .setToUserId(2L)
                .setCreateTime(System.currentTimeMillis())
                .setMsgType(1)
                .setStatus(0);


        //序列化
        BaseMsgProto.BaseMsg build = demo.build();

        System.out.println(build);

        //转换成字节数组
        byte[] s = build.toByteArray();
        System.out.println("protobuf数据bytes[]:" + Arrays.toString(s));
        System.out.println("protobuf序列化大小: " + s.length);


        BaseMsgProto.BaseMsg demo1 = null;
        String jsonObject = null;
        try {
            //反序列化
            demo1 = BaseMsgProto.BaseMsg.parseFrom(s);
            //转 json
            jsonObject = JsonFormat.printer().print(demo1);

        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

        System.out.println("Json格式化结果:\n" + jsonObject);
        System.out.println("Json格式化数据大小: " + jsonObject.getBytes().length);
    }

    @org.junit.jupiter.api.Test
    public void testJson(){
        List<OfflineMsgDetailBo> offlineMsgDetailBoList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            OfflineMsgDetailBo offlineMsgDetailBo = new OfflineMsgDetailBo(1L + i, UUID.randomUUID().toString(), new Date(), "哈哈" + i);
            offlineMsgDetailBoList.add(offlineMsgDetailBo);
        }

        String jsonString = JSONObject.toJSONString(offlineMsgDetailBoList);
        System.out.println(jsonString);

        List<UserBo> offlineMsgDetailBo = JSONObject.parseObject(jsonString, new TypeReference<>(UserBo.class){});

        System.out.println(offlineMsgDetailBo.toString());
    }

    @org.junit.jupiter.api.Test
    public void testParseObject(){
        String s = "BaseMsg(id=0, uniqueId=354907874004992, fromUserId=0, toUserId=1, createTime=Sun Apr 14 16:30:16 CST 2024, chatType=0, msgType=7, content={\"content\":\"222条\",\"fromUserId\":2,\"gotoTimestamp\":1713066118684,\"lastTimestamp\":1713082432304,\"msgCount\":3,\"msgType\":7}, status=3)";

        BaseMsg baseMsg = JSONObject.parseObject(s, BaseMsg.class);

        System.out.println(baseMsg);
    }

    @org.junit.jupiter.api.Test
    public void testRedis(){
        String[] keys = {"k1", "k2", "k3"};

        redisTemplate.delete(List.of(keys));
    }

    @org.junit.jupiter.api.Test
    public void testRedisConverter(){
        String key = "chatSession:2";
        //GlobalChatSession o = (GlobalChatSession)redisTemplate.opsForValue().get(key);
        Object o = redisTemplate.opsForValue().get(key);

        System.out.println(o);
    }


    @org.junit.jupiter.api.Test
    public void testJson2(){
        HashMap<String, String> map = new HashMap<>();
        map.put("id", "123");
        map.put("age", "2");

        String jsonString = JSONObject.toJSONString(map);
        System.out.println(jsonString);
    }

    @org.junit.jupiter.api.Test
    public void testJson3(){
        String key = "im:data:base_msg:1713706973420";
        String jsonString = RedisUtil.get(key, String.class);

        BaseMsgBo baseMsgBo = JSONObject.parseObject(jsonString, BaseMsgBo.class);

        System.out.println(baseMsgBo);
    }

    @org.junit.jupiter.api.Test
    public void testJson4(){
        BigInteger bigInteger = new BigInteger("4283975923048209352873894270358239084");

        String jsonString = JSONObject.toJSONString(bigInteger);
        System.out.println(jsonString);
    }

}
