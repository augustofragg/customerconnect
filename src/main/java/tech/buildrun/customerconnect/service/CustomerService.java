package tech.buildrun.customerconnect.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tech.buildrun.customerconnect.controller.dto.CustomerDto;
import tech.buildrun.customerconnect.controller.dto.CustomerUpdateDto;
import tech.buildrun.customerconnect.entity.CustomerEntity;
import tech.buildrun.customerconnect.repository.CustomerRepository;

import java.util.Optional;

import static org.springframework.util.StringUtils.*;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Page<CustomerEntity> findAll(Integer page, String orderBy, Integer pageSize, String cpf, String email) {

        PageRequest pageRequest = getPageRequest(page,orderBy,pageSize);

        return findWithFilters(cpf,email,pageRequest);
    }

    public CustomerEntity createCustomer(CustomerDto dto) {

        CustomerEntity customer = new CustomerEntity();
        customer.setFullName(dto.fullName());
        customer.setCpf(dto.cpf());
        customer.setEmail(dto.email());
        customer.setPhoneNumber(dto.phoneNumber());

        return customerRepository.save(customer);
    }

    public Optional<CustomerEntity> findById(Long customerId) {

        Optional<CustomerEntity> customer = customerRepository.findById(customerId);

        return customer;
    }


    public Optional<CustomerEntity> updateById(Long id, CustomerUpdateDto dto) {

        Optional<CustomerEntity> customer = customerRepository.findById(id);

        if(customer.isPresent()) {
            validateFields(dto, customer);
            customerRepository.save(customer.get());
        }
        return customer;
    }

    public boolean deleteById(Long id) {
        boolean exist = customerRepository.existsById(id);

        if(exist) {
            customerRepository.deleteById(id);
        }
        return exist;
    }


    private  void validateFields(CustomerUpdateDto dto, Optional<CustomerEntity> customer) {
        if(hasText(dto.fullName())) {
            customer.get().setFullName(dto.fullName());
        }

        if(hasText(dto.phoneNumber())) {
            customer.get().setPhoneNumber(dto.phoneNumber());
        }

        if(hasText(dto.email())) {
            customer.get().setEmail(dto.email());
        }
    }

    private PageRequest getPageRequest(Integer page,String orderBy,Integer pageSize) {

        Sort.Direction direction = Sort.Direction.DESC;

        if(orderBy.equalsIgnoreCase("asc")) {
            direction = Sort.Direction.ASC;
        }

        return PageRequest.of(page,pageSize,direction,"createdAt");
    }

    private Page<CustomerEntity> findWithFilters(String cpf, String email, PageRequest pageRequest) {

        if(hasText(cpf) && hasText(email)) {
            return customerRepository.findByCpfAndEmail(cpf,email,pageRequest);
        }

        if(hasText(cpf)) {
            return customerRepository.findByCpf(cpf,pageRequest);
        }
        if(hasText(email)) {
            return customerRepository.findByEmail(email,pageRequest);
        }
        return customerRepository.findAll(pageRequest);
    }


}
