package tech.buildrun.customerconnect.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tech.buildrun.customerconnect.controller.dto.CustomerDto;
import tech.buildrun.customerconnect.entity.CustomerEntity;
import tech.buildrun.customerconnect.repository.CustomerRepository;

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

    public Page<CustomerEntity> listAll(Integer page,String orderBy,Integer pageSize,String cpf, String email) {

        PageRequest pageRequest = getPageRequest(page,orderBy,pageSize);

        return findWithFilters(cpf,email,pageRequest);
    }

    public PageRequest getPageRequest(Integer page,String orderBy,Integer pageSize) {

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
