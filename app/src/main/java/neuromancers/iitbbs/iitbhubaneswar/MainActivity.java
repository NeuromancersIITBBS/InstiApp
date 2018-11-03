package neuromancers.iitbbs.iitbhubaneswar;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,NavFragment.communicator {
    private Switch myswitch;
    private int boolVal;
    private int Val = -1;
    private LinearLayout Nav_Back;
    SaveTheme savetheme;

    private InstiAppUtil instiAppUtil = new InstiAppUtil();

    private ProgressBar progressBar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        savetheme = new SaveTheme(this);
        if(savetheme.loadNightModeSate()==false) {
            setTheme(R.style.AppTheme);
            Val = 0;
        }
        else {
            setTheme(R.style.DarkTheme);
            Val = 1;
        }
        super.onCreate(savedInstanceState);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        setContentView(R.layout.activity_main);

        //Change the Navigation Header background.
        NavigationView navi = (NavigationView)findViewById(R.id.nav_view);
        Nav_Back = navi.getHeaderView(0).findViewById(R.id.header);
        if(Val==1)
            Nav_Back.setBackgroundResource(R.drawable.side_nav_bar_dark);
        else
            Nav_Back.setBackgroundResource(R.drawable.side_nav_bar);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        jumpToHome();
    }
    @Override
    public void themes(View view) {
        myswitch = (Switch)findViewById(R.id.theme_switch);
        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES)
        {
            myswitch.setChecked(true);
        }
        myswitch.setOnCheckedChangeListener (new CompoundButton.OnCheckedChangeListener() {
            //Navigation bar Background

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    savetheme.setNightModeState(true);
                    restartApp();
                } else {
                    savetheme.setNightModeState(false);
                    restartApp();
                }
            }
        });
        Button b = findViewById(R.id.theme_button);
        if(Val!=-1)
            boolVal = Val;
        else if((String)b.getText()=="Dark Theme")
            boolVal = 1;
        if(boolVal==1)
            b.setText((CharSequence)"Light Theme");
        else
            b.setText((CharSequence)"Dark Theme");
        if(boolVal==0) {
            savetheme.setNightModeState(true);
            restartApp();
        }
        else {
            savetheme.setNightModeState(false);
            restartApp();
        }
    }
    public void restartApp()
    {
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
        finish(); }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (findViewById(R.id.about_layout) != null) {
            jumpToHome();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                setTitle("Settings");
                setNavFragment(R.layout.settings);
                break;
            case R.id.action_about:
                setTitle("About");
                setNavFragment(R.layout.about);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_map:
                setTitle("Campus Map");
                setNavFragment(R.layout.map);
                break;
//            case R.id.nav_gymkhana:
//                setTitle("Students' Gymkhana");
//                setNavFragment(R.layout.gymkhana);
//                break;
            case R.id.nav_timetable:
                setTitle("Academic Time Table");
                setNavFragment(R.layout.timetable);
                break;
            case R.id.nav_calendar:
                setTitle("Academic Calendar");
                setNavFragment(R.layout.calendar);
                break;
//            case R.id.nav_messMenu:
//                setTitle("Mess Menu");
//                setNavFragment(R.layout.mess_menu);
//                break;
            case R.id.nav_transport:
                setTitle("Transportation");
                setNavFragment(R.layout.transport);
                break;
            case R.id.nav_holidays:
                setTitle("Public Holidays");
                setNavFragment(R.layout.holiday_list);
                break;
            case R.id.nav_regulations:
                setTitle("Regulations");
                setNavFragment(R.layout.regulations);
                break;
            case R.id.nav_erp:
                Fragment erp_frag = new Erp();
                if (erp_frag != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.content_frame, erp_frag);
                    ft.commit();
                }
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        setNavFragment(R.layout.map);
    }

    /**
     * OnClick function to view files (or download from internet if not available)
     *
     * @param view
     */
    public void downloadFromWeb(View view) {
        String fileName = "";
        String fileExtension = "";
        String website = "";

        switch (view.getId()) {
            case R.id.transport_pdf:
                fileName = "transport_pdf";
                fileExtension = "pdf";
                website = getString(R.string.transport_link);
                break;
            //case R.id.transport_xls:
            //application/vnd.ms-excel
//                String fileName = "transport";
//                String fileExtension = "xls";
//                String website = "http://www.iitbbs.ac.in/transportation.php";
//
//                ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
//                progressBar.setVisibility(View.VISIBLE);
//                IITBbsScraping iitBbsScraping = new IITBbsScraping(website, fileName, fileExtension, progressBar);
//                iitBbsScraping.execute();
            //    break;
            case R.id.timetable_freshers_pdf:
                fileName = "timetable_freshers_pdf";
                fileExtension = "pdf";
                website = getString(R.string.timetable_freshers_link);
                break;
            case R.id.timetable_sbs_pdf:
                fileName = "timetable_sbs_pdf";
                fileExtension = "pdf";
                website = getString(R.string.timetable_sbs_link);
                break;
            case R.id.timetable_seocs_pdf:
                fileName = "timetable_seocs_pdf";
                fileExtension = "pdf";
                website = getString(R.string.timetable_seocs_link);
                break;
            case R.id.timetable_ses_pdf:
                fileName = "timetable_ses_pdf";
                fileExtension = "pdf";
                website = getString(R.string.timetable_ses_link);
                break;
            case R.id.timetable_sif_pdf:
                fileName = "timetable_sif_pdf";
                fileExtension = "pdf";
                website = getString(R.string.timetable_sif_link);
                break;
            case R.id.timetable_smmme_pdf:
                fileName = "timetable_smmme_pdf";
                fileExtension = "pdf";
                website = getString(R.string.timetable_smmme_link);
                break;
            case R.id.timetable_sms_pdf:
                fileName = "timetable_sms_pdf";
                fileExtension = "pdf";
                website = getString(R.string.timetable_sms_link);
                break;
            case R.id.acad_calendar_pdf:
                fileName = "acad_calendar_pdf";
                fileExtension = "pdf";
                website = getString(R.string.acad_calendar_link);
                break;
            case R.id.monthly_autumn_pdf:
                fileName = "monthly_autumn_pdf";
                fileExtension = "pdf";
                website = getString(R.string.monthly_autumn_link);
                break;
            case R.id.monthly_spring_pdf:
                fileName = "monthly_spring_pdf";
                fileExtension = "pdf";
                website = getString(R.string.monthly_spring_link);
                break;

            case R.id.regulations_btech_pdf:
                fileName = "regulations_btech_pdf";
                fileExtension = "pdf";
                website = getString(R.string.regulations_btech_link);
                break;
            case R.id.regulations_msc_pdf:
                fileName = "regulations_msc_pdf";
                fileExtension = "pdf";
                website = getString(R.string.regulations_msc_link);
                break;
            case R.id.regulations_mtech_pdf:
                fileName = "regulations_mtech_pdf";
                fileExtension = "pdf";
                website = getString(R.string.regulations_mtech_link);
                break;
            case R.id.regulations_phd_pdf:
                fileName = "regulations_phd_pdf";
                fileExtension = "pdf";
                website = getString(R.string.regulations_phd_link);
                break;
        }

        IITBbsScraping iitBbsScraping = new IITBbsScraping(website, fileName, fileExtension, progressBar, false);
        iitBbsScraping.execute();
    }

    /**
     * OnClick function to force download from internet (for updating files)
     *
     * @param view
     */
    public void forceDownloadFromWeb(View view) {
        String fileName = "";
        String fileExtension = "";
        String website = "";

        switch (view.getId()) {
            case R.id.transport_pdf_force:
                fileName = "transport_pdf";
                fileExtension = "pdf";
                website = getString(R.string.transport_link);
                break;
            case R.id.timetable_freshers_pdf_force:
                fileName = "timetable_freshers_pdf";
                fileExtension = "pdf";
                website = getString(R.string.timetable_freshers_link);
                break;
            case R.id.timetable_sbs_pdf_force:
                fileName = "timetable_sbs_pdf";
                fileExtension = "pdf";
                website = getString(R.string.timetable_sbs_link);
                break;
            case R.id.timetable_seocs_pdf_force:
                fileName = "timetable_seocs_pdf";
                fileExtension = "pdf";
                website = getString(R.string.timetable_seocs_link);
                break;
            case R.id.timetable_ses_pdf_force:
                fileName = "timetable_ses_pdf";
                fileExtension = "pdf";
                website = getString(R.string.timetable_ses_link);
                break;
            case R.id.timetable_sif_pdf_force:
                fileName = "timetable_sif_pdf";
                fileExtension = "pdf";
                website = getString(R.string.timetable_sif_link);
                break;
            case R.id.timetable_smmme_pdf_force:
                fileName = "timetable_smmme_pdf";
                fileExtension = "pdf";
                website = getString(R.string.timetable_smmme_link);
                break;
            case R.id.timetable_sms_pdf_force:
                fileName = "timetable_sms_pdf";
                fileExtension = "pdf";
                website = getString(R.string.timetable_sms_link);
                break;
            case R.id.acad_calendar_pdf_force:
                fileName = "acad_calendar_pdf";
                fileExtension = "pdf";
                website = getString(R.string.acad_calendar_link);
                break;
            case R.id.monthly_autumn_pdf_force:
                fileName = "monthly_autumn_pdf";
                fileExtension = "pdf";
                website = getString(R.string.monthly_autumn_link);
                break;
            case R.id.monthly_spring_pdf_force:
                fileName = "monthly_spring_pdf";
                fileExtension = "pdf";
                website = getString(R.string.monthly_spring_link);
                break;

            case R.id.regulations_btech_pdf_force:
                fileName = "regulations_btech_pdf";
                fileExtension = "pdf";
                website = getString(R.string.regulations_btech_link);
                break;
            case R.id.regulations_msc_pdf_force:
                fileName = "regulations_msc_pdf";
                fileExtension = "pdf";
                website = getString(R.string.regulations_msc_link);
                break;
            case R.id.regulations_mtech_pdf_force:
                fileName = "regulations_mtech_pdf";
                fileExtension = "pdf";
                website = getString(R.string.regulations_mtech_link);
                break;
            case R.id.regulations_phd_pdf_force:
                fileName = "regulations_phd_pdf";
                fileExtension = "pdf";
                website = getString(R.string.regulations_phd_link);
                break;
        }

        if (!website.isEmpty()) {
            IITBbsScraping iitBbsScraping = new IITBbsScraping(website, fileName, fileExtension, progressBar, true);
            iitBbsScraping.execute();
        }
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

        fragmentTransaction.replace(R.id.content_frame, navFragment);
        fragmentTransaction.commit();
    }
}
