package com.example.livre3;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Sébastien Poulain
 * cette classe permet de retrouver son mot de passe si on a oublié notre mot de passe
 */

public class Forgot extends Fragment {

    Toolbar toolbar;
    Button btnEnvoyer;
    TextView tvInfo;
    EditText etCourriel;
    private InterfaceServeur serveur = RetrofitInstance.getInstance().create(InterfaceServeur.class);

    /**
     * Cette fonction permet d'initialiser les différents composants
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return root
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.forgot, container, false);

        btnEnvoyer = root.findViewById(R.id.btEnvoyer);
        tvInfo = root.findViewById(R.id.tvInfo);
        etCourriel = root.findViewById(R.id.etCourriel);
        toolbar =  getActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
                toolbar.setTitle("Connexion");
            }
        });

        btnEnvoyer.setOnClickListener(new View.OnClickListener() {
            /**
             * Cette fonction envoit au serveur l'adresse courriel à qui envoyer un mot de passe temporaire si toutes les informations sont correctes
             * @param v
             */
            @Override
            public void onClick(View v) {
                if(!etCourriel.getText().toString().trim().equals("") && isValidEmail(etCourriel.getText().toString().trim())){

                        Call<ResponseBody> call = serveur.forgot(etCourriel.getText().toString().trim());

                        call.enqueue(new Callback<ResponseBody>() {
                            /**
                             * Réponse du serveur lors de l'envoit du courriel
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

                                if( reponse.equals("Ce courriel est relié à aucun compte")){
                                    Toast.makeText(getContext(), reponse, Toast.LENGTH_LONG).show();

                                }
                                else{
                                    Toast.makeText(getContext(), reponse, Toast.LENGTH_LONG).show();
                                    etCourriel.setText("");
                                    tvInfo.setVisibility(View.VISIBLE);
                                }

                            }

                            /**
                             * affichage du messge d'erreur si il y a un problème au niveau du serveur
                             * @param call
                             * @param t
                             */
                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(getContext(), t.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });


                }else{
                    Toast.makeText(getContext(), "Vous devez entrer une adresse courriel valide", Toast.LENGTH_LONG).show();
                }
            }
        });

        return root;
    }

    /**
     * fonction qui permet de valider une adresse courriel
     * @param target
     * @return boolean
     */
    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}

