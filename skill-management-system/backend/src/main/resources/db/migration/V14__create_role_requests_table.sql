-- V14__create_role_requests_table.sql
-- Creates role_requests table for managing user role change requests
-- Supports workflow where users request role changes and admins approve/reject them

CREATE TABLE role_requests (
                               id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                               user_id UUID NOT NULL,
                               requested_role VARCHAR(50) NOT NULL,
                               status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
                               reason TEXT,
                               reviewed_by UUID,
                               reviewed_at TIMESTAMP,
                               admin_comment TEXT,
                               created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               CONSTRAINT fk_role_request_user FOREIGN KEY (user_id)
                                   REFERENCES users(id) ON DELETE CASCADE,
                               CONSTRAINT fk_role_request_reviewer FOREIGN KEY (reviewed_by)
                                   REFERENCES users(id) ON DELETE SET NULL,
                               CONSTRAINT chk_reason_length CHECK (LENGTH(reason) <= 1000),
                               CONSTRAINT chk_admin_comment_length CHECK (LENGTH(admin_comment) <= 1000),
                               CONSTRAINT chk_requested_role CHECK (requested_role IN ('USER', 'MANAGER', 'ADMIN')),
                               CONSTRAINT chk_status CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED'))
);

CREATE INDEX idx_role_requests_user_status ON role_requests(user_id, status);
CREATE INDEX idx_role_requests_status ON role_requests(status);
CREATE INDEX idx_role_requests_created_at ON role_requests(created_at DESC);

COMMENT ON TABLE role_requests IS 'User requests for role changes with admin review workflow';
COMMENT ON COLUMN role_requests.user_id IS 'User who submitted the role change request';
COMMENT ON COLUMN role_requests.requested_role IS 'Desired role: USER, MANAGER, or ADMIN';
COMMENT ON COLUMN role_requests.status IS 'Request status: PENDING, APPROVED or REJECTED';
COMMENT ON COLUMN role_requests.reason IS 'User justification for the role change request';
COMMENT ON COLUMN role_requests.reviewed_by IS 'Admin who reviewed the request';
COMMENT ON COLUMN role_requests.reviewed_at IS 'Timestamp when the request was reviewed';
COMMENT ON COLUMN role_requests.admin_comment IS 'Admin feedback or reason for decision';
