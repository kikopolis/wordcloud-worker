package com.kikopolis.wordcloudworker.service;

import com.kikopolis.wordcloudworker.entity.WordCount;
import com.kikopolis.wordcloudworker.entity.WorkOrder;
import com.kikopolis.wordcloudworker.repository.WordCountRepository;
import com.kikopolis.wordcloudworker.repository.WorkOrderRepository;
import org.slf4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class MessageProcessingService {
    private static final Logger LOGGER = getLogger(MessageProcessingService.class);
    private final WordCounterService wordCounterService;
    private final WorkOrderRepository workOrderRepository;
    private final WordCountRepository wordCountRepository;
    private final MessageParsingService messageParsingService;

    @Autowired
    public MessageProcessingService(final WordCounterService wordCounterService,
                                    final WorkOrderRepository workOrderRepository,
                                    final WordCountRepository wordCountRepository,
                                    final MessageParsingService messageParsingService) {
        this.wordCounterService = wordCounterService;
        this.workOrderRepository = workOrderRepository;
        this.wordCountRepository = wordCountRepository;
        this.messageParsingService = messageParsingService;
    }

    public void process(Message message) {
        final var parsedMessage = messageParsingService.parseMessage(message);
        LOGGER.info(" [x] Received and parsed");

        final UUID workOrderUuid = parsedMessage.getWorkOrderUuid();
        final var workOrder = createWorkOrder(workOrderUuid);
        final Long workOrderId = workOrder.getId();

        LOGGER.info(" [x] Processing message");
        startWorkOrder(workOrderId);
        final Map<String, Integer> wordCountResults = wordCounterService
                .countUniqueWords(parsedMessage);
        LOGGER.info(" [x] Finished processing message");

        saveResults(wordCountResults, workOrder);

        finishWorkOrder(workOrderId);

    }

    private WorkOrder createWorkOrder(final UUID uuid) {
        LOGGER.info(" [x] Creating work order");
        final var workOrder = new WorkOrder();
        workOrder.setUuid(uuid);
        workOrder.setStatus(WorkOrder.Status.PENDING);
        workOrder.setCreatedAt(LocalDateTime.now());
        final var resultingWorkOrder = workOrderRepository.save(workOrder);
        LOGGER.info(" [x] Created work order: {}", resultingWorkOrder);
        return resultingWorkOrder;
    }

    private void startWorkOrder(final Long id) {
        LOGGER.info(" [x] Starting work order");
        final var workOrder = workOrderRepository.findById(id).orElseThrow();
        workOrder.setStatus(WorkOrder.Status.PROCESSING);
        workOrder.setProcessingStartedAt(LocalDateTime.now());
        workOrder.setUpdatedAt(LocalDateTime.now());
        workOrderRepository.save(workOrder);
        LOGGER.info(" [x] Starting work order successful");
    }

    private void finishWorkOrder(final Long id) {
        LOGGER.info(" [x] Finishing work order");
        final var workOrder = workOrderRepository.findById(id).orElseThrow();
        workOrder.setStatus(WorkOrder.Status.PROCESSED);
        workOrder.setProcessingFinishedAt(LocalDateTime.now());
        workOrder.setUpdatedAt(LocalDateTime.now());
        workOrderRepository.save(workOrder);
        LOGGER.info(" [x] Finishing work order successful");
    }

    private void saveResults(final Map<String, Integer> wordCountResults,
                             final WorkOrder workOrder) {
        LOGGER.info(" [x] Saving results");
        final Long workOrderId = workOrder.getId();
        wordCountResults.forEach((word, count) -> {
            final WordCount wordCount = new WordCount(word, count, workOrderId);
            wordCountRepository.save(wordCount);
        });
        LOGGER.info(" [x] Saving results completed successfully");
    }
}
