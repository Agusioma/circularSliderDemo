package com.tcreatesllc.circularsliderdemo

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tcreatesllc.circularsliderdemo.ui.theme.CircularSliderDemoTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()

        //Hide the status bars

        WindowCompat.setDecorFitsSystemWindows(window, true)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else {
            window.insetsController?.apply {
                hide(WindowInsets.Type.systemBars())
                systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
        setContent {
            CircularSliderDemoTheme {
                // A surface container using the 'background' color from the theme

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    sliderBox(viewModel())
                }

            }
        }
    }
}

val appFontFamily = FontFamily(
    Font(R.font.montserrat_regular, FontWeight.Normal),
    Font(R.font.montserrat_bold, FontWeight.Bold)
)

@Composable
fun circularBox() {
    Box(
        Modifier
            .wrapContentSize()
            .clip(CircleShape)
            .background(Color(0xFF343144))

            .padding(30.dp),
    ) {
        middleColumn(Modifier.align(Alignment.Center), viewModel())
    }
}


@Composable
fun middleColumn(mods: Modifier, mainViewModel: MainViewModel) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = mods.padding(15.dp)
    ) {
        Row() {
            var tempValue = mainViewModel.tempValue.observeAsState("16.00")
            Text(
                text = tempValue.value,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                fontFamily = appFontFamily,
                color = Color.White,
                modifier = Modifier
                    .padding(bottom = 12.5.dp)
                    .align(Alignment.CenterVertically)
                //color = Color.White,
            )
            Text(
                text = "°C",
                fontSize = 10.sp,
                fontFamily = appFontFamily,
                fontWeight = FontWeight.Normal,
                color = Color.White,
                modifier = Modifier
                    .padding(bottom = 15.dp)
                    .align(Alignment.Top)
                //color = Color.White,
            )
        }
        Text(
            text = "Room",
            color = Color.LightGray,
            fontSize = 11.sp,
            fontFamily = appFontFamily,
            textAlign = TextAlign.Center
            //color = Color.White,
        )
        Text(
            text = "Temperature",
            color = Color.LightGray,
            fontSize = 11.sp,
            fontFamily = appFontFamily,
            textAlign = TextAlign.Center
            //color = Color.White,
        )
    }
}


@Composable
fun sliderBox(mainViewModel: MainViewModel) {
    Box(
        modifier = Modifier
            .wrapContentSize()
            .padding(25.dp)

        ,
        contentAlignment = Alignment.Center
    ) {

        CircularSlider(
            modifier = Modifier.fillMaxSize().padding(25.dp),
        ) {
            Log.d("Original VAL", "${it * 180}")
            var sliderPercentage = it
            var currTempValue = 16 + (sliderPercentage * 16)
            var roundedTempValue = String.format("%.0f", currTempValue)
            mainViewModel.tempValue.value = roundedTempValue
            Log.d("progress", "$roundedTempValue°C")
        }
        circularBox()
        Text(
            text = "16°",
            fontSize = 14.sp,
            fontFamily = appFontFamily,
            modifier = Modifier
                .align(Alignment.CenterStart)
            //color = Color.White,
        )
        Text(
            text = "32°",
            fontSize = 14.sp,
            fontFamily = appFontFamily,
            modifier = Modifier
                .align(Alignment.CenterEnd)
            //color = Color.White,
        )
    }
}