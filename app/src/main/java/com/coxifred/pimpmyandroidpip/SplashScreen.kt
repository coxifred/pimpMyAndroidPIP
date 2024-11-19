package com.coxifred.pimpmyandroidpip

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.core.app.ActivityCompat
import androidx.tv.material3.Surface
import com.coxifred.pimpmyandroidpip.theme.ui.PimpMyAndroidPIPTheme

class SplashScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                // ask for permission
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                ActivityCompat.startActivityForResult(this,intent,1,null)
            }
        }

        Handler().postDelayed({
            val svc = Intent(
                this,
                MainService::class.java
            )
            stopService(svc)
            startService(svc)
            finish()
        }, 1000)

        setContent {
            PimpMyAndroidPIPTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = RectangleShape
                ) {
                    Image(
                        painter = painterResource(R.drawable.splashlogo),
                        contentDescription = null
                    )
                }
            }
        }
        println("Load next Activity")
        Handler().postDelayed({
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
        }, 1000)




    }
}


