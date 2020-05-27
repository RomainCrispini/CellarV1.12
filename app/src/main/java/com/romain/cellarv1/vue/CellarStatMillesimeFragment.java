package com.romain.cellarv1.vue;


import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.StackedValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.romain.cellarv1.R;
import com.romain.cellarv1.modele.AccesLocal;
import com.romain.cellarv1.modele.WineBottle;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CellarStatMillesimeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CellarStatMillesimeFragment extends Fragment {

    private AccesLocal accesLocal;

    private BarChart barChartMillesime;

    private TextView txtTotalEstimate, txtTextEstimate, txtEmptyPieChart;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CellarStatMillesimeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CellarStatMillesimeFragment newInstance(String param1, String param2) {
        CellarStatMillesimeFragment fragment = new CellarStatMillesimeFragment();
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

        View cellarStatMillesimeFragment = inflater.inflate(R.layout.fragment_cellar_stat_millesime, container, false);

        barChartMillesime = (BarChart) cellarStatMillesimeFragment.findViewById(R.id.barChartMillesime);

        txtTotalEstimate = (TextView) cellarStatMillesimeFragment.findViewById(R.id.txtTotalEstimate);

        txtTextEstimate = (TextView) cellarStatMillesimeFragment.findViewById(R.id.txtTextEstimate);
        txtEmptyPieChart = (TextView) cellarStatMillesimeFragment.findViewById(R.id.txtEmptyPieChart);
        txtEmptyPieChart.setVisibility(View.INVISIBLE);

        loadTotalEstimate();
        loadMillesimeBarChart();

        return cellarStatMillesimeFragment;
    }

    // TODO IL FAUT MULTIPLIER LE NOMBRE DE CHAQUE AVEC L'ESTIMATE
    private void loadTotalEstimate() {
        accesLocal = new AccesLocal(getContext());
        Integer nbTotalEstimate = accesLocal.nbTotalEstimate();

        txtTotalEstimate.setText(nbTotalEstimate.toString() + " €");

        // Affiche un message s'il n'y a pas d'estimation de bouteilles dans la BDD & efface quelques TextView
        if(nbTotalEstimate < 1) {
            txtTotalEstimate.setVisibility(View.INVISIBLE);
            txtTextEstimate.setVisibility(View.INVISIBLE);
            txtEmptyPieChart.setVisibility(View.VISIBLE);
        }
    }

    private void loadMillesimeBarChart() {

        // Caractéristiques du message s'il n'y a pas de données
        barChartMillesime.setNoDataText("");

        //pieChart.invalidate();
        //Paint p = barChartMillesime.getPaint(PieChart.PAINT_INFO);
        //p.setTextSize(15f);
        //p.setColor(Color.parseColor("#8DB3C5"));

        barChartMillesime.getDescription().setEnabled(false);
        barChartMillesime.getLegend().setEnabled(true);
        //barChartMillesime.setDrawBarShadow(true);
        //barChartMillesime.setDrawValueAboveBar(true);
        //barChartMillesime.setMaxVisibleValueCount(50);
        //barChartMillesime.setPinchZoom(false);
        //barChartMillesime.setDrawGridBackground(false);
        barChartMillesime.getXAxis().setDrawGridLines(false);
        barChartMillesime.getAxisLeft().setDrawGridLines(false);
        barChartMillesime.getAxisRight().setDrawGridLines(false);
        barChartMillesime.getXAxis().setDrawAxisLine(false);
        barChartMillesime.getAxisLeft().setDrawAxisLine(false);
        barChartMillesime.getAxisRight().setDrawAxisLine(false);
        barChartMillesime.setDrawValueAboveBar(true);
        barChartMillesime.setPinchZoom(false);
        barChartMillesime.getAxisRight().setDrawLabels(false);
        barChartMillesime.getAxisLeft().setDrawLabels(false);
        barChartMillesime.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        barChartMillesime.getAxisLeft().setTextColor(getResources().getColor(R.color.green_very_light));
        barChartMillesime.getAxisLeft().setAxisLineColor(getResources().getColor(R.color.green_very_light));
        barChartMillesime.getXAxis().setTextColor(getResources().getColor(R.color.green_very_light));

        barChartMillesime.setExtraOffsets(5, 10, 5, 5);

        // Arrangement de la légende
        Legend legend = barChartMillesime.getLegend();
        //legend.setTextColor(Color.parseColor("#8DB3C5"));
        legend.setTextColor(getResources().getColor(R.color.green_very_light));
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);


        /*
        for(int i = 0; i < wineBottleList.size(); i++) {
            if(wineBottleList.get(i).getEstimate() > 0) {

                values.add(new BarEntry(wineBottleList.get(i).getYear(), wineBottleList.get(i).getEstimate()));
                //Toast.makeText(getContext(), wineBottleList.get(i).getYear(), Toast.LENGTH_SHORT).show();
            } else {
            }

        }

         */






        BarDataSet barDataSetEstimate = new BarDataSet(estimateValues(), "Estimation");
        BarDataSet barDataSetNumber = new BarDataSet(numberValues(), "Quantité");
        // Set des deux couleurs
        barDataSetEstimate.setColors(Color.rgb(141, 179, 197));
        barDataSetNumber.setColors(Color.rgb(103, 130, 143));


        BarData data = new BarData(barDataSetEstimate, barDataSetNumber);

        // Largeur des bars (90% de la largeur totale)
        data.setBarWidth(0.9f);


        // couleur et taille des valeurs dans les bars
        barDataSetEstimate.setValueTextColor(getResources().getColor(R.color.green_dark));
        barDataSetEstimate.setValueTextSize(10);
        barDataSetNumber.setValueTextColor(getResources().getColor(R.color.green_dark));
        barDataSetNumber.setValueTextSize(10);

        barDataSetEstimate.setValueFormatter(new MyPieChartValueFormatter());
        barDataSetNumber.setValueFormatter(new MyPieChartValueFormatter());

        // 6 colonnes maximum affichées
        barChartMillesime.setVisibleXRangeMaximum(6);

        barChartMillesime.animateY(1000);

        barChartMillesime.setData(data);
        barChartMillesime.invalidate();



        //barChartMillesime.notifyDataSetChanged();

    }

    private ArrayList<BarEntry> estimateValues(){
        // Récupération de la liste des bouteilles
        accesLocal = new AccesLocal(getContext());
        ArrayList<WineBottle> wineBottleList = (ArrayList<WineBottle>) accesLocal.recoverWineBottleList();

        Iterator iterator = wineBottleList.iterator();

        // Injection des données dans le barChart
        ArrayList<BarEntry> valuesEstimate = new ArrayList<>();
        while (iterator.hasNext()) {
            WineBottle wineBottle = (WineBottle) iterator.next();
            if(wineBottle.getEstimate() > 0) {
                valuesEstimate.add(new BarEntry(wineBottle.getYear(), wineBottle.getEstimate()));
            } else {
            }
        }
        return valuesEstimate;
    }

    private ArrayList<BarEntry> numberValues(){
        // Récupération de la liste des bouteilles
        accesLocal = new AccesLocal(getContext());
        ArrayList<WineBottle> wineBottleList = (ArrayList<WineBottle>) accesLocal.recoverWineBottleList();

        Iterator iterator = wineBottleList.iterator();

        // Injection des données dans le barChart
        ArrayList<BarEntry> valuesNumber = new ArrayList<>();
        while (iterator.hasNext()) {
            WineBottle wineBottle = (WineBottle) iterator.next();
            if(wineBottle.getEstimate() > 0) {
                valuesNumber.add(new BarEntry(wineBottle.getYear(), wineBottle.getNumber()));
            } else {
            }
        }
        return valuesNumber;
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
