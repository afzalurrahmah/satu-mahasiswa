package com.usu.satu.repository;

import com.usu.satu.model.Billing;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillingRepository extends MongoRepository<Billing, String> {
    List<Billing> findAllByVirtualAccount(String va);
    Optional<Billing> findBillingByTrxId(String trxId);
    List<Billing> findBillingsByNim(String nim);
}
