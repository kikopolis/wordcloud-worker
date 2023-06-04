package com.kikopolis.wordcloudworker.service;

import com.kikopolis.wordcloudworker.dto.ParsedMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WordCounterService {
    private final WordIgnoreService wordIgnoreService;
    private final ContentCleanService contentCleanService;

    @Autowired
    public WordCounterService(final WordIgnoreService wordIgnoreService,
                              final ContentCleanService contentCleanService) {
        this.wordIgnoreService = wordIgnoreService;
        this.contentCleanService = contentCleanService;
    }

    public HashMap<String, Integer> countUniqueWords(final ParsedMessage parsedMessage) {
        final String content = parsedMessage.getContent();
        final String ignoredWords = parsedMessage.getIgnoredWords();
        final boolean ignoreDefaultWords = parsedMessage.isIgnoreDefaultWords();
        wordIgnoreService.compile(ignoredWords, ignoreDefaultWords);
        final String cleanText = contentCleanService.clean(content);
        return countWords(cleanText);
    }

    private HashMap<String, Integer> countWords(final String cleanText) {
        final String[] words = cleanText.split(" ");
        final var wordCounts = new HashMap<String, Integer>(0);
        for (final var word : words) {
            if (!wordIgnoreService.isIgnored(word)) {
                addWord(wordCounts, word);
            }
        }
        return wordCounts;
    }

    private void addWord(final Map<String, Integer> wordCounts, final String word) {
        if (wordCounts.containsKey(word)) {
            final var count = wordCounts.get(word);
            wordCounts.put(word, count + 1);
        } else {
            wordCounts.put(word, 1);
        }
    }
}
