package pt.ul.fc.css.salesys.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.ul.fc.css.salesys.entities.Customer;
import pt.ul.fc.css.salesys.entities.Product;
import pt.ul.fc.css.salesys.entities.Sale;
import pt.ul.fc.css.salesys.entities.Sale.SaleStatus;
import pt.ul.fc.css.salesys.entities.SaleProduct;
import pt.ul.fc.css.salesys.exceptions.ResourceNotFoundException;
import pt.ul.fc.css.salesys.repository.SaleRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private CustomerService customerService;

    @Transactional
    public Optional<Sale> getSaleById(long saleId) {
        return saleRepository.findById(saleId);
    }

    @Transactional
    public List<SaleProduct> getSaleProductByVat(String vat) {
        Optional<Customer> customer = customerService.getCustomerByVat(vat);
        if (customer.isEmpty()) {
            throw new ResourceNotFoundException("Customer with VAT number " + vat + " not found");
        }

        Optional<Sale> saleOptional = saleRepository.findByCustomerId(customer.get().getId());
        if (saleOptional.isEmpty()) {
            throw new ResourceNotFoundException("Sale not found for customer with VAT number " + vat);
        }
        Sale sale = saleOptional.get();

        return new java.util.ArrayList<>(sale.getSaleProducts());
    }

    @Transactional
    public Sale createSale(String vat) {

        Customer customer = isValidCustomer(vat);

        Sale sale = new Sale();
        sale.setDate(LocalDateTime.now());
        sale.setStatus(SaleStatus.OPEN);
        sale.setTotal(0.0);
        sale.setDiscountTotal(0.0);
        sale.setCustomer(customer);

        return saleRepository.save(sale);
    }

    private Customer isValidCustomer(String vat) {
        Customer customer = customerService.getCustomerByVat(vat)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with VAT number " + vat + " not found"));

        boolean haveOpenSale = saleRepository.openSaleByCustomer(customer.getId(), SaleStatus.OPEN);
        if (haveOpenSale) {
            throw new IllegalArgumentException("Customer '" + customer.getDesignation() + "' already has an open sale");
        }

        return customer;
    }

    @Transactional
    public Sale closeSale(long saleId) {
        Optional<Sale> saleOptional = saleRepository.findById(saleId);
        if (saleOptional.isEmpty()) {
            throw new ResourceNotFoundException("Sale does not exist");
        }
        Sale sale = saleOptional.get();
        sale.setStatus(SaleStatus.CLOSED);
        return saleRepository.save(sale);
    }

    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

    @Transactional
    public Sale addProductToSale(long saleId, int prodCode, int quantity) {

        Optional<Sale> saleOptional = saleRepository.findById(saleId);
        if (saleOptional.isEmpty()) {
            throw new ResourceNotFoundException("Sale does not exist");
        }
        Sale sale = saleOptional.get();

        if(sale.getStatus() != SaleStatus.OPEN) {
            throw new IllegalArgumentException("Cannot add products to a closed sale");
        }

        Optional<Product> productOptional = productService.getProductByCode(prodCode);
        if (productOptional.isEmpty()) {
            throw new ResourceNotFoundException("Product code does not exist");
        }

        Product prod = productOptional.get();

        if (prod.getStockQuantity() < quantity) {
            throw new IllegalArgumentException("Not enough products in stock");
        }

        sale.addItem(prod, quantity);
        double newTotal = sale.getTotal() + prod.getFaceValue() * quantity;
        sale.setTotal(newTotal);

        productService.updateStock(prodCode, -quantity);
        return saleRepository.save(sale);
    }

    public void deleteSale(long saleId) {
        Optional<Sale> saleOptional = saleRepository.findById(saleId);
        if (saleOptional.isPresent()) {
            Sale sale = saleOptional.get();
            if (sale.isOpen()) {
                throw new IllegalArgumentException("Cannot delete an open sale");
            }
            saleRepository.delete(sale);
        } else {
            throw new ResourceNotFoundException("Sale does not exist");
        }
    }

    public Sale updateStatus(long saleId, boolean b) {
        Optional<Sale> saleOptional = saleRepository.findById(saleId);
        if (saleOptional.isPresent()) {
            Sale sale = saleOptional.get();
            // vamos assumir que o status só pode ser OPEN ou CLOSED por agora
            sale.setStatus(b ? SaleStatus.OPEN : Sale.SaleStatus.CLOSED);
            return saleRepository.save(sale);
        } else {
            throw new ResourceNotFoundException("Sale does not exist");
        }
    }
}
