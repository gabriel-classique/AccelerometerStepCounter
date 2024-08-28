package com.xcvi.stepcounter.service.accdetec

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.xcvi.stepcounter.service.SensorService

class StepDetectorSensor(
    private val context: Context,
) : StepDetector, SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
    private var stepListener: StepListener? = null

    override fun registerListener(stepListener: StepListener): Boolean {
        this.stepListener = stepListener

        if (!SensorService.hasPermissions(context)) {
            return false
        }

        val stepDetectorSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
        if (stepDetectorSensor != null) {
            return sensorManager?.registerListener(
                /* listener = */ this@StepDetectorSensor,
                /* sensor = */ stepDetectorSensor,
                /* samplingPeriodUs = */ SensorManager.SENSOR_DELAY_FASTEST,
            ) ?: false
        }
        return false
    }

    override fun unregisterListener() {
        stepListener = null
        sensorManager?.unregisterListener(this@StepDetectorSensor)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_DETECTOR) {
            stepListener?.onStep(count = event.values.size)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
}

