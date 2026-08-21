package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.ripple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainRole
import com.example.ui.viewmodel.RestaurantViewModel
import com.example.util.HapticHelper

@Composable
fun InicioScreen(
    viewModel: RestaurantViewModel
) {
    val context = LocalContext.current
    val kitchenOrders by viewModel.kitchenOrders.collectAsState()
    val cashierOrders by viewModel.cashierOrders.collectAsState()
    val lowStock by viewModel.lowStockInventory.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val syncStatusLabel by viewModel.syncStatusLabel.collectAsState()
    val isDarkTheme by viewModel.isDarkTheme.collectAsState()
    val allDeviceBindings by viewModel.allDeviceBindings.collectAsState()
    val accessRestrictedMessage by viewModel.accessRestrictedMessage.collectAsState()

    var showPairDialog by remember { mutableStateOf(false) }
    var showAuthDialog by remember { mutableStateOf(false) }
    var codeInput by remember { mutableStateOf("") }
    var pairMessage by remember { mutableStateOf<String?>(null) }
    var isPairSuccess by remember { mutableStateOf(false) }

    val pendingKitchenCount = kitchenOrders.size
    val pendingCashierCount = cashierOrders.size
    val lowStockCount = lowStock.size

    val userInitials = if (!currentUser?.name.isNullOrEmpty()) {
        currentUser!!.name.split(" ").mapNotNull { it.firstOrNull() }.take(2).joinToString("").uppercase()
    } else "GP"

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Header Top Bar - Modern Gastronomy Brand
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 3.dp,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(46.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.RestaurantMenu,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }

                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "RESTAURANTE RIVERA",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 1.8.sp,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                )
                                com.example.ui.components.SyncStatusChip(
                                    statusLabel = syncStatusLabel,
                                    onClick = { viewModel.triggerManualSync() }
                                )
                            }
                            Text(
                                text = "POS & Kitchen Hub",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }
                    }

                    // Action Area: Theme Toggle, Pairing & User Profile
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilledTonalIconButton(
                            onClick = {
                                HapticHelper.triggerLightClick(context)
                                viewModel.toggleDarkTheme()
                            },
                            colors = IconButtonDefaults.filledTonalIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Icon(
                                imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = "Cambiar Tema",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        FilledTonalIconButton(
                            onClick = {
                                HapticHelper.triggerLightClick(context)
                                showPairDialog = true
                            },
                            colors = IconButtonDefaults.filledTonalIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            modifier = Modifier.testTag("btn_vincular_dispositivo_header")
                        ) {
                            Icon(
                                imageVector = Icons.Default.QrCodeScanner,
                                contentDescription = "Vincular Dispositivo",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .clickable {
                                    HapticHelper.triggerLightClick(context)
                                    showAuthDialog = true
                                }
                                .testTag("btn_usuario_perfil_header")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (currentUser != null) {
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = currentUser!!.name,
                                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = currentUser!!.role,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                } else {
                                    Text(
                                        text = "Iniciar Sesión",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }

                                Surface(
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = userInitials,
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontWeight = FontWeight.ExtraBold,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Main Content Area
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Live Operational Stats Banner
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)),
                    shadowElevation = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatusSummaryChip(
                            icon = Icons.Default.SoupKitchen,
                            count = pendingKitchenCount,
                            label = "Cocina",
                            badgeColor = if (isDarkTheme) DarkStatusCocinaBadge else StatusCocinaBadge,
                            containerColor = if (isDarkTheme) DarkStatusCocinaContainer else StatusCocinaContainer
                        )
                        HorizontalDivider(
                            modifier = Modifier
                                .height(28.dp)
                                .width(1.dp),
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        )
                        StatusSummaryChip(
                            icon = Icons.Default.PointOfSale,
                            count = pendingCashierCount,
                            label = "Por Cobrar",
                            badgeColor = if (isDarkTheme) DarkStatusCobrarBadge else StatusCobrarBadge,
                            containerColor = if (isDarkTheme) DarkStatusCobrarContainer else StatusCobrarContainer
                        )
                        if (lowStockCount > 0) {
                            HorizontalDivider(
                                modifier = Modifier
                                    .height(28.dp)
                                    .width(1.dp),
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            )
                            StatusSummaryChip(
                                icon = Icons.Default.WarningAmber,
                                count = lowStockCount,
                                label = "Stock Bajo",
                                badgeColor = if (isDarkTheme) DarkStatusCanceladoBadge else RedError,
                                containerColor = if (isDarkTheme) DarkStatusCanceladoContainer else StatusCanceladoContainer
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Módulos de Trabajo",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Text(
                        text = "Seleccione un área para ingresar",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 2x2 Bento Grid Layout for Roles
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    item {
                        val cardBg = MaterialTheme.colorScheme.surface
                        val cardContainer = MaterialTheme.colorScheme.primaryContainer
                        val cardPrimary = MaterialTheme.colorScheme.primary

                        BentoRoleCard(
                            title = "MESERO",
                            subtitle = "Toma de comandas y pedidos",
                            badgeText = null,
                            icon = Icons.Default.Assignment,
                            accentColor = cardPrimary,
                            containerGradient = listOf(
                                cardBg,
                                cardContainer.copy(alpha = 0.5f)
                            ),
                            testTag = "btn_mesero",
                            onClick = {
                                HapticHelper.triggerSuccessVibration(context)
                                viewModel.requestAccessToRole(MainRole.MESERO)
                            }
                        )
                    }

                    item {
                        val cardBg = MaterialTheme.colorScheme.surface
                        val cardContainer = MaterialTheme.colorScheme.primaryContainer
                        val cardPrimary = MaterialTheme.colorScheme.primary

                        BentoRoleCard(
                            title = "COCINA",
                            subtitle = "KDS y comandas en vivo",
                            badgeText = if (pendingKitchenCount > 0) "$pendingKitchenCount PENDIENTES" else null,
                            icon = Icons.Default.SoupKitchen,
                            accentColor = cardPrimary,
                            containerGradient = listOf(
                                cardBg,
                                cardContainer.copy(alpha = 0.5f)
                            ),
                            testTag = "btn_cocina",
                            onClick = {
                                HapticHelper.triggerSuccessVibration(context)
                                viewModel.requestAccessToRole(MainRole.COCINA)
                            }
                        )
                    }

                    item {
                        val cardBg = MaterialTheme.colorScheme.surface
                        val cardContainer = MaterialTheme.colorScheme.primaryContainer
                        val cardPrimary = MaterialTheme.colorScheme.primary
                        val isCajaRestricted = currentUser != null && currentUser?.role?.uppercase() in listOf("MESERO", "COCINA")

                        BentoRoleCard(
                            title = "CAJA",
                            subtitle = "Cobros, facturación y cierre",
                            badgeText = if (isCajaRestricted) "🔒 RESTRINGIDO" else if (pendingCashierCount > 0) "$pendingCashierCount POR COBRAR" else null,
                            icon = Icons.Default.PointOfSale,
                            accentColor = cardPrimary,
                            containerGradient = listOf(
                                cardBg,
                                cardContainer.copy(alpha = 0.5f)
                            ),
                            testTag = "btn_caja",
                            onClick = {
                                HapticHelper.triggerSuccessVibration(context)
                                viewModel.requestAccessToRole(MainRole.CAJA)
                            }
                        )
                    }

                    item {
                        val cardBg = MaterialTheme.colorScheme.surface
                        val cardContainer = MaterialTheme.colorScheme.primaryContainer
                        val cardPrimary = MaterialTheme.colorScheme.primary
                        val isGerenteRestricted = currentUser != null && currentUser?.role?.uppercase() in listOf("MESERO", "COCINA")

                        BentoRoleCard(
                            title = "GERENTE",
                            subtitle = "Inventario, precios y reportes",
                            badgeText = if (isGerenteRestricted) "🔒 REQUIERE PIN" else "🔒 ACCESO PIN",
                            icon = Icons.Default.AdminPanelSettings,
                            accentColor = cardPrimary,
                            containerGradient = listOf(
                                cardBg,
                                cardContainer.copy(alpha = 0.5f)
                            ),
                            testTag = "btn_gerente",
                            onClick = {
                                HapticHelper.triggerLightClick(context)
                                viewModel.requestAccessToRole(MainRole.GERENTE)
                            }
                        )
                    }
                }
            }

            // Footer Navigation Status Bar
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(9.dp)
                                .clip(CircleShape)
                                .background(EmeraldSuccess)
                        )
                        Text(
                            text = syncStatusLabel.uppercase(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.8.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }

                    Text(
                        text = "Sistema Restaurante Rivera • Moneda: Q (GTQ)",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    )
                }
            }
        }
    }

    if (showPairDialog) {
        com.example.ui.components.DevicePairingDialog(
            allActiveBindings = allDeviceBindings,
            onPairWithCode = { code, callback ->
                viewModel.pairDeviceWithCode(code) { success, msg, _ ->
                    callback(success, msg)
                    if (success) {
                        showPairDialog = false
                    }
                }
            },
            onDismiss = { showPairDialog = false }
        )
    }

    if (showAuthDialog) {
        com.example.ui.components.FirebaseAuthDialog(
            viewModel = viewModel,
            onDismiss = { showAuthDialog = false }
        )
    }

    if (accessRestrictedMessage != null) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissAccessRestriction() },
            icon = {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(36.dp)
                )
            },
            title = {
                Text(
                    text = "Control de Acceso por Rol",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = accessRestrictedMessage ?: "",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Si necesitas acceder para supervisión o cierre de turno, solicita autorización de un Gerente o cambia de usuario.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    OutlinedButton(
                        onClick = {
                            viewModel.dismissAccessRestriction()
                            showAuthDialog = true
                        }
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Cambiar Usuario")
                    }

                    Button(
                        onClick = {
                            viewModel.dismissAccessRestriction()
                            viewModel.navigateToRole(MainRole.GERENTE) // Will prompt for PIN
                        }
                    ) {
                        Icon(Icons.Default.LockOpen, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("PIN")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissAccessRestriction() }) {
                    Text("Cerrar")
                }
            }
        )
    }
}

@Composable
fun StatusSummaryChip(
    icon: ImageVector,
    count: Int,
    label: String,
    badgeColor: Color,
    containerColor: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = containerColor,
            modifier = Modifier.size(38.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = badgeColor,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Column {
            Text(
                text = "$count",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = badgeColor
                )
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun BentoRoleCard(
    title: String,
    subtitle: String,
    badgeText: String?,
    icon: ImageVector,
    accentColor: Color,
    containerGradient: List<Color>,
    testTag: String,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(targetValue = if (isPressed) 0.97f else 1f, label = "bentoScale")

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = containerGradient.first()),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp, pressedElevation = 1.dp),
        border = BorderStroke(1.5.dp, accentColor.copy(alpha = 0.2f)),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .scale(scale)
            .testTag(testTag)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true, color = accentColor),
                onClick = onClick
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = containerGradient
                    )
                )
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = accentColor.copy(alpha = 0.15f),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = icon,
                                contentDescription = title,
                                tint = accentColor,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = accentColor.copy(alpha = 0.6f),
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = accentColor,
                            letterSpacing = 0.5.sp
                        )
                    )

                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            fontSize = 11.sp,
                            lineHeight = 14.sp
                        ),
                        maxLines = 2
                    )
                }

                if (badgeText != null) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = accentColor,
                        modifier = Modifier.align(Alignment.Start)
                    ) {
                        Text(
                            text = badgeText,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 10.sp
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }
        }
    }
}


