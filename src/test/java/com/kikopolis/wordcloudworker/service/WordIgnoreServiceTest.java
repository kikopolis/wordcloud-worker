package com.kikopolis.wordcloudworker.service;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WordIgnoreServiceTest {
    private static ContentCleanService contentCleanService;

    @BeforeAll
    static void setUp() {
        contentCleanService = mock(ContentCleanService.class);
    }

    @Test
    void test_compile_adds_default_words() {
        final var wordIgnoreService = new WordIgnoreService(contentCleanService);
        when(contentCleanService.clean(anyString())).thenReturn("a", "at", "in", "on", "the", "to", "and", "but", "or");
        wordIgnoreService.compile("", true);
        final Matcher<Iterable<String>> defaults = hasItems("a", "at", "in", "on", "the", "to", "and", "but", "or");
        final List<String> ignoredWords = wordIgnoreService.getIgnoredWords();
        assertThat(ignoredWords, defaults);
    }

    @Test
    void test_compile_adds_user_words() {
        final var wordIgnoreService = new WordIgnoreService(contentCleanService);
        when(contentCleanService.clean(anyString())).thenReturn("always", "at", "the", "beginning");
        wordIgnoreService.compile("always,at,the,beginning", false);
        final Matcher<Iterable<String>> defaults = hasItems("always", "at", "the", "beginning");
        final List<String> ignoredWords = wordIgnoreService.getIgnoredWords();
        assertThat(ignoredWords, defaults);
    }

    @Test
    void test_compile_cleans_user_words_of_numbers_and_symbols() {
        final var wordIgnoreService = new WordIgnoreService(contentCleanService);
        when(contentCleanService.clean(anyString())).thenReturn("always", "at", "the", "beginning");
        wordIgnoreService.compile("always@,##at,the,beginning123,456,789,.,!,@,#,$,%,^,&,*,(,),-,_,+,=", false);
        final Matcher<Iterable<String>> defaults = hasItems("always", "at", "the", "beginning");
        final List<String> ignoredWords = wordIgnoreService.getIgnoredWords();
        assertThat(ignoredWords, defaults);
    }

    @Test
    void test_compile_cleans_spaces() {
        final var wordIgnoreService = new WordIgnoreService(contentCleanService);
        when(contentCleanService.clean(anyString())).thenReturn("always", "at", "the", "beginning");
        wordIgnoreService.compile("always, at, the, beginning", false);
        final Matcher<Iterable<String>> defaults = hasItems("always", "at", "the", "beginning");
        final List<String> ignoredWords = wordIgnoreService.getIgnoredWords();
        assertThat(ignoredWords, defaults);
    }

    @Test
    void test_ignored_returns_true() {
        final var wordIgnoreService = new WordIgnoreService(contentCleanService);
        when(contentCleanService.clean(anyString())).thenReturn("always", "at", "the", "beginning");
        wordIgnoreService.compile("always,at,the,beginning", false);
        final boolean ignored = wordIgnoreService.isIgnored("always");
        assertTrue(ignored);
    }

    @Test
    void test_ignored_returns_false() {
        final var wordIgnoreService = new WordIgnoreService(contentCleanService);
        when(contentCleanService.clean(anyString())).thenReturn("always", "at", "the", "beginning");
        wordIgnoreService.compile("always,at,the,beginning", false);
        final boolean ignored = wordIgnoreService.isIgnored("never");
        assertTrue(!ignored);
    }
}