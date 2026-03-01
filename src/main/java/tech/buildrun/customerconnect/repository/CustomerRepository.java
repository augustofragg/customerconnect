package tech.buildrun.customerconnect.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import tech.buildrun.customerconnect.entity.CustomerEntity;

public interface CustomerRepository extends JpaRepository<CustomerEntity,Long> {

    Page<CustomerEntity> findByCpf(String cpf, PageRequest pageRequest);

    Page<CustomerEntity> findByEmail(String email, PageRequest pageRequest);
}