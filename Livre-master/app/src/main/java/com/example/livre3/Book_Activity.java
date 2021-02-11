package com.example.livre3;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Sébastien Poulain
 * cette classe permet d'afficher les différentes concessions associées à un livre qui a été sélectionné dans le fragment homefragment
 */

public class Book_Activity extends Fragment {

    private TextView tvtitle,tvAuteur,tvEditeur,tvEdition,tvCodeBarre,tvAucun;
    private ImageView img;
    private Button btnReserv;
    String lien,lien2;
    CheckBox chkAnnotation;
    Spinner spinner;
    List<Livre> listelivres;
    List<Integer> listAnnotation,listId;
    List<String> SpinnerPrix;
    ArrayAdapter<String> dataAdapter;
    Integer id,position2;
    Livre temp;
    Fragment fragRech, fragLogin;
    DataSingleton datas;

    List listImg;
    private InterfaceServeur serveur = RetrofitInstance.getInstance().create(InterfaceServeur.class);
    View root;

    /**
     * permet d'initialiser les différents composants
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return root
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

         root = inflater.inflate(R.layout.livre_details, container, false);

        final Toolbar toolbar =  getActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            /**
             * permet de retourner au fragment précèdent
             * @param v
             */
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
                toolbar.setTitle("Résultats de la recherche");
            }
        });
        listelivres  = new ArrayList<Livre>();
        SpinnerPrix = new ArrayList<String>();
        listImg = new ArrayList<>();
        listAnnotation = new ArrayList<Integer>();
        listId = new ArrayList<Integer>();

        SpinnerPrix.add(0,"Vous devez choisir une concession");


        datas=DataSingleton.getInstance();
        tvAucun = root.findViewById(R.id.tvAucun);
        spinner  =  root.findViewById(R.id.spinner1);
        spinner.setPrompt("Vous devez choisir une concession");
        chkAnnotation = root.findViewById(R.id.chkAnnotation);
        tvtitle = (TextView) root.findViewById(R.id.txttitle);
        tvEditeur = (TextView) root.findViewById(R.id.txtEditeur);
        tvAuteur = (TextView) root.findViewById(R.id.txtAuteur);
        tvEdition = (TextView) root.findViewById(R.id.txtEdition);
        tvCodeBarre = (TextView) root.findViewById(R.id.txtCode);
        img = (ImageView) root.findViewById(R.id.bookthumbnail);
        btnReserv=root.findViewById(R.id.btnReserve);

        if (datas.getEtatConnexion() == false) {
            btnReserv.setEnabled(false);
        }



        id = getArguments().getInt("id");
        String Title = getArguments().getString("Title","N/A");
        String auteur = getArguments().getString("auteur", "N/A");
        String editeur = getArguments().getString("editeur", "N/A");
        String edition = getArguments().getString("edition", "N/A");
        String codebar = getArguments().getString("codebar", "N/A");
        String imgPath = getArguments().getString("imagePath", "");


        lien = RetrofitInstance.getInstance().baseUrl() + "img/" + imgPath;

        Picasso.get().load(lien)
                .resize(220, 220)
                .centerCrop()
                .into(img);

        tvtitle.setText(Title);
        tvEditeur.setText(editeur);
        tvAuteur.setText(auteur);
        tvEdition.setText(edition);
        tvCodeBarre.setText(codebar);

        Call<List<Livre>> call = serveur.getConcessionLivre(id);

        call.enqueue(new Callback<List<Livre>>() {
            /**
             * réponse du serveur lors de la récupération des concessions
             * @param call
             * @param response
             */
            @Override
            public void onResponse(Call<List<Livre>> call, Response<List<Livre>> response) {
                List<Livre> reponse = response.body();

                if (reponse.get(0).getImgpath() == null){
                    SpinnerPrix.clear();
                    SpinnerPrix.add("Il y a aucune concession pour ce livre");
                    btnReserv.setEnabled(false);
                }
                else {
                    for (int i = 0; i < reponse.size(); i++) {
                        temp = new Livre(reponse.get(i).getId(),reponse.get(i).getSellingPrice(),reponse.get(i).getImgpath(),reponse.get(i).getAnnotation());
                        listelivres.add(temp);
                        listId.add(reponse.get(i).getId());
                        listImg.add(reponse.get(i).getImgpath());
                        SpinnerPrix.add(reponse.get(i).getSellingPrice().toString() + " $");
                        listAnnotation.add(reponse.get(i).getAnnotation());
                    }
                }
                dataAdapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_item, SpinnerPrix);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(dataAdapter);

            }

            /**
             * affichage du messge d'erreur si problème au niveau du serveur
             * @param call
             * @param t
             */
            @Override
            public void onFailure(Call<List<Livre>> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        spinner.setOnTouchListener(new View.OnTouchListener() {
            /**
             * Quand on clique sur le menu déroulant, l'affichage est différente dépendemment si il y a des concessions de disponible ou non
             * @param v
             * @param event
             * @return boolean
             */
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (SpinnerPrix.size() == 1 && spinner.getSelectedItem().toString() == "Il y a aucune concession pour ce livre") {
        spinner.setEnabled(false);
                } else {

                    if (spinner.getSelectedItem().toString() == "Vous devez choisir une concession") {
                        SpinnerPrix.remove(0);
                        dataAdapter.notifyDataSetChanged();
                        lien2 = RetrofitInstance.getInstance().baseUrl() + "img/" + listImg.get(0);

                        Picasso.get().load(lien2)
                                .resize(220, 220)
                                .centerCrop()
                                .into(img);

                        if (listAnnotation.get(0) == 0) {
                            chkAnnotation.setChecked(false);
                        } else {
                            chkAnnotation.setChecked(true);
                        }
                    }
                    return false;
                }
                return false;
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * On change l'annotation,l'image dépendemment du prix de la concession sélectionné
             * @param parent
             * @param view
             * @param position
             * @param id
             */
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

               if(spinner.getSelectedItem().toString() != "Vous devez choisir une concession" && SpinnerPrix.size() != 1){
                    lien2 = RetrofitInstance.getInstance().baseUrl() + "img/" + listImg.get(spinner.getSelectedItemPosition());
                   position2 = spinner.getSelectedItemPosition();
                    Picasso.get().load(lien2)
                            .resize(220, 220)
                            .centerCrop()
                            .into(img);

                    if (listAnnotation.get(spinner.getSelectedItemPosition()) == 0) {
                        chkAnnotation.setChecked(false);
                    } else {
                        chkAnnotation.setChecked(true);
                    }
                }else position2 = 0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnReserv.setOnClickListener(new View.OnClickListener() {
            /**
             * quand on clique sur réserver  cela réserve la concession sélectionée dans le menu déroulant et nous renvoit sur la page de recherche
             * @param view
             */
            @Override
            public void onClick(View view) {

                    Call<ResponseBody> call = serveur.setReservation(listId.get(position2), datas.getConnectedUser().getId());

                call.enqueue(new Callback<ResponseBody>() {
                    /**
                     * réponse du serveur lors de la réservation
                     * @param call
                     * @param response
                     */
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        String reponse = null;
                        try {
                            reponse = response.body().string();
                            Toast.makeText(getActivity(), reponse, Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();

                        }

                        fragRech = new Recherche();

                        FragmentManager fragmentManager2 = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                        fragmentTransaction2.replace(R.id.flFragment, fragRech);
                        fragmentTransaction2.commit();


                        toolbar.setTitle("Recherche");

                    }

                    /**
                     * affichage de l'erreur du serveur si erreur
                     * @param call
                     * @param t
                     */
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }


        });
    return root;
    }


}
