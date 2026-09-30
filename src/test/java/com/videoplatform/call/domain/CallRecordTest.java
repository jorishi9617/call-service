package com.videoplatform.call.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CallRecordTest {
    @Test
    void recipientCanJoinThenEitherParticipantCanEnd() {
        UUID caller = UUID.randomUUID();
        UUID callee = UUID.randomUUID();
        CallRecord call = new CallRecord(caller, callee);

        call.join(callee);
        assertEquals(CallStatus.ACTIVE, call.getStatus());
        call.end(caller);
        assertEquals(CallStatus.ENDED, call.getStatus());
    }

    @Test
    void unrelatedUserCannotJoin() {
        CallRecord call = new CallRecord(UUID.randomUUID(), UUID.randomUUID());
        assertThrows(IllegalStateException.class, () -> call.join(UUID.randomUUID()));
    }

    @Test
    void onlyRecipientCanRejectARingingCall() {
        UUID callee = UUID.randomUUID();
        CallRecord call = new CallRecord(UUID.randomUUID(), callee);
        call.reject(callee);
        assertEquals(CallStatus.REJECTED, call.getStatus());
    }
}
