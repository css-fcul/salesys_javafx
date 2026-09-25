package pt.ul.fc.css.salesys.dto;

import java.time.LocalDateTime;

public record ApiErrorResponseDto(
    LocalDateTime timestamp,
    int status,
    String message
) {
}