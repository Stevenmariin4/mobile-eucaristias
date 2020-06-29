package com.ecucaristia.EcucaristiaAppmobile;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class decoderPerson {
    dataPerson NewPerson;
    JSONObject json;
    public decoderPerson()
    {

    }


    public dataPerson decoderIdentification(String data) {
        NewPerson=new dataPerson();
        json = new JSONObject();

        String REGEX = "[0-9][0-9][a-zA-Z][a-zA-Z]";
        if (!data.isEmpty()) {

            data = data.replace("PDF417", "");
            data = data.replace("\n", "");
            data = data.replace("\0"," ");
            String[] dta = data.split(" ");
            List<String> s = Arrays.asList(dta);
            List<String> s2  = new ArrayList<String>();
            int indexDD=-1;
            String noIdentity="";
            String OriginalNoIdentiry="";
            List<String> LastName = new ArrayList<String>();
            List<String> names=new ArrayList<String>();
            String BirthDate="";
            String Gender="";
            for (String value : s) {
                if (!TextUtils.isEmpty(value)) {
                    s2.add(value);
                }
            }

            for (int i = 0; i <s2.size() ; i++) {
                String localvalue = s2.get(i);
                Pattern mpattern = Pattern.compile("[0-9][0-9][a-zA-Z][a-zA-Z]");
                Matcher m = mpattern.matcher(localvalue);
                boolean isregex = m.find();
                if (isregex && noIdentity == "") {
                    char[] tex = localvalue.toCharArray();

                    int index = 0;
                    for (int ii = 0; ii < tex.length; ii++) {

                        Pattern mpattern2 = Pattern.compile("[a-zA-Z]+$");
                        Matcher m2 = mpattern2.matcher(String.valueOf(tex[ii]));
                        boolean isregex2 = m2.find();
                        if (isregex2) {
                            index = ii;
                            break;
                        }
                    }

                    noIdentity = localvalue.substring(0, index);
                    noIdentity = noIdentity.substring(noIdentity.length() - 10);
                    OriginalNoIdentiry=noIdentity;
                    noIdentity = Integer.toString(Integer.parseInt(noIdentity));
                    NewPerson.NoIdentity=noIdentity;
                    Log.i("decoderIdentification: ", noIdentity);
                }

                Pattern mpattern3 = Pattern.compile("^0[F-M]");
                Matcher m3 = mpattern3.matcher(localvalue);
                boolean isregex3 = m3.find();
                if (isregex3) {
                    indexDD = i;
                }
            }
            if (indexDD>-1) {
                String TemporalData = data.substring(data.indexOf(OriginalNoIdentiry));
                TemporalData = TemporalData.substring(0, TemporalData.lastIndexOf(s2.get(indexDD))+s2.get(indexDD).length()+3);

                char[] tex2 = TemporalData.toCharArray();

                NewPerson.LastNameList.clear();
                NewPerson.NameList.clear();
                NewPerson.LastNameList.add(TemporalData.substring(10, 33).replace(" ", ""));
                NewPerson.LastNameList.add(TemporalData.substring(33, 56).replace(" ", ""));
                NewPerson.NameList.add(TemporalData.substring(56, 79).replace(" ", ""));
                NewPerson.NameList.add(TemporalData.substring(79, 102).replace(" ", ""));
                NewPerson.Gender = TemporalData.substring(103, 104);
                String fecha = TemporalData.substring(104, 112);
                NewPerson.BirthDate = fecha.substring(0, 4) + "-" + fecha.substring(4, 6) + "-" + fecha.substring(6);
                for (String na:NewPerson.LastNameList) {
                    NewPerson.LastNames+=na+" ";
                }
                NewPerson.LastNames=NewPerson.LastNames.trim();
                for (String na:NewPerson.NameList) {
                    NewPerson.Names+=na+" ";
                }
                NewPerson.Names=NewPerson.Names.trim();

                Log.i("decoderIdentification: ",NewPerson.BirthDate);
                Log.i("decoderIdentification: ",NewPerson.Gender);
            }


        }

        return NewPerson;
    }
}
