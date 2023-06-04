package com.kikopolis.wordcloudworker.service;

import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ContentCleanService {
    private static final Pattern PATTERN = Pattern.compile("\\s{2,}|\\n");

    public String clean(final String text) {
        final StringBuilder builder = stripNonAlphabetical(text);
        final Matcher matcher = PATTERN.matcher(builder);
        final String matchedAndReplaced = matcher.replaceAll(" ");
        final String trimmed = matchedAndReplaced.trim();
        return trimmed.toLowerCase(Locale.ROOT);
    }

    private static StringBuilder stripNonAlphabetical(final String text) {
        final var builder = new StringBuilder(0);
        final int textLen = text.length();
        for (int i = 0; i < textLen; i++) {
            final char c = text.charAt(i);
            if (Character.isLetter(c) || Character.isWhitespace(c)) {
                builder.append(c);
            } else {
                builder.append(" ");
            }
        }
        return builder;
    }
}
