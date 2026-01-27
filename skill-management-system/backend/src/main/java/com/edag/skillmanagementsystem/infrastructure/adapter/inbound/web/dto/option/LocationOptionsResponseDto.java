package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.option;

import com.edag.skillmanagementsystem.domain.model.option.Location;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/** Response DTO containing available location options. */
@Schema(description = "List of available location options")
public record LocationOptionsResponseDto(
    @Schema(description = "List of locations with ID and name") List<LocationOptionDto> locations) {

  /** DTO representing a single location option. */
  @Schema(description = "Location option with ID and name")
  public record LocationOptionDto(
      @Schema(description = "Location ID", example = "550e8400-e29b-41d4-a716-446655440030")
          String id,
      @Schema(description = "Location name", example = "Fulda") String name) {

    public static LocationOptionDto from(Location location) {
      return new LocationOptionDto(location.id().toString(), location.name());
    }
  }

  /**
   * Creates a LocationOptionsResponseDto from a list of domain Location objects.
   *
   * @param locations the list of location domain models
   * @return the response DTO
   */
  public static LocationOptionsResponseDto from(List<Location> locations) {
    List<LocationOptionDto> locationDtos =
        locations.stream()
            .map(LocationOptionDto::from)
            .sorted((a, b) -> a.name().compareToIgnoreCase(b.name()))
            .toList();
    return new LocationOptionsResponseDto(locationDtos);
  }
}
