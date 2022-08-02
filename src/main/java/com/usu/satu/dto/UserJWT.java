package com.usu.satu.dto;

import org.json.JSONObject;

public class UserJWT {

    private String userId;
    private String identity;
    private String name;
    private String email;
    private String photo;
    private String unitId;

    public UserJWT(JSONObject userData){
        this.setUserId(userData.get("user_id").toString());
        this.setIdentity(userData.get("identity").toString());
        this.setName(userData.get("name").toString());
        this.setEmail(userData.get("email").toString());
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    @Override
    public String toString() {
        return "UserJWT{" +
                "userId='" + userId + '\'' +
                ", identity='" + identity + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", photo='" + photo + '\'' +
                ", unitId='" + unitId + '\'' +
                '}';
    }
}
