package com.example.livre3;
import java.util.Date;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Cette interface permet de construire des appels qui seront envoyés au serveur par le bias de fichier php
 * @author emilie cormier houle, sébastien poulain,jérémy lampron
 */

public interface InterfaceServeur {
    /**
     * permet l'ajout de livres
     *
     */

    @POST("AGECTR/php/SelectLivre.php")
    @FormUrlEncoded
    Call<ResponseBody> livreDATA(@Field("title") String title, @Field("author") String author, @Field("publisher") String publisher, @Field("edition") String edition , @Field("barcode") String barcode,@Field("customerPrice") String customerPrice,@Field("idCustomer") Integer idCustomer,@Field("imgPath")String imgPath);

    /**
     *créer le compte
     */
    @POST("AGECTR/php/userdata.php")
    @FormUrlEncoded
    Call<ResponseBody> userdata(@Field("referenceCode") String referenceCode,@Field("firstName") String firstName,@Field("lastName") String lastName,@Field("phoneNumber") String phoneNumber,@Field("email") String email,@Field("password") String password);

    /**
     *  modifier le livre qui nous appartient
     */
    @POST("AGECTR/php/updateConcession.php")
    @FormUrlEncoded
    Call<ResponseBody> updateConcession(@Field("id") Integer id, @Field("modif_prix") Double prix, @Field ("annotation") Integer annotation, @Field ("imgPath") String imgPath);

    /**
     * envoie un email quand on a perdu son mot de passe
     * @param email
     *
     */
    @POST("AGECTR/php/forgot.php")
    @FormUrlEncoded
    Call<ResponseBody> forgot(@Field("email") String email);

    /**
     * permet de modifier son profil
     * @param id
     * @param firstName
     * @param lastName
     * @param email
     * @param ancienEmail
     * @param phoneNumber
     * @param password
     *
     */
    @POST("AGECTR/php/modif_info.php")
    @FormUrlEncoded
    Call<ResponseBody> modif_info(@Field("id") Integer id, @Field("firstName") String firstName,@Field("lastName") String lastName,@Field("email") String email,@Field("ancienEmail") String ancienEmail,@Field("phoneNumber") String phoneNumber, @Field("password") String password);

    /**
     * permet de modifier le mot de passse
     * @param id
     * @param mdpa
     * @param mdp
     *
     */
    @POST("AGECTR/php/modif_password.php")
    @FormUrlEncoded
    Call<ResponseBody> modif_password(@Field("id") Integer id, @Field("mdpa") String mdpa, @Field("mdp") String mdp);

    /**
     * permet de voir les livre qui nous appartienne
     * @param id
     * @return une liste de livre qui appartienne à l'utilisateur connecté
     */
    @POST("AGECTR/php/getMesLivres.php")
    @FormUrlEncoded
    Call<List<Livre>> getMesLivres(@Field("id") Integer id);

    /**
     * permet de voir les livre qu'on a réserver
     * @param id
     * @return la liste de livres que l'utilisateur a réservé
     */
    @POST("AGECTR/php/getMesReservation.php")
    @FormUrlEncoded
    Call<List<Livre>> getMesReservation(@Field("id") Integer id);

    /**
     * permet de faire un recherche selon le titre , l'édition,etc
     * @param title
     * @param author
     * @param publisher
     * @param edition
     * @param barcode
     * @return la liste des livres qui correspont aux critères de recherche
     */
    @POST("AGECTR/php/rechercherLivre.php")
    @FormUrlEncoded
    Call<List<Livre>> RechercherLivre(@Field("title") String title, @Field("author") String author, @Field("publisher") String publisher, @Field("edition") String edition, @Field("barcode")String barcode);

    /**
     * permet d'avoir les informations des concessions selon le livre sélectionné
     * @param id
     * @return la liste des concessions selon le livre sélectionné
     */
    @POST("AGECTR/php/getConcessionLivre.php")
    @FormUrlEncoded
    Call<List<Livre>> getConcessionLivre(@Field("id") Integer id);

    /**
     * renouvelle une concession
     * @param id
     *
     */
    @POST("AGECTR/php/renouvelerConcession.php")
    @FormUrlEncoded
    Call<ResponseBody> renouvelerConcession(@Field("id") Integer id);

    /**
     * reserve le livre
     * @param idCons
     * @param idCon
     *
     */
    @POST("AGECTR/php/setReservation.php")
    @FormUrlEncoded
    Call<ResponseBody> setReservation(@Field("idCons") Integer idCons,@Field("idCon") Integer idCon );

    /**
     * permet de supprimer une reservation
     * @param idCons
     *
     */
    @POST("AGECTR/php/setReservation.php")
    @FormUrlEncoded
    Call<ResponseBody> supReservation(@Field("idCons") Integer idCons);

    /**
     * permet de savoir qui est connecter
     * @param email
     * @param password
     * @return l'utilisateur connecté
     */
    @POST("AGECTR/php/getUserlogin.php")
    @FormUrlEncoded
    Call<User> getUserLogin(@Field("email") String email, @Field("password") String password);

    /**
     * permet de rester connecter
     * @param email
     * @return l'utilisateur qui est déjà connecté
     */
    @POST("AGECTR/php/reconnect.php")
    @FormUrlEncoded
    Call<User> Reconnecter(@Field("email") String email);

    /**
     * upload les images
     * @param requete
     * @param image
     *
     */
    @Multipart
    @POST("AGECTR/php/uploadfile.php")
    Call<ResponseBody> upload(
            @Part("requete") RequestBody requete,
            @Part MultipartBody.Part image
    );

    /**
     * donner le livre
      * @param id
     *
     */
    @POST("AGECTR/php/donConcession.php")
    @FormUrlEncoded
    Call<ResponseBody> donConcession(@Field("id") Integer id);

    /**
     * supprime la concession
     * @param id
     *
     */
    @POST("AGECTR/php/supConcession.php")
    @FormUrlEncoded
    Call<ResponseBody> supConcession(@Field("id") Integer id);

    /**
     * sélectionne un livre a partir d'un code barrre
     * @param barecode
     * @return les informations du livre selon le code-barres
     */
    @POST("AGECTR/php/SelectLivre.php")
    @FormUrlEncoded
    Call<CodeBarre>SelectCodeBarre(@Field("barcode")String barecode);

    /**
     * ajouter une concession
     * @param customerPrice
     * @param idBook
     * @param idCustomer
     * @param imgPath
     * @param annotation
     *
     */
    @POST("AGECTR/php/SelectLivre.php")
    @FormUrlEncoded
    Call<ResponseBody>AjoutConcession(@Field("customerPrice") String customerPrice,@Field("idBook") Integer idBook,@Field("idCustomer") Integer idCustomer,@Field("imgPath")String imgPath,@Field("annotation") Integer annotation);
}

