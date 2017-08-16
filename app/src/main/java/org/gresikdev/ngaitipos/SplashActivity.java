package org.gresikdev.ngaitipos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_splash);
        setContentView(R.layout.activity_coba);

        final TextView gresikdev = (TextView) findViewById(R.id.tv_splash_gresikdev);
        final LinearLayout ngaitipos = (LinearLayout) findViewById(R.id.ly_splash_ngaitipos);

        final long[] millisInFuture = {2000};
        final long countDownInterval = 1000;
        final Handler handler = new Handler();
        final Runnable counter = new Runnable(){

            public void run(){
                long sec = 0;
                if(millisInFuture[0] > 0) {
                    sec = millisInFuture[0] / 1000;
                    millisInFuture[0] -= countDownInterval;
                    handler.postDelayed(this, countDownInterval);
                }
                Log.i("tes", "tes "+sec);
                if (sec == 1){
                    gresikdev.setVisibility(View.GONE);
                    ngaitipos.setVisibility(View.VISIBLE);
                } else if (sec == 0){
                    SharedPreferences sharedPreferences = getSharedPreferences("NgaitiPOS", 0);
                    if (sharedPreferences.getInt("firstview", 0) == 0){
                        keChoose();
                    } else {
                        keMain();
                    }
                }
            }
        };

        handler.postDelayed(counter, countDownInterval);
    }

    private void keChoose(){
        startActivity(new Intent(this, ChooseActivity.class));
        finish();
    }

    private void keMain(){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
