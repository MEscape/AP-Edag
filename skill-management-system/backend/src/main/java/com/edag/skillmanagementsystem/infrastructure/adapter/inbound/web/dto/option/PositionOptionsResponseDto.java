package com.edag.skillmanagementsystem.infrastructure.adapter.inbound.web.dto.option;

import com.edag.skillmanagementsystem.domain.model.option.Position;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/** Response DTO containing available position options. */
@Schema(description = "List of available position options")
public record PositionOptionsResponseDto(
    @Schema(description = "List of positions with ID and name") List<PositionOptionDto> positions) {

  /** DTO representing a single position option. */
  @Schema(description = "Position option with ID and name")
  public record PositionOptionDto(
      @Schema(description = "Position ID", example = "550e8400-e29b-41d4-a716-446655440020")
          String id,
      @Schema(description = "Position name", example = "Full Stack Developer") String name) {

    public static PositionOptionDto from(Position position) {
      return new PositionOptionDto(position.id().toString(), position.name());
    }
  }

  /**
   * Creates a PositionOptionsResponseDto from a list of domain Position objects.
   *
   * @param positions the list of position domain models
   * @return the response DTO
   */
  public static PositionOptionsResponseDto from(List<Position> positions) {
    List<PositionOptionDto> positionDtos =
        positions.stream()
            .map(PositionOptionDto::from)
            .sorted((a, b) -> a.name().compareToIgnoreCase(b.name()))
            .toList();
    return new PositionOptionsResponseDto(positionDtos);
  }
}
