package pt.ul.fc.css.salesys.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import pt.ul.fc.css.salesys.dto.SaleCreateRequestDto;
import pt.ul.fc.css.salesys.dto.SaleProductRequestDto;
import pt.ul.fc.css.salesys.dto.SaleProductResponseDto;
import pt.ul.fc.css.salesys.dto.SaleResponseDto;
import pt.ul.fc.css.salesys.dto.SaleStatusUpdateRequestDto;
import pt.ul.fc.css.salesys.exceptions.ResourceNotFoundException;
import pt.ul.fc.css.salesys.mapper.RestDtoMapper;
import pt.ul.fc.css.salesys.services.SaleService;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @Autowired
    private RestDtoMapper restDtoMapper;

    @GetMapping
    public ResponseEntity<List<SaleResponseDto>> getAllSales() {
        List<SaleResponseDto> sales = restDtoMapper.mapToSaleResponseDtos(
                saleService.getAllSales());
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/{saleId}")
    public ResponseEntity<SaleResponseDto> getSaleById(@PathVariable("saleId") long saleId) {
        SaleResponseDto saleDto = saleService.getSaleById(saleId)
                .map(restDtoMapper::mapToSaleResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found"));
        return ResponseEntity.ok(saleDto);
    }

    @GetMapping("/products/{vat}")
    public ResponseEntity<List<SaleProductResponseDto>> getSaleProductsByVat(@PathVariable("vat") String vat) {
        List<SaleProductResponseDto> products = restDtoMapper.mapToSaleProductResponseDtos(
                saleService.getSaleProductByVat(vat));
        return ResponseEntity.ok(products);
    }

    @PostMapping("/create")
    @Operation(summary = "Create sale", description = "Creates a new sale for the provided customer's VAT.")
    public ResponseEntity<SaleResponseDto> createSale(@RequestBody SaleCreateRequestDto saleCreateRequestDto) {
        if (saleCreateRequestDto == null || saleCreateRequestDto.vat() == null || saleCreateRequestDto.vat().isBlank()) {
            throw new IllegalArgumentException("VAT is required");
        }

        var sale = saleService.createSale(saleCreateRequestDto.vat());
        SaleResponseDto saleDto = restDtoMapper.mapToSaleResponseDto(sale);
        return ResponseEntity.ok(saleDto);
    }

    @DeleteMapping("/{saleId}")
    @Operation(summary = "Delete sale", description = "Deletes a sale by its ID.")
    public ResponseEntity<Void> deleteSale(@PathVariable("saleId") long saleId) {
        saleService.deleteSale(saleId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/close/{saleId}")
    @Operation(summary = "Close sale", description = "Closes a sale")
    public ResponseEntity<SaleResponseDto> closeSale(@PathVariable("saleId") long saleId) {
        var sale = saleService.closeSale(saleId);
        return ResponseEntity.ok(restDtoMapper.mapToSaleResponseDto(sale));
    }

    @PostMapping("/{saleId}/product")
    @Operation(summary = "Add product to sale", description = "Adds a product.")
    public ResponseEntity<SaleResponseDto> addProductToSale(
            @PathVariable("saleId") long saleId,
            @RequestBody SaleProductRequestDto saleProductRequestDto) {
        if (saleProductRequestDto == null) {
            throw new IllegalArgumentException("Product data is required");
        }

        var sale = saleService.addProductToSale(
                saleId,
                saleProductRequestDto.productCode(),
                saleProductRequestDto.quantity());
        SaleResponseDto saleDto = restDtoMapper.mapToSaleResponseDto(sale);
        return ResponseEntity.ok(saleDto);
    }

    @PatchMapping("/{saleId}/status")
    @Operation(summary = "Update sale status", description = "Update to CLOSED or OPEN")
    public ResponseEntity<SaleResponseDto> updateSaleStatus(
            @PathVariable("saleId") long saleId,
            @RequestBody SaleStatusUpdateRequestDto saleStatusUpdateRequestDto) {
        if (saleStatusUpdateRequestDto == null || saleStatusUpdateRequestDto.status() == null) {
            throw new IllegalArgumentException("Status is required");
        }

        String status = saleStatusUpdateRequestDto.status();
        var sale = switch (status.toLowerCase()) {
            case "closed" -> saleService.updateStatus(saleId, false);
            case "open" -> saleService.updateStatus(saleId, true);
            default -> throw new IllegalArgumentException("Status must be OPEN or CLOSED");
        };

        SaleResponseDto saleDto = restDtoMapper.mapToSaleResponseDto(sale);
        return ResponseEntity.ok(saleDto);
    }
}
