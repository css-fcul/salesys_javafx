package pt.ul.fc.css.salesys.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pt.ul.fc.css.salesys.entities.Customer;
import pt.ul.fc.css.salesys.entities.Sale;
import pt.ul.fc.css.salesys.entities.SaleProduct;
import pt.ul.fc.css.salesys.grpc.CustomerRequest;
import pt.ul.fc.css.salesys.grpc.CustomerResponse;
import pt.ul.fc.css.salesys.grpc.SaleProductItem;
import pt.ul.fc.css.salesys.grpc.SaleResponse;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GrpcMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "discount", ignore = true)
    @Mapping(target = "phoneNumber", source = "phone")
    Customer mapRequestToCustomer(CustomerRequest request);

    @Mapping(target = "phone", source = "phoneNumber")
    CustomerResponse mapCustomerToResponse(Customer customer);

    @Mapping(target = "productCode", source = "product.code")
    @Mapping(target = "quantity", source = "quantity")
    SaleProductItem mapSaleProductToItem(SaleProduct saleProduct);

    default SaleResponse mapSaleToResponse(Sale sale) {
        if (sale == null) {
            return null;
        }

        return SaleResponse.newBuilder()
                .setId(sale.getId())
                .setDate(sale.getDate().toString())
                .setTotalAmount(sale.getTotal())
                .setStatus(sale.getStatus().name())
                .build();
    }


    // Product ??
}
