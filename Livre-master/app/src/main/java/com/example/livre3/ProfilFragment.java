package com.example.livre3;


/**
 * @author Jérémy Lampron
 * cette classe permet d'afficher les différents informations du compte de l'utilisateur connecté
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

public class ProfilFragment extends Fragment {

    DataSingleton data = DataSingleton.getInstance();
    EditText editprenom, editnom, editnum, editcourriel;
    Button btModif, btModifPassword;
    private DrawerLayout drawer;
    NavigationView navigationView;
    Menu nav_Menu;
    View headerView;
    Toolbar toolbar;

    /**
     *cette fonction permet d'initialiser les différents composants
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        User user = data.getConnectedUser();

            View root = inflater.inflate(R.layout.profil2, container, false);
            editprenom = root.findViewById(R.id.tbPrenom);
            editnom = root.findViewById(R.id.txttitle);
            editnum = root.findViewById(R.id.tbCell);
            editcourriel = root.findViewById(R.id.tbCourriel);
            btModifPassword = root.findViewById(R.id.btModifPassword);
            btModif = root.findViewById(R.id.btModif);

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



        editprenom.setText(user.getPrenom());
            editnom.setText(user.getNom());
            editcourriel.setText(user.getEmail());
            editnum.setText(user.getTel());
        btModif.setOnClickListener(new View.OnClickListener() {
            /**
             * quand on clique sur le bouton modifier, cela nous amène sur le fragment modifierProfil
             * @param view
             */
            @Override
            public void onClick(View view) {

                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.flFragment, new Modif_info());
                fr.addToBackStack(null);
                fr.commit();
                toolbar.setTitle("Modifier mon profil");

            }
        });

        btModifPassword.setOnClickListener(new View.OnClickListener() {
            /**
             * Quand on clique sur le bouton modifier mot de passe, cela nous amène sur le fragment modifier mot de passe
             * @param view
             */
            @Override
            public void onClick(View view) {

                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.flFragment, new Modif_password());
                fr.addToBackStack(null);
                fr.commit();
                toolbar.setTitle("Modifier mon mot de passe");
            }


        });

            return root;

    }

}

