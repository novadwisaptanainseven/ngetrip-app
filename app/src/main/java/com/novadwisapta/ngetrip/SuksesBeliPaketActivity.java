package com.novadwisapta.ngetrip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SuksesBeliPaketActivity extends AppCompatActivity {

    Animation appSplash, btt, ttb;
    Button btnLihatPaket, btnBeranda;
    TextView appTitle, appSubTitle;
    ImageView iconSuccessPaket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sukses_beli_paket);

        btnLihatPaket = findViewById(R.id.btn_view_ticket);
        btnBeranda = findViewById(R.id.btn_beranda);
        appTitle = findViewById(R.id.app_title);
        appSubTitle = findViewById(R.id.app_subtitle);
        iconSuccessPaket = findViewById(R.id.icon_success_ticket);

        // Load Animation
        appSplash = AnimationUtils.loadAnimation(this, R.anim.app_splash);
        btt = AnimationUtils.loadAnimation(this, R.anim.btt);
        ttb = AnimationUtils.loadAnimation(this, R.anim.ttb);

        // Run Animation
        iconSuccessPaket.startAnimation(appSplash);
        appTitle.startAnimation(ttb);
        appSubTitle.startAnimation(ttb);
        btnLihatPaket.startAnimation(btt);
        btnBeranda.startAnimation(btt);

        btnLihatPaket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToProfil = new Intent(SuksesBeliPaketActivity.this, MyProfileActivity.class);
                startActivity(goToProfil);
            }
        });

        btnBeranda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToBeranda = new Intent(SuksesBeliPaketActivity.this, HomeActivity.class);
                startActivity(goToBeranda);
            }
        });
    }
}