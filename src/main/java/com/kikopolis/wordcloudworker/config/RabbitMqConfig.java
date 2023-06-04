package com.kikopolis.wordcloudworker.config;

import com.kikopolis.wordcloudworker.listener.RabbitMqListener;
import org.slf4j.Logger;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.slf4j.LoggerFactory.getLogger;

@Configuration
public class RabbitMqConfig {
    private static final Logger LOGGER = getLogger(RabbitMqConfig.class);
    @Value("${mq.queue}")
    private String mqQueue;
    @Value("${mq.exchange}")
    private String mqExchange;
    @Value("${mq.routingkey}")
    private String mqRoutingKey;
    @Value("${mq.host}")
    private String mqHost;
    @Value("${mq.port}")
    private int mqPort;
    @Value("${mq.username}")
    private String mqUsername;
    @Value("${mq.password}")
    private String mqPassword;

    private final RabbitMqListener rabbitMqListener;

    @Autowired
    public RabbitMqConfig(final RabbitMqListener rabbitMqListener) {
        this.rabbitMqListener = rabbitMqListener;
    }

    @Bean
    Queue getQueue() {
        return new Queue(mqQueue);
    }

    @Bean
    DirectExchange getExchange() {
        return new DirectExchange(mqExchange);
    }

    @Bean
    Binding getBinding(final Queue queue, final DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(mqRoutingKey);
    }

    @Bean
    ConnectionFactory connectionFactory() {
        final var factory = new CachingConnectionFactory();
        factory.setHost(mqHost);
        factory.setPort(mqPort);
        factory.setUsername(mqUsername);
        factory.setPassword(mqPassword);
        return factory;
    }

    @Bean
    MessageListenerContainer container(final ConnectionFactory connectionFactory) {
        final var container = new SimpleMessageListenerContainer();
        final var queue = getQueue();
        container.setConnectionFactory(connectionFactory);
        container.setQueues(queue);
        container.setMessageListener(rabbitMqListener);
        return container;
    }
}
