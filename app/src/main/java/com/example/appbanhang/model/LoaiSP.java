package com.example.appbanhang.model;

public class LoaiSP {
    int id;
    String tendm;
    String hinhanh;

    public LoaiSP(String tensanpham, String hinhanh) {
        this.tendm = tensanpham;
        this.hinhanh = hinhanh;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getTendm() {
        return tendm;
    }

    public void setTendm(String tendm) {
        this.tendm = tendm;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }




}
