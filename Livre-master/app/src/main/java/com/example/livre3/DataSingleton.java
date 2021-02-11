package com.example.livre3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * @author Jérémy Lampron et Émilie Cormier Houle et Sébastien Poulain
 * cette classe permet d'accéder à des informations globalement dans l'application
 */


public class DataSingleton {
    private static DataSingleton instance;
    private User user = new User(-10, "", "", "","");
    private boolean etatConnexion = false;
    private InterfaceServeur server = RetrofitInstance.getInstance().create(InterfaceServeur.class);
    private Context context;
    int idLivre;
    String code = "";
    private static final DataSingleton ourInstance = new DataSingleton();
    public static DataSingleton getInstance() {
        return ourInstance;
    }
    private DataSingleton() {
    }

    /**
     * Cette fonction retourne l'état de connexion
     * @return etatConnexion
     */
    public boolean getEtatConnexion(){
        return etatConnexion;
    }

    /**
     * Cette fonction déconnecte l'utilisateur connecté
     */
    public void deconnexion(){
        user.setNom("");
        user.setPrenom("");
        user.setEmail("");
        user.setTel("");
        setConnectedUser(user);
        etatConnexion = false;

        SharedPreferences pref = getContext().getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }




    private User connectedUser = null;

    public User getConnectedUser() {
        return connectedUser;
    }

    public void setConnectedUser(User connectedUser) {
        this.connectedUser = connectedUser;
        etatConnexion = true;
    }

    public void setIdLivre(int id){
        this.idLivre=id;
    }
    public int getIdLivre(){
        return idLivre;
    }

    /**
     * cette fonction retourne l'instance du singleton,elle en créée une nouvelle si elle n'existe pas
     * @return instance
     */
    private static DataSingleton Instance() {
        if (instance == null) {
            instance = new DataSingleton();
        }
        return instance;
    }

    /**
     * Cette fonction hash les mot de passe avant de les envoyer dans la BD
     * @param password
     * @return mot de passe hashé
     */
    public static String getSha256Hash(String password) {
        try {
            MessageDigest digest = null;
            try {
                digest = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e1) {
                e1.printStackTrace();
            }
            digest.reset();
            return bin2hex(digest.digest(password.getBytes()));
        } catch (Exception ignored) {
            return null;
        }
    }

    /**
     * Cette fonction transforme les caractères binaires en hexadécimal (utilisé pour le hashage de mot de passe)
     * @param data
     * @return mot de passe en hexadécimal
     */
    private static String bin2hex(byte[] data) {
        StringBuilder hex = new StringBuilder(data.length * 2);
        for (byte b : data)
            hex.append(String.format("%02x", b & 0xFF));
        return hex.toString();
    }

    /**
     * Initialise le contexte
     * @param context
     */
    public void initContext(Context context){
        this.context = context.getApplicationContext();
    }

    /**
     * Retourne le contexte
     * @return context
     */
    public Context getContext(){
        return context;
    }



    /**
     * Cette fonction ajoute l'utilisateur a la BD
     * @param matricule
     * @param prenom
     * @param nom
     * @param tel
     * @param email
     * @param passwd
     */
    public void addUser(String matricule, String prenom, String nom, String tel, String email, String passwd){

        Call<ResponseBody> call = server.userdata(matricule.trim(), prenom.trim(), nom.trim(), tel.trim(), email.trim(), getSha256Hash(passwd));

        call.enqueue(new Callback<ResponseBody>() {
            /**
             * réponse du serveur lors de l'ajout d'un utilisateur
             * @param call
             * @param response
             */
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String reponse = "";

                try {
                     reponse = response.body().string();


                } catch (Exception e) {
                    e.printStackTrace();
                }

                if( reponse.equals("Le matricule entré existe déjà") || reponse.equals("L'adresse courriel entrée existe déjà")){
                    Toast.makeText(getContext(), reponse, Toast.LENGTH_LONG).show();

                }
                else{
                    Toast.makeText(getContext(), reponse, Toast.LENGTH_LONG).show();


                    Intent intent = new Intent();
                    intent.setAction("com.info.lesreceivers.MON_ACTION");
                    getContext().sendBroadcast(intent);

                }

            }

            /**
             * affichage du messge d'erreur si problème au niveau du serveur
             * @param call
             * @param t
             */
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(),"TOAST ON FAILURE " + t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }

    /**
     * Cette fonction  upload les images dans le dossier image sur le serveur
     * @param file
     */
    public void upload(File file)
    {
        RequestBody requete = RequestBody.create(MediaType.parse("text/plain"), "upload");

        MediaType mediaType = MediaType.parse("image/*");
        RequestBody fichier_requete =RequestBody.create(mediaType,file);

        MultipartBody.Part part_fichier = MultipartBody.Part.createFormData("photo",
                file.getName(),
                fichier_requete);

        Call<ResponseBody> call = server.upload(requete, part_fichier);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }




    public void  setCode(String code){
        this.code=code;

    }
    public String getCode(){
        return code;
    }
}
