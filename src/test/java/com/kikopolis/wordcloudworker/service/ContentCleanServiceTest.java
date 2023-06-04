package com.kikopolis.wordcloudworker.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContentCleanServiceTest {
    @Test
    void test_clean_strips_numbers() {
        final var contentCleanService = new ContentCleanService();
        final var text = "asd 1234 asd";
        final var expected = "asd asd";
        final var actual = contentCleanService.clean(text);
        assertEquals(expected, actual);
    }

    @Test
    void test_clean_strips_leading_and_trailing_whitespaces() {
        final var contentCleanService = new ContentCleanService();
        final var text = " asd 1234 asd ";
        final var expected = "asd asd";
        final var actual = contentCleanService.clean(text);
        assertEquals(expected, actual);
    }

    @Test
    void test_clean_works_with_custom_separator() {
        final var contentCleanService = new ContentCleanService();
        final var text = "asd 1234 asd";
        final var expected = "asd asd";
        final var actual = contentCleanService.clean(text);
        assertEquals(expected, actual);
    }

    @Test
    void test_clean_works_with_non_english_characters() {
        final var contentCleanService = new ContentCleanService();
        final var text = "asd 1234 asd ąęć üõäö";
        final var expected = "asd asd ąęć üõäö";
        final var actual = contentCleanService.clean(text);
        assertEquals(expected, actual);
    }

    @Test
    void test_clean_removes_symbols() {
        final var contentCleanService = new ContentCleanService();
        final var text = "asd 1234 asd ąęć üõäö !@#$%^&*()_+";
        final var expected = "asd asd ąęć üõäö";
        final var actual = contentCleanService.clean(text);
        assertEquals(expected, actual);
    }

    @Test
    void test_clean_always_returns_lowercase_text() {
        final var contentCleanService = new ContentCleanService();
        final var text = "ASD 1234 ASD ąęć ÜÕÄÖ !@#$%^&*()_+";
        final var expected = "asd asd ąęć üõäö";
        final var actual = contentCleanService.clean(text);
        assertEquals(expected, actual);
    }

    @Test
    void test_real_text_for_valid_result() {
        final var contentCleanService = new ContentCleanService();
        final var text = "The quick brown bear, walked oveR the lazy turtle. " +
                "The Turtle did not respond.in any manner. The end!!!";
        final var expected = "the quick brown bear walked over the lazy turtle " +
                "the turtle did not respond in any manner the end";
        final var actual = contentCleanService.clean(text);
        assertEquals(expected, actual);
    }

    @Test
    void test_clean_removes_line_breaks() {
        final var contentCleanService = new ContentCleanService();
        final var text = "asd 1234 asd\nasd 1234 asd";
        final var expected = "asd asd asd asd";
        final var actual = contentCleanService.clean(text);
        assertEquals(expected, actual);
    }
}