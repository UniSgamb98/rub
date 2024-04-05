package com.example.rub.functionalities;

import java.io.*;

public abstract class MyUtils {
    public static void write(Object file, String fileName){
        try {
            File fileOne = new File("bin\\data\\" + fileName);
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

    public static void writeAll(Object database, Object indice, Object mondo){
        String[] datas = {"database", "indice", "mondo"};
        for (String i : datas) {
            try {
                File fileOne = new File("bin\\data\\" + i);
                FileOutputStream fos = new FileOutputStream(fileOne);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                switch (i) {
                    case "database":
                        oos.writeObject(database);
                        break;
                    case "mondo":
                        oos.writeObject(mondo);
                        break;
                    case "indice":
                        oos.writeObject(indice);
                        break;
                }
                oos.flush();
                oos.close();
                fos.close();
            } catch (Exception e) {
                System.out.println("Scrittura file fallita: " + i);
            }
        }
    }
    public static Object read(String file) throws IOException, ClassNotFoundException {
        File toRead = new File("bin\\data\\" + file);
        FileInputStream fis = new FileInputStream(toRead);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object temp = ois.readObject();
        ois.close();
        fis.close();
        return temp;
    }

    //TODO mettere qua la funzione dell'apertura dei files
}
