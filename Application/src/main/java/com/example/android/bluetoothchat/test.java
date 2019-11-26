package com.example.android.bluetoothchat;

import java.io.Serializable;


public class test implements Serializable {
    String data;
    String data2;
    int data3;


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData2() {
        return data2;
    }

    public void setData2(String data2) {
        this.data2 = data2;
    }

    public int getData3() {
        return data3;
    }

    public void setData3(int data3) {
        this.data3 = data3;
    }

    public test(String d) {
        data=d;
        data2="Hello";
        data3=1000000;
    }





}
