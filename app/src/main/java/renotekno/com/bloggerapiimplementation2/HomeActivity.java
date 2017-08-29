package renotekno.com.bloggerapiimplementation2;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import renotekno.com.bloggerapiimplementation2.Adapter.HomeViewPagerAdapter;

public class HomeActivity extends AppCompatActivity {

    // home viewpager
    private ViewPager homeViewPager;

    // toolbar for action menu and other home menu and act as ActionBar
    private Toolbar homeToolBar;

    // navigation on the bottom of the home activity
    private BottomNavigationView menuBottom;

    // drawer layout for swiping from left to right menu
    private DrawerLayout homeDrawerLayout;

    /*** ActionBarDrawerToogle
     * Listener for {@link #homeDrawerLayout}
     */
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        initView();

        // set ToolBar as ActionBar for this Activity
        // so that getSupportActionBar will return ToolBar as ActionBar
        setSupportActionBar(homeToolBar);

        // check if support ActionBar has been set or not null (!= null)
        if (getSupportActionBar() != null) {
            // enable ActionBar to display Activity title
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            // change the elevation to 4dp (material recommendation of ActionBar elevation)
            getSupportActionBar().setElevation(4);
        }

        /**
         * create new ActionBarDrawerToggle instance as the listener for DrawerLayout
         * This will inflate drawer hamburger to ActionBar on the left and opened when clicked
         */
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, homeDrawerLayout, homeToolBar, R.string.home_drawer_opened, R.string.home_drawer_closed);
        // Add the listener to the drawer layout
        homeDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        // sync state of the indicator if ever the Activity restored
        actionBarDrawerToggle.syncState();

        // Create new instance of adapter for the Viewpager to place fragments
        HomeViewPagerAdapter homeViewPagerAdapter = new HomeViewPagerAdapter(getSupportFragmentManager());
        // link the adapter with the view pager
        homeViewPager.setAdapter(homeViewPagerAdapter);
    }

    private void initView() {
        homeViewPager = (ViewPager) findViewById(R.id.homeViewPager);
        homeToolBar = (Toolbar) findViewById(R.id.homeToolBar);
        menuBottom = (BottomNavigationView) findViewById(R.id.menuBottom);
        homeDrawerLayout= (DrawerLayout) findViewById(R.id.homeDrawerLayout);
    }
}
