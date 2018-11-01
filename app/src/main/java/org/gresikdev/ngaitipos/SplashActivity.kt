package org.gresikdev.ngaitipos

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_coba.*

public class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coba)

        var millisInFuture = 2000L
        val countDownInterval: Long = 1000
        val handler = Handler()
        val counter = object : Runnable {
            override fun run() {
                var sec: Long = 0
                if (millisInFuture > 0) {
                    sec = millisInFuture / 1000
                    millisInFuture -= countDownInterval
                    handler.postDelayed(this, countDownInterval)
                }
                Log.i("tes", "tes $sec")
                if (sec == 1L) {
                    tv_splash_gresikdev?.gone()
                    ly_splash_ngaitipos?.visible()
                } else if (sec == 0L) {
                    val sharedPreferences = getSharedPreferences("NgaitiPOS", 0)
                    if (sharedPreferences.getInt("firstview", 0) == 0) {
                        keChoose()
                    } else {
                        keMain()
                    }
                }
            }
        }

        handler.postDelayed(counter, countDownInterval)
    }

    private fun keChoose() {
        startActivity(Intent(this, ChooseActivity::class.java))
        finish()
    }

    private fun keMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onBackPressed() {
    }
}