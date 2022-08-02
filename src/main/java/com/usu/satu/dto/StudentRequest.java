package com.usu.satu.dto;

import com.usu.satu.model.Address;
import com.usu.satu.model.Billing;
import com.usu.satu.model.Parent;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public class StudentRequest {
    private String id;
    private String nim;
    private String name;

    private String entry_status;     // pindahan atau baru

    private String entry_year;
    private String period;

    private String major_code;
    private String faculty_unit_id;
    private String nationality;
    private String identity_number;
    private String identity_type;
    private String npwp;
    private String address;
    private String domicile_status; // kos

    private String transportation;
    private String telephone;
    private String hp;
    private String email;

    private boolean is_kps;
    private String kps_number;

    private String curriculum_id;    // get from sia

    private String blood_type;
    private String birth_date;
    private String birth_place;
    private String gender;

    private String family_card_number;
    private String religion;
    private String disabilities;

    private String scholarship_name;
    private String billing;
    private String bill_items;
    private String admission;
    private String class_type; // reguler, paralel

    private String ukt_level;
    private String registration_date;
    private String registration_number;
    private String password_reg;
    private String siblings;
    private String dependents;
    private String marital_status;
    private String parent_marital_status;
    private String parents;
    private String education;
    private String hobby;

    private MultipartFile photo;

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

    public String getEntry_status() {
        return entry_status;
    }

    public void setEntry_status(String entry_status) {
        this.entry_status = entry_status;
    }

    public String getEntry_year() {
        return entry_year;
    }

    public void setEntry_year(String entry_year) {
        this.entry_year = entry_year;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getMajor_code() {
        return major_code;
    }

    public void setMajor_code(String major_code) {
        this.major_code = major_code;
    }

    public String getFaculty_unit_id() {
        return faculty_unit_id;
    }

    public void setFaculty_unit_id(String faculty_unit_id) {
        this.faculty_unit_id = faculty_unit_id;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getIdentity_number() {
        return identity_number;
    }

    public void setIdentity_number(String identity_number) {
        this.identity_number = identity_number;
    }

    public String getIdentity_type() {
        return identity_type;
    }

    public void setIdentity_type(String identity_type) {
        this.identity_type = identity_type;
    }

    public String getNpwp() {
        return npwp;
    }

    public void setNpwp(String npwp) {
        this.npwp = npwp;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDomicile_status() {
        return domicile_status;
    }

    public void setDomicile_status(String domicile_status) {
        this.domicile_status = domicile_status;
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

    public boolean isIs_kps() {
        return is_kps;
    }

    public void setIs_kps(boolean is_kps) {
        this.is_kps = is_kps;
    }

    public String getKps_number() {
        return kps_number;
    }

    public void setKps_number(String kps_number) {
        this.kps_number = kps_number;
    }

    public String getCurriculum_id() {
        return curriculum_id;
    }

    public void setCurriculum_id(String curriculum_id) {
        this.curriculum_id = curriculum_id;
    }

    public String getBlood_type() {
        return blood_type;
    }

    public void setBlood_type(String blood_type) {
        this.blood_type = blood_type;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getBirth_place() {
        return birth_place;
    }

    public void setBirth_place(String birth_place) {
        this.birth_place = birth_place;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFamily_card_number() {
        return family_card_number;
    }

    public void setFamily_card_number(String family_card_number) {
        this.family_card_number = family_card_number;
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

    public String getScholarship_name() {
        return scholarship_name;
    }

    public void setScholarship_name(String scholarship_name) {
        this.scholarship_name = scholarship_name;
    }

    public String getBilling() {
        return billing;
    }

    public void setBilling(String billing) {
        this.billing = billing;
    }

    public String getBill_items() {
        return bill_items;
    }

    public void setBill_items(String bill_items) {
        this.bill_items = bill_items;
    }

    public String getAdmission() {
        return admission;
    }

    public void setAdmission(String admission) {
        this.admission = admission;
    }

    public String getClass_type() {
        return class_type;
    }

    public void setClass_type(String class_type) {
        this.class_type = class_type;
    }

    public String getUkt_level() {
        return ukt_level;
    }

    public void setUkt_level(String ukt_level) {
        this.ukt_level = ukt_level;
    }

    public String getRegistration_date() {
        return registration_date;
    }

    public void setRegistration_date(String registration_date) {
        this.registration_date = registration_date;
    }

    public String getRegistration_number() {
        return registration_number;
    }

    public void setRegistration_number(String registration_number) {
        this.registration_number = registration_number;
    }

    public String getPassword_reg() {
        return password_reg;
    }

    public void setPassword_reg(String password_reg) {
        this.password_reg = password_reg;
    }

    public String getSiblings() {
        return siblings;
    }

    public void setSiblings(String siblings) {
        this.siblings = siblings;
    }

    public String getDependents() {
        return dependents;
    }

    public void setDependents(String dependents) {
        this.dependents = dependents;
    }

    public String getMarital_status() {
        return marital_status;
    }

    public void setMarital_status(String marital_status) {
        this.marital_status = marital_status;
    }

    public String getParent_marital_status() {
        return parent_marital_status;
    }

    public void setParent_marital_status(String parent_marital_status) {
        this.parent_marital_status = parent_marital_status;
    }

    public String getParents() {
        return parents;
    }

    public void setParents(String parents) {
        this.parents = parents;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public MultipartFile getPhoto() {
        return photo;
    }

    public void setPhoto(MultipartFile photo) {
        this.photo = photo;
    }
}
