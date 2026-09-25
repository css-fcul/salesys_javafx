package pt.ul.fc.css.salesys.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pt.ul.fc.css.salesys.dto.CustomerRequestDto;
import pt.ul.fc.css.salesys.dto.CustomerResponseDto;
import pt.ul.fc.css.salesys.dto.ProductRequestDto;
import pt.ul.fc.css.salesys.dto.ProductResponseDto;
import pt.ul.fc.css.salesys.dto.SaleProductResponseDto;
import pt.ul.fc.css.salesys.dto.SaleResponseDto;
import pt.ul.fc.css.salesys.entities.Customer;
import pt.ul.fc.css.salesys.entities.Product;
import pt.ul.fc.css.salesys.entities.Sale;
import pt.ul.fc.css.salesys.entities.SaleProduct;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestDtoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "discount", ignore = true)
    @Mapping(target = "phoneNumber", source = "phone")
    Customer mapRequestToCustomer(CustomerRequestDto dto);

    @Mapping(target = "phone", source = "phoneNumber")
    CustomerResponseDto mapToCustomerResponseDto(Customer customer);

    List<CustomerResponseDto> mapToCustomerResponseDtos(List<Customer> customers);

    @Mapping(target = "stockQuantity", source = "stockQuantity")
    Product mapRequestToProduct(ProductRequestDto dto);

    @Mapping(target = "stockQuantity", source = "stockQuantity")
    ProductResponseDto mapToProductResponseDto(Product product);

    List<ProductResponseDto> mapToProductResponseDtos(List<Product> products);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "date", source = "date")
    @Mapping(target = "totalAmount", source = "total")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "customer", source = "customer")
    @Mapping(target = "saleProducts", source = "saleProducts")
    SaleResponseDto mapToSaleResponseDto(Sale sale);

    List<SaleResponseDto> mapToSaleResponseDtos(List<Sale> sales);

    @Mapping(target = "productCode", source = "product.code")
    @Mapping(target = "quantity", source = "quantity")
    SaleProductResponseDto mapToSaleProductResponseDto(SaleProduct saleProduct);

    List<SaleProductResponseDto> mapToSaleProductResponseDtos(List<SaleProduct> saleProducts);
}
