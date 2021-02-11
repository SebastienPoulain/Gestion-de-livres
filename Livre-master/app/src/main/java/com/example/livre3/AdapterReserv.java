package com.example.livre3;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Émilie Cormier Houle
 * cette classe permet de remplir le recyclerview dans le fragment Reservation
 */

public class AdapterReserv extends RecyclerView.Adapter<AdapterReserv.MonViewHolder> {
    private List<Livre> listeLivres;
     Context context;
    TextView tv_Titre, tv_Auteur, tv_Editeur, tv_Edition;
    String lien;
    private InterfaceServeur serveur = RetrofitInstance.getInstance().create(InterfaceServeur.class);
    private DataSingleton data = DataSingleton.getInstance();

    public AdapterReserv(List<Livre> liste) {
        //this.context = context;
        this.listeLivres = liste;
    }


    @NonNull
    @Override
    public AdapterReserv.MonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_livres, parent, false);
        context = parent.getContext();
        return new MonViewHolder(view);
    }

    /**
     * Cette fonction permet d'associer les différentes données dans chaque item du recyclerview
     * @param holder
     * @param position
     */
    public void onBindViewHolder(@NonNull MonViewHolder holder,final int position) {

        holder.tv_Titre.setText(listeLivres.get(position).getTitre());
        holder.tv_Auteur.setText(String.valueOf(listeLivres.get(position).getAuteur()));
        holder.tv_Edition.setText(listeLivres.get(position).getEdition());
        holder.tv_Editeur.setText((listeLivres.get(position).getEditeur()));

        lien = RetrofitInstance.getInstance().baseUrl() + "img/" + listeLivres.get(position).getImgpath();

       Picasso.get().load(lien)
               .resize(220, 220)
               .centerCrop()
              .into(holder.img);
        holder.linear.setOnClickListener(new View.OnClickListener() {
            /**
             * Quand on clique sur un item du recyclerview cela ouvre un alertDialog qui permet de supprimer la réservation
             * @param v
             */
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(false);
                builder.setTitle("Supprimer");
                builder.setMessage("Voulez-vous vraimment annuler la réservation ?");

                builder.setPositiveButton("Oui",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Call<ResponseBody> call = serveur.supReservation(listeLivres.get(position).getIdC());

                                call.enqueue(new Callback<ResponseBody>() {
                                    /**
                                     * Ce call va supprimer la réservation de la base de données et de ce fait même de la section réservation de l'application
                                     * @param call
                                     * @param response
                                     */
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        String reponse = null;
                                        try {
                                            reponse = response.body().string();
                                            Toast.makeText(context, reponse, Toast.LENGTH_LONG).show();
                                        } catch (IOException e) {
                                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();

                                        }
                                        notifyItemRemoved(position);
                                        listeLivres.remove(position);
                                    }

                                    /**
                                     * Si il y a une erreur provenant du serveur, l'erreur sera afficher par cette fonction
                                     * @param call
                                     * @param t
                                     */
                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        });
                builder.setNegativeButton("Non",
                        new DialogInterface.OnClickListener() {
                            /**
                             * Quand on clique sur non dans le dialog, il se ferme
                             * @param dialog
                             * @param which
                             */
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            }
                        });
                builder.show();

            }
        });
    }

    /**
     * @return la quantité  de livres qui ont été réservée par la personne qui est connectée
     */
    @Override
    public int getItemCount() {
        return listeLivres.size();
    }

    public class MonViewHolder extends RecyclerView.ViewHolder {
        TextView tv_Titre, tv_Edition, tv_Editeur, tv_Auteur;
        ImageView img;
        LinearLayout linear;
        AlertDialog.Builder builder;
        AlertDialog.Builder builderInfo;


        public MonViewHolder(View view) {
            super(view);


            tv_Titre = view.findViewById(R.id.tv_Titre);
            tv_Edition = view.findViewById(R.id.tv_Edition);
            tv_Editeur = view.findViewById(R.id.tv_Editeur);
            tv_Auteur = view.findViewById(R.id.tv_Auteur);
            linear =view.findViewById(R.id.linearlayoutid);
            img = view.findViewById(R.id.recyclerItem_imageView);
            builder = new AlertDialog.Builder(view.getContext());
            builderInfo = new AlertDialog.Builder(view.getContext());


        }
    }

}


