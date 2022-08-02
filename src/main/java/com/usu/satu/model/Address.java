package com.usu.satu.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Field;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Address {
    @Field("street_name")
    private String streetName;

    @Field("county_id")
    private String countryId;
    @Transient
    private String country;

    @Field("province_id")
    private String provinceId;
    @Transient
    private String province;

    @Field("city_id")
    private String cityId;
    @Transient
    private String city;

    @Field("sub_district_id")
    private String subDistrictId;
    @Transient
    @Field("sub_district")
    private String subDistrict;     // kecamatan

    @Field("urban_village")
    private String urbanVillage;    // kelurahan

    @Field("post_code")
    private String postCode;
    private String village;         // dusun
    private String rt;
    private String rw;

    @Field("address_type")
    private String addressType;     // eg. domisili, asal

    public Address() {
    }

    public Address(String streetName, String countryId, String country, String provinceId, String province, String cityId, String city, String subDistrictId, String subDistrict, String urbanVillage, String postCode, String village, String rt, String rw, String addressType) {
        this.streetName = streetName;
        this.countryId = countryId;
        this.country = country;
        this.provinceId = provinceId;
        this.province = province;
        this.cityId = cityId;
        this.city = city;
        this.subDistrictId = subDistrictId;
        this.subDistrict = subDistrict;
        this.urbanVillage = urbanVillage;
        this.postCode = postCode;
        this.village = village;
        this.rt = rt;
        this.rw = rw;
        this.addressType = addressType;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSubDistrictId() {
        return subDistrictId;
    }

    public void setSubDistrictId(String subDistrictId) {
        this.subDistrictId = subDistrictId;
    }

    public String getSubDistrict() {
        return subDistrict;
    }

    public void setSubDistrict(String subDistrict) {
        this.subDistrict = subDistrict;
    }

    public String getUrbanVillage() {
        return urbanVillage;
    }

    public void setUrbanVillage(String urbanVillage) {
        this.urbanVillage = urbanVillage;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getRt() {
        return rt;
    }

    public void setRt(String rt) {
        this.rt = rt;
    }

    public String getRw() {
        return rw;
    }

    public void setRw(String rw) {
        this.rw = rw;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    @Override
    public String toString() {
        return "Address{" +
                "streetName='" + streetName + '\'' +
                ", countryId='" + countryId + '\'' +
                ", country='" + country + '\'' +
                ", provinceId='" + provinceId + '\'' +
                ", province='" + province + '\'' +
                ", cityId='" + cityId + '\'' +
                ", city='" + city + '\'' +
                ", subDistrictId='" + subDistrictId + '\'' +
                ", subDistrict='" + subDistrict + '\'' +
                ", urbanVillage='" + urbanVillage + '\'' +
                ", postCode='" + postCode + '\'' +
                ", village='" + village + '\'' +
                ", rt='" + rt + '\'' +
                ", rw='" + rw + '\'' +
                ", addressType='" + addressType + '\'' +
                '}';
    }
}
