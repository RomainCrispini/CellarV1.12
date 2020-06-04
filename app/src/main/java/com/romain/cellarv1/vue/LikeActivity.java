package com.romain.cellarv1.vue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.romain.cellarv1.R;
import com.romain.cellarv1.modele.AccesLocal;
import com.romain.cellarv1.modele.WineBottle;
import com.romain.cellarv1.outils.CellarPageAdapter;
import com.romain.cellarv1.outils.CellarTabsTransition;
import com.romain.cellarv1.outils.CurvedBottomNavigationView;
import com.romain.cellarv1.outils.MyAdapterCellarRecyclerView;

import java.util.ArrayList;


public class LikeActivity extends AppCompatActivity {

    // Initialisation de la Custom FAB et de ses caractéristiques
    private FloatingActionButton fabWineMenu, fabRed, fabRose, fabWhite, fabChamp;
    private Boolean isFABWineMenuOpen = false;

    // Initialisation accesLocal
    private AccesLocal accesLocal;

    // Initialisation des Tabs
    private CellarPageAdapter cellarPageAdapter;
    private TabLayout cellarTabLayout;
    private ViewPager viewPager;

    private LikeListFragment likeListFragment;
    private LikeRateFragment likeRateFragment;
    private LikeWishlistFragment likeWishlistFragment;

    // Initialisation du menu bis
    private OvershootInterpolator interpolator = new OvershootInterpolator();
    private FrameLayout sortMenu;
    private ImageButton sortMap;
    private ImageButton sortColor;
    private ImageButton sortYear;
    private ImageButton sortApogee;
    private ImageView sortRecover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);
        init();


    }

    private void init() {

        initCurvedNavigationView();
        initFabWineMenu();
        getFabWineMenuValue();
        initTabs();
        menuBisSelectedItems();

        TabLayout cellarTabLayout = (TabLayout) findViewById(R.id.likeTabLayout);
        cellarTabLayout.setTranslationY(-200f);
        cellarTabLayout.animate().translationY(0f).setInterpolator(interpolator).setDuration(1500).start();

        FrameLayout sortMenu = (FrameLayout) findViewById(R.id.sortMenu);
        sortMenu.setTranslationY(200f);
        sortMenu.animate().translationY(0f).setInterpolator(interpolator).setDuration(1500).start();

    }

    private void initFabWineMenu() {
        fabWineMenu = findViewById(R.id.fabWineMenu);
        fabRed = findViewById(R.id.fabRed);
        fabRose = findViewById(R.id.fabRose);
        fabWhite = findViewById(R.id.fabWhite);
        fabChamp = findViewById(R.id.fabChamp);

        fabWineMenu.setAlpha(1f);
        fabRed.setAlpha(0f);
        fabRose.setAlpha(0f);
        fabWhite.setAlpha(0f);
        fabChamp.setAlpha(0f);

        fabRed.setTranslationX(0f);
        fabRed.setTranslationY(0f);
        fabRose.setTranslationX(0f);
        fabRose.setTranslationY(0f);
        fabWhite.setTranslationX(0f);
        fabWhite.setTranslationY(0f);
        fabChamp.setTranslationX(0f);
        fabChamp.setTranslationY(0f);
    }

    private void getFabWineMenuValue() {
        FloatingActionButton fabWineMenu = (FloatingActionButton) findViewById(R.id.fabWineMenu);
        fabWineMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFABWineMenuOpen) {
                    closeFabWineMenu();
                } else {
                    openFabWineMenu();
                }
                //Toast.makeText(getApplicationContext(), "FAB WINE MENU",Toast.LENGTH_SHORT).show();
            }
        });

        FloatingActionButton fabRed = (FloatingActionButton) findViewById(R.id.fabRed);
        fabRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LikeActivity.this, AddActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("redWine", "redWine");
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                closeFabWineMenu();
                //Toast.makeText(getApplicationContext(), "FAB ROUGE",Toast.LENGTH_SHORT).show();
            }
        });

        FloatingActionButton fabRose = (FloatingActionButton) findViewById(R.id.fabRose);
        fabRose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LikeActivity.this, AddActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("roseWine", "roseWine");
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                closeFabWineMenu();
                //Toast.makeText(getApplicationContext(), "FAB ROSE",Toast.LENGTH_SHORT).show();
            }
        });

        FloatingActionButton fabWhite = (FloatingActionButton) findViewById(R.id.fabWhite);
        fabWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LikeActivity.this, AddActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("whiteWine", "whiteWine");
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                closeFabWineMenu();
                //Toast.makeText(getApplicationContext(), "FAB BLANC",Toast.LENGTH_SHORT).show();
            }
        });

        FloatingActionButton fabChamp = (FloatingActionButton) findViewById(R.id.fabChamp);
        fabChamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LikeActivity.this, AddActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("champWine", "champWine");
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                closeFabWineMenu();
                //Toast.makeText(getApplicationContext(), "FAB CHAMP",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openFabWineMenu() {
        isFABWineMenuOpen = !isFABWineMenuOpen;

        fabWineMenu.animate().setInterpolator(interpolator).rotation(135f).setDuration(300).start();

        fabRed.animate().translationX(-250f).translationY(-180f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabRose.animate().translationX(-90f).translationY(-240f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabWhite.animate().translationX(90f).translationY(-240f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabChamp.animate().translationX(250f).translationY(-180f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
    }

    private void closeFabWineMenu() {
        isFABWineMenuOpen = !isFABWineMenuOpen;

        fabWineMenu.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();

        fabRed.animate().translationX(0f).translationY(0f).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabRose.animate().translationX(0f).translationY(0f).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabWhite.animate().translationX(0f).translationY(0f).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabChamp.animate().translationX(0f).translationY(0f).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
    }

    private void initTabs() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.likeViewPager);
        final TabLayout cellarTabLayout = (TabLayout) findViewById(R.id.likeTabLayout);

        likeListFragment = new LikeListFragment();
        likeRateFragment = new LikeRateFragment();
        likeWishlistFragment = new LikeWishlistFragment();

        cellarTabLayout.setupWithViewPager(viewPager);
        CellarPageAdapter cellarPageAdapter = new CellarPageAdapter(getSupportFragmentManager(), 0);

        viewPager.setPageTransformer(true, new CellarTabsTransition());

        cellarPageAdapter.addFragment(likeListFragment, "Like");
        cellarPageAdapter.addFragment(likeRateFragment, "Rate");
        cellarPageAdapter.addFragment(likeWishlistFragment, "Wish");

        viewPager.setAdapter(cellarPageAdapter);

        cellarTabLayout.getTabAt(0).setIcon(R.drawable.icone_menu_tabs_like);
        cellarTabLayout.getTabAt(1).setIcon(R.drawable.icone_menu_tabs_rate);
        cellarTabLayout.getTabAt(2).setIcon(R.drawable.icone_menu_tabs_wishlist);

        cellarTabLayout.getTabAt(0).getIcon().setColorFilter(getResources().getColor(R.color.green_light), PorterDuff.Mode.SRC_IN);
        cellarTabLayout.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
        cellarTabLayout.getTabAt(2).getIcon().setColorFilter(getResources().getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);


        cellarTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#67828f"), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#4F656F"), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }

    private void menuBisSelectedItems() {

        sortMap = (ImageButton) findViewById(R.id.sortMap);
        sortColor = (ImageButton) findViewById(R.id.sortColor);
        sortRecover = (ImageView) findViewById(R.id.sortRecover);
        sortYear = (ImageButton) findViewById(R.id.sortYear);
        sortApogee = (ImageButton) findViewById(R.id.sortApogee);

        // On sélectionne par défaut l'item du centre
        sortRecover.setColorFilter(getResources().getColor(R.color.green_light));

        sortMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSortMapLikeBottleInListView();
                //loadSortMapWishBottleInListView();
                sortMap.setColorFilter(LikeActivity.this.getColor(R.color.green_light), PorterDuff.Mode.SRC_IN);
                sortColor.setColorFilter(LikeActivity.this.getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortRecover.setColorFilter(LikeActivity.this.getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortYear.setColorFilter(LikeActivity.this.getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortApogee.setColorFilter(LikeActivity.this.getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
            }
        });

        sortColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSortColorLikeBottleInListView();
                //loadSortColorWishBottleInListView();
                sortMap.setColorFilter(LikeActivity.this.getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortColor.setColorFilter(LikeActivity.this.getColor(R.color.green_light), PorterDuff.Mode.SRC_IN);
                sortRecover.setColorFilter(LikeActivity.this.getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortYear.setColorFilter(LikeActivity.this.getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortApogee.setColorFilter(LikeActivity.this.getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
            }
        });

        sortRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLikeBottleInListView();
                //loadWishBottleInListView();
                sortMap.setColorFilter(LikeActivity.this.getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortColor.setColorFilter(LikeActivity.this.getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortRecover.setColorFilter(LikeActivity.this.getColor(R.color.green_light), PorterDuff.Mode.SRC_IN);
                sortYear.setColorFilter(LikeActivity.this.getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortApogee.setColorFilter(LikeActivity.this.getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
            }
        });

        sortYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSortYearLikeBottleInListView();
                //loadSortYearWishBottleInListView();
                sortMap.setColorFilter(LikeActivity.this.getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortColor.setColorFilter(LikeActivity.this.getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortRecover.setColorFilter(LikeActivity.this.getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortYear.setColorFilter(LikeActivity.this.getColor(R.color.green_light), PorterDuff.Mode.SRC_IN);
                sortApogee.setColorFilter(LikeActivity.this.getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
            }
        });

        sortApogee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSortApogeeLikeBottleInListView();
                //loadSortApogeeWishBottleInListView();
                sortMap.setColorFilter(LikeActivity.this.getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortColor.setColorFilter(LikeActivity.this.getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortRecover.setColorFilter(LikeActivity.this.getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortYear.setColorFilter(LikeActivity.this.getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortApogee.setColorFilter(LikeActivity.this.getColor(R.color.green_light), PorterDuff.Mode.SRC_IN);
            }
        });
    }

    private void loadSortMapLikeBottleInListView() {

        RecyclerView cellarRecyclerView = (RecyclerView)findViewById(R.id.likeRecyclerView);
        accesLocal = new AccesLocal(LikeActivity.this);
        ArrayList<WineBottle> wineBottleList = (ArrayList<WineBottle>) accesLocal.sortMapLikeBottleList();

        MyAdapterCellarRecyclerView myAdapterCellarRecyclerView = new MyAdapterCellarRecyclerView(LikeActivity.this, wineBottleList);
        cellarRecyclerView.setAdapter(myAdapterCellarRecyclerView);
        myAdapterCellarRecyclerView.notifyDataSetChanged();
    }

    private void loadSortColorLikeBottleInListView() {

        RecyclerView cellarRecyclerView = (RecyclerView)findViewById(R.id.likeRecyclerView);
        accesLocal = new AccesLocal(LikeActivity.this);
        ArrayList<WineBottle> wineBottleList = (ArrayList<WineBottle>) accesLocal.sortColorLikeBottleList();

        MyAdapterCellarRecyclerView myAdapterCellarRecyclerView = new MyAdapterCellarRecyclerView(LikeActivity.this, wineBottleList);
        cellarRecyclerView.setAdapter(myAdapterCellarRecyclerView);
        myAdapterCellarRecyclerView.notifyDataSetChanged();
    }

    private void loadLikeBottleInListView() {

        RecyclerView cellarRecyclerView = (RecyclerView)findViewById(R.id.likeRecyclerView);
        accesLocal = new AccesLocal(LikeActivity.this);
        ArrayList<WineBottle> wineBottleList = (ArrayList<WineBottle>) accesLocal.recoverLikeWineBottleList();

        MyAdapterCellarRecyclerView myAdapterCellarRecyclerView = new MyAdapterCellarRecyclerView(LikeActivity.this, wineBottleList);
        cellarRecyclerView.setAdapter(myAdapterCellarRecyclerView);
        myAdapterCellarRecyclerView.notifyDataSetChanged();
    }

    private void loadSortYearLikeBottleInListView() {

        RecyclerView cellarRecyclerView = (RecyclerView)findViewById(R.id.likeRecyclerView);
        accesLocal = new AccesLocal(LikeActivity.this);
        ArrayList<WineBottle> wineBottleList = (ArrayList<WineBottle>) accesLocal.sortYearLikeBottleList();

        MyAdapterCellarRecyclerView myAdapterCellarRecyclerView = new MyAdapterCellarRecyclerView(LikeActivity.this, wineBottleList);
        cellarRecyclerView.setAdapter(myAdapterCellarRecyclerView);
        myAdapterCellarRecyclerView.notifyDataSetChanged();
    }

    private void loadSortApogeeLikeBottleInListView() {

        RecyclerView cellarRecyclerView = (RecyclerView)findViewById(R.id.likeRecyclerView);
        accesLocal = new AccesLocal(LikeActivity.this);
        ArrayList<WineBottle> wineBottleList = (ArrayList<WineBottle>) accesLocal.sortApogeeLikeBottleList();

        MyAdapterCellarRecyclerView myAdapterCellarRecyclerView = new MyAdapterCellarRecyclerView(LikeActivity.this, wineBottleList);
        cellarRecyclerView.setAdapter(myAdapterCellarRecyclerView);
        myAdapterCellarRecyclerView.notifyDataSetChanged();
    }

    private void loadSortMapWishBottleInListView() {

        RecyclerView cellarRecyclerView = (RecyclerView)findViewById(R.id.wishlistRecyclerView);
        accesLocal = new AccesLocal(LikeActivity.this);
        ArrayList<WineBottle> wineBottleList = (ArrayList<WineBottle>) accesLocal.sortMapWishBottleList();

        MyAdapterCellarRecyclerView myAdapterCellarRecyclerView = new MyAdapterCellarRecyclerView(LikeActivity.this, wineBottleList);
        cellarRecyclerView.setAdapter(myAdapterCellarRecyclerView);
        myAdapterCellarRecyclerView.notifyDataSetChanged();
    }

    private void loadSortColorWishBottleInListView() {

        RecyclerView cellarRecyclerView = (RecyclerView)findViewById(R.id.wishlistRecyclerView);
        accesLocal = new AccesLocal(LikeActivity.this);
        ArrayList<WineBottle> wineBottleList = (ArrayList<WineBottle>) accesLocal.sortColorWishBottleList();

        MyAdapterCellarRecyclerView myAdapterCellarRecyclerView = new MyAdapterCellarRecyclerView(LikeActivity.this, wineBottleList);
        cellarRecyclerView.setAdapter(myAdapterCellarRecyclerView);
        myAdapterCellarRecyclerView.notifyDataSetChanged();
    }

    private void loadWishBottleInListView() {

        RecyclerView cellarRecyclerView = (RecyclerView)findViewById(R.id.wishlistRecyclerView);
        accesLocal = new AccesLocal(LikeActivity.this);
        ArrayList<WineBottle> wineBottleList = (ArrayList<WineBottle>) accesLocal.recoverWishWineBottleList();

        MyAdapterCellarRecyclerView myAdapterCellarRecyclerView = new MyAdapterCellarRecyclerView(LikeActivity.this, wineBottleList);
        cellarRecyclerView.setAdapter(myAdapterCellarRecyclerView);
        myAdapterCellarRecyclerView.notifyDataSetChanged();
    }

    private void loadSortYearWishBottleInListView() {

        RecyclerView cellarRecyclerView = (RecyclerView)findViewById(R.id.wishlistRecyclerView);
        accesLocal = new AccesLocal(LikeActivity.this);
        ArrayList<WineBottle> wineBottleList = (ArrayList<WineBottle>) accesLocal.sortYearWishBottleList();

        MyAdapterCellarRecyclerView myAdapterCellarRecyclerView = new MyAdapterCellarRecyclerView(LikeActivity.this, wineBottleList);
        cellarRecyclerView.setAdapter(myAdapterCellarRecyclerView);
        myAdapterCellarRecyclerView.notifyDataSetChanged();
    }

    private void loadSortApogeeWishBottleInListView() {

        RecyclerView cellarRecyclerView = (RecyclerView)findViewById(R.id.wishlistRecyclerView);
        accesLocal = new AccesLocal(LikeActivity.this);
        ArrayList<WineBottle> wineBottleList = (ArrayList<WineBottle>) accesLocal.sortApogeeWishBottleList();

        MyAdapterCellarRecyclerView myAdapterCellarRecyclerView = new MyAdapterCellarRecyclerView(LikeActivity.this, wineBottleList);
        cellarRecyclerView.setAdapter(myAdapterCellarRecyclerView);
        myAdapterCellarRecyclerView.notifyDataSetChanged();
    }

    private void initCurvedNavigationView() {
        CurvedBottomNavigationView curvedBottomNavigationView = findViewById(R.id.curvedBottomNavigationView);
        curvedBottomNavigationView.setSelectedItemId(R.id.likeMenu);
        curvedBottomNavigationView.setOnNavigationItemSelectedListener(new CurvedBottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.cellarMenu:
                        //Toast.makeText(UserActivity.this, "USER", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LikeActivity.this, CellarActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        //overridePendingTransition(0, 0);
                        return true;
                    case R.id.mapMenu:
                        //Toast.makeText(UserActivity.this, "USER", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LikeActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        //overridePendingTransition(0, 0);
                        return true;
                    case R.id.scanMenu:
                        //Toast.makeText(UserActivity.this, "SCAN", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LikeActivity.this, ScanActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        //overridePendingTransition(0, 0);
                        return true;
                    case R.id.userMenu:
                        //Toast.makeText(UserActivity.this, "SEARCH", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LikeActivity.this, UserActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        //overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

}