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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.romain.cellarv1.R;
import com.romain.cellarv1.modele.AccesLocal;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CellarStatMillesimeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CellarStatMillesimeFragment extends Fragment {

    private AccesLocal accesLocal;

    private BarChart barChartMillesime;

    private TextView txtTotalEstimate;







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



        loadTotalEstimate();
        loadMillesimeBarChart();

        return cellarStatMillesimeFragment;
    }

    // TODO IL FAUT MULTIPLIER LE NOMBRE DE CHAQUE AVEC L'ESTIMATE
    private void loadTotalEstimate() {
        accesLocal = new AccesLocal(getContext());
        Integer nbTotalEstimate = accesLocal.nbTotalEstimate();

        txtTotalEstimate.setText(nbTotalEstimate.toString() + " €");
    }

    private void loadMillesimeBarChart() {

        // Caractéristiques du message s'il n'y a pas de données
        barChartMillesime.setNoDataText("Il manque quelques bouteilles pour éditer des statistiques !");

        //pieChart.invalidate();
        //Paint p = barChartMillesime.getPaint(PieChart.PAINT_INFO);
        //p.setTextSize(15f);
        //p.setColor(Color.parseColor("#8DB3C5"));

        barChartMillesime.getDescription().setEnabled(false);
        barChartMillesime.getLegend().setEnabled(false);
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
        barChartMillesime.setDrawValueAboveBar(false);
        barChartMillesime.getAxisRight().setDrawLabels(false);
        barChartMillesime.getAxisLeft().setDrawLabels(false);
        barChartMillesime.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        barChartMillesime.getAxisLeft().setTextColor(getResources().getColor(R.color.green_very_light));
        barChartMillesime.getAxisLeft().setAxisLineColor(getResources().getColor(R.color.green_very_light));
        barChartMillesime.getXAxis().setTextColor(getResources().getColor(R.color.green_very_light));

        accesLocal = new AccesLocal(getContext());
        //Integer nbRed = accesLocal.nbRed();
        //Integer nbRose = accesLocal.nbRose();
        //Integer nbWhite = accesLocal.nbWhite();
        //Integer nbChamp = accesLocal.nbChamp();



        ArrayList<Integer> colors = new ArrayList<>();

        ArrayList<BarEntry> values = new ArrayList<>();
        values.add(new BarEntry(1, 123));
        values.add(new BarEntry(2, 543));
        values.add(new BarEntry(3, 54));
        values.add(new BarEntry(4, 54));
        values.add(new BarEntry(5, 54));
        values.add(new BarEntry(6, 54));
        values.add(new BarEntry(7, 54));
        values.add(new BarEntry(8, 54));


        /*
        if(nbRed > 0) {
            values.add(new PieEntry(nbRed, "Rouge"));
            //COLORS.add(Color.rgb(159, 6, 52)); // Rouge
        } else {
            values.add(new PieEntry(0, "Rouge"));
            //COLORS.add(Color.rgb(159, 6, 52)); // Rouge
        }

        if(nbRose > 0) {
            values.add(new PieEntry(nbRose, "Rosé"));
            //COLORS.add(Color.rgb(249, 175, 164)); // Rosé
        } else {
            values.add(new PieEntry(0, "Rosé"));
            //COLORS.add(Color.rgb(249, 175, 164)); // Rosé
        }

        if(nbWhite > 0) {
            values.add(new PieEntry(nbWhite, "Blanc"));
            //COLORS.add(Color.rgb(254, 207, 29)); // Blanc
        } else {
            values.add(new PieEntry(0, "Blanc"));
            //COLORS.add(Color.rgb(254, 207, 29)); // Blanc
        }

        if(nbChamp > 0) {
            values.add(new PieEntry(nbChamp, "Effervescent"));
            //COLORS.add(Color.rgb(222, 203, 135)); // Effervescent
        } else {
            values.add(new PieEntry(0, "Effervescent"));
            //COLORS.add(Color.rgb(222, 203, 135)); // Effervescent
        }

         */

        BarDataSet barDataSet = new BarDataSet(values, "Cells");



        ArrayList<String> labels = new ArrayList<>();
        labels.add("Dec");
        labels.add("Jv");
        labels.add("Fev");

        BarData data = new BarData(barDataSet);

        data.setBarWidth(1f);

        barChartMillesime.setData(data);
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barChartMillesime.invalidate();

        //barChartMillesime.setDescription("description ??????????");

        //barChartMillesime.setDrawEntryLabels(false);
        //barChartMillesime.setEntryLabelTextSize(4f);
        //barChartMillesime.setEntryLabelColor(Color.parseColor("#2F3B40"));



        //Legend legend = barChartMillesime.getLegend();
        //legend.setTextColor(Color.parseColor("#8DB3C5"));
        //legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);

        //dataSet.setColors(COLORS);


        barChartMillesime.animateY(1000);

        //data.setValueTextSize(15f);
        //data.setValueFormatter(new CellarStatMillesimeFragment.MyPieChartValueFormatter());
        //data.setValueTextColor(Color.parseColor("#2F3B40"));

        //barChartMillesime.setData(data);
        //barChartMillesime.notifyDataSetChanged();

    }


    public class MyPieChartValueFormatter extends ValueFormatter {

        private DecimalFormat mFormat;

        public MyPieChartValueFormatter() {
            mFormat = new DecimalFormat("#");
        }

        @Override
        public String getFormattedValue(float value) {
            return mFormat.format(value);
        }
    }

}
