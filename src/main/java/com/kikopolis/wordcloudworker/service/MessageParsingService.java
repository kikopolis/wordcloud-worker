package com.kikopolis.wordcloudworker.service;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.kikopolis.mq.MqMessageProto;
import com.kikopolis.wordcloudworker.dto.ParsedMessage;
import org.slf4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class MessageParsingService {
    private static final Logger LOGGER = getLogger(MessageParsingService.class);

    public ParsedMessage parseMessage(final Message message) {
        LOGGER.info(" [x] Begin parsing RabbitMQ message");
        MqMessageProto.MqMessage mqMsg = null;
        try {
            final byte[] bytes = message.getBody();
            mqMsg = MqMessageProto.MqMessage.parseFrom(bytes);
        } catch (final InvalidProtocolBufferException e) {
            LOGGER.error(" [!!] Error parsing RabbitMQ message, invalid protocol buffer");
        }
        return getParsedMessage(mqMsg);
    }

    private ParsedMessage getParsedMessage(final MqMessageProto.MqMessage mqMsg) {
        validate(mqMsg);
        final ByteString fileContents = mqMsg.getFile();
        final String content = fileContents.toStringUtf8();
        final String ignoredWords = mqMsg.getIgnoredWords();
        final boolean ignoreDefaultWords = mqMsg.getIgnoreDefaultWords();
        final String strUuid = mqMsg.getUuid();
        final UUID workOrderUuid = UUID.fromString(strUuid);
        final var parsedMessage = new ParsedMessage(content, ignoredWords, ignoreDefaultWords, workOrderUuid);
        LOGGER.info(" [x] Finished parsing RabbitMQ message");
        return parsedMessage;
    }

    private void validate(final MqMessageProto.MqMessage mqMsg) {
        if (mqMsg == null) {
            LOGGER.error(" [!!] Error parsing RabbitMQ message, null message");
            throw new IllegalArgumentException(" [!!] Error parsing RabbitMQ message, null message");
        }
        if (mqMsg.getFile() == null) {
            LOGGER.error(" [!!] Error parsing RabbitMQ message, null file");
            throw new IllegalArgumentException(" [!!] Error parsing RabbitMQ message, null file");
        }
        if (mqMsg.getUuid() == null) {
            LOGGER.error(" [!!] Error parsing RabbitMQ message, null uuid");
            throw new IllegalArgumentException(" [!!] Error parsing RabbitMQ message, null uuid");
        }
    }
}
