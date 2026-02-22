package com.example.productapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for the Product App.
 * Annotated with @HiltAndroidApp to enable Hilt dependency injection.
 */
@HiltAndroidApp
class ProductApplication : Application()