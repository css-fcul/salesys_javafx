package pt.ul.fc.css.salesys;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pt.ul.fc.css.salesys.dto.CustomerRequestDto;
import pt.ul.fc.css.salesys.services.CustomerService;
import pt.ul.fc.css.salesys.services.ProductService;
import pt.ul.fc.css.salesys.services.SaleService;

@SpringBootApplication
public class SaleSysApplication {
    public static void main(String[] args) {
        SpringApplication.run(SaleSysApplication.class, args);
    }

    @Bean
    CommandLineRunner initData(ProductService productService, 
                                CustomerService customerService,
                                SaleService saleService) {
        return args -> {

            productService.addProduct(1001, "Laptop Dell XPS 13", 1299.99, 15);
            productService.addProduct(1002, "Wireless Mouse Logitech", 29.99, 50);
            productService.addProduct(1003, "USB-C Cable 2m", 12.99, 100);
            productService.addProduct(1004, "Mechanical Keyboard", 89.99, 30);
            productService.addProduct(1005, "Monitor 27\" 4K", 449.99, 20);
            productService.addProduct(1006, "Webcam HD 1080p", 79.99, 25);
            productService.addProduct(1007, "Desk Lamp LED", 34.99, 40);


            customerService.createCustomer(new CustomerRequestDto("Tech Solutions Ltd", "123456789", "912345678"));
            customerService.createCustomer(new CustomerRequestDto("Digital Innovations", "987654321", "923456789"));
            customerService.createCustomer(new CustomerRequestDto("StartUp Hub", "555666777", "934567890"));
            customerService.createCustomer(new CustomerRequestDto("Corporate Systems", "111222333", "945678901"));


            saleService.createSale("912345678");
            saleService.addProductToSale(1L, 1001, 1);
            saleService.addProductToSale(1L, 1002, 2);
            saleService.closeSale(1L);

            saleService.createSale("945678901");
            saleService.addProductToSale(2L, 1005, 1);
            saleService.addProductToSale(2L, 1004, 1);
            saleService.addProductToSale(2L, 1003, 3);

            saleService.createSale("923456789");
            saleService.createSale("934567890");


        };
    }
}
