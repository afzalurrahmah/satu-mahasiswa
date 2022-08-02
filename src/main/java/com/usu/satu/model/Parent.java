package com.usu.satu.model;

import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

public class Parent {
    private String type;

    @Field("identity_number")
    private String identityNumber;
    private String name;

    @Field("birth_date")
    private LocalDate birthDate;
    private String education;
    private String job;
    private String income;
    private String disabilities;
    private String telephone;
    private String email;

    private String address;
    @Field("post_code")
    private String postCode;

    public Parent() {
    }

    public Parent(String type, String identityNumber, String name, LocalDate birthDate, String education, String job, String income, String disabilities, String telephone, String email, String address, String postCode) {
        this.type = type;
        this.identityNumber = identityNumber;
        this.name = name;
        this.birthDate = birthDate;
        this.education = education;
        this.job = job;
        this.income = income;
        this.disabilities = disabilities;
        this.telephone = telephone;
        this.email = email;
        this.address = address;
        this.postCode = postCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getDisabilities() {
        return disabilities;
    }

    public void setDisabilities(String disabilities) {
        this.disabilities = disabilities;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    @Override
    public String toString() {
        return "Parent{" +
                "type='" + type + '\'' +
                ", identityNumber='" + identityNumber + '\'' +
                ", name='" + name + '\'' +
                ", birthDate=" + birthDate +
                ", education='" + education + '\'' +
                ", job='" + job + '\'' +
                ", income='" + income + '\'' +
                ", disabilities='" + disabilities + '\'' +
                ", telephone='" + telephone + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", postCode='" + postCode + '\'' +
                '}';
    }
}
