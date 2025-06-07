package app.lawnchair.gestures

import android.content.Context
import android.util.Log
import androidx.lifecycle.lifecycleScope
import app.lawnchair.gestures.config.GestureHandlerConfig
import app.lawnchair.gestures.type.GestureType
import app.lawnchair.launcher
import app.lawnchair.preferences2.PreferenceManager2
import com.patrykmichalik.opto.core.firstBlocking
import app.lawnchair.preferences2.subscribeBlocking
import com.android.launcher3.model.data.ItemInfo
import com.android.launcher3.util.VibratorWrapper
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class IconGestureListener(
    private val context: Context,
    private val prefs: PreferenceManager2,
    private val cmp: ItemInfo?,
) : DirectionalGestureListener(
    context,
    prefs.verticalSwipeThreshold.firstBlocking(),
    prefs.horizontalSwipeThreshold.firstBlocking(),
) {

    init {
        prefs.verticalSwipeThreshold.subscribeBlocking(context.launcher.lifecycleScope) {
            verticalSwipeThreshold = it
        }
        prefs.horizontalSwipeThreshold.subscribeBlocking(context.launcher.lifecycleScope) {
            horizontalSwipeThreshold = it
        }
    }

    override fun onSwipeRight() = handleGesture(GestureType.SWIPE_RIGHT)
    override fun onSwipeLeft() = handleGesture(GestureType.SWIPE_LEFT)
    override fun onSwipeTop() = handleGesture(GestureType.SWIPE_UP)
    override fun onSwipeDown() = handleGesture(GestureType.SWIPE_DOWN)

    private fun handleGesture(gestureType: GestureType) {
        Log.d("GESTURE_HANDLER", "Handling gesture: ${gestureType.name}")

        cmp?.componentKey?.let {
            context.launcher.lifecycleScope.launch {
                val gesture = prefs.getGestureForApp(it, gestureType).firstOrNull()
                if (gesture !is GestureHandlerConfig.NoOp) {
                    Log.d("GESTURE_HANDLER", "Triggering gesture: ${gestureType.name}")
                    VibratorWrapper.INSTANCE.get(context.launcher).vibrate(VibratorWrapper.OVERVIEW_HAPTIC)
                    gesture?.createHandler(context)?.onTrigger(context.launcher)
                } else {
                    Log.d("GESTURE_HANDLER", "NoOp gesture, ignoring")
                }
            }
        }
    }
}
