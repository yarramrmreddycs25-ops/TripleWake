package com.rmr.triplewake

import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rmr.triplewake.ui.theme.TripleWakeTheme
import java.util.Calendar

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TripleWakeTheme {
                TripleWakeApp()
            }
        }
    }
}

@Composable
fun TripleWakeApp() {

    val context = LocalContext.current

    var selectedHour by remember {
        mutableIntStateOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))
    }

    var selectedMinute by remember {
        mutableIntStateOf(Calendar.getInstance().get(Calendar.MINUTE))
    }

    var showSchedule by remember {
        mutableStateOf(false)
    }

    val backgroundColor = Color(0xFF101014)
    val cardColor = Color(0xFF202027)
    val accentColor = Color(0xFFBB86FC)

    val alarmTimes = remember(selectedHour, selectedMinute) {

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, selectedHour)
            set(Calendar.MINUTE, selectedMinute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        listOf(0, 3, 5).map { offset ->

            val alarm = calendar.clone() as Calendar

            alarm.add(Calendar.MINUTE, offset)

            String.format(
                "%02d:%02d %s",
                if (alarm.get(Calendar.HOUR) == 0) 12
                else alarm.get(Calendar.HOUR),
                alarm.get(Calendar.MINUTE),
                if (alarm.get(Calendar.AM_PM) == Calendar.AM) "AM"
                else "PM"
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(24.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(35.dp))

        Text(
            text = "TripleWake",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = accentColor
        )

        Text(
            text = "Never miss your wake-up call",
            color = Color.Gray,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(55.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = cardColor
            ),
            shape = RoundedCornerShape(20.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),

                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "SELECT ALARM TIME",
                    color = Color.LightGray,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = String.format(
                        "%02d:%02d",
                        if (selectedHour % 12 == 0) 12
                        else selectedHour % 12,
                        selectedMinute
                    ),
                    fontSize = 55.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = if (selectedHour < 12) "AM" else "PM",
                    color = accentColor,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(25.dp))

                Button(
                    onClick = {

                        TimePickerDialog(
                            context,
                            { _, hour, minute ->

                                selectedHour = hour
                                selectedMinute = minute
                                showSchedule = false

                            },
                            selectedHour,
                            selectedMinute,
                            false
                        ).show()
                    },

                    colors = ButtonDefaults.buttonColors(
                        containerColor = accentColor
                    )
                ) {

                    Text(
                        text = "Choose Time",
                        color = Color.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(35.dp))

        Text(
            text = "YOUR THREE ALARMS",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(15.dp))

        alarmTimes.forEachIndexed { index, time ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),

                colors = CardDefaults.cardColors(
                    containerColor = cardColor
                )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),

                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "Alarm ${index + 1}",
                        color = Color.LightGray,
                        fontSize = 16.sp
                    )

                    Text(
                        text = time,
                        color = accentColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(25.dp))

        Button(
            onClick = {

                showSchedule = true

                Toast.makeText(
                    context,
                    "Three alarm times calculated!",
                    Toast.LENGTH_SHORT
                ).show()
            },

            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = accentColor
            ),

            shape = RoundedCornerShape(15.dp)
        ) {

            Text(
                text = "Preview Alarm Schedule",
                color = Color.Black,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (showSchedule) {

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = "Your three alarm times are ready!",
                color = Color.Green,
                fontSize = 14.sp
            )
        }
    }
}