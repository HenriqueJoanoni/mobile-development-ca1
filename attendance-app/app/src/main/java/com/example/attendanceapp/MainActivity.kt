package com.example.attendanceapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.attendanceapp.ui.theme.AttendanceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            AttendanceTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    AttendanceLayout()
                }
            }
        }
    }
}

@Composable
fun AttendanceLayout() {
    var daysPresentInput by remember { mutableStateOf("") }
    var daysAbsentInput by remember { mutableStateOf("") }
    var excusedInput by remember { mutableStateOf("") }
    var showAsPercentage by remember { mutableStateOf(true) }
    var subtractExcused by remember { mutableStateOf(false) }

    val daysPresent = daysPresentInput.toIntOrNull() ?: 0
    val daysAbsent = daysAbsentInput.toIntOrNull() ?: 0
    val excused = excusedInput.toIntOrNull() ?: 0
    val effectiveAbsent = if (subtractExcused) (daysAbsent - excused).coerceAtLeast(0) else daysAbsent

    val attendancePercent = calculateAttendance(daysPresent, effectiveAbsent)
    val total = daysPresent + effectiveAbsent
    val isLow = total > 0 && attendancePercent < 75.0

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        /** Header with Box for overlaying the badge */
        Box(modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)) {
            Image(
                painter = painterResource(id = R.drawable.scholarship),
                contentDescription = null,
                modifier = Modifier.size(72.dp)
            )
            /** Custom badge at the top-right corner */
            if (daysAbsent > 0) {
                BadgeCount(
                    count = daysAbsent,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 2.dp, end = 2.dp)
                )
            }
        }
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.calculate_presence),
            modifier = Modifier
                .padding(bottom = 8.dp, top = 16.dp)
                .fillMaxWidth()
        )

        /** Row: side-by-side fields for present classes and absences */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            EditNumberField(
                label = R.string.calculate_presence,
                leadingIcon = R.drawable.money,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                value = daysPresentInput,
                onValueChanged = { daysPresentInput = it },
                modifier = Modifier
                    .weight(1f)
            )
            EditNumberField(
                label = R.string.days_absent,
                leadingIcon = R.drawable.money,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                value = daysAbsentInput,
                onValueChanged = { daysAbsentInput = it },
                modifier = Modifier
                    .weight(1f)
            )
        }

        /** Checkbox to subtract excused absences and field for excused absences */
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = subtractExcused,
                    onCheckedChange = { subtractExcused = it }
                )
                Text(
                    text = "Subtract justified absences",
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            if (subtractExcused) {
                EditNumberField(
                    label = R.string.days_absent,
                    leadingIcon = R.drawable.money,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    value = excusedInput,
                    onValueChanged = { excusedInput = it },
                    modifier = Modifier
                        .width(300.dp)
                        .padding(start = 8.dp)
                )
            }
        }

        /** Switch to toggle between percentage and fraction */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Display as percentage", modifier = Modifier.weight(1f))
            Switch(
                checked = showAsPercentage,
                onCheckedChange = { showAsPercentage = it }
            )
        }

        /** Result: percentage or fraction and visual indicator when attendance is low */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (showAsPercentage) {
                Text(
                    text = stringResource(
                        R.string.attendance_amount,
                        "%.2f%%".format(attendancePercent)
                    ),
                    style = MaterialTheme.typography.displaySmall
                )
            } else {
                Text(
                    text = if (total > 0) "$daysPresent / $total classes" else "No Data",
                    style = MaterialTheme.typography.displaySmall
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            /** Badge indicating low attendance */
            if (isLow) {
                Box(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFB00020))
                        .size(36.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "!", color = Color.White, fontSize = 20.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(150.dp))
    }
}

@Composable
fun BadgeCount(count: Int, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(22.dp)
            .clip(CircleShape)
            .background(Color(0xFFCF3C3C)),
        contentAlignment = Alignment.Center
    ) {
        Text(text = count.toString(), color = Color.White, fontSize = 12.sp)
    }
}

@Composable
fun EditNumberField(
    @StringRes label: Int,
    @DrawableRes leadingIcon: Int,
    keyboardOptions: KeyboardOptions,
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        singleLine = true,
        leadingIcon = { Icon(painter = painterResource(id = leadingIcon), contentDescription = null) },
        modifier = modifier,
        onValueChange = onValueChanged,
        label = { Text(stringResource(label)) },
        keyboardOptions = keyboardOptions
    )
}

private fun calculateAttendance(present: Int, absent: Int): Double {
    val total = present + absent
    return if (total > 0) (present.toDouble() / total) * 100 else 0.0
}

@Preview(showBackground = true)
@Composable
fun AttendanceLayoutPreview() {
    AttendanceTheme {
        AttendanceLayout()
    }
}
