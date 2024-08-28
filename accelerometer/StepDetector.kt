package com.xcvi.stepcounter.service.accdetec

interface StepDetector {
    fun registerListener(stepListener: StepListener): Boolean
    fun unregisterListener()
}

