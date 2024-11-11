package com.coxifred.pimpmyandroidpip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.tv.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.tv.material3.Card
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Surface
import com.coxifred.pimpmyandroidpip.theme.ui.PimpMyAndroidPIPTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        var isLoading=true
        lifecycleScope.launch {
            delay(3000L)
            isLoading=false
        }

        val splash=installSplashScreen().apply {
            setKeepOnScreenCondition{ isLoading}
        }
        super.onCreate(savedInstanceState)

        val pimpVersion="Welcome to version 1.0"
        val listen = listOf("a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","x","y","z")

        val extra=pimpVersion + listen
        setContent {
            PimpMyAndroidPIPTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = RectangleShape
                ) {
                    LazyColumn {
                        items(listen) {
                            ListItem(it)
                        }
                    }
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun ListItem(it: String)
{
    Card(
        onClick = {},
        modifier = Modifier.fillMaxSize().padding(start=10.dp,24.dp)

        ) {
            Row {
                Image(
                    painter = painterResource(id = R.drawable.baseline_person_24),
                    contentDescription = "logoPicture",
                    modifier = Modifier.width(100.dp).height(100.dp)
                )
                Text(text = it, modifier = Modifier.padding(12.dp))
            }
        }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PimpMyAndroidPIPTheme {
        Greeting("Android")
    }
}