package com.example.livre3;




import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * @author Jérémy Lampron et Sébastien Poulain
 * Cette classe gère le fragment de connexion.
 */

public class LoginFragment extends Fragment {

    TextView tvForgot,tvBookDeja;
    Context context;
    Button btConnexion,btCreerCompte;
    EditText tbCourriel;
    EditText tbMotdepasse;
    ProgressBar progressBar;
    CheckBox chkRemember;
    private DrawerLayout drawer;
    NavigationView navigationView;
    Menu nav_Menu;
    AlertDialog dialog;
    View headerView;
    Toolbar toolbar;
    Fragment Inscription;
   private InterfaceServeur serveur = RetrofitInstance.getInstance().create(InterfaceServeur.class);
    private DataSingleton data = DataSingleton.getInstance();

    /**
     * Cette fonction initialise les différents composants
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.connexion, container, false);

         toolbar =  getActivity().findViewById(R.id.toolbar);
        navigationView = getActivity().findViewById(R.id.nav_view);

        nav_Menu = navigationView.getMenu();
        headerView = navigationView.getHeaderView(0);

        navigationView.bringToFront();

        drawer = getActivity().findViewById(R.id.drawer_layout);
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) getActivity());

        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

       btCreerCompte= root.findViewById(R.id.btModifPassword);
        tvBookDeja = root.findViewById(R.id.tvBookDeja);
        btConnexion     = root.findViewById(R.id.btModif);
        tbCourriel      = root.findViewById(R.id.txttitle);
        tbMotdepasse    = root.findViewById(R.id.login_tbMotdepasse);
        tvForgot = root.findViewById(R.id.tvforgotpass);
        chkRemember = root.findViewById(R.id.chkRemember);
        context=getContext();


      btConnexion.setOnClickListener(new View.OnClickListener() {

          /**
           * Cette fonction gère l'évenement du clic sur le bouton connexion et envoit au serveur les informations de connexion pour les comparer à ceux de la base de données
           * @param view
           */
            @Override
            public void onClick(View view) {
            Call<User> call = serveur.getUserLogin(tbCourriel.getText().toString().trim(), data.getSha256Hash(tbMotdepasse.getText().toString().trim()));

            call.enqueue(new Callback<User>() {
                /**
                 * Cette fonction interprète la réponse renvoyer par le fichier PHP getUserLogin
                 * @param call
                 * @param response
                 */
                @Override
                public void onResponse(Call<User> call, Response<User> response) {

                    if (response.body().getNom() == null){
                        Toast.makeText(context.getApplicationContext(), "Adresse courriel ou mot de passe invalide" ,Toast.LENGTH_SHORT).show();
                    }
                    else if (response.body().getNom().equals("inactive")){
                        Toast.makeText(context.getApplicationContext(), "Ce compte n'a pas encore été activé, veuillez vous déplacez au bureau de l'AGECTR pour l'activer" ,Toast.LENGTH_LONG).show();
                    }
                    else {
                        data.setConnectedUser(response.body());
                        if(chkRemember.isChecked()) {
                            SharedPreferences pref = getActivity().getSharedPreferences("login", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putBoolean("connected", true);
                            editor.putString("email", data.getConnectedUser().getEmail());
                            editor.putBoolean("start", false);
                            editor.commit();
                        }
                        Toast.makeText(context.getApplicationContext(), "Connexion réussie", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        intent.setAction("com.info.lesreceivers.MON_ACTION2");
                        getContext().sendBroadcast(intent);
                    }

                }

                /**
                 * Cette fonction interprète l'erreur généré par le PHP
                 * @param call
                 * @param t
                 */
                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(context.getApplicationContext(), "Il y a une erreur avec le serveur de l'application" ,Toast.LENGTH_SHORT).show();
                }
            });

            }
        });

        tvBookDeja.setOnClickListener(new View.OnClickListener() {
            /**
             * Cette fonction gère l'évenement du clic sur le text disant que l'on a déjà des livres a l'AGECTR, mais que l'on a pas de compte. Elle affiche une alerte Dialog
             * @param view
             */
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(false);
                builder.setTitle("Compte existant à l'AGECTR");
                builder.setMessage("Si vous avez des livres à l'AGECTR et que vous avez donné votre adresse courriel à ceux-ci, vous devez cliquez sur le bouton 'obtenir un mot de passe' et inscrire votre adresse courriel sur la page recouvrement de mot de passe, ceci va vous générer un mot de passe que vous recevrez par courriel. Sinon, vous devez vous rendre à l'AGECTR pour avoir accès à votre compte.");

                builder.setPositiveButton("Annuler",
                        new DialogInterface.OnClickListener()
                        {
                            /**
                             * Cette fonction gère l'évenement du clic sur le bouton Annuler de la fenêtre de dialogue, elle ferme l'alertDialog
                             * @param dialog
                             * @param which
                             */
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                            dialog.cancel();
                            }
                        });
                builder.setNeutralButton("Obtenir un mot de passe",
                        new DialogInterface.OnClickListener()
                        {
                            /**
                             * Cette fonction gère l'évenement du clic sur le bouton Obtenir un mot de passe de la fenêtre de dialogue.Elle ouvre le fragment mot de passe oublié
                             * @param dialog
                             * @param which
                             */
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                FragmentTransaction fr=getFragmentManager().beginTransaction();
                                fr.replace(R.id.flFragment,new Forgot());
                                fr.addToBackStack(null);
                                fr.commit();
                                toolbar.setTitle("Recouvrement de mot de passe");
                            }
                        });
                dialog = builder.create();
                dialog.show();
            }
        });

        tvForgot.setOnClickListener(new View.OnClickListener() {
            /**
             * Cette fonction gère l'évenement du clic sur le texte Mot de passe oublié, Elle ouvre le fragment mot de passe oublié
             * @param view
             */
            @Override
            public void onClick(View view) {
                FragmentTransaction fr=getFragmentManager().beginTransaction();
                fr.replace(R.id.flFragment,new Forgot());
                fr.addToBackStack(null);
                fr.commit();
                toolbar.setTitle("Recouvrement de mot de passe");
            }
        });

        btCreerCompte.setOnClickListener(new View.OnClickListener() {
            /**
             * Cette fonction gère l'évenement du clic sur le bouton Création de compte, Elle ouvre le fragment création de compte
             * @param view
             */
           @Override
            public void onClick(View view) {
               FragmentTransaction fr=getFragmentManager().beginTransaction();
               fr.replace(R.id.flFragment,new Inscription());
               fr.addToBackStack(null);
                fr.commit();
                toolbar.setTitle("Inscription");
            }
        });



        return root;
    }

    /**
     * Cette fonction vient reset le fragment de connexion
     */
    @Override
    public void onResume() {
        super.onResume();
        toolbar.setTitle("Connexion");
        clearBackStack();
        tbCourriel.setText("");
        tbMotdepasse.setText("");
    }

    /**
     * Cette fonction vide la pile contenant les fragments, utilisé pour le changement de fragments
     */
    private void clearBackStack() {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }
}