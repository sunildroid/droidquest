package in.technodroid.swap;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;

import org.json.JSONObject;


public class HomeScreen extends AppCompatActivity {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */

    private CharSequence mTitle;
    JSONObject jsonQA;
    DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        mTitle = getTitle();


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().show();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.navigation);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                drawerLayout.closeDrawers();
                menuItem.setChecked(true);
                FragmentManager fragmentManager = getSupportFragmentManager();
                switch (menuItem.getItemId()) {
                   /*case R.id.nav_home:
                        ImageSliderFragment fragImages = new ImageSliderFragment();
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, fragImages, "ImageSlider").commit();
                        break;*/
                    case R.id.nav_Notes:
                        SavedNotes savedNotes = new SavedNotes();
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, savedNotes, "SavedNotes").commit();
                        break;
                    case R.id.nav_QA:
                        QnA fragQA = new QnA();
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, fragQA, "Q&A").commit();
                        break;
                  /*  case R.id.nav_Updates:
                        Updates fragUpdates = new Updates();
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, fragUpdates, "Updates").commit();
                        break;
                    case R.id.nav_SubmitQA:
                        SubmitQA fragSubmit = new SubmitQA();
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, fragSubmit, "SubmitQA").commit();
                        break;*/
                    case R.id.nav_Recommend:
                        Recommendations recommendations = new Recommendations();
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, recommendations, "Recommendations").commit();
                        break;

                  /*  case R.id.nav_Join:
                        JoinCommunity fragJoin = new JoinCommunity();
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, fragJoin, "JoinCommunity").commit();

                        break;*/
                    case R.id.nav_AboutUS:
                        AboutUsFragment fragAboutUS = new AboutUsFragment();
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, fragAboutUS, "AboutUS").commit();
                        break;
                    default:
                        QnA fragQA1 = new QnA();
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, fragQA1, "Q&A").commit();
                        break;

                }
                return true;
            }
        });

        // Initializing Drawer Layout and ActionBarToggle
        //AdRequest.Builder.addTestDevice("374A47C4252A7972AC22FBE654678764") to get test ads on this device.
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
       // AdView mAdView = (AdView) findViewById(R.id.adView);
       // AdRequest adRequest = new AdRequest.Builder().addTestDevice("374A47C4252A7972AC22FBE654678764").build();
       // adRequest.addTestDevice("374A47C4252A7972AC22FBE654678764");
       // mAdView.loadAd(adRequest);

       // PublisherAdView adView = new PublisherAdView(this);
        PublisherAdView adView = (PublisherAdView) findViewById(R.id.adView);
        adView.setAdSizes(AdSize.BANNER);
        PublisherAdRequest padRequest = new PublisherAdRequest.Builder().addTestDevice("374A47C4252A7972AC22FBE654678764").build();
        adView.loadAd(padRequest);


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

        if (savedInstanceState == null) {
            QnA fragImagesaa = new QnA();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragImagesaa, "ImageSlider").commit();
        }

    }

    private void showGlobalContextActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(R.string.app_name);
    }

    private  void setActionBar(){
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                    /* host Activity */
                drawerLayout,                    /* DrawerLayout object */
                toolbar,             /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };
        drawerLayout.setDrawerListener(mDrawerToggle);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
