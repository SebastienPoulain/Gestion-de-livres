package com.example.livre3;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
/**
 *
 *
 *cette classe permet d'ajouter un livre à partir d'un code-barres ou non
 * @author  emilie cormier houle et Sébastien Poulain
 * @version 2020 fev
 * @since 1.0
 */

public class Ajoutlivre extends Fragment {

   ImageView imgAffichePhoto;
    View root;
    Bitmap bitmap3;
    EditText titre,auteur,edition,editeur,codebarre,prix;
    String titre2,auteur2,edition2,editeur2,codebarre2,prix2,currentPhotoPath,nomImg,imgPath;
    Button annuler,ajouter;
    CheckBox chkAnnotation;
    DataSingleton datas;
    File fichierPhoto,fichierPhotoGallery = null;
    Context context;
    Fragment fragAjout , fragScanner,fragshare;
    AlertDialog dialog;
    Calendar currentTime;
    SimpleDateFormat format;
    String last_id_livre=null;
    Toolbar toolbar;
    boolean verif=false;
    int idLivre,annotation;
    private InterfaceServeur serveur = RetrofitInstance.getInstance().create(InterfaceServeur.class);
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_GALLERY = 2;
    private FragmentAjout callbackFragment;


    public interface FragmentAjout{
       public void gerer();
    }


    /**
     * cette fonction permet d'initialiser les différents composants de l'application
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return root
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       root= inflater.inflate(R.layout.addlivre, container, false);
       titre= root.findViewById(R.id.edittitre);
       auteur= root.findViewById(R.id.editauteur);
       edition=root.findViewById(R.id.editedition);
       editeur=root.findViewById(R.id.editediteur);
       codebarre=root.findViewById(R.id.editcodebarre);
       prix=root.findViewById(R.id.editprix);
       chkAnnotation = root.findViewById(R.id.chkAnnotation);
       ajouter= root.findViewById(R.id.btSave);
       imgAffichePhoto= root.findViewById(R.id.imageView2);

       datas =DataSingleton.getInstance();
       context=getActivity().getApplicationContext();

       toolbar =  getActivity().findViewById(R.id.toolbar);
       toolbar.setNavigationIcon(R.drawable.ic_back);
       toolbar.setNavigationOnClickListener(new View.OnClickListener() {
           /**
            * cette fonction permet de retourner au fragment précèdent
            * @param v
            */
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
                toolbar.setTitle("Gestion de livres");
            }
        });



/**
 * ajouter.setOnClickListener
 *
 * permet d'ajouter un livre à la base de données
 *
 * @author  emilie cormier houle
 */
                ajouter.setOnClickListener(new View.OnClickListener() {
                    @Override


                    public void onClick(View view) {
                titre2= titre.getText().toString().trim();
                prix2=prix.getText().toString().trim();
                edition2=edition.getText().toString().trim();
                editeur2=auteur.getText().toString().trim();
                auteur2=editeur.getText().toString().trim();
                codebarre2=codebarre.getText().toString().trim();

                if(TextUtils.isEmpty(titre2)){
                    titre.setError( "le nom est requis" );
                }
                else if(TextUtils.isEmpty(prix2)){
                    prix.setError( "le prix est requis" );
                }
                else if(TextUtils.isDigitsOnly(prix2)==false){
                    prix.setError( "le prix est un chiffre" );
                }
                else if(TextUtils.isEmpty(edition2)){
                    edition.setError( "l'édition est requise" );
                }
                else if(TextUtils.isEmpty(auteur2)){
                    auteur.setError( "l'auteur est requise" );
                }
                else if(TextUtils.isEmpty(editeur2)){
                    editeur.setError( "l'éditeur est requis" );
                }
                else if(TextUtils.isEmpty(codebarre2)){
                    codebarre.setError( "le code à barre est requis" );
                }
                else if( TextUtils.isDigitsOnly(codebarre2)==false){
                    codebarre.setError( "le code à barre est un chiffre" );
                }
                else{

                    if (imgAffichePhoto.getDrawable() != null && fichierPhotoGallery != null){
                        datas.upload(fichierPhotoGallery);
                        imgPath = fichierPhotoGallery.getName();
                        imgPath = imgPath.replace(":","");
                    } else if(imgAffichePhoto.getDrawable() != null && fichierPhoto != null){
                        datas.upload(fichierPhoto);
                        imgPath = fichierPhoto.getName();
                        imgPath = imgPath.replace(":","");
                    }else
                        {
                            imgPath="imgvide.png";
                    }

                    if(verif==false){
/**
 * Call<ResponseBody> call = serveur.livreDATA
 * Call<ResponseBody> call = serveur.AjoutConcession
 *
 * appelle php (SelectLivre.php")
 *
 * @author  emilie cormier houle
 */
                        Call<ResponseBody> call = serveur.livreDATA(titre2,auteur2,editeur2,edition2,codebarre2,prix2,datas.getConnectedUser().getId(),imgPath);

                        call.enqueue(new Callback<ResponseBody>() {
                            /**
                             * Cette fonction interprète la réponse envoyée par le fichier PHP livreDATA
                             * @param call
                             * @param response
                             */
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                try {
                                    String reponse = response.body().string();
                                    Toast.makeText(context.getApplicationContext(), reponse,Toast.LENGTH_SHORT).show();

                                } catch (Exception e) {
                                    Toast.makeText(context.getApplicationContext(), e.getMessage(),Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }

                            }

                            /**
                             * Cette fonction interprète l'erreur envoyé par le fichier PHP livreDATA.
                             * @param call
                             * @param t
                             */
                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(context.getApplicationContext(), t.getMessage() ,Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                   else{
                       if(chkAnnotation.isChecked()){
                           annotation = 1;
                       }else{
                           annotation = 0;
                       }

                        Call<ResponseBody> call = serveur.AjoutConcession(prix2,datas.getIdLivre(),datas.getConnectedUser().getId(),imgPath,annotation);

                        call.enqueue(new Callback<ResponseBody>() {
                            /**
                             * Cette fonction interprète la réponse envoyée par le fichier PHP AjoutConcession
                             * @param call
                             * @param response
                             */
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                try {
                                    String reponse = response.body().string();
                                    Toast.makeText(context.getApplicationContext(), reponse,Toast.LENGTH_SHORT).show();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }

                            /**
                             * Cette fonction interprète l'erreur envoyé par le fichier PHP AjoutConession.
                             * @param call
                             * @param t
                             */
                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(context.getApplicationContext(), t.getMessage() ,Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
/**
 * FragmentManager
 *
 * permet de revenir au fragment gestion livre à
 * la fin de la transaction
 *
 * @author  emilie cormier houle
 */
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.detach(new Ajoutlivre());
                    fragmentTransaction.attach(new GestionLivreFragment());
                    fragmentTransaction.replace(R.id.flFragment,new GestionLivreFragment());
                    fragmentTransaction.commit();
                    dialog.cancel();

            }
            }
        });


/**
 * AlertDialog.Builder builder
 *
 * obligation d'entrez un codebarre pour ajouter le livre
 * peut utiliser le scan de code à barre
 *
 * builder à été redéfinie
 *
 * @author  emilie cormier houle
 */
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setTitle("Entrez votre code à barre");
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);
        if (!datas.getCode().trim().equals("")){
            input.setText(datas.getCode());
            datas.setCode("");
        }
        builder.setPositiveButton("ok",
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //Do nothing here because we override this button later to change the close behaviour.
                        //However, we still need this because on older versions of Android unless we
                        //pass a handler the button doesn't get instantiated
                    }
                });
        builder.setNeutralButton("Scan", new DialogInterface.OnClickListener() {
            /**
             * Cette fonction gère l'évenement du clic sur le bouton Scan du AlertDialog
             * @param dialogInterface
             * @param i
             */
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            /**
             * Cette fonction gère l'évenement du clic sur le bouton Annuler du AlertDialog
             * @param dialog
             * @param which
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fragAjout = new GestionLivreFragment();
                dialog.cancel();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flFragment,fragAjout);
                fragmentTransaction.commit();
                toolbar.setTitle("Gestion des livres");
            }
        });
         dialog = builder.create();
        dialog.show();


        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            /**
             * Cette fonction gère l'évenement du clic sur le bouton OK du AlertDialog
             * @param v
             */
            @Override
            public void onClick(View v)
            {

                if(input.getText().toString().trim().equals("")){
                    input.setError( "Le code-barres est requis" );
                }
                else{

                    Call<CodeBarre> call = serveur.SelectCodeBarre(input.getText().toString());

                    call.enqueue(new Callback<CodeBarre>() {
                        /**
                         * Cette fonction interprète la réponse du fichier PHP SelectCodeBarre.
                         * @param call
                         * @param response
                         */
                        @Override
                        public void onResponse(Call<CodeBarre> call, Response<CodeBarre> response) {


                            CodeBarre codeBarre = response.body();

                            if(response.body().getAuteur()!=null ){

                                if(response.body().getTitre()!=null){
                                titre.setText(response.body().getTitre());
                                titre.setEnabled(false);
                                titre.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.drawable.ic_title),null,context.getDrawable(R.drawable.ic_lock_black_24dp),null);
                                titre.setTextColor(Color.parseColor("#bfbfbf"));}
                                else{
                                    titre.setText("N/A");
                                    titre.setEnabled(false);
                                }
                                if(response.body().getEdition()!=null){
                                edition.setText(response.body().getEdition());
                                edition.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.drawable.auteur),null,context.getDrawable(R.drawable.ic_lock_black_24dp),null);
                                edition.setTextColor(Color.parseColor("#bfbfbf"));
                                edition.setEnabled(false);}
                                else{
                                    edition.setText("N/A");
                                    edition.setEnabled(false);
                                }
                                    if(response.body().getEditeur()!=null){
                                        editeur.setText(response.body().getEditeur());
                                        editeur.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.drawable.ic_book),null,context.getDrawable(R.drawable.ic_lock_black_24dp),null);
                                        editeur.setTextColor(Color.parseColor("#bfbfbf"));
                                        editeur.setEnabled(false);}
                                    else{
                                        editeur.setText("N/A");
                                        editeur.setEnabled(false);
                                    }
                                        if(response.body().getAuteur()!=null)
                                        {
                                            auteur.setText(response.body().getAuteur());
                                            auteur.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.drawable.ic_book),null,context.getDrawable(R.drawable.ic_lock_black_24dp),null);
                                            auteur.setTextColor(Color.parseColor("#bfbfbf"));
                                            auteur.setEnabled(false);
                                        }
                                        else{
                                            auteur.setText("N/A");
                                            auteur.setEnabled(false);
                                        }

                                        codebarre.setText(input.getText().toString());
                                        codebarre.setTextColor(Color.parseColor("#bfbfbf"));
                                        codebarre.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.drawable.barcode),null,context.getDrawable(R.drawable.ic_lock_black_24dp),null);
                                        codebarre.setEnabled(false);
                                        datas.setIdLivre(codeBarre.getId());
                                        verif=true;
                            }
                            else{
                                codebarre.setText(input.getText().toString());
                                codebarre.setTextColor(Color.parseColor("#bfbfbf"));
                                codebarre.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.drawable.barcode),null,context.getDrawable(R.drawable.ic_lock_black_24dp),null);
                                codebarre.setEnabled(false);
                                verif=false;
                            }

                        }

                        /**
                         * Cette fonction interprète l'erreur envoyé par le fichier PHP SelectCodeBarre.
                         * @param call
                         * @param t
                         */
                        @Override
                        public void onFailure(Call<CodeBarre> call, Throwable t) {

                            Toast.makeText(context.getApplicationContext(), t.getMessage() ,Toast.LENGTH_LONG).show();
                        }
                    });
                    dialog.dismiss();
                }
            }
        });
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            /**
             * Cette fonction gère l'évenement du clic sur le bouton scan du alert dialog.
             * @param view
             */
            @Override
            public void onClick(View view) {
                if (verifierPermissions()) {
                    fragScanner = new Scannerview();
                    dialog.cancel();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.flFragment, fragScanner);
                    fragmentTransaction.commit();
                }
            }
        });
/**
 *  imgAffichePhoto.setOnClickListener
 *
 * permet d'ouvrir l'appareil photo ou la galerie
 *
 *
 * @author  emilie cormier houle
 */
        imgAffichePhoto.setOnClickListener(new View.OnClickListener() {
            /**
             * Cette fonction gère l'évenement du clic sur l'imageview
             * @param view
             */
            @Override
            public void onClick(View view) {

                if (verifierPermissions()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Ajouter une photo");



                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);


                    builder.setPositiveButton("Gallerie",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    builder.setNegativeButton("Caméra",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    builder.setNeutralButton("Annuler", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    final AlertDialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();


                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                        /**
                         * Cette fonction gère l'évenement du clic sur le bouton Gallerie du AlertDialog
                         * @param v
                         */
                        @Override
                        public void onClick(View v) {


                            pickImageFromGallery();
                            dialog.dismiss();

                        }
                    });
                    dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                        /**
                         * Cette fonction gère l'évenement du bouton Caméra du AlertDialog
                         * @param view
                         */
                        @Override
                        public void onClick(View view) {

                            dialog.dismiss();
                        }
                    });
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                        /**
                         * Cette fonction gère l'évenement du bouton Annuler du AlertDialog
                         * @param view
                         */
                        @Override
                        public void onClick(View view) {

                            lancerCamera();

                            dialog.dismiss();
                        }
                    });
                }
            }
        });

        return root;
    }

    /**
     *  onResume
     *
     * permet d'avoir la date actuel pour le nom de l'image
     *
     *
     * @author  Sébastien poulain
     */
    @Override
    public void onResume() {

        currentTime = Calendar.getInstance();
        format = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");

        super.onResume();
    }


    /**
     *  onActivityResult
     *
     *
     * @author  Sébastien Poulain
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(fichierPhoto.getAbsolutePath());
            imgAffichePhoto.setImageBitmap(bitmap);
        }

        if(resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_GALLERY && data != null ) {
            try {
                Uri path = data.getData();
                bitmap3 = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),path);
                imgAffichePhoto.setImageBitmap(bitmap3);
                nomImg = datas.getConnectedUser().getId() + codebarre.getText().toString() +  format.format(currentTime.getTime()) ;
                nomImg = nomImg.replace(":","");
                bitmap_to_file(bitmap3,nomImg );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     *  bitmap_to_file
     *
     *
     * @author  Sébastien poulin
     */
    private  void bitmap_to_file(Bitmap bitmap, String name) {
        File filesDir = getContext().getFilesDir();
        fichierPhotoGallery = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(fichierPhotoGallery);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Erreur de bitmap", e);
        }
    }

    /**
     *  pickImageFromGallery()
     *
     *
     * @author  Sébastien poulain
     */
    private void pickImageFromGallery(){

    Intent intent = new Intent(Intent.ACTION_PICK);
    intent.setType("image/*");
        startActivityForResult(intent,REQUEST_IMAGE_GALLERY);
    }

    public void lancerProgramme()
    {
        imgAffichePhoto.setEnabled(true);
    }
    /**
     *  verifierPermissions()
     *
     *vérifie les permissions appareil photo, ecriture et lecture d'une base de donnée externe
     *
     * @author  Émilie cormier houle
     */
    public boolean verifierPermissions()
    {
        String[] permissions = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};

        List<String> listePermissionsADemander = new ArrayList<>();

        for(int i=0; i< permissions.length; i++)
        {
            if(ContextCompat.checkSelfPermission(getActivity(),permissions[i]) != PackageManager.PERMISSION_GRANTED)
            {
                listePermissionsADemander.add(permissions[i]);
            }
        }

        if(listePermissionsADemander.isEmpty())
            return true;
        else
        {
            ActivityCompat.requestPermissions(getActivity(), listePermissionsADemander.toArray(new String[listePermissionsADemander.size()]),1111 );

            return false;
        }
    }
    /**
     *  lancerCamera()
     *
     *ouvre la caméra
     *
     * @author  Émilie cormier houle
     */
    private void lancerCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

            fichierPhoto = null;
            try {
                fichierPhoto = creationFichierPhoto();
            } catch (IOException ex) {

            }

            if (fichierPhoto != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.example.livre3.fileprovider",
                        fichierPhoto);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }
    /**
     *  creationFichierPhoto()
     *
     *
     *
     * @author  Sébastien Poulain
     */
    private File creationFichierPhoto() throws IOException {
        String imageFileName = datas.getConnectedUser().getId() + codebarre.getText().toString() +  format.format(currentTime.getTime()) ;
        imageFileName = imageFileName.replace(":","");
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg" ,         /* suffix */
                storageDir   /* dossier */
        );


        currentPhotoPath = image.getAbsolutePath();

        return image;
    }


}