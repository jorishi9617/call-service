package com.videoplatform.call.api;

import com.videoplatform.call.domain.CallRecord;
import com.videoplatform.call.domain.CallStatus;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public final class CallDtos {
    private CallDtos() {}
    public record CreateCallRequest(@NotNull UUID calleeId) {}
    public record CallResponse(UUID id, UUID callerId, UUID calleeId, CallStatus status,
                               Instant createdAt, Instant answeredAt, Instant endedAt) {
        public static CallResponse from(CallRecord call) {
            return new CallResponse(call.getId(), call.getCallerId(), call.getCalleeId(), call.getStatus(),
                    call.getCreatedAt(), call.getAnsweredAt(), call.getEndedAt());
        }
    }
}
