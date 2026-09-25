package pt.ul.fc.css.salesys.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Sale {

    public enum SaleStatus {
        OPEN, CLOSED
    }

    enum SaleDiscount {
        NO_DISCOUNT(0.0, 0.0),
        ELIGIBLE_PRODUCTS(0.10, 0.0), // 10% discount, no threshold needed
        THRESHOLD_PERCENTAGE(0.15, 100.0); // 15% discount if total exceeds 100

        private final double percentage;
        private final double threshold;

        SaleDiscount(double percentage, double threshold) {
            this.percentage = percentage;
            this.threshold = threshold;
        }

        public double getPercentage() {
            return percentage;
        }

        public double getThreshold() {
            return threshold;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;

    private SaleStatus status;

    private double total;

    private double discountTotal;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<SaleProduct> getSaleProducts() {
        return items;
    }

    public void setSaleProducts(List<SaleProduct> items) {
        this.items = items;
    }

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SaleProduct> items = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private SaleDiscount discount = SaleDiscount.NO_DISCOUNT;

    public Sale() {
        this.date = LocalDateTime.now();
        this.status = SaleStatus.OPEN;
    }

    public Sale(Customer customer) {
        this();
        this.customer = customer;
    }

    public void addItem(Product product, int quantity) {
        for (SaleProduct item : items) {
            if (item.getProduct().getId() == product.getId()) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        for (SaleProduct item : items) {
            if (item.getProduct().getCode() == product.getCode()) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        items.add(new SaleProduct(this, product, quantity));
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public boolean isOpen() {
        return status == SaleStatus.OPEN;
    }

    public void setStatus(SaleStatus status) {
        this.status = status;
    }

    public SaleStatus getStatus() {
        return status;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getDiscountTotal() {
        return discountTotal;
    }

    public void setDiscountTotal(double discountTotal) {
        this.discountTotal = discountTotal;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SaleDiscount getDiscount() {
        return discount;
    }

    public void setDiscount(SaleDiscount discount) {
        this.discount = discount;
    }
}
