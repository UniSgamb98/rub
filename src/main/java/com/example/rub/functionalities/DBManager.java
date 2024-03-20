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
        MyUtils.write(database, "database");
        MyUtils.write(index, "indice");
        MyUtils.write(groupedTags, "glossario");
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
                index = (HashMap<String, LinkedList<UUID>>) MyUtils.read("indice");
                database = (HashMap<UUID, Contatto>) MyUtils.read("database");
                groupedTags = (ArrayList<Filter>) MyUtils.read("glossario");
            } catch (Exception e) {
                database = new HashMap<>();
                index = new HashMap<>();
                groupedTags = new ArrayList<>();
                System.out.println("Database non trovato... Scrittura di uno nuovo");
                MyUtils.write(database, "database");
                MyUtils.write(index, "indice");
                MyUtils.write(groupedTags, "glossario");
            }
        }
    }

    public static void update(){
        try {
            index = (HashMap<String, LinkedList<UUID>>) MyUtils.read("indice");
            database = (HashMap<UUID, Contatto>) MyUtils.read("database");
            groupedTags = (ArrayList<Filter>) MyUtils.read("glossario");
        } catch (Exception e){
            System.out.println("Problemi durante l'update dal Database persistente");
        }
    }

    public static void modifyEntry(UUID id, Contatto modifiedBean){
        Contatto oldBean = database.get(id);
        String changes = oldBean.compareChanges(modifiedBean);
        for (int i = 0; i < changes.length(); i++){
            switch (changes.charAt(i)){
                case '0':
                    oldBean.setRagioneSociale(modifiedBean.getRagioneSociale());
                    break;
                case '1':
                    oldBean.setPersonaRiferimento(modifiedBean.getPersonaRiferimento());
                    break;
                case '2':
                    oldBean.setTelefono(modifiedBean.getTelefono());
                    break;
                case '3':
                    oldBean.setEmail(modifiedBean.getEmail());
                    break;
                case '4':
                    oldBean.setInteressamento(modifiedBean.getInteressamento());
                    break;
                case '5':
                    oldBean.setTipoCliente(modifiedBean.getTipoCliente());
                    break;
                case '6':
                    changeIndexEntry(oldBean.getId(),TagCategories.PAESE, oldBean.getPaese(), modifiedBean.getPaese());
                    break;
                case '7':
                    changeIndexEntry(oldBean.getId(),TagCategories.CITTA, oldBean.getCitta(), modifiedBean.getCitta());
                    break;
            }
        }
        database.put(id, modifiedBean);
        MyUtils.write(database, "database");
        MyUtils.write(index, "indice");
        MyUtils.write(groupedTags, "glossario");
    }
    //TODO: Funzione che ricostruisce index e glossario dal solo database
}
