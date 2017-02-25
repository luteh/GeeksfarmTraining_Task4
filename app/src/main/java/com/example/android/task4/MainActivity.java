package com.example.android.task4;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
//    private Button btn_del;
    DatabaseHelper myDB;
    ProgressDialog progressDialog;
    Firebase firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        btn_del = (Button) findViewById(R.id.btn_del);
//        btn_del.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                myDB.deleteSQL();
//                firebase.removeValue();
//            }
//        });

        Firebase.setAndroidContext(this);

        firebase = new Firebase("https://geeksfarm-task4.firebaseio.com");

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
        Class fragmentClass;
        switch (menuItem.getItemId()) {
            case R.id.nav_dashboard:
                fragmentClass = Dashboard.class;
                break;
            case R.id.nav_transaction:
                fragmentClass = Transaction.class;
                break;
            case R.id.nav_synchronize:
//                progressDialog = new ProgressDialog(MainActivity.this);
//                progressDialog.setTitle("Proccessing");
//                progressDialog.setMessage("Proccessing ...");
//                progressDialog.show();
//                sync();

                syncExpenses();
                syncIncome();
                Toast.makeText(this, "Data Success Synchronize to Server", Toast.LENGTH_SHORT).show();
                fragmentClass = Dashboard.class;
//                progressDialog.dismiss();
                break;
            default:
                fragmentClass = Dashboard.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    public void syncExpenses() {
        Expenses expenses = new Expenses();
        ArrayList<ArrayList<Object>> data_ex = myDB.getAllExpenses();

        for (int posisi = 0; posisi < data_ex.size(); posisi++) {
            ArrayList<Object> baris = data_ex.get(posisi);
            expenses.setNama(baris.get(1).toString());
            expenses.setHarga("$" + baris.get(2).toString());

            firebase.child("Overview Expenses/id : " + baris.get(0).toString()).setValue(expenses);
        }

    }

    public void syncIncome() {
        Income income = new Income();
        ArrayList<ArrayList<Object>> data_in = myDB.getAllIncome();

        for (int posisi = 0; posisi < data_in.size(); posisi++) {
            ArrayList<Object> baris = data_in.get(posisi);
            income.setNama(baris.get(1).toString());
            income.setHarga("$" + baris.get(2).toString());

            firebase.child("Overview Income/id : " + baris.get(0).toString()).setValue(income);
        }
    }

    public void sync() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Proccessing");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgress(0);
        progressDialog.show();

        final int totalProgressTime = 100;
        final Thread t = new Thread() {
            @Override
            public void run() {
                int jumpTime = 0;

                while (jumpTime < totalProgressTime) {
                    try {
                        sleep(200);
                        jumpTime += 5;
                        progressDialog.setProgress(jumpTime);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
    }

}
