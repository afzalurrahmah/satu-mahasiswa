//package com.usu.satu.mapper;
//
//import com.usu.satu.dto.PeriodList;
//import com.usu.satu.dto.StatusList;
//import com.usu.satu.exception.AcceptedException;
//import com.usu.satu.helper.FormatData;
//import com.usu.satu.helper.SIAGenerate;
//import com.usu.satu.model.StudentStatus;
//import com.usu.satu.service.StudentService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Locale;
//import java.util.stream.Collectors;
//
//@Component
//public class StatusMapperImpl implements StatusMapper {
//
//    @Autowired
//    FormatData formatData;
//
//    @Autowired
//    SIAGenerate siaGenerate;
//
//    @Autowired
//    StudentService studentService;
//
//    @Override
//    public void addStatus(HashMap<String, String> hashMap, StudentStatus studentStatus, String nim) {
//
//        if ( studentStatus == null ) {
//            return;
//        }
//
//        if ( hashMap != null ) {
//            String periodNew = hashMap.get("period_id");
//            String statusNew = hashMap.get("status");
//            String schemaNew = hashMap.get("schema_id");
//
//            List<PeriodList> periodLists = studentStatus.getPeriodLists();
//
//            String majorCode = studentService.getMajorCode(nim);
//
//            int pkaMaxMajor = siaGenerate.getMaxPkaMajor(majorCode);
//            int pkaStudent = formatData.getTotalPkaStudent(periodLists);
//
//            boolean valPka = pkaStudent < pkaMaxMajor;
//
//            // if nim existed
//            if ( periodLists != null ) {
//
//                // if period input existed
//                if ( periodLists.stream().anyMatch(old->old.getPeriodId().equalsIgnoreCase(periodNew)) ) {
//
//                    if ( hashMap.get("status").toUpperCase(Locale.ROOT).equalsIgnoreCase("pka") && !valPka ) {
//                        throw new AcceptedException("PKA is limited");
//                    }
//
//                    PeriodList currentPeriod = periodLists
//                            .stream()
//                            .filter(old->old.getPeriodId().equalsIgnoreCase(periodNew))
//                            .collect(Collectors.toList())
//                            .get(0);
//
//                    // check index
//                    int currentPeriodIndex = periodLists.indexOf(currentPeriod);
//
//                    List<StatusList> currentStatusList = currentPeriod.getStatusLists();
//
//                    // if status input not exist
//                    if ( currentStatusList.stream().noneMatch(old->old.getStatus().equalsIgnoreCase(statusNew)) ){
//
//                        //add status to list
//                        StatusList status = new StatusList();
//                        status.setUpdatedAt( formatData.getCurrentTime() );
//                        status.setSchemaId( schemaNew );
//                        status.setStatus( statusNew );
//
//                        //add new status to list
//                        currentStatusList.add( status );
//
//                        //set new statusList
//                        currentPeriod.setStatusLists( currentStatusList );
//
//                        //replace current period with the new one
//                        periodLists.set(currentPeriodIndex, currentPeriod);
//
//                        //set new period list
//                        studentStatus.setPeriodLists( periodLists );
//                    }
//                } else {
//                    StatusList status = new StatusList();
//                    status.setStatus( statusNew );
//                    status.setSchemaId( schemaNew );
//                    status.setUpdatedAt( formatData.getCurrentTime() );
//
//                    List<StatusList> statusLists = new ArrayList<>();
//                    statusLists.add( status );
//
//                    PeriodList period = new PeriodList();
//                    period.setPeriodId( periodNew );
//                    period.setStatusLists( statusLists );
//
//                    periodLists.add( period );
//                }
//            } else {
//
//                if ( hashMap.get("status").toUpperCase(Locale.ROOT).equalsIgnoreCase("pka") && !valPka ) {
//                    throw new AcceptedException("PKA is limited");
//                }
//
//                StatusList status = new StatusList();
//                status.setStatus(statusNew);
//                status.setUpdatedAt( formatData.getCurrentTime() );
//                status.setSchemaId( schemaNew );
//
//                List<StatusList> statusLists = new ArrayList<>();
//                statusLists.add( status );
//
//                PeriodList period = new PeriodList();
//                period.setPeriodId( periodNew );
//                period.setStatusLists( statusLists );
//
//                List<PeriodList> periodList = new ArrayList<>();
//                periodList.add( period );
//
//                studentStatus.setNim(nim );
//                studentStatus.setPeriodLists( periodList );
//            }
//        }
//    }
//}
