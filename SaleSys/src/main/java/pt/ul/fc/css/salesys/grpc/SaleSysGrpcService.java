package pt.ul.fc.css.salesys.grpc;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import pt.ul.fc.css.salesys.dto.CustomerRequestDto;
import pt.ul.fc.css.salesys.entities.Customer;
import pt.ul.fc.css.salesys.entities.Sale;
import pt.ul.fc.css.salesys.entities.SaleProduct;
import pt.ul.fc.css.salesys.exceptions.ResourceNotFoundException;
import pt.ul.fc.css.salesys.mapper.GrpcMapper;
import pt.ul.fc.css.salesys.services.CustomerService;
import pt.ul.fc.css.salesys.services.SaleService;

import java.util.List;

@GrpcService
public class SaleSysGrpcService extends SaleSysServiceGrpc.SaleSysServiceImplBase {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SaleService saleService;

    @Autowired
    private GrpcMapper grpcMapper;

    // ─── CreateCustomer ───────────────────────────────────────────────────────

    @Override
    public void createCustomer(CustomerRequest request,
                               StreamObserver<CustomerResponse> responseObserver) {
        try {
            // Map gRPC request to domain entity
            Customer customer = grpcMapper.mapRequestToCustomer(request);
            
            // Create DTO from entity for service
            CustomerRequestDto dto = new CustomerRequestDto(
                    customer.getDesignation(),
                    customer.getPhoneNumber(),
                    customer.getVatNumber());

            Customer saved = customerService.createCustomer(dto);

            // Map entity back to gRPC response
            CustomerResponse response = grpcMapper.mapCustomerToResponse(saved);

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (IllegalArgumentException e) {
            responseObserver.onError(
                    Status.INVALID_ARGUMENT.withDescription(e.getMessage()).asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(
                    Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    // ─── GetAllCustomers ──────────────────────────────────────────────────────

    @Override
    public void getAllCustomers(Empty request,
                                StreamObserver<CustomerListResponse> responseObserver) {
        try {
            List<Customer> customers = customerService.getAllCustomers();

            CustomerListResponse.Builder builder = CustomerListResponse.newBuilder();
            for (Customer c : customers) {
                CustomerResponse response = grpcMapper.mapCustomerToResponse(c);
                builder.addCustomers(response);
            }

            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            responseObserver.onError(
                    Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    // ─── GetProductsByVat ─────────────────────────────────────────────────────

    @Override
    public void getProductsByVat(VatRequest request,
                                 StreamObserver<SaleProductListResponse> responseObserver) {
        try {
            List<SaleProduct> products =
                    saleService.getSaleProductByVat(request.getVat());

            SaleProductListResponse.Builder builder = SaleProductListResponse.newBuilder();
            for (SaleProduct sp : products) {
                SaleProductItem item = grpcMapper.mapSaleProductToItem(sp);
                builder.addItems(item);
            }

            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();

        } catch (ResourceNotFoundException e) {
            responseObserver.onError(
                    Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
        } catch (IllegalArgumentException e) {
            responseObserver.onError(
                    Status.INVALID_ARGUMENT.withDescription(e.getMessage()).asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(
                    Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    // ─── CreateSale ───────────────────────────────────────────────────────────

    @Override
    public void createSale(VatRequest request,
                           StreamObserver<SaleResponse> responseObserver) {
        try {
            Sale sale = saleService.createSale(request.getVat());

            // Map entity to gRPC response
            SaleResponse response = grpcMapper.mapSaleToResponse(sale);

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (ResourceNotFoundException e) {
            responseObserver.onError(
                    Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
        } catch (IllegalArgumentException e) {
            responseObserver.onError(
                    Status.INVALID_ARGUMENT.withDescription(e.getMessage()).asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(
                    Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void addProduct(ProductRequest request,
                        StreamObserver<ProductResponse> responseObserver) {
        // complete aqui
    }
}
