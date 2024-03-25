package com.example.rub.functionalities;

import com.example.rub.beans.Contatto;
import com.example.rub.functionalities.locations.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

public abstract class TagsManager {
    protected static HashMap<UUID, Contatto> database;
    protected static HashMap<String, LinkedList<UUID>> index;
    protected static LocationManager locationManager;

    protected static void indexNewEntry(Contatto newEntry, UUID uuid){    //indicizza un Contatto che appena stato creato
        System.out.println("Aggiornamento tag di filtraggio");
        insertTagInIndex(newEntry.getPaese(), uuid);
        insertTagInIndex(newEntry.getCitta(), uuid);
        insertTagInIndex(newEntry.getRegione(), uuid);
        insertTagInIndex(newEntry.getProvincia(), uuid);

        insertNewEntryInLocationManager(newEntry);
    }

    protected static void insertNewEntryInLocationManager (Contatto newEntry){
        State state = new State(newEntry.getPaese());
        Region region = new Region(newEntry.getRegione());
        City city = new City(newEntry.getCitta());
        region.addCity(city);
        state.addRegion(region);
        try {
            LocationManager manager = (LocationManager) MyUtils.read("mondo");
            if (manager.contains(newEntry.getPaese())){
                State stateContained = (State) manager.getSubLocality(newEntry.getPaese());
                if (stateContained.contains(newEntry.getRegione())){
                    Region regionContained = (Region) stateContained.getSubLocality(newEntry.getRegione());
                    regionContained.addCity(city);
                }else {
                    stateContained.addRegion(region);
                }
            } else {
                manager.addState(state);
            }
            MyUtils.write(manager, "mondo");
        } catch (Exception e) {
            System.out.println("Errore durante l'inserimento di newEntry in LocationManager");
        }
    }
    protected static void changeIndexEntry(UUID id, String oldTag, String newTag){  //RIMOZIONE DEL TAG DA INDEX E DA GROUPEDTAGS SE NECESSARIO
        if (index.get(oldTag).size() == 1){
            index.remove(oldTag);
        } else {
            index.get(oldTag).remove(id);
        }
        insertTagInIndex(newTag, id);    //REINSERIMENTO DEL TAG NUOVO RICEVUTO
    }
    private static void insertTagInIndex(String tag, UUID uuid){    //Aggiunge il tag l'uuid sotto la voce del tag fornito, se non trovato crea un tag
        if (index.containsKey(tag)) {   //TAG APPESO
            System.out.println("   Tag " + tag + " trovato in indice.");
            LinkedList<UUID> tagsUUID = index.get(tag);
            tagsUUID.add(uuid);
            index.put(tag, tagsUUID);
        } else {
            System.out.println("   Creazione di un nuovo tag: " + tag);    //TAG AGGIUNTO
            LinkedList<UUID> temp = new LinkedList<>();
            temp.add(uuid);
            index.put(tag, temp);
        }
    }
    private static void removeTag(){
        //TODO remove in indice
    }
}
