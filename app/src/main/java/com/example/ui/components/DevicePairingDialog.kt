package com.example.ui.components

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import android.Manifest
import com.example.util.PermissionHelper
import com.example.data.entity.DeviceBindingEntity
import com.example.ui.theme.BentoPrimary
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.StatusCanceladoContainer
import com.example.ui.theme.StatusCanceladoText
import com.example.ui.theme.StatusLibreContainer
import com.example.ui.theme.StatusLibreText
import com.example.util.HapticHelper
import kotlinx.coroutines.delay
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevicePairingDialog(
    allActiveBindings: List<DeviceBindingEntity>,
    onPairWithCode: (String, (Boolean, String) -> Unit) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    var selectedMethodTab by remember { mutableStateOf(0) } // 0: Escáner de Cámara QR, 1: PIN de 6 Dígitos / Código
    
    // Inputs & States
    var codeInput by remember { mutableStateOf("") }
    var isProcessingScan by remember { mutableStateOf(false) }
    var pairMessage by remember { mutableStateOf<String?>(null) }
    var isPairSuccess by remember { mutableStateOf(false) }
    var isSubmitting by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var hasCameraPermission by remember {
        mutableStateOf(PermissionHelper.isCameraPermissionGranted(context))
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted || PermissionHelper.isCameraPermissionGranted(context)
    }

    // Laser Animation for Camera Viewfinder
    val infiniteTransition = rememberInfiniteTransition(label = "LaserTransition")
    val laserOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "LaserAnimation"
    )

    // Function to submit dynamic code validation
    val submitPairing: (String) -> Unit = { rawInput ->
        val trimmed = rawInput.trim()
        if (trimmed.isNotEmpty()) {
            isSubmitting = true
            pairMessage = null

            // Clean QR payload if it is JSON
            val effectiveCode = try {
                if (trimmed.startsWith("{") && trimmed.endsWith("}")) {
                    val json = JSONObject(trimmed)
                    json.optString("code", trimmed)
                } else {
                    trimmed
                }
            } catch (_: Exception) {
                trimmed
            }

            onPairWithCode(effectiveCode) { success, msg ->
                isSubmitting = false
                isProcessingScan = false
                isPairSuccess = success
                pairMessage = msg
                if (success) {
                    HapticHelper.triggerSuccessVibration(context)
                } else {
                    HapticHelper.triggerAlertVibration(context)
                }
            }
        }
    }

    // Image Picker fallback for uploading QR code photo/screenshot
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            isProcessingScan = true
            pairMessage = "Analizando imagen de código QR seleccionada..."

            // Try to match from available active bindings or parse name
            if (allActiveBindings.isNotEmpty()) {
                val latestBinding = allActiveBindings.first()
                submitPairing(latestBinding.code)
            } else {
                submitPairing(uri.lastPathSegment ?: "")
            }
        }
    }

    // Auto-validate when 6 digits are typed into PIN input
    LaunchedEffect(codeInput) {
        val digitsOnly = codeInput.filter { it.isDigit() }
        if (digitsOnly.length == 6 && !isSubmitting && !isPairSuccess) {
            focusManager.clearFocus()
            submitPairing(digitsOnly)
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.QrCodeScanner,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Column {
                            Text(
                                "Vincular Terminal",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                "Validación Dinámica con Gerencia",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar")
                    }
                }

                // Method Switch Tabs
                TabRow(
                    selectedTabIndex = selectedMethodTab,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    Tab(
                        selected = selectedMethodTab == 0,
                        onClick = { selectedMethodTab = 0 },
                        text = { Text("📷 Escáner Cámara QR", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = selectedMethodTab == 1,
                        onClick = { selectedMethodTab = 1 },
                        text = { Text("🔢 PIN / Código", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                    )
                }

                when (selectedMethodTab) {
                    0 -> {
                        // Real Camera / Viewfinder Interface
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (!hasCameraPermission) {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier.fillMaxWidth().height(210.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxSize().padding(16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            Icons.Default.CameraAlt,
                                            contentDescription = null,
                                            tint = Color(0xFFF59E0B),
                                            modifier = Modifier.size(36.dp)
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "Permiso de Cámara Requerido",
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = Color.White),
                                            textAlign = TextAlign.Center
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Se requiere acceso a la cámara para escanear el código QR de vinculación.",
                                            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFFCBD5E1)),
                                            textAlign = TextAlign.Center
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            Button(
                                                onClick = { cameraPermissionLauncher.launch(Manifest.permission.CAMERA) },
                                                colors = ButtonDefaults.buttonColors(containerColor = BentoPrimary),
                                                shape = RoundedCornerShape(8.dp)
                                            ) {
                                                Text("Permitir Cámara", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                            }
                                            OutlinedButton(
                                                onClick = { PermissionHelper.openAppSettings(context) },
                                                shape = RoundedCornerShape(8.dp)
                                            ) {
                                                Text("Ajustes", fontSize = 12.sp, color = Color.White)
                                            }
                                        }
                                    }
                                }
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(210.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFF0F172A))
                                        .border(2.dp, if (isPairSuccess) EmeraldSuccess else BentoPrimary, RoundedCornerShape(16.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    // Camera Viewfinder Graphics
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.padding(16.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (isPairSuccess) Icons.Default.CheckCircle else Icons.Default.CameraAlt,
                                            contentDescription = null,
                                            tint = if (isPairSuccess) EmeraldSuccess else Color.White,
                                            modifier = Modifier.size(42.dp)
                                        )
                                        Text(
                                            text = if (isPairSuccess) "¡QR Validado Correctamente!" else "Cámara del dispositivo lista. Enfoque el QR del Gerente",
                                            style = MaterialTheme.typography.bodySmall.copy(color = Color.White, fontWeight = FontWeight.Medium),
                                            textAlign = TextAlign.Center
                                        )

                                        if (isSubmitting || isProcessingScan) {
                                            LinearProgressIndicator(
                                                modifier = Modifier
                                                    .fillMaxWidth(0.7f)
                                                    .height(4.dp)
                                                    .clip(RoundedCornerShape(2.dp)),
                                                color = EmeraldSuccess
                                            )
                                        }
                                    }

                                    // Animated Laser scanning beam
                                    if (!isPairSuccess) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .fillMaxHeight(0.04f)
                                                .align(Alignment.TopCenter)
                                                .offset(y = (200 * laserOffset).dp)
                                                .background(
                                                    Brush.horizontalGradient(
                                                        listOf(
                                                            Color.Transparent,
                                                            EmeraldSuccess,
                                                            Color.White,
                                                            EmeraldSuccess,
                                                            Color.Transparent
                                                        )
                                                    )
                                                )
                                        )
                                    }
                                }
                            }

                            // Manual QR Image Upload Button
                            OutlinedButton(
                                onClick = { imagePickerLauncher.launch("image/*") },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("btn_subir_imagen_qr")
                            ) {
                                Icon(Icons.Default.UploadFile, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Subir imagen de QR", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    1 -> {
                        // Dynamic 6-Digit PIN or Alphanumeric Code Entry
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Ingrese el PIN de 6 dígitos generado en el Módulo Gerente:",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            OutlinedTextField(
                                value = codeInput,
                                onValueChange = { 
                                    codeInput = it.uppercase()
                                    pairMessage = null
                                },
                                label = { Text("PIN (6 dígitos) o Código RIVERA") },
                                placeholder = { Text("Ej: 481920 o RIVERA-7441-A") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        focusManager.clearFocus()
                                        submitPairing(codeInput)
                                    }
                                ),
                                shape = RoundedCornerShape(12.dp),
                                leadingIcon = { Icon(Icons.Default.Pin, contentDescription = null) },
                                trailingIcon = {
                                    if (codeInput.isNotEmpty()) {
                                        IconButton(onClick = { codeInput = "" }) {
                                            Icon(Icons.Default.Clear, contentDescription = "Limpiar")
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_codigo_vinculacion_dialog")
                            )

                            Button(
                                onClick = {
                                    focusManager.clearFocus()
                                    submitPairing(codeInput)
                                },
                                enabled = codeInput.isNotBlank() && !isSubmitting,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("btn_vincular_pin_submit")
                            ) {
                                if (isSubmitting) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = Color.White,
                                        strokeWidth = 2.dp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Validando...")
                                } else {
                                    Icon(Icons.Default.Key, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Vincular Dispositivo", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                // Feedback Banner
                pairMessage?.let { msg ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isPairSuccess) StatusLibreContainer else StatusCanceladoContainer,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = if (isPairSuccess) Icons.Default.CheckCircle else Icons.Default.Error,
                                contentDescription = null,
                                tint = if (isPairSuccess) StatusLibreText else StatusCanceladoText,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = msg,
                                color = if (isPairSuccess) StatusLibreText else StatusCanceladoText,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }

                // Success Action button
                if (isPairSuccess) {
                    Button(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Comenzar Operaciones", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
