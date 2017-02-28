package com.example.android.task4;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private DatabaseHelper myDB;
    private ProgressDialog progressBar;
    private FirebaseDatabase database;
    private DatabaseReference ref, postsRefEx, postsRefIn;
    private int progressBarStatus = 0;
    private Handler progressBarbHandler = new Handler();
    private long fileSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        postsRefEx = ref.child("Overview Expenses");
        postsRefIn = ref.child("Overview Income");

        myDB = new DatabaseHelper(this);
        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();

        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                selectDrawerItem(menuItem);
                return true;
            }
        });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass = Dashboard.class;
        switch (menuItem.getItemId()) {
            case R.id.nav_dashboard:
                fragmentClass = Dashboard.class;
                break;
            case R.id.nav_transaction:
                fragmentClass = Transaction.class;
                break;
            case R.id.nav_synchronize:
                if (!isNetworkAvailable()) {
                    alertSync();
                } else {
                    syncInEx();
                }
                break;
            default:
                fragmentClass = Dashboard.class;
        }

        try

        {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (
                Exception e
                )

        {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().

                replace(R.id.flContent, fragment)

                .

                        commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);

        // Set action bar title
        setTitle(menuItem.getTitle()

        );
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    //region syncExpenses()
    //    public boolean syncExpenses() {
//        if (!isNetworkAvailable()) {
//            alertSync();
//            return false;
//        } else {
//            Expenses expenses = new Expenses();
//            ArrayList<ArrayList<Object>> data_ex = myDB.getAllExpenses();
//
//            for (int posisi = 0; posisi < data_ex.size(); posisi++) {
//                ArrayList<Object> baris = data_ex.get(posisi);
//                expenses.setNama(baris.get(1).toString());
//                expenses.setHarga("$" + baris.get(2).toString());
//
//                firebase.child("Overview Expenses/id : " + baris.get(0).toString()).setValue(expenses);
//            }
//            return true;
//        }
//    }
    //endregion

    //region syncIncome()
//    public boolean syncIncome() {
//        if (!isNetworkAvailable()) {
//            alertSync();
//            return false;
//        } else {
//            Income income = new Income();
//            ArrayList<ArrayList<Object>> data_in = myDB.getAllIncome();
//
//            for (int posisi = 0; posisi < data_in.size(); posisi++) {
//                ArrayList<Object> baris = data_in.get(posisi);
//                income.setNama(baris.get(1).toString());
//                income.setHarga("$" + baris.get(2).toString());
//
//                firebase.child("Overview Income/id : " + baris.get(0).toString()).setValue(income);
//            }
//            return true;
//        }
//    }
    //endregion

    public boolean syncInEx() {
        sync();
        if (!isNetworkAvailable()) {
            alertSync();
            return false;
        } else {
            Expenses expenses = new Expenses();
            ArrayList<ArrayList<Object>> data_ex = myDB.getAllExpenses();

            for (int posisi = 0; posisi < data_ex.size(); posisi++) {
                ArrayList<Object> baris = data_ex.get(posisi);
                expenses.setId(baris.get(0).toString());
                expenses.setNama(baris.get(1).toString());
                expenses.setHarga("$" + baris.get(2).toString());
                postsRefEx.push().setValue(expenses);
            }
            Income income = new Income();
            ArrayList<ArrayList<Object>> data_in = myDB.getAllIncome();

            for (int posisi = 0; posisi < data_in.size(); posisi++) {
                ArrayList<Object> baris = data_in.get(posisi);
                income.setId(baris.get(0).toString());
                income.setNama(baris.get(1).toString());
                income.setHarga("$" + baris.get(2).toString());
                postsRefIn.push().setValue(income);
            }

            return true;
        }
    }

    public void sync() {
        if(isNetworkAvailable()){
            progressBar = new ProgressDialog(this);
            progressBar.setCancelable(true);
            progressBar.setMessage("Proccessing . . . ");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setProgress(0);
            progressBar.setMax(100);
            progressBar.show();
            progressBarStatus = 0;

            fileSize = 0;
            new Thread(new Runnable() {
                public void run() {
                    while (progressBarStatus < 100) {
                        progressBarStatus = downloadFile();

                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        progressBarbHandler.post(new Runnable() {
                            public void run() {
                                progressBar.setProgress(progressBarStatus);
                                if (progressBarStatus >= 100) {
                                    progressBar.dismiss();
                                    Toast.makeText(getBaseContext(), "Data Success Synchronize to Server", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }).start();
        }
    }

    public int downloadFile() {
        while (fileSize <= 100) {
            fileSize++;

            if (fileSize == 10) {
                return 10;
            }else if (fileSize == 20) {
                return 20;
            }else if (fileSize == 30) {
                return 30;
            }else if (fileSize == 40) {
                return 40;
            }else if (fileSize == 50) {
                return 50;
            }else if (fileSize == 70) {
                return 70;
            }else if (fileSize == 80) {
                return 80;
            }
            else if (fileSize == 90) {
                return 90;
            }
        }
        return 100;
    }

    public void alertSync() {
        Toast.makeText(this, "Fails", Toast.LENGTH_SHORT).show();
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Fails Synchronize");
        alertDialogBuilder.setPositiveButton("Retry",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        syncInEx();
                    }
                });

        alertDialogBuilder.setNegativeButton("Skip", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialogBuilder.create().dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
