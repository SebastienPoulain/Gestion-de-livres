package com.example.livre3;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * Reservation
 *
 * permet d'afficher la liste des réservations de l'utilisateur connecté
 *
 *
 * @author  Émilie cormier houle
 * 
 */
public class Reservation extends Fragment{
    View root;
    TextView tvMessageVide;
    List<Livre> listelivres;
    AdapterReserv myAdapter;
    private DrawerLayout drawer;
    RecyclerView myrv;
    NavigationView navigationView;
    Menu nav_Menu;
    View headerView;
    Toolbar toolbar;
    private InterfaceServeur serveur = RetrofitInstance.getInstance().create(InterfaceServeur.class);
    private DataSingleton data = DataSingleton.getInstance();
    private GestionLivreFragment.FragmentAjout callbackFragment;

    /**
     * fonction qui initialise les différentes composants
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return root
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.reserver_layout, container, false);


        toolbar =  getActivity().findViewById(R.id.toolbar);
        navigationView = getActivity().findViewById(R.id.nav_view);

        nav_Menu = navigationView.getMenu();
        headerView = navigationView.getHeaderView(0);


        navigationView.bringToFront();
        myrv = (RecyclerView) root.findViewById(R.id.rv_meslivres);

        drawer = getActivity().findViewById(R.id.drawer_layout);
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) getActivity());

        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        listelivres = new ArrayList<Livre>();
        myAdapter = new AdapterReserv(listelivres);

        tvMessageVide = root.findViewById(R.id.tvMessageVide);

        Call<List<Livre>> call = serveur.getMesReservation(data.getConnectedUser().getId());

        call.enqueue(new Callback<List<Livre>>() {
            /**
             *  Ce call permet d'aller chercher toutes les réservations de la personne connectée
             */
            @Override
            public void onResponse(Call<List<Livre>> call, Response<List<Livre>> response) {
                List<Livre> reponse = response.body();


                if(reponse.get(0).getTitre() == null){
                    tvMessageVide.setVisibility(root.VISIBLE);
                }
                else{
                    for (int i = 0; i < reponse.size(); i++) {
                        Livre temp = new Livre(reponse.get(i).getId(),reponse.get(i).getTitre(), reponse.get(i).getAuteur(), reponse.get(i).getEditeur(), reponse.get(i).getEdition(), reponse.get(i).getCodeBarre(), reponse.get(i).getImgpath(),reponse.get(i).getIdC());
                        listelivres.add(temp);
                    }

                    myrv.setHasFixedSize(false);
                    myrv.setLayoutManager(new LinearLayoutManager(getContext()));
                    myrv.setAdapter(myAdapter);}

            }
            /**
             *  Cette fonction permet d'afficher les erreurs qui pourraient survenir lors de l'appel au fichier php (serveur)
             */
            @Override
            public void onFailure(Call<List<Livre>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });




        return root;
    }

    /**
     * On informe les modifications de la liste à l'adapter pour que par la suite il soit actualisé
     */
    @Override
    public void onResume() {
        super.onResume();
        myAdapter.notifyDataSetChanged();
    }

    /**
     *
     * Quand on attache le fragment on lui donne son contexte
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        callbackFragment = (GestionLivreFragment.FragmentAjout) context;
    }


}

