package com.example.teht3compstart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import androidx.core.text.isDigitsOnly
import com.example.teht3compstart.ui.theme.Teht3CompStartTheme
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import android.content.SharedPreferences as SharedPreferences

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pref = getDefaultSharedPreferences(this)

        setContent {
            Teht3CompStartTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android", pref)  // TODO: välitä asetukset parametreinä
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun Greeting(name: String, sharedPreferences: SharedPreferences?, modifier: Modifier = Modifier) {
    val materialBlue700 = Color(0xFF1976D2)
// state of the menu
    var expanded by remember { mutableStateOf(false) }
    //TODO: lisää muuttujia
    var screen by remember { mutableStateOf(0) }   // 0 = pääsivu
    var ctx = LocalContext.current.applicationContext  // jos tarvitaan
    val localFocusManager = LocalFocusManager.current
    var lahtoTaksa = sharedPreferences?.getFloat("lähtötaksa", 0f)
    var kmTaksa = sharedPreferences?.getFloat("kilometritaksa", 0f)

    var taksa by remember { mutableStateOf("Taksa") }
    var textLahtoTaksa by remember { mutableStateOf("${lahtoTaksa}") }
    var textKmTaksa by remember { mutableStateOf("${kmTaksa}") }
    var textKm by remember { mutableStateOf("0") }

    var kmTaksaVal by remember { mutableStateOf(0f)}
    var lahtoTaksaVal by remember { mutableStateOf(0f)}



    Scaffold(
        /*Yläpalkin asetukset*/
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text("Taksamittari  ") },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = materialBlue700),
                navigationIcon = {
                    IconButton(onClick = { if (screen == 1) screen = 0 }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Takaisin")
                    }
                },
                actions = {
                    // 3 vertical dots icon
                    IconButton(onClick = { expanded = true }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Asetukset")
                    }
                    // drop down menu
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(text = { Text("Asetukset") }, onClick = {
                            expanded = false
                            screen = 1

                        })
                    }
                }
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            if (screen == 0) { // Only show FloatingActionButton when screen is 0
                FloatingActionButton(onClick = {
                    kmTaksaVal = textKmTaksa.toFloatOrNull()!!
                    lahtoTaksaVal = textLahtoTaksa.toFloatOrNull()!!
                    val kmVal = textKm.toFloatOrNull()!!
                    taksa = "${(kmTaksaVal * kmVal) + lahtoTaksaVal}"
                }) {
                    Text("+")
                }
            }
        },

        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .consumeWindowInsets(innerPadding)
                    .padding(innerPadding)
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            localFocusManager.clearFocus()
                        })
                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                when (screen) {
                    0 -> {


                        OutlinedTextField(
                            value = textKm,
                            onValueChange = { textKm = it },
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done // Set imeAction to Done
                            ),
                            label = {
                                Text(
                                    text = "Kilometrit",
                                    style = TextStyle(fontSize = 20.sp),
                                    modifier = Modifier
                                )
                            },
                            textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 30.sp),
                        )

                        Text(
                            text = taksa,
                            modifier = Modifier.padding(10.dp),
                            style = TextStyle(fontSize = 40.sp, color = Color.Red)
                        )
                    }
                }
                when (screen) {
                    1 -> {
                        OutlinedTextField(
                            modifier = Modifier.padding(10.dp),
                            value = textLahtoTaksa,
                            onValueChange = { textLahtoTaksa = it },
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done // Set imeAction to Done
                            ),
                            label = {
                                Text(
                                    text = "Lähtötaksa (€)",
                                    style = TextStyle(fontSize = 20.sp)
                                )
                            },
                            textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 30.sp),


                        )

                        OutlinedTextField(
                            modifier = Modifier.padding(10.dp),
                            value = textKmTaksa,
                            onValueChange = { textKmTaksa = it },
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done // Set imeAction to Done
                            ),
                            label = {
                                Text(
                                    text = "Kilometritaksa (€)",
                                    style = TextStyle(fontSize = 20.sp)
                                )
                            },
                            textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 30.sp),
                        )

                        Button(modifier = Modifier.padding(10.dp), onClick = { screen = 0
                            if (textKmTaksa.isNotEmpty() && textLahtoTaksa.isNotEmpty()){
                                kmTaksaVal = textKmTaksa.toFloatOrNull()!!
                                lahtoTaksaVal = textLahtoTaksa.toFloatOrNull()!!

                                if (kmTaksaVal != null && lahtoTaksaVal != null) {
                                    sharedPreferences?.edit {
                                        putFloat("lähtötaksa", lahtoTaksaVal)
                                        putFloat("kilometritaksa", kmTaksaVal)

                                    }
                                }
                            }
                        }) {
                            Text(text = "Tallenna", style = TextStyle(fontSize = 20.sp))
                        }
                    }
                }
            }
        },
        //bottomBar = { BottomAppBar(Modifier.background(materialBlue700) ) { Text("BottomAppBar") } }
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Teht3CompStartTheme {
        Greeting("Android", sharedPreferences = null)  //TODO: mahdolliset parametrit tännekin, jos tarpeen
    }
}