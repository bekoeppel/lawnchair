package app.lawnchair.gestures

import androidx.test.filters.SmallTest
import com.android.launcher3.util.LauncherMultivalentJUnit
import kotlin.math.absoluteValue
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import com.android.launcher3.Utilities

@SmallTest
@RunWith(LauncherMultivalentJUnit::class)
class VerticalSwipeTouchControllerTest {

    private class TestDetector(private val threshold: Int, private val velocityThreshold: Float) {
        private var currentMillis = 0L
        private var currentVelocity = 0f
        private var currentDisplacement = 0f
        var triggered = false
            private set

        fun onDrag(displacement: Float, time: Long) {
            val delta = displacement - currentDisplacement
            currentDisplacement = displacement
            val velocity = computeVelocity(delta, time)
            if (!triggered &&
                displacement.absoluteValue > threshold &&
                velocity.absoluteValue > velocityThreshold
            ) {
                triggered = true
            }
        }

        private fun computeVelocity(delta: Float, millis: Long): Float {
            val previousMillis = currentMillis
            currentMillis = millis
            val deltaTimeMillis = (currentMillis - previousMillis).toFloat()
            val velocity = if (deltaTimeMillis > 0) delta / deltaTimeMillis else 0f
            currentVelocity = if (currentVelocity.absoluteValue < 0.001f) {
                velocity
            } else {
                val alpha = deltaTimeMillis / (SCROLL_VELOCITY_DAMPENING_RC + deltaTimeMillis)
                Utilities.mapRange(alpha, currentVelocity, velocity)
            }
            return currentVelocity
        }
    }

    @Test
    fun gestureTriggersAfterThresholdExceeded() {
        val detector = TestDetector(150, DEFAULT_VELOCITY_THRESHOLD)
        detector.onDrag(-50f, 16)
        assertTrue(!detector.triggered)
        detector.onDrag(-160f, 32)
        assertTrue(detector.triggered)
    }

    @Test
    fun gestureDoesNotTriggerWhenVelocityBelowThreshold() {
        val detector = TestDetector(100, 3f)
        detector.onDrag(-60f, 50) // velocity < 3
        detector.onDrag(-160f, 100) // still below velocity threshold
        assertTrue(!detector.triggered)
        detector.onDrag(-400f, 116) // high velocity now
        assertTrue(detector.triggered)
    }

    companion object {
        private const val SCROLL_VELOCITY_DAMPENING_RC = 1000f / (2f * Math.PI.toFloat() * 10f)
        private const val DEFAULT_VELOCITY_THRESHOLD = 2.25f
    }
}
