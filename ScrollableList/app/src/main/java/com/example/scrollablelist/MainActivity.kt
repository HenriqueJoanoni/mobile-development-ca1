package com.example.scrollablelist

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.scrollablelist.data.DataSource
import com.example.scrollablelist.model.ListItem
import com.example.scrollablelist.ui.theme.ScrollableListTheme

/**
 * Small state holder for each card using mutableStateOf for reactive fields.
 */
class CardState(expanded: Boolean = false) {
    var expanded by mutableStateOf(expanded)
    var liked by mutableStateOf(false)
    var clicks by mutableStateOf(0)
}

class MainActivity : ComponentActivity() {

    private val TAG = "ScrollableList"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Log.d(TAG, "onCreate: MainActivity started")

        setContent {
            ScrollableListTheme {
                val items = DataSource.getSampleItems()

                /** Use a map of id -> CardState to hoist state for each card. */
                val cardStates = remember { mutableStateMapOf<Int, CardState>() }

                /** Demonstrating HOF use: log each item */
                DataSource.applyToList(items) { item ->
                    Log.d(TAG, "DataSource item: ${item.id} - ${item.title}")
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items, key = { it.id }) { item ->
                        val state = cardStates.getOrPut(item.id) { CardState() }

                        ExpandableCard(
                            item = item,
                            expanded = state.expanded,
                            liked = state.liked,
                            clicks = state.clicks,
                            onExpandToggle = {
                                state.expanded = !state.expanded
                                Log.d(TAG, "Toggled expand for item ${item.id}: ${state.expanded}")
                            },
                            onAction = {
                                state.liked = !state.liked
                                state.clicks += 1
                                Log.d(
                                    TAG,
                                    "Action on item ${item.id}: liked=${state.liked}, clicks=${state.clicks}"
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

/**
 * Expandable card composable. Visual change when liked (background color and button text).
 */
@Composable
fun ExpandableCard(
    item: ListItem,
    expanded: Boolean,
    liked: Boolean,
    clicks: Int,
    onExpandToggle: () -> Unit,
    onAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    val background = if (liked) Color(0xFFE1F5FE) else MaterialTheme.colorScheme.surface

    Card(
        modifier = modifier
            .clickable { onExpandToggle() },
        colors = CardDefaults.cardColors(containerColor = background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = item.imageRes),
                    contentDescription = item.title,
                    modifier = Modifier
                        .height(80.dp)
                        .padding(4.dp),
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                ) {
                    Text(text = item.title, style = MaterialTheme.typography.titleMedium)
                    if (expanded) {
                        Text(text = item.subtitle, style = MaterialTheme.typography.bodyMedium)
                    }
                }
                Button(onClick = onAction) {
                    val label = if (liked) "Liked ($clicks)" else "Like ($clicks)"
                    Text(text = label)
                }
            }
        }
    }
}
