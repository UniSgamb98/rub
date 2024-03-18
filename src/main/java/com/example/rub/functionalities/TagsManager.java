package com.example.rub.functionalities;

import com.example.rub.beans.Contatto;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

public abstract class TagsManager {
    protected static HashMap<UUID, Contatto> database;
    protected static HashMap<String, LinkedList<UUID>> index;

    protected static void indexNewEntry(Contatto beans, UUID uuid){    //indicizza un Contatto che appena stato creato
        String tagPaese = beans.getPaese();
        String tagCitta = beans.getCitta();
        String tagTipoCliente = beans.getTipoCliente();
        String tagInteressamento = beans.getInteressamento();

        insertTagInIndex(tagPaese, uuid);
        insertTagInIndex(tagCitta, uuid);
        insertTagInIndex(tagTipoCliente, uuid);
        insertTagInIndex(tagInteressamento, uuid);
        System.out.println("Aggiornamento tag di filtraggio");
    }

    private static void insertTagInIndex(String tag, UUID uuid){    //Aggiunge il tag l'uuid sotto la voce del tag fornito, se non trovato crea un tag
        if (index.containsKey(tag)) {
            System.out.println("Tag " + tag + " trovato in indice.");
            LinkedList<UUID> tagsUUID = index.get(tag);
            tagsUUID.add(uuid);
            index.put(tag, tagsUUID);
        } else {
            System.out.println("Creazione di un nuovo tag: " + tag);
            LinkedList<UUID> temp = new LinkedList<>();
            temp.add(uuid);
            index.put(tag, temp);
        }
    }
    protected static void write(Object file, String fileName){
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
    protected static Object read(String file) throws IOException, ClassNotFoundException {
        File toRead = new File(file);
        FileInputStream fis = new FileInputStream(toRead);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object temp = ois.readObject();
        ois.close();
        fis.close();
        return temp;
    }
}
