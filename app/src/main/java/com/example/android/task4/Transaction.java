package com.example.android.task4;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Luteh on 2/7/2017.
 */

public class Transaction extends Fragment implements View.OnClickListener {
    private DatabaseHelper transaksiDB;
    private EditText et_ex_des, et_in_des, et_ex_am, et_in_am;
    private Button btn_add_ex, btn_add_in;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_transaction, container, false);

        transaksiDB = new DatabaseHelper(getContext());
        btn_add_ex = (Button) view.findViewById(R.id.btn_add_ex);
        btn_add_in = (Button) view.findViewById(R.id.btn_add_in);
        btn_add_ex.setOnClickListener(this);
        btn_add_in.setOnClickListener(this);

        et_ex_des = (EditText) view.findViewById(R.id.et_ex_des);
        et_ex_am = (EditText) view.findViewById(R.id.et_ex_am);
        et_in_des = (EditText) view.findViewById(R.id.et_in_des);
        et_in_am = (EditText) view.findViewById(R.id.et_in_am);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_ex:
                String ex_am = et_ex_am.getText().toString();
                String ex_des = et_ex_des.getText().toString();

                if (ex_am.equals("") || ex_des.equals("")) {
                    Toast.makeText(getActivity(), "Isi Description dan Amount terlebih dahulu!", Toast.LENGTH_LONG).show();
                } else {
                    boolean result_ex = transaksiDB.saveExpenses(ex_des, ex_am);
                    if (result_ex)
                        Toast.makeText(getActivity(), "Success Add New Expenses", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getActivity(), "Fails Add New Expenses", Toast.LENGTH_LONG).show();
                    et_ex_des.setText("");
                    et_ex_am.setText("");
                }
                break;
            case R.id.btn_add_in:
                String in_am = et_in_am.getText().toString();
                String in_des = et_in_des.getText().toString();

                if (in_am.equals("") || in_des.equals("")) {
                    Toast.makeText(getActivity(), "Isi Description dan Amount terlebih dahulu!", Toast.LENGTH_LONG).show();
                } else {
                    boolean result_in = transaksiDB.saveIncome(in_des, in_am);
                    if (result_in)
                        Toast.makeText(getActivity(), "Success Add New Income", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getActivity(), "Fails Add New Income", Toast.LENGTH_LONG).show();
                    et_in_des.setText("");
                    et_in_am.setText("");
                }
                break;
        }
    }

//
}
