package com.usu.satu.service;

import com.usu.satu.dto.StatusList;
import com.usu.satu.exeption.AcceptedException;
import com.usu.satu.helper.FormatData;
import com.usu.satu.helper.SiaStatus;
import com.usu.satu.model.StudentStatus;
import com.usu.satu.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StatusService {

    @Autowired
    StatusRepository statusRepository;

//    @Autowired
//    StatusMapper statusMapper;

    @Autowired
    FormatData formatData;

    @Autowired
    SiaStatus siaStatus;

    public StudentStatus saveStatus(String nim, String periodId, String schemaId) {
        Optional<StudentStatus> optionalStudentStatus = statusRepository.findStudentStatusByNimAndAndPeriodId(nim, periodId);

        if (optionalStudentStatus.isEmpty()) {
            StudentStatus studentStatus = this.fillNew(nim, periodId, schemaId);

            statusRepository.save(studentStatus);

            return studentStatus;
        } else {
            throw new AcceptedException("no data periodId "+periodId+", nim "+nim);
        }
    }

    // for migration sia
    public StudentStatus saveStatusUnsaved(String nim, String periodId, String schemaId) {
        Optional<StudentStatus> optionalStudentStatus = statusRepository.findStudentStatusByNimAndAndPeriodId(nim, periodId);

        if (optionalStudentStatus.isEmpty()) {
            StudentStatus studentStatus = this.createStatusNewStudent(nim, periodId, schemaId);
            return studentStatus;
        } else {
            throw new AcceptedException("no data periodId "+periodId+", nim "+nim);
        }
    }

    // for migration sia
    public StudentStatus createStatusNewStudent(String nim, String periodId, String schemaId) {
        StudentStatus studentStatus = new StudentStatus();

        List<StatusList> list = new ArrayList<>();

        siaStatus.status().forEach(st -> {
            StatusList statusList = new StatusList();

            if (st.equalsIgnoreCase("non_active")) {
                statusList.setStatus(true);
                statusList.setSchemaId(schemaId);
            } else {
                statusList.setStatus(false);
                statusList.setSchemaId(null);
            }
            statusList.setName(st);
            statusList.setUpdatedAt(formatData.getNowLocal());
            list.add(statusList);
        });
        studentStatus.setStatusLists(list);
        studentStatus.setNim(nim);
        studentStatus.setPeriodId(periodId);
        studentStatus.setCreatedAt(formatData.getNowLocal());

        return studentStatus;
    }

    public List<StudentStatus> getAll() {
        return statusRepository.findAll();
    }

    public List<StudentStatus> getMyStatus(String nim) {
        return statusRepository.findStudentStatusByNim(nim);
    }

    // fill statusList default false except request
    public StudentStatus fillNew(String nim, String periodId, String schemaId) {
        StudentStatus studentStatus = new StudentStatus();

        List<StatusList> list = new ArrayList<>();

        siaStatus.status().forEach(st -> {
            StatusList statusList = new StatusList();

            if (st.equalsIgnoreCase("non_active")) {
                statusList.setStatus(true);
                statusList.setSchemaId(schemaId);
            } else {
                statusList.setStatus(false);
                statusList.setSchemaId(null);
            }
            statusList.setName(st);
            statusList.setUpdatedAt(formatData.getNowLocal());
            list.add(statusList);
        });
        studentStatus.setStatusLists(list);
        studentStatus.setNim(nim);
        studentStatus.setPeriodId(periodId);
        studentStatus.setCreatedAt(formatData.getNowLocal());

        return studentStatus;
    }

    // update statusList from false to true
    public StudentStatus updateStatus(String nim, String periodId, String name, boolean value, String schemaId) {
        Optional<StudentStatus> optional = statusRepository.findStudentStatusByNimAndAndPeriodId(nim, periodId);

        if (optional.isPresent()) {
            StudentStatus studentStatus = optional.get();
            studentStatus.getStatusLists().forEach(e->{
                if (e.getName().equalsIgnoreCase(name)) {
                    e.setName(name);
                    e.setStatus(value);
                    e.setUpdatedAt(formatData.getNowLocal());
                    e.setSchemaId(schemaId);
                }
            });
            statusRepository.save(studentStatus);
            return studentStatus;
        } else {
            throw new AcceptedException("no data periodId "+periodId+", nim "+nim);
        }
    }

    public boolean checkRegistered (String nim, String periodId) {
        Optional<StudentStatus> optional = statusRepository.findStudentStatusByNimAndAndPeriodId(nim, periodId);
        final boolean[] val = new boolean[1];
        if (optional.isPresent()) {
            StudentStatus studentStatus = optional.get();
            studentStatus.getStatusLists().forEach(e->{
                if (e.getName().equalsIgnoreCase("registered") && e.isStatus()) {
                    val[0] = true;
                }
                else {
                    val[0] = false;
                }
            });
            return val[0];
        } else {
            return false;
        }
    }
}
