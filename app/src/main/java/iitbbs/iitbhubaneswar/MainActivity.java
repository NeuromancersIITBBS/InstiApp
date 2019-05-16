package iitbbs.iitbhubaneswar;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.internal.NavigationMenu;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    boolean doublepress=false;
    private InstiAppUtil instiAppUtil = new InstiAppUtil();

    private ProgressBar progressBar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        setContentView(R.layout.new_home);
        Toolbar toolbar = (Toolbar) findViewById(iitbbs.iitbhubaneswar.R.id.toolbar);
        setSupportActionBar(toolbar);

//        DrawerLayout drawer = (DrawerLayout) findViewById(iitbbs.iitbhubaneswar.R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, iitbbs.iitbhubaneswar.R.string.navigation_drawer_open, iitbbs.iitbhubaneswar.R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(iitbbs.iitbhubaneswar.R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        progressBar = (ProgressBar) findViewById(iitbbs.iitbhubaneswar.R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        jumpToHome();
    }

    @Override
    public void onBackPressed() {
        WebView webView = (WebView) this.findViewById( iitbbs.iitbhubaneswar.R.id.erp_webview );
        DrawerLayout drawer = (DrawerLayout) findViewById(iitbbs.iitbhubaneswar.R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        else if(findViewById(iitbbs.iitbhubaneswar.R.id.map_layout) != null)  {
            if(doublepress)
                super.onBackPressed();
            else{
                this.doublepress=true;
                Snackbar snackbar = Snackbar.make( drawer,"Please press BACK again to exit", Snackbar.LENGTH_SHORT );
                snackbar.show();
                View snackview = snackbar.getView();
                TextView textView = snackview.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.rgb( 255,255,255 ) );
                new Handler().postDelayed( new Runnable() {
                    @Override
                    public void run() {
                        doublepress=false;
                    }
                },2000 );
            }}
        else if(findViewById( iitbbs.iitbhubaneswar.R.id.erp_webview  )!=null&&webView.canGoBack()) {
            webView.goBack();
        }
        else
            jumpToHome();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(iitbbs.iitbhubaneswar.R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
//            case R.id.action_settings:
//                setNavFragment(R.layout.settings);
//                break;
            case iitbbs.iitbhubaneswar.R.id.action_about:
                setTitle("About");
                setNavFragment(iitbbs.iitbhubaneswar.R.layout.about);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case iitbbs.iitbhubaneswar.R.id.nav_map:
                setTitle("Campus Map");
                setNavFragment(iitbbs.iitbhubaneswar.R.layout.map);
                break;
            case iitbbs.iitbhubaneswar.R.id.nav_gymkhana:
                setTitle("Students' Gymkhana Office");
                setNavFragment(iitbbs.iitbhubaneswar.R.layout.gymkhana);
                break;
            case iitbbs.iitbhubaneswar.R.id.nav_timetable:
                setTitle("Academic Time Table");
                setNavFragment(iitbbs.iitbhubaneswar.R.layout.timetable);
                break;
            case iitbbs.iitbhubaneswar.R.id.nav_calendar:
                setTitle("Academic Calendar");
                setNavFragment(iitbbs.iitbhubaneswar.R.layout.calendar);
                break;
            case iitbbs.iitbhubaneswar.R.id.nav_messMenu:
                setTitle("Mess Menu");
                setNavFragment(iitbbs.iitbhubaneswar.R.layout.mess_menu);
                break;
            case iitbbs.iitbhubaneswar.R.id.nav_transport:
                setTitle("Transportation");
                setNavFragment(iitbbs.iitbhubaneswar.R.layout.transport);
                break;
            case iitbbs.iitbhubaneswar.R.id.nav_holidays:
                setTitle("Public Holidays");
                setNavFragment(iitbbs.iitbhubaneswar.R.layout.holiday_list);
                break;
            case iitbbs.iitbhubaneswar.R.id.nav_regulations:
                setTitle("Regulations");
                setNavFragment(iitbbs.iitbhubaneswar.R.layout.regulations);
                break;
            case iitbbs.iitbhubaneswar.R.id.nav_erp:
                Fragment erp_frag = new Erp();
                if (erp_frag != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    ft.replace(iitbbs.iitbhubaneswar.R.id.content_frame, erp_frag);
                    ft.commit();
                }
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(iitbbs.iitbhubaneswar.R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void redirectToWeb(View view) {
        instiAppUtil.onClick(view);
    }

    /**
     * OnClick function to handle intents for mail
     *
     * @param view
     */
    public void composeMail(View view) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"secyprogsoc.sg@iitbbs.ac.in"});
        startActivity(intent);
    }

    private void jumpToHome() {
        setTitle("Campus Map");
        setNavFragment(iitbbs.iitbhubaneswar.R.layout.map);
    }

    /**
     * OnClick function to view files (or download from internet if not available)
     *
     * @param view
     */
    public void downloadFromWeb(View view) {
        String fileName = "";

        switch (view.getId()) {
            case iitbbs.iitbhubaneswar.R.id.transport_pdf:
                fileName = "transport";
                break;
            case iitbbs.iitbhubaneswar.R.id.timetable_freshers_pdf:
                fileName = "timetable_freshers";
                break;
            case iitbbs.iitbhubaneswar.R.id.timetable_sbs_pdf:
                fileName = "timetable_sbs";
                break;
            case iitbbs.iitbhubaneswar.R.id.timetable_seocs_pdf:
                fileName = "timetable_seocs";
                break;
            case iitbbs.iitbhubaneswar.R.id.timetable_ses_ece_pdf:
                fileName = "timetable_ses_ece";
                break;
            case iitbbs.iitbhubaneswar.R.id.timetable_ses_cse_pdf:
                fileName = "timetable_ses_cse";
                break;
            case iitbbs.iitbhubaneswar.R.id.timetable_ses_ee_pdf:
                fileName = "timetable_ses_ee";
                break;
            case iitbbs.iitbhubaneswar.R.id.timetable_sif_pdf:
                fileName = "timetable_sif";
                break;
            case iitbbs.iitbhubaneswar.R.id.timetable_smmme_pdf:
                fileName = "timetable_smmme";
                break;
            case iitbbs.iitbhubaneswar.R.id.timetable_sms_pdf:
                fileName = "timetable_sms";
                break;
            case iitbbs.iitbhubaneswar.R.id.timetable_phd_pdf:
                fileName = "timetable_phd";
                break;
            case iitbbs.iitbhubaneswar.R.id.acad_calendar_pdf:
                fileName = "acad_calendar";
                break;
            case iitbbs.iitbhubaneswar.R.id.monthly_autumn_pdf:
                fileName = "monthly_autumn";
                break;
            case iitbbs.iitbhubaneswar.R.id.monthly_spring_pdf:
                fileName = "monthly_spring";
                break;
            case iitbbs.iitbhubaneswar.R.id.regulations_btech_pdf:
                fileName = "regulations_btech";
                break;
            case iitbbs.iitbhubaneswar.R.id.regulations_msc_pdf:
                fileName = "regulations_msc";
                break;
            case iitbbs.iitbhubaneswar.R.id.regulations_mtech_pdf:
                fileName = "regulations_mtech";
                break;
            case iitbbs.iitbhubaneswar.R.id.regulations_phd_pdf:
                fileName = "regulations_phd";
                break;
            case iitbbs.iitbhubaneswar.R.id.mess_menu_pdf:
                fileName = "mess_menu";
                break;
        }

        IITBbsScraping iitBbsScraping = new IITBbsScraping(fileName, progressBar, false);
        iitBbsScraping.execute();
    }

    /**
     * OnClick function to force download from internet (for updating files)
     *
     * @param view
     */
    public void forceDownloadFromWeb(View view) {
        String fileName = "";

        switch (view.getId()) {
            case iitbbs.iitbhubaneswar.R.id.transport_pdf_force:
                fileName = "transport";
                break;
            case iitbbs.iitbhubaneswar.R.id.timetable_freshers_pdf_force:
                fileName = "timetable_freshers";
                break;
            case iitbbs.iitbhubaneswar.R.id.timetable_sbs_pdf_force:
                fileName = "timetable_sbs";
                break;
            case iitbbs.iitbhubaneswar.R.id.timetable_seocs_pdf_force:
                fileName = "timetable_seocs";
                break;
            case iitbbs.iitbhubaneswar.R.id.timetable_ses_ece_pdf_force:
                fileName = "timetable_ses_ece";
                break;
            case iitbbs.iitbhubaneswar.R.id.timetable_ses_cse_pdf_force:
                fileName = "timetable_ses_cse";
                break;
            case iitbbs.iitbhubaneswar.R.id.timetable_ses_ee_pdf_force:
                fileName = "timetable_ses_ee";
                break;
            case iitbbs.iitbhubaneswar.R.id.timetable_sif_pdf_force:
                fileName = "timetable_sif";
                break;
            case iitbbs.iitbhubaneswar.R.id.timetable_smmme_pdf_force:
                fileName = "timetable_smmme";
                break;
            case iitbbs.iitbhubaneswar.R.id.timetable_sms_pdf_force:
                fileName = "timetable_sms";
                break;
            case iitbbs.iitbhubaneswar.R.id.timetable_phd_pdf_force:
                fileName = "timetable_phd";
                break;
            case iitbbs.iitbhubaneswar.R.id.acad_calendar_pdf_force:
                fileName = "acad_calendar";
                break;
            case iitbbs.iitbhubaneswar.R.id.monthly_autumn_pdf_force:
                fileName = "monthly_autumn";
                break;
            case iitbbs.iitbhubaneswar.R.id.monthly_spring_pdf_force:
                fileName = "monthly_spring";
                break;
            case iitbbs.iitbhubaneswar.R.id.regulations_btech_pdf_force:
                fileName = "regulations_btech";
                break;
            case iitbbs.iitbhubaneswar.R.id.regulations_msc_pdf_force:
                fileName = "regulations_msc";
                break;
            case iitbbs.iitbhubaneswar.R.id.regulations_mtech_pdf_force:
                fileName = "regulations_mtech";
                break;
            case iitbbs.iitbhubaneswar.R.id.regulations_phd_pdf_force:
                fileName = "regulations_phd";
                break;
            case iitbbs.iitbhubaneswar.R.id.mess_menu_pdf_force:
                fileName = "mess_menu";
                break;
        }

        IITBbsScraping iitBbsScraping = new IITBbsScraping(fileName, progressBar, true);
        iitBbsScraping.execute();
    }

    /**
     * Replace fragments in navigation drawer setup on changing navigation drawer options
     *
     * @param navLayout
     */
    private void setNavFragment(int navLayout) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        NavFragment navFragment = new NavFragment();
        navFragment.setNewLayout(navLayout);

        fragmentTransaction.replace(iitbbs.iitbhubaneswar.R.id.content_frame, navFragment);
        fragmentTransaction.commit();
    }
}
