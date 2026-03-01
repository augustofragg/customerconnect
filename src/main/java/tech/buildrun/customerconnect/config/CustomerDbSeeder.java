package tech.buildrun.customerconnect.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import tech.buildrun.customerconnect.entity.CustomerEntity;
import tech.buildrun.customerconnect.repository.CustomerRepository;

import java.util.List;

@Profile("dev")
@Configuration
public class CustomerDbSeeder {

    @Bean
    CommandLineRunner initDataBase(CustomerRepository customerRepository) {
        return args -> {

            if (customerRepository.count() > 0) return;

            List<CustomerEntity> customers = List.of(
                    createCustomer("Augusto Silva", "11111111111", "augusto@gmail.com", "11999999901"),
                    createCustomer("Maria Souza", "22222222222", "maria@gmail.com", "11999999902"),
                    createCustomer("João Pereira", "33333333333", "joao@gmail.com", "11999999903"),
                    createCustomer("Ana Oliveira", "44444444444", "ana@gmail.com", "11999999904"),
                    createCustomer("Carlos Lima", "55555555555", "carlos@gmail.com", "11999999905")
            );
            customerRepository.saveAll(customers);
        };
    }

    private CustomerEntity createCustomer(String fullName, String cpf, String email, String phoneNumber) {

        CustomerEntity createdCustomer = new CustomerEntity();
        createdCustomer.setFullName(fullName);
        createdCustomer.setCpf(cpf);
        createdCustomer.setEmail(email);
        createdCustomer.setPhoneNumber(phoneNumber);

        return createdCustomer;
    }
}
