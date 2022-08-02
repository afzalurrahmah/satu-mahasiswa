package com.usu.satu.repository;

import com.usu.satu.model.StudentStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface StatusRepository extends MongoRepository<StudentStatus,String> {
    List<StudentStatus> findStudentStatusByNim(String nim);
    Optional<StudentStatus> findStudentStatusByNimAndAndPeriodId(String nim, String periodId);

}
