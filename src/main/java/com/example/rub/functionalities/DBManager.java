package com.example.rub.functionalities;

import com.example.rub.beans.Contatto;
import com.example.rub.beans.DeletedContatto;
import com.example.rub.enums.Interessamento.InteressamentoStatus;
import com.example.rub.enums.LogType;
import com.example.rub.enums.Operatori;
import com.example.rub.enums.Outcome;
import com.example.rub.enums.TipoCliente;
import com.example.rub.enums.comparator.InteressamentoComp;
import com.example.rub.functionalities.locations.LocationManager;
import com.example.rub.objects.note.DisplayableEntry;
import javafx.scene.control.Alert;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
                MyUtils.log(LogType.ERROR);
                MyUtils.log(LogType.MESSAGE, e);
            }
            MyUtils.log(LogType.NEWENTRY, bean);
        }
        format(bean);
        UUID uuid = UUID.randomUUID();
        bean.setId(uuid);
        database.put(uuid, bean);
        indexNewEntry(bean, uuid);
        if (!isBeingReconstructed) {
            MyUtils.writeAll(database, index, locationManager);
        }
        System.out.println("Nuovo contatto inserito in database!");
    }
    public static void deleteEntry(UUID id){
        MyUtils.log(LogType.DELETEENTRY, database.get(id));
        try {
            update();
        } catch (Exception e){
            MyUtils.log(LogType.ERROR);
            MyUtils.log(LogType.MESSAGE, e);
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
    public static DisplayableEntry getDisplayableEntry(UUID uuid){
        return new DisplayableEntry(uuid);
    }
    public static LinkedList<UUID> getEntriesFromFilter(String filtro){
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
    private static void loadData() {     //Legge il database e indice dai file salvati persistentemente
        try {
            GlobalContext.notProgrammedCalls = (LinkedList<UUID>) MyUtils.read(GlobalContext.operator.name());
        } catch (Exception e) {
            GlobalContext.notProgrammedCalls = new LinkedList<>();
        }
        update();
    }
    public static void saveData(){
        MyUtils.writeAll(database, index, locationManager);
    }
    public static Outcome update(){
        Outcome res = Outcome.FAILURE;
        LinkedList<UUID> callRemindersNoteId = new LinkedList<>();
            try {
                for (UUID i : GlobalContext.notProgrammedCalls) {
                    callRemindersNoteId.add(DBManager.retriveEntry(i).getNoteId());
                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("L'ultima volta avevi lasciato alcuni contatti da riprogrammare la richiamata");
                alert.setContentText("Sono state perse le identità di questi. Cercali tra quelli evidenziati con un riquadro giallo");
                alert.setTitle("Perdita di dati");
                //alert.show();
               // GlobalContext.notProgrammedCalls.clear();
              //  MyUtils.write(GlobalContext.notProgrammedCalls, GlobalContext.operator.name());
            }
        try {
            database = (HashMap<UUID, Contatto>) MyUtils.read("database");
            try {
                index = (HashMap<String, LinkedList<UUID>>) MyUtils.read("indice");
                locationManager = (LocationManager) MyUtils.read("mondo");
            } catch (Exception e) {
                MyUtils.log(LogType.CAUSED, " recupero indici");
                MyUtils.log(LogType.MESSAGE, e);
                reconstruct();
            }
            res = Outcome.SUCCESS;
        } catch (Exception e){
            String recover = export(true);
            short attempts = 0;
            boolean result;
            do{
                result = autoRecover();
                attempts++;     //TODO: qua un log per fare un test doppio su deploy
            } while (attempts < 3 && !result);
            if(result){
                reconstruct();
                MyUtils.log(LogType.ERROR);
                MyUtils.log(LogType.MESSAGE, e + "\n recupero dati in fase 1");
                res = Outcome.SUCCESS;
            }else{
                Alert recoveryAlert = new Alert(Alert.AlertType.WARNING);
                recoveryAlert.setTitle("Attenzione!");
                recoveryAlert.setHeaderText("Si è attivato il sistema automatico di recupero dati");
                recoveryAlert.setContentText("L'ultima operazione potrebbe essere stata persa");
                try {
                    rebuild();
                    importa(recover);
                    if (database.isEmpty()){
                        MyUtils.log(LogType.ERROR);
                        MyUtils.log(LogType.MESSAGE, "Non sono stati recuperati i dati. Database vuoto");
                        recoveryAlert.setAlertType(Alert.AlertType.ERROR);
                        recoveryAlert.setHeaderText("I dati sono andati persi");
                        recoveryAlert.setContentText("Per recuperarli è necessario un ripristino da backup");
                        rebuild();
                    } else {
                        MyUtils.log(LogType.ERROR);
                        MyUtils.log(LogType.MESSAGE, e + "\n recupero dati in fase 2");
                        saveData();
                        res = Outcome.RECOVERED_SUCCESS;
                    }
                } catch (IOException ex) {
                    MyUtils.log(LogType.ERROR);
                    MyUtils.log(LogType.MESSAGE, e + "\n" + ex + "\n nessun dato recuperato :(");
                    recoveryAlert.setAlertType(Alert.AlertType.ERROR);
                    recoveryAlert.setContentText("Ma non è stato potuto recuperare niente");
                    rebuild();
                } catch (NullPointerException ex){
                    rebuild();
                    recoveryAlert.setAlertType(Alert.AlertType.ERROR);
                    recoveryAlert.setHeaderText("Database non trovato");
                    recoveryAlert.setContentText("E' necessario un ripristino da backup");
                }
                recoveryAlert.show();
                GlobalContext.notProgrammedCalls.clear();
                for(UUID i : callRemindersNoteId){
                    GlobalContext.notProgrammedCalls.add(recoverFromNoteId(i));
                }
            }
        }
        return res;
    }
    private static boolean autoRecover(){
        boolean ret;
        try {
            database = (HashMap<UUID, Contatto>) MyUtils.read("database");
            ret = true;
        } catch (Exception e) {
            ret = false;
        }
        return ret;
    }

    public static Outcome modifyEntry(UUID id, Contatto modifiedBean){
        Outcome ret = Outcome.FAILURE;
        try {
            update();
            Contatto oldBean = database.get(id);

            removeEntryFromLocationManager(id);
            removeEntryFromIndex(id);
            MyUtils.log(LogType.MODIFYENTRY, oldBean);
            if (!oldBean.getRagioneSociale().equals(modifiedBean.getRagioneSociale())){
                MyUtils.log(LogType.SPECIFYCHANGE, "ragione sociale", oldBean.getRagioneSociale(), modifiedBean.getRagioneSociale());
                oldBean.setRagioneSociale(modifiedBean.getRagioneSociale());
            }

            if (!oldBean.getPersonaRiferimento().equals(modifiedBean.getPersonaRiferimento())){
                MyUtils.log(LogType.SPECIFYCHANGE, "persona riferimento", oldBean.getPersonaRiferimento(), modifiedBean.getPersonaRiferimento());
                oldBean.setPersonaRiferimento(modifiedBean.getPersonaRiferimento());
            }
            if (!oldBean.getEmailReferente().equals(modifiedBean.getEmailReferente())){
                MyUtils.log(LogType.SPECIFYCHANGE, "email referente", oldBean.getEmailReferente(), modifiedBean.getEmailReferente());
                oldBean.setEmailReferente(modifiedBean.getEmailReferente());
            }
            if (!oldBean.getPaese().equals(modifiedBean.getPaese())){
                MyUtils.log(LogType.SPECIFYCHANGE, "paese", oldBean.getPaese(), modifiedBean.getPaese());
                oldBean.setPaese(modifiedBean.getPaese());
            }
            if (!oldBean.getRegione().equals(modifiedBean.getRegione())){
                MyUtils.log(LogType.SPECIFYCHANGE, "regione", oldBean.getRegione(), modifiedBean.getRegione());
                oldBean.setRegione(modifiedBean.getRegione());
            }
            if (!oldBean.getCitta().equals(modifiedBean.getCitta())){
                MyUtils.log(LogType.SPECIFYCHANGE, "citta", oldBean.getCitta(), modifiedBean.getCitta());
                oldBean.setCitta(modifiedBean.getCitta());
            }
            if (!oldBean.getIndirizzo().equals(modifiedBean.getIndirizzo())){
                MyUtils.log(LogType.SPECIFYCHANGE, "indirizzo", oldBean.getIndirizzo(), modifiedBean.getIndirizzo());
                oldBean.setIndirizzo(modifiedBean.getIndirizzo());
            }
            if (!oldBean.getNumeroCivico().equals(modifiedBean.getNumeroCivico())){
                MyUtils.log(LogType.SPECIFYCHANGE, "numero civico", oldBean.getNumeroCivico(), modifiedBean.getNumeroCivico());
                oldBean.setNumeroCivico(modifiedBean.getNumeroCivico());
            }
            if (!oldBean.getProvincia().equals(modifiedBean.getProvincia())){
                MyUtils.log(LogType.SPECIFYCHANGE, "provincia", oldBean.getProvincia(), modifiedBean.getProvincia());
                oldBean.setProvincia(modifiedBean.getProvincia());
            }
            if (!oldBean.getCap().equals(modifiedBean.getCap())){
                MyUtils.log(LogType.SPECIFYCHANGE, "cap", oldBean.getCap(), modifiedBean.getCap());
                oldBean.setCap(modifiedBean.getCap());
            }
            if (!oldBean.getPartitaIva().equals(modifiedBean.getPartitaIva())){
                MyUtils.log(LogType.SPECIFYCHANGE, "partita IVA", oldBean.getPartitaIva(), modifiedBean.getPartitaIva());
                oldBean.setPartitaIva(modifiedBean.getPartitaIva());
            }
            if (!oldBean.getCodiceFiscale().equals(modifiedBean.getCodiceFiscale())){
                MyUtils.log(LogType.SPECIFYCHANGE, "codice fiscale", oldBean.getCodiceFiscale(), modifiedBean.getCodiceFiscale());
                oldBean.setCodiceFiscale(modifiedBean.getCodiceFiscale());
            }
            if (!oldBean.getTelefono().equals(modifiedBean.getTelefono())){
                MyUtils.log(LogType.SPECIFYCHANGE, "telefono", oldBean.getTelefono(), modifiedBean.getTelefono());
                oldBean.setTelefono(modifiedBean.getTelefono());
            }
            if (!oldBean.getEmailGenereica().equals(modifiedBean.getEmailGenereica())){
                MyUtils.log(LogType.SPECIFYCHANGE, "email generica", oldBean.getEmailGenereica(), modifiedBean.getEmailGenereica());
                oldBean.setEmailGenereica(modifiedBean.getEmailGenereica());
            }
            if (!oldBean.getEmailCertificata().equals(modifiedBean.getEmailCertificata())){
                MyUtils.log(LogType.SPECIFYCHANGE, "email certificata", oldBean.getEmailCertificata(), modifiedBean.getEmailCertificata());
                oldBean.setEmailCertificata(modifiedBean.getEmailCertificata());
            }
            if (!oldBean.getSitoWeb().equals(modifiedBean.getSitoWeb())){
                MyUtils.log(LogType.SPECIFYCHANGE, "sito web", oldBean.getSitoWeb(), modifiedBean.getSitoWeb());
                oldBean.setSitoWeb(modifiedBean.getSitoWeb());
            }
            if (!oldBean.getTitolare().equals(modifiedBean.getTitolare())){
                MyUtils.log(LogType.SPECIFYCHANGE, "titolare", oldBean.getTitolare(), modifiedBean.getTitolare());
                oldBean.setTitolare(modifiedBean.getTitolare());
            }
            if (oldBean.getVolteContattati() != modifiedBean.getVolteContattati()){
                MyUtils.log(LogType.SPECIFYCHANGE, "volte contattati", oldBean.getVolteContattati(), modifiedBean.getVolteContattati());
                oldBean.setVolteContattati(modifiedBean.getVolteContattati());
            }
            if (!Objects.equals(oldBean.getTipoCliente(), modifiedBean.getTipoCliente())){
                MyUtils.log(LogType.SPECIFYCHANGE, "tipo cliente", oldBean.getTipoCliente(), modifiedBean.getTipoCliente());
                oldBean.setTipoCliente(modifiedBean.getTipoCliente());
            }
            if (!Objects.equals(oldBean.getInteressamento(), modifiedBean.getInteressamento())){
                MyUtils.log(LogType.SPECIFYCHANGE, "interessamento", oldBean.getInteressamento(), modifiedBean.getInteressamento());
                oldBean.setInteressamento(modifiedBean.getInteressamento());
            }
            if (!Objects.equals(oldBean.getUltimaChiamata(), modifiedBean.getUltimaChiamata())){
                MyUtils.log(LogType.SPECIFYCHANGE, "ultima chiamata", oldBean.getUltimaChiamata(), modifiedBean.getUltimaChiamata());
                oldBean.setUltimaChiamata(modifiedBean.getUltimaChiamata());
            }
            if (!Objects.equals(oldBean.getProssimaChiamata(), modifiedBean.getProssimaChiamata())){
                MyUtils.log(LogType.SPECIFYCHANGE, "prossima chiamata", oldBean.getProssimaChiamata(), modifiedBean.getProssimaChiamata());
                oldBean.setProssimaChiamata(modifiedBean.getProssimaChiamata());
            }
            if (!Objects.equals(oldBean.getCoinvolgimento(), modifiedBean.getCoinvolgimento())){
                MyUtils.log(LogType.SPECIFYCHANGE, "coinvolgimento", oldBean.getCoinvolgimento(), modifiedBean.getCoinvolgimento());
                oldBean.setCoinvolgimento(modifiedBean.getCoinvolgimento());
            }
            if (!Objects.equals(oldBean.getCheckpoint(), modifiedBean.getCheckpoint())){
                MyUtils.log(LogType.SPECIFYCHANGE, "Checkpoint", oldBean.getCheckpoint(), modifiedBean.getCheckpoint());
                oldBean.setCheckpoint(modifiedBean.getCheckpoint());
            }
            if (!Objects.equals(oldBean.getOperator(), modifiedBean.getOperator())){
                MyUtils.log(LogType.SPECIFYCHANGE, "Operator", oldBean.getOperator(), modifiedBean.getOperator());
                oldBean.setOperator(modifiedBean.getOperator());
            }
            if (!Objects.equals(oldBean.getNoteId(), modifiedBean.getNoteId())){
                MyUtils.log(LogType.SPECIFYCHANGE, "NoteId", oldBean.getNoteId(), modifiedBean.getNoteId());
                oldBean.setNoteId(modifiedBean.getNoteId());
            }
            if (!Objects.equals(oldBean.getAcquisizione(), modifiedBean.getAcquisizione())){
                MyUtils.log(LogType.SPECIFYCHANGE, "Acquisizione", oldBean.getAcquisizione(), modifiedBean.getAcquisizione());
                oldBean.setAcquisizione(modifiedBean.getAcquisizione());
            }
            if (!Objects.equals(oldBean.getCellulare(), modifiedBean.getCellulare())){
                MyUtils.log(LogType.SPECIFYCHANGE, "Cellulare", oldBean.getCellulare(), modifiedBean.getCellulare());
                oldBean.setCellulare(modifiedBean.getCellulare());
            }
            if (!Objects.equals(oldBean.getTelefono2(), modifiedBean.getTelefono2())){
                MyUtils.log(LogType.SPECIFYCHANGE, "Telefono2", oldBean.getTelefono2(), modifiedBean.getTelefono2());
                oldBean.setTelefono2(modifiedBean.getTelefono2());
            }


            if (GlobalContext.notProgrammedCalls.contains(id) && oldBean.getProssimaChiamata() != null){
                GlobalContext.notProgrammedCalls.remove(id);
            }
            indexNewEntry(oldBean, id);

            MyUtils.writeAll(database, index, locationManager);

            ret = Outcome.SUCCESS;
        } catch (NullPointerException e){
            if (modifyEntry(recoverFromNoteId(modifiedBean.getNoteId()), modifiedBean).equals(Outcome.SUCCESS)) ret = Outcome.RECOVERED_SUCCESS;
        } catch (Exception e){
            System.out.println("Errore durante la modifica di entry");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Operazione non compiuta");
            alert.setTitle("Attenzione");
            alert.setContentText("Riprovare");
            alert.show();
            MyUtils.log(LogType.ERROR);
            MyUtils.log(LogType.MESSAGE, e);
        }
        return ret;
    }
    public static Outcome setNextCall(UUID uuid, LocalDate date, InteressamentoStatus feedback, double coinvolgimento, boolean setAlsoProssimaChiamata, boolean isPersonalNote, UUID noteId){
        Outcome outcome = Outcome.FAILURE;
        try {
            //database = (HashMap<UUID, Contatto>) MyUtils.read("database");
            outcome = update();
            Contatto data = database.get(uuid);
            if (outcome.equals(Outcome.RECOVERED_SUCCESS)) {
                data = retriveEntry(recoverFromNoteId(noteId));
            }
            if (!isPersonalNote){
                data.incrementVolteContattati();
                if (coinvolgimento != -1)   data.setCoinvolgimento(coinvolgimento);
                if (feedback != null){
                    if (new InteressamentoComp().compare(data.getInteressamento(), feedback) < 0){
                        data.setInteressamento(feedback);
                    }
                    if (feedback.equals(InteressamentoStatus.LISTINO) && data.getCheckpoint() < 1){
                        data.setCheckpoint(1);
                    }else if (feedback.equals(InteressamentoStatus.CAMPIONE) && data.getCheckpoint() < 2){
                        data.setCheckpoint(2);
                    } else if (feedback.equals(InteressamentoStatus.CLIENTE) && data.getCheckpoint() < 3){
                        data.setCheckpoint(3);
                    }
                }
                Calendar now = Calendar.getInstance();
                int year = now.get(Calendar.YEAR);
                int month = now.get(Calendar.MONTH)+1;
                int day = now.get(Calendar.DAY_OF_MONTH);
                LocalDate today = LocalDate.of(year,month,day);
                data.setUltimaChiamata(today);
            }
            if (setAlsoProssimaChiamata) {
                data.setProssimaChiamata(date);
            }
            modifyEntry(uuid, data);
        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Operazione non compiuta");
            alert.setTitle("Attenzione");
            alert.setContentText("Riprovare");
            alert.show();
            System.out.println("Errore durante setNextCall in DBManager");
            MyUtils.log(LogType.ERROR);
            MyUtils.log(LogType.MESSAGE, e);
        }
        return outcome;
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
        System.out.println("Indici ricostruiti");
    }

    private static void rebuild(){
        database = new HashMap<>();
        index = new HashMap<>();
        locationManager = new LocationManager();
        System.out.println("Database non trovato... Scrittura di uno nuovo");
        MyUtils.writeAll(database, index, locationManager);
        System.out.println("Database ricostruito");
    }

    public static LinkedList<UUID> getCallList(LocalDate fromWhen) {
        LinkedList<UUID> callList = new LinkedList<>();
        for (Contatto i : database.values()){
            try {
                if (i.getProssimaChiamata().equals(fromWhen) && GlobalContext.operator == i.getOperator()) {
                    callList.add(i.getId());
                }
            } catch (NullPointerException ignored)  {}
        }
        return callList;
    }

    public static String export(boolean isBackup) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Esportazione");
        alert.setContentText("Esportazione avvenuta");
        File file;
        String recoverFile = "";
        try {
            if (isBackup) {
                Calendar now = Calendar.getInstance();
                recoverFile = "Backups\\" + now.get(Calendar.DAY_OF_MONTH) + "-" + (now.get(Calendar.MONTH)+1) + "-" + now.get(Calendar.YEAR) + "_" + now.get(Calendar.HOUR_OF_DAY) + "x" + now.get(Calendar.MINUTE) + "x" + now.get(Calendar.SECOND) + ".txt";
                file = new File(recoverFile);
            } else {
                file = new File("Importa.txt");
            }
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
                }catch (Exception e) {  bw.write(InteressamentoStatus.BLANK.name() + ";");  }
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
                bw.write(i.getCoinvolgimento()+";");
                bw.write(i.getAcquisizione()+";");
                bw.write(i.getCheckpoint()+";");
                bw.write(i.getTelefono2()+";");
                bw.write(i.getCellulare()+";");
                bw.newLine();
            }
            bw.flush();
            bw.close();
        }
        catch(IOException e) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Esportazione fallita");
            MyUtils.log(LogType.ERROR);
            MyUtils.log(LogType.MESSAGE, e);
        } catch (NullPointerException ignored) {}
        if (!isBackup) {
            alert.show();
        }
        return recoverFile;
    }

    public static void notes() {
        for (Contatto i : database.values()){
            if (i.getOperator() == Operatori.SANTOLO)   {
                i.setOperator(Operatori.TERESA);
                modifyEntry(i.getId(), i);
            }
        }
        MyUtils.writeAll(database, index, locationManager);
        /*for(Contatto i : database.values()){
            try {
                NoteManager nm = new NoteManager();
                Document doc = nm.readXml(""+i.getNoteId());
                doc.getDocumentElement().normalize();
                NodeList nodeList = doc.getElementsByTagName("chiamata");
                for (int j = 0; j < nodeList.getLength(); j++){
                    Node node = nodeList.item(j);
                    if (node.getNodeType() == Node.ELEMENT_NODE){
                        Element e = (Element) node;
                        e.setAttribute("checkpoint", "0");
                    }
                }
                nm.writeXml(doc, i.getNoteId()+"");
            } catch (Exception e) {
                MyUtils.log(LogType.ERROR);
                MyUtils.log(LogType.MESSAGE, e);
                System.out.println("Errore con " + i);
            }
        }*/
    }

    public static void reduceVolteContattati(UUID entryID) {
        update();
        retriveEntry(entryID).setVolteContattati(retriveEntry(entryID).getVolteContattati()-1);
        MyUtils.write(database, "database");
    }

    public static int getOperatorTotalContacts(Operatori operatore){
        int ret = 0;
        for (Contatto i : database.values()){
            if (i.getOperator().equals(operatore))  ret++;
        }
        return ret;
    }

    public static void changeInteressamento(UUID entryID, InteressamentoStatus interessamentoStatus) {
        removeTagFromIndex(entryID, database.get(entryID).getInteressamento().name());
        insertTagInIndex(interessamentoStatus.name(), entryID);
        database.get(entryID).setInteressamento(interessamentoStatus);
    }

    public static LinkedList<Contatto> importa(String importPath) throws IOException {
        File file = new File(importPath);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String in = br.readLine();
        Contatto newEntryFromFile;
        LinkedList<Contatto> failedImported = new LinkedList<>();
        do{
            newEntryFromFile = new Contatto();
            try {
                int subStringStart = 0;
                int subStringEnd = in.indexOf(";");
                String subString;
                for (int i = 0; i <= 28; i++) {
                    subString = in.substring(subStringStart, subStringEnd);
                    fillAttribute(i, newEntryFromFile, subString);

                    subStringStart = subStringEnd + 1;
                    subStringEnd = in.indexOf(";", subStringStart);
                }
                System.out.println("   Inserimento di " + newEntryFromFile);
                DBManager.saveEntry(newEntryFromFile, true);
            }catch (Exception e)    {
                failedImported.add(newEntryFromFile);}
        }while((in = br.readLine()) != null );
        br.close();
        return failedImported;
    }

    private static void fillAttribute (int index, Contatto bean, String attribute){
        switch (index){
            case 0:
                bean.setRagioneSociale(attribute);
                break;
            case 1:
                bean.setPersonaRiferimento(attribute);
                break;
            case 2:
                bean.setEmailReferente(attribute);
                break;
            case 3:
                bean.setTelefono(attribute);
                break;
            case 4:
                bean.setPaese(attribute);
                break;
            case 5:
                bean.setRegione(attribute);
                break;
            case 6:
                bean.setCitta(attribute);
                break;
            case 7:
                bean.setIndirizzo(attribute);
                break;
            case 8:
                bean.setNumeroCivico(attribute);
                break;
            case 9:
                bean.setProvincia(attribute);
                break;
            case 10:
                bean.setCap(attribute);
                break;
            case 11:
                if(attribute.isEmpty()) {
                    bean.setInteressamento(InteressamentoStatus.BLANK);
                } else{
                    bean.setInteressamento(InteressamentoStatus.valueOf(attribute));
                }
                break;
            case 12:
                if (attribute.isEmpty()){
                    bean.setTipoCliente(TipoCliente.BLANK);
                } else {
                    bean.setTipoCliente(TipoCliente.valueOf(attribute));
                }
                break;
            case 13:
                bean.setPartitaIva(attribute);
                break;
            case 14:
                bean.setCodiceFiscale(attribute);
                break;
            case 15:
                bean.setTitolare(attribute);
                break;
            case 16:
                bean.setEmailGenereica(attribute);
                break;
            case 17:
                bean.setEmailCertificata(attribute);
                break;
            case 18:
                bean.setSitoWeb(attribute);
                break;
            case 19:
                if(!attribute.isBlank()){
                    System.out.println(UUID.fromString(attribute));
                    bean.setNoteId(UUID.fromString(attribute));
                }
                break;
            case 20:
                if (attribute.isEmpty()) {
                    bean.setOperator(Operatori.BLANK);
                } else {
                    bean.setOperator(Operatori.valueOf(attribute));
                }
                break;
            case 21:
                bean.setVolteContattati(Integer.parseInt(attribute));
                break;
            case 22:
                LocalDate t;
                try{
                    t = LocalDate.parse(attribute);
                } catch (Exception e){
                    t = null;
                }
                bean.setUltimaChiamata(t);
                break;
            case 23:
                LocalDate j;
                try{
                    j = LocalDate.parse(attribute);
                }catch (Exception e){
                    j = null;
                }
                bean.setProssimaChiamata(j);
                break;
            case 24:
                double c;
                if (attribute.isEmpty() || Double.parseDouble(attribute) == 0.0){
                    c = 1;
                }else {
                    c = Double.parseDouble(attribute);
                }
                bean.setCoinvolgimento(c);
                break;
            case 25:
                LocalDate o = LocalDate.now();
                if (attribute.isEmpty()){
                    o = o.minusMonths(1);
                }else {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
                    o = LocalDate.parse(attribute, formatter);
                }
                bean.setAcquisizione(o);
                break;
            case 26:
                bean.setCheckpoint(Integer.parseInt(attribute));
                break;
            case 27:
                bean.setTelefono2(attribute);
                break;
            case 28:
                bean.setCellulare(attribute);
                break;
        }
    }
    public static UUID recoverFromNoteId(UUID noteId){
        UUID ret = null;
        for (Contatto i : database.values()){
            if(i.getNoteId().equals(noteId)) ret = i.getId();
        }
        return ret;
    }
    public  static boolean isNull(){
        return database == null;
    }
}
