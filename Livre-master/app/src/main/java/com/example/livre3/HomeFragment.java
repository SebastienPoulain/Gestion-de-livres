
package com.example.livre3;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Sébastien Poulain
 * cette classe affiche les livres résultants de la recherche de livre
 */

public class HomeFragment extends Fragment {

    private RecyclerView.Adapter myAdapter;
    List<Livre> listelivres;
    private InterfaceServeur serveur = RetrofitInstance.getInstance().create(InterfaceServeur.class);
    private DataSingleton data = DataSingleton.getInstance();
    TextView tvAucun;
    View root;
    Toolbar toolbar;

    /**
     * cette fonction permet d'initialiser les différents composants
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return root
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        listelivres  = new ArrayList<Livre>();

       root = inflater.inflate(R.layout.fragment_home, container, false);
         toolbar =  getActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
                toolbar.setTitle("Recherche de livres");
            }
        });

       tvAucun = root.findViewById(R.id.tvAucun);

        String title = getArguments().getString("titre", "");
        String auteur = getArguments().getString("auteur", "");
        String edition = getArguments().getString("edition", "");
        String editeur = getArguments().getString("editeur", "");
        String code = getArguments().getString("code", "");

        Call<List<Livre>> call = serveur.RechercherLivre(title,auteur,editeur,edition,code);

        call.enqueue(new Callback<List<Livre>>() {
            /**
             * Réponse du serveur lors de la recherche de livre
             * @param call
             * @param response
             */
            @Override
            public void onResponse(Call<List<Livre>> call, Response<List<Livre>> response) {
                List<Livre> reponse = response.body();

                if (reponse.get(0).getTitre() == null){
                    tvAucun.setVisibility(root.VISIBLE);
                }
                else {
                    for (int i = 0; i < reponse.size(); i++) {
                        Livre temp = new Livre(reponse.get(i).getId(),reponse.get(i).getTitre(), reponse.get(i).getAuteur(), reponse.get(i).getEdition(), reponse.get(i).getEditeur(), reponse.get(i).getCodeBarre(), reponse.get(i).getImgpath());
                        listelivres.add(temp);
                    }
                }
                    RecyclerView myrv = (RecyclerView) root.findViewById(R.id.recycler_view);
                    BookAdapter myAdapter = new BookAdapter(getActivity(),listelivres);
                    myrv.setLayoutManager(new GridLayoutManager(getActivity(),3));
                    myrv.setAdapter(myAdapter);

            }

            /**
             * affichage du message d'erreur si problème au niveau du serveur
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
     * quand on revient sur ce fragment, le titre du fragment revient à 'Résultat de la recherche'
     */
    @Override
    public void onResume() {
        super.onResume();
        toolbar.setTitle("Résultats de la recherche");
    }
}
