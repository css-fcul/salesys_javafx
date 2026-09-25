package pt.ul.fc.di.css.javafxexample.api;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import pt.ul.fc.di.css.javafxexample.grpc.CustomerListResponse;
import pt.ul.fc.di.css.javafxexample.grpc.CustomerRequest;
import pt.ul.fc.di.css.javafxexample.grpc.CustomerResponse;
import pt.ul.fc.di.css.javafxexample.grpc.Empty;
import pt.ul.fc.di.css.javafxexample.grpc.SaleProductItem;
import pt.ul.fc.di.css.javafxexample.grpc.SaleProductListResponse;
import pt.ul.fc.di.css.javafxexample.grpc.SaleSysServiceGrpc;
import pt.ul.fc.di.css.javafxexample.grpc.VatRequest;
import pt.ul.fc.di.css.javafxexample.grpc.ProductResponse;

import java.util.List;

public class ApiClient {

    private static final String HOST = "localhost";
    private static final int PORT = 9090;

    private static final ManagedChannel channel = ManagedChannelBuilder
            .forAddress(HOST, PORT)
            .usePlaintext()
            .build();

    private static final SaleSysServiceGrpc.SaleSysServiceBlockingStub stub =
            SaleSysServiceGrpc.newBlockingStub(channel);

    public static void createCustomer(String vatNumber, String designation, String phone) {
        CustomerRequest request = CustomerRequest.newBuilder()
                .setVatNumber(vatNumber)
                .setDesignation(designation)
                .setPhone(phone)
                .build();
        stub.createCustomer(request);
    }

    public static List<CustomerResponse> getAllCustomers() {
        CustomerListResponse response = stub.getAllCustomers(Empty.newBuilder().build());
        return response.getCustomersList();
    }

    public static void createSale(String vat) {
        VatRequest request = VatRequest.newBuilder().setVat(vat).build();
        stub.createSale(request);
    }

    public static List<SaleProductItem> getProductByVat(String vat) {
        VatRequest request = VatRequest.newBuilder().setVat(vat).build();
        SaleProductListResponse response = stub.getProductsByVat(request);
        return response.getItemsList();
    }

    public static ProductResponse addProduct(int code, String description,
                                            double faceValue, int stockQuantity) {
        // complete aqui
        return null;
    }
}
