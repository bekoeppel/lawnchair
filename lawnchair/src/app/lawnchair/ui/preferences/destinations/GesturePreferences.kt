package app.lawnchair.ui.preferences.destinations

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import app.lawnchair.preferences.getAdapter
import app.lawnchair.preferences2.preferenceManager2
import app.lawnchair.ui.preferences.LocalIsExpandedScreen
import app.lawnchair.ui.preferences.components.GestureHandlerPreference
import app.lawnchair.ui.preferences.components.controls.SliderPreference
import app.lawnchair.ui.preferences.components.layout.PreferenceGroup
import app.lawnchair.ui.preferences.components.layout.PreferenceLayout
import com.android.launcher3.R

@Composable
fun GesturePreferences(
    modifier: Modifier = Modifier,
) {
    val prefs = preferenceManager2()
    PreferenceLayout(
        label = stringResource(id = R.string.gestures_label),
        backArrowVisible = !LocalIsExpandedScreen.current,
        modifier = modifier,
    ) {
        PreferenceGroup {
            GestureHandlerPreference(
                adapter = prefs.doubleTapGestureHandler.getAdapter(),
                label = stringResource(id = R.string.gesture_double_tap),
            )
            GestureHandlerPreference(
                adapter = prefs.swipeUpGestureHandler.getAdapter(),
                label = stringResource(id = R.string.gesture_swipe_up),
            )
            GestureHandlerPreference(
                adapter = prefs.swipeDownGestureHandler.getAdapter(),
                label = stringResource(id = R.string.gesture_swipe_down),
            )
            GestureHandlerPreference(
                adapter = prefs.homePressGestureHandler.getAdapter(),
                label = stringResource(id = R.string.gesture_home_tap),
            )
            GestureHandlerPreference(
                adapter = prefs.backPressGestureHandler.getAdapter(),
                label = stringResource(id = R.string.gesture_back_tap),
            )
            SliderPreference(
                label = stringResource(id = R.string.pref_home_swipe_vertical_distance),
                adapter = prefs.homeSwipeVerticalMinDistance.getAdapter(),
                step = 1,
                valueRange = 0..200,
                showUnit = "px",
            )
            SliderPreference(
                label = stringResource(id = R.string.pref_home_swipe_horizontal_distance),
                adapter = prefs.homeSwipeHorizontalMinDistance.getAdapter(),
                step = 1,
                valueRange = 0..200,
                showUnit = "px",
            )
            SliderPreference(
                label = stringResource(id = R.string.pref_home_swipe_velocity),
                adapter = prefs.homeSwipeTriggerVelocity.getAdapter(),
                step = 0.05f,
                valueRange = 0f..5f,
                showUnit = "px/ms",
            )
        }
    }
}
