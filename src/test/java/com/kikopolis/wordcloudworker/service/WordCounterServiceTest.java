package com.kikopolis.wordcloudworker.service;

import com.kikopolis.wordcloudworker.dto.ParsedMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WordCounterServiceTest {
    private WordIgnoreService wordIgnoreService;
    private ContentCleanService contentCleanService;

    @BeforeEach
    void setUp() {
        wordIgnoreService = mock(WordIgnoreService.class);
        contentCleanService = mock(ContentCleanService.class);
    }

    @Test
    void test_count_unique_words_with_normal_text() {
        final var text = "This is a test";
        final var ignoredWords = "";
        final var ignoreDefaultWords = false;
        final var wordCounterService = new WordCounterService(wordIgnoreService, contentCleanService);

        when(contentCleanService.clean(anyString())).thenReturn("this is a test");

        final ParsedMessage parsedMessage = new ParsedMessage(text, ignoredWords, ignoreDefaultWords, null);
        final var result = wordCounterService.countUniqueWords(parsedMessage);

        assertEquals(4, result.size());
        assertEquals(1, result.get("this"));
        assertEquals(1, result.get("is"));
        assertEquals(1, result.get("a"));
        assertEquals(1, result.get("test"));
    }

    @Test
    void test_count_unique_words_with_ignored_words() {
        final var text = "This is a test";
        final var ignoredWords = "test";
        final var ignoreDefaultWords = false;
        final var wordCounterService = new WordCounterService(wordIgnoreService, contentCleanService);

        when(contentCleanService.clean(anyString())).thenReturn("this is a test");
        when(wordIgnoreService.isIgnored("test")).thenReturn(true);

        final ParsedMessage parsedMessage = new ParsedMessage(text, ignoredWords, ignoreDefaultWords, null);
        final var result = wordCounterService.countUniqueWords(parsedMessage);

        assertEquals(3, result.size());
        assertEquals(1, result.get("this"));
        assertEquals(1, result.get("is"));
        assertEquals(1, result.get("a"));
    }

    @Test
    void test_count_unique_words_with_ignored_default_words() {
        final var text = "This is a test";
        final var ignoredWords = "";
        final var ignoreDefaultWords = true;
        final var wordCounterService = new WordCounterService(wordIgnoreService, contentCleanService);

        when(contentCleanService.clean(anyString())).thenReturn("this is a test");
        when(wordIgnoreService.isIgnored("is")).thenReturn(true);
        when(wordIgnoreService.isIgnored("a")).thenReturn(true);

        final ParsedMessage parsedMessage = new ParsedMessage(text, ignoredWords, ignoreDefaultWords, null);
        final var result = wordCounterService.countUniqueWords(parsedMessage);

        assertEquals(2, result.size());
        assertEquals(1, result.get("this"));
        assertEquals(1, result.get("test"));
    }

    @Test
    void test_count_unique_words_with_symbols_and_numbers() {
        final var text = "This is a test with symbols and numbers 1234 !@#$%^&*()";
        final var ignoredWords = "";
        final var ignoreDefaultWords = false;
        final var wordCounterService = new WordCounterService(wordIgnoreService, contentCleanService);

        when(contentCleanService.clean(anyString())).thenReturn("this is a test with symbols and numbers");

        final ParsedMessage parsedMessage = new ParsedMessage(text, ignoredWords, ignoreDefaultWords, null);
        final var result = wordCounterService.countUniqueWords(parsedMessage);

        assertEquals(8, result.size());
        assertEquals(1, result.get("this"));
        assertEquals(1, result.get("is"));
        assertEquals(1, result.get("a"));
        assertEquals(1, result.get("test"));
        assertEquals(1, result.get("with"));
        assertEquals(1, result.get("symbols"));
        assertEquals(1, result.get("and"));
        assertEquals(1, result.get("numbers"));
        assertNull(result.get("1234"));
        assertNull(result.get("!@#$%^&*()"));
    }

    @Test
    void test_count_unique_words_with_duplicate_words_ignoring_defaults() {
        final var text = "This is a test with duplicate words This is a test test test this";
        final var ignoredWords = "";
        final var ignoreDefaultWords = true;
        final var wordCounterService = new WordCounterService(wordIgnoreService, contentCleanService);

        when(contentCleanService.clean(anyString())).thenReturn("this is a test with duplicate words this is a test test test this");
        when(wordIgnoreService.isIgnored("is")).thenReturn(true);
        when(wordIgnoreService.isIgnored("a")).thenReturn(true);

        final ParsedMessage parsedMessage = new ParsedMessage(text, ignoredWords, ignoreDefaultWords, null);
        final var result = wordCounterService.countUniqueWords(parsedMessage);

        assertEquals(5, result.size());
        assertEquals(3, result.get("this"));
        assertEquals(4, result.get("test"));
        assertEquals(1, result.get("with"));
        assertEquals(1, result.get("duplicate"));
        assertEquals(1, result.get("words"));
    }
}