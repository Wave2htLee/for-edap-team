package org.wave;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class GenerateSql {
    public static void main(String[] args) {
        main1(args);
    }
    public static void main1(String[] args) {
        "ss".trim()
        if (args.length < 1) {
            throw new RuntimeException (" Need a file which contain tableName,column, if need encrypt");
        } else
            System.out.println(args[0]);
        String FileName = args[0];
        String condition = "";
        if (args.length > 1) {
            condition = args[1];
            System.out.println("condition is : " + condition);
        }
        BufferedReader  reader = null;
        Map<String,ArrayList<Field>> map = new HashMap<String,ArrayList<Field>>();

        try {
            reader = new BufferedReader(new FileReader(
                    FileName));
            String line = reader.readLine();
            while (line != null) {
                String[] split = line.split("\t");
                List<Field> fields = map.computeIfAbsent(split[0], key -> new ArrayList<Field>());
                fields.add(new Field(split[1],split[2],split[3],split[4]));
                //map.computeIfAbsent()
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Set<String> tableNames = map.keySet();
        for (String tableName : tableNames) {
            System.out.println(String.format("select count(1) from %s where true %s",tableName , condition));
            System.out.println("-- COMMAND ----------");
            // 加密字段
            ArrayList<Field> fields = map.get(tableName);

            Set<String> f = new HashSet<>();
            Set<String> eF = new HashSet<>();
            Set<String> timeF = new HashSet<>();
            Set<String> notNF = new HashSet<>();


            for (Field field: fields) {
                f.add(field.toString());
                eF.add(field.EncryptFieldName());
                timeF.add(field.timeStampFieldName());
                notNF.add(field.IsNotNullFieldName());


            }
            eF.remove(null);
            timeF.remove(null);
            notNF.remove(null);

            System.out.println(String.format("select %s from %s where true %s limit 1", Joins.join(f.toArray(new String[0]), ","),tableName));
            System.out.println("-- COMMAND ----------");
            //  加密
            if (!eF.isEmpty()) {
                System.out.println(String.format("select %s from %s where true %s limit 10", Joins.join(eF.toArray(new String[0]), ","), tableName));
                System.out.println("-- COMMAND ----------");

            } else
            {
                System.out.println("-- 表 "+ tableName +" 不用加秘密");
                System.out.println("-- COMMAND ----------");
            }

            // 时间戳
            if (!timeF.isEmpty()) {
                StringBuilder sql = new StringBuilder("select ");
                for (String col : timeF) {
                    sql.append(String.format("sum(if (%s is null),1,0)) as %s ,",col,col));
                }
                sql.append(String.format("'end' as endColumn from %s where true %s",tableName,condition));
                System.out.println(sql);
                System.out.println("-- COMMAND ----------");

            } else
            {
                System.out.println("-- TABLE "+ tableName +" has not timestamp column");
                System.out.println("-- COMMAND ----------");
            }

            // 字段不为空
            if (!notNF.isEmpty()) {
                StringBuilder sql = new StringBuilder("select ");
                for (String col : notNF) {
                    sql.append(String.format("sum(if (%s is null),1,0)) as %s ,",col,col));
                }
                sql.append(String.format("'end' as endColumn from %s where true %s",tableName,condition));
                System.out.println(sql);
                System.out.println("-- COMMAND ----------");

            } else
            {
                System.out.println("-- TABLE "+ tableName +" has no column need not null");
                System.out.println("-- COMMAND ----------");
            }

            System.out.println();
            System.out.println();
            System.out.println();


        }
    }
}
class Field {
    String filedName;
    String isEncrypt;
    String type;
    String isNotNull;
    public Field (String filedName,String isEncrypt){
        this.filedName = filedName;
        this.isEncrypt = isEncrypt;
    }

    public Field (String filedName,String type,String isNotNull, String isEncrypt){
        this.filedName = filedName;
        this.type = type;
        this.isNotNull = isNotNull;
        this.isEncrypt = isEncrypt;
    }

    public String EncryptFieldName(){
        if (isEncrypt.equals("Y")) return filedName; else return null;
    }
    public String timeStampFieldName(){
        if (type.equals("timestamp")) return filedName; else return null;
    }

    public String IsNotNullFieldName(){
        if (isNotNull.equals("Y")) return filedName; else return null;
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
