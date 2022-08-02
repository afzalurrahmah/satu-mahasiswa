package com.usu.satu.service;

import com.usu.satu.helper.FormatData;
import com.usu.satu.helper.SIAGenerate;
import com.usu.satu.model.StudyCard;
import com.usu.satu.repository.StudyCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class PeriodService {

    @Autowired
    StudentService studentService;

    @Autowired
    SIAGenerate siaGenerate;

    @Autowired
    StudyCardRepository studyCardRepository;

    public List<HashMap<String, Object>> getPeriodList(String nim) {
        return siaGenerate.listPeriod(studentService.getMajorCode(nim));
    }

    public HashMap<String, Object> periodActive (String nim) {
        String majorCode = studentService.getMajorCode(nim);
        return siaGenerate.periodActive(majorCode);
    }

    public HashMap<String,Object> getDetail(String nim, String periodId) {
        String majorCode = studentService.getMajorCode(nim);
        return siaGenerate.getDetailPeriod(periodId, majorCode);
    }

    public List<String> getPeriodIdListStudent(String nim) {
        List<StudyCard> studyCardList = studyCardRepository.findStudyCardsByNimAndIsDeletedIsFalse(nim);
        if (studyCardList.isEmpty()) {
//            throw new AcceptedException(nim+ " have not study card");
            return new ArrayList<>();
        } else {
            studyCardList.sort(new FormatData.sortCreatedAtStudyCard());
            List<String> periodList = new ArrayList<>();
            studyCardList.forEach(e->{
                periodList.add(e.getPeriodTaken());
            });
            return periodList;
        }
    }
}
