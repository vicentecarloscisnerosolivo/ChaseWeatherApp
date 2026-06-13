package com.vcco.weather.di

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.vcco.weather.di.application.MyTestApplication

class HiltTestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application? {
        return super.newApplication(
            cl,
            "com.vcco.weather.di.application.MyTestApplication_Application",
            context
        )
    }
}
