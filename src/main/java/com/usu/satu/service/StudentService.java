package com.usu.satu.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.usu.satu.dto.StudentRequest;
import com.usu.satu.exeption.AcceptedException;
import com.usu.satu.helper.FormatData;
import com.usu.satu.helper.SIAGenerate;
import com.usu.satu.mapper.StudentMapper;
import com.usu.satu.model.Address;
import com.usu.satu.model.Billing;
import com.usu.satu.model.Student;
import com.usu.satu.model.StudentStatus;
import com.usu.satu.repository.BillingRepository;
import com.usu.satu.repository.StatusRepository;
import com.usu.satu.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentService {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    StudentMapper studentMapper;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    SIAGenerate siaGenerate;

    @Autowired
    StatusRepository statusRepository;

    @Autowired
    BillingRepository billingRepository;

    @Autowired
    DataSourceService dataSourceService;

    public String getMajorCode(String nim) {
        Optional<Student> student = studentRepository.findByNimAndIsDeletedIsFalse(nim);
        return student.map(Student::getMajorCode).orElse(null);
    }

    public Object saveStudent(StudentRequest studentRequest) throws JsonProcessingException {
        Optional<Student> data = studentRepository.findByNimAndIsDeletedIsFalse(studentRequest.getNim());
        if(data.isPresent()){
            throw new AcceptedException("NIM "+data.get().getNim()+" is exist");
        }
        else {
            Student student = new Student();
            studentMapper.updateStudent(studentRequest, student, "/photos");
            studentRepository.save(student);
            return student;
        }
    }

    public List<Student> getAllStudents(){
        List<Student> mhs = studentRepository.findAllByIsDeletedIsFalse();
        if(mhs.isEmpty()){
            return null;
        }
        else {
            return mhs;
        }
    }

    public Student getStudentNim(String nim){
        Optional<Student> data = studentRepository.findByNimAndIsDeletedIsFalse(nim);
        if(data.isPresent()){
            return setLastStatus(nim, "nim","", "get");
        }
        else {
            throw new AcceptedException("nim "+ nim + "is not exist");
        }
    }

    public Student getStudentId(String id){
        Optional<Student> data = studentRepository.findByIdAndIsDeletedIsFalse(id);
        if(data.isPresent()){
            return setLastStatus(id, "id", "", "get");
        }
        else {
            throw new AcceptedException("id "+ id + "is not exist");
        }
    }

    public Student editStudent(String id, StudentRequest studentRequest) throws JsonProcessingException {
        Optional<Student> data = studentRepository.findByIdAndIsDeletedIsFalse(id);
        if(data.isPresent()){
            Student student = data.get();
            studentMapper.updateStudent(studentRequest, student, "/photos");
            studentRepository.save(student);
            return student;
        }else {
            return null;
        }
    }

    public Student removeStudent(String id){
        Optional<Student> data = studentRepository.findByIdAndIsDeletedIsFalse(id);
        if (data.isPresent()){
            Student student = data.get();
            student.setDeleted(true);
            studentRepository.save(student);

            return student;
        } else {
            return null;
        }
    }

    public Student setLastStatus(String data, String type, String period, String purpose) {
//        LookupOperation lookupOperation = Aggregation.lookup("student_status", "nim", "nim", "student_status");
//        MatchOperation match =  Aggregation.match(new Criteria().andOperator(andCriteria.toArray(new Criteria[andCriteria.size()])));
//        SortOperation sort = Aggregation.sort(Sort.Direction.DESC, "datetime_payment");
//        AggregationOptions options = AggregationOptions.builder().allowDiskUse(true).build();
//        Aggregation aggregation = Aggregation.newAggregation(match, lookupOperation, sort).withOptions(options);
//
//        List<Student> studentList = mongoTemplate.aggregate(aggregation, "students", Student.class).getMappedResults();


        Student student = studentRepository.findByNimAndIsDeletedIsFalse(data).get();
//        if (type.equalsIgnoreCase("id")) {
//            student = studentRepository.getMatchStatusId(data);
//        } else {
//            student = studentRepository.getMatchStatusNim(data);
//        }

        String periodUsed;
        if (period.isEmpty()) {
            HashMap<String, Object> periodActiveHashmap = siaGenerate.periodActive(student.getMajorCode());
            periodUsed = periodActiveHashmap.get("id").toString();
//            periodUsed = "62e69f7d12ed34573949177b";
        } else {
            periodUsed = period;
        }

        // match period active //
        StudentStatus periodActive = statusRepository.findStudentStatusByNimAndAndPeriodId(student.getNim(), periodUsed).get();

        List<StudentStatus> statusList = statusRepository.findStudentStatusByNim(student.getNim());
        student.setStudentStatus(statusList);

//        String facultyId = siaGenerate.facultyUnitId(student.getMajorCode());

//        student.setFacultyUnitId(facultyId);
//        student.setFacultyName(siaGenerate.facultyName(facultyId));
//        student.setFacultyUnitId("44");
//        student.setFacultyName("Fakultas Ilmu Komputer dan Teknologi Informasi");

        List<Billing> billings = billingRepository.findBillingsByNim(student.getNim());
        billings.sort(new FormatData.sortCreatedAtBilling());
        student.setBilling(billings);

        List<Address> addresses = student.getAddress();
        addresses.forEach(ad -> {
            ad.setCountry(dataSourceService.getSourceDetail("country", ad.getCountryId()));
            ad.setProvince(dataSourceService.getSourceDetail("province&ref_id="+ad.getCountryId(), ad.getProvinceId()));
            ad.setCity(dataSourceService.getSourceDetail("district&ref_id="+ad.getProvinceId(), ad.getCityId()));
            ad.setSubDistrict(dataSourceService.getSourceDetail("sub_district&ref_id="+ad.getCityId(), ad.getSubDistrictId()));
        });

        if (periodActive != null) {
            student.setStatus(periodActive);
            return student;
        } else {
            if (purpose.equalsIgnoreCase("count")) {
                return null;
            } else {
                return student;
            }
        }
    }

    public HashMap<String, Object> getFilterStudent(Map<String,String> params) {
        // --- Pagination ---
        int size = 0;
        int page = 0;
        int currentPage = params.containsKey("page") ? Integer.parseInt(params.get("page").toString()) : 1;
        Boolean allPage;
//            Pageable pagination = null;

        //PAGINATION PARAMETER
        // If size=all, deactivate pagination
        if (!params.containsKey("size") && !params.containsKey("page")){
            allPage = true;
        } else{
            page = params.containsKey("page") ? ( page + Integer.parseInt(params.get("page").toString()) ) - 1 : 0;
            size = params.containsKey("size") ? size + Integer.parseInt(params.get("size").toString()) : 10;
//                pagination = PageRequest.of(page, size);
            allPage = false;
        }
        params.remove("page");
        params.remove("size");

        Query qry = new Query();

        List<Criteria> andCriteria = new ArrayList<>();

        // nim, nama, prodi, fakultas, kelas, periode tahun akademik

        andCriteria.add(Criteria.where("is_deleted").is(false));
        params.forEach((param, value) -> {
            if (param.equalsIgnoreCase("major")) {
                andCriteria.add(Criteria.where("major_code").is(value));
            } else if (param.equalsIgnoreCase("period")) {
                andCriteria.add(Criteria.where("period").is(value));
            } else if (param.equalsIgnoreCase("year")) {
                andCriteria.add(Criteria.where("entry_year").is(value));
            } else if (param.equalsIgnoreCase("class")) {
                andCriteria.add(Criteria.where("class_type").is(value));
            } else {
                andCriteria.add(Criteria.where(param).regex(value, "i"));
            }
        });

        LookupOperation lookupStatus = Aggregation.lookup("student_status", "nim", "nim", "student_status");

        MatchOperation match =  Aggregation.match(new Criteria().andOperator(andCriteria.toArray(new Criteria[andCriteria.size()])));
        SortOperation sort = Aggregation.sort(Sort.Direction.DESC, "nim");
        AggregationOptions options = AggregationOptions.builder().allowDiskUse(true).build();
        Aggregation aggregation = Aggregation.newAggregation(match, lookupStatus, sort).withOptions(options);

        qry.addCriteria(new Criteria().andOperator(andCriteria.toArray(new Criteria[andCriteria.size()])));

        // --- count total data ---
        long count = mongoTemplate.count(qry, Student.class);

        if(!allPage){
            //--- limit data in list ---
            qry.limit(size).skip(size*page);
        }
//        List<Student> studentList = new ArrayList<>();
//        mongoTemplate.find(qry, Student.class).forEach(studentList::add);

        List<Student> studentList = mongoTemplate.aggregate(aggregation, "students", Student.class).getMappedResults();

        HashMap<String, Object> res = new HashMap<>();
        if(studentList.isEmpty()){
            res.put("current_page", 0);
            res.put("page_size", 0);
            res.put("total_page", 0);
            res.put("total_records", 0);
            res.put("data", null);
        } else{
            if(page == 0 && size == 0){
                res.put("current_page", currentPage);
                res.put("page_size", count);
                res.put("total_page", currentPage);
                res.put("total_records", count);
                res.put("data", studentList);
            }else {
                res.put("current_page", currentPage);
                res.put("page_size", size);
                res.put("total_page", count/size);
                res.put("total_records", count);
                res.put("data", studentList);
            }
        }

        return res;
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    public List<HashMap<String, Object>> getStudentsDistinct () {
        Criteria criteria = Criteria.where("is_deleted").is(false).andOperator(Criteria.where("nationality").ne(null));
        MatchOperation match = Aggregation.match(criteria);
        AggregationOptions options = AggregationOptions.builder().allowDiskUse(true).build();
        Aggregation aggregation = Aggregation.newAggregation(match).withOptions(options);

        List<Student> studentList = mongoTemplate.aggregate(aggregation, "students", Student.class).getMappedResults();

        List<String> distinctNationality = studentList.stream()
                .filter(distinctByKey(p -> p.getNationality()))
                .map(student -> student.getNationality())
                .collect(Collectors.toList());

        List<HashMap<String, Object>> list = new ArrayList<>();

        distinctNationality.forEach(data -> {
            HashMap<String, Object> hashMap = new HashMap<>();
            long count = studentList.stream().filter(p -> p.getNationality().equalsIgnoreCase(data)).count();
            hashMap.put("code", data);
            hashMap.put("total", count);
            list.add(hashMap);
        });

        return list;
    }

    public List<Student> replaceNationality() {
        Criteria criteria = Criteria.where("is_deleted").is(false);
        MatchOperation match = Aggregation.match(criteria);
        AggregationOptions options = AggregationOptions.builder().allowDiskUse(true).build();
        Aggregation aggregation = Aggregation.newAggregation(match).withOptions(options);

        List<Student> studentList = mongoTemplate.aggregate(aggregation, "students", Student.class).getMappedResults();

//        List<String> distinct = studentList
//                .stream()
//                .filter(std->{
//                    if (std.getNationality() != null){
//                         distinctByKey(Student::getNationality);
//                    }
//                    return false;
//                })
//                .map(Student::getNationality)
//                .collect(Collectors.toList());

        List<String> distinctNationality = studentList.stream()
                .filter(distinctByKey(p -> p.getNationality() == null ? "" : p.getNationality()))
                .map(student -> student.getNationality() == null ? "" : student.getNationality())
                .collect(Collectors.toList());

        studentList.forEach(std->{
            if (std.getNationality()!=null) {
                if (std.getNationality().equalsIgnoreCase("344") || std.getNationality().equalsIgnoreCase("china")) {
                    std.setNationality("China");
                    System.out.println("China - " +std.getNim());
                } else if (std.getNationality().equalsIgnoreCase("458") || std.getNationality().equalsIgnoreCase("malaysia")) {
                    std.setNationality("Malaysia");
                    System.out.println("Malaysia - " +std.getNim());
                } else if (std.getNationality().equalsIgnoreCase("702") || std.getNationality().equalsIgnoreCase("singapore")) {
                    std.setNationality("Singapore");
                    System.out.println("Singapore - " +std.getNim());
                } else {
                    std.setNationality("Indonesia");
                }
            } else {
                std.setNationality("Indonesia");
            }

        });
        studentRepository.saveAll(studentList);
        return studentList;
    }

}
