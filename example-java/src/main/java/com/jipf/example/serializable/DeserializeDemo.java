package com.jipf.example.serializable;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

/**
 * 反序列化
 *
 * @author mayn
 */
public class DeserializeDemo {

    public static void main(String[] args) {
        List<Employee> list = null;
        try {
            FileInputStream fileIn = new FileInputStream("d:/tmp/employee.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            list = (List<Employee>) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            System.out.println("Employee class not found");
            c.printStackTrace();
            return;
        }

        list.stream().forEach(e->{

            System.out.println("Deserialized Employee...");
            System.out.println("Name: " + e.getName());
            System.out.println("Address: " + e.getAddress());
            System.out.println("Number: " + e.getNumber());
            System.out.println();
        });
    }
}
