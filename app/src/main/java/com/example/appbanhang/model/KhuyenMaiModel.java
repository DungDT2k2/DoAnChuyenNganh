package com.example.appbanhang.model;

import java.util.List;

public class KhuyenMaiModel {
    boolean success;
    String massage;
    List<KhuyenMai> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public List<KhuyenMai> getResult() {
        return result;
    }

    public void setResult(List<KhuyenMai> result) {
        this.result = result;
    }
}
