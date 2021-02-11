package com.example.livre3;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
/**
 *  Scannerview
 *
 *  fragment qui permet d'ouvrir le scan(pour les code à barre)
 *
 *@author  Émilie cormier houle
 *
 */
public class Scannerview extends Fragment {
    private CodeScanner mCodeScanner;
    Fragment fragAjout;
    DataSingleton data;
    @Nullable
    @Override

    /**
     * fonction qui initialise les différents composants
     */
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final Activity activity = getActivity();
        View root = inflater.inflate(R.layout.scanner, container, false);
        CodeScannerView scannerView = root.findViewById(R.id.scanner_view);
        data =DataSingleton.getInstance();
        mCodeScanner = new CodeScanner(activity, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    /**
                     * Après avoir pris un scan d'un code-barres, on va dans le fragment ajoutlivre et on complète le code-barres avec le résultat du scan
                     */
                    public void run() {
                        fragAjout = new Ajoutlivre();

                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.flFragment,fragAjout);
                        fragmentTransaction.commit();
                         data.setCode(result.getText());
                    }
                });
            }
        });

        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * quand on clique sur le bouton scan, le layout du scanner apparaît
             */
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
        return root;
    }

    /**
     * Apparition de l'interface pour scanner un code-barres
     */
    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    /**
     * Libère les ressources quand on quitte le scanner
     */
    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}
