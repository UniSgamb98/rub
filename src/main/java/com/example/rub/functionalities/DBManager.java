package com.example.rub.functionalities;

import com.example.rub.beans.Contatto;
import com.example.rub.enums.TagCategories;
import com.example.rub.objects.DisplayableEntry;
import com.example.rub.objects.filter.Filter;
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
        write(groupedTags, "glossario");
        System.out.println("Nuovo contatto inserito in database!");
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

    public static LinkedList<String> getFilterOptionsFromCategory(TagCategories category){
        LinkedList<String> temp = new LinkedList<>();
        for(int i = 0; i < TagCategories.values().length; i++ ){
            if (!groupedTags.isEmpty() && groupedTags.get(i).getCategory() == category){
                temp.addAll(groupedTags.get(i).getTags());
            }
        }
        return temp;
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
                groupedTags = (ArrayList<Filter>) read("glossario");
            } catch (Exception e) {
                database = new HashMap<>();
                index = new HashMap<>();
                groupedTags = new ArrayList<>();
                System.out.println("Database non trovato... Scrittura di uno nuovo");
                write(database, "database");
                write(index, "indice");
                write(groupedTags, "glossario");
            }
        }
    }
}
