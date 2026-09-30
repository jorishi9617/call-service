CREATE TABLE calls (
    id UUID PRIMARY KEY,
    caller_id UUID NOT NULL,
    callee_id UUID NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('RINGING', 'ACTIVE', 'ENDED', 'REJECTED', 'MISSED')),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    answered_at TIMESTAMPTZ,
    ended_at TIMESTAMPTZ,
    version BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT calls_distinct_participants CHECK (caller_id <> callee_id)
);

CREATE INDEX idx_calls_caller_created ON calls (caller_id, created_at DESC);
CREATE INDEX idx_calls_callee_created ON calls (callee_id, created_at DESC);
CREATE INDEX idx_calls_status_created ON calls (status, created_at);
