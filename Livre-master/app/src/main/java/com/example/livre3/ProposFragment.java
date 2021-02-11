package com.example.livre3;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import java.util.ArrayList;

/**
 * @author  Sébastien Poulain
 * Ce fragment permet d'afficher les informations de l'AGECTR et permet aux usagers de suivre l'AGECTR à travers ces différentes réseaux sociaux (Facebook,Instagramme et Snapchat)
 */

public class ProposFragment extends Fragment {
ImageView facebook,snap,insta;
ListView list;
ArrayList list1;
    public ProposFragment(){

    }

    /**
     * fonction qui permet d'initialiser les différents composants
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return view
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_propos, container, false);
        list1 = new ArrayList<String>();

        String[] array = getResources().getStringArray(R.array.liste_services);

        for (int i = 0; i < array.length - 1; i++) {
            list1.add(array[i]);
        }

        list = view.findViewById(R.id.listeServices);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), R.layout.row,list1);
        list.setAdapter(adapter);

        facebook = view.findViewById(R.id.ImageFacebook);
        insta = view.findViewById(R.id.ImageInsta);
        snap = view.findViewById(R.id.ImageSnap);

        facebook.setOnClickListener(new View.OnClickListener() {
            /**
             * Quand on clique sur le bouton de facebook, l'application va s'ouvrir ou internet
             * @param v
             */
            public void onClick(View v) {

                    Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                    String facebookUrl = getFacebookPageURL(getActivity());
                    facebookIntent.setData(Uri.parse(facebookUrl));
                    startActivity(facebookIntent);
                }
        });
        insta.setOnClickListener(new View.OnClickListener() {
            /**
             * Quand on clique sur le bouton de instagram, l'application va s'ouvrir ou internet
             * @param v
             */
            public void onClick(View v) {

                Intent instagramIntent = getOpenInstagramIntent(getActivity().getApplicationContext());
                startActivity(instagramIntent);
            }
        });
        snap.setOnClickListener(new View.OnClickListener() {
            /**
             * Quand on clique sur le bouton de snapchat, l'application va s'ouvrir ou internet
             * @param v
             */
            public void onClick(View v) {
                getOpenSnapIntent();
            }
        });
        return view;
    }

    /**
     * Cette fonction permet de de détecter si la personne a Instagram sur son téléphone, sinon elle sera redirigée vers le site d'instagram
     * @param context
     * @return le lien du site instagram de l'AGECTR
     */

    public static Intent getOpenInstagramIntent(Context context) {

        try {
            context.getPackageManager()
                    .getPackageInfo("com.instagram.android", 0);
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.instagram.com/agecegeptr/"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.instagram.com/agecegeptr/"));
        }
    }

    /**
     * Cette fonction permet de détecter si la personne a l'application Snapchat sur son téléphone, sinon elle sera dirigée vers le site de snapchat
     */
    public  void getOpenSnapIntent() {

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://snapchat.com/add/agectr"));
            intent.setPackage("com.snapchat.android");
            startActivity(intent);
        } catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://snapchat.com/add/agectr")));
        }
    }

    public static String FACEBOOK_URL = "https://www.facebook.com/agectr";
    public static String FACEBOOK_PAGE_ID = "agectr";

    /**
     *
     * @param context
     * cette fonction permet de vérifier si la personne possède l'application Facebook,sinon elle redirigera l'utilisateur vers le lien internet de Facebook
     * @return le lien du facebook de l'AGECTR
     */
    public String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) {
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else {
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL;
        }
    }
}

