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

public class SuccessRegisterActivity extends AppCompatActivity {
    Button btn_explore, btn_gotofund;
    ImageView icon_success;
    TextView app_title, app_subtitle;
    Animation app_splash, btt, ttb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_register);

        icon_success = findViewById(R.id.icon_succes);
        app_title = findViewById(R.id.app_title);
        app_subtitle = findViewById(R.id.app_subtitle);
        btn_explore = findViewById(R.id.btn_explore);
        btn_gotofund = findViewById(R.id.btn_gotofund);

        // Load Animation
        app_splash = AnimationUtils.loadAnimation(this, R.anim.app_splash);
        btt = AnimationUtils.loadAnimation(this, R.anim.btt);
        ttb = AnimationUtils.loadAnimation(this, R.anim.ttb);

        // Run Animation
        btn_gotofund.startAnimation(btt);
        btn_explore.startAnimation(btt);
        icon_success.startAnimation(app_splash);
        app_title.startAnimation(ttb);
        app_subtitle.startAnimation(ttb);

        btn_explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotohome = new Intent(SuccessRegisterActivity.this, HomeActivity.class);
                startActivity(gotohome);
            }
        });

        btn_gotofund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToFund = new Intent(SuccessRegisterActivity.this, AddFundActivity.class);
                startActivity(goToFund);
            }
        });
    }
}
















