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
        insertTag(TagCategories.PAESE, tagPaese, uuid);
        insertTag(TagCategories.CITTA, tagCitta, uuid);
    }
    protected static void changeIndexEntry(UUID id, TagCategories category, String oldTag, String newTag){  //RIMOZIONE DEL TAG DA INDEX E DA GROUPEDTAGS SE NECESSARIO
        if (getTagSizeInIndex(oldTag) == 1){
            removeTagInGroupedTags(category, oldTag);
        }
        if (index.get(oldTag).size() == 1){
            index.remove(oldTag);
        } else {
            index.get(oldTag).remove(id);
        }
        insertTag(category, newTag, id);    //REINSERIMENTO DEL TAG NUOVO RICEVUTO
    }
    private static void insertTag(TagCategories tagCategory, String tag, UUID uuid){    //Aggiunge il tag l'uuid sotto la voce del tag fornito, se non trovato crea un tag
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

            addTagInGroupedTags(tagCategory, tag);
        }
    }
    private static void removeTag(){
        //TODO (in indice e grouped tag)
    }
    private static void addTagInGroupedTags (TagCategories tagCategory, String tag) {
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
    private static void removeTagInGroupedTags (TagCategories tagCategory, String tag){
        int i = 0;
        boolean filterFound = false;

        while(!filterFound){
            if(groupedTags.get(i).getCategory() == tagCategory){
                if (groupedTags.get(i).getSize() == 1){  //RIMOZIONE DELL'INTERO FILTER SE RISULTA COME ULTIMO
                    groupedTags.remove(groupedTags.get(i));
                } else {
                    groupedTags.get(i).removeTag(tag);   //RIMOZIONE DEL TAG IN FILTER
                }
                filterFound = true;
            }
        }
    }
    private static int getTagSizeInIndex (String tag){
        return index.get(tag).size();
    }



   /* protected static int getTagSize(TagCategories category){
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
    }*/
}
