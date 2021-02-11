package com.example.livre3;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Jérémy Lampron
 * Cette classe gère tout ce qui à rapport avec la modification du mot de passe.
 */

public class Modif_password extends Fragment {
    private DataSingleton data = DataSingleton.getInstance();
    private View root;
    private InterfaceServeur serveur = RetrofitInstance.getInstance().create(InterfaceServeur.class);
    Button btEnregistrer1, btAnnuler1;
    EditText tbMdp, tbMdpa, tbMdpc;
    String mdpa, mdp;
    Integer id;
    Toolbar toolbar;

    /**
     * cette fonction initialise les différents composants
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return root
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

         root = inflater.inflate(R.layout.modif_password, container, false);

        toolbar =  getActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            /**
             * Cette fonction gère l'évenement du clic sur la toolbar pour nous ramener sur le fragment précèdent
             * @param v
             */
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
                toolbar.setTitle("Profil");
            }
        });

        tbMdp = root.findViewById(R.id.tbMdpp);
        tbMdpa = root.findViewById(R.id.tbMdpa);
        tbMdpc = root.findViewById(R.id.tbMdpc);
        btAnnuler1 = root.findViewById(R.id.btAnnuler1);
        btEnregistrer1 = root.findViewById(R.id.btEnregistrer1);


        btEnregistrer1.setOnClickListener(new View.OnClickListener() {
            /**
             * Cette fonction gère l'évenement du clic sur le bouton Enregistrer. Elle envoie les modifications du mot de passe au serveur (php)
             * @param view
             */
            @Override
            public void onClick(View view) {

                if((tbMdp.getText().toString().trim().equals(tbMdpc.getText().toString().trim())) && (!tbMdpa.getText().toString().trim().equals(""))){
                    data.initContext(root.getContext());
                    mdpa = data.getSha256Hash(tbMdpa.getText().toString().trim());
                    mdp = data.getSha256Hash(tbMdp.getText().toString().trim());
                    id = data.getConnectedUser().getId();
                    Call<ResponseBody> call = serveur.modif_password(id, mdpa, mdp);
                    call.enqueue(new Callback<ResponseBody>() {
                        /**
                         * Cette fonction interprète la réponse envoyée par le fichier PHP modif_password.
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
                            if (reponse.equals("notfound")){
                                Toast.makeText(getContext().getApplicationContext(), "Votre mot de passe actuel ne correspond pas", Toast.LENGTH_SHORT).show();
                            }
                            else if(reponse.equals("success")){
                                Toast.makeText(getContext().getApplicationContext(),"Votre mot de passe a bien été modifié", Toast.LENGTH_LONG).show();

                                InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                        InputMethodManager.HIDE_NOT_ALWAYS);

                                Intent intent = new Intent();
                                intent.setAction("com.info.lesreceivers.MON_ACTION3");
                                getContext().sendBroadcast(intent);
                            }else{
                                Toast.makeText(getContext().getApplicationContext(), "Il y a un problème avec le serveur.",Toast.LENGTH_LONG).show();
                            }
                        }

                        /**
                         * Cette fonction interprète l'erreur envoyé par le fichier PHP modif_password
                         * @param call
                         * @param t
                         */
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(getContext().getApplicationContext(), t.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                }else if(tbMdp.getText().toString().trim().equals("") || tbMdpc.getText().toString().trim().equals("") || tbMdpa.getText().toString().trim().equals("")){
                    Toast.makeText(getContext().getApplicationContext(), "Vous devez remplir tous les champs",Toast.LENGTH_LONG).show();
                }else if(!tbMdp.getText().toString().trim().equals(tbMdpc.getText().toString().trim())){
                    Toast.makeText(getContext().getApplicationContext(), "Les mots de passe entrés ne sont pas équivalent",Toast.LENGTH_LONG).show();
                }

            }
        });


        return root;
    }


}
