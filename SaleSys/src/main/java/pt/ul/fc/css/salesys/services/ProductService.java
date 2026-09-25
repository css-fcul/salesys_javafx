package pt.ul.fc.css.salesys.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.salesys.dto.ProductRequestDto;
import pt.ul.fc.css.salesys.entities.Product;
import pt.ul.fc.css.salesys.mapper.RestDtoMapper;
import pt.ul.fc.css.salesys.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RestDtoMapper restDtoMapper;

    public Optional<Product> getProductByCode(int prodCode) {
        return productRepository.findByCode(prodCode);
    }

    public void updateStock(int prodCode, int delta) {
        productRepository.updateStock(prodCode, delta);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAllSortedByCode();
    }

    public Product addProduct(ProductRequestDto productRequestDto) {
        isValidProduct(productRequestDto);

        return productRepository.save(restDtoMapper.mapRequestToProduct(productRequestDto));
    }

    // Overload used by Thymeleaf web flow to avoid DTO constructor assumptions
    public Product addProduct(int code, String description, double faceValue, int stockQuantity) {
        if (code <= 0) {
            throw new IllegalArgumentException("Product code must be positive");
        }
        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Product description cannot be null or empty");
        }
        if (faceValue <= 0) {
            throw new IllegalArgumentException("Product face value must be positive");
        }
        if (stockQuantity < 0) {
            throw new IllegalArgumentException("Product stock quantity cannot be negative");
        }
        if (productRepository.findByCode(code).isPresent()) {
            throw new IllegalArgumentException("Product with code already exists");
        }

        return productRepository.save(new Product(code, description, faceValue, stockQuantity));
    }

    private void isValidProduct(ProductRequestDto productRequestDto) {
        if (productRequestDto.code() <= 0) {
            throw new IllegalArgumentException("Product code must be positive");
        }
        if (productRequestDto.description() == null || productRequestDto.description().isEmpty()) {
            throw new IllegalArgumentException("Product description cannot be null or empty");
        }
        if (productRequestDto.faceValue() <= 0) {
            throw new IllegalArgumentException("Product face value must be positive");
        }
        if (productRequestDto.stockQuantity() < 0) {
            throw new IllegalArgumentException("Product stock quantity cannot be negative");
        }
        if (productRepository.findByCode(productRequestDto.code()).isPresent()) {
            throw new IllegalArgumentException("Product with code already exists");
        }
    }
}
