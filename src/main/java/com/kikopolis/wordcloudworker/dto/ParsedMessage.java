package com.kikopolis.wordcloudworker.dto;

import java.util.UUID;

public class ParsedMessage {
    private final String content;
    private final String ignoredWords;
    private final boolean ignoreDefaultWords;
    private final UUID workOrderUuid;

    public ParsedMessage(final String content,
                         final String ignoredWords,
                         final boolean ignoreDefaultWords,
                         final UUID workOrderUuid) {
        this.content = content;
        this.ignoredWords = ignoredWords;
        this.ignoreDefaultWords = ignoreDefaultWords;
        this.workOrderUuid = workOrderUuid;
    }

    public String getContent() {
        return content;
    }

    public String getIgnoredWords() {
        return ignoredWords;
    }

    public boolean isIgnoreDefaultWords() {
        return ignoreDefaultWords;
    }

    public UUID getWorkOrderUuid() {
        return workOrderUuid;
    }
}
