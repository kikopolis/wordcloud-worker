package com.kikopolis.wordcloudworker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class WordIgnoreService {
    private static final List<String> defaults = List.of("a", "at", "in", "on", "the", "to", "and", "but", "or");
    private final ContentCleanService contentCleanService;
    private final List<String> ignoredWords;

    @Autowired
    public WordIgnoreService(final ContentCleanService contentCleanService) {
        ignoredWords = new ArrayList<>(0);
        this.contentCleanService = contentCleanService;
    }

    public List<String> getIgnoredWords() {
        return Collections.unmodifiableList(ignoredWords);
    }

    public void compile(final String userIgnoredWordStr, final boolean ignoreDefaultWords) {
        if (userIgnoredWordStr != null && !userIgnoredWordStr.isEmpty()) {
            final List<String> userIgnoredWords = populateWithUserWords(userIgnoredWordStr);
            ignoredWords.addAll(userIgnoredWords);
        }
        if (ignoreDefaultWords) {
            ignoredWords.addAll(defaults);
        }
    }

    public boolean isIgnored(final String word) {
        return ignoredWords.contains(word);
    }

    private List<String> populateWithUserWords(final String ignoredWords) {
        final var wordList = new ArrayList<String>(0);
        final String[] split = ignoredWords.split(",");
        final var userWordList = List.of(split);
        for (final var word : userWordList) {
            final var cleanedWord = contentCleanService.clean(word);
            if (!cleanedWord.isEmpty()) {
                wordList.add(cleanedWord);
            }
        }
        return wordList;
    }
}
