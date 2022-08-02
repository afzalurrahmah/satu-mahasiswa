package com.usu.satu.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.usu.satu.dto.*;
import com.usu.satu.helper.FormatData;
import com.usu.satu.helper.SIAGenerate;
import com.usu.satu.model.*;
import com.usu.satu.repository.BillingRepository;
import com.usu.satu.repository.StatusRepository;
import com.usu.satu.repository.StudentRepository;
import com.usu.satu.service.StatusService;
import com.usu.satu.service.StudentService;
import com.usu.satu.storage.FilesStorageService;
import org.bson.types.Decimal128;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Component
@Transactional
public class StudentMapperImpl implements StudentMapper {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    FilesStorageService storageService;

    @Autowired
    SIAGenerate siaGenerate;

    @Autowired
    FormatData formatData;

    @Autowired
    StudentService studentService;

    @Autowired
    StatusRepository statusRepository;

    @Autowired
    BillingRepository billingRepository;

    @Autowired
    StatusService statusService;

    @Override
    public void updateStudent(StudentRequest student, Student entity, String path) throws JsonProcessingException {

        GsonBuilder gsonBuilder         = new GsonBuilder();
        Gson gson                       = gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

        List<Parent> listParent         = gson.fromJson(student.getParents(), ArrayList.class);
        HashMap<String, Object> hashMap = gson.fromJson(student.getEducation(), HashMap.class);
//        List<Address> listAddress       = gson.fromJson(student.getAddress(), ArrayList.class);

        Type medicineListType = new TypeToken<List<Address>>() {}.getType();
        List<Address> listAddress = gson.fromJson(student.getAddress(), medicineListType);

        if ( student == null ) {
            return;
        }

        if ( student.getId() != null ) {
            entity.setId( student.getId() );
        } else {
            // create new status non_active
            String periodId                 = siaGenerate.periodActive(student.getMajor_code()).get("id").toString();
            statusService.saveStatus(student.getNim(), periodId, null);

            entity.setAcademicLectures(new ArrayList<>());

            List<BillingItem> bills         = gson.fromJson(student.getBill_items(), ArrayList.class);

            JSONObject jsonObject = new JSONObject(student.getBilling());
            LocalDateTime timeCreated = formatData.stringToLocalDateTime(jsonObject.getString("datetime_created"));
            LocalDateTime timeExpired = formatData.stringToLocalDateTime(jsonObject.getString("datetime_expired"));
            LocalDateTime timePayment = formatData.stringToLocalDateTime(jsonObject.getString("datetime_payment"));
            Billing billing = new Billing();
            billing.setNim(student.getNim());
            billing.setBillingItems(bills);
            billing.setPaymentNtb(jsonObject.getString("payment_ntb"));
            billing.setTrxId(jsonObject.getString("trx_id"));
            billing.setPeriodId(jsonObject.getString("period_id"));
            billing.setVirtualAccount(jsonObject.getString("virtual_account"));
            billing.setTrxAmount(Decimal128.parse(jsonObject.getString("trx_amount")));
            billing.setPaid(true);
            billing.setDatetimePayment(timePayment);
            billing.setDatetimeCreated(timeCreated);
            billing.setDatetimeExpired(timeExpired);
            billing.setPaymentType("va");
            billing.setPaymentAmount(Decimal128.parse(jsonObject.getString("payment_amount")));

            billingRepository.save(billing);

            String curriculum = siaGenerate.getCurriculum(student.getMajor_code());
            entity.setCurriculumId(curriculum);
        }

        if ( student.getNim() != null ) {
            entity.setNim( student.getNim() );
        }
        if ( student.getName() != null ) {
            entity.setName( student.getName() );
        }

//        if ( student.getBilling() != null ) {
//            List<Billing> billingList = entity.getBilling();
//            billingList.add(bill);
//            entity.setBilling( billingList );
//        }

//        if ( student.getStatus() != null ) {
//            entity.setStatus( student.getStatus() );
//        } else if ( student.getStatus() == null && entity.getStatus() != null ){
//            entity.setStatus( entity.getStatus() );
//        } else {
//            entity.setStatus("aktif");
//        }

//        entity.setStatusId(new ObjectId("62344dce43a3551e5e79ff4d"));

        if ( student.getEntry_status() != null ) {
            entity.setEntryStatus( student.getEntry_status() );
        }
        if ( student.getEntry_year() != null ) {
            entity.setEntryYear( student.getEntry_year() );
        }

        if ( student.getMajor_code() != null ) {
            entity.setMajorCode( student.getMajor_code() );
        }
        if ( student.getNationality() != null ) {
            entity.setNationality( student.getNationality() );
        }
        if ( student.getIdentity_number() != null ) {
            entity.setIdentityNumber( student.getIdentity_number() );
        }
        if ( student.getIdentity_type() != null ) {
            entity.setIdentityType( student.getIdentity_type() );
        }
        if ( student.getNpwp() != null ) {
            entity.setNpwp( student.getNpwp() );
        }

        if ( entity.getAddress() != null ) {
            if ( listAddress != null ) {
                if (listAddress.size()>1) {
                    entity.getAddress().clear();
                    entity.getAddress().addAll( listAddress );
                } else {
                    List<Address> list = entity.getAddress();
                    for (int i=0;i<(list.size());i++) {
                        if (list.get(i).getAddressType().equalsIgnoreCase(listAddress.get(0).getAddressType())) {
                            list.remove(list.get(i));
                            list.add(listAddress.get(0));
                            entity.setAddress(list );
                        }
                    }
                }
            }
        }
        else {
            if ( listAddress != null ) {
                entity.setAddress(new ArrayList<>(listAddress) );
            }
        }

        if ( student.getTransportation() != null ) {
            entity.setTransportation( student.getTransportation() );
        }
        if ( student.getTelephone() != null ) {
            entity.setTelephone( student.getTelephone() );
        }
        if ( student.getHp() != null ) {
            entity.setHp( student.getHp() );
        }
        if ( student.getEmail() != null ) {
            entity.setEmail( student.getEmail() );
        }
        entity.setKps( student.isIs_kps() );
        if ( student.getKps_number() != null ) {
            entity.setKpsNumber( student.getKps_number() );
        }

        if ( student.getCurriculum_id() != null ) {
            entity.setCurriculumId(student.getCurriculum_id());
        }

        if ( student.getBlood_type() != null ) {
            entity.setBloodType( student.getBlood_type() );
        }
        if ( student.getBirth_date() != null ) {

            LocalDate localDate = LocalDate.parse(student.getBirth_date());
            entity.setBirthDate( localDate );
        }
        if ( student.getBirth_place() != null ) {
            entity.setBirthPlace( student.getBirth_place() );
        }
        if ( student.getGender() != null ) {
            entity.setGender( student.getGender() );
        }
        if ( student.getFamily_card_number() != null ) {
            entity.setFamilyCardNumber( student.getFamily_card_number() );
        }
        if ( student.getReligion() != null ) {
            entity.setReligion( student.getReligion() );
        }
        if ( student.getDisabilities() != null ) {
            entity.setDisabilities( student.getDisabilities() );
        }
        if ( student.getScholarship_name() != null ) {
            entity.setScholarshipName( student.getScholarship_name() );
        }

        if ( student.getAdmission() != null ) {
            entity.setAdmission( student.getAdmission() );
        }
        if ( student.getClass_type() != null ) {
            entity.setClassType( student.getClass_type() );
        }
        if ( student.getRegistration_date() != null ) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(student.getRegistration_date(), formatter);
            entity.setRegistrationDate(date);
        }
        if ( student.getRegistration_number() != null ) {
            entity.setRegistrationNumber( student.getRegistration_number() );
        }
        if ( student.getPeriod() != null ) {
            entity.setPeriod( student.getPeriod() );
        }
        if ( student.getMarital_status() != null ) {
            entity.setMaritalStatus( student.getMarital_status() );
        }
        if ( student.getParent_marital_status() != null ) {
            entity.setParentMaritalStatus( student.getParent_marital_status() );
        }
        if ( entity.getParents() != null ) {
            if ( listParent != null ) {
                entity.getParents().clear();
                entity.getParents().addAll( listParent );
            }
        }
        else {
            if ( listParent != null ) {
                entity.setParents(new ArrayList<>(listParent) );
            }
        }
        if ( entity.getEducation() != null ) {
            if ( hashMap != null ) {
                entity.getEducation().clear();
                entity.getEducation().putAll( hashMap );
            }
        }
        else {
            if ( hashMap != null ) {
                entity.setEducation(new HashMap<>(hashMap) );
            }
        }
        if ( student.getPhoto() != null ) {
            String filePath                 = storageService.saveFile(student.getPhoto(), path, student.getNim(), "data");
            String fileName                 = storageService.getFileName(student.getPhoto(), student.getNim(), "data");

            entity.setPhoto( fileName );
            entity.setUrlPhoto(filePath);
        }

        if (student.getUkt_level() != null) {
            entity.setUktLevel(student.getUkt_level());
        }

        if ( student.getDomicile_status() != null ) {
            entity.setDomicileStatus( student.getDomicile_status() );
        }
        if ( student.getPassword_reg() != null ) {
            entity.setPasswordReg( student.getPassword_reg() );
        }

        if ( student.getSiblings() != null ) {
            entity.setSiblings( Integer.parseInt(student.getSiblings()) );
        }

        if ( student.getDependents() != null ) {
            entity.setDependents( Integer.parseInt(student.getDependents()) );
        }
        if ( student.getHobby() != null ) {
            entity.setHobby( student.getHobby() );
        }

        entity.setDeleted( false );
    }
}
