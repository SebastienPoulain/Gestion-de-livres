
package com.example.livre3;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import java.util.regex.Pattern;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * @author Jérémy Lampron
 * Cette classe gère tous ce qui concerne le changement des informations personelles
 */

public class Modif_info extends Fragment {

    Context context;
    EditText tbPrenom, tbNom, tbCellulaire, tbCourriel, tbMdpOld;
    Button btSave, btAnnuler;
    private DataSingleton data = DataSingleton.getInstance();
    private View root;
    private InterfaceServeur serveur = RetrofitInstance.getInstance().create(InterfaceServeur.class);
    Toolbar toolbar;

    /**
     * Cette fonction permet d'initialiser les différents composants
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return root
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.modif_info, container, false);

         toolbar =  getActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            /**
             * Cette fonction gère l'évenement du clic sur le toolbar et de retourner au fragment précèdent
             * @param v
             */
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
                toolbar.setTitle("Profil");
            }
        });

        tbPrenom        = root.findViewById(R.id.tbPrenom);
        tbNom           = root.findViewById(R.id.txttitle);
        tbCourriel      = root.findViewById(R.id.tbCourriel);
        tbCellulaire    = root.findViewById(R.id.tbCell);
        tbMdpOld        = root.findViewById(R.id.tbMdpOld);
        btSave          = root.findViewById(R.id.btSave);
        btAnnuler       = root.findViewById(R.id.btAnnuler1);
        data            = DataSingleton.getInstance();
        context         = getContext();

        tbPrenom        .setText(data.getConnectedUser().getPrenom());
        tbNom           .setText(data.getConnectedUser().getNom());
        tbCellulaire    .setText(data.getConnectedUser().getTel());
        tbCourriel      .setText(data.getConnectedUser().getEmail());

        tbCellulaire.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

       btSave.setOnClickListener(new View.OnClickListener() {
           /**
            * Cette fonction gère l'évenement du clic sur le bouton sauvegarder. Elle envoie les nouvelles informations du profil au serveur
            * @param view
            */
            @Override
            public void onClick(View view) {

                if (!tbPrenom.getText().toString().trim().equals("") && !tbNom.getText().toString().trim().equals("") && !tbCourriel.getText().toString().trim().equals("") && !tbCellulaire.getText().toString().trim().equals("") && !tbMdpOld.getText().toString().trim().equals("")) {
                    if ( checkTextOnly(tbPrenom.getText().toString())) {
                        if ( checkTextOnly(tbNom.getText().toString().trim())){
                            if( isValidEmail(tbCourriel.getText().toString().trim())){
                              if(checkPhone(tbCellulaire.getText().toString().trim()) && tbCellulaire.getText().toString().trim().length() == 14) {
                                  Integer id = data.getConnectedUser().getId();
                                  String firstName = tbPrenom.getText().toString();
                                  String lastName = tbNom.getText().toString();
                                  String email = tbCourriel.getText().toString();
                                  String phoneNumber = tbCellulaire.getText().toString();
                                  String password = tbMdpOld.getText().toString();
                                  String ancienEmail = data.getConnectedUser().getEmail();
                                  Call<ResponseBody> call = serveur.modif_info(id, firstName, lastName, email,ancienEmail, phoneNumber, data.getSha256Hash(password));
                                  data.initContext(root.getContext());


                                  call.enqueue(new Callback<ResponseBody>() {
                                      /**
                                       * Cette fonction interprète la réponse envoyer par le fichier PHP modif_info
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

                                          if (reponse.equals("notfound")) {
                                              Toast.makeText(getContext().getApplicationContext(), "Le mot de passe ne correspond pas à votre mot de passe actuel", Toast.LENGTH_SHORT).show();
                                          } else if (reponse.equals("success")) {
                                              Toast.makeText(getContext().getApplicationContext(), "Vos informations ont bien été modifiées", Toast.LENGTH_LONG).show();
                                              data.getConnectedUser().setPrenom(tbPrenom.getText().toString());
                                              data.getConnectedUser().setNom(tbNom.getText().toString());
                                              data.getConnectedUser().setEmail(tbCourriel.getText().toString());
                                              data.getConnectedUser().setTel(tbCellulaire.getText().toString());

                                              SharedPreferences pref = getActivity().getSharedPreferences("login", MODE_PRIVATE);
                                              SharedPreferences.Editor editor = pref.edit();
                                              editor.putBoolean("connected",true);
                                              editor.putString("email",data.getConnectedUser().getEmail());
                                              editor.putBoolean("start",false);
                                              editor.commit();

                                              InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                              inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                                      InputMethodManager.HIDE_NOT_ALWAYS);

                                              Intent intent = new Intent();
                                              intent.setAction("com.info.lesreceivers.MON_ACTION3");
                                              getContext().sendBroadcast(intent);
                                          }else if(reponse.equals("emailExiste")){
                                              Toast.makeText(getContext().getApplicationContext(), "L'adresse courriel existe déjà", Toast.LENGTH_SHORT).show();
                                          }
                                          else {
                                              Toast.makeText(getContext().getApplicationContext(), "Il y a un problème avec le serveur.", Toast.LENGTH_LONG).show();
                                          }

                                      }

                                      /**
                                       * Cette fonction interprète l'erreur envoyé par le fichier PHP modif_info et l'affiche
                                       * @param call
                                       * @param t
                                       */
                                      @Override
                                      public void onFailure(Call<ResponseBody> call, Throwable t) {
                                          Toast.makeText(getContext().getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                                      }
                                  });
                              }else Toast.makeText(context, "Le numéro de téléphone est invalide", Toast.LENGTH_LONG).show();
                            }
                            else Toast.makeText(context, "Le courriel entré est invalide", Toast.LENGTH_LONG).show();
                        }
                        else Toast.makeText(context, "Le nom entré est invalide", Toast.LENGTH_LONG).show();
                    }
                    else Toast.makeText(context, "Le prénom entré est invalide", Toast.LENGTH_LONG).show();


                }
                else Toast.makeText(context, "Veuillez remplir tous les champs", Toast.LENGTH_LONG).show();

            }
            });


        return  root;

    }

    /**
     * Cette fonction vient vérifier si le format de l'adresse courriel entrée est conforme.
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

    /**
     * Cette fonction vient vérifier si les caractères entrés dans le champ ne sont pas des chiffres.
     * @param texte
     * @return boolean
     */
    public final static boolean checkTextOnly(CharSequence texte) {
        return Pattern.compile("^([a-zA-Z.,' -])*$").matcher(texte).matches();

    }

    /**
     * Cette fonction vient vérifier si le numéro de téléphone entré est bien un numéro de téléphone
     * @param tel
     * @return boolean
     */
    public final static boolean checkPhone(String tel) {
       return Pattern.compile("^[(][0-9]{3}[)][ ][0-9]{3}[-][0-9]{4}$").matcher(tel).matches();

    }


}
