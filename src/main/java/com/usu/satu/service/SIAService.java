package com.usu.satu.service;

import com.usu.satu.dto.StudentCourse;
import com.usu.satu.helper.FormatData;
import com.usu.satu.helper.SIAGenerate;
import com.usu.satu.helper.SiaStatus;
import com.usu.satu.model.Student;
import com.usu.satu.model.StudentStatus;
import com.usu.satu.model.StudyCard;
import com.usu.satu.repository.StatusRepository;
import com.usu.satu.repository.StudentRepository;
import com.usu.satu.repository.StudyCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class SIAService {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    StudyCardRepository studyCardRepository;

    @Autowired
    MongoOperations mongoOperations;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    FormatData formatData;

    @Autowired
    StudentService studentService;

    @Autowired
    SiaStatus siaStatus;

    @Autowired
    SIAGenerate siaGenerate;

    @Autowired
    StatusRepository statusRepository;

    @Autowired
    StatusService statusService;

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    public List<HashMap<String, String>> getFilterStudent(Map<String, Object> params) {
        List<HashMap<String,String>> data = new ArrayList<>();

        Query qry = new Query();
        List<Criteria> criteria = new ArrayList<>();
        List<Student> studentList = new ArrayList<>();

        params.forEach((param, value) -> {
            if(param.equalsIgnoreCase("period")){
                criteria.add(Criteria.where("entry_year").is(value.toString()));
            }
            else if(param.equalsIgnoreCase("major")){
                criteria.add(Criteria.where("major_code").is(value));
            }
            else if(param.equalsIgnoreCase("name")){
                criteria.add(Criteria.where("name").regex(value.toString(), "i"));
            }
            else {
                criteria.add(Criteria.where(param).regex(value.toString(), "i"));
            }
        });
        criteria.add(new Criteria().andOperator(Criteria.where("is_deleted").is(false)));
        qry.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[0])));
        studentList.addAll(mongoOperations.find(qry, Student.class));

//        List<Student> dataStd = studentRepository.findStudentsByMajorCodeAndEntryYearLikeAndIsDeletedIsFalse(major, period);

        for (Student std : studentList) {
            Student dataStudent = studentService.setLastStatus(std.getId(), "id", "", "get");

//            Optional<StudentStatus> optional = statusRepository.findStudentStatusByNimAndAndPeriodId(std.getNim(), periodId);
//            StudentStatus studentStatus = optional.get();
//            studentStatus

            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("nim", dataStudent.getNim());
            hashMap.put("name", dataStudent.getName());
            hashMap.put("major_code", dataStudent.getMajorCode());
//            hashMap.put("status", dataStudent.getStatus());
            hashMap.put("id", dataStudent.getId());
            hashMap.put("entry_year", dataStudent.getEntryYear());

            data.add(hashMap);
        }
        return data;
    }

    public HashMap<String, Object> getStudent(String classId, String periodId) {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("semester_id", periodId);
        hashMap.put("class_schedule_id", classId);

        List<StudyCard> studyCardList = studyCardRepository.findStudyCardsByPeriodTakenAndIsDeletedIsFalse(periodId);
        List<HashMap<String,Object>> hashMapList = new ArrayList<>();

        studyCardList.forEach(e->{
            HashMap<String,Object> getData = new HashMap<>();
            for (int i=0;i<e.getStudentCourses().size();i++) {
                StudentCourse studentCourse = e.getStudentCourses().get(i);
                Student student = studentRepository.findByNimAndIsDeletedIsFalse(e.getNim()).get();

                if (studentCourse.getClassId().equalsIgnoreCase(classId)) {
                    getData.put("nim", e.getNim());
                    getData.put("grade", studentCourse.getGrade());
                    getData.put("name", student.getName());
                    getData.put("major_code", student.getMajorCode());
                    getData.put("student_id", student.getId());
                    getData.put("entry_year", student.getEntryYear());
                    getData.put("study_card_id", e.getId());
                }
            }
            if (!getData.isEmpty()) {
                hashMapList.add(getData);
            }
        });
        hashMap.put("total_participants", hashMapList.size());
        hashMap.put("participants", hashMapList);

        return hashMap;
    }

//    public List<HashMap<String, Object>> getTotalStatus(Map<String, Object> params) {
//        List<Criteria> andCriteria = new ArrayList<>();
//
//        LookupOperation lookupOperation = Aggregation.lookup("student_status", "nim", "nim", "student_status");
//
//        andCriteria.add(Criteria.where("is_deleted").is(false));
//        params.forEach((param, value) -> {
//            if (param.equalsIgnoreCase("major")) {
//                andCriteria.add(Criteria.where("student_status.").is(value));
//            } else if (param.equalsIgnoreCase("period")) {
//                andCriteria.add(Criteria.where("student_status.period_id").is(value));
//            } else if (param.equalsIgnoreCase("status")) {
//                andCriteria.add(Criteria.where("student_status.status_lists.name").is(value).andOperator(Criteria.where("status").is(true)));
//            } else {
//                andCriteria.add(Criteria.where(param).regex(value.toString(), "i"));
//            }
//        });
//
//        MatchOperation match =  Aggregation.match(new Criteria().andOperator(andCriteria.toArray(new Criteria[andCriteria.size()])));
//        SortOperation sort = Aggregation.sort(Sort.Direction.DESC, "nim");
//        AggregationOptions options = AggregationOptions.builder().allowDiskUse(true).build();
//        Aggregation aggregation = Aggregation.newAggregation(match, lookupOperation, sort).withOptions(options);
//
//        List<Student> studentList = mongoTemplate.aggregate(aggregation, "students", Student.class).getMappedResults();
//
//        String periodId = params.get("period").toString();
//        String majorCode= params.get("major").toString();
//        String periodUsed;
//        if (periodId.isEmpty()) {
//            periodUsed = siaGenerate.periodActive(majorCode).get("id").toString();
//        } else {
//            periodUsed = periodId;
//        }
//
//        if (studentList.isEmpty()) {
//            throw new AcceptedException("major code "+ majorCode + " have not student");
//        } else {
//            List<Student> distStd = studentList.stream().filter(distinctByKey(Student::getEntryYear)).collect(Collectors.toList());
//
//            // list for keep hashmap data //
//            List<HashMap<String, Object>> listStudentTotal = new ArrayList<>();
//
//            for (Student distinctYear : distStd) {
//                HashMap<String, Object> studentTotalData = new HashMap<>();
//                List<Student> students = new ArrayList<>();
//                String entryYear = distinctYear.getEntryYear();
//
//                studentTotalData.put("period", entryYear);
//
//                studentList.forEach(e -> {
//                    String stdYear = e.getEntryYear();
//                    if (stdYear.equals(distinctYear.getEntryYear())) {
//                        Student studentDetail = studentService.setLastStatus(e.getId(), "id", periodUsed, "count");
//                        students.add(studentDetail);
//                    }
//
//                });
//                long countStd = students.stream().filter(Objects::nonNull).count();
//                for (String status : siaStatus.status()) {
//                    if (countStd == 0) {
//                        studentTotalData.put(status, 0);
//                        studentTotalData.put("students", Collections.emptyList());
//                    } else {
//                        students.forEach();
//                        List<StudentStatus> statusList = statusRepository.findStudentStatusByNimAndAndPeriodId()
//                        long count = students.stream().filter(s -> s.getStatus().equalsIgnoreCase(status)).filter(a -> a.getEntryYear().equalsIgnoreCase(entryYear)).count();
//                        studentTotalData.put(status, count);
//                        studentTotalData.put("students", students);
//                    }
//                }
//                listStudentTotal.add(studentTotalData);
//            }
//            return listStudentTotal;
//        }
//    }

    // create new status "registration"
    public List<StudentStatus> saveAllStatus(String periodIdActive, String schemaId) {

        // filter status active
        MatchOperation match =  Aggregation.match(Criteria.where("is_deleted").is(false));
        SortOperation sort = Aggregation.sort(Sort.Direction.DESC, "nim");
        AggregationOptions options = AggregationOptions.builder().allowDiskUse(true).build();
        Aggregation aggregation = Aggregation.newAggregation(match, sort).withOptions(options);

        List<Student> studentList = mongoTemplate.aggregate(aggregation, "students", Student.class).getMappedResults();

        List<StudentStatus> studentStatusList = new ArrayList<>();

        studentList.forEach(std -> {
            Student student             = studentService.setLastStatus(std.getNim(), "nim", "", "get");
            StudentStatus studentStatus = student.getStatus();

            // count status no active, get data active / count = 0
            final int[] countNoStatus = {0};
            studentStatus.getStatusLists().forEach(st -> {
                if ( (st.getName().equalsIgnoreCase("drop_out") && st.isStatus()) ||
                    (st.getName().equalsIgnoreCase("graduate") && st.isStatus()) ||
                    (st.getName().equalsIgnoreCase("resign") && st.isStatus()) )
                {
                    countNoStatus[0] = countNoStatus[0] +1;
                }
            });
            if (Integer.parseInt(String.valueOf(countNoStatus[0])) == 0) {
                StudentStatus status = statusService.saveStatus(std.getNim(), periodIdActive, schemaId);
                studentStatusList.add(status);
            }
        });

        return studentStatusList;
    }
}
