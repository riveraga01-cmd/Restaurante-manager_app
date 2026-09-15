package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SwitchAccount
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

import com.example.data.entity.UserEntity
import com.example.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

fun formatQuetzales(amount: Double): String {
    val nf = NumberFormat.getCurrencyInstance(Locale("es", "GT"))
    val formatted = nf.format(amount)
    return if (formatted.startsWith("GTQ")) {
        formatted.replace("GTQ", "Q")
    } else if (!formatted.contains("Q")) {
        "Q ${String.format(Locale.US, "%.2f", amount)}"
    } else {
        formatted
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModuleTopBar(
    title: String,
    subtitle: String? = null,
    onBackClick: () -> Unit,
    actions: @Composable RowScope.() -> Unit = {}
) {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        tonalElevation = 4.dp,
        shadowElevation = 3.dp
    ) {
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                    if (subtitle != null) {
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.88f)
                            )
                        )
                    }
                }
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Regresar a Inicio",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },
            actions = actions,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                actionIconContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )
    }
}

@Composable
fun SyncStatusChip(
    statusLabel: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val isDark = MaterialTheme.colorScheme.background == DarkRestaurantBackground

    val (bgColor, borderColor, textColor, dotColor) = when (statusLabel) {
        "Sincronizado" -> if (isDark) {
            Quadruple(DarkStatusLibreContainer, DarkStatusLibreBorder, DarkStatusLibreText, DarkStatusLibreBadge)
        } else {
            Quadruple(StatusLibreContainer, StatusLibreBorder, StatusLibreText, StatusLibreBadge)
        }
        "Sincronizando" -> if (isDark) {
            Quadruple(DarkStatusCobrarContainer, DarkStatusCobrarBorder, DarkStatusCobrarText, DarkStatusCobrarBadge)
        } else {
            Quadruple(StatusCobrarContainer, StatusCobrarBorder, StatusCobrarText, StatusCobrarBadge)
        }
        "Sin conexión" -> if (isDark) {
            Quadruple(DarkStatusOcupadaContainer, DarkStatusOcupadaBorder, DarkStatusOcupadaText, DarkStatusOcupadaBadge)
        } else {
            Quadruple(StatusOcupadaContainer, StatusOcupadaBorder, StatusOcupadaText, StatusOcupadaBadge)
        }
        "Error de sincronización" -> if (isDark) {
            Quadruple(DarkStatusCanceladoContainer, DarkStatusCanceladoBorder, DarkStatusCanceladoText, DarkStatusCanceladoBadge)
        } else {
            Quadruple(StatusCanceladoContainer, StatusCanceladoBorder, StatusCanceladoText, StatusCanceladoBadge)
        }
        "Pendiente de sincronizar" -> if (isDark) {
            Quadruple(DarkStatusCocinaContainer, DarkStatusCocinaBorder, DarkStatusCocinaText, DarkStatusCocinaBadge)
        } else {
            Quadruple(StatusCocinaContainer, StatusCocinaBorder, StatusCocinaText, StatusCocinaBadge)
        }
        else -> if (isDark) {
            Quadruple(DarkStatusLibreContainer, DarkStatusLibreBorder, DarkStatusLibreText, DarkStatusLibreBadge)
        } else {
            Quadruple(StatusLibreContainer, StatusLibreBorder, StatusLibreText, StatusLibreBadge)
        }
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, borderColor),
        modifier = modifier.then(
            if (onClick != null) Modifier.clickable { onClick() } else Modifier
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(color = dotColor, shape = CircleShape)
            )
            Text(
                text = statusLabel,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            )
        }
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

@Composable
fun StatusBadge(status: String) {
    val isDark = MaterialTheme.colorScheme.background == DarkRestaurantBackground

    val (bgColor, borderColor, textColor, label) = when (status.uppercase()) {
        "PENDIENTE", "EN_ESPERA", "EN ESPERA" -> if (isDark) {
            Quadruple(SemanticCocinaEnEsperaContainer, SemanticCocinaEnEsperaBorder, SemanticCocinaEnEsperaText, "EN ESPERA")
        } else {
            Quadruple(SemanticCocinaEnEsperaContainer, SemanticCocinaEnEsperaBorder, SemanticCocinaEnEsperaText, "EN ESPERA")
        }
        "EN_PROCESO", "EN PROCESO" -> if (isDark) {
            Quadruple(DarkStatusCobrarContainer, DarkStatusCobrarBorder, DarkStatusCobrarText, "EN PROCESO")
        } else {
            Quadruple(SemanticCocinaEnProcesoContainer, SemanticCocinaEnProcesoBorder, SemanticCocinaEnProcesoText, "EN PROCESO")
        }
        "FINALIZADO", "SERVIDO", "LISTO", "LISTA" -> if (isDark) {
            Quadruple(DarkStatusLibreContainer, DarkStatusLibreBorder, DarkStatusLibreText, "LISTA")
        } else {
            Quadruple(SemanticCocinaListaContainer, SemanticCocinaListaBorder, SemanticCocinaListaText, "LISTA")
        }
        "PAGADO" -> if (isDark) {
            Quadruple(DarkStatusCobrarContainer, DarkStatusCobrarBorder, DarkStatusCobrarText, "PAGADO")
        } else {
            Quadruple(SemanticMesaCobrarContainer, SemanticMesaCobrarBorder, SemanticMesaCobrarText, "PAGADO")
        }
        "CANCELADO" -> if (isDark) {
            Quadruple(DarkStatusCanceladoContainer, DarkStatusCanceladoBorder, DarkStatusCanceladoText, "CANCELADO")
        } else {
            Quadruple(SemanticCocinaCanceladoContainer, SemanticCocinaCanceladoBorder, SemanticCocinaCanceladoText, "CANCELADO")
        }
        else -> Quadruple(
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.outline,
            MaterialTheme.colorScheme.onSurfaceVariant,
            status
        )
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Text(
            text = label,
            color = textColor,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun UserSwitchDialog(
    title: String,
    role: String,
    users: List<UserEntity>,
    activeUserName: String,
    onSelectUser: (UserEntity) -> Unit,
    onAddNewUser: (name: String, role: String) -> Unit,
    onDismiss: () -> Unit
) {
    var showCreateForm by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.SwitchAccount,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Seleccione el usuario que iniciará sesión en esta pantalla:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (users.isEmpty()) {
                    Text(
                        text = "No hay usuarios registrados para el rol de $role.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                } else {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        users.forEach { user ->
                            val isSelected = user.name.equals(activeUserName, ignoreCase = true)
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                                ),
                                border = BorderStroke(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onSelectUser(user)
                                        onDismiss()
                                    }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Surface(
                                            shape = CircleShape,
                                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                            modifier = Modifier.size(36.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    imageVector = Icons.Default.Person,
                                                    contentDescription = null,
                                                    tint = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                        }
                                        Column {
                                            Text(
                                                text = user.name,
                                                style = MaterialTheme.typography.titleMedium.copy(
                                                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium
                                                )
                                            )
                                            Text(
                                                text = "Rol: ${user.role}",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }

                                    if (isSelected) {
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = MaterialTheme.colorScheme.primary
                                        ) {
                                            Text(
                                                text = "ACTIVO",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colorScheme.onPrimary
                                                ),
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (showCreateForm) {
                    HorizontalDivider()
                    Text(
                        text = "Registrar nuevo $role:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newName = it },
                        label = { Text("Nombre del usuario") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            if (newName.isNotBlank()) {
                                onAddNewUser(newName.trim(), role)
                                newName = ""
                                showCreateForm = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Guardar Usuario")
                    }
                } else {
                    OutlinedButton(
                        onClick = { showCreateForm = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Agregar nuevo $role")
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}

@Composable
fun EmptyStateCard(
    icon: ImageVector,
    title: String,
    message: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(68.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(36.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}
