package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.entity.UserEntity
import com.example.ui.theme.*
import com.example.ui.viewmodel.RestaurantViewModel
import com.example.util.SecurityUtils

@Composable
fun UserManagementAdminView(
    viewModel: RestaurantViewModel,
    onMessage: (String) -> Unit = {}
) {
    val allUsers by viewModel.allUsers.collectAsState()
    val isFirestoreSyncActive by viewModel.isFirestoreSyncActive.collectAsState()

    var selectedRoleFilter by remember { mutableStateOf("TODOS") }
    var searchQuery by remember { mutableStateOf("") }

    var userToEdit by remember { mutableStateOf<UserEntity?>(null) }
    var isNewUser by remember { mutableStateOf(false) }
    var userToTestPin by remember { mutableStateOf<UserEntity?>(null) }
    var userToDelete by remember { mutableStateOf<UserEntity?>(null) }

    val filteredUsers = remember(allUsers, selectedRoleFilter, searchQuery) {
        allUsers.filter { user ->
            val matchesRole = when (selectedRoleFilter) {
                "MESERO" -> user.role.equals("MESERO", ignoreCase = true)
                "COCINA" -> user.role.equals("COCINA", ignoreCase = true)
                "CAJA" -> user.role.equals("CAJA", ignoreCase = true)
                "GERENTE" -> user.role.equals("GERENTE", ignoreCase = true)
                else -> true
            }
            val matchesSearch = searchQuery.isBlank() ||
                    user.name.contains(searchQuery, ignoreCase = true) ||
                    user.role.contains(searchQuery, ignoreCase = true)
            matchesRole && matchesSearch
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header Banner & Stats
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, CardBorderColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Gestión de Usuarios en Firestore",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = BentoPrimary
                            )
                        )
                        Text(
                            text = "Asignación de PIN de 4 dígitos con encriptación SHA-256 + Salt por Rol",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }

                    // Cloud Firestore Sync Badge
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isFirestoreSyncActive) EmeraldSuccess.copy(alpha = 0.12f) else AmberWarning.copy(alpha = 0.12f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudSync,
                                contentDescription = null,
                                tint = if (isFirestoreSyncActive) EmeraldSuccess else AmberWarning,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isFirestoreSyncActive) "Firestore Sync" else "Local / Sync",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isFirestoreSyncActive) EmeraldSuccess else AmberWarning
                                )
                            )
                        }
                    }
                }

                // Stats breakdown cards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    RoleStatBadge(
                        label = "Total",
                        count = allUsers.size,
                        color = BentoPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    RoleStatBadge(
                        label = "Meseros",
                        count = allUsers.count { it.role == "MESERO" },
                        color = BlueInfo,
                        modifier = Modifier.weight(1f)
                    )
                    RoleStatBadge(
                        label = "Cocina",
                        count = allUsers.count { it.role == "COCINA" },
                        color = AmberWarning,
                        modifier = Modifier.weight(1f)
                    )
                    RoleStatBadge(
                        label = "Caja",
                        count = allUsers.count { it.role == "CAJA" },
                        color = EmeraldSuccess,
                        modifier = Modifier.weight(1f)
                    )
                    RoleStatBadge(
                        label = "Gerencia",
                        count = allUsers.count { it.role == "GERENTE" },
                        color = BentoSecondary,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Action Toolbar & Search
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Buscar empleado...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .testTag("search_user_input")
            )

            Button(
                onClick = {
                    isNewUser = true
                    userToEdit = UserEntity(
                        name = "",
                        role = "MESERO",
                        pin = "",
                        isActive = true
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = BentoPrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("btn_nuevo_empleado")
            ) {
                Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Nuevo Empleado")
            }
        }

        // Filter chips by Role
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf("TODOS", "MESERO", "COCINA", "CAJA", "GERENTE").forEach { role ->
                FilterChip(
                    selected = selectedRoleFilter == role,
                    onClick = { selectedRoleFilter = role },
                    label = { Text(role, fontSize = 12.sp) },
                    modifier = Modifier.testTag("filter_role_$role")
                )
            }
        }

        // Employees List
        if (filteredUsers.isEmpty()) {
            EmptyStateCard(
                icon = Icons.Default.People,
                title = "No se encontraron empleados",
                message = "Presione 'Nuevo Empleado' para agregar personal con un PIN encriptado de 4 dígitos."
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredUsers, key = { it.id }) { user ->
                    EmployeeUserCard(
                        user = user,
                        onEdit = {
                            isNewUser = false
                            userToEdit = user
                        },
                        onTestPin = {
                            userToTestPin = user
                        },
                        onDelete = {
                            userToDelete = user
                        }
                    )
                }
            }
        }
    }

    // --- ADD / EDIT USER DIALOG WITH ENCRYPTED 4-DIGIT PIN KEYPAD ---
    if (userToEdit != null) {
        AddEditUserWithPinDialog(
            user = userToEdit!!,
            isNew = isNewUser,
            onDismiss = { userToEdit = null },
            onSave = { name, role, raw4DigitPin, isActive, branch ->
                val finalPin = if (raw4DigitPin.length == 4 && raw4DigitPin.all { it.isDigit() }) {
                    SecurityUtils.hashPin(raw4DigitPin)
                } else {
                    userToEdit!!.pin // keep existing hash if untouched
                }

                viewModel.saveUser(
                    id = if (isNewUser) 0L else userToEdit!!.id,
                    name = name,
                    role = role,
                    pin = finalPin,
                    isActive = isActive,
                    branchName = branch
                )
                userToEdit = null
                onMessage("Usuario '$name' ($role) guardado exitosamente con PIN encriptado.")
            }
        )
    }

    // --- TEST PIN DIALOG ---
    if (userToTestPin != null) {
        TestUserPinDialog(
            user = userToTestPin!!,
            onDismiss = { userToTestPin = null },
            onFailedPin = {
                viewModel.logFailedPinAttempt(
                    user = userToTestPin!!.name,
                    role = userToTestPin!!.role,
                    actionContext = "Prueba de PIN para Empleado ${userToTestPin!!.name}"
                )
            }
        )
    }

    // --- CONFIRM DELETE DIALOG ---
    if (userToDelete != null) {
        AlertDialog(
            onDismissRequest = { userToDelete = null },
            title = { Text("Eliminar Empleado") },
            text = { Text("¿Desea eliminar a '${userToDelete!!.name}' (${userToDelete!!.role})? También se eliminará del registro en Firestore.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteUser(userToDelete!!.id)
                        onMessage("Empleado '${userToDelete!!.name}' eliminado de Firestore.")
                        userToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RedError)
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { userToDelete = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun EmployeeUserCard(
    user: UserEntity,
    onEdit: () -> Unit,
    onTestPin: () -> Unit,
    onDelete: () -> Unit
) {
    val isPinConfigured = user.pin.isNotBlank()
    val isEncrypted = SecurityUtils.isEncryptedHash(user.pin)

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CardBorderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Role Avatar
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(
                            when (user.role) {
                                "GERENTE" -> BentoPrimary.copy(alpha = 0.12f)
                                "MESERO" -> BlueInfo.copy(alpha = 0.12f)
                                "CAJA" -> EmeraldSuccess.copy(alpha = 0.12f)
                                else -> AmberWarning.copy(alpha = 0.12f)
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (user.role) {
                            "GERENTE" -> Icons.Default.AdminPanelSettings
                            "MESERO" -> Icons.Default.Assignment
                            "CAJA" -> Icons.Default.PointOfSale
                            else -> Icons.Default.SoupKitchen
                        },
                        contentDescription = user.role,
                        tint = when (user.role) {
                            "GERENTE" -> BentoPrimary
                            "MESERO" -> BlueInfo
                            "CAJA" -> EmeraldSuccess
                            else -> AmberWarning
                        },
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = user.name,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = when (user.role) {
                                "GERENTE" -> BentoPrimary
                                "MESERO" -> BlueInfo
                                "CAJA" -> EmeraldSuccess
                                else -> AmberWarning
                            }
                        ) {
                            Text(
                                text = user.role,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                ),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (isPinConfigured) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = EmeraldSuccess,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isEncrypted) "PIN 4 dígitos (Encriptado SHA-256)" else "PIN 4 dígitos Asignado",
                                style = MaterialTheme.typography.labelSmall,
                                color = EmeraldSuccess
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = AmberWarning,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Sin PIN de acceso",
                                style = MaterialTheme.typography.labelSmall,
                                color = AmberWarning
                            )
                        }
                    }
                }
            }

            // Quick Actions
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (isPinConfigured) {
                    IconButton(
                        onClick = onTestPin,
                        modifier = Modifier.testTag("btn_probar_pin_${user.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.VpnKey,
                            contentDescription = "Probar PIN",
                            tint = BentoPrimary
                        )
                    }
                }

                IconButton(
                    onClick = onEdit,
                    modifier = Modifier.testTag("btn_editar_usuario_${user.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = BlueInfo
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.testTag("btn_eliminar_usuario_${user.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = RedError
                    )
                }
            }
        }
    }
}

@Composable
private fun RoleStatBadge(
    label: String,
    count: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = color.copy(alpha = 0.08f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.2f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$count",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = Color.Gray,
                maxLines = 1
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditUserWithPinDialog(
    user: UserEntity,
    isNew: Boolean,
    onDismiss: () -> Unit,
    onSave: (name: String, role: String, raw4DigitPin: String, isActive: Boolean, branch: String) -> Unit
) {
    var name by remember { mutableStateOf(user.name) }
    var role by remember { mutableStateOf(user.role) }
    var rawPin by remember { mutableStateOf("") } // 4-digit input
    var branchName by remember { mutableStateOf(user.branchName) }
    var isActive by remember { mutableStateOf(user.isActive) }
    var pinVisible by remember { mutableStateOf(false) }

    val roles = listOf("MESERO", "COCINA", "CAJA", "GERENTE")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            tonalElevation = 6.dp,
            border = BorderStroke(1.dp, CardBorderColor),
            modifier = Modifier.widthIn(max = 420.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = if (isNew) Icons.Default.PersonAdd else Icons.Default.Edit,
                        contentDescription = null,
                        tint = BentoPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isNew) "Agregar Nuevo Empleado" else "Editar Empleado",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                // Full Name
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre Completo del Empleado") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_employee_name")
                )

                // Role Selection
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Rol de Sistema (Permisos de Acceso):",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        roles.forEach { r ->
                            FilterChip(
                                selected = role == r,
                                onClick = { role = r },
                                label = { Text(r, fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                modifier = Modifier.testTag("select_role_$r")
                            )
                        }
                    }
                }

                // PIN Assignment Section
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
                    border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "PIN de Acceso de 4 Dígitos",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )

                            TextButton(
                                onClick = {
                                    rawPin = (1000..9999).random().toString()
                                },
                                modifier = Modifier.testTag("btn_generar_pin_random")
                            ) {
                                Icon(Icons.Default.AutoFixHigh, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Generar Random", fontSize = 11.sp)
                            }
                        }

                        // PIN Dot Indicators
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            repeat(4) { idx ->
                                val isFilled = idx < rawPin.length
                                val digitChar = if (isFilled && pinVisible) rawPin[idx].toString() else "•"

                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isFilled) BentoPrimary.copy(alpha = 0.1f) else Color.White)
                                        .border(
                                            width = 1.dp,
                                            color = if (isFilled) BentoPrimary else Color(0xFFD1D5DB),
                                            shape = RoundedCornerShape(10.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (isFilled) digitChar else "",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = BentoPrimary
                                        )
                                    )
                                }
                            }

                            IconButton(onClick = { pinVisible = !pinVisible }) {
                                Icon(
                                    imageVector = if (pinVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Mostrar/Ocultar PIN",
                                    tint = Color.Gray
                                )
                            }
                        }

                        // Compact Numeric Keypad
                        CustomNumericKeypad(
                            onDigitClick = { digit ->
                                if (rawPin.length < 4) {
                                    rawPin += digit
                                }
                            },
                            onBackspace = {
                                if (rawPin.isNotEmpty()) {
                                    rawPin = rawPin.dropLast(1)
                                }
                            },
                            onClear = { rawPin = "" }
                        )

                        Text(
                            text = "🔒 El PIN se guardará encriptado (SHA-256 + Salt) en Firestore y Room DB.",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("btn_cancelar_usuario")
                    ) {
                        Text("Cancelar")
                    }

                    Button(
                        onClick = {
                            if (name.isNotBlank()) {
                                onSave(name.trim(), role, rawPin, isActive, branchName)
                            }
                        },
                        enabled = name.isNotBlank(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BentoPrimary),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("btn_guardar_usuario")
                    ) {
                        Text("Guardar")
                    }
                }
            }
        }
    }
}

@Composable
fun TestUserPinDialog(
    user: UserEntity,
    onDismiss: () -> Unit,
    onFailedPin: (() -> Unit)? = null
) {
    var enteredPin by remember { mutableStateOf("") }
    var testResult by remember { mutableStateOf<Boolean?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            border = BorderStroke(1.dp, CardBorderColor),
            modifier = Modifier.widthIn(max = 360.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.VpnKey,
                    contentDescription = null,
                    tint = BentoPrimary,
                    modifier = Modifier.size(36.dp)
                )

                Text(
                    text = "Verificar PIN de ${user.name}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )

                Text(
                    text = "Pruebe la autenticación con el PIN de 4 dígitos ingresando la clave:",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                // 4 Masked Dots
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    repeat(4) { idx ->
                        val isFilled = idx < enteredPin.length
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(if (isFilled) BentoPrimary else Color(0xFFE5E7EB))
                        )
                    }
                }

                // Keypad
                CustomNumericKeypad(
                    onDigitClick = { d ->
                        if (enteredPin.length < 4) {
                            enteredPin += d
                            testResult = null
                        }
                    },
                    onBackspace = {
                        if (enteredPin.isNotEmpty()) {
                            enteredPin = enteredPin.dropLast(1)
                            testResult = null
                        }
                    },
                    onClear = {
                        enteredPin = ""
                        testResult = null
                    }
                )

                AnimatedVisibility(visible = testResult != null) {
                    if (testResult == true) {
                        Text(
                            text = "✅ PIN Correcto - Autenticación Autorizada en Firestore",
                            color = EmeraldSuccess,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    } else if (testResult == false) {
                        Text(
                            text = "❌ PIN Incorrecto - Acceso Denegado",
                            color = RedError,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cerrar")
                    }

                    Button(
                        onClick = {
                            val isValid = SecurityUtils.verifyPin(enteredPin, user.pin)
                            testResult = isValid
                            if (!isValid) {
                                onFailedPin?.invoke()
                            }
                        },
                        enabled = enteredPin.length == 4,
                        colors = ButtonDefaults.buttonColors(containerColor = BentoPrimary),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Validar")
                    }
                }
            }
        }
    }
}
