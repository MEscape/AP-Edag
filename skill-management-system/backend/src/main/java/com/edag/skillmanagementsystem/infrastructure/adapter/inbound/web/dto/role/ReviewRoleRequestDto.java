package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for reviewing a role request (approve/reject).
 *
 * @since 1.0.0
 */
@Schema(description = "Request to review (approve or reject) a role request")
public record ReviewRoleRequestDto(
    @Schema(
            description = "Admin comment on the decision",
            example = "Approved due to team leadership responsibilities.")
        @Size(max = 1000, message = "{role.request.comment.size}")
        String adminComment) {}
