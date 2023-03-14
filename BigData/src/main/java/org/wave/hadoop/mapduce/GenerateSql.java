package org.wave.hadoop.mapduce;

import java.io.*;
import java.util.*;

public class GenerateSql {
    public static void main(String[] args) {
        main1(args);
    }
    public static void main1(String[] args) {
        BufferedReader  reader = null;
        Map<String,ArrayList<Field>> map = new HashMap<String,ArrayList<Field>>();

        try {
            reader = new BufferedReader(new FileReader(
                    "D:\\lht\\aa.txt.txt"));
            String line = reader.readLine();
            while (line != null) {
                String[] split = line.split("\t");
                List<Field> fields = map.computeIfAbsent(split[0], key -> new ArrayList<Field>());
                fields.add(new Field(split[1],split[2]));
                //map.computeIfAbsent()
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Set<String> tableNames = map.keySet();
        for (String tableName : tableNames) {
            System.out.println(String.format("select count(1) from %s",tableName));
            System.out.println("-- COMMAND ----------");
            // 加密字段
            ArrayList<Field> fields = map.get(tableName);


            Set<String> f = new HashSet<>();
            Set<String> eF = new HashSet<>();

            for (Field field: fields) {
                f.add(field.toString());
                eF.add(field.EncryptFieldName());
            }
            eF.remove(null);
            System.out.println(String.format("select %s from %s", Joins.join(f.toArray(new String[0]), ","),tableName));
            System.out.println("-- COMMAND ----------");

            if (!eF.isEmpty()) {
                System.out.println(String.format("select %s from %s", Joins.join(eF.toArray(new String[0]), ","), tableName));
                System.out.println("-- COMMAND ----------");

            } else
            {
                System.out.println("-- 表 "+ tableName +" 不用加秘密");
                System.out.println("-- COMMAND ----------");
            };

        }
    }
}
class Field {
    String filedName;
    String isEncrypt;
    public Field (String filedName,String isEncrypt){
        this.filedName = filedName;
        this.isEncrypt = isEncrypt;
    }
    public String EncryptFieldName(){
        if (isEncrypt.equals("Y")) return filedName; else return null;
    }
    @Override
    public String toString(){
        return filedName;
    }
}

class Joins {
    public static String join (String[] strings, String s) {
        StringBuilder stringBuilder = new StringBuilder();
        if (strings == null || strings.length == 0) {
            return "";
        } else {
            for (int i = 0; i < strings.length; i++) {
                if(strings[i] != null && !strings[i].equals("null") && !strings[i] .equals( "")) {
                    stringBuilder.append(strings[i]);
                    if (i != strings.length - 1) {
                        stringBuilder.append(s);
                    }
                }
            }
        }
        return stringBuilder.toString();
    }
}
