package com.example.livre3;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;


import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Sébastien Poulain
 * cette activité gère toute l'application
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, GestionLivreFragment.FragmentAjout {

   Fragment fragProfil,fragHome,fragLogin,fragshare,fragPropos,fragReservation, fragHome2,fragRecherche, fragAjout,fragMesLivres;
    private DrawerLayout drawer;
    NavigationView navigationView;
    Menu nav_Menu;
    MenuItem item2;
    View headerView;
    TextView tvPersonneNom;
    Toolbar toolbar;
    MonBroadcastReceiver br;
    MonBroadcastReceiver2 br2;
    MonBroadcastReceiver3 br3;
    MonBroadcastReceiver4 br4;

    int frag;
    DataSingleton data = DataSingleton.getInstance();

    private InterfaceServeur serveur = RetrofitInstance.getInstance().create(InterfaceServeur.class);
    @Override
    /**
     * vérifie les préférences partagés pour voir si l'utilisateur est toujours connecté
     * initialise les différents composants
     *
     *
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //item2=findViewById(R.id.info);

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("start",true);
        editor.commit();
        boolean estConnecte = prefs.getBoolean("connected", false);
        String email = prefs.getString("email", "");
        if (estConnecte) {
            Call<User> call = serveur.Reconnecter(email);

            call.enqueue(new Callback<User>() {
                /**
                 * réponse du serveur lors de la reconnection
                 * @param call
                 * @param response
                 */
                @Override
                public void onResponse(Call<User> call, Response<User> response) {

                        data.setConnectedUser(response.body());
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("login", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putBoolean("connected",true);
                        editor.putString("email",data.getConnectedUser().getEmail());
                        editor.commit();

                    Intent intent = new Intent();
                    intent.setAction("com.info.lesreceivers.MON_ACTION2");
                   getApplicationContext().sendBroadcast(intent);
                    }

                /**
                 * affichage du message d'erreur si le serveur ne répond pas
                 * @param call
                 * @param t
                 */
                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Il y a une erreur avec le serveur de l'application" ,Toast.LENGTH_SHORT).show();
                }
            });

        }

        br = new MonBroadcastReceiver();

        IntentFilter filter = new IntentFilter("com.info.lesreceivers.MON_ACTION");
        this.registerReceiver(br, filter);

        br2 = new MonBroadcastReceiver2();

        IntentFilter filter2 = new IntentFilter("com.info.lesreceivers.MON_ACTION2");
        this.registerReceiver(br2, filter2);

        br3 = new MonBroadcastReceiver3();
        IntentFilter filter3 = new IntentFilter("com.info.lesreceivers.MON_ACTION3");
        this.registerReceiver(br3, filter3);

        br4 = new MonBroadcastReceiver4();

        IntentFilter filter4 = new IntentFilter("com.info.lesreceivers.MON_ACTION4");
        this.registerReceiver(br4, filter4);



         toolbar = findViewById(R.id.toolbar);
         toolbar.setTitle("Recherche de livres");
        setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.nav_view);

        nav_Menu = navigationView.getMenu();
        headerView = navigationView.getHeaderView(0);

        navigationView.bringToFront();


        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        fragRecherche = new Recherche();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.flFragment,fragRecherche);
        fragmentTransaction.commit();

        toolbar.setTitle("Recherche de livres");

        onNavigationItemSelected(navigationView.getMenu().getItem(0));

    }

    /**
     * quand on revient dans l'activité on se remet à écouter les actions faites auparavant
     *
     */
    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter("com.info.lesreceivers.MON_ACTION");
        this.registerReceiver(br, filter);

        IntentFilter filter2 = new IntentFilter("com.info.lesreceivers.MON_ACTION2");
        this.registerReceiver(br2, filter2);

        IntentFilter filter3 = new IntentFilter("com.info.lesreceivers.MON_ACTION3");
        this.registerReceiver(br3, filter3);

        IntentFilter filter4 = new IntentFilter("com.info.lesreceivers.MON_ACTION4");
        this.registerReceiver(br4, filter4);



    }
    /**
     * fonction pour retourner au fragment ajoutlivre après avoir ajouté un livre
     *
     */
    public void gerer(){
        fragAjout = new Ajoutlivre();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flFragment,fragAjout);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    /**
     * création du menu de navigation (menu en haut)
     *
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        item2 = menu.getItem(0);

        return true;
    }/**
     * bouton info
     * permet de donne de l,infomation a l'usagé
     *
     */

    /**
     * permet de savoir si un élément du menu est sélectionné
     * @param item
     * @return boolean
     */

    public boolean onOptionsItemSelected(MenuItem item) {



if (frag==1) {
    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setCancelable(false);
    builder.setTitle("Réservation");
    builder.setMessage("Vous avez 36 heures pour allez chercher votre livre, Après vous devrez refaire la réservation.");

    builder.setPositiveButton("Ok",
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
    builder.show();
    return true;
}else{
    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setCancelable(false);
    builder.setTitle("Concession");
    builder.setMessage("Votre livre ne sera pas afficher tant que l'AGE n'aura pas accepté votre livre.");

    builder.setPositiveButton("Ok",
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
    builder.show();
    return true;
}


    }
    /**
     * reçoit  les permissions valides et notifie les refusées
     *
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        int nbPermissionsRefusees = 0;

        for(int i = 0; i<grantResults.length; i++ )
        {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {

                nbPermissionsRefusees++;
            }
        }

        if(nbPermissionsRefusees > 0) {

            Toast.makeText(this, "Veuillez accepter les permissions--", Toast.LENGTH_LONG).show();

            /*fragAjout = new GestionLivreFragment();

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.flFragment,fragAjout);
            fragmentTransaction.commit();
            dialog.dismiss();*/
        }

    }

    /**
     * différentes actions selon l'élément sélectionné dans le navigation drawer
     *
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId())
        {

            case R.id.nav_home:

                fragHome = new Recherche();

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flFragment,fragHome);
                fragmentTransaction.commit();
                item.setChecked(true);
                toolbar.setTitle("Recherche de livres");
                item2.setVisible(false);
                break;

            case R.id.nav_login:

                fragLogin = new LoginFragment();

                FragmentManager fragmentManager3 = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction3 = fragmentManager3.beginTransaction();
                fragmentTransaction3.replace(R.id.flFragment,fragLogin);
                fragmentTransaction3.commit();
                item.setChecked(true);
               toolbar.setTitle("Connexion");
                item2.setVisible(false);
                break;


            case R.id.nav_profil:

            fragProfil = new ProfilFragment();

            FragmentManager fragmentManager2 = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
            fragmentTransaction2.replace(R.id.flFragment,fragProfil);
            fragmentTransaction2.commit();
                item.setChecked(true);
                toolbar.setTitle("Profil");
                item2.setVisible(false);
            break;

            case R.id.nav_gestion_livres:

                fragshare = new GestionLivreFragment();

                FragmentManager fragmentManager4 = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction4 = fragmentManager4.beginTransaction();
                fragmentTransaction4.replace(R.id.flFragment,fragshare);
                fragmentTransaction4.commit();
                item.setChecked(true);
                toolbar.setTitle("Gestion des livres");
                item2.setVisible(true);
                frag=0;
                break;

            case R.id.nav_propos:

                fragPropos = new ProposFragment();

                FragmentManager fragmentManager5 = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction5 = fragmentManager5.beginTransaction();
                fragmentTransaction5.replace(R.id.flFragment,fragPropos);
                fragmentTransaction5.commit();
                item.setChecked(true);
                toolbar.setTitle("À propos");
                item2.setVisible(false);
                break;
            case R.id.nav_reservation_livres:

                fragReservation = new Reservation();

                FragmentManager fragmentManager6 = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction6 = fragmentManager6.beginTransaction();
                fragmentTransaction6.replace(R.id.flFragment,fragReservation);
                fragmentTransaction6.commit();
                item.setChecked(true);
                item2.setVisible(true);
                frag=1;
                toolbar.setTitle("Réservation de livres");
                break;

            case R.id.nav_logout:
                data.initContext(getApplicationContext());
                data.deconnexion();
                Intent intent = new Intent();
                intent.setAction("com.info.lesreceivers.MON_ACTION2");
                getApplicationContext().sendBroadcast(intent);


                item.setChecked(true);


                Toast.makeText(getApplicationContext(), "Déconnexion réussie" ,Toast.LENGTH_SHORT).show();
                fragHome2 = new Recherche();

                FragmentManager fragmentManager7 = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction7 = fragmentManager7.beginTransaction();
                fragmentTransaction7.replace(R.id.flFragment,fragHome2);
                fragmentTransaction7.commit();

                nav_Menu.findItem(R.id.nav_gestion_livres).setVisible(false);
                nav_Menu.findItem(R.id.nav_reservation_livres).setVisible(false);
                nav_Menu.findItem(R.id.nav_login).setVisible(true);
                nav_Menu.findItem(R.id.nav_logout).setVisible(false);
                nav_Menu.findItem(R.id.nav_profil).setVisible(false);
                item2.setVisible(false);
               clearBackStack();
                toolbar.setTitle("Recherche de livres");
                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * broadcastreceiver pour retourner au fragment de connexion après s'être inscrit
     */
    public class MonBroadcastReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {


            fragLogin = new LoginFragment();

            FragmentManager fragmentManager2 = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
            fragmentTransaction2.replace(R.id.flFragment,fragLogin);
            fragmentTransaction2.commit();

            toolbar.setTitle("Connexion");

        }
    }
    /**
     * broadcastreceiver qui écoute lorsque l'on se connecte et qu'on vient de se connecter
     */
    public class MonBroadcastReceiver2 extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {

            tvPersonneNom = findViewById(R.id.tvPersonneNom);
            tvPersonneNom = findViewById(R.id.tvPersonneNom);
            if (data.getEtatConnexion() == true) {

                SharedPreferences prefs = getApplicationContext().getSharedPreferences("login", MODE_PRIVATE);

                boolean start = prefs.getBoolean("start", false);

                if(start){
                    tvPersonneNom.setText(data.getConnectedUser().getPrenom() + " " + data.getConnectedUser().getNom());
                    nav_Menu.findItem(R.id.nav_gestion_livres).setVisible(true);
                    nav_Menu.findItem(R.id.nav_reservation_livres).setVisible(true);
                    nav_Menu.findItem(R.id.nav_login).setVisible(false);
                    nav_Menu.findItem(R.id.nav_logout).setVisible(true);
                    nav_Menu.findItem(R.id.nav_profil).setVisible(true);
                    fragHome = new Recherche();

                    FragmentManager fragmentManager2 = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                    fragmentTransaction2.replace(R.id.flFragment,fragHome);
                    fragmentTransaction2.commit();

                    onNavigationItemSelected(navigationView.getMenu().getItem(0));

                }
                else {
                    tvPersonneNom.setText(data.getConnectedUser().getPrenom() + " " + data.getConnectedUser().getNom());
                    fragHome = new Recherche();

                    FragmentManager fragmentManager2 = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                    fragmentTransaction2.replace(R.id.flFragment,fragHome);
                    fragmentTransaction2.commit();

                    InputMethodManager imm = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);


                    onNavigationItemSelected(navigationView.getMenu().getItem(0));

                    nav_Menu.findItem(R.id.nav_gestion_livres).setVisible(true);
                    nav_Menu.findItem(R.id.nav_reservation_livres).setVisible(true);
                    nav_Menu.findItem(R.id.nav_login).setVisible(false);
                    nav_Menu.findItem(R.id.nav_logout).setVisible(true);
                    nav_Menu.findItem(R.id.nav_profil).setVisible(true);
                    toolbar.setTitle("Recherche de livres");

                }

            }
            else tvPersonneNom.setText("Veuillez vous connecter");
            fragRecherche = new Recherche();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.flFragment,fragRecherche);
            fragmentTransaction.commit();
            onNavigationItemSelected(navigationView.getMenu().getItem(0));
        }
    }


    /**
     * broadcastreceiver pour retourner au fragment de profil lorsque l'on a modifié notre profil
     */
    public class MonBroadcastReceiver3 extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {


            fragProfil = new ProfilFragment();

            FragmentManager fragmentManager2 = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
            fragmentTransaction2.replace(R.id.flFragment,fragProfil);
            fragmentTransaction2.commit();
            tvPersonneNom.setText(data.getConnectedUser().getPrenom() + " " + data.getConnectedUser().getNom());
            toolbar.setTitle("Profil");
        }
    }
    /**
     * broadcastreceiver pour retourner au fragment de gestion de livres lors de la modification d'un livre
     */
    public class MonBroadcastReceiver4 extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {


            fragMesLivres = new GestionLivreFragment();

            FragmentManager fragmentManager2 = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
            fragmentTransaction2.replace(R.id.flFragment,fragMesLivres);
            fragmentTransaction2.commit();

            toolbar.setTitle("Gestion des livres");

        }
    }

    /**
     * fonction qui permet d'effacer la pile de fragment (utilisé à la déconnexion)
     */
    private void clearBackStack() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }
    /**
     * gère la fleche de retour (retourne au fragment précédent) si le conteneur de fragments contient d'autres fragments
     *
     */
    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            //Do nothing
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }



}
