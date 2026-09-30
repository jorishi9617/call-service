package com.videoplatform.call.api;

import com.videoplatform.call.domain.CallRecord;
import com.videoplatform.call.domain.CallRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class CallService {
    private final CallRepository calls;

    public CallService(CallRepository calls) { this.calls = calls; }

    @Transactional
    public CallDtos.CallResponse create(UUID callerId, CallDtos.CreateCallRequest request) {
        if (callerId.equals(request.calleeId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot call yourself");
        }
        return CallDtos.CallResponse.from(calls.save(new CallRecord(callerId, request.calleeId())));
    }

    @Transactional
    public CallDtos.CallResponse join(UUID callId, UUID userId) {
        CallRecord call = accessibleCall(callId, userId);
        try {
            call.join(userId);
        } catch (IllegalStateException exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, exception.getMessage());
        }
        return CallDtos.CallResponse.from(call);
    }

    @Transactional
    public CallDtos.CallResponse end(UUID callId, UUID userId) {
        CallRecord call = accessibleCall(callId, userId);
        try {
            call.end(userId);
        } catch (IllegalStateException exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, exception.getMessage());
        }
        return CallDtos.CallResponse.from(call);
    }

    @Transactional
    public CallDtos.CallResponse reject(UUID callId, UUID userId) {
        CallRecord call = accessibleCall(callId, userId);
        try {
            call.reject(userId);
        } catch (IllegalStateException exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, exception.getMessage());
        }
        return CallDtos.CallResponse.from(call);
    }

    @Transactional(readOnly = true)
    public List<CallDtos.CallResponse> history(UUID userId) {
        return calls.findTop100ByCallerIdOrCalleeIdOrderByCreatedAtDesc(userId, userId).stream()
                .map(CallDtos.CallResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public CallDtos.CallResponse get(UUID callId, UUID userId) {
        return CallDtos.CallResponse.from(accessibleCall(callId, userId));
    }

    private CallRecord accessibleCall(UUID callId, UUID userId) {
        return calls.findByIdAndCallerIdOrIdAndCalleeId(callId, userId, callId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Call not found"));
    }
}
