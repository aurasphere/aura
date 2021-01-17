package co.aurasphere.aura.nebula;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import co.aurasphere.aura.nebula.ioc.AuraNebulaComponent;
import co.aurasphere.aura.nebula.modules.dashboard.view.DashboardFragment;
import co.aurasphere.aura.nebula.modules.monitor.MonitorFragment;
import co.aurasphere.aura.nebula.modules.social.SocialFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private int currentView = 0;

    private AuraNebulaComponent dependencyInjector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Gets the dependency injector from application.
//        dependencyInjector = ((AuraNebula) getApplication()).getDependencyInjector();

        setContentView(R.layout.navigation_drawer_main_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Starts on dashboard view.
        MenuItem dashboardMenuItem = navigationView.getMenu().getItem(0);
        onNavigationItemSelected(dashboardMenuItem);
        dashboardMenuItem.setChecked(true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (id == R.id.nav_dashboard && currentView != R.id.nav_dashboard) {
//           new WolController().execute();
            currentView = R.id.nav_dashboard;
            toolbar.setTitle("Aura Dashboard");
            getFragmentManager().beginTransaction().replace(R.id.main_content, new DashboardFragment()).commit();

        } else if (id == R.id.nav_social && currentView != R.id.nav_social) {
            currentView = R.id.nav_social;
            toolbar.setTitle("Aura Social Controller");
            getFragmentManager().beginTransaction().replace(R.id.main_content, new SocialFragment()).commit();

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_monitor) {
            currentView = R.id.nav_monitor;
            toolbar.setTitle("Aura Galaxia Monitor");
            getFragmentManager().beginTransaction().replace(R.id.main_content, new MonitorFragment()).commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
