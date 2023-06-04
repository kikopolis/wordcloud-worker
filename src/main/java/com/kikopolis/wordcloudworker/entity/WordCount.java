package com.kikopolis.wordcloudworker.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "word_count")
public class WordCount {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private Long workOrderId;
    private UUID uuid;
    private String word;
    private int count;
    private LocalDateTime createdAt;

    public WordCount() {
    }

    public WordCount(final String word, final Integer count, final Long workOrderId) {
        this.word = word;
        this.count = count.intValue();
        this.workOrderId = workOrderId;
        createdAt = LocalDateTime.now();
        uuid = UUID.randomUUID();
    }

    public Long getId() {
        return id;
    }

    public Long getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(final Long workOrderId) {
        this.workOrderId = workOrderId;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(final UUID uuid) {
        this.uuid = uuid;
    }

    public String getWord() {
        return word;
    }

    public void setWord(final String word) {
        this.word = word;
    }

    public int getCount() {
        return count;
    }

    public void setCount(final int count) {
        this.count = count;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
