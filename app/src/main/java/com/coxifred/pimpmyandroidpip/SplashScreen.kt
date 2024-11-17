package com.coxifred.pimpmyandroidpip

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.lifecycleScope
import androidx.tv.material3.Surface
import com.coxifred.pimpmyandroidpip.theme.ui.PimpMyAndroidPIPTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SplashScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)




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


