package com.vanky.chat.mq.config;

import com.vanky.chat.common.bo.BaseMsgBo;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.mapping.Jackson2JavaTypeMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * kafka 配置类
 */
@Configuration
public class KafkaConfig {

	@Bean
	public ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory(
			ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
			ConsumerFactory<Object, Object> kafkaConsumerFactory) {

		ConcurrentKafkaListenerContainerFactory<Object, Object> factory =
				new ConcurrentKafkaListenerContainerFactory<>();
		factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
		configurer.configure(factory, kafkaConsumerFactory);

		return factory;
	}

	// 当传输的是个实体类时，进行消息格式转换
	@Bean
	public RecordMessageConverter converter() {
		StringJsonMessageConverter converter = new StringJsonMessageConverter();
		DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
		typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID);
		typeMapper.addTrustedPackages("com.vanky.chat");
		Map<String, Class<?>> mappings = new HashMap<>();
		mappings.put("baseMsgBo", BaseMsgBo.class);
		typeMapper.setIdClassMapping(mappings);
		converter.setTypeMapper(typeMapper);
		return converter;
	}

	@Bean
	public NewTopic pushPrivateMsg() {
		return new NewTopic("privateMsg", 1, (short) 2);
	}

	@Bean
	public NewTopic pushGroupMsg() {
		return new NewTopic("groupMsg", 1, (short) 2);
	}

	@Bean
	public NewTopic pushUserStatusChangeMsg(){
		return new NewTopic("userStatusChangeMsg", 1, (short) 2);
	}

	@Bean
	public NewTopic pushHistoryMsg(){
		return new NewTopic("historyMsg", 1, (short) 2);
	}

	@Bean
	public NewTopic pushOfflinePrivateMsg() {
		return new NewTopic("offlinePrivateMsg", 1, (short) 2);
	}

	@Bean
	public NewTopic pushOfflineGroupMsg() {
		return new NewTopic("offlineGroupMsg", 1, (short) 2);
	}

	@Bean
	public NewTopic pushOfflineMsgInfo() {
		return new NewTopic("offlineMsgInfo", 1, (short) 2);
	}
}
