package com.pixelkaveman.firestoreexample;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.PropertyName;

public class Note {

    @PropertyName("id")
    private String documentId;

    @PropertyName("title")
    private String title;

    @PropertyName("description")
    private String description;

    @PropertyName("priority")
    private int priority;

    public Note() {
        //public no-arg constructor needed
    }


    public Note(String title, String description , int priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    @Exclude
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
