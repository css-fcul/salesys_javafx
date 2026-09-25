package pt.ul.fc.css.salesys.entities;

import jakarta.persistence.*;

enum CustomerDiscount{
    ELIGIBLE, NOT_ELIGIBLE
}

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String vatNumber;

    @Column(nullable = false)
    private String designation;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerDiscount discount = CustomerDiscount.NOT_ELIGIBLE;

    public Customer() {}

    public Customer(String vatNumber, String designation, String phoneNumber) {
        this.vatNumber = vatNumber;
        this.designation = designation;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public CustomerDiscount getDiscount() {
        return discount;
    }

    public void setDiscount(CustomerDiscount discount) {
        this.discount = discount;
    }


}
