package ke.co.qkut.qkut.views.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ke.co.qkut.qkut.R;
import ke.co.qkut.qkut.constants.GLobalHeaders;
import ke.co.qkut.qkut.constants.QkutBase;
import ke.co.qkut.qkut.constants.URL;
import ke.co.qkut.qkut.controller.adapters.PageAdapter;
import ke.co.qkut.qkut.datastore.LocalDatabase;
import ke.co.qkut.qkut.interfaces.ChangeInvoked;
import ke.co.qkut.qkut.interfaces.LogInListener;
import ke.co.qkut.qkut.models.Person;
import ke.co.qkut.qkut.util.newtork.local.NetworkConnection;
import ke.co.qkut.qkut.util.newtork.local.OnReceivingResult;
import ke.co.qkut.qkut.util.newtork.local.RemoteResponse;
import ke.co.qkut.qkut.views.dialogs.QRDialog;
import ke.co.qkut.qkut.views.dialogs.SortDialog;
import ke.co.qkut.qkut.views.fragments.HomeFragment;
import ke.co.qkut.qkut.views.fragments.LoginFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DrawerLayout.DrawerListener,LogInListener {
    public static  ViewPager viewPager;
    private  static  PageAdapter pageAdapter;
    private  static  String [] PAGES_NAME_NOT_LOGGED_IN= new String[]{"Home","Login","Sign Up"};
    private  static String [] PAGES_NAME_LOGGED_IN= new String[]{"Home","Schedule","Alerts"};
    static TextView textView;
    static ImageView nav_profile_photo,sort;

FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sort=findViewById(R.id.sort);
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SortDialog sortDialog= new SortDialog();
                sortDialog.show(getSupportFragmentManager(),"sort");
            }
        });
        floatingActionButton=findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QRDialog qrDialog= new QRDialog();


                qrDialog.show(getSupportFragmentManager(), "user");

            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.addDrawerListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View view=navigationView.getHeaderView(0);
        textView=view.findViewById(R.id.nav_profile_name);
        nav_profile_photo=view.findViewById(R.id.nav_profile_photo);
        navigationView.setNavigationItemSelectedListener(this);


    }

    private void createNotice() {
        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), "1");
        notificationBuilder.setContentTitle("david");
        notificationBuilder.setSmallIcon(R.mipmap.logo_qkut);
        notificationBuilder.setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName()+R.raw.sound_effect_message ));
        NotificationManager notificationManager =(NotificationManager)getApplicationContext(). getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify_001",
                    "Channel human readable title",NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(1 + 2 /* ID of notification */, notificationBuilder.build());
    }

    @Override
    protected void onResume() {
        String token;
        viewPager= findViewById(R.id.pager);
        if ((token=LocalDatabase.getToken(this)).equals(LocalDatabase.NOT_LOGGED_IN)){
            LoginFragment.setLogInListener(this);
            pageAdapter=new PageAdapter( viewPager,MainActivity.this,PAGES_NAME_NOT_LOGGED_IN);
            pageAdapter.setLoggedIn(false);
            viewPager.setAdapter(pageAdapter);

        }else{
            FirebaseApp.initializeApp(getApplicationContext());
            fetchUser(MainActivity.this);
            pageAdapter=new PageAdapter( viewPager,MainActivity.this,PAGES_NAME_LOGGED_IN);
            floatingActionButton.setVisibility(View.VISIBLE);
            pageAdapter.setLoggedIn(true);
            viewPager.setAdapter(pageAdapter);


        }
        // Give the PagerSlidingTabStrip the ViewPager
        PagerSlidingTabStrip tabsStrip = findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);

        super.onResume();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);


        return true;
    }
    public  static void fetchUser(final AppCompatActivity appCompatActivity){
        NetworkConnection.makeAGetRequest(URL.USER_ACCOUNT, GLobalHeaders.getGlobalHeaders(appCompatActivity), new OnReceivingResult() {
            @Override
            public void onErrorResult(IOException e) {

            }

            @Override
            public void onReceiving100SeriesResponse(RemoteResponse remoteResponse) {

            }

            @Override
            public void onReceiving200SeriesResponse(RemoteResponse remoteResponse) {
                try {
                    JSONObject jsonObject= new JSONObject(remoteResponse.getMessage());
                    Person person=Person.getPerson(jsonObject.getString("data"));
                    getProfilePicture(person,appCompatActivity);
                    QkutBase.setPerson(person);
                    textView.setText(person.getName());
                    if ( LoginFragment.getLogInListener()!=null){
                        LoginFragment.getLogInListener().onLogin(person);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                NetworkConnection.remoteResponseLogger(remoteResponse);
            }

            @Override
            public void onReceiving300SeriesResponse(RemoteResponse remoteResponse) {

            }

            @Override
            public void onReceiving400SeriesResponse(RemoteResponse remoteResponse) {

            }

            @Override
            public void onReceiving500SeriesResponse(RemoteResponse remoteResponse) {

            }
        });
    }

    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {

    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {
        if (DrawerLayout.STATE_IDLE==newState){

        }else{

        }
    }
    public static  void getProfilePicture(final Person person, AppCompatActivity appCompatActivity){
        NetworkConnection.downloadImage(URL.USER_PROFILE_PIC, GLobalHeaders.getGlobalHeaders(appCompatActivity), new OnReceivingResult() {
            @Override
            public void onErrorResult(IOException e) {

            }

            @Override
            public void onReceiving100SeriesResponse(RemoteResponse remoteResponse) {

            }

            @Override
            public void onReceiving200SeriesResponse(RemoteResponse remoteResponse) {
                person.setProfileByte(remoteResponse.getResponseBody());
                nav_profile_photo.setImageBitmap(person.getProfileByte());
                NetworkConnection.remoteResponseLogger(remoteResponse);
                // Toast.makeText(appCompatActivity, "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReceiving300SeriesResponse(RemoteResponse remoteResponse) {

            }

            @Override
            public void onReceiving400SeriesResponse(RemoteResponse remoteResponse) {

            }

            @Override
            public void onReceiving500SeriesResponse(RemoteResponse remoteResponse) {

            }
        });
    }


    @Override
    public void onLogin(Person person) {

    }
}
