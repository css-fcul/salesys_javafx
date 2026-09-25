package pt.ul.fc.css.salesys.dto;

import java.time.LocalDateTime;
import java.util.List;
import pt.ul.fc.css.salesys.entities.Sale.SaleStatus;

public record SaleResponseDto(
    Long id,
    LocalDateTime date,
    double totalAmount,
    SaleStatus status,
    CustomerResponseDto customer,
    List<SaleProductResponseDto> saleProducts
) {
}