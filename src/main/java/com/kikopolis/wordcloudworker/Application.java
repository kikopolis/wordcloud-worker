package com.kikopolis.wordcloudworker;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.nio.charset.StandardCharsets;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {
    private static String queueName = "wordcloud";
    
    public static void main(final String[] args) throws Exception {
        final var application = new Application();
        final var configure = application.configure(new SpringApplicationBuilder(Application.class));
        configure.run(args);
        
        final var factory = new ConnectionFactory();
        factory.setHost("mq");
        factory.setPort(5672);
        factory.setUsername("compose-mq");
        factory.setPassword("compose-mq");
        final var connection = factory.newConnection();
        final var channel = connection.createChannel();
        
        channel.queueDeclare(queueName, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [x] Received '" + message + "'");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }
}
