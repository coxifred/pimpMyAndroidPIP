package com.coxifred.pimpmyandroidpip

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.Icon
import androidx.tv.material3.Surface
import com.coxifred.pimpmyandroidpip.ui.theme.PimpMyAndroidPIPTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Menu : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val context: Context = this
        val activity: Activity = this
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PimpMyAndroidPIPTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding),
                        context=context,
                        activity=activity
                    )
                }
            }
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier,context: Context,activity: Activity) {
    val scope = rememberCoroutineScope()
    var statusText by remember { mutableStateOf("No state known") }
    Surface(
        modifier = Modifier.fillMaxSize().background(color= Color.DarkGray)



    )
    {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            //verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize().background(color= Color.DarkGray)
        )
        {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth().padding(start = 10.dp, 24.dp)

            )
            {
                Text(
                    statusText,
                    color = Color.Black
                )
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth().padding(start = 10.dp, 24.dp)

            )
            {
                Button(
                    onClick = {
                                HttpServerManager.startServer()
                                // Wait 1s to let start the server in the background
                                scope.launch {
                                    delay(10000)
                                    statusText=HttpServerManager.status(context,activity,0)
                                    println("statusText=" + statusText)
                                    println("statusServer=" + HttpServerManager.status(context,activity,0))
                                }

                              },
                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                    modifier = Modifier.padding(24.dp),
                    colors = ButtonDefaults.colors(
                        containerColor = Color.Green
                    ),
                ) {
                    Icon(
                        Icons.Filled.PlayArrow,
                        contentDescription = "Localized description",
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Start http server", color = Color.Black)

                }
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth().padding(start = 10.dp, 24.dp)

            )
            {
                Button(
                    onClick = {
                                HttpServerManager.stopServer()
                                // Wait 1s to let stop the server in the background
                                scope.launch {
                                    delay(10000)
                                    statusText=HttpServerManager.status(context,activity,0)
                                    println("statusText=" + statusText)
                                    println("statusServer=" + HttpServerManager.status(context,activity,0))
                                }
                              },
                    colors = ButtonDefaults.colors(
                        containerColor = Color.Red
                    ),
                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Icon(
                        Icons.Filled.Close,

                        contentDescription = "Localized description",
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Stop http server", color = Color.White)

                }
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth().padding(start = 10.dp, 24.dp)

            )
            {
                Button(
                    onClick = {


                    },
                    colors = ButtonDefaults.colors(
                        containerColor = Color.Red
                    ),
                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Icon(
                        Icons.Filled.Close,

                        contentDescription = "Localized description",
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Test popup", color = Color.White)

                }
            }
        }
    }
}

/*
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PimpMyAndroidPIPTheme {
        Greeting("Android")
    }
}
*/


@Composable
fun MinimalDialog(onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(
                text = "This is a minimal dialog",
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center),
                textAlign = TextAlign.Center,
            )
        }
    }
}