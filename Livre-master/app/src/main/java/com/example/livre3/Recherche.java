package com.example.livre3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

/**
 * @author  Sébastien Poulain
 * dans ce fragment on peut inscire les informations par lesquelles on veut faire notre recherche de livres
 */

public class Recherche extends Fragment {

    Button btnRech;
    EditText etTitre,etAuteur,etEditeur,etEdition,etCode;
    TextView txtTitre,txtAuteur,txtEditeur,txtEdition,txtCode;
    private DrawerLayout drawer;
    NavigationView navigationView;
    Menu nav_Menu;
    View headerView;
    View root;
    Toolbar toolbar;

    /**
     * fonction qui initialise les différents composants
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return root
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

         root = inflater.inflate(R.layout.rechercher2, container, false);

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


        btnRech = root.findViewById(R.id.btnRechercher);
        etTitre = root.findViewById(R.id.etTitre);
        etAuteur = root.findViewById(R.id.etAuteur);
        etEditeur = root.findViewById(R.id.etEditeur);
        etEdition = root.findViewById(R.id.etEdition);
        etCode = root.findViewById(R.id.etCode);

        txtTitre = root.findViewById(R.id.txtTitre);
        txtAuteur = root.findViewById(R.id.txtAuteur);
        txtEditeur = root.findViewById(R.id.txtEditeur);
        txtEdition = root.findViewById(R.id.txtEdition);
        txtCode = root.findViewById(R.id.txtCode);

        btnRech.setOnClickListener(new View.OnClickListener() {
            /**
             *
             * Quand on clique sur le bouton rechercher, on transfert l'information des champs de recherche au fragment suivant soit HomeFragment, qui lui enverra les informations au serveur pour afficher les livres associés à la recherche.
             */
            @Override
            public void onClick(View v) {

                if(etTitre.getText().toString().trim().equals("") && etAuteur.getText().toString().trim().equals("") && etEditeur.getText().toString().trim().equals("") && etEdition.getText().toString().trim().equals("") && etCode.getText().toString().trim().equals("")) {
                    Toast.makeText(getContext(), "Vous devez rechercher par au moins un champ", Toast.LENGTH_LONG).show();
                }
                else {


                    Fragment fragment = new HomeFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("titre", etTitre.getText().toString().trim());
                    bundle.putString("auteur", etAuteur.getText().toString().trim());
                    bundle.putString("editeur", etEditeur.getText().toString().trim());
                    bundle.putString("edition", etEdition.getText().toString().trim());
                    bundle.putString("code", etCode.getText().toString().trim());
                    fragment.setArguments(bundle);

                    FragmentTransaction fr = getFragmentManager().beginTransaction();
                    fr.replace(R.id.flFragment, fragment);
                    fr.addToBackStack(null);
                    fr.commit();
                    toolbar.setTitle("Résultats de la recherche");

                }
            }
        });


        return root;
    }

    /**
     * Quand on revient sur ce fragment on efface les champs de texte et on remet le titre de la page au titre du fragment soit 'recherche de livres'
     */
    @Override
    public void onResume() {
        super.onResume();
        etTitre.setText("");
        etAuteur.setText("");
        etEditeur.setText("");
        etEdition.setText("");
        etCode.setText("");
        toolbar.setTitle("Recherche de livres");
    }
}
