package com.example.livre3;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.example.livre3.Ajoutlivre.REQUEST_IMAGE_CAPTURE;
import static com.example.livre3.Ajoutlivre.REQUEST_IMAGE_GALLERY;

/**
 * @author  Jérémy Lampron
 * Ce fragment permet d'afficher les détails des livres de la personne connectée quand on clique sur un de ses livres
 */

public class MesLivresDetailsFragment extends Fragment {

    private TextView tvtitle,tvAuteur,tvEditeur,tvEdition,tvCodeBarre,tvAucun;
    EditText txtPrix;
    private ImageView IVimg;
    private Button btnSauvegarder;
    String lien,lien2, nomImg, currentPhotoPath, imgPath, prix2;
    CheckBox chkAnnotation;
    List<Livre> listelivres;
    List<Integer> listAnnotation,listId, listIdC;
    List<Double> listPrix, prix3;
    List listImg;
    ArrayAdapter<String> dataAdapter;
    Integer id,position2;
    Livre temp;
    Fragment fragGestionLivreFragment;
    DataSingleton datas;
    Calendar currentTime;
    SimpleDateFormat format;
    File fichierPhoto,fichierPhotoGallery = null;
    Bitmap bitmap3;
    int check;



    private InterfaceServeur serveur = RetrofitInstance.getInstance().create(InterfaceServeur.class);
    View root;

    /**
     * cette fonction permet d'initialiser les différents composants
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return root
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

         root = inflater.inflate(R.layout.meslivresdetails, container, false);


        final Toolbar toolbar =  getActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
                toolbar.setTitle("Gestion des livres");
            }
        });
        listelivres  = new ArrayList<Livre>();
        listImg = new ArrayList<>();
        listAnnotation = new ArrayList<Integer>();
        listId = new ArrayList<Integer>();
        listPrix = new ArrayList<Double>();




        datas=DataSingleton.getInstance();
        tvAucun = root.findViewById(R.id.tvAucun);
        chkAnnotation = root.findViewById(R.id.chkAnnotation);
        tvtitle = (TextView) root.findViewById(R.id.txttitle);
        tvEditeur = (TextView) root.findViewById(R.id.txtEditeur);
        tvAuteur = (TextView) root.findViewById(R.id.txtAuteur);
        tvEdition = (TextView) root.findViewById(R.id.txtEdition);
        tvCodeBarre = (TextView) root.findViewById(R.id.txtCode);
        txtPrix = (EditText) root.findViewById(R.id.txtPrix);
        IVimg = (ImageView) root.findViewById(R.id.bookthumbnail);
        btnSauvegarder=root.findViewById(R.id.btnSauvegarder);

        id = getArguments().getInt("id");
        String Title = getArguments().getString("Title","N/A");
        String auteur = getArguments().getString("auteur", "N/A");
        String editeur = getArguments().getString("editeur", "N/A");
        String edition = getArguments().getString("edition", "N/A");
        String codebar = getArguments().getString("codebar", "N/A");
        imgPath = getArguments().getString("imagePath", "");
        final Double prix = getArguments().getDouble("prix", 0);
        final Integer annotation = getArguments().getInt("annotation", 0);
        final Integer idc = getArguments().getInt("idC",0);

        lien = RetrofitInstance.getInstance().baseUrl() + "img/" + imgPath;

        Picasso.get().load(lien)
                .resize(220, 220)
                .centerCrop()
                .into(IVimg);

        tvtitle.setText(Title);
        tvEditeur.setText(editeur);
        tvAuteur.setText(auteur);
        tvEdition.setText(edition);
        tvCodeBarre.setText(codebar);

        final NumberFormat formatter = new DecimalFormat("#0.00");
        txtPrix.setText( formatter.format(prix).toString());

        if (annotation==1){
            chkAnnotation.setChecked(true);
        }


        btnSauvegarder.setOnClickListener(new View.OnClickListener() {
            /**
             * Quand on clique sur le bouton sauvegarder, on peut changer les informations de nos concessions comme l'image, l'annotation et le prix
             * @param view
             */
            @Override
            public void onClick(View view) {
                prix2 =txtPrix.getText().toString().replace(",",".");
              if (!prix2.trim().equals("")) {


                  if (IVimg.getDrawable() != null && fichierPhotoGallery != null){
                      datas.upload(fichierPhotoGallery);
                      imgPath = fichierPhotoGallery.getName();
                      imgPath = imgPath.replace(":","");
                  } else if(IVimg.getDrawable() != null && fichierPhoto != null){
                      datas.upload(fichierPhoto);
                      imgPath = fichierPhoto.getName();
                      imgPath = imgPath.replace(":","");
                  }else
                  {
                      imgPath="imgvide.png";
                  }
                  Double modif_prix = Double.parseDouble(prix2);
                  if (chkAnnotation.isChecked()){
                      check=1;
                  }
                  else{
                      check=0;
                  }

                Call<ResponseBody> call = serveur.updateConcession(idc, modif_prix, check, imgPath);

                call.enqueue(new Callback<ResponseBody>() {
                    /**
                     * On reçoit la réponse du serveur qui va mettre à jours les informations de la concession et on retourne dans la section gestion des livres
                     * @param call
                     * @param response
                     */
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        String reponse = "";
                        try {
                            reponse = response.body().string();

                        } catch (IOException e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();

                        }

                        if (reponse.equals("modification avec prix reussie")){

                            Toast.makeText(getActivity(), "Modification réussie, l'administrateur doit tout d'abord aller accepter vos modifications avant que celles-ci soient visible", Toast.LENGTH_LONG).show();


                        }
                        else if (reponse.equals("modification reussie")){

                           Toast.makeText(getActivity(), "Modification réussie", Toast.LENGTH_LONG).show();


                        }
                        else{ Toast.makeText(getActivity(), "Une erreur est survenue lors de la modification de votre concession", Toast.LENGTH_LONG).show();
                        }

                        fragGestionLivreFragment = new GestionLivreFragment();

                        FragmentManager fragmentManager2 = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                        fragmentTransaction2.replace(R.id.flFragment,fragGestionLivreFragment);
                        fragmentTransaction2.commit();

                        toolbar.setTitle("Gestion des livres");

                    }

                    /**
                     * Si il y a une erreur au niveau du serveur, on fait afficher le message d'erreur
                     * @param call
                     * @param t
                     */
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            }
            else{
                Toast.makeText(getContext(),"Le prix entré est invalide", Toast.LENGTH_LONG).show();
              }
            }
        });

        IVimg.setOnClickListener(new View.OnClickListener() {
            /**
             * On peut changer l'image de la concession en cliquant sur l'image précèdente par le bias de la gallerie ou de l'appareil photo
             * @param view
             */
            @Override
            public void onClick(View view) {

                if (verifierPermissions()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Modifier une photo");



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
                        @Override
                        public void onClick(View v) {
                            pickImageFromGallery();
                            dialog.dismiss();

                        }
                    });
                    dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            dialog.dismiss();
                        }
                    });
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
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
     * On récupère la date  et l'heure du jour pour l'utiliser dans le nommage des images
     */
    @Override
    public void onResume() {

        currentTime = Calendar.getInstance();
        format = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");

        super.onResume();
    }

    /**
     * dans le onActivityResult on récupère l'image de la gallerie ou de l'appareil photo et on l'affiche, si c'est un fichier de la gallerie on créer un fichier file à partir du bitmap récupéré
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Bitmap bitmap = BitmapFactory.decodeFile(fichierPhoto.getAbsolutePath());
            IVimg.setImageBitmap(bitmap);
        }

        if(resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_GALLERY && data != null ) {
            try {
                Uri path = data.getData();
                bitmap3 = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),path);
                IVimg.setImageBitmap(bitmap3);
                nomImg = datas.getConnectedUser().getId() + tvCodeBarre.getText().toString() +  format.format(currentTime.getTime()) ;
                nomImg = nomImg.replace(":","");
                bitmap_to_file(bitmap3,nomImg );
                imgPath = nomImg;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * méthode qui permet de convertir un fichier bitmap en file
     * @param bitmap
     * @param name
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
     * fonction qui permet d'ouvrir la gallerie
     */
    private void pickImageFromGallery(){

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,REQUEST_IMAGE_GALLERY);
    }

    /**
     * fonction qui permet de récupérer l'état des permissions de la personne connectée en fonction des différents droits demandés (camera,écriture,lecture)
     * @return boolean
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
     * fonction qui permet de lancer la caméra
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
     * fonction qui permet de créer un fichier lorsque nous avons pris une photo
     * @return image
     * @throws IOException
     */
    private File creationFichierPhoto() throws IOException {
        String imageFileName = datas.getConnectedUser().getId() + tvCodeBarre.getText().toString() +  format.format(currentTime.getTime()) ;
        imageFileName = imageFileName.replace(":","");
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir   /* dossier */
        );


        currentPhotoPath = image.getAbsolutePath();


        return image;
    }

}
