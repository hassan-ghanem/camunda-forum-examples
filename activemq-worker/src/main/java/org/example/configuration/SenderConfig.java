package org.example.configuration;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.example.Sender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class SenderConfig {

	private Logger logger = LoggerFactory.getLogger(SenderConfig.class);

	@Value("${activemq.broker-url}")
	private String brokerUrl;

	public ActiveMQConnectionFactory senderActiveMQConnectionFactory() {
		logger.info("initializing ActiveMQConnectionFactory");
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();

		logger.info("Setting up broker " + brokerUrl);
		activeMQConnectionFactory.setBrokerURL(brokerUrl);

		return activeMQConnectionFactory;
	}

	@Bean
	public CachingConnectionFactory cachingConnectionFactory() {
		return new CachingConnectionFactory(senderActiveMQConnectionFactory());
	}

	@Bean
	public JmsTemplate jmsTemplate() {
		return new JmsTemplate(cachingConnectionFactory());
	}

	@Bean
	public Sender sender() {
		return new Sender();
	}
}
