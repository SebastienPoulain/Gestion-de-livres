package com.example.livre3;

import androidx.annotation.NonNull;

import java.util.Date;

/**
 * @author emilie cormier houle, sébastien poulain,jérémy lampron
 */
public class Livre {

    @NonNull
    private int id, idC, annotation;
    private String title;
    private String author;
    private String publisher;
    private String edition;
    private String barcode;
    private String imgPath;
    private Double sellingPrice;
    private Double customerPrice;
    private String expireDate;


    public Livre(String titre, String auteur, String edition, String editeur, String codeBarre,String imgPath){
        this.title = titre;
        this.author = auteur;
        this.publisher = editeur;
        this.edition=edition;
        this.barcode =codeBarre;
        this.imgPath = imgPath;
    }
    public Livre(Integer id,String titre, String auteur, String edition, String editeur, String codeBarre,String imgPath){
        this.id = id;
        this.title = titre;
        this.author = auteur;
        this.publisher = editeur;
        this.edition=edition;
        this.barcode =codeBarre;
        this.imgPath = imgPath;
    }
    public Livre(Integer id,Double sellingPrice,String imgPath,Integer annotation){
        this.id = id;
        this.sellingPrice = sellingPrice;
        this.imgPath = imgPath;
        this.annotation = annotation;
    }
    public Livre(Integer id,String titre, String auteur, String edition, String editeur, String codeBarre,String imgPath, Integer idC, Double prix, Integer annotation, String expireDate){
        this.id = id;
        this.title = titre;
        this.author = auteur;
        this.publisher = editeur;
        this.edition=edition;
        this.barcode =codeBarre;
        this.imgPath = imgPath;
        this.idC = idC;
        this.customerPrice = prix;
        this.annotation = annotation;
        this.expireDate = expireDate;
    }
    public Livre(Integer id,String titre, String auteur, String edition, String editeur, String codeBarre,String imgPath, Integer idC){
        this.id = id;
        this.title = titre;
        this.author = auteur;
        this.publisher = editeur;
        this.edition=edition;
        this.barcode =codeBarre;
        this.imgPath = imgPath;
        this.idC = idC;

    }

    public Double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getCodeBarre() {
        return barcode;
    }

    public void setCodeBarre(String codeBarre) {
        this.barcode = codeBarre;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public int getId() {
        return id;
    }

    public String getTitre() {
        return title;
    }

    public void setTitre(String titre) {
        this.title = titre;
    }


    public String getAuteur() {
        return author;
    }

    public String getImgpath() {
        return imgPath;
    }

    public String getEditeur() {
        return publisher;
    }

    public Double getPrix() {return customerPrice; }

    public String getExpireDate(){
        return expireDate;
    }

    public int getAnnotation(){return annotation;}

    public int getIdC() {
        return idC;
    }

    public void setIdC(int idC) {
        this.idC = idC;
    }

    public void setId(int id) {
        this.id = id;
    }



    public void setNom(String nom) {
        this.title = nom;
    }

    public void setAuteur(String auteur) {
        this.author = auteur;
    }

    public void setEditeur(String editeur) {
        this.publisher = editeur;
    }

    public void setImgpath(String imgpath) {
        this.imgPath = imgpath;
    }

}
