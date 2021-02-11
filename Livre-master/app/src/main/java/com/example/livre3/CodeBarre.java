package com.example.livre3;

/**
 * @author Émilie Cormier Houle
 * classe qui permet de créer un objet de type CodeBarre
 */

public class CodeBarre {
    private int id;
    private String title;
    private String author;
    private String publisher;
    private String edition;

    public CodeBarre(int id, String titre, String auteur, String edition, String editeur){
        this.id=id;
        this.title = titre;
        this.author = auteur;
        this.publisher = editeur;
        this.edition=edition;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getEditeur() {
        return publisher;
    }

    public void setEditeur(String editeur) {
        this.publisher = editeur;
    }

    public String getAuteur() {
        return author;
    }

    public void setAuteur(String auteur) {
        this.author = auteur;
    }

    public String getTitre() {
        return title;
    }

    public void setTitre(String titre) {
        this.title = titre;
    }
}
