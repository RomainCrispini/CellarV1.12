package com.romain.cellarv1.vue;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.romain.cellarv1.R;
import com.romain.cellarv1.modele.AccesLocalCellar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CellarStatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CellarStatsFragment extends Fragment {

    private AccesLocalCellar accesLocalCellar;

    // Interpolator pour animation des menuBis
    private OvershootInterpolator interpolator = new OvershootInterpolator();

    // Tableaux
    private PieChart pieChart;

    // TextViews
    private TextView txtTotalNumber;




    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CellarStatsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CellarStatsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CellarStatsFragment newInstance(String param1, String param2) {
        CellarStatsFragment fragment = new CellarStatsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View cellarStatsFragment = inflater.inflate(R.layout.fragment_cellar_stats, container, false);

        loadCellarWineColorFragment();

        return cellarStatsFragment;

    }


    // TODO IL MANQUE L'ANIMATION AU CHARGEMENT ?????????????????????
    public void loadCellarWineColorFragment() {

        CellarStatWineColorFragment cellarStatWineColorFragment = new CellarStatWineColorFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.layoutStatFragment,cellarStatWineColorFragment);

        fragmentTransaction.addToBackStack(cellarStatWineColorFragment.toString());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }




}
