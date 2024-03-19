package com.example.rub.functionalities;

import com.example.rub.beans.Contatto;
import com.example.rub.enums.TagCategories;
import com.example.rub.objects.filter.Filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

public abstract class TagsManager {
    protected static HashMap<UUID, Contatto> database;
    protected static HashMap<String, LinkedList<UUID>> index;
    protected static ArrayList<Filter> groupedTags;

    protected static void indexNewEntry(Contatto beans, UUID uuid){    //indicizza un Contatto che appena stato creato
        String tagPaese = beans.getPaese();
        String tagCitta = beans.getCitta();

        System.out.println("Aggiornamento tag di filtraggio");
        insertTagInIndex(TagCategories.PAESE, tagPaese, uuid);
        insertTagInIndex(TagCategories.CITTA, tagCitta, uuid);
    }
    protected static void changeIndexEntry(UUID id, TagCategories category, String oldTag, String newTag){
        index.get(oldTag).remove(id);
        insertTagInIndex(category, newTag, id);
    }
    private static void insertTagInIndex(TagCategories tagCategory, String tag, UUID uuid){    //Aggiunge il tag l'uuid sotto la voce del tag fornito, se non trovato crea un tag
        if (index.containsKey(tag)) {
            System.out.println("   Tag " + tag + " trovato in indice.");
            LinkedList<UUID> tagsUUID = index.get(tag);
            tagsUUID.add(uuid);
            index.put(tag, tagsUUID);
        } else {
            System.out.println("   Creazione di un nuovo tag: " + tag);    //AGGIUNTA TAG IN INDICE
            LinkedList<UUID> temp = new LinkedList<>();
            temp.add(uuid);
            index.put(tag, temp);

            //TODO: CREDO CHE QUESTA PARTE VADA MESSA IN UNA FUNZIONE A PARTE
            boolean filterFound = false;
            for (Filter i : groupedTags){   //APPENDO TAG IN GROUPED TAGS
                if(i.getCategory() == tagCategory){
                    if (!i.getTags().contains(tag)) {
                        i.addTag(tag);
                    }
                    filterFound = true;
                }
            }
            if (!filterFound){
                Filter temp2 = new Filter(tagCategory);     //AGGIUNTA NUOVO TAG IN GROUPED TAGS
                temp2.addTag(tag);
                groupedTags.add(temp2);
            }
        }
    }
    protected static int getTagSize(TagCategories category){
        int ret = 0;
        switch (category){
            case PAESE:
                ret = groupedTags.get(0).getTags().size();
                break;
            case CITTA:
                ret = groupedTags.get(1).getTags().size();
                break;
        }
        return ret;
    }
}
