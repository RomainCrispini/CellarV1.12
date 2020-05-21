package com.romain.cellarv1.vue;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import com.romain.cellarv1.R;
import com.romain.cellarv1.modele.AccesLocal;
import com.romain.cellarv1.modele.WineBottle;
import com.romain.cellarv1.outils.MyAdapterCellarRecyclerView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LikeRateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LikeRateFragment extends Fragment {

    private AccesLocal accesLocal;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private OvershootInterpolator interpolator = new OvershootInterpolator();


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LikeRateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LikeRateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LikeRateFragment newInstance(String param1, String param2) {
        LikeRateFragment fragment = new LikeRateFragment();
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

        View likeRateFragment = inflater.inflate(R.layout.fragment_like_rate, container, false);
        mRecyclerView = (RecyclerView) likeRateFragment.findViewById(R.id.rateRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));

        loadRateWineBottleInRecycleView();

        return likeRateFragment;
    }

    private void loadRateWineBottleInRecycleView() {

        //Context context = mRecyclerView.getContext();

        accesLocal = new AccesLocal(getContext());
        ArrayList<WineBottle> wineBottleArrayList = (ArrayList<WineBottle>) accesLocal.recoverRateWineBottleList();

        mRecyclerView.setHasFixedSize(true);

        //mLayoutManager = new LinearLayoutManager(getContext());
        //mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyAdapterCellarRecyclerView(getContext(), wineBottleArrayList);

        mRecyclerView.setAdapter(mAdapter);


        //mRecyclerView.setAlpha(0f);
        //mRecyclerView.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(2500).start();

    }

}
