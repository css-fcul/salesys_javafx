package pt.ul.fc.css.salesys.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.salesys.dto.CustomerRequestDto;
import pt.ul.fc.css.salesys.entities.Customer;
import pt.ul.fc.css.salesys.mapper.RestDtoMapper;
import pt.ul.fc.css.salesys.repository.CustomerRepository;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RestDtoMapper restDtoMapper;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerByVat(String vat) {
        isValidVad(vat);
        return customerRepository.findByVatNumber(vat);
    }

    public Customer createCustomer(CustomerRequestDto customerRequestDto) {
        isValidCustomer(customerRequestDto);

        Customer customer = restDtoMapper.mapRequestToCustomer(customerRequestDto);

        return customerRepository.save(customer);
    }

    private void isValidVad(String vat) {
        if (vat == null || vat.isEmpty()) {
            throw new IllegalArgumentException("VAT number cannot be null or empty");
        }
        if (vat.length() != 9) {
            throw new IllegalArgumentException("VAT number must be 9 characters long. Input: " + vat);
        }
    }

    private void isValidCustomer(CustomerRequestDto customerRequestDto) {
        isValidVad(customerRequestDto.vatNumber());
        if (customerRepository.findByVatNumber(customerRequestDto.vatNumber()).isPresent()) {
            throw new IllegalArgumentException("Customer with VAT number already exists");
        }
        if (customerRequestDto.phone() != null && !customerRequestDto.phone().matches("\\d+")) {
            throw new IllegalArgumentException("Phone number must be numeric");
        }
        if (customerRequestDto.designation().length() > 50) {
            throw new IllegalArgumentException("Designation cannot be longer than 50 characters");
        }
    }
}
