package com.example.chscommunityprototype1;

public class DataClass {

    private String dataDate;
    private String dataDesc;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String key;

    public DataClass(String dataDate, String dataDesc) {
        this.dataDate = dataDate;
        this.dataDesc = dataDesc;
    }

    public String getDataDate() {
        return dataDate;
    }

    public String getDataDesc() {
        return dataDesc;
    }

    public DataClass(){

    }

}
