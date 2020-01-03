package com.xhat.aminem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    private TextView tvPertanyaanSatu, tvJawabanSatu;
    private TextView tvPertanyaanDua, tvJawabanDua;
    private TextView tvPertanyaanTiga, tvJawabanTiga;
    private TextView tvPertanyaanEmpat, tvJawabanEmpat;
    private TextView tvPertanyaanLima, tvJawabanLima;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("About");

        tvPertanyaanSatu = findViewById(R.id.btnPertanyaanSatu);
        tvJawabanSatu = findViewById(R.id.jawabanSatu);

        tvPertanyaanSatu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(tvJawabanSatu.getVisibility() == View.GONE){
                    tvJawabanSatu.setVisibility(View.VISIBLE);
                }else {
                    tvJawabanSatu.setVisibility(View.GONE);
                }
            }
        });

        tvPertanyaanDua = findViewById(R.id.btnPertanyaanDua);
        tvJawabanDua = findViewById(R.id.jawabanDua);

        tvPertanyaanDua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvJawabanDua.getVisibility() == View.GONE){
                    tvJawabanDua.setVisibility(View.VISIBLE);
                }else {
                    tvJawabanDua.setVisibility(View.GONE);
                }
            }
        });

        tvPertanyaanTiga = findViewById(R.id.btnPertanyaanTiga);
        tvJawabanTiga = findViewById(R.id.jawabanTiga);

        tvPertanyaanTiga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvJawabanTiga.getVisibility() == View.GONE){
                    tvJawabanTiga.setVisibility(View.VISIBLE);
                }else {
                    tvJawabanTiga.setVisibility(View.GONE);
                }
            }
        });

        tvPertanyaanEmpat = findViewById(R.id.btnPertanyaanEmpat);
        tvJawabanEmpat = findViewById(R.id.jawabanEmpat);

        tvPertanyaanEmpat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvJawabanEmpat.getVisibility() == View.GONE){
                    tvJawabanEmpat.setVisibility(View.VISIBLE);
                }else {
                    tvJawabanEmpat.setVisibility(View.GONE);
                }
            }
        });

        tvPertanyaanLima = findViewById(R.id.btnPertanyaanLima);
        tvJawabanLima = findViewById(R.id.jawabanLima);

        tvPertanyaanLima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvJawabanLima.getVisibility() == View.GONE){
                    tvJawabanLima.setVisibility(View.VISIBLE);
                }else {
                    tvJawabanLima.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}