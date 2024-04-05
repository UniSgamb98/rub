package com.example.rub.functionalities;

import com.example.rub.beans.Contatto;
import com.example.rub.beans.DeletedContatto;
import com.example.rub.enums.Interessamento;
import com.example.rub.enums.Operatori;
import com.example.rub.enums.TipoCliente;
import com.example.rub.enums.comparator.InteressamentoComp;
import com.example.rub.functionalities.locations.LocationManager;
import com.example.rub.objects.DisplayableEntry;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public abstract class DBManager extends TagsManager{
    private static void format(Contatto bean){
        try {
            bean.setPaese(bean.getPaese().substring(0, 1).toUpperCase() + bean.getPaese().substring(1).toLowerCase());
        }catch (Exception e)    { bean.setPaese("?"); }
        try {
            bean.setRegione(bean.getRegione().substring(0, 1).toUpperCase() + bean.getRegione().substring(1).toLowerCase());
        }catch (Exception e)    { bean.setRegione("??");}
        try {
            bean.setCitta(bean.getCitta().substring(0, 1).toUpperCase() + bean.getCitta().substring(1).toLowerCase());
        }catch (Exception e)    { bean.setCitta("???");}
    }

    public static void saveEntry(Contatto bean, boolean isBeingReconstructed){ //Salva un Contatto nel database
        if (!isBeingReconstructed) {
            try {
                update();
            } catch (Exception e) {
                System.out.println("Salvataggio rischioso, Errore durante l'update del Database persistente");
            }
        }
        format(bean);
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
            boolean a = evaluate(i.getPaese(), input);
            boolean b = evaluate(i.getCitta(), input);
            boolean c = evaluate(i.getRagioneSociale(), input);
            boolean d = evaluate(i.getPersonaRiferimento(), input);
            boolean e = evaluate(i.getRegione(), input);
            boolean f = evaluate(i.getProvincia(), input);
            boolean g = evaluate(i.getTitolare(), input);
            boolean h = evaluate(i.getIndirizzo(), input);
            if (a||b||c||d||e||f||g||h) ret.add(i.getId());
        }
        return ret;
    }
    private static boolean evaluate(String whole, String input){
        int start = 0;
        int fin = input.length()-1;
        input = input.toLowerCase();
        whole = whole.toLowerCase();
        boolean ret = false;
        for (int i = 0; i < whole.length()-input.length()+1; i++){
            if (whole.substring(start, fin+1).equals(input)) {
                ret = true;
                break;
            }
            start++;
            fin++;
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
            if (oldBean.getVolteContattati() != modifiedBean.getVolteContattati())  oldBean.setVolteContattati(modifiedBean.getVolteContattati());
            if (!Objects.equals(oldBean.getTipoCliente(), modifiedBean.getTipoCliente()))   oldBean.setTipoCliente(modifiedBean.getTipoCliente());
            if (!Objects.equals(oldBean.getInteressamento(), modifiedBean.getInteressamento()))   oldBean.setInteressamento(modifiedBean.getInteressamento());
            if (!Objects.equals(oldBean.getUltimaChiamata(), modifiedBean.getUltimaChiamata()))   oldBean.setUltimaChiamata(modifiedBean.getUltimaChiamata());
            if (!Objects.equals(oldBean.getProssimaChiamata(), modifiedBean.getProssimaChiamata()))   oldBean.setProssimaChiamata(modifiedBean.getProssimaChiamata());

            indexNewEntry(oldBean, id);

            MyUtils.writeAll(database, index, locationManager);
        } catch (Exception e){
            System.out.println("Errore durante la modifica di entry");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Operazione non compiuta");
            alert.setTitle("Attenzione");
            alert.setContentText("Riprovare");
            alert.show();
        }
    }
    public static void setNextCall(UUID uuid, LocalDate date, Interessamento feedback){
        try {
            database = (HashMap<UUID, Contatto>) MyUtils.read("database");
            Contatto data = database.get(uuid);
            data.setProssimaChiamata(date);
            data.incrementVolteContattati();
            Calendar now = Calendar.getInstance();
            int year = now.get(Calendar.YEAR);
            int month = now.get(Calendar.MONTH)+1;
            int day = now.get(Calendar.DAY_OF_MONTH);
            LocalDate today = LocalDate.of(year,month,day);
            data.setUltimaChiamata(today);
            if (feedback != null){
                if (new InteressamentoComp().compare(feedback, data.getInteressamento()) > 0) {
                    data.setInteressamento(feedback);
                }
            }
            modifyEntry(uuid, data);
        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Operazione non compiuta");
            alert.setTitle("Attenzione");
            alert.setContentText("Riprovare");
            alert.show();
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

    public static LinkedList<UUID> getCallList() {
        LinkedList<UUID> callList = new LinkedList<>();
        for (Contatto i : database.values()){
            if(isToday(i.getProssimaChiamata())) { callList.add(i.getId());}
        }
        return callList;
    }

    private static boolean isToday(LocalDate dateToConvert) {
        boolean ret = false;
        Calendar now = Calendar.getInstance();
        try {
            if (now.get(Calendar.DAY_OF_MONTH) == dateToConvert.getDayOfMonth() &&
                    now.get(Calendar.MONTH) + 1 == dateToConvert.getMonthValue() &&
                    now.get(Calendar.YEAR) == dateToConvert.getYear()) {
                ret = true;
            }
        } catch (Exception ignored) {}
        return ret;
    }
    public static void export() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Esportazione");
        alert.setContentText("Esportazione avvenuta");
        try {
            File file = new File("Importa.txt");
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            for (Contatto i : database.values()){
                bw.write(i.getRagioneSociale()+";");
                bw.write(i.getPersonaRiferimento()+";");
                bw.write(i.getEmailReferente()+";");
                bw.write(i.getTelefono()+";");
                bw.write(i.getPaese()+";");
                bw.write(i.getRegione()+";");
                bw.write(i.getCitta()+";");
                bw.write(i.getIndirizzo()+";");
                bw.write(i.getNumeroCivico()+";");
                bw.write(i.getProvincia()+";");
                bw.write(i.getCap()+";");
                try {
                    bw.write(i.getInteressamento().name() + ";");
                }catch (Exception e) {  bw.write(Interessamento.BLANK.name() + ";");  }
                try {
                    bw.write(i.getTipoCliente().name() + ";");
                } catch (Exception e) { bw.write(TipoCliente.BLANK.name() +";");    }
                bw.write(i.getPartitaIva()+";");
                bw.write(i.getCodiceFiscale()+";");
                bw.write(i.getTitolare()+";");
                bw.write(i.getEmailGenereica()+";");
                bw.write(i.getEmailCertificata()+";");
                bw.write(i.getSitoWeb()+";");
                bw.write(i.getNoteId()+";");
                try {
                    bw.write(i.getOperator() + ";");
                } catch (Exception e) {bw.write(Operatori.BLANK.name() + ";"); }
                bw.write(i.getVolteContattati()+";");
                bw.write(i.getUltimaChiamata()+";");
                bw.write(i.getProssimaChiamata()+";");
                bw.newLine();
            }
            bw.flush();
            bw.close();
        }
        catch(IOException e) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Esportazione fallita");
        }
        alert.show();
    }
}
