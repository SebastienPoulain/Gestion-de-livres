package com.example.livre3;


import android.content.BroadcastReceiver;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
 * @author Jérémy Lampron et Émilie Cormier Houle et Sébastien Poulain
 * cette classe permet d'afficher la liste des livres de l'utilisateur et d'ajouter d'autres livres
 */

public class GestionLivreFragment extends Fragment {
    Button ajouter;
    View root;
    TextView tvMessageVide;
    Fragment fragAjout;
    List<Livre> listelivres;
    MesLivresAdapter myAdapter;
    private DrawerLayout drawer;
    RecyclerView myrv;
    NavigationView navigationView;
    Menu nav_Menu;
    View headerView;
    Toolbar toolbar;
    private InterfaceServeur serveur = RetrofitInstance.getInstance().create(InterfaceServeur.class);
    private DataSingleton data = DataSingleton.getInstance();
    private FragmentAjout callbackFragment;

    public interface FragmentAjout{
        public void gerer();
    }

    /**
     * cette fonction permet d'initialiser les différents composants
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return root
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       root = inflater.inflate(R.layout.fragment_gestiondelivres, container, false);






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
        myAdapter = new MesLivresAdapter(listelivres);
        ajouter =root.findViewById(R.id.btnajout);
        tvMessageVide = root.findViewById(R.id.tvMessageVide);
        ajouter.setOnClickListener(new View.OnClickListener() {
            /**
             * Cette fonction permet de rafraichir la liste du recycleview
             * @param view
             */
            @Override
            public void onClick(View view) {
                toolbar.setTitle("Ajout de livre");
                callbackFragment.gerer();

            }
        });

        Call<List<Livre>> call = serveur.getMesLivres(data.getConnectedUser().getId());

                call.enqueue(new Callback<List<Livre>>() {
                    /**
                     * réponse du serveur lors de la réception des livres de la personne conntectée
                     * @param call
                     * @param response
                     */
                    @Override
                    public void onResponse(Call<List<Livre>> call, Response<List<Livre>> response) {
                        List<Livre> reponse = response.body();

                        if(reponse.get(0).getTitre() == null){
                            tvMessageVide.setVisibility(root.VISIBLE);
                        }
                        else{
                            for (int i = 0; i < reponse.size(); i++) {
                                Livre temp = new Livre(reponse.get(i).getId(), reponse.get(i).getTitre(), reponse.get(i).getAuteur(), reponse.get(i).getEditeur(), reponse.get(i).getEdition(), reponse.get(i).getCodeBarre(), reponse.get(i).getImgpath(),reponse.get(i).getIdC(), reponse.get(i).getPrix(), reponse.get(i).getAnnotation(), reponse.get(i).getExpireDate());
                                listelivres.add(temp);
                            }

                            myrv.setHasFixedSize(false);
                            myrv.setLayoutManager(new LinearLayoutManager(getContext()));
                            myrv.setAdapter(myAdapter);}

            }

                    /**
                     * affichage du messge d'erreur si problème au niveau du serveur
                     * @param call
                     * @param t
                     */
            @Override
            public void onFailure(Call<List<Livre>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });




        return root;
    }

    /**
     * fonction qui attache le contexte au callback
     * @param context
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        callbackFragment = (FragmentAjout) context;
    }

}