package com.example.benchmark

import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FrameTimingBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun goToPlantList() = benchmarkRule.measureRepeated(
        packageName = "com.example.notweshare",
        metrics = listOf(FrameTimingMetric()),
        iterations = 5,
        startupMode = StartupMode.COLD,
        setupBlock = {
            // start the default activity, but don't measure the frames yet
            pressHome()
            startActivityAndWait()
        }
    ) {
        // find the tab with plants list
        val registerTab = device.findObject(By.descContains("register"))
        registerTab.click()
        // wait until window changes finish
        device.waitForIdle()
    }
}