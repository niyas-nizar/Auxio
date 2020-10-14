package org.oxycblt.auxio.theme

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.view.MenuItem
import android.widget.ImageButton
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import org.oxycblt.auxio.R

// Pairs of the base accent and its theme
private val ACCENTS = listOf(
    Pair(R.color.red, R.style.Theme_Red), // 0
    Pair(R.color.pink, R.style.Theme_Pink), // 1
    Pair(R.color.purple, R.style.Theme_Purple), // 2
    Pair(R.color.deep_purple, R.style.Theme_DeepPurple), // 3
    Pair(R.color.indigo, R.style.Theme_Indigo), // 4
    Pair(R.color.blue, R.style.Theme_Blue), // 5
    Pair(R.color.light_blue, R.style.Theme_Blue), // 6
    Pair(R.color.cyan, R.style.Theme_Cyan), // 7
    Pair(R.color.teal, R.style.Theme_Teal), // 8
    Pair(R.color.green, R.style.Theme_Green), // 9
    Pair(R.color.light_green, R.style.Theme_LightGreen), // 10
    Pair(R.color.lime, R.style.Theme_Lime), // 11
    Pair(R.color.yellow, R.style.Theme_Yellow), // 12
    Pair(R.color.amber, R.style.Theme_Amber), // 13
    Pair(R.color.orange, R.style.Theme_Orange), // 14
    Pair(R.color.deep_orange, R.style.Theme_DeepOrange), // 15
    Pair(R.color.brown, R.style.Theme_Brown), // 16
    Pair(R.color.grey, R.style.Theme_Gray), // 17
    Pair(R.color.blue_grey, R.style.Theme_BlueGrey) // 18
)

val accent = ACCENTS[5]

// Get the transparent variant of a color int
@ColorInt
fun getTransparentAccent(context: Context, @ColorRes color: Int, alpha: Int): Int {
    return ColorUtils.setAlphaComponent(
        color.toColor(context),
        alpha
    )
}

// Get the inactive transparency of an accent
@ColorInt
fun getInactiveAlpha(@ColorRes color: Int): Int {
    return if (color == R.color.yellow) 100 else 150
}

// Convert an integer to a color
@ColorInt
fun Int.toColor(context: Context): Int {
    return try {
        ContextCompat.getColor(context, this)
    } catch (e: Exception) {
        // Default to the emergency color [Black] if the loading fails.
        ContextCompat.getColor(context, android.R.color.black)
    }
}

// Resolve an attribute into a color
@ColorInt
fun resolveAttr(context: Context, @AttrRes attr: Int): Int {
    // Convert the attribute into its color
    val resolvedAttr = TypedValue().apply {
        context.theme.resolveAttribute(attr, this, true)
    }

    // Then convert it to a proper color
    val color = if (resolvedAttr.resourceId != 0) {
        resolvedAttr.resourceId
    } else {
        resolvedAttr.data
    }

    return color.toColor(context)
}

// Apply a color to a Menu Item
fun MenuItem.applyColor(@ColorInt color: Int) {
    SpannableString(title).apply {
        setSpan(ForegroundColorSpan(color), 0, length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        title = this
    }
}

// Disable an ImageButton
fun ImageButton.disable(context: Context) {
    if (isEnabled) {
        imageTintList = ColorStateList.valueOf(
            R.color.inactive_color.toColor(context)
        )

        isEnabled = false
    }
}

// Enable an ImageButton
fun ImageButton.enable(context: Context) {
    if (!isEnabled) {
        imageTintList = ColorStateList.valueOf(
            R.color.control_color.toColor(context)
        )

        isEnabled = true
    }
}

// Apply a custom vertical divider
fun RecyclerView.applyDivider() {
    val div = DividerItemDecoration(
        context,
        DividerItemDecoration.VERTICAL
    )

    div.setDrawable(
        ColorDrawable(
            R.color.divider_color.toColor(context)
        )
    )

    addItemDecoration(div)
}
