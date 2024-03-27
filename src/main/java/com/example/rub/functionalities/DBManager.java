package com.example.rub.functionalities;

import com.example.rub.beans.Contatto;
import com.example.rub.functionalities.locations.LocationManager;
import com.example.rub.objects.DisplayableEntry;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public abstract class DBManager extends TagsManager{

    public static void saveEntry(Contatto bean){ //Salva un Contatto nel database
        try {
            update();
        } catch (Exception e) {
            System.out.println("Salvataggio rischioso, Errore durante l'update del Database persistente");
        }
        UUID uuid = UUID.randomUUID();
        bean.setId(uuid);
        database.put(uuid, bean);
        indexNewEntry(bean, uuid);
        MyUtils.write(database, "database");
        MyUtils.write(index, "indice");
        System.out.println("Nuovo contatto inserito in database!");
    }
    public static void deleteEntry(UUID id){
        try {
            update();
        } catch (Exception e){
            System.out.println("Errore durante l'eliminazione di una entry");
        }
        removeEntryFromLocationManager(id);
        removeEntryFromIndex(id);
        database.remove(id);

        MyUtils.write(database, "database");
        MyUtils.write(index, "indice");
        MyUtils.write(locationManager, "mondo");
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
                update();
            } catch (Exception e) {
                database = new HashMap<>();
                index = new HashMap<>();
                locationManager = new LocationManager();
                System.out.println("Database non trovato... Scrittura di uno nuovo");
                MyUtils.write(database, "database");
                MyUtils.write(index, "indice");
                MyUtils.write(locationManager, "mondo");
            }
        }
    }
    public static void update() throws IOException, ClassNotFoundException {
        index = (HashMap<String, LinkedList<UUID>>) MyUtils.read("indice");
        database = (HashMap<UUID, Contatto>) MyUtils.read("database");
        locationManager = (LocationManager) MyUtils.read("mondo");
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
                    oldBean.setEmailReferente(modifiedBean.getEmailReferente());
                    break;
                case '4':
                    oldBean.setInteressamento(modifiedBean.getInteressamento());
                    break;
                case '5':
                    oldBean.setTipoCliente(modifiedBean.getTipoCliente());
                    break;
                case '6':
                    changeIndexEntry(oldBean.getId(), oldBean.getPaese(), modifiedBean.getPaese());
                    break;
                case '7':
                    changeIndexEntry(oldBean.getId(), oldBean.getCitta(), modifiedBean.getCitta());
                    break;
            }
        }
        database.put(id, modifiedBean);
        MyUtils.write(database, "database");
        MyUtils.write(index, "indice");
    }
    public static void setNextCall(UUID uuid, LocalDate date){
        try {
            database = (HashMap<UUID, Contatto>) MyUtils.read("database");
            Contatto data = database.get(uuid);
            data.setProssimaChiamata(date);
            data.incrementVolteContattati();
            Calendar now = Calendar.getInstance();
            System.out.println(now.get(Calendar.DATE));
            int year = now.get(Calendar.YEAR);
            int month = now.get(Calendar.MONTH);
            int day = now.get(Calendar.DAY_OF_MONTH);
            LocalDate today = LocalDate.of(year,month,day);
            data.setUltimaChiamata(today);
            data.setRememberMe(date == null);
            database.put(uuid, data);
            MyUtils.write(database, "database");
        } catch (Exception e){
            System.out.println("Errore durante setNextCall in DBManager");
        }
    }

    public static void reconstruct(){
        HashMap<UUID, Contatto> oldDatabase = database;
        database = new HashMap<>();
        for (Contatto i : oldDatabase.values()){
            DBManager.saveEntry(i);
        }
        MyUtils.write(database, "database");
        MyUtils.write(index, "indice");
        MyUtils.write(locationManager, "mondo");
        System.out.println("il Database è stato ricostruito");
    }
}
