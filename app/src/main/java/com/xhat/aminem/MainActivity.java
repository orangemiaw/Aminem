package com.xhat.aminem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.xhat.aminem.Fragment.HistoryFragment;
import com.xhat.aminem.Fragment.HomeFragment;
import com.xhat.aminem.Fragment.ProfileFragment;
import com.xhat.aminem.Fragment.SearchFragment;
import com.xhat.aminem.Utils.Constant;
import com.xhat.aminem.Utils.Helper;
import com.xhat.aminem.Utils.SessionManager;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment selectedFragment = null;
    Context mContext;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mContext = this;
        sessionManager = new SessionManager(this);

        // Code berikut berfungsi untuk mengecek session, Jika session false ( belum login )
        // maka langsung meredirect ke LoginActivity.
        if (!sessionManager.getSPSudahLogin()){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_search:
                            selectedFragment = new SearchFragment();
                            break;
                        case R.id.nav_add:
                            selectedFragment = null;
                            startActivity(new Intent(MainActivity.this, PostActivity.class));
                            break;
                        case R.id.nav_heart:
                            selectedFragment = new HistoryFragment();
                            break;
                        case R.id.nav_profile:
                            selectedFragment = new ProfileFragment();
                            break;
                    }

                    if(selectedFragment != null){
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                selectedFragment).commit();
                    }

                    return true;
                }
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.top_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.log_activity:
                startActivity(new Intent(mContext, LogActivity.class));
                break;
            case R.id.about:
                startActivity(new Intent(mContext, AboutActivity.class));
                break;
            case R.id.community:
                String appName = "org.telegram.messenger";
                boolean isAppInstalled = Helper.isAppAvailable(mContext, appName);
                if (isAppInstalled) {
                    Intent telegram = new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.TELEGRAM_URL_GROUP));
                    telegram.setPackage(appName);
                    startActivity(telegram);
                } else {
                    startActivity(new Intent(Intent.ACTION_VIEW , Uri.parse(Constant.TELEGRAM_URL_GROUP)));
                }
                break;
            case R.id.logout:
                logOutConfirmation();
                break;
        }

        return true;
    }

    private void logOutConfirmation(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title dialog
        alertDialogBuilder.setTitle("Hmm, sure?");

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("Are you sure you want to log out? It will make this app fell heartbroken. Please, don't go :(")
                .setIcon(R.drawable.ic_help)
                .setCancelable(false)
                .setNeutralButton("Yes, of course",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Helper.clearSession(mContext);
                        startActivity(new Intent(mContext, LoginActivity.class));
                        finish();
                    }
                })
                .setNegativeButton("No, I changed my mind",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog logOutDialog = alertDialogBuilder.create();
        logOutDialog.show();
    }
}
