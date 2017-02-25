package com.example.android.task4;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luteh on 2/7/2017.
 */

public class Dashboard extends Fragment {
    DatabaseHelper myDB;
    TableLayout tabelExpenses, tabelIncome;
    TextView total_ex, total_in, tv_balance;
    int totalex = 0, totalin = 0, balance = 0;
    ProgressDialog progressDialog;
    private Button btn_del;
    Firebase firebase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_dashboard, container, false);

        tabelExpenses = (TableLayout) view.findViewById(R.id.tabel_data_ex);
        tabelIncome = (TableLayout) view.findViewById(R.id.tabel_data_in);
        total_ex = (TextView) view.findViewById(R.id.tv_total_ex);
        total_in = (TextView) view.findViewById(R.id.tv_total_in);
        tv_balance = (TextView) view.findViewById(R.id.tv_balance);
        btn_del = (Button) view.findViewById(R.id.btn_del);

        myDB = new DatabaseHelper(getContext());
        Firebase.setAndroidContext(getActivity());

        firebase = new Firebase("https://geeksfarm-task4.firebaseio.com");
        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDB.deleteSQL();
                firebase.removeValue();
                Toast.makeText(getActivity(),"Deleted",Toast.LENGTH_SHORT).show();
            }
        });

        updateTableExpenses();
        updateTableIncome();
        balanceResult();
//        progressDialog.dismiss();
        return view;
    }

    protected void updateTableExpenses() {
//  TODO  Auto-generated  method  stub
        while (tabelExpenses.getChildCount() > 1) {
            tabelExpenses.removeViewAt(1);
        }

        ArrayList<ArrayList<Object>> data = myDB.getAllExpenses();

        for (int posisi = 0; posisi < data.size(); posisi++) {
            TableRow tabelBaris = new TableRow(getContext());
            ArrayList<Object> baris = data.get(posisi);

            TextView idTxt = new TextView(getContext());
            idTxt.setText(baris.get(0).toString());
            tabelBaris.addView(idTxt);

            TextView namaTxt = new TextView(getContext());
            namaTxt.setText(baris.get(1).toString());
            tabelBaris.addView(namaTxt);

            TextView hargaTxt = new TextView(getContext());
            hargaTxt.setText("$" + baris.get(2).toString());
            tabelBaris.addView(hargaTxt);

            tabelExpenses.addView(tabelBaris);
            totalex += Integer.parseInt(baris.get(2).toString());
        }
        total_ex.setText("$" + totalex);
    }

    protected void updateTableIncome() {
//  TODO  Auto-generated  method  stub
        while (tabelIncome.getChildCount() > 1) {
            tabelIncome.removeViewAt(1);
        }

        ArrayList<ArrayList<Object>> data = myDB.getAllIncome();//

        for (int posisi = 0; posisi < data.size(); posisi++) {
            TableRow tabelBaris = new TableRow(getContext());
            ArrayList<Object> baris = data.get(posisi);

            TextView idTxt = new TextView(getContext());
            idTxt.setText(baris.get(0).toString());
            tabelBaris.addView(idTxt);

            TextView namaTxt = new TextView(getContext());
            namaTxt.setText(baris.get(1).toString());
            tabelBaris.addView(namaTxt);

            TextView hargaTxt = new TextView(getContext());
            hargaTxt.setText("$" + baris.get(2).toString());
            tabelBaris.addView(hargaTxt);

            tabelIncome.addView(tabelBaris);
            totalin += Integer.parseInt(baris.get(2).toString());
        }
        total_in.setText("$" + totalin);

    }

    public void balanceResult() {
        balance = totalin - totalex;
        tv_balance.setText("$" + balance);
    }
}
