package com.romain.cellarv1.vue;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.romain.cellarv1.R;
import com.romain.cellarv1.modele.AccesLocal;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CellarStatWineColorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CellarStatWineColorFragment extends Fragment {

    private AccesLocal accesLocal;

    // Interpolator pour animation des menuBis
    private OvershootInterpolator interpolator = new OvershootInterpolator();

    // Tableaux
    private PieChart pieChartWineColor;

    // TextViews
    private TextView txtTotalNumber, txtTextNumber, txtEmptyPieChart;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CellarStatWineColorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CellarStatWineColorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CellarStatWineColorFragment newInstance(String param1, String param2) {
        CellarStatWineColorFragment fragment = new CellarStatWineColorFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View cellarStatWineColorFragment = inflater.inflate(R.layout.fragment_cellar_stat_wine_color, container, false);

        pieChartWineColor = (PieChart) cellarStatWineColorFragment.findViewById(R.id.pieChartWineColor);
        txtTotalNumber = (TextView) cellarStatWineColorFragment.findViewById(R.id.txtTotalNumber);

        txtTextNumber = (TextView) cellarStatWineColorFragment.findViewById(R.id.txtTextNumber);
        txtEmptyPieChart = (TextView) cellarStatWineColorFragment.findViewById(R.id.txtEmptyPieChart);
        txtEmptyPieChart.setVisibility(View.INVISIBLE);

        loadTotalNumber();
        loadWineColorPieChart();

        return cellarStatWineColorFragment;
    }

    private void loadTotalNumber() {
        accesLocal = new AccesLocal(getContext());
        Integer nbTotalBottle = accesLocal.nbTotal();

        txtTotalNumber.setText(nbTotalBottle.toString());

        // Affiche un message s'il n'y a pas de bouteille dans la BDD & efface quelques TextView
        if(nbTotalBottle < 1) {
            txtTotalNumber.setVisibility(View.INVISIBLE);
            txtTextNumber.setVisibility(View.INVISIBLE);
            txtEmptyPieChart.setVisibility(View.VISIBLE);
            pieChartWineColor.getLegend().setEnabled(false);
        }
    }

    private void loadWineColorPieChart() {

        pieChartWineColor.setUsePercentValues(false);

        //pieChart.invalidate();
        Paint p = pieChartWineColor.getPaint(PieChart.PAINT_INFO);
        p.setTextSize(15f);
        p.setColor(Color.parseColor("#8DB3C5"));
        p.setColor(getResources().getColor(R.color.green_very_light));

        pieChartWineColor.getDescription().setEnabled(false);
        pieChartWineColor.setExtraOffsets(5, 10, 5, 5);

        pieChartWineColor.setDragDecelerationFrictionCoef(0.95f);

        pieChartWineColor.setDrawHoleEnabled(true);
        pieChartWineColor.setHoleColor(Color.TRANSPARENT);
        pieChartWineColor.setHoleRadius(70f);
        pieChartWineColor.setTransparentCircleRadius(0f);
        //pieChart.getLegend().setEnabled(false);

        accesLocal = new AccesLocal(getContext());
        //ArrayList<WineBottle> wineBottleArrayList = (ArrayList<WineBottle>) accesLocal.recoverWineBottleList();

        Integer nbRed = accesLocal.nbRed();
        Integer nbRose = accesLocal.nbRose();
        Integer nbWhite = accesLocal.nbWhite();
        Integer nbChamp = accesLocal.nbChamp();

        // Modifie le nombre de couleurs du pie suivant celle des bouteilles
        ArrayList<Integer> COLORS = new ArrayList<>();
        ArrayList<PieEntry> values = new ArrayList<>();

        if(nbRed > 0) {
            values.add(new PieEntry(nbRed, "Rouge"));
            COLORS.add(Color.rgb(159, 6, 52)); // Rouge
        } else {
            values.add(new PieEntry(0, "Rouge"));
            COLORS.add(Color.rgb(159, 6, 52)); // Rouge
        }

        if(nbRose > 0) {
            values.add(new PieEntry(nbRose, "Rosé"));
            COLORS.add(Color.rgb(249, 175, 164)); // Rosé
        } else {
            values.add(new PieEntry(0, "Rosé"));
            COLORS.add(Color.rgb(249, 175, 164)); // Rosé
        }

        if(nbWhite > 0) {
            values.add(new PieEntry(nbWhite, "Blanc"));
            COLORS.add(Color.rgb(254, 207, 29)); // Blanc
        } else {
            values.add(new PieEntry(0, "Blanc"));
            COLORS.add(Color.rgb(254, 207, 29)); // Blanc
        }

        if(nbChamp > 0) {
            values.add(new PieEntry(nbChamp, "Effervescent"));
            COLORS.add(Color.rgb(222, 203, 135)); // Effervescent
        } else {
            values.add(new PieEntry(0, "Effervescent"));
            COLORS.add(Color.rgb(222, 203, 135)); // Effervescent
        }


        pieChartWineColor.setDrawEntryLabels(false);
        //pieChart.setEntryLabelTextSize(4f);
        //pieChart.setEntryLabelColor(Color.parseColor("#2F3B40"));

        // Arrangement de la légende
        Legend legend = pieChartWineColor.getLegend();
        //legend.setTextColor(Color.parseColor("#8DB3C5"));
        legend.setTextColor(getResources().getColor(R.color.green_very_light));
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);

        PieDataSet dataSet = new PieDataSet(values, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(COLORS);
        //dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        pieChartWineColor.animateXY(0, 1000);

        PieData data = new PieData(dataSet);

        data.setValueTextSize(15f);
        data.setValueFormatter(new CellarStatWineColorFragment.MyPieChartValueFormatter());
        //data.setValueTextColor(Color.parseColor("#2F3B40"));
        data.setValueTextColor(getResources().getColor(R.color.green_very_dark));

        pieChartWineColor.setData(data);
        //pieChartWineColor.notifyDataSetChanged();

    }

    public class MyPieChartValueFormatter extends ValueFormatter {

        private DecimalFormat mFormat;

        public MyPieChartValueFormatter() {
            mFormat = new DecimalFormat("#");
        }

        @Override
        public String getFormattedValue(float value) {

            // Le if permet de ne rien écrire si la valeur est < 1
            if(value > 0) {
                return mFormat.format(value);
            } else {
                return "";
            }
        }
    }
}
