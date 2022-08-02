package com.usu.satu.repository;

import com.usu.satu.model.StudyCard;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudyCardRepository extends MongoRepository<StudyCard, String> {
    Optional<StudyCard> findByIdAndIsDeletedIsFalse(String id);
    List<StudyCard> findAllByIsDeletedIsFalse();
//    @Query("{$and : [{'nim':  ?0}, {'periodTaken':  ?1} ]}")

    Optional<StudyCard> findStudyCardByNimAndPeriodTakenAndIsDeletedIsFalse(String nim, String periodTaken);
    Optional<StudyCard> findStudyCardByNimAndScheduleIdAndIsDeletedIsFalse(String nim, String scheduleId);

    String matchCapacity = "{\n" +
            "    $match: {\n" +
            "        $and: [{\n" +
            "                periodTaken: '?0'\n" +
            "            },\n" +
            "            {\n" +
            "                'studentCourses.classId': '?1'\n" +
            "            }\n" +
            "        ]\n" +
            "    }\n" +
            "}";

    String count = "{\n" +
            "    $count: 'count'\n" +
            "}";

    @Aggregation(pipeline = {matchCapacity, count})
    String countCapacity(String periodId, String classId);

    List<StudyCard> findStudyCardsByPeriodTakenAndIsDeletedIsFalse(String periodId);
    List<StudyCard> findStudyCardsByNimAndIsDeletedIsFalse(String nim);
    Optional<StudyCard> findStudyCardByPeriodTakenInAndNimAndIsDeletedIsFalse(String period, String nim);

}
