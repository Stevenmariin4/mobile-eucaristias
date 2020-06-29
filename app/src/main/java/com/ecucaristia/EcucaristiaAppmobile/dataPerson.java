package com.ecucaristia.EcucaristiaAppmobile;

import java.util.ArrayList;
import java.util.List;

public class dataPerson {
    public String NoIdentity;
    public List<String> NameList;
    public String Names = "";
    public String LastNames = "";
    public List<String> LastNameList;
    public String BirthDate;
    public String Gender;


    public dataPerson() {
        LastNameList = new ArrayList<String>();
        NameList = new ArrayList<String>();
        LastNameList.clear();
        NameList.clear();
    }
}
