package com.example.rub.functionalities;

import com.example.rub.enums.LogType;

import java.io.*;
import java.util.Calendar;

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
            log(LogType.ERROR);
            log(LogType.MESSAGE, "durante la scrittura di " + fileName + ": " + e.getCause());
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
                MyUtils.log(LogType.ERROR);
                MyUtils.log(LogType.MESSAGE, e);
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

    public static void log(LogType s, Object... objects){
        try{
            FileWriter fw = new FileWriter(GlobalContext.operator.name() + "_log.txt", true);
            Calendar now = Calendar.getInstance();
            fw.write("[" + now.get(Calendar.DAY_OF_MONTH) + "/" + (now.get(Calendar.MONTH)+1) + "/" + now.get(Calendar.YEAR) + "--" + now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE) + "." + now.get(Calendar.SECOND) + "] ");
            switch (s){
                case ACCESS:
                    fw.write("Ha aperto CRM");
                    break;

                case EXIT:
                    fw.write("Ha chiuso CRM");
                    break;

                case NEWENTRY:
                    fw.write("Ha inserito il contatto: " + objects[0]);
                    break;

                case MODIFYENTRY:
                    fw.write("Ha modificato il contatto: " + objects[0]);
                    break;

                case SPECIFYCHANGE:
                    fw.write("          " + objects[0] + ": " + objects[1] + " -> " + objects[2]);
                    break;

                case DELETEENTRY:
                    fw.write("Ha eliminato contatto: " + objects[0]);
                    break;

                case ADDNOTE:
                    fw.write("Ha annotato: " + objects[0]);
                    break;

                case MODIFYNOTE:
                    fw.write("Ha cambiato una nota di: " + objects[0]);
                    break;

                case ERROR:
                    fw.write("Un errore è avvenuto");
                    break;

                case WINDOW:
                    fw.write("Ha aperto finestra: " + objects[0]);
                    break;

                case CAUSED:
                    fw.write("Ha causato un" + objects[0]);
                    break;

                case MESSAGE:
                    fw.write(objects[0]+"");
                    break;
            }
            fw.write(System.getProperty( "line.separator" ));
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException ignored) {}
    }
}
