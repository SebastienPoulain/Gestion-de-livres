package com.example.livre3;



import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import java.util.regex.Pattern;

/**
 * @author Jérémy Lampron et Émilie Cormier Houle
 * Cette classe gère le fragment d'inscription.
 */
public class Inscription extends Fragment {

    EditText editmail, editpwrd, editprenom, editnom, editphone, editcrfmpwrd, editmatricule;
    Button buttonCreer;
    String matricule, prenom, nom, tel, email, passwrd;
    private DataSingleton data = DataSingleton.getInstance();
    AlertDialog dialog;

    /**
     * cette fonction permet d'initialiser les différents composants
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return root
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.register, container, false);

        final Toolbar toolbar =  getActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
                toolbar.setTitle("Connexion");
            }
        });

        editmail = (EditText) root.findViewById(R.id.etCode);
        editpwrd = (EditText) root.findViewById(R.id.editpwrd);
        editprenom = (EditText) root.findViewById(R.id.etAuteur);
        editnom = (EditText) root.findViewById(R.id.etEditeur);
        editphone = (EditText) root.findViewById(R.id.etEdition);
        editcrfmpwrd = (EditText) root.findViewById(R.id.editcfrmpwrd);
        editmatricule = (EditText) root.findViewById(R.id.etTitre);
        buttonCreer = (Button) root.findViewById(R.id.buttonCreer);
        editphone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        buttonCreer.setOnClickListener(new View.OnClickListener() {
            /**
             * Cette fonction gère ce qui ce passe lorsque l'on clic sur le bouton d'inscription. Elle ouvre un alertDialog avec les conditions d'utilisation de l'application que l'utilisateur doit accepter
             * @param view
             */
            @Override
            public void onClick(View view) {

                if ((!"".equals(editprenom.getText().toString().trim())) && (!"".equals(editnom.getText().toString().trim())) && (!"".equals(editmail.getText().toString().trim())) && (!"".equals(editpwrd.getText().toString().trim())) && (!"".equals(editcrfmpwrd.getText().toString().trim()))) {
                    if (editcrfmpwrd.getText().toString().trim().equals(editpwrd.getText().toString().trim())) {
                        if (checkMatricule(editmatricule.getText().toString().trim())) {
                            if (checkTextOnly(editprenom.getText().toString().trim())) {
                                if (checkTextOnly(editnom.getText().toString().trim())) {
                                    if (checkEmail(editmail.getText().toString())) {
                                        if (isValidEmail(editmail.getText().toString())) {
                                            if (!"".equals(editphone.getText().toString().trim()) && checkPhone(editphone.getText().toString().trim()) && editphone.getText().length() == 14) {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                builder.setCancelable(false);
                                                builder.setTitle("Responsabilités application mobile");

                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                    builder.setMessage(Html.fromHtml("Par le présent contrat, je m’engage à respecter les règlements de la consigne de livres usagés.  Ainsi :" +
                                                            "<br> •    Je consens à respecter la date limite pour la remise de l’argent et le renouvellement de mes livres et à ne pas revenir contre l’AGECTR inc. si je ne respecte pas la date (contrat d’un an);" +
                                                            "<br> •    Je consens que l’AGECTR inc. conserve mes livres et/ou l’argent provenant de la vente de ces derniers si je ne respecte pas la date limite pour récupérer mes biens;" +
                                                            "<br> •    Je prends note que si les livres ne sont pas vendus, je peux toujours renouveler le contrat directement au bureau de l’AGECTR ou par l’application mobile avant la date limite;" +
                                                            "<br> •    <b>L’AGECTR inc. se dégage de toute responsabilité en ce qui a trait au vol ou aux pertes de volumes.  Elle fera cependant tout ce qui est en son pouvoir afin d’éviter de telles situations.<b>\n", Html.FROM_HTML_MODE_LEGACY));
                                                }else{
                                                    builder.setMessage(Html.fromHtml("Par le présent contrat, je m’engage à respecter les règlements de la consigne de livres usagés.  Ainsi :" +
                                                            "<br> •    Je consens à respecter la date limite pour la remise de l’argent et le renouvellement de mes livres et à ne pas revenir contre l’AGECTR inc. si je ne respecte pas la date (contrat d’un an);" +
                                                            "<br> •    Je consens que l’AGECTR inc. conserve mes livres et/ou l’argent provenant de la vente de ces derniers si je ne respecte pas la date limite pour récupérer mes biens;" +
                                                            "<br> •    Je prends note que si les livres ne sont pas vendus, je peux toujours renouveler le contrat directement au bureau de l’AGECTR ou par l’application mobile avant la date limite;" +
                                                            "<br> •    <b>L’AGECTR inc. se dégage de toute responsabilité en ce qui a trait au vol ou aux pertes de volumes.  Elle fera cependant tout ce qui est en son pouvoir afin d’éviter de telles situations.<b>\n"));
                                                }
                                                builder.setPositiveButton("Accepter",
                                                        new DialogInterface.OnClickListener()
                                                        {
                                                            /**
                                                             * Cette fonction gère l'évenement du clic sur le bouton accepter lorsque l'on accepte la charte des responsabilités,Elle créée un nouvel utilisateur
                                                             * @param dialog
                                                             * @param which
                                                             */
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which)
                                                            {
                                                                matricule = editmatricule.getText().toString().trim();
                                                                prenom = editprenom.getText().toString().trim();
                                                                nom = editnom.getText().toString().trim();
                                                                tel = editphone.getText().toString().trim();
                                                                email = editmail.getText().toString().trim();
                                                                passwrd = editpwrd.getText().toString().trim();
                                                                data.initContext(root.getContext());
                                                                data.addUser(matricule, prenom, nom, tel, email, passwrd);
                                                                InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                                                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                                                        InputMethodManager.HIDE_NOT_ALWAYS);
                                                            }
                                                        });
                                                builder.setNeutralButton("Annuler", new DialogInterface.OnClickListener() {
                                                    /**
                                                     * Cette fonction gère l'événement du clic lorsque l'on clic sur le bouton annuler de la charte des responsaibilités, Elle affiche une message disant que l'on doit accepter les conditions d'utilisation
                                                     * @param dialogInterface
                                                     * @param i
                                                     */
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialog.cancel();
                                                        InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                                        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                                                InputMethodManager.HIDE_NOT_ALWAYS);
                                                    }
                                                });
                                                builder.setNegativeButton("Refuser", new DialogInterface.OnClickListener() {
                                                    /**
                                                     * Cette fonction gère l'événement du clic lorsque l'on clic sur le bouton refuser de la charte des responsaibilités, Elle ferme l'alertDialog
                                                     * @param dialog
                                                     * @param which
                                                     */
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Toast.makeText(root.getContext(), "Vous devez accepter les termes d'utilisation de l'application pour créer votre compte", Toast.LENGTH_LONG).show();
                                                        InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                                        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                                                InputMethodManager.HIDE_NOT_ALWAYS);
                                                    }
                                                });
                                                dialog = builder.create();
                                                dialog.show();
                                            } else
                                                Toast.makeText(root.getContext(), "Le numéro de téléphone est invalide", Toast.LENGTH_SHORT).show();
                                        } else
                                            Toast.makeText(root.getContext(), "L'adresse courriel entrée est invalide", Toast.LENGTH_SHORT).show();
                                    } else
                                        Toast.makeText(root.getContext(), "Vous ne pouvez pas entrer l'adresse courriel du cégep", Toast.LENGTH_SHORT).show();
                                } else
                                    Toast.makeText(root.getContext(), "Le nom entré est invalide", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(root.getContext(), "Le prénom entré est invalide", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(root.getContext(), "La format du matricule entré est invalide", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(root.getContext(), "Les mots de passe entrés ne sont pas équivalent", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(root.getContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();

            }
        });

        return root;
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

    /**
     * Cette fonction vient vérifier si le format du matricule entré est correct
     * @param matricule
     * @return boolean
     */
    public final static boolean checkMatricule(String matricule) {
        return Pattern.compile("^[0-9]{7}$").matcher(matricule).matches();
    }

    /**
     * Cette fonction vient vérifier le courriel entré contient edu.cegeptr.qc.ca, si oui --> invalide
     * @param email
     * @return boolean
     */
    public final static boolean checkEmail(String email) {

        if(email.contains("edu.cegeptr.qc.ca")){
            return false;
        }
        else return true;

    }

}
