package tech.buildrun.customerconnect.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.buildrun.customerconnect.Service.CustomerService;
import tech.buildrun.customerconnect.controller.dto.ApiResponse;
import tech.buildrun.customerconnect.controller.dto.CustomerDto;
import tech.buildrun.customerconnect.controller.dto.PaginationResponse;
import tech.buildrun.customerconnect.entity.CustomerEntity;

import java.net.URI;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<Void> createCustomer(@RequestBody CustomerDto dto) {

        CustomerEntity customer = customerService.createCustomer(dto);

        return ResponseEntity.created(URI.create("/customers/" + customer.getCustomerId())).build();
    }

    @GetMapping
    public ResponseEntity<ApiResponse<CustomerEntity>> findAll(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "orderBy", defaultValue = "desc") String orderBy,
            @RequestParam(name = "pageSize", defaultValue = "1") Integer pageSize,
            @RequestParam(name = "cpf",required = false) String cpf,
            @RequestParam(name = "email", required = false) String email
    ) {

        Page<CustomerEntity> customerPage = customerService.listAll(page, orderBy, pageSize, cpf, email);


        return ResponseEntity.ok(new ApiResponse<>(
                customerPage.getContent(),
                new PaginationResponse(
                        customerPage.getNumber(),
                        customerPage.getSize(),
                        customerPage.getTotalElements(),
                        customerPage.getTotalPages()
                )
        ));
    }
}
