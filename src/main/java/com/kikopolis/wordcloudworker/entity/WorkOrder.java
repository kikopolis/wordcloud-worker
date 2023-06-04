package com.kikopolis.wordcloudworker.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "work_order")
public class WorkOrder {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private UUID uuid;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime processingStartedAt;
    private LocalDateTime processingFinishedAt;
    private LocalDateTime failedAt;
    private String processingError;

    public Long getId() {
        return id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(final UUID uuid) {
        this.uuid = uuid;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(final LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getProcessingStartedAt() {
        return processingStartedAt;
    }

    public void setProcessingStartedAt(final LocalDateTime processingStartedAt) {
        this.processingStartedAt = processingStartedAt;
    }

    public LocalDateTime getProcessingFinishedAt() {
        return processingFinishedAt;
    }

    public void setProcessingFinishedAt(final LocalDateTime processingFinishedAt) {
        this.processingFinishedAt = processingFinishedAt;
    }

    public LocalDateTime getFailedAt() {
        return failedAt;
    }

    public void setFailedAt(final LocalDateTime failedAt) {
        this.failedAt = failedAt;
    }

    public String getProcessingError() {
        return processingError;
    }

    public void setProcessingError(final String processingError) {
        this.processingError = processingError;
    }

    public enum Status {
        PENDING("PENDING"),
        PROCESSING("PROCESSING"),
        PROCESSED("PROCESSED"),
        FAILED("FAILED");

        private final String status;

        Status(final String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }
    }
}
