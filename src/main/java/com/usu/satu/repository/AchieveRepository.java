package com.usu.satu.repository;

import com.usu.satu.model.Achievement;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AchieveRepository extends MongoRepository<Achievement,String> {
    List<Achievement> findAllByNimAndIsDeletedIsFalse(String nim);
    List<Achievement> findAllByIsDeletedIsFalse();
    Optional<Achievement> findByIdAndIsDeletedIsFalse(String id);
}
