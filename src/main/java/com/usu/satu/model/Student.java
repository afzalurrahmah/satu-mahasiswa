package com.usu.satu.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.usu.satu.dto.AcademicLecture;
import com.usu.satu.dto.PeriodList;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Document(collection = "students")
public class Student {
    @Id
    private String id;
    private String nim;
    private String name;
    private StudentStatus status;      // status list of active period

    @Field("student_status")
    private List<StudentStatus> studentStatus;

    @Field("entry_status")
    private String entryStatus;     // pindahan atau baru

    @Field("academic_lectures")
    private List<AcademicLecture> academicLectures;

    @Field("entry_year")
    private String entryYear;

    private String period;

    @Field("major_code")
    private String majorCode;

    @Transient
    @Field("faculty_unit_id")
    private String facultyUnitId;

    @Transient
    @Field("faculty_name")
    private String facultyName;

    private String nationality;

    @Field("identity_number")
    private String identityNumber;

    @Field("identity_type")
    private String identityType;

    private String npwp;
    private List<Address> address;

    @Field("domicile_status")
    private String domicileStatus; // kos

    private String transportation;
    private String telephone;
    private String hp;
    private String email;

    @Field("is_kps")
    private boolean isKps;

    @Field("kps_number")
    private String kpsNumber;

    @Field("curriculum_id")
    private String curriculumId;    // get from sia

    @Field("blood_type")
    private String bloodType;

    @Field("birth_date")
    private LocalDate birthDate;

    @Field("birth_place")
    private String birthPlace;
    private String gender;

    @Field("family_card_number")
    private String familyCardNumber;
    private String religion;
    private String disabilities;

    @Field("scholarship_name")
    private String scholarshipName;

    private List<Billing> billing;
    private String admission;

    @Field("class_type")
    private String classType; // reguler, paralel

    @Field("ukt_level")
    private String uktLevel;

    @Field("registration_date")
    private LocalDate registrationDate;

    @Field("registration_number")
    private String registrationNumber;

    @Field("password_reg")
    private String passwordReg;

    private int siblings;
    private int dependents;

    @Field("marital_status")
    private String maritalStatus;

    @Field("parent_marital_status")
    private String parentMaritalStatus;

    private List<Parent> parents;

    private HashMap<String, Object> education;

    private String hobby;

    @Field("url_photo")
    private String urlPhoto;
    private String photo;

    @Field("study_card")
    private StudyCard studyCard;

    @CreatedDate
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Field("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Field("updated_at")
    private LocalDateTime updatedAt;

    @CreatedBy
    @Field("created_by")
    private String createdBy;

    @LastModifiedBy
    @Field("updated_by")
    private String updatedBy;

    @Field("is_deleted")
    private boolean isDeleted;

    public Student() {
    }

    public Student(String id, String nim, String name, StudentStatus status, List<StudentStatus> studentStatus, String entryStatus, List<AcademicLecture> academicLectures, String entryYear, String period, String majorCode, String facultyUnitId, String facultyName, String nationality, String identityNumber, String identityType, String npwp, List<Address> address, String domicileStatus, String transportation, String telephone, String hp, String email, boolean isKps, String kpsNumber, String curriculumId, String bloodType, LocalDate birthDate, String birthPlace, String gender, String familyCardNumber, String religion, String disabilities, String scholarshipName, List<Billing> billing, String admission, String classType, String uktLevel, LocalDate registrationDate, String registrationNumber, String passwordReg, int siblings, int dependents, String maritalStatus, String parentMaritalStatus, List<Parent> parents, HashMap<String, Object> education, String hobby, String urlPhoto, String photo, StudyCard studyCard, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy, boolean isDeleted) {
        this.id = id;
        this.nim = nim;
        this.name = name;
        this.status = status;
        this.studentStatus = studentStatus;
        this.entryStatus = entryStatus;
        this.academicLectures = academicLectures;
        this.entryYear = entryYear;
        this.period = period;
        this.majorCode = majorCode;
        this.facultyUnitId = facultyUnitId;
        this.facultyName = facultyName;
        this.nationality = nationality;
        this.identityNumber = identityNumber;
        this.identityType = identityType;
        this.npwp = npwp;
        this.address = address;
        this.domicileStatus = domicileStatus;
        this.transportation = transportation;
        this.telephone = telephone;
        this.hp = hp;
        this.email = email;
        this.isKps = isKps;
        this.kpsNumber = kpsNumber;
        this.curriculumId = curriculumId;
        this.bloodType = bloodType;
        this.birthDate = birthDate;
        this.birthPlace = birthPlace;
        this.gender = gender;
        this.familyCardNumber = familyCardNumber;
        this.religion = religion;
        this.disabilities = disabilities;
        this.scholarshipName = scholarshipName;
        this.billing = billing;
        this.admission = admission;
        this.classType = classType;
        this.uktLevel = uktLevel;
        this.registrationDate = registrationDate;
        this.registrationNumber = registrationNumber;
        this.passwordReg = passwordReg;
        this.siblings = siblings;
        this.dependents = dependents;
        this.maritalStatus = maritalStatus;
        this.parentMaritalStatus = parentMaritalStatus;
        this.parents = parents;
        this.education = education;
        this.hobby = hobby;
        this.urlPhoto = urlPhoto;
        this.photo = photo;
        this.studyCard = studyCard;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.isDeleted = isDeleted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StudentStatus getStatus() {
        return status;
    }

    public void setStatus(StudentStatus status) {
        this.status = status;
    }

    public List<StudentStatus> getStudentStatus() {
        return studentStatus;
    }

    public void setStudentStatus(List<StudentStatus> studentStatus) {
        this.studentStatus = studentStatus;
    }

    public String getEntryStatus() {
        return entryStatus;
    }

    public void setEntryStatus(String entryStatus) {
        this.entryStatus = entryStatus;
    }

    public List<AcademicLecture> getAcademicLectures() {
        return academicLectures;
    }

    public void setAcademicLectures(List<AcademicLecture> academicLectures) {
        this.academicLectures = academicLectures;
    }

    public String getEntryYear() {
        return entryYear;
    }

    public void setEntryYear(String entryYear) {
        this.entryYear = entryYear;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getMajorCode() {
        return majorCode;
    }

    public void setMajorCode(String majorCode) {
        this.majorCode = majorCode;
    }

    public String getFacultyUnitId() {
        return facultyUnitId;
    }

    public void setFacultyUnitId(String facultyUnitId) {
        this.facultyUnitId = facultyUnitId;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getIdentityType() {
        return identityType;
    }

    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }

    public String getNpwp() {
        return npwp;
    }

    public void setNpwp(String npwp) {
        this.npwp = npwp;
    }

    public List<Address> getAddress() {
        return address;
    }

    public void setAddress(List<Address> address) {
        this.address = address;
    }

    public String getDomicileStatus() {
        return domicileStatus;
    }

    public void setDomicileStatus(String domicileStatus) {
        this.domicileStatus = domicileStatus;
    }

    public String getTransportation() {
        return transportation;
    }

    public void setTransportation(String transportation) {
        this.transportation = transportation;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isKps() {
        return isKps;
    }

    public void setKps(boolean kps) {
        isKps = kps;
    }

    public String getKpsNumber() {
        return kpsNumber;
    }

    public void setKpsNumber(String kpsNumber) {
        this.kpsNumber = kpsNumber;
    }

    public String getCurriculumId() {
        return curriculumId;
    }

    public void setCurriculumId(String curriculumId) {
        this.curriculumId = curriculumId;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFamilyCardNumber() {
        return familyCardNumber;
    }

    public void setFamilyCardNumber(String familyCardNumber) {
        this.familyCardNumber = familyCardNumber;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getDisabilities() {
        return disabilities;
    }

    public void setDisabilities(String disabilities) {
        this.disabilities = disabilities;
    }

    public String getScholarshipName() {
        return scholarshipName;
    }

    public void setScholarshipName(String scholarshipName) {
        this.scholarshipName = scholarshipName;
    }

    public List<Billing> getBilling() {
        return billing;
    }

    public void setBilling(List<Billing> billing) {
        this.billing = billing;
    }

    public String getAdmission() {
        return admission;
    }

    public void setAdmission(String admission) {
        this.admission = admission;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getUktLevel() {
        return uktLevel;
    }

    public void setUktLevel(String uktLevel) {
        this.uktLevel = uktLevel;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getPasswordReg() {
        return passwordReg;
    }

    public void setPasswordReg(String passwordReg) {
        this.passwordReg = passwordReg;
    }

    public int getSiblings() {
        return siblings;
    }

    public void setSiblings(int siblings) {
        this.siblings = siblings;
    }

    public int getDependents() {
        return dependents;
    }

    public void setDependents(int dependents) {
        this.dependents = dependents;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getParentMaritalStatus() {
        return parentMaritalStatus;
    }

    public void setParentMaritalStatus(String parentMaritalStatus) {
        this.parentMaritalStatus = parentMaritalStatus;
    }

    public List<Parent> getParents() {
        return parents;
    }

    public void setParents(List<Parent> parents) {
        this.parents = parents;
    }

    public HashMap<String, Object> getEducation() {
        return education;
    }

    public void setEducation(HashMap<String, Object> education) {
        this.education = education;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public StudyCard getStudyCard() {
        return studyCard;
    }

    public void setStudyCard(StudyCard studyCard) {
        this.studyCard = studyCard;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", nim='" + nim + '\'' +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", studentStatus=" + studentStatus +
                ", entryStatus='" + entryStatus + '\'' +
                ", academicLectures=" + academicLectures +
                ", entryYear='" + entryYear + '\'' +
                ", period='" + period + '\'' +
                ", majorCode='" + majorCode + '\'' +
                ", facultyUnitId='" + facultyUnitId + '\'' +
                ", nationality='" + nationality + '\'' +
                ", identityNumber='" + identityNumber + '\'' +
                ", identityType='" + identityType + '\'' +
                ", npwp='" + npwp + '\'' +
                ", address=" + address +
                ", domicileStatus='" + domicileStatus + '\'' +
                ", transportation='" + transportation + '\'' +
                ", telephone='" + telephone + '\'' +
                ", hp='" + hp + '\'' +
                ", email='" + email + '\'' +
                ", isKps=" + isKps +
                ", kpsNumber='" + kpsNumber + '\'' +
                ", curriculumId='" + curriculumId + '\'' +
                ", bloodType='" + bloodType + '\'' +
                ", birthDate=" + birthDate +
                ", birthPlace='" + birthPlace + '\'' +
                ", gender='" + gender + '\'' +
                ", familyCardNumber='" + familyCardNumber + '\'' +
                ", religion='" + religion + '\'' +
                ", disabilities='" + disabilities + '\'' +
                ", scholarshipName='" + scholarshipName + '\'' +
                ", billing=" + billing +
                ", admission='" + admission + '\'' +
                ", classType='" + classType + '\'' +
                ", uktLevel='" + uktLevel + '\'' +
                ", registrationDate=" + registrationDate +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", passwordReg='" + passwordReg + '\'' +
                ", siblings=" + siblings +
                ", dependents=" + dependents +
                ", maritalStatus='" + maritalStatus + '\'' +
                ", parentMaritalStatus='" + parentMaritalStatus + '\'' +
                ", parents=" + parents +
                ", education=" + education +
                ", hobby='" + hobby + '\'' +
                ", urlPhoto='" + urlPhoto + '\'' +
                ", photo='" + photo + '\'' +
                ", studyCard=" + studyCard +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", createdBy='" + createdBy + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
