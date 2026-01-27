package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.profile;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.UUID;

/**
 * Request DTO for updating user profile information.
 *
 * <p>All fields are optional - only provided fields will be updated.
 */
@Schema(description = "Request to update user profile information")
public record UpdateProfileRequestDto(
    @Schema(
            description = "User's position/role ID",
            example = "550e8400-e29b-41d4-a716-446655440001")
        UUID positionId,
    @Schema(
            description = "User's work location ID",
            example = "550e8400-e29b-41d4-a716-446655440020")
        UUID locationId,
    @Schema(
            description = "User's availability status",
            example = "available",
            allowableValues = {"available", "partially_available", "unavailable"})
        @Pattern(
            regexp = "available|partially_available|unavailable",
            message = "{profile.availability.invalid}")
        String availability,
    @Schema(description = "Total years of professional experience", example = "5")
        @DecimalMin(value = "0.0", message = "Years of experience cannot be negative")
        @DecimalMax(value = "100.0", message = "Years of experience seems too high")
        Double yearsOfExperience,
    @Schema(
            description = "User's biography",
            example = "Experienced developer with passion for clean code.")
        @Size(max = 500, message = "Bio must not exceed 500 characters")
        String bio) {}
