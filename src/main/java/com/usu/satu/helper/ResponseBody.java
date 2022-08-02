package com.usu.satu.helper;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class ResponseBody {
    private String status;
    private boolean error;
    private String message;
    private Object data;

    public ResponseBody(String status, boolean error, String message, Object data) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.data = data;
    }

    public ResponseBody() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ResponseEntity created(String message, Object data){
        ResponseBody body = new ResponseBody("success", false, message, data);
        return ResponseEntity.status(201).contentType(MediaType.APPLICATION_JSON).body(body);
    }

    public ResponseEntity existed(String message, Object data){
        ResponseBody body = new ResponseBody("exist", false, message, data);
        return ResponseEntity.status(201).contentType(MediaType.APPLICATION_JSON).body(body);
    }

    public ResponseEntity found(String message, Object data){
        ResponseBody body = new ResponseBody("success", false, message, data);
        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(body);
    }

    public ResponseEntity notFound(String message, Object data){
        ResponseBody body = new ResponseBody("failed", false, message, data);
        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(body);
    }

    public ResponseEntity failed(String message, Object data){
        ResponseBody body = new ResponseBody("failed", true, message, data);
        return ResponseEntity.status(500).contentType(MediaType.APPLICATION_JSON).body(body);
    }

    public ResponseBody unAuth(String message, Object data){
        return new ResponseBody("failed", true, message, data);
    }

    @Override
    public String toString() {
        return "ResponseBody{" +
                "status='" + status + '\'' +
                ", error=" + error +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
