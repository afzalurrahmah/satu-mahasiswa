package com.usu.satu.repository;

import com.usu.satu.model.Student;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends MongoRepository<Student,String> {
    Optional<Student> findByNimAndIsDeletedIsFalse(String nim);
    Optional<Student> findByIdAndIsDeletedIsFalse(String id);
    List<Student> findAllByIsDeletedIsFalse();
    List<Student> findAllByIsDeletedIsFalseAndMajorCode(String major);

    String matchById = "{\n" +
                        "    $match: {\n" +
                        "        _id: ObjectId('?0')\n" +
                        "    }\n" +
                        "}";

    String matchByNim = "{\n" +
                        "    $match: {\n" +
                        "        nim: '?0'\n" +
                        "    }\n" +
                        "}";

    String lookup = "{\n" +
            "    $lookup: {\n" +
            "        from: 'studentStatus',\n" +
            "        localField: 'status_id',\n" +
            "        foreignField: '_id',\n" +
            "        as: 'student_status'\n" +
            "    }\n" +
            "}";

    String unwind = "{\n" +
            "    $unwind: {\n" +
            "        path: '$student_status',\n" +
            "        preserveNullAndEmptyArrays: true\n" +
            "    }\n" +
            "}";

    String matchNimStudyCard = "{\n" +
            "    $match: {\n" +
            "        nim: '?0',\n" +
            "        is_deleted: false\n" +
            "    }\n" +
            "}";

    String lookupStudyCard = "{\n" +
            "    $lookup: {\n" +
            "        from: 'studyCards',\n" +
            "        localField: 'nim',\n" +
            "        foreignField: 'nim',\n" +
            "        as: 'study_card'\n" +
            "    }\n" +
            "}";

    String unwindStudyCard = "{\n" +
            "    $unwind: {\n" +
            "        path: '$study_card'\n" +
            "    }\n" +
            "}";

    String matchStudyCard = "{\n" +
            "    $match: {\n" +
            "        'study_card.period_taken': '?1',\n" +
            "        'study_card.schedule_id': '?2'\n" +
            "    }\n" +
            "}";

    @Aggregation(pipeline = {matchById, lookup, unwind})
    Student getMatchStatusId(String id);

    @Aggregation(pipeline = {matchByNim, lookup, unwind})
    Student getMatchStatusNim(String nim);

    @Aggregation(pipeline = {matchNimStudyCard, lookupStudyCard, unwindStudyCard, matchStudyCard})
    Student getMatchStudyCard(String nim, String periodId, String scheduleId);
}
