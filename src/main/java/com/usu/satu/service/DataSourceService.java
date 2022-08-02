package com.usu.satu.service;

import com.usu.satu.exeption.AcceptedException;
import com.usu.satu.helper.FormatData;
import com.usu.satu.helper.SIAGenerate;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class DataSourceService {

    @Autowired
    SIAGenerate siaGenerate;

    @Autowired
    FormatData formatData;

    public List<HashMap<String, String>> getBillings(String nim){
        JSONArray jsonArray = formatData.getDataBilling(nim);

        List<HashMap<String, String>> billings = new ArrayList<>();
        for (int i=0; i<jsonArray.length(); i++){
            JSONObject bills    = jsonArray.getJSONObject(i);
            JSONArray items     = bills.getJSONArray("items");

            String status = null;
            if (bills.getString("billing_status").equalsIgnoreCase("paid")){
                status = "Sudah Dibayar";
            } else if (bills.getString("billing_status").equalsIgnoreCase("unpaid")){
                status = "Belum Dibayar";
            } else if (bills.getString("billing_status").equalsIgnoreCase("expired")){
                status = "Pembayaran Ditutup";
            } else if (bills.getString("billing_status").equalsIgnoreCase("partial_paid")){
                status = "Sudah Dicicil";
            }

            for (int j=0; j<items.length(); j++){
                HashMap<String, String> hashMap = new HashMap<>();
                JSONObject item = items.getJSONObject(j);

                hashMap.put("nim", nim);
                hashMap.put("item_name", item.getString("item_name"));
                hashMap.put("status", status);
                hashMap.put("datetime_payment", bills.getString("datetime_payment_iso8601").equalsIgnoreCase("unpaid") ? null : bills.getString("datetime_payment_iso8601"));
                billings.add(hashMap);
            }
        }
        return billings;
    }

    public List<HashMap<String, String>> getAsa(String nim, String ssoToken){
        JSONArray jsonArray = formatData.getDataAsa(nim, ssoToken);
        List<HashMap<String, String>> asa = new ArrayList<>();

        for (int i=0; i<jsonArray.length(); i++){
            HashMap<String, String> hashMap = new HashMap<>();
            JSONObject getData = jsonArray.getJSONObject(i);

            String status;
            if (!getData.isNull("finished_date")){
                status = "Selesai";
            } else {
                if (!getData.isNull("rejected_date")){
                    status = "Ditolak";
                } else {
                    if (!getData.isNull("processed_date")){
                        status = "Diproses";
                    } else {
                        status = "Diajukan";
                    }
                }
            }
            hashMap.put("nim", getData.getString("requester"));
            hashMap.put("letters_name", getData.getString("letters_name"));
            hashMap.put("submitted_date", getData.getString("submitted_date"));
            hashMap.put("status", status);
            asa.add(hashMap);
        }
        return asa;
    }

    public List<HashMap<String,Object>> getFaculties() {
        return siaGenerate.getFaculties();
    }

    public List<HashMap<String,Object>> getMajors(String id) {
        return siaGenerate.getMajors(id);
    }

    public List<HashMap<String,String>> getDataCollect(String param) {
        JSONObject result   = formatData.restTemplate("https://akademik.usu.ac.id/api/reference/collection?key="+param);

        if (result.isNull("data")) {
            throw new AcceptedException("data collect academic is not exist key="+param);
        }

        JSONArray jsonData  = result.getJSONArray("data");
        List<HashMap<String, String>> hashList = new ArrayList<>();
        for (int i=0;i<jsonData.length();i++) {
            JSONObject jsonObject = jsonData.getJSONObject(i);
            HashMap<String,String> periodHash = new HashMap<>();
            jsonObject.keys().forEachRemaining(key->{
                if (jsonObject.isNull(key)){
                    periodHash.put(key, "");
                } else{
                    periodHash.put(key, jsonObject.get(key).toString());
                }
            });
            hashList.add(periodHash);
        }
        return hashList;
    }

    public String getSourceDetail(String param, String id) {
        if (id != null) {
            JSONObject result   = formatData.restTemplate("https://akademik.usu.ac.id/api/reference/collection?key="+param);

            if (result.isNull("data")) {
                throw new AcceptedException("data collect academic is not exist key="+param);
            }

            String name = null;
            JSONArray jsonData  = result.getJSONArray("data");
            for (int i=0;i<jsonData.length();i++) {
                JSONObject jsonObject = jsonData.getJSONObject(i);
                if (jsonObject.get("id").toString().equalsIgnoreCase(id)) {
                    name = jsonObject.get("name").toString();
                }
            }
            return name;
        }
        else {
            return null;
        }
    }

    public List<HashMap<String,String>> getDataSource(String param) {
        JSONObject result   = formatData.restTemplate("https://akademik.usu.ac.id/api/reference/"+param);

        if (result.isNull("data")) {
            throw new AcceptedException("data collect academic is not exist key="+param);
        }

        JSONArray jsonData  = result.getJSONArray("data");
        List<HashMap<String, String>> hashList = new ArrayList<>();
        for (int i=0;i<jsonData.length();i++) {
            JSONObject jsonObject = jsonData.getJSONObject(i);
            HashMap<String,String> periodHash = new HashMap<>();
            jsonObject.keys().forEachRemaining(key->{
                if (jsonObject.isNull(key)){
                    periodHash.put(key, "");
                } else{
                    periodHash.put(key, jsonObject.get(key).toString());
                }
            });
            hashList.add(periodHash);
        }
        return hashList;
    }
}
