package com.vcco.weather.di.application

import android.app.Application
import dagger.hilt.android.testing.CustomTestApplication

@CustomTestApplication(Application::class)
interface MyTestApplication
