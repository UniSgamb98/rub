package com.example.rub.functionalities;

import com.example.rub.beans.Contatto;
import com.example.rub.beans.DeletedContatto;
import com.example.rub.functionalities.locations.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

public abstract class TagsManager {
    protected static HashMap<UUID, Contatto> database;
    protected static HashMap<String, LinkedList<UUID>> index;
    protected static LocationManager locationManager;
    protected static LinkedList<DeletedContatto> trashcan;

    protected static void indexNewEntry(Contatto newEntry, UUID uuid){    //indicizza un Contatto che appena stato creato
        System.out.println("    Aggiornamento tag di filtraggio");
        insertTagInIndex(newEntry.getPaese(), uuid);
        insertTagInIndex(newEntry.getCitta(), uuid);
        insertTagInIndex(newEntry.getRegione(), uuid);
        insertTagInIndex(newEntry.getProvincia(), uuid);
        insertTagInIndex(newEntry.getOperator().name(), uuid);
        insertTagInIndex(newEntry.getInteressamento().name(), uuid);
        insertTagInIndex(newEntry.getTipoCliente().name(), uuid);

        insertNewEntryInLocationManager(newEntry);
    }
    protected static void removeEntryFromIndex(UUID id){
        Contatto temp = database.get(id);
        removeTagFromIndex(id, temp.getPaese());
        removeTagFromIndex(id, temp.getRegione());
        removeTagFromIndex(id, temp.getProvincia());
        removeTagFromIndex(id, temp.getCitta());
        removeTagFromIndex(id, temp.getTipoCliente().name());
        removeTagFromIndex(id, temp.getInteressamento().name());
    }
    private static void removeTagFromIndex(UUID id, String tag){
        if (index.get(tag).size() == 1){
            index.remove(tag);
        } else {
            index.get(tag).remove(id);
        }
    }

    protected static void insertNewEntryInLocationManager (Contatto newEntry){
        State state = new State(newEntry.getPaese());
        Region region = new Region(newEntry.getRegione());
        City city = new City(newEntry.getCitta());
        region.addCity(city);
        state.addRegion(region);
        try {
            if (locationManager.contains(newEntry.getPaese())){
                State stateContained = (State) locationManager.getSubLocality(newEntry.getPaese());
                if (stateContained.contains(newEntry.getRegione())){
                    Region regionContained = (Region) stateContained.getSubLocality(newEntry.getRegione());
                    if (!regionContained.contains(newEntry.getCitta())) {
                        regionContained.addCity(city);
                    }
                }else {
                    stateContained.addRegion(region);
                }
            } else {
                locationManager.addState(state);
            }
        } catch (Exception e) {
            System.out.println("Errore durante l'inserimento di newEntry in LocationManager");
        }
    }
    protected static void removeEntryFromLocationManager(UUID id){
        String paese = database.get(id).getPaese();
        String regione = database.get(id).getRegione();
        String citta = database.get(id).getCitta();
        boolean nc = index.get(citta).size() == 1;
        if (nc){
            locationManager.getSubLocality(paese).getSubLocality(regione).removeSubLocality(citta);
            if (locationManager.getSubLocality(paese).getSubLocality(regione).getSubLocalities().isEmpty()){
                locationManager.getSubLocality(paese).removeSubLocality(regione);
                if (locationManager.getSubLocality(paese).getSubLocalities().isEmpty()){
                    locationManager.removeSubLocality(paese);
                }
            }
        }
    }
    private static void insertTagInIndex(String tag, UUID uuid){    //Aggiunge il tag l'uuid sotto la voce del tag fornito, se non trovato crea un tag
        if (index.containsKey(tag)) {   //TAG APPESO
            System.out.println("       Tag " + tag + " trovato in indice.");
            LinkedList<UUID> tagsUUID = index.get(tag);
            tagsUUID.add(uuid);
            index.put(tag, tagsUUID);
        } else {
            System.out.println("       Creazione di un nuovo tag: " + tag);    //TAG AGGIUNTO
            LinkedList<UUID> temp = new LinkedList<>();
            temp.add(uuid);
            index.put(tag, temp);
        }
    }
}
