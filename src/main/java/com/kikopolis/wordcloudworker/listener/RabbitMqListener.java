package com.kikopolis.wordcloudworker.listener;

import com.kikopolis.wordcloudworker.service.MessageProcessingService;
import org.slf4j.Logger;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class RabbitMqListener implements MessageListener {
    private static final Logger LOGGER = getLogger(RabbitMqListener.class);
    private final MessageProcessingService messageProcessingService;

    @Autowired
    public RabbitMqListener(final MessageProcessingService messageProcessingService) {
        this.messageProcessingService = messageProcessingService;
    }

    @Override
    public void onMessage(final Message message) {
        LOGGER.info(" [x] Received from RabbitMQ");
        messageProcessingService.process(message);
    }

    @Override
    public void containerAckMode(AcknowledgeMode mode) {
        MessageListener.super.containerAckMode(mode);
    }

    @Override
    public boolean isAsyncReplies() {
        return MessageListener.super.isAsyncReplies();
    }

    @Override
    public void onMessageBatch(List<Message> messages) {
        MessageListener.super.onMessageBatch(messages);
    }
}
