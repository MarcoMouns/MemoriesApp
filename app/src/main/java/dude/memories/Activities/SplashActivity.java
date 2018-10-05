package dude.memories.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import dude.memories.R;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.splash_icon)
    TextView splash_icon;
    SharedPreferences prefs;

    Animation animS;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        animS = AnimationUtils.loadAnimation(this, R.anim.scale_splashscreen_anim);
        splash_icon.setAnimation(animS);
        prefs = getSharedPreferences("sh", MODE_PRIVATE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (prefs.contains("id")) {
                    startActivity(new Intent(SplashActivity.this, MapsActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    finish();
                }
            }
        }, 2000);

    }
}
