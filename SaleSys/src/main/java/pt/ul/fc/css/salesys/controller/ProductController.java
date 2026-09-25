package pt.ul.fc.css.salesys.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ul.fc.css.salesys.dto.ProductRequestDto;
import pt.ul.fc.css.salesys.dto.ProductResponseDto;
import pt.ul.fc.css.salesys.mapper.RestDtoMapper;
import pt.ul.fc.css.salesys.services.ProductService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/products")
@Api(value = "Product API", tags = "Products")
public class ProductController {

    @Autowired
    private ProductService productHandler;

    @Autowired
    private RestDtoMapper restDtoMapper;

    @GetMapping
    @Operation(summary = "Get all products", description = "Returns a list of all products.")
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        List<ProductResponseDto> products = restDtoMapper.mapToProductResponseDtos(
                productHandler.getAllProducts());
        return ResponseEntity.ok(products);
    }

    @PostMapping
    @Operation(summary = "Add Product", description = "Add a new product and returns the product DTO.")
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductRequestDto productRequestDto) {

        var product = productHandler.addProduct(productRequestDto);
        ProductResponseDto responseDto = restDtoMapper.mapToProductResponseDto(product);
        return ResponseEntity.ok(responseDto);

    }

}
