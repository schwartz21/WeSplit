package com.example.benchmark

import android.content.Intent
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.uiautomator.By
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class TextInputFrameTimingBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun inputText() {
        benchmarkRule.measureRepeated(
            packageName = "com.example.notweshare",
            metrics = listOf(FrameTimingMetric()),
            // Try switching to different compilation modes to see the effect
            // it has on frame timing metrics.
            compilationMode = CompilationMode.None(),
            startupMode = StartupMode.WARM, // restarts activity each iteration
            iterations = 5,
            setupBlock = {
                // Before starting to measure, navigate to the UI to be measured
                val intent = Intent("$packageName.screens.LoginScreen")
                startActivityAndWait(intent)
            }
        ) {
            val input = device.findObject(By.res("input"))
            repeat(3) {
                input.text = ""
                input.text = "Benchmark input $it"
            }
        }
    }
}