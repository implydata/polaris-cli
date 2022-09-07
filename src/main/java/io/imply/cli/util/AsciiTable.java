package io.imply.cli.util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AsciiTable {

    private List<String> headers = new ArrayList<>();
    private List<List<String>> data = new ArrayList<>();

    public AsciiTable(JSONArray array){
        if(array.isEmpty()){
            return;
        }
        Object o = array.get(0);
        if(o instanceof JSONObject){
            JSONObject jo = (JSONObject)o;
            Set<String> keys = jo.keySet();
            this.headers.addAll(keys);
            for(int i=0; i<array.length(); i++){
                JSONObject obj = array.getJSONObject(i);
                List<String> row = new ArrayList<>();
                for(String k: keys){
                    row.add(obj.optString(k, ""));
                }
                this.data.add(row);
            }
        }else if(o instanceof JSONArray){
            if(array.isEmpty()){
                return;
            }
            JSONArray ha = array.getJSONArray(0);
            List<String> hx = new ArrayList<>();
            for(int i=0; i<ha.length(); i++){
                hx.add(ha.get(i).toString());
            }
            this.headers.addAll(hx);

            for(int i=1; i<array.length(); i++){
                List<String> row = new ArrayList<>();
                JSONArray ja = array.getJSONArray(i);
                for(int j=0; j<ja.length(); j++){
                    Object oo = ja.get(j);
                    row.add(oo.toString());
                }
                this.data.add(row);
            }
        }
    }

    public AsciiTable(JSONObject obj){
        Set<String> keys = obj.keySet();
        this.headers.addAll(keys);
        List<String> row = new ArrayList<>();
        for(String k: keys){
            row.add(obj.optString(k, ""));
        }
        this.data.add(row);
    }

    private int getMaxSize(int column) {
        int maxSize = headers.get(column).length();
        for (List<String> row : data) {
            if (row.get(column).length() > maxSize)
                maxSize = row.get(column).length();
        }
        return maxSize;
    }

    private String formatRow(List<String> row) {
        StringBuilder result = new StringBuilder();
        result.append("|");
        for (int i = 0; i < row.size(); i++) {
            result.append(formatCell(row.get(i), i));
            result.append("|");
        }
        result.append("\n");
        return result.toString();
    }

    private String formatCell(String x, int i){
        int maxSize = getMaxSize(i) + 2;
        int spaces = maxSize - x.length();
        return String.format("%1$"+spaces+"s"+x, " ");
    }

    private String formatRule() {
        StringBuilder result = new StringBuilder();
        result.append("+");
        for (int i = 0; i < headers.size(); i++) {
            for (int j = 0; j < getMaxSize(i) + 2; j++) {
                result.append("-");
            }
            result.append("+");
        }
        result.append("\n");
        return result.toString();
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(formatRule());
        result.append(formatRow(headers));
        result.append(formatRule());
        for (List<String> row : data) {
            result.append(formatRow(row));
        }
        result.append(formatRule());
        return result.toString();
    }


}
