package com.videoplatform.call.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "calls")
public class CallRecord {
    @Id
    private UUID id;
    @Column(name = "caller_id", nullable = false)
    private UUID callerId;
    @Column(name = "callee_id", nullable = false)
    private UUID calleeId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CallStatus status;
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
    @Column(name = "answered_at")
    private Instant answeredAt;
    @Column(name = "ended_at")
    private Instant endedAt;
    @Version
    private long version;

    protected CallRecord() {}

    public CallRecord(UUID callerId, UUID calleeId) {
        this.id = UUID.randomUUID();
        this.callerId = callerId;
        this.calleeId = calleeId;
        this.status = CallStatus.RINGING;
        this.createdAt = Instant.now();
    }

    public void join(UUID userId) {
        if (!calleeId.equals(userId) || status != CallStatus.RINGING) {
            throw new IllegalStateException("Call cannot be joined by this user in its current state");
        }
        status = CallStatus.ACTIVE;
        answeredAt = Instant.now();
    }

    public void end(UUID userId) {
        if ((!callerId.equals(userId) && !calleeId.equals(userId))
                || (status != CallStatus.RINGING && status != CallStatus.ACTIVE)) {
            throw new IllegalStateException("Call cannot be ended by this user in its current state");
        }
        status = CallStatus.ENDED;
        endedAt = Instant.now();
    }

    public void reject(UUID userId) {
        if (!calleeId.equals(userId) || status != CallStatus.RINGING) {
            throw new IllegalStateException("Call cannot be rejected by this user in its current state");
        }
        status = CallStatus.REJECTED;
        endedAt = Instant.now();
    }

    public UUID getId() { return id; }
    public UUID getCallerId() { return callerId; }
    public UUID getCalleeId() { return calleeId; }
    public CallStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getAnsweredAt() { return answeredAt; }
    public Instant getEndedAt() { return endedAt; }
}
