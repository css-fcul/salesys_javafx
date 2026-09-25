package pt.ul.fc.css.salesys.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ul.fc.css.salesys.dto.CustomerRequestDto;
import pt.ul.fc.css.salesys.dto.CustomerResponseDto;
import pt.ul.fc.css.salesys.exceptions.ResourceNotFoundException;
import pt.ul.fc.css.salesys.mapper.RestDtoMapper;
import pt.ul.fc.css.salesys.services.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/customers")
@Api(value = "Customer API", tags = "Customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private RestDtoMapper restDtoMapper;

    @GetMapping
    @Operation(summary = "Get all customers", description = "Returns a list of all customers.")
    public ResponseEntity<List<CustomerResponseDto>> getAllCustomers() {
        List<CustomerResponseDto> customers = restDtoMapper.mapToCustomerResponseDtos(
                customerService.getAllCustomers());
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/by-vat/{vat}")
    @Operation(summary = "Get customer by VAT", description = "Returns a customer given its VAT number.")
    public ResponseEntity<CustomerResponseDto> getCustomerByVat(@PathVariable("vat") String vat) {
        CustomerResponseDto customer = customerService.getCustomerByVat(vat)
                .map(restDtoMapper::mapToCustomerResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with VAT number " + vat + " not found"));
        return ResponseEntity.ok(customer);
    }

    @PostMapping
    @Operation(summary = "Create customer", description = "Creates a new customer and returns the created customer DTO.")
    public ResponseEntity<CustomerResponseDto> createCustomer(@RequestBody CustomerRequestDto customerRequestDto) {

        var customer = customerService.createCustomer(customerRequestDto);
        CustomerResponseDto responseDto = restDtoMapper.mapToCustomerResponseDto(customer);
        return ResponseEntity.ok(responseDto);
    }
}
