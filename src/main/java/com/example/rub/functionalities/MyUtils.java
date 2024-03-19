package com.example.rub.functionalities;

import java.io.*;

public abstract class MyUtils {
    public static void write(Object file, String fileName){
        try {
            File fileOne = new File(fileName);
            FileOutputStream fos = new FileOutputStream(fileOne);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(file);
            oos.flush();
            oos.close();
            fos.close();
        } catch (Exception e){
            System.out.println("Scrittura file fallita: "+ fileName);
        }
    }
    public static Object read(String file) throws IOException, ClassNotFoundException {
        File toRead = new File(file);
        FileInputStream fis = new FileInputStream(toRead);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object temp = ois.readObject();
        ois.close();
        fis.close();
        return temp;
    }

    public static void delete(String fileName){
        File fileOne = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(fileOne);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(new Object());
        } catch (Exception e){
            System.out.println("Cancellazione fallita di: "+ fileName);
        }
    }
}
