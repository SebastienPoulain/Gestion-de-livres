package com.example.livre3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 *cette classe permet de remplir le recyclerview dans le fragment gestionlivrefragment
 * @author Jérémy Lampron
 * @version 2020 Mars
 * @since 1.0
 */

public class MesLivresAdapter extends RecyclerView.Adapter<MesLivresAdapter.MonViewHolder> {
    private List<Livre> listeLivres;

        Context context;
        TextView tv_Titre, tv_Auteur, tv_Editeur, tv_Edition;
        String lien;
        Toolbar toolbar;
        AlertDialog alertDialog;
        Date expireDate, newexpireDate;
        SimpleDateFormat format1;
        Integer position2;
        RecyclerView myrv;

    private InterfaceServeur serveur = RetrofitInstance.getInstance().create(InterfaceServeur.class);
    /**
     *
     *
     */
    public MesLivresAdapter(List<Livre> liste) {
        //this.context = context;
        this.listeLivres = liste;
    }

    @NonNull
    @Override
    public MesLivresAdapter.MonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_livres, parent, false);
        context = parent.getContext();
        myrv = view.findViewById(R.id.rv_meslivres);

        return new MonViewHolder(view);




    }
    /**
     * Cette fonction permet d'associer les différentes données dans chaque item du recyclerview
     * @param holder
     * @param position
     */
    public void onBindViewHolder(@NonNull MonViewHolder holder, final int position) {
        position2 = position;
        holder.tv_Titre.setText(listeLivres.get(position).getTitre());
        holder.tv_Auteur.setText(String.valueOf(listeLivres.get(position).getAuteur()));
        holder.tv_Edition.setText(listeLivres.get(position).getEdition());
        holder.tv_Editeur.setText((listeLivres.get(position).getEditeur()));

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            expireDate = formatter.parse(listeLivres.get(position).getExpireDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (new Date().after(expireDate)) {
            holder.linearLayoutid.setBackgroundResource(R.drawable.shapelogin2);
            }

        lien = RetrofitInstance.getInstance().baseUrl() + "img/" + listeLivres.get(position).getImgpath();

       Picasso.get().load(lien)
               .resize(220, 220)
               .centerCrop()
              .into(holder.img);

        holder.linearLayoutid.setOnClickListener(new View.OnClickListener() {
            /**
             * Quand on clique sur un item du recyclerview soit une concession, le fragment de détail meslivredetails apparait et on voit toutes les informations concernant sa concession
             * @param v
             */
            @Override
            public void onClick(View v) {

                MesLivresDetailsFragment fragment = new MesLivresDetailsFragment();
                Bundle args = new Bundle();
                args.putInt("id",listeLivres.get(position).getId());
                args.putString("Title",listeLivres.get(position).getTitre());
                args.putString("auteur",listeLivres.get(position).getAuteur());
                args.putString("editeur",listeLivres.get(position).getEditeur());
                args.putString("edition",listeLivres.get(position).getEdition());
                args.putString("codebar",listeLivres.get(position).getCodeBarre());
                args.putString("imagePath",listeLivres.get(position).getImgpath());
                args.putDouble("prix", listeLivres.get(position).getPrix());
                args.putInt("idC", listeLivres.get(position).getIdC());
                args.putInt("annotation", listeLivres.get(position).getAnnotation());
                fragment.setArguments(args);
                toolbar =((AppCompatActivity) context).findViewById(R.id.toolbar);
                toolbar.setTitle("Détails de la concession");



                FragmentTransaction ft = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.flFragment, fragment);
                ft.addToBackStack(null);
                ft.commit();

            }
        });

        holder.linearLayoutid.setOnLongClickListener(new View.OnLongClickListener() {
            /**
             * Quand on appuit longuement sur un item du recycleview, un alertDialog apparaît et on peut renouveler,donner ou supprimer une concession
             * @param view
             * @return
             */
            @Override
            public boolean onLongClick(final View view) {
                //second alert dialog (Don)
                AlertDialog.Builder alertDialogBuilderDon = new AlertDialog.Builder(
                        context);
                alertDialogBuilderDon.setTitle("Renouveler");
                // set dialog message
                alertDialogBuilderDon
                        .setMessage(
                                "Voulez-vous vraiment faire don de votre livre?")
                        .setCancelable(false)
                        .setIcon(R.drawable.logo)
                        .setPositiveButton("Donner",
                                new DialogInterface.OnClickListener() {
                                    /**
                                     * La concession est donnée à l'AGECTR et n'appartient plus à l'utilisateur
                                     * @param dialog
                                     * @param id
                                     */
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        Call<ResponseBody> call = serveur.donConcession(listeLivres.get(position).getIdC());

                                        call.enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                String reponse = "";
                                                try {
                                                    reponse = response.body().string();

                                                } catch (IOException e) {
                                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();

                                                }
                                                if (reponse.equals("success")){

                                                    Toast.makeText(context.getApplicationContext(), "Votre livre a bien été transféré à l'AGECTR" ,Toast.LENGTH_SHORT).show();
                                                    listeLivres.remove(position);
                                                    notifyItemRemoved(position);
                                                    if (getItemCount() == 0){
                                                        Intent intent = new Intent();
                                                        intent.setAction("com.info.lesreceivers.MON_ACTION4");
                                                        context.sendBroadcast(intent);
                                                    }

                                                }
                                                else if (reponse.equals("notfound")){
                                                    Toast.makeText(context.getApplicationContext(), "Cette concession est introuvable" ,Toast.LENGTH_SHORT).show();
                                                }
                                                else{
                                                    Toast.makeText(context.getApplicationContext(), "Erreur lors du renouvelement de la concession" ,Toast.LENGTH_LONG).show();
                                                }

                                            }

                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                Toast.makeText(context.getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                                            }

                                        });

                                    }
                                }).setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    /**
                     * L'alerte dialog se ferme
                     * @param dialog
                     * @param which
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                final AlertDialog alertDialogDon = alertDialogBuilderDon.create();

                //second alert dialog (renouveler)
                AlertDialog.Builder alertDialogBuilderrenouveler = new AlertDialog.Builder(
                        context);
                alertDialogBuilderrenouveler.setTitle("Renouveler");
                // set dialog message
                alertDialogBuilderrenouveler
                        .setMessage("Voulez-vous vraiment renouveler votre concession?")
                        .setIcon(R.drawable.logo)
                        .setPositiveButton("Renouveler",
                                new DialogInterface.OnClickListener() {
                                    /**
                                     * La concession est renouvelée pour une autre année
                                     * @param dialog
                                     * @param id
                                     */
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        Call<ResponseBody> call = serveur.renouvelerConcession(listeLivres.get(position).getIdC());

                                        call.enqueue(new Callback<ResponseBody>() {
                                            /**
                                             * réponse du serveur quand le renouvellement a été fait
                                             * @param call
                                             * @param response
                                             */
                                            @Override
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                String reponse = "";
                                                try {
                                                    reponse = response.body().string();

                                                } catch (IOException e) {
                                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();

                                                }
                                                if (reponse.equals("success")){

                                                    Toast.makeText(context.getApplicationContext(), "Cette concession à bien été renouvelée" ,Toast.LENGTH_SHORT).show();
                                                    notifyDataSetChanged();
                                                }
                                                else if (reponse.equals("notfound")){
                                                    Toast.makeText(context.getApplicationContext(), "Cette concession est introuvable" ,Toast.LENGTH_SHORT).show();
                                                }
                                                else{
                                                    Toast.makeText(context.getApplicationContext(), "Erreur lors du renouvelement de la concession" ,Toast.LENGTH_LONG).show();
                                                }

                                            }

                                            /**
                                             * affichage de l'erreur si erreur au niveau du serveur
                                             * @param call
                                             * @param t
                                             */
                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                Toast.makeText(context.getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                                            }

                                        });

                                    }
                                }).setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    /**
                     * l'alertDialog se ferme
                     * @param dialog
                     * @param which
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // create alert dialog
                final AlertDialog alertDialogRenouveler = alertDialogBuilderrenouveler.create();

                //second alert dialog (suppression)
                AlertDialog.Builder alertDialogBuilderSupprimer= new AlertDialog.Builder(
                        context);
                alertDialogBuilderSupprimer.setTitle("Suppresion");
                // set dialog message
                alertDialogBuilderSupprimer
                        .setMessage(
                                "Voulez-vous vraiment supprimer votre concession?")
                        .setIcon(R.drawable.logo)
                        .setPositiveButton("Supprimer",
                                new DialogInterface.OnClickListener() {
                                    /**
                                     * La concession est supprimée de la base de données et n'est plus visible pour personne
                                     * @param dialog
                                     * @param id
                                     */
                                    public void onClick(
                                            DialogInterface dialog, int id) {

                                        Call<ResponseBody> call = serveur.supConcession(listeLivres.get(position2).getIdC());

                                        call.enqueue(new Callback<ResponseBody>() {
                                            /**
                                             * Réponse du serveur une fois la concession supprimée
                                             * @param call
                                             * @param response
                                             */
                                            @Override
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                String reponse = "";
                                                try {
                                                    reponse = response.body().string();

                                                } catch (IOException e) {
                                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();

                                                }
                                                if (reponse.equals("success")){
                                                    Toast.makeText(context.getApplicationContext(), "Cette concession à bien été supprimée" ,Toast.LENGTH_SHORT).show();
                                                    listeLivres.remove(position);
                                                    notifyItemRemoved(position);
                                                    if (getItemCount() == 0){
                                                        Intent intent = new Intent();
                                                        intent.setAction("com.info.lesreceivers.MON_ACTION4");
                                                        context.sendBroadcast(intent);
                                                    }


                                                }
                                                else if (reponse.equals("notfound")){
                                                    Toast.makeText(context.getApplicationContext(), "Cette concession à déjà été supprimée" ,Toast.LENGTH_SHORT).show();
                                                }
                                                else{
                                                    Toast.makeText(context.getApplicationContext(), "Erreur lors de la suppression de la concession" ,Toast.LENGTH_LONG).show();
                                                }

                                            }

                                            /**
                                             * affichage du message d'erreur si il y a un problème au niveau du serveur
                                             * @param call
                                             * @param t
                                             */
                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                Toast.makeText(context.getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                                            }

                                        });


                                    }
                                }).setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    /**
                     * On ferme l'alertDialog
                     * @param dialog
                     * @param which
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // create alert dialog
                final AlertDialog alertDialogSupprimer = alertDialogBuilderSupprimer.create();



            //first alert dialog
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);
                alertDialogBuilder.setTitle("Options de la concession");
                // set dialog message
                alertDialogBuilder
                        .setMessage("Que désirez-vous faire?")
                        .setIcon(R.drawable.logo)
                        .setPositiveButton("Supprimer",
                                new DialogInterface.OnClickListener() {
                                    /**
                                     * ouvre le dialog de suppression
                                     * @param dialog
                                     * @param id
                                     */
                                    public void onClick(
                                            DialogInterface dialog, int id) {

                                        alertDialogSupprimer.show();
                                    }
                                })
                        .setNegativeButton("Renouveler",
                                new DialogInterface.OnClickListener() {
                                    /**
                                     * ouvre le dialog de renouvellement
                                     * @param dialog
                                     * @param id
                                     */
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        alertDialogRenouveler.show();

                                    }
                                })
                        .setNeutralButton("Donner à l'AGECTR",
                        new DialogInterface.OnClickListener() {
                            /**
                             * ouvre le dialog de donation
                             * @param dialog
                             * @param id
                             */
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                alertDialogDon.show();

                            }
                });

                // create alert dialog
               final AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();






                return true;
            }

        });

            }


    /**
     *
     * @return la quantité de livre  appartenant à l'utilisateur connecté
     */
    @Override
    public int getItemCount() {
        return listeLivres.size();
    }

    public class MonViewHolder extends RecyclerView.ViewHolder {

        TextView tv_Titre, tv_Edition, tv_Editeur, tv_Auteur;
        ImageView img;
        LinearLayout linearLayoutid;
        AlertDialog.Builder builder;
        AlertDialog.Builder builderInfo;


        public MonViewHolder(View view) {
            super(view);

            tv_Titre = view.findViewById(R.id.tv_Titre);
            tv_Edition = view.findViewById(R.id.tv_Edition);
            tv_Editeur = view.findViewById(R.id.tv_Editeur);
            tv_Auteur = view.findViewById(R.id.tv_Auteur);
            linearLayoutid = (LinearLayout) itemView.findViewById(R.id.linearlayoutid);
            img = view.findViewById(R.id.recyclerItem_imageView);
            builder = new AlertDialog.Builder(view.getContext());
            builderInfo = new AlertDialog.Builder(view.getContext());


        }
    }

}


