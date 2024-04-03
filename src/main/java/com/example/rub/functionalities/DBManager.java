package com.example.rub.functionalities;

import com.example.rub.beans.Contatto;
import com.example.rub.beans.DeletedContatto;
import com.example.rub.functionalities.locations.LocationManager;
import com.example.rub.objects.DisplayableEntry;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.util.*;

public abstract class DBManager extends TagsManager{

    public static void saveEntry(Contatto bean, boolean isBeingReconstructed){ //Salva un Contatto nel database
        if (!isBeingReconstructed) {
            try {
                update();
            } catch (Exception e) {
                System.out.println("Salvataggio rischioso, Errore durante l'update del Database persistente");
            }
        }
        UUID uuid = UUID.randomUUID();
        bean.setId(uuid);
        database.put(uuid, bean);
        indexNewEntry(bean, uuid);
        MyUtils.writeAll(database, index, locationManager);
        System.out.println("Nuovo contatto inserito in database!");
    }
    public static void deleteEntry(UUID id){
        try {
            update();
        } catch (Exception e){
            System.out.println("Errore durante l'eliminazione di una entry");
        }
        try {
            trashcan = (LinkedList<DeletedContatto>) MyUtils.read("dataStructure");
        } catch (Exception e) {
            trashcan = new LinkedList<>();
        }
            trashcan.add(new DeletedContatto(database.get(id)));

        removeEntryFromLocationManager(id);
        removeEntryFromIndex(id);
        database.remove(id);

        MyUtils.writeAll(database, index, locationManager);
        MyUtils.write(trashcan, "dataStructure");
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
        if (database == null) { //questa condizione è per eseguire il load solo la prima volta che si accede a firstpage
            update();
        }
    }
    public static void update(){
        try {
            database = (HashMap<UUID, Contatto>) MyUtils.read("database");
            try {
                index = (HashMap<String, LinkedList<UUID>>) MyUtils.read("indice");
                locationManager = (LocationManager) MyUtils.read("mondo");
            } catch (Exception e) {
                reconstruct();
            }
        } catch (Exception e){
            rebuild();
        }
    }

    public static void modifyEntry(UUID id, Contatto modifiedBean){
        try {
            update();
            Contatto oldBean = database.get(id);

            removeEntryFromLocationManager(id);
            removeEntryFromIndex(id);

            if (!oldBean.getRagioneSociale().equals(modifiedBean.getRagioneSociale()))   oldBean.setRagioneSociale(modifiedBean.getRagioneSociale());
            if (!oldBean.getPersonaRiferimento().equals(modifiedBean.getPersonaRiferimento()))   oldBean.setPersonaRiferimento(modifiedBean.getPersonaRiferimento());
            if (!oldBean.getEmailReferente().equals(modifiedBean.getEmailReferente())) oldBean.setEmailReferente(modifiedBean.getEmailReferente());
            if (!oldBean.getPaese().equals(modifiedBean.getPaese()))   oldBean.setPaese(modifiedBean.getPaese());
            if (!oldBean.getRegione().equals(modifiedBean.getRegione()))   oldBean.setRegione(modifiedBean.getRegione());
            if (!oldBean.getCitta().equals(modifiedBean.getCitta()))   oldBean.setCitta(modifiedBean.getCitta());
            if (!oldBean.getIndirizzo().equals(modifiedBean.getIndirizzo()))   oldBean.setIndirizzo(modifiedBean.getIndirizzo());
            if (!oldBean.getNumeroCivico().equals(modifiedBean.getNumeroCivico()))   oldBean.setNumeroCivico(modifiedBean.getNumeroCivico());
            if (!oldBean.getProvincia().equals(modifiedBean.getProvincia()))   oldBean.setProvincia(modifiedBean.getProvincia());
            if (!oldBean.getCap().equals(modifiedBean.getCap()))   oldBean.setCap(modifiedBean.getCap());
            if (!oldBean.getPartitaIva().equals(modifiedBean.getPartitaIva()))   oldBean.setPartitaIva(modifiedBean.getPartitaIva());
            if (!oldBean.getCodiceFiscale().equals(modifiedBean.getCodiceFiscale()))   oldBean.setCodiceFiscale(modifiedBean.getCodiceFiscale());
            if (!oldBean.getTelefono().equals(modifiedBean.getTelefono()))   oldBean.setTelefono(modifiedBean.getTelefono());
            if (!oldBean.getEmailGenereica().equals(modifiedBean.getEmailGenereica()))   oldBean.setEmailGenereica(modifiedBean.getEmailGenereica());
            if (!oldBean.getEmailCertificata().equals(modifiedBean.getEmailCertificata()))   oldBean.setEmailCertificata(modifiedBean.getEmailCertificata());
            if (!oldBean.getSitoWeb().equals(modifiedBean.getSitoWeb()))   oldBean.setSitoWeb(modifiedBean.getSitoWeb());
            if (!oldBean.getTitolare().equals(modifiedBean.getTitolare()))   oldBean.setTitolare(modifiedBean.getTitolare());
            if (!Objects.equals(oldBean.getTipoCliente(), modifiedBean.getTipoCliente()))   oldBean.setTipoCliente(modifiedBean.getTipoCliente());
            if (!Objects.equals(oldBean.getInteressamento(), modifiedBean.getInteressamento()))   oldBean.setInteressamento(modifiedBean.getInteressamento());
            if (!Objects.equals(oldBean.getUltimaChiamata(), modifiedBean.getUltimaChiamata()))   oldBean.setUltimaChiamata(modifiedBean.getUltimaChiamata());
            if (!Objects.equals(oldBean.getProssimaChiamata(), modifiedBean.getProssimaChiamata()))   oldBean.setProssimaChiamata(modifiedBean.getProssimaChiamata());

            indexNewEntry(oldBean, id);

            MyUtils.writeAll(database, index, locationManager);
        } catch (Exception e){
            System.out.println("Errore durante la modifica di entry");
        }
    }
    public static void setNextCall(UUID uuid, LocalDate date){
        try {
            database = (HashMap<UUID, Contatto>) MyUtils.read("database");
            Contatto data = database.get(uuid);
            data.setProssimaChiamata(date);
            data.incrementVolteContattati();
            Calendar now = Calendar.getInstance();
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
        index = new HashMap<>();
        locationManager = new LocationManager();
        for (Contatto i : oldDatabase.values()){
            DBManager.saveEntry(i, true);
        }
        MyUtils.writeAll(database, index, locationManager);
        System.out.println("il Database è stato ricostruito");
    }

    private static void rebuild(){
        database = new HashMap<>();
        index = new HashMap<>();
        locationManager = new LocationManager();
        System.out.println("Database non trovato... Scrittura di uno nuovo");
        MyUtils.writeAll(database, index, locationManager);
        System.out.println("il Database è stato ricreato");
    }

}
