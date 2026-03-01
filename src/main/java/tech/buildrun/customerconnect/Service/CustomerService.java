package tech.buildrun.customerconnect.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tech.buildrun.customerconnect.controller.dto.CustomerDto;
import tech.buildrun.customerconnect.controller.dto.CustomerUpdate;
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

    public CustomerEntity createCustomer(CustomerDto dto) {

        CustomerEntity customer = new CustomerEntity();
        customer.setFullName(dto.fullName());
        customer.setCpf(dto.cpf());
        customer.setEmail(dto.email());
        customer.setPhoneNumber(dto.phoneNumber());

        return customerRepository.save(customer);
    }

    public Optional<CustomerEntity> updateById(Long id,CustomerUpdate dto) {

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

    public Page<CustomerEntity> listAll(Integer page,String orderBy,Integer pageSize,String cpf, String email) {

        PageRequest pageRequest = getPageRequest(page,orderBy,pageSize);

        return findWithFilters(cpf,email,pageRequest);
    }

    private static void validateFields(CustomerUpdate dto, Optional<CustomerEntity> customer) {
        if(hasText(dto.fullName())) {
            customer.get().setFullName(dto.fullName());
        }

        if(hasText(dto.cpf())) {
            customer.get().setCpf(dto.cpf());
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

        if(hasText(cpf)) {
            return customerRepository.findByCpf(cpf,pageRequest);
        }
        if(hasText(email)) {
            return customerRepository.findByEmail(email,pageRequest);
        }
        return customerRepository.findAll(pageRequest);
    }
}
