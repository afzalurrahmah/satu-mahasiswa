package com.usu.satu.repository;

import com.usu.satu.model.Presence;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PresenceRepository extends MongoRepository<Presence,String> {
    String match = "{\n" +
            "    $match: {\n" +
            "        student: {\n" +
            "            $elemMatch: {\n" +
            "                nim: '?0',\n" +
            "                isPresent: true\n" +
            "            }\n" +
            "        },\n" +
            "        classId: '?1',\n" +
            "        isDeleted: false\n" +
            "    }\n" +
            "}";

    String group = "{\n" +
            "    $group: {\n" +
            "        _id: 'nim',\n" +
            "        count: {\n" +
            "            $sum: 1\n" +
            "        }\n" +
            "    }\n" +
            "}";

    String countCourse = "{\n" +
            "    $match: {\n" +
            "        classId: '?0',\n" +
            "        isDeleted: false\n" +
            "    }\n" +
            "}";

    String countCourseGroup = "{\n" +
            "    $group: {\n" +
            "        _id: 'classId',\n" +
            "        count: {\n" +
            "            $sum: 1\n" +
            "        }\n" +
            "    }\n" +
            "}";

    String arrCourse = "{\n" +
            "    $group: {\n" +
            "        _id: \"$classId\"\n" +
            "    }\n" +
            "}, {\n" +
            "    $group: {\n" +
            "        _id: \"class\",\n" +
            "        classId: {\n" +
            "            $push: '$_id'\n" +
            "        }\n" +
            "    }\n" +
            "}";

    @Aggregation(pipeline = {match, group})
    String countAttendance(String nim, String classId);

    @Aggregation(pipeline = {countCourse, countCourseGroup})
    String countMeeting(String classId);

    @Aggregation(pipeline = {arrCourse})
    List<String> courseList();

    Optional<Presence> findByIdAndIsDeletedFalse(String id);
    List<Presence> findAllByIsDeletedFalse();
    List<Presence> findAllByClassIdAndIsDeletedIsFalse(String classId);


}
