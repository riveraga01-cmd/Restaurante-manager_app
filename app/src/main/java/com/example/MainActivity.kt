package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.example.ui.screens.*
import com.example.ui.theme.RestauranteTheme
import com.example.ui.viewmodel.MainRole
import com.example.ui.viewmodel.RestaurantViewModel
import com.example.util.NotificationHelper

class MainActivity : ComponentActivity() {

    private val viewModel: RestaurantViewModel by viewModels()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            NotificationHelper.subscribeToKitchenTopic(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Setup notification channel & subscribe to kitchen topic
        NotificationHelper.createNotificationChannel(this)
        NotificationHelper.subscribeToKitchenTopic(this)

        // Request notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        // Handle target screen from notification
        intent?.let {
            if (it.getStringExtra("target_screen") == "COCINA") {
                viewModel.navigateToRole(MainRole.COCINA)
            }
        }

        setContent {
            val systemSettings by viewModel.systemSettings.collectAsState()
            val isDarkTheme by viewModel.isDarkTheme.collectAsState()
            val effectiveDarkMode = systemSettings.isDarkMode || isDarkTheme
            val paletteCode = systemSettings.themePalette.ifEmpty { "AZUL_RIVERA" }

            RestauranteTheme(
                darkTheme = effectiveDarkMode,
                paletteCode = paletteCode
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RestaurantAppMain(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun RestaurantAppMain(viewModel: RestaurantViewModel) {
    val currentRole by viewModel.currentRole.collectAsState()
    val showPinPrompt by viewModel.showPinPrompt.collectAsState()
    val pinError by viewModel.pinError.collectAsState()

    // Handle back button / gesture: if PIN prompt is open, close it; if in a sub-module, return to INICIO
    BackHandler(enabled = showPinPrompt || currentRole != MainRole.INICIO) {
        if (showPinPrompt) {
            viewModel.dismissPinPrompt()
        } else if (currentRole != MainRole.INICIO) {
            viewModel.navigateBackToInicio()
        }
    }

    when (currentRole) {
        MainRole.INICIO -> {
            InicioScreen(viewModel = viewModel)
        }
        MainRole.MESERO -> {
            MeseroScreen(
                viewModel = viewModel,
                onBackToInicio = { viewModel.navigateBackToInicio() }
            )
        }
        MainRole.COCINA -> {
            CocinaScreen(
                viewModel = viewModel,
                onBackToInicio = { viewModel.navigateBackToInicio() }
            )
        }
        MainRole.CAJA -> {
            CajaScreen(
                viewModel = viewModel,
                onBackToInicio = { viewModel.navigateBackToInicio() }
            )
        }
        MainRole.GERENTE -> {
            GerenteScreen(
                viewModel = viewModel,
                onBackToInicio = { viewModel.navigateBackToInicio() }
            )
        }
        MainRole.REPARTIDOR -> {
            RepartidorScreen(
                viewModel = viewModel,
                onBackToInicio = { viewModel.navigateBackToInicio() }
            )
        }
    }

    if (showPinPrompt) {
        PinDialog(
            showError = pinError,
            onPinSubmitted = { pin -> viewModel.verifyManagerPin(pin) },
            onDismiss = { viewModel.dismissPinPrompt() }
        )
    }
}

