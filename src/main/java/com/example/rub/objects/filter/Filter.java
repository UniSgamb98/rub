package com.example.rub.objects.filter;

import com.example.rub.enums.TagCategories;

import java.io.Serializable;
import java.util.LinkedList;

public class Filter implements Serializable {
    private final TagCategories category;
    private final LinkedList<String> tags;

    public Filter (TagCategories category){
        tags = new LinkedList<>();
        this.category = category;
    }

    public TagCategories getCategory() {
        return category;
    }
    public LinkedList<String> getTags(){
        return tags;
    }
    public void addTag(String tag){
        tags.add(tag);
    }
    public void removeTag(String tag) {
        tags.remove(tag);
    }
    public int getSize(){
        return tags.size();
    }
}
