package com.romain.cellarv1.outils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.romain.cellarv1.R;
import com.romain.cellarv1.modele.WineBottle;
import java.util.ArrayList;


public class MyAdapterCellarListView extends BaseAdapter {

    private Context context;
    private ArrayList<WineBottle> arrayList;


    public MyAdapterCellarListView(Context context, ArrayList<WineBottle> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return this.arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.activity_cellar_custom_list_view, null);

        ImageView imageWineColor = (ImageView) convertView.findViewById(R.id.imageWineColorListView);
        ImageView image = (ImageView) convertView.findViewById(R.id.imageBottleListView);
        TextView region = (TextView) convertView.findViewById(R.id.regionListView);
        TextView appellation = (TextView) convertView.findViewById(R.id.appellationListView);
        TextView domain = (TextView) convertView.findViewById(R.id.domainListView);
        TextView year = (TextView) convertView.findViewById(R.id.yearListView);

        WineBottle wineBottle = arrayList.get(position);

        Tools tools = new Tools();
        image.setImageBitmap(tools.stringToBitmap(wineBottle.getImage()));

        region.setText(wineBottle.getRegion());
        appellation.setText((wineBottle.getAppellation()));
        domain.setText((wineBottle.getDomain()));
        year.setText((wineBottle.getYear().toString()));

        switch(wineBottle.getWineColor().trim()) {
            case "Rouge" :
                imageWineColor.setImageResource(R.drawable.red_wine_listview);
                break;
            case "Rose" :
                imageWineColor.setImageResource(R.drawable.rose_wine_listview);
                break;
            case "Blanc" :
                imageWineColor.setImageResource(R.drawable.white_wine_listview);
                break;
            case "Effervescent" :
                imageWineColor.setImageResource(R.drawable.champ_wine_listview);
                break;
        }


        return convertView;
    }


}
