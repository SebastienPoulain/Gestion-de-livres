package com.example.livre3;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @author Sébastien Poulain
 * cette classe permet de remplir le recyclerview lors de la recherche de livres dans le fragment homefragment
 */

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyViewHolder> {

    private Context mContext ;
    private List<Livre> mData ;
    Toolbar toolbar;
String lien;

    public BookAdapter(Context mContext, List<Livre> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.item_book,parent,false);
        return new MyViewHolder(view);
    }

    /**
     * permet d'associer les données de la requete de la recherche de livre aux différents items du recyclerview
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.tv_book_title.setText(mData.get(position).getTitre());
        holder.tv_book_Auteur.setText(mData.get(position).getAuteur());
        holder.tv_book_Editeur.setText(mData.get(position).getEditeur());
        holder.tv_book_Edition.setText(mData.get(position).getEdition());
        lien = RetrofitInstance.getInstance().baseUrl() + "img/" + mData.get(position).getImgpath();

        Picasso.get().load(lien)
                .resize(220, 220)
                .centerCrop()
                .into(holder.img_book_thumbnail);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            /**
             * Quand on clique sur un livre, on nous amène sur la page book_activity qui nous affiche les détails du livre et les concessions disponibles associées
             * @param v
             */
            @Override
            public void onClick(View v) {

                Book_Activity fragment = new Book_Activity();
                Bundle args = new Bundle();
                args.putInt("id",mData.get(position).getId());
                args.putString("Title",mData.get(position).getTitre());
                args.putString("auteur",mData.get(position).getAuteur());
                args.putString("editeur",mData.get(position).getEditeur());
                args.putString("edition",mData.get(position).getEdition());
                args.putString("codebar",mData.get(position).getCodeBarre());
                args.putString("imagePath",mData.get(position).getImgpath());
                fragment.setArguments(args);
                toolbar =((AppCompatActivity) mContext).findViewById(R.id.toolbar);
                toolbar.setTitle("Détails du livre");

                FragmentTransaction ft = ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.flFragment, fragment);
                ft.addToBackStack(null);
                ft.commit();

            }
        });



    }

    /**
     *
     * @return la liste des livres qui correspondre aux résultats de la recherche
     */
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_book_title,tv_book_Editeur,tv_book_Edition,tv_book_Auteur;
        ImageView img_book_thumbnail;
        CardView cardView ;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_book_Auteur = (TextView) itemView.findViewById(R.id.book_auteur_id) ;
            tv_book_Editeur = (TextView) itemView.findViewById(R.id.book_editeur_id) ;
            tv_book_Edition = (TextView) itemView.findViewById(R.id.book_edition_id) ;
            tv_book_title = (TextView) itemView.findViewById(R.id.book_title_id) ;
            img_book_thumbnail = (ImageView) itemView.findViewById(R.id.book_img_id);
            cardView = (CardView) itemView.findViewById(R.id.cardview_id);

        }
    }


}