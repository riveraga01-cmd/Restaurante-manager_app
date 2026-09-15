package com.example

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions

class RestauranteApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initFirebaseSafely()
    }

    private fun initFirebaseSafely() {
        try {
            if (FirebaseApp.getApps(this).isEmpty()) {
                val initialized = try {
                    FirebaseApp.initializeApp(this) != null
                } catch (e: Throwable) {
                    false
                }

                if (!initialized && FirebaseApp.getApps(this).isEmpty()) {
                    try {
                        val options = FirebaseOptions.Builder()
                            .setApplicationId("1:433380736991:android:restauranteapp")
                            .setApiKey("AIzaSyFallbackKeyForRestauranteAppClient")
                            .setProjectId("dev-restaurante-app")
                            .setDatabaseUrl("https://dev-restaurante-app.firebaseio.com")
                            .setStorageBucket("dev-restaurante-app.appspot.com")
                            .setGcmSenderId("433380736991")
                            .build()
                        FirebaseApp.initializeApp(this, options)
                        Log.d("RestauranteApp", "FirebaseApp initialized with fallback options")
                    } catch (e: Throwable) {
                        Log.w("RestauranteApp", "Fallback FirebaseApp initialization failed: ${e.message}")
                    }
                } else {
                    Log.d("RestauranteApp", "FirebaseApp initialized normally")
                }
            }
        } catch (e: Throwable) {
            Log.w("RestauranteApp", "Error during Firebase initialization: ${e.message}")
        }
    }
}
