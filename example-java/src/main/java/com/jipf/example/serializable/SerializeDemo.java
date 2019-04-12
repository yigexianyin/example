package com.jipf.example.serializable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SerializeDemo {
    public static void main(String[] args) {

        List<Employee> list = new ArrayList<Employee>();

        Employee e1 = new Employee();
        e1.setName("小明");
        e1.setAddress("北京");
        e1.setNumber(101);

        Employee e2 = new Employee();
        e2.setName("小红");
        e2.setAddress("上海");
        e2.setNumber(102);

        Employee e3 = new Employee();
        e3.setName("小白");
        e3.setAddress("广州");
        e3.setNumber(103);

        Collections.addAll(list,e1,e2,e3);

        try {
            FileOutputStream fileOut =
                    new FileOutputStream("d:/tmp/employee.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(list);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in /tmp/employee.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
}
