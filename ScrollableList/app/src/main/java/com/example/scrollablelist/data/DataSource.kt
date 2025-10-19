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
            ListItem(1, "Title 1", "Subtitle 1", R.drawable.image1),
            ListItem(2, "Title 2", "Subtitle 2", R.drawable.image2),
            ListItem(3, "Title 3", "Subtitle 3", R.drawable.image3),
            ListItem(4, "Title 4", "Subtitle 4", R.drawable.image4),
            ListItem(5, "Title 5", "Subtitle 5", R.drawable.image5),
            ListItem(6, "Title 6", "Subtitle 6", R.drawable.image6),
            ListItem(7, "Title 7", "Subtitle 7", R.drawable.image7),
            ListItem(8, "Title 8", "Subtitle 8", R.drawable.image8),
            ListItem(9, "Title 9", "Subtitle 9", R.drawable.image9),
            ListItem(10, "Title 10", "Subtitle 10", R.drawable.image10),
        )
    }

    /**
     * Apply the provided action to every element of the list (Higher-Order Function).
     */
    fun <T> applyToList(list: List<T>, action: (T) -> Unit) {
        list.forEach(action)
    }
}
