package ar.com.healthyapple.crm_web.repository.Client;

import ar.com.healthyapple.crm_web.model.Client.Client;
import ar.com.healthyapple.crm_web.model.Product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>, QuerydslPredicateExecutor<Client>, ClientCustomRepository {

    @Query("SELECT client FROM Client client WHERE LOWER(client.firstName) LIKE  %:firstNameOrLastName% OR  LOWER(client.lastName) LIKE %:firstNameOrLastName% ORDER BY client.firstName, client.lastName")
    Page<Client> findByFirstNameContainingAndLastNameContaining(@Param("firstNameOrLastName") String firstNameOrLastName, Pageable pageable);

    Optional<Client> findByMobile(Long mobile);

}
