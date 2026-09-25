package pt.ul.fc.css.salesys.entities;


import jakarta.persistence.*;


enum ProductDiscount {
    ELIGIBLE, NOT_ELIGIBLE
}

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private int code;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private double faceValue;

    @Column(nullable = false)
    private int stockQuantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductDiscount discount = ProductDiscount .NOT_ELIGIBLE;

    public Product() {}

    public Product(int code, String description, double faceValue, int stockQuantity) {
        this.code = code;
        this.description = description;
        this.faceValue = faceValue;
        this.stockQuantity = stockQuantity;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public double getFaceValue() {
        return faceValue;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFaceValue(double faceValue) {
        this.faceValue = faceValue;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductDiscount getDiscount() {
        return discount;
    }

    public void setDiscount(ProductDiscount discount) {
        this.discount = discount;
    }


}
