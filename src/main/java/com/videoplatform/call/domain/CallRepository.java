package com.videoplatform.call.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CallRepository extends JpaRepository<CallRecord, UUID> {
    List<CallRecord> findTop100ByCallerIdOrCalleeIdOrderByCreatedAtDesc(UUID callerId, UUID calleeId);
    Optional<CallRecord> findByIdAndCallerIdOrIdAndCalleeId(UUID id1, UUID callerId, UUID id2, UUID calleeId);
}
