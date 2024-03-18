package com.example.rub.functionalities;

import com.example.rub.beans.Contatto;
import com.example.rub.objects.DisplayableEntry;
import javafx.scene.layout.HBox;

import java.util.*;


public abstract class DBManager extends TagsManager{

    public static void saveEntry(Contatto bean){ //Salva un Contatto nel database
        UUID uuid = UUID.randomUUID();
        bean.setId(uuid);
        database.put(uuid, bean);
        indexNewEntry(bean, uuid);
        write(database, "database");
        write(index, "indice");
        System.out.println("Nuovi dati salvati correttamente!");
    }
    public static Contatto retriveEntry(UUID uuid){  //carica un Contatto dal database
        return database.get(uuid);
    }
    public static LinkedList<UUID> getAllEntries (){
        return new LinkedList<>(database.keySet());
    }
    public static HBox getDisplayableEntry(UUID uuid){
        return new DisplayableEntry(DBManager.retriveEntry(uuid));
    }
    public static LinkedList<UUID> getEntryFromFilter (String filtro){
        return index.get(filtro);
    }   //Restituisce una LinkedList di Contatti con il valore del filtro in comune
    public static void init(){
        loadData();
    }   //Recupera i dati all'avvio grazie alla chiamata in firstPageController

    public static ArrayList<String> getFilterOptions(){
        return new ArrayList<>(index.keySet());
    }

    public static LinkedList<UUID> rapidSearch(String input){
        LinkedList<UUID> ret = new LinkedList<>();
        for (Contatto i : database.values()){
            boolean a = Objects.equals(i.getPaese(), input);
            boolean b = Objects.equals(i.getCitta(), input);
            boolean c = Objects.equals(i.getRagioneSociale(), input);
            boolean d = Objects.equals(i.getPersonaRiferimento(), input);
            if (a||b||c||d) ret.add(i.getId());
        }
        return ret;
    }
    private static void loadData(){     //Legge il database e indice dai file salvati persistentemente
        if (database == null) {
            try {
                index = (HashMap<String, LinkedList<UUID>>) read("indice");
                database = (HashMap<UUID, Contatto>) read("database");
            } catch (Exception e) {
                database = new HashMap<>();
                index = new HashMap<>();
                System.out.println("Database non trovato... Scrittura di uno nuovo");
                write(database, "database");
                write(index, "indice");
            }
        }
    }
}
