package io.github.archbloom.greenenergy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    ImageView display_pic;
    TextView personName;
    NavigationView mNavigationView;

   // FragmentManager to handle the fragments
    FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //------------------------------------------------------------------------------------------
        View headerView = mNavigationView.inflateHeaderView(R.layout.nav_header_main);
        display_pic = (ImageView)headerView.findViewById(R.id.display_pic);
        personName = (TextView)headerView.findViewById(R.id.person_name);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            if(bundle.getString("URL")!=null){
                if(display_pic==null){
                    Log.d("ABC"," null");
                }
                else {
                    Picasso.with(this).load(bundle.getString("URL"))
                            .fit()
                            .into(display_pic);
                }
            }
            if(bundle.getString("Name")!=null){
                personName.setText(bundle.getString("Name"));
            }
        }
        //------------------------------------------------------------------------------------------

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setTitle("Home");
        fragmentManager.beginTransaction().replace(R.id.content_main, new Home()).commit();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                View view = (View) findViewById(R.id.content_main);
                switch (item.getItemId()) {
                    case R.id.home:
                        Log.d("ABC", "home");
                        getSupportActionBar().setTitle("Home");
                        Home home = new Home();
                        fragmentManager.beginTransaction().replace(R.id.content_main, home).commit();
                        drawer.closeDrawer(navigationView);
                        return true;
                    case R.id.profile:
                        Log.d("ABC","Manage Profile");
                        Intent intent = new Intent(MainActivity.this,ManageProfile.class);
                        drawer.closeDrawer(navigationView);
                        startActivity(intent);
                        return true;
                    case R.id.dashboard:
                        getSupportActionBar().setTitle("Dashboard");
                        fragmentManager.beginTransaction().replace(R.id.content_main, new Dashboard()).commit();
                        drawer.closeDrawer(navigationView);
                        return true;
                    case R.id.showTips:
                        getSupportActionBar().setTitle("Tips to save Energy.");
                        Log.d("ABC","Show tips");
                        ShowTips showTips = new ShowTips();
                        fragmentManager.beginTransaction().replace(R.id.content_main,showTips).commit();
                        drawer.closeDrawer(navigationView);
                        return true;

                }
                return true;
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(UniversalData.getBaseUrl())
                .build();
        MyAPI myAPI = retrofit.create(MyAPI.class);
        if (id == R.id.indoor) {
            Toast.makeText(getApplicationContext(),"Indoor",Toast.LENGTH_SHORT).show();
            Map<String,String> mapp = new HashMap<>();
            mapp.put("Name","Indoor");
            Call<DData> call = myAPI.getProfile(mapp);
            call.enqueue(new Callback<DData>() {
                @Override
                public void onResponse(Response<DData> response, Retrofit retrofit) {
                    Log.d("ABC","Indoor success");
                }
                @Override
                public void onFailure(Throwable t) {

                }
            });

        }
        else{
            Toast.makeText(getApplicationContext(),"outdoor",Toast.LENGTH_SHORT).show();
            Map<String,String> mapp = new HashMap<>();
            mapp.put("Name","Outdoor");
            Call<DData> call = myAPI.getProfile(mapp);
            call.enqueue(new Callback<DData>() {
                @Override
                public void onResponse(Response<DData> response, Retrofit retrofit) {
                    Log.d("ABC","Outdoor success");
                }
                @Override
                public void onFailure(Throwable t) {

                }
            });
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }
}