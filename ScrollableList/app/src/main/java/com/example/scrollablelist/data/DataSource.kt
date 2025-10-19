package com.example.scrollablelist.data

import com.example.scrollablelist.model.ListItem
import com.example.scrollablelist.R

/**
 * Simple data source providing sample items and a higher-order function.
 */
object DataSource {

    /**
     * Returns a sample list of items. Replace drawable names with your actual drawables.
     */
    fun getSampleItems(): List<ListItem> {
        return listOf(
            ListItem(1, "Morning Dew", "Fresh start", R.drawable.image1),
            ListItem(2, "City Lights", "Night skyline", R.drawable.image2),
            ListItem(3, "Forest Path", "Woodland trail", R.drawable.image3),
            ListItem(4, "Mountain Peak", "High altitude", R.drawable.image4),
            ListItem(5, "Calm Lake", "Reflective water", R.drawable.image5),
            ListItem(6, "Desert Dunes", "Sandy landscape", R.drawable.image6),
            ListItem(7, "Bloom Garden", "Spring flowers", R.drawable.image7),
            ListItem(8, "Snowy Field", "Winter silence", R.drawable.image8),
            ListItem(9, "Old Town", "Historic streets", R.drawable.image9),
            ListItem(10, "Ocean Voyage", "Open sea", R.drawable.image10),
        )
    }

    /**
     * Apply the provided action to every element of the list (Higher-Order Function).
     */
    fun <T> applyToList(list: List<T>, action: (T) -> Unit) {
        list.forEach(action)
    }
}
