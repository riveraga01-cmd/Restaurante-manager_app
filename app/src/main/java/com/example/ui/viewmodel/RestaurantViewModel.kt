package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.AppDatabase
import com.example.data.entity.*
import com.example.data.repository.RestaurantRepository
import com.example.data.firebase.FirestoreSyncManager
import com.example.util.NotificationHelper
import com.example.util.SecurityUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class MainRole { INICIO, MESERO, COCINA, CAJA, GERENTE, REPARTIDOR }
enum class WaiterTab { MAPA_MESAS, NUEVO_PEDIDO, PEDIDOS_ACTIVOS, HISTORIAL }
enum class CashierTab { POR_COBRAR, VENTAS_DIA, CIERRE_TURNO }
enum class ManagerTab { MENU, TEMAS_WEB, INVENTARIO, FACTURACION, RESUMEN_FINANCIERO, VENTAS, HISTORIAL, EMPLEADOS, AUDIT_LOGS, SEGURIDAD, QR_MENU, CONFIGURACION }
enum class DeliveryTab { LISTOS, EN_CAMINO, INCIDENCIAS, HISTORIAL }

class RestaurantViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: RestaurantRepository

    val isFirestoreSyncActive: StateFlow<Boolean>
    val lastSyncTimestamp: StateFlow<Long>
    val syncError: StateFlow<String?>
    val syncStatusLabel: StateFlow<String>

    private val prefs = application.getSharedPreferences("rivera_pos_session", Context.MODE_PRIVATE)

    private val persistentDeviceId: String = run {
        val existing = prefs.getString("unique_device_id", null)
        if (!existing.isNullOrBlank()) {
            existing
        } else {
            val generated = "DEV-" + java.util.UUID.randomUUID().toString().substring(0, 8).uppercase()
            prefs.edit().putString("unique_device_id", generated).apply()
            generated
        }
    }

    val deviceId = MutableStateFlow(persistentDeviceId).asStateFlow()

    init {
        val dao = AppDatabase.getDatabase(application, viewModelScope).restaurantDao()
        val syncManager = FirestoreSyncManager(dao, viewModelScope, application)
        repository = RestaurantRepository(dao, syncManager)

        isFirestoreSyncActive = syncManager.isLiveSyncActive
        lastSyncTimestamp = syncManager.lastSyncTimestamp
        syncError = syncManager.syncError
        syncStatusLabel = repository.syncStatusLabel ?: MutableStateFlow("Sincronizado").asStateFlow()
    }

    // --- SYSTEM SETTINGS FLOW ---
    val systemSettings: StateFlow<SystemSettingsEntity> = repository.systemSettings
        .map { settings ->
            val current = settings ?: SystemSettingsEntity(id = 1)
            if (current.website.isBlank() ||
                current.website == "www.restauranterivera.com" ||
                current.website.contains("Restaurante-manager_app")
            ) {
                current.copy(website = "https://riveraga01-cmd.github.io/Restaurante/")
            } else {
                current
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SystemSettingsEntity(id = 1, website = "https://riveraga01-cmd.github.io/Restaurante/"))

    fun saveSystemSettings(settings: SystemSettingsEntity, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.saveSystemSettings(settings)
            repository.logAudit(
                user = currentUser.value?.name ?: "Gerente",
                role = "GERENTE",
                deviceId = deviceId.value,
                action = "AJUSTES_SISTEMA",
                details = "Configuración general actualizada (${settings.restaurantName} - ${settings.branchName})"
            )
            onComplete()
        }
    }

    // --- DEVICE BINDINGS & PAIRING ---
    val allDeviceBindings: StateFlow<List<DeviceBindingEntity>> = repository.allDeviceBindings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allLinkedDevices: StateFlow<List<LinkedDeviceEntity>> = repository.allLinkedDevices
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun pairDeviceWithCode(code: String, onResult: (Boolean, String, String?) -> Unit) {
        viewModelScope.launch {
            val manufacturer = android.os.Build.MANUFACTURER.orEmpty().replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
            val model = android.os.Build.MODEL.orEmpty()
            val deviceModelName = if (manufacturer.isNotBlank() || model.isNotBlank()) {
                "$manufacturer $model".trim()
            } else {
                "Terminal Rivera " + persistentDeviceId.takeLast(4)
            }

            val (success, message, role) = repository.validateAndPairDeviceCode(
                code = code,
                deviceId = deviceId.value,
                deviceName = deviceModelName
            )
            if (success && role != null) {
                val targetRole = when (role.uppercase()) {
                    "MESERO" -> MainRole.MESERO
                    "COCINA" -> MainRole.COCINA
                    "CAJA" -> MainRole.CAJA
                    "GERENTE" -> MainRole.GERENTE
                    "REPARTIDOR", "DELIVERY" -> MainRole.REPARTIDOR
                    else -> MainRole.INICIO
                }
                _currentRole.value = targetRole
                prefs.edit()
                    .putString("saved_current_role", targetRole.name)
                    .putString("usuario_rol", role)
                    .putString("terminal_id", deviceId.value)
                    .apply()
            }
            onResult(success, message, role)
        }
    }

    fun generateDeviceBindingCode(
        role: String,
        branch: String = "Sucursal Central",
        isMultiUse: Boolean = true,
        daysValid: Int = 30,
        onGenerated: (DeviceBindingEntity) -> Unit = {}
    ) {
        viewModelScope.launch {
            val randomPin = (100000..999999).random().toString()
            val randomCode = "RIVERA-${(1000..9999).random()}-${('A'..'Z').random()}"
            val binding = DeviceBindingEntity(
                code = "$randomCode-$randomPin",
                branchName = branch,
                assignedRole = role,
                expiresAt = System.currentTimeMillis() + (86400000L * daysValid),
                isMultiUse = isMultiUse,
                createdByUser = currentUser.value?.name ?: "Gerente Principal"
            )
            repository.saveDeviceBinding(binding)
            repository.logAudit(
                user = currentUser.value?.name ?: "Gerente",
                role = "GERENTE",
                deviceId = deviceId.value,
                action = "GENERAR_CODIGO",
                details = "Código de vinculación ${binding.code} generado para rol $role (Multiuso: $isMultiUse)"
            )
            onGenerated(binding)
        }
    }

    fun deleteDeviceBinding(id: Long) {
        viewModelScope.launch {
            repository.deleteDeviceBinding(id)
        }
    }

    fun deleteLinkedDevice(targetDeviceId: String) {
        viewModelScope.launch {
            repository.deleteLinkedDevice(targetDeviceId)
            repository.logAudit(
                user = currentUser.value?.name ?: "Gerente",
                role = "GERENTE",
                deviceId = deviceId.value,
                action = "DESVINCULAR_DISPOSITIVO",
                details = "Dispositivo terminal $targetDeviceId desvinculado del restaurante"
            )
        }
    }

    // --- AUDIT LOGS FLOW ---
    val allAuditLogs: StateFlow<List<AuditLogEntity>> = repository.allAuditLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- MANUAL SYNC ACTION ---
    fun triggerManualSync(onComplete: (Boolean, String) -> Unit = { _, _ -> }) {
        repository.triggerManualSync(onComplete)
    }

    // --- NAVIGATION ROLE STATE & ANTI-REINICIOS PERSISTENCE ---
    private val _currentRole = MutableStateFlow(
        try {
            val saved = prefs.getString("saved_current_role", MainRole.INICIO.name) ?: MainRole.INICIO.name
            val role = MainRole.valueOf(saved)
            if (role == MainRole.GERENTE) MainRole.INICIO else role
        } catch (_: Exception) {
            MainRole.INICIO
        }
    )
    val currentRole: StateFlow<MainRole> = _currentRole.asStateFlow()

    // --- ROLE-BASED ACCESS CONTROL (NAVIGATION GUARD) ---
    private val _accessRestrictedMessage = MutableStateFlow<String?>(null)
    val accessRestrictedMessage: StateFlow<String?> = _accessRestrictedMessage.asStateFlow()

    fun dismissAccessRestriction() {
        _accessRestrictedMessage.value = null
    }

    fun canUserAccessModule(targetRole: MainRole): Boolean {
        val user = currentUser.value ?: return true
        val roleName = user.role.uppercase().trim()
        return when (targetRole) {
            MainRole.INICIO -> true
            MainRole.MESERO -> roleName in listOf("MESERO", "CAJA", "GERENTE", "ADMIN", "ADMINISTRADOR")
            MainRole.COCINA -> roleName in listOf("COCINA", "GERENTE", "ADMIN", "ADMINISTRADOR")
            MainRole.CAJA -> roleName in listOf("CAJA", "GERENTE", "ADMIN", "ADMINISTRADOR")
            MainRole.GERENTE -> roleName in listOf("GERENTE", "ADMIN", "ADMINISTRADOR")
            MainRole.REPARTIDOR -> roleName in listOf("REPARTIDOR", "CAJA", "GERENTE", "ADMIN", "ADMINISTRADOR")
        }
    }

    fun requestAccessToRole(role: MainRole) {
        val user = currentUser.value
        val roleName = user?.role?.uppercase()?.trim()

        when (role) {
            MainRole.CAJA -> {
                if (user != null && roleName in listOf("MESERO", "COCINA", "REPARTIDOR")) {
                    _accessRestrictedMessage.value = "Acceso Restringido: El usuario '${user.name}' tiene rol '$roleName'. El Módulo Caja requiere permisos de Cajero o Gerente."
                    return
                }
                navigateToRole(MainRole.CAJA)
            }
            MainRole.GERENTE -> {
                if (user != null && roleName in listOf("MESERO", "COCINA", "REPARTIDOR")) {
                    _accessRestrictedMessage.value = "Acceso Restringido: El usuario '${user.name}' tiene rol '$roleName'. El Módulo Gerente requiere autorización de Gerencia con PIN."
                    return
                }
                if (!isManagerUnlocked.value) {
                    _showPinPrompt.value = true
                } else {
                    navigateToRole(MainRole.GERENTE)
                }
            }
            MainRole.REPARTIDOR -> {
                if (user != null && roleName in listOf("COCINA")) {
                    _accessRestrictedMessage.value = "Acceso Restringido: El usuario '${user.name}' tiene rol '$roleName'. El Módulo Repartidor es para Personal de Entregas, Caja o Gerente."
                    return
                }
                navigateToRole(MainRole.REPARTIDOR)
            }
            else -> {
                navigateToRole(role)
            }
        }
    }

    fun navigateToRole(role: MainRole) {
        if (role == MainRole.GERENTE && !isManagerUnlocked.value) {
            // Require PIN entry
            _showPinPrompt.value = true
        } else {
            _currentRole.value = role
            prefs.edit().putString("saved_current_role", role.name).apply()
        }
    }

    fun navigateBackToInicio() {
        _currentRole.value = MainRole.INICIO
        prefs.edit().putString("saved_current_role", MainRole.INICIO.name).apply()
    }

    // --- MANAGER PIN SECURITY ---
    private val _isManagerUnlocked = MutableStateFlow(false)
    val isManagerUnlocked: StateFlow<Boolean> = _isManagerUnlocked.asStateFlow()

    private val _showPinPrompt = MutableStateFlow(false)
    val showPinPrompt: StateFlow<Boolean> = _showPinPrompt.asStateFlow()

    private val _pinError = MutableStateFlow(false)
    val pinError: StateFlow<Boolean> = _pinError.asStateFlow()

    fun verifyManagerPin(pinInput: String) {
        viewModelScope.launch {
            val isCorrect = repository.verifyManagerPin(pinInput)
            if (isCorrect) {
                _isManagerUnlocked.value = true
                _showPinPrompt.value = false
                _pinError.value = false
                _currentRole.value = MainRole.GERENTE
            } else {
                _pinError.value = true
            }
        }
    }

    fun dismissPinPrompt() {
        _showPinPrompt.value = false
        _pinError.value = false
    }

    fun lockManager() {
        _isManagerUnlocked.value = false
    }

    fun changeManagerPin(newPin: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.updateManagerPin(newPin)
            onComplete()
        }
    }

    // --- FIREBASE AUTHENTICATION & USERS STATE ---
    private val firebaseAuth: FirebaseAuth? by lazy {
        try {
            val app = getApplication<Application>()
            if (com.google.firebase.FirebaseApp.getApps(app).isEmpty()) {
                val initialized = try {
                    com.google.firebase.FirebaseApp.initializeApp(app) != null
                } catch (e: Throwable) {
                    false
                }
                if (!initialized && com.google.firebase.FirebaseApp.getApps(app).isEmpty()) {
                    try {
                        val options = com.google.firebase.FirebaseOptions.Builder()
                            .setApplicationId("1:433380736991:android:restauranteapp")
                            .setApiKey("AIzaSyFallbackKeyForRestauranteAppClient")
                            .setProjectId("dev-restaurante-app")
                            .build()
                        com.google.firebase.FirebaseApp.initializeApp(app, options)
                    } catch (e: Throwable) {
                        android.util.Log.w("RestaurantViewModel", "Fallback Firebase options failed: ${e.message}")
                    }
                }
            }
            FirebaseAuth.getInstance()
        } catch (e: Throwable) {
            android.util.Log.w("RestaurantViewModel", "FirebaseAuth not available: ${e.message}")
            null
        }
    }

    private val _firebaseUser = MutableStateFlow<FirebaseUser?>(null)
    val firebaseUser: StateFlow<FirebaseUser?> = _firebaseUser.asStateFlow()

    private val _isAuthLoading = MutableStateFlow(false)
    val isAuthLoading: StateFlow<Boolean> = _isAuthLoading.asStateFlow()

    private val _authErrorMessage = MutableStateFlow<String?>(null)
    val authErrorMessage: StateFlow<String?> = _authErrorMessage.asStateFlow()

    val allUsers: StateFlow<List<UserEntity>> = repository.allUsers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val waiterUsers: StateFlow<List<UserEntity>> = repository.getUsersByRole("MESERO")
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cashierUsers: StateFlow<List<UserEntity>> = repository.getUsersByRole("CAJA")
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val kitchenUsers: StateFlow<List<UserEntity>> = repository.getUsersByRole("COCINA")
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val managerUsers: StateFlow<List<UserEntity>> = repository.getUsersByRole("GERENTE")
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val repartidorUsers: StateFlow<List<UserEntity>> = repository.getUsersByRole("REPARTIDOR")
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

    fun clearAuthError() {
        _authErrorMessage.value = null
    }

    fun signInWithFirebaseAuth(
        email: String,
        pass: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        if (email.isBlank() || pass.isBlank()) {
            _authErrorMessage.value = "Ingresa correo y contraseña válidos"
            onResult(false, "Ingresa correo y contraseña válidos")
            return
        }

        val auth = firebaseAuth
        if (auth == null) {
            _authErrorMessage.value = "Servicio de Firebase Auth no disponible en este entorno"
            onResult(false, "Servicio de Firebase Auth no disponible en este entorno")
            return
        }

        _isAuthLoading.value = true
        _authErrorMessage.value = null

        auth.signInWithEmailAndPassword(email.trim(), pass)
            .addOnSuccessListener { authResult ->
                _isAuthLoading.value = false
                val fbUser = authResult.user
                _firebaseUser.value = fbUser

                // Match with local UserEntity by email or create profile
                val matchingUser = allUsers.value.firstOrNull { it.email.equals(email.trim(), ignoreCase = true) }
                if (matchingUser != null) {
                    setCurrentUser(matchingUser)
                } else {
                    // Create local user representation
                    val defaultName = fbUser?.displayName?.ifEmpty { null } ?: email.substringBefore("@").replaceFirstChar { it.uppercase() }
                    val newUser = UserEntity(
                        name = defaultName,
                        role = "GERENTE", // Default role for Firebase authenticated owner/admin
                        email = email.trim(),
                        branchName = "Sucursal Central"
                    )
                    viewModelScope.launch {
                        repository.saveUser(newUser)
                        setCurrentUser(newUser)
                    }
                }
                onResult(true, null)
            }
            .addOnFailureListener { exc ->
                _isAuthLoading.value = false
                val errorMsg = exc.localizedMessage ?: "Error de autenticación en Firebase"
                _authErrorMessage.value = errorMsg
                onResult(false, errorMsg)
            }
    }

    fun signUpWithFirebaseAuth(
        email: String,
        pass: String,
        name: String,
        role: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        if (email.isBlank() || pass.length < 6) {
            val msg = "El correo debe ser válido y la contraseña tener mínimo 6 caracteres"
            _authErrorMessage.value = msg
            onResult(false, msg)
            return
        }

        val auth = firebaseAuth
        if (auth == null) {
            _authErrorMessage.value = "Servicio de Firebase Auth no disponible en este entorno"
            onResult(false, "Servicio de Firebase Auth no disponible en este entorno")
            return
        }

        _isAuthLoading.value = true
        _authErrorMessage.value = null

        auth.createUserWithEmailAndPassword(email.trim(), pass)
            .addOnSuccessListener { authResult ->
                _isAuthLoading.value = false
                val fbUser = authResult.user
                _firebaseUser.value = fbUser

                val newUser = UserEntity(
                    name = name.ifBlank { email.substringBefore("@") },
                    role = role.uppercase(),
                    email = email.trim(),
                    branchName = "Sucursal Central"
                )
                viewModelScope.launch {
                    repository.saveUser(newUser)
                    setCurrentUser(newUser)
                }
                onResult(true, null)
            }
            .addOnFailureListener { exc ->
                _isAuthLoading.value = false
                val errorMsg = exc.localizedMessage ?: "Error al registrar en Firebase"
                _authErrorMessage.value = errorMsg
                onResult(false, errorMsg)
            }
    }

    fun signOutFirebaseAuth() {
        try {
            firebaseAuth?.signOut()
        } catch (e: Throwable) {
            android.util.Log.w("RestaurantViewModel", "Error signing out: ${e.message}")
        }
        _firebaseUser.value = null
        _currentUser.value = null
    }

    fun setCurrentUser(user: UserEntity) {
        _currentUser.value = user
        if (user.role == "MESERO") {
            _waiterName.value = user.name
        } else if (user.role == "CAJA") {
            _cashierName.value = user.name
        }
    }

    fun saveUser(
        id: Long,
        name: String,
        role: String,
        pin: String = "",
        email: String = "",
        password: String = "123456",
        isActive: Boolean = true,
        branchName: String = "Sucursal Central"
    ) {
        viewModelScope.launch {
            val processedPin = if (pin.length == 4 && pin.all { it.isDigit() }) {
                SecurityUtils.hashPin(pin)
            } else {
                pin
            }

            val user = UserEntity(
                id = id,
                name = name,
                role = role,
                pin = processedPin,
                email = email,
                password = password,
                isActive = isActive,
                branchName = branchName
            )
            repository.saveUser(user)
            repository.logAudit(
                user = currentUser.value?.name ?: "Gerente",
                role = "GERENTE",
                deviceId = deviceId.value,
                action = if (id == 0L) "CREAR_USUARIO" else "EDITAR_USUARIO",
                details = "Empleado $name ($role) ${if (id == 0L) "creado" else "actualizado"} con PIN encriptado en Firestore"
            )
        }
    }

    fun deleteUser(id: Long) {
        viewModelScope.launch {
            repository.deleteUser(id)
        }
    }

    // --- GENERAL DATA FLOWS ---
    val allMenuItems: StateFlow<List<MenuItemEntity>> = repository.allMenuItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val availableMenuItems: StateFlow<List<MenuItemEntity>> = repository.availableMenuItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allInventory: StateFlow<List<InventoryItemEntity>> = repository.allInventory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val lowStockInventory: StateFlow<List<InventoryItemEntity>> = repository.lowStockInventory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val kitchenOrders: StateFlow<List<OrderEntity>> = repository.kitchenOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cashierOrders: StateFlow<List<OrderEntity>> = repository.cashierOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allOrders: StateFlow<List<OrderEntity>> = repository.allOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allSales: StateFlow<List<SaleEntity>> = repository.allSales
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allDailyCloses: StateFlow<List<DailyCloseEntity>> = repository.allDailyCloses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- DARK THEME STATE ---
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    fun toggleDarkTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }

    fun setDarkTheme(enabled: Boolean) {
        _isDarkTheme.value = enabled
    }

    // --- FIREBASE AUTH COMPATIBILITY BRIDGES ---
    fun loginWithEmailPassword(email: String, pass: String, onResult: (Boolean, String?) -> Unit) {
        signInWithFirebaseAuth(email, pass) { success, err ->
            if (success) {
                val roleStr = currentUser.value?.role ?: "GERENTE"
                when (roleStr) {
                    "MESERO" -> navigateToRole(MainRole.MESERO)
                    "COCINA" -> navigateToRole(MainRole.COCINA)
                    "CAJA" -> navigateToRole(MainRole.CAJA)
                    "GERENTE" -> {
                        _isManagerUnlocked.value = true
                        navigateToRole(MainRole.GERENTE)
                    }
                }
                onResult(true, "Autenticado con Firebase como $roleStr ($email)")
            } else {
                onResult(false, err)
            }
        }
    }

    fun registerEmployeeWithEmailPassword(
        email: String,
        pass: String,
        name: String,
        role: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        signUpWithFirebaseAuth(email, pass, name, role, onResult)
    }

    fun logoutFirebaseAuth() {
        signOutFirebaseAuth()
        _currentRole.value = MainRole.INICIO
    }

    // --- CASHIER CORTE DE CAJA ---
    fun recordCorteDeCaja(
        cashierName: String,
        initialCash: Double,
        actualCashCounted: Double,
        cardSales: Double,
        transferSales: Double,
        totalSales: Double,
        onComplete: (DailyCloseEntity) -> Unit
    ) {
        viewModelScope.launch {
            val cashSales = totalSales - cardSales - transferSales
            val expectedCash = initialCash + cashSales
            val difference = actualCashCounted - expectedCash
            val dateCompact = java.text.SimpleDateFormat("yyyyMMdd", java.util.Locale.getDefault()).format(java.util.Date())
            val timeCompact = java.text.SimpleDateFormat("HHmmss", java.util.Locale.getDefault()).format(java.util.Date())
            val folio = "CLO-$dateCompact-$timeCompact"

            val summaryJson = """
                {
                    "cajero": "$cashierName",
                    "fondoInicial": $initialCash,
                    "ventasEfectivo": $cashSales,
                    "ventasTarjeta": $cardSales,
                    "ventasTransferencia": $transferSales,
                    "granTotal": $totalSales,
                    "efectivoEsperado": $expectedCash,
                    "efectivoContado": $actualCashCounted,
                    "diferencia": $difference
                }
            """.trimIndent()

            val dailyClose = DailyCloseEntity(
                folio = folio,
                closeDate = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date()),
                totalSales = totalSales,
                totalOrders = allSales.value.size,
                closedBy = cashierName,
                role = "CAJA",
                deviceId = deviceId.value,
                summaryJson = summaryJson,
                timestamp = System.currentTimeMillis()
            )

            repository.saveDailyClose(dailyClose)
            repository.logAudit(
                user = cashierName,
                role = "CAJA",
                deviceId = deviceId.value,
                action = "CORTE_DE_CAJA",
                details = "Corte de caja $folio procesado por $cashierName. Total Q$totalSales, Efectivo Real Q$actualCashCounted, Diferencia Q$difference"
            )
            onComplete(dailyClose)
        }
    }

    fun resetShiftAfterCorte(onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            val user = _cashierName.value.ifBlank { currentUser.value?.name ?: "Cajero" }
            repository.resetShiftAfterCorte(
                user = user,
                deviceId = deviceId.value
            )
            onComplete()
        }
    }

    suspend fun validateManagerPin(pinInput: String): Boolean {
        return repository.verifyManagerPin(pinInput.trim())
    }


    // --- MESERO STATE ---
    private val _waiterTab = MutableStateFlow(WaiterTab.MAPA_MESAS)
    val waiterTab: StateFlow<WaiterTab> = _waiterTab.asStateFlow()
    fun setWaiterTab(tab: WaiterTab) { _waiterTab.value = tab }

    private val _selectedTable = MutableStateFlow("Mesa 1")
    val selectedTable: StateFlow<String> = _selectedTable.asStateFlow()
    fun setSelectedTable(table: String) { _selectedTable.value = table }

    private val _waiterName = MutableStateFlow("Carlos López")
    val waiterName: StateFlow<String> = _waiterName.asStateFlow()
    fun setWaiterName(name: String) { _waiterName.value = name }

    private val _menuSearchQuery = MutableStateFlow("")
    val menuSearchQuery: StateFlow<String> = _menuSearchQuery.asStateFlow()
    fun setMenuSearchQuery(query: String) { _menuSearchQuery.value = query }

    private val _menuCategoryFilter = MutableStateFlow("Todos")
    val menuCategoryFilter: StateFlow<String> = _menuCategoryFilter.asStateFlow()
    fun setMenuCategoryFilter(category: String) { _menuCategoryFilter.value = category }

    // Cart Draft: MenuItem -> Pair(Quantity, ItemNotes)
    private val _cartItems = MutableStateFlow<Map<MenuItemEntity, Pair<Int, String>>>(emptyMap())
    val cartItems: StateFlow<Map<MenuItemEntity, Pair<Int, String>>> = _cartItems.asStateFlow()

    private val _generalOrderNotes = MutableStateFlow("")
    val generalOrderNotes: StateFlow<String> = _generalOrderNotes.asStateFlow()
    fun setGeneralOrderNotes(notes: String) { _generalOrderNotes.value = notes }

    fun addItemToCart(item: MenuItemEntity, notes: String = "") {
        val current = _cartItems.value.toMutableMap()
        val existing = current[item]
        val newQty = (existing?.first ?: 0) + 1
        val newNotes = if (notes.isNotEmpty()) notes else (existing?.second ?: "")
        current[item] = Pair(newQty, newNotes)
        _cartItems.value = current
    }

    fun updateCartItemQuantity(item: MenuItemEntity, delta: Int) {
        val current = _cartItems.value.toMutableMap()
        val existing = current[item] ?: return
        val newQty = existing.first + delta
        if (newQty <= 0) {
            current.remove(item)
        } else {
            current[item] = Pair(newQty, existing.second)
        }
        _cartItems.value = current
    }

    fun updateCartItemNotes(item: MenuItemEntity, notes: String) {
        val current = _cartItems.value.toMutableMap()
        val existing = current[item] ?: return
        current[item] = Pair(existing.first, notes)
        _cartItems.value = current
    }

    fun clearCart() {
        _cartItems.value = emptyMap()
        _generalOrderNotes.value = ""
    }

    fun submitOrderToKitchen(onSuccess: () -> Unit) {
        val cartMap = _cartItems.value
        if (cartMap.isEmpty()) return

        viewModelScope.launch {
            val list = cartMap.entries.map { Pair(it.key, it.value) }
            val table = _selectedTable.value
            val waiter = _waiterName.value
            val totalQty = list.sumOf { it.second.first }

            val orderId = repository.createOrder(
                tableNumber = table,
                waiterName = waiter,
                items = list,
                generalNotes = _generalOrderNotes.value.ifBlank { null }
            )

            val order = repository.getOrderById(orderId)
            val orderNum = order?.orderNumber ?: "PED-$orderId"

            // Trigger real-time FCM / Local Notification for Kitchen
            val currentSettings: SystemSettingsEntity = systemSettings.value
            if (currentSettings.notificationsEnabled && currentSettings.orderAlertsEnabled) {
                val soundTone: String? = if (currentSettings.kitchenRingtoneUri.isNotBlank()) currentSettings.kitchenRingtoneUri else currentSettings.notificationRingtoneUri.ifBlank { null }
                NotificationHelper.sendKitchenNotificationLocally(
                    context = getApplication<Application>(),
                    orderNumber = orderNum,
                    tableNumber = table,
                    itemCount = totalQty,
                    waiterName = waiter,
                    soundUriString = soundTone,
                    enableSound = currentSettings.kitchenSoundEnabled && currentSettings.notificationSoundEnabled,
                    enableVibration = currentSettings.notificationVibrationEnabled
                )
            }

            clearCart()
            _waiterTab.value = WaiterTab.PEDIDOS_ACTIVOS
            onSuccess()
        }
    }

    val waiterActiveOrders: StateFlow<List<OrderEntity>> = _waiterName
        .flatMapLatest { name -> repository.getWaiterActiveOrders(name) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val waiterHistoryOrders: StateFlow<List<OrderEntity>> = _waiterName
        .flatMapLatest { name -> repository.getWaiterHistory(name) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    // --- COCINA STATE ---
    fun updateOrderStatus(orderId: Long, newStatus: String) {
        viewModelScope.launch {
            repository.updateOrderStatus(orderId, newStatus)
        }
    }

    fun updateOrderItemKitchenStatus(itemId: Long, newStatus: String) {
        viewModelScope.launch {
            repository.updateOrderItemKitchenStatus(itemId, newStatus)
        }
    }

    fun getOrderItemsFlow(orderId: Long): Flow<List<OrderItemEntity>> {
        return repository.getOrderItemsFlow(orderId)
    }


    // --- CAJA STATE ---
    private val _cashierTab = MutableStateFlow(CashierTab.POR_COBRAR)
    val cashierTab: StateFlow<CashierTab> = _cashierTab.asStateFlow()
    fun setCashierTab(tab: CashierTab) { _cashierTab.value = tab }

    private val _selectedPaymentOrder = MutableStateFlow<OrderEntity?>(null)
    val selectedPaymentOrder: StateFlow<OrderEntity?> = _selectedPaymentOrder.asStateFlow()
    fun setSelectedPaymentOrder(order: OrderEntity?) { _selectedPaymentOrder.value = order }

    private val _paymentMethod = MutableStateFlow("Efectivo")
    val paymentMethod: StateFlow<String> = _paymentMethod.asStateFlow()
    fun setPaymentMethod(method: String) { _paymentMethod.value = method }

    private val _cashierName = MutableStateFlow("Ana Rivas")
    val cashierName: StateFlow<String> = _cashierName.asStateFlow()
    fun setCashierName(name: String) { _cashierName.value = name }

    private val _cashTendered = MutableStateFlow("")
    val cashTendered: StateFlow<String> = _cashTendered.asStateFlow()
    fun setCashTendered(amount: String) { _cashTendered.value = amount }

    fun processOrderPayment(orderId: Long, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.processPayment(
                orderId = orderId,
                paymentMethod = _paymentMethod.value,
                cashierName = _cashierName.value
            )
            _selectedPaymentOrder.value = null
            _cashTendered.value = ""
            onSuccess()
        }
    }


    // --- GERENTE STATE ---
    private val _managerTab = MutableStateFlow(ManagerTab.MENU)
    val managerTab: StateFlow<ManagerTab> = _managerTab.asStateFlow()
    fun setManagerTab(tab: ManagerTab) { _managerTab.value = tab }

    private val _salesPeriod = MutableStateFlow("Diario")
    val salesPeriod: StateFlow<String> = _salesPeriod.asStateFlow()
    fun setSalesPeriod(period: String) { _salesPeriod.value = period }

    val filteredSales: StateFlow<List<SaleEntity>> = _salesPeriod
        .flatMapLatest { period -> repository.getSalesForPeriod(period) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categorySalesForPeriod: StateFlow<List<CategorySaleSummary>> = _salesPeriod
        .flatMapLatest { period -> repository.getCategorySalesForPeriod(period) }
        .map { dbList ->
            val defaultCategories = listOf("Platillos", "Bebidas", "Postres", "Entradas")
            val map = dbList.associateBy { it.category }
            defaultCategories.map { cat ->
                map[cat] ?: CategorySaleSummary(category = cat, totalAmount = 0.0, itemCount = 0)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- REPORT FILTERS & DETAILED REPORT STATE ---
    private val _reportCategoryFilter = MutableStateFlow("Todas")
    val reportCategoryFilter: StateFlow<String> = _reportCategoryFilter.asStateFlow()
    fun setReportCategoryFilter(category: String) { _reportCategoryFilter.value = category }

    private val _reportSearchQuery = MutableStateFlow("")
    val reportSearchQuery: StateFlow<String> = _reportSearchQuery.asStateFlow()
    fun setReportSearchQuery(query: String) { _reportSearchQuery.value = query }

    private val _reportWaiterFilter = MutableStateFlow("Todos")
    val reportWaiterFilter: StateFlow<String> = _reportWaiterFilter.asStateFlow()
    fun setReportWaiterFilter(waiter: String) { _reportWaiterFilter.value = waiter }

    private val _reportSortOption = MutableStateFlow("Más vendidos")
    val reportSortOption: StateFlow<String> = _reportSortOption.asStateFlow()
    fun setReportSortOption(option: String) { _reportSortOption.value = option }

    val detailedSalesReport: StateFlow<DetailedSalesReportData> = combine(
        _salesPeriod,
        allOrders,
        repository.allOrderItems,
        allMenuItems,
        _reportCategoryFilter,
        _reportSearchQuery,
        _reportWaiterFilter,
        _reportSortOption
    ) { args ->
        val period = args[0] as String
        @Suppress("UNCHECKED_CAST") val orders = args[1] as List<OrderEntity>
        @Suppress("UNCHECKED_CAST") val orderItems = args[2] as List<OrderItemEntity>
        @Suppress("UNCHECKED_CAST") val menuItems = args[3] as List<MenuItemEntity>
        val categoryFilter = args[4] as String
        val searchQuery = args[5] as String
        val waiterFilter = args[6] as String
        val sortOption = args[7] as String

        val cal = java.util.Calendar.getInstance()
        val now = System.currentTimeMillis()
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0)
        cal.set(java.util.Calendar.MINUTE, 0)
        cal.set(java.util.Calendar.SECOND, 0)
        cal.set(java.util.Calendar.MILLISECOND, 0)

        val periodStartMs = when (period.lowercase()) {
            "diario" -> cal.timeInMillis
            "semanal" -> { cal.add(java.util.Calendar.DAY_OF_YEAR, -7); cal.timeInMillis }
            "quincenal" -> { cal.add(java.util.Calendar.DAY_OF_YEAR, -15); cal.timeInMillis }
            "mensual" -> { cal.add(java.util.Calendar.DAY_OF_YEAR, -30); cal.timeInMillis }
            else -> 0L
        }

        val menuCategoryMap = menuItems.associate { it.id to it.category }

        // Filter paid orders for the selected period & waiter
        val paidOrdersInPeriod = orders.filter { order ->
            order.status == "PAGADO" &&
            (order.paidAt ?: order.createdAt) >= periodStartMs &&
            (order.paidAt ?: order.createdAt) <= now &&
            (waiterFilter == "Todos" || order.waiterName.equals(waiterFilter, ignoreCase = true))
        }

        val paidOrderIds = paidOrdersInPeriod.map { it.id }.toSet()

        // Filter order items matching paid orders
        val periodOrderItems = orderItems.filter { it.orderId in paidOrderIds }

        // Group order items by product name
        val productGrouped = periodOrderItems.groupBy { it.productName }

        val allReportItems = productGrouped.map { (prodName, itemsList) ->
            val totalQty = itemsList.sumOf { it.quantity }
            val totalRev = itemsList.sumOf { it.subtotal }
            val unitPrice = if (itemsList.isNotEmpty()) itemsList.first().unitPrice else 0.0
            val sampleMenuItemId = itemsList.firstOrNull()?.menuItemId
            val category = (sampleMenuItemId?.let { menuCategoryMap[it] })
                ?: menuItems.firstOrNull { it.name.equals(prodName, ignoreCase = true) }?.category
                ?: "Platillos"

            SoldProductReportItem(
                productName = prodName,
                category = category,
                unitPrice = unitPrice,
                quantitySold = totalQty,
                totalRevenue = totalRev,
                participationPercentage = 0.0
            )
        }

        // Calculate grand revenue of all items in period
        val grandRevenue = allReportItems.sumOf { it.totalRevenue }

        val reportItemsWithParticipation = allReportItems.map { item ->
            val percentage = if (grandRevenue > 0) (item.totalRevenue / grandRevenue) * 100 else 0.0
            item.copy(participationPercentage = percentage)
        }

        // Apply product search and category filter
        val filteredItems = reportItemsWithParticipation.filter { item ->
            val matchesCategory = (categoryFilter == "Todas" || item.category.equals(categoryFilter, ignoreCase = true))
            val matchesSearch = (searchQuery.isBlank() || item.productName.contains(searchQuery, ignoreCase = true))
            matchesCategory && matchesSearch
        }

        // Sort items
        val sortedItems = when (sortOption) {
            "Menos vendidos" -> filteredItems.sortedBy { it.quantitySold }
            "Mayor ingreso" -> filteredItems.sortedByDescending { it.totalRevenue }
            "Cantidad" -> filteredItems.sortedByDescending { it.quantitySold }
            else -> filteredItems.sortedByDescending { it.quantitySold } // "Más vendidos"
        }

        // Group into map by standard categories
        val defaultCats = listOf("Platillos", "Bebidas", "Postres", "Entradas")
        val groupedMap = mutableMapOf<String, List<SoldProductReportItem>>()

        defaultCats.forEach { cat ->
            val itemsInCat = sortedItems.filter { it.category.equals(cat, ignoreCase = true) }
            groupedMap[cat] = itemsInCat
        }

        // Add any additional non-standard categories if present
        sortedItems.forEach { item ->
            val matchingCat = defaultCats.firstOrNull { it.equals(item.category, ignoreCase = true) }
            if (matchingCat == null) {
                val existing = groupedMap.getOrPut(item.category) { emptyList() }
                if (existing.none { it.productName == item.productName }) {
                    groupedMap[item.category] = existing + item
                }
            }
        }

        // Summary Statistics
        val totalProductsSold = allReportItems.sumOf { it.quantitySold }
        val totalRevenue = paidOrdersInPeriod.sumOf { it.totalAmount }
        val orderCount = paidOrdersInPeriod.size
        val averageTicket = if (orderCount > 0) totalRevenue / orderCount else 0.0

        val mostSoldProduct = allReportItems.maxByOrNull { it.quantitySold }?.let { Pair(it.productName, it.quantitySold) }
        val leastSoldProduct = allReportItems.minByOrNull { it.quantitySold }?.let { Pair(it.productName, it.quantitySold) }

        // Peak Sales Hour calculation
        val peakSalesHour = if (paidOrdersInPeriod.isNotEmpty()) {
            val hourMap = paidOrdersInPeriod.groupBy { ord ->
                val c = java.util.Calendar.getInstance()
                c.timeInMillis = ord.paidAt ?: ord.createdAt
                c.get(java.util.Calendar.HOUR_OF_DAY)
            }
            val topHour = hourMap.maxByOrNull { entry -> entry.value.sumOf { it.totalAmount } }?.key ?: 12
            val nextHour = (topHour + 1) % 24
            String.format(java.util.Locale.US, "%02d:00 - %02d:00 hrs", topHour, nextHour)
        } else {
            "N/A"
        }

        // Top Selling Waiter
        val topSellingWaiter = if (paidOrdersInPeriod.isNotEmpty()) {
            paidOrdersInPeriod.groupBy { it.waiterName }
                .mapValues { entry -> entry.value.sumOf { it.totalAmount } }
                .maxByOrNull { it.value }
                ?.toPair()
        } else null

        // Top Consuming Table
        val topConsumingTable = if (paidOrdersInPeriod.isNotEmpty()) {
            paidOrdersInPeriod.groupBy { it.tableNumber }
                .mapValues { entry -> entry.value.sumOf { it.totalAmount } }
                .maxByOrNull { it.value }
                ?.toPair()
        } else null

        DetailedSalesReportData(
            periodName = period,
            startDateMs = periodStartMs,
            endDateMs = now,
            itemsByCategory = groupedMap,
            totalProductsSold = totalProductsSold,
            totalRevenue = totalRevenue,
            averageTicket = averageTicket,
            orderCount = orderCount,
            mostSoldProduct = mostSoldProduct,
            leastSoldProduct = leastSoldProduct,
            peakSalesHour = peakSalesHour,
            topSellingWaiter = topSellingWaiter,
            topConsumingTable = topConsumingTable
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DetailedSalesReportData())

    // --- THEMES & WEB PERSONALIZATION ---
    val allThemes: StateFlow<List<ThemeConfigEntity>> = repository.allThemes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeTheme: StateFlow<ThemeConfigEntity?> = repository.activeTheme
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun saveTheme(
        id: Long,
        themeName: String,
        primaryColorHex: String,
        secondaryColorHex: String,
        bannerImageUrl: String,
        welcomeMessage: String,
        isActive: Boolean = false,
        onComplete: () -> Unit = {}
    ) {
        viewModelScope.launch {
            val theme = ThemeConfigEntity(
                id = id,
                themeName = themeName.trim(),
                primaryColorHex = primaryColorHex.trim(),
                secondaryColorHex = secondaryColorHex.trim(),
                bannerImageUrl = bannerImageUrl.trim(),
                welcomeMessage = welcomeMessage.trim(),
                isActive = isActive,
                updatedAt = System.currentTimeMillis()
            )
            repository.saveTheme(theme)
            logAuditEvent("CONFIG_TEMA_WEB", "Tema web '$themeName' guardado (Activo: $isActive) y sincronizado con Firestore.")
            onComplete()
        }
    }

    fun activateTheme(themeId: Long, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.activateTheme(themeId)
            val theme = allThemes.value.find { it.id == themeId }
            logAuditEvent("ACTIVAR_TEMA_WEB", "Tema web '${theme?.themeName ?: themeId}' activado para el Menú Web en tiempo real.")
            onComplete()
        }
    }

    fun deleteTheme(themeId: Long, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            val theme = allThemes.value.find { it.id == themeId }
            repository.deleteTheme(themeId)
            logAuditEvent("ELIMINAR_TEMA_WEB", "Tema '${theme?.themeName ?: themeId}' eliminado.")
            onComplete()
        }
    }

    // --- WEB ORDERS (PEDIDOS WEB / QR / WHATSAPP) ---
    val allWebOrders: StateFlow<List<WebOrderEntity>> = repository.allWebOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pendingWebOrders: StateFlow<List<WebOrderEntity>> = repository.pendingWebOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeWebOrders: StateFlow<List<WebOrderEntity>> = repository.activeWebOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val newPendingWebOrderAlert: StateFlow<WebOrderEntity?> = repository.newPendingWebOrderAlert

    fun clearPendingWebOrderAlert() {
        repository.clearPendingWebOrderAlert()
    }

    fun validateWebOrderAndSendToKitchen(
        webOrderId: Long,
        assignedWaiter: String = currentUser.value?.name ?: "Mesero Web",
        onSuccess: (Long) -> Unit = {}
    ) {
        viewModelScope.launch {
            repository.validateAndTransferWebOrderToKitchen(
                webOrderId = webOrderId,
                assignedWaiter = assignedWaiter,
                onSuccess = { posOrderId ->
                    logAuditEvent("VALIDAR_PEDIDO_WEB", "Pedido Web #$webOrderId validado y transferido a Cocina como Pedido POS #$posOrderId")
                    onSuccess(posOrderId)
                }
            )
        }
    }

    fun approveWebOrder(
        webOrderId: Long,
        assignedWaiter: String = currentUser.value?.name ?: "Mesero/Caja",
        onSuccess: (Long) -> Unit = {}
    ) {
        validateWebOrderAndSendToKitchen(webOrderId, assignedWaiter, onSuccess)
    }

    fun rejectWebOrder(
        webOrderId: Long,
        reason: String = "Rechazado por Mesero / Caja",
        onComplete: () -> Unit = {}
    ) {
        viewModelScope.launch {
            repository.rejectWebOrder(webOrderId, reason)
            logAuditEvent("RECHAZAR_PEDIDO_WEB", "Pedido Web #$webOrderId rechazado y cancelado de la cola.")
            onComplete()
        }
    }

    fun updateWebOrderStatus(webOrderId: Long, status: String) {
        viewModelScope.launch {
            repository.updateWebOrderStatus(webOrderId, status)
            logAuditEvent("ESTADO_PEDIDO_WEB", "Pedido Web #$webOrderId cambiado a estado '$status'")
        }
    }

    fun deleteWebOrder(webOrderId: Long) {
        viewModelScope.launch {
            repository.deleteWebOrder(webOrderId)
            logAuditEvent("ELIMINAR_PEDIDO_WEB", "Pedido Web #$webOrderId descartado.")
        }
    }

    // Menu Item CRUD & Visual Editor
    fun saveMenuItem(
        id: Long,
        name: String,
        category: String,
        price: Double,
        description: String,
        imageUrl: String = "",
        isAvailable: Boolean = true,
        isVisibleWeb: Boolean = true
    ) {
        viewModelScope.launch {
            val item = MenuItemEntity(
                id = id,
                name = name.trim(),
                category = category.trim(),
                price = price,
                description = description.trim(),
                imageUrl = imageUrl.trim(),
                isAvailable = isAvailable,
                isVisibleWeb = isVisibleWeb
            )
            if (id == 0L) {
                repository.insertMenuItem(item)
                logAuditEvent("CREAR_PRODUCTO", "Producto '${item.name}' agregado al menú (Q${item.price}, Web: $isVisibleWeb)")
            } else {
                repository.updateMenuItem(item)
                logAuditEvent("EDITAR_PRODUCTO", "Producto '${item.name}' actualizado (Q${item.price}, Disponible: $isAvailable, Web: $isVisibleWeb)")
            }
        }
    }

    fun toggleMenuItemAvailability(item: MenuItemEntity) {
        viewModelScope.launch {
            val updated = item.copy(isAvailable = !item.isAvailable)
            repository.updateMenuItem(updated)
            logAuditEvent("DISPONIBILIDAD_PRODUCTO", "Producto '${item.name}' marcado como ${if (updated.isAvailable) "DISPONIBLE" else "AGOTADO"}")
        }
    }

    fun toggleMenuItemVisibilityWeb(item: MenuItemEntity) {
        viewModelScope.launch {
            val updated = item.copy(isVisibleWeb = !item.isVisibleWeb)
            repository.updateMenuItem(updated)
            logAuditEvent("VISIBILIDAD_WEB_PRODUCTO", "Producto '${item.name}' ${if (updated.isVisibleWeb) "MOSTRADO EN MENÚ WEB" else "OCULTO DE MENÚ WEB"}")
        }
    }

    fun deleteMenuItem(id: Long) {
        viewModelScope.launch {
            val item = allMenuItems.value.find { it.id == id }
            repository.deleteMenuItem(id)
            logAuditEvent("ELIMINAR_PRODUCTO", "Producto '${item?.name ?: id}' eliminado del menú.")
        }
    }

    // Inventory CRUD
    fun saveInventoryItem(
        id: Long,
        name: String,
        stock: Double,
        minStock: Double,
        idealStock: Double = minStock * 2.5,
        unitCost: Double = 0.0,
        supplier: String = "Distribuidora Rivera",
        unit: String = "unidad",
        expirationDate: Long? = null
    ) {
        viewModelScope.launch {
            val item = InventoryItemEntity(
                id = id,
                productName = name,
                currentStock = stock,
                minStock = minStock,
                idealStock = idealStock,
                unitCost = unitCost,
                supplier = supplier,
                unit = unit,
                expirationDate = expirationDate
            )
            if (id == 0L) {
                repository.insertInventory(item)
            } else {
                repository.updateInventory(item)
            }
        }
    }

    // --- RECIPES (BILL OF MATERIALS) & MOVEMENTS STATE ---
    val allRecipeItems: StateFlow<List<RecipeItemEntity>> = repository.allRecipeItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allInventoryMovements: StateFlow<List<InventoryMovementEntity>> = repository.allInventoryMovements
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun registerManualMovement(
        ingredientId: Long,
        type: String,
        quantity: Double,
        reason: String,
        userName: String
    ) {
        viewModelScope.launch {
            repository.registerManualInventoryMovement(
                ingredientId = ingredientId,
                type = type,
                quantity = quantity,
                reason = reason,
                userName = userName
            )
        }
    }

    fun saveRecipeItem(
        id: Long,
        menuItemId: Long,
        ingredientId: Long,
        ingredientName: String,
        quantityRequired: Double,
        unit: String
    ) {
        viewModelScope.launch {
            repository.saveRecipeItem(
                RecipeItemEntity(
                    id = id,
                    menuItemId = menuItemId,
                    ingredientId = ingredientId,
                    ingredientName = ingredientName,
                    quantityRequired = quantityRequired,
                    unit = unit
                )
            )
        }
    }

    fun deleteRecipeItem(id: Long) {
        viewModelScope.launch {
            repository.deleteRecipeItem(id)
        }
    }

    fun deleteInventoryItem(id: Long) {
        viewModelScope.launch {
            repository.deleteInventory(id)
        }
    }

    fun cancelOrder(orderId: Long) {
        viewModelScope.launch {
            repository.cancelOrder(orderId)
        }
    }

    fun deleteOrder(orderId: Long) {
        viewModelScope.launch {
            repository.deleteOrderAndDetails(orderId)
        }
    }

    suspend fun getOrderItems(orderId: Long): List<com.example.data.entity.OrderItemEntity> {
        return repository.getOrderItems(orderId)
    }

    // --- DAILY CLOSES & SYSTEM RESET MAINTENANCE ---
    fun performDailyClose(onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val user = currentUser.value?.name ?: "Gerente Principal"
            repository.performDailyClose(
                user = user,
                deviceId = deviceId.value,
                branchName = systemSettings.value.branchName,
                onResult = onResult
            )
        }
    }

    fun startNewJornada(onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val user = currentUser.value?.name ?: "Gerente Principal"
            repository.startNewJornada(
                user = user,
                deviceId = deviceId.value,
                onResult = onResult
            )
        }
    }

    fun resetOperationalSystem(onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val user = currentUser.value?.name ?: "Gerente Principal"
            repository.resetOperationalSystem(
                user = user,
                deviceId = deviceId.value,
                onResult = onResult
            )
        }
    }

    fun performFactoryResetTotalSystem(
        reason: String,
        createBackup: Boolean,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            val user = currentUser.value?.name ?: "Gerente Principal"
            // Clear persistent local session
            prefs.edit().clear().apply()
            _currentRole.value = MainRole.INICIO
            _isManagerUnlocked.value = false
            _currentUser.value = null

            repository.performFactoryResetTotalSystem(
                user = user,
                reason = reason,
                createBackup = createBackup,
                deviceId = deviceId.value,
                onResult = onResult
            )
        }
    }

    fun logFailedPinAttempt(
        user: String = currentUser.value?.name ?: "Gerente",
        role: String = "GERENTE",
        actionContext: String = "Autorización de Acción Crítica"
    ) {
        viewModelScope.launch {
            repository.logAudit(
                user = user,
                role = role,
                deviceId = deviceId.value,
                action = "INTENTO_PIN_FALLIDO",
                details = "Intento fallido de PIN numérico para '$actionContext' desde dispositivo ${deviceId.value}"
            )
        }
    }

    fun logAuditEvent(action: String, details: String) {
        viewModelScope.launch {
            repository.logAudit(
                user = currentUser.value?.name ?: "Gerente Principal",
                role = currentUser.value?.role ?: "GERENTE",
                deviceId = deviceId.value,
                action = action,
                details = details
            )
        }
    }

    // --- TABLES MANAGEMENT (ADMINISTRACIÓN DE MESAS) ---
    val allTables: StateFlow<List<TableEntity>> = repository.allTables
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeTables: StateFlow<List<TableEntity>> = repository.activeTables
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addTable(tableNumber: String, capacity: Int = 4, status: String = "Disponible", onResult: (Boolean, String) -> Unit = { _, _ -> }) {
        viewModelScope.launch {
            if (tableNumber.isBlank()) {
                onResult(false, "El nombre o número de mesa no puede estar vacío.")
                return@launch
            }
            val existing = allTables.value.find { it.tableNumber.equals(tableNumber.trim(), ignoreCase = true) }
            if (existing != null) {
                onResult(false, "Ya existe una mesa con el nombre '${tableNumber.trim()}'.")
                return@launch
            }
            val nextDisplayOrder = (allTables.value.maxOfOrNull { it.displayOrder } ?: 0) + 1
            repository.insertTable(
                TableEntity(
                    tableNumber = tableNumber.trim(),
                    capacity = capacity,
                    status = status,
                    isActive = true,
                    displayOrder = nextDisplayOrder
                )
            )
            logAuditEvent("AGREGAR_MESA", "Nueva mesa creada: ${tableNumber.trim()} (Capacidad: $capacity)")
            onResult(true, "Mesa '${tableNumber.trim()}' creada exitosamente.")
        }
    }

    fun updateTable(table: TableEntity, onResult: (Boolean, String) -> Unit = { _, _ -> }) {
        viewModelScope.launch {
            val updated = if (table.status == "Ocupada" && table.occupiedSince == null) {
                table.copy(occupiedSince = System.currentTimeMillis())
            } else if (table.status != "Ocupada") {
                table.copy(occupiedSince = null)
            } else {
                table
            }
            repository.updateTable(updated)
            logAuditEvent("ACTUALIZAR_MESA", "Mesa ${table.tableNumber} actualizada (Estado: ${table.status}, Capacidad: ${table.capacity})")
            onResult(true, "Mesa '${table.tableNumber}' actualizada.")
        }
    }

    fun moveTableOrder(table: TableEntity, moveUp: Boolean) {
        viewModelScope.launch {
            val list = allTables.value.sortedWith(compareBy({ it.displayOrder }, { it.id }))
            val index = list.indexOfFirst { it.id == table.id }
            if (index == -1) return@launch
            val targetIndex = if (moveUp) index - 1 else index + 1
            if (targetIndex in list.indices) {
                val currentTable = list[index]
                val targetTable = list[targetIndex]

                val currentOrder = currentTable.displayOrder
                val targetOrder = targetTable.displayOrder

                val newCurrentOrder = if (currentOrder == targetOrder) {
                    if (moveUp) targetOrder - 1 else targetOrder + 1
                } else targetOrder

                repository.updateTable(currentTable.copy(displayOrder = newCurrentOrder))
                repository.updateTable(targetTable.copy(displayOrder = currentOrder))
            }
        }
    }

    fun updateTableStatus(tableId: Long, newStatus: String) {
        viewModelScope.launch {
            val table = allTables.value.find { it.id == tableId } ?: return@launch
            val occupiedSince = if (newStatus == "Ocupada") System.currentTimeMillis() else null
            repository.updateTable(table.copy(status = newStatus, occupiedSince = occupiedSince))
            logAuditEvent("ESTADO_MESA", "Estado de mesa '${table.tableNumber}' cambiado a $newStatus")
        }
    }

    fun releaseTableService(tableName: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val orders = repository.allOrders.firstOrNull() ?: emptyList()
            val activeOrdersForTable = orders.filter {
                it.tableNumber.equals(tableName, ignoreCase = true) &&
                        it.status != "PAGADO" && it.status != "CANCELADO"
            }

            if (activeOrdersForTable.isNotEmpty()) {
                onResult(false, "Hay consumos pendientes de cobro antes de liberar la mesa")
                return@launch
            }

            val tableEntity = allTables.value.find { it.tableNumber.equals(tableName, ignoreCase = true) }
            if (tableEntity != null) {
                repository.updateTable(tableEntity.copy(status = "Disponible", occupiedSince = null))
            }

            if (_selectedTable.value.equals(tableName, ignoreCase = true)) {
                val otherAvailable = allTables.value.firstOrNull {
                    !it.tableNumber.equals(tableName, ignoreCase = true) && it.isActive
                }?.tableNumber ?: "Mesa 1"
                _selectedTable.value = otherAvailable
            }

            logAuditEvent("LIBERAR_MESA", "Servicio finalizado y mesa '$tableName' liberada exitosamente.")
            onResult(true, "Servicio finalizado. Mesa '$tableName' liberada.")
        }
    }

    fun addConsecutiveTable(onResult: (Boolean, String) -> Unit = { _, _ -> }) {
        viewModelScope.launch {
            val tables = allTables.value
            var maxNum = 0
            tables.forEach { table ->
                val num = table.tableNumber.replace(Regex("[^0-9]"), "").toIntOrNull()
                if (num != null && num > maxNum) {
                    maxNum = num
                }
            }
            if (maxNum == 0) maxNum = tables.size

            val nextNum = maxNum + 1
            val newTableNumber = "Mesa $nextNum"
            val nextDisplayOrder = (tables.maxOfOrNull { it.displayOrder } ?: 0) + 1

            repository.insertTable(
                TableEntity(
                    tableNumber = newTableNumber,
                    capacity = 4,
                    status = "Disponible",
                    isActive = true,
                    displayOrder = nextDisplayOrder
                )
            )
            logAuditEvent("AGREGAR_MESA", "Nueva mesa consecutiva creada: $newTableNumber")
            onResult(true, "Mesa '$newTableNumber' creada exitosamente.")
        }
    }

    fun deleteTable(table: TableEntity, onError: (String) -> Unit = {}, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            // Validate if table has active order
            val currentKitchenOrders = repository.kitchenOrders.firstOrNull() ?: emptyList()
            val hasActiveOrder = currentKitchenOrders.any {
                it.tableNumber.equals(table.tableNumber, ignoreCase = true) && it.status in listOf("PENDIENTE", "EN_PROCESO", "FINALIZADO")
            }
            if (hasActiveOrder) {
                onError("No se puede eliminar porque tiene pedidos pendientes.")
                return@launch
            }
            repository.deleteTable(table.id)
            logAuditEvent("ELIMINAR_MESA", "Mesa ${table.tableNumber} eliminada. Historial de pedidos conservado.")
            onSuccess()
        }
    }

    fun setTotalTablesCount(targetCount: Int, onResult: (Boolean, String) -> Unit = { _, _ -> }) {
        viewModelScope.launch {
            repository.setTotalTablesCount(targetCount) { success, message ->
                if (success) {
                    logAuditEvent("AJUSTAR_TOTAL_MESAS", "Total de mesas ajustado a $targetCount por el Gerente.")
                }
                onResult(success, message)
            }
        }
    }


    // --- INVOICES (FACTURACIÓN PROFESIONAL) ---
    val allInvoices: StateFlow<List<InvoiceEntity>> = repository.allInvoices
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun createAndSaveInvoice(
        order: OrderEntity,
        items: List<OrderItemEntity>,
        customerType: String = "Consumidor Final",
        customerName: String = "",
        customerPhone: String = "",
        discount: Double = 0.0,
        onComplete: (InvoiceEntity) -> Unit
    ) {
        viewModelScope.launch {
            val settings = systemSettings.value
            val nextNum = settings.nextInvoiceNumber
            val prefix = settings.ticketPrefix.ifBlank { "FAC-" }
            val formattedInvoiceNum = prefix + nextNum.toString().padStart(6, '0')

            val total = (order.totalAmount - discount).coerceAtLeast(0.0)

            val invoice = InvoiceEntity(
                invoiceNumber = formattedInvoiceNum,
                orderId = order.id,
                orderNumber = order.orderNumber,
                cashierName = order.cashierName ?: currentUser.value?.name ?: "Cajero",
                paymentMethod = order.paymentMethod ?: "Efectivo",
                customerType = customerType,
                customerName = customerName.trim(),
                customerNit = "",
                customerPhone = customerPhone.trim(),
                subtotal = total,
                discount = discount,
                totalAmount = total,
                timestamp = System.currentTimeMillis(),
                restaurantName = settings.restaurantName,
                branchName = settings.branchName,
                restaurantTaxId = settings.taxId,
                restaurantAddress = settings.address,
                restaurantPhone = settings.phone,
                restaurantEmail = settings.email,
                footerMessage = settings.footerMessage,
                legalNotice = settings.legalNotice,
                currencySymbol = settings.currencySymbol,
                paperWidthMm = settings.paperWidthMm,
                logoUri = settings.logoUri,
                waiterName = order.waiterName,
                tableNumber = order.tableNumber,
                ticketDividerStyle = settings.ticketDividerStyle,
                showLogo = settings.showLogo,
                showRestaurantName = settings.showRestaurantName,
                showBranchName = settings.showBranchName,
                showTaxId = settings.showTaxId,
                showAddress = settings.showAddress,
                showPhone = settings.showPhone,
                showEmail = settings.showEmail,
                showInvoiceNumber = settings.showInvoiceNumber,
                showOrderNumber = settings.showOrderNumber,
                showDate = settings.showDate,
                showTime = settings.showTime,
                showCashierName = settings.showCashierName,
                showWaiterName = settings.showWaiterName,
                showTableNumber = settings.showTableNumber,
                showPaymentMethod = settings.showPaymentMethod,
                showCustomerType = settings.showCustomerType,
                showCustomerNit = settings.showCustomerNit,
                showCustomerPhone = settings.showCustomerPhone,
                showQuantity = settings.showQuantity,
                showProductName = settings.showProductName,
                showUnitPrice = settings.showUnitPrice,
                showSubtotal = settings.showSubtotal,
                showTotal = settings.showTotal,
                showTaxBreakdown = settings.showTaxBreakdown,
                showTipLine = settings.showTipLine,
                showLegalNotice = settings.showLegalNotice,
                showFooterMessage = settings.showFooterMessage,
                showQrCode = settings.showQrCode,
                showPrintTimestamp = settings.showPrintTimestamp
            )

            val id = repository.insertInvoice(invoice)
            val savedInvoice = invoice.copy(id = id)

            // Increment consecutive invoice number in settings
            saveSystemSettings(settings.copy(nextInvoiceNumber = nextNum + 1))

            logAuditEvent("GENERAR_FACTURA", "Factura $formattedInvoiceNum generada para Pedido #${order.orderNumber} por ${savedInvoice.totalAmount} Quetzales.")
            onComplete(savedInvoice)
        }
    }

    fun updateInvoice(invoice: InvoiceEntity, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.updateInvoice(invoice)
            logAuditEvent("EDITAR_FACTURA", "Factura ${invoice.invoiceNumber} modificada manualmente.")
            onComplete()
        }
    }

    fun updateBillingSettings(
        restaurantName: String,
        branchName: String = systemSettings.value.branchName,
        address: String,
        phone: String,
        email: String,
        taxId: String,
        footerMessage: String,
        legalNotice: String = systemSettings.value.legalNotice,
        nextInvoiceNumber: Long,
        taxPercent: Double,
        currencySymbol: String,
        paperWidthMm: Int,
        ticketPrefix: String = systemSettings.value.ticketPrefix,
        ticketDividerStyle: String = systemSettings.value.ticketDividerStyle,
        logoUri: String = systemSettings.value.logoUri,
        showLogo: Boolean = systemSettings.value.showLogo,
        showRestaurantName: Boolean = systemSettings.value.showRestaurantName,
        showBranchName: Boolean = systemSettings.value.showBranchName,
        showTaxId: Boolean = systemSettings.value.showTaxId,
        showAddress: Boolean = systemSettings.value.showAddress,
        showPhone: Boolean = systemSettings.value.showPhone,
        showEmail: Boolean = systemSettings.value.showEmail,
        showInvoiceNumber: Boolean = systemSettings.value.showInvoiceNumber,
        showOrderNumber: Boolean = systemSettings.value.showOrderNumber,
        showDate: Boolean = systemSettings.value.showDate,
        showTime: Boolean = systemSettings.value.showTime,
        showCashierName: Boolean = systemSettings.value.showCashierName,
        showWaiterName: Boolean = systemSettings.value.showWaiterName,
        showTableNumber: Boolean = systemSettings.value.showTableNumber,
        showPaymentMethod: Boolean = systemSettings.value.showPaymentMethod,
        showCustomerType: Boolean = systemSettings.value.showCustomerType,
        showCustomerNit: Boolean = systemSettings.value.showCustomerNit,
        showCustomerPhone: Boolean = systemSettings.value.showCustomerPhone,
        showQuantity: Boolean = systemSettings.value.showQuantity,
        showProductName: Boolean = systemSettings.value.showProductName,
        showUnitPrice: Boolean = systemSettings.value.showUnitPrice,
        showSubtotal: Boolean = systemSettings.value.showSubtotal,
        showTotal: Boolean = systemSettings.value.showTotal,
        showTaxBreakdown: Boolean = systemSettings.value.showTaxBreakdown,
        showTipLine: Boolean = systemSettings.value.showTipLine,
        showLegalNotice: Boolean = systemSettings.value.showLegalNotice,
        showFooterMessage: Boolean = systemSettings.value.showFooterMessage,
        showQrCode: Boolean = systemSettings.value.showQrCode,
        showPrintTimestamp: Boolean = systemSettings.value.showPrintTimestamp,
        onComplete: () -> Unit = {}
    ) {
        viewModelScope.launch {
            val current = systemSettings.value
            val updated = current.copy(
                restaurantName = restaurantName,
                branchName = branchName,
                address = address,
                phone = phone,
                email = email,
                taxId = taxId,
                footerMessage = footerMessage,
                legalNotice = legalNotice,
                nextInvoiceNumber = nextInvoiceNumber,
                taxPercent = taxPercent,
                currencySymbol = currencySymbol,
                paperWidthMm = paperWidthMm,
                ticketPrefix = ticketPrefix,
                ticketDividerStyle = ticketDividerStyle,
                logoUri = logoUri,
                showLogo = showLogo,
                showRestaurantName = showRestaurantName,
                showBranchName = showBranchName,
                showTaxId = showTaxId,
                showAddress = showAddress,
                showPhone = showPhone,
                showEmail = showEmail,
                showInvoiceNumber = showInvoiceNumber,
                showOrderNumber = showOrderNumber,
                showDate = showDate,
                showTime = showTime,
                showCashierName = showCashierName,
                showWaiterName = showWaiterName,
                showTableNumber = showTableNumber,
                showPaymentMethod = showPaymentMethod,
                showCustomerType = showCustomerType,
                showCustomerNit = showCustomerNit,
                showCustomerPhone = showCustomerPhone,
                showQuantity = showQuantity,
                showProductName = showProductName,
                showUnitPrice = showUnitPrice,
                showSubtotal = showSubtotal,
                showTotal = showTotal,
                showTaxBreakdown = showTaxBreakdown,
                showTipLine = showTipLine,
                showLegalNotice = showLegalNotice,
                showFooterMessage = showFooterMessage,
                showQrCode = showQrCode,
                showPrintTimestamp = showPrintTimestamp
            )
            saveSystemSettings(updated, onComplete)
            logAuditEvent("CONFIG_FACTURACION", "Configuración de datos fiscales y personalización de factura actualizada.")
        }
    }

    // --- REPARTIDOR (DELIVERY) MODULE STATE & ACTIONS ---
    private val _deliveryTab = MutableStateFlow(DeliveryTab.LISTOS)
    val deliveryTab: StateFlow<DeliveryTab> = _deliveryTab.asStateFlow()
    fun setDeliveryTab(tab: DeliveryTab) { _deliveryTab.value = tab }

    private val _deliveryDriverName = MutableStateFlow("Héctor Soto")
    val deliveryDriverName: StateFlow<String> = _deliveryDriverName.asStateFlow()
    fun setDeliveryDriverName(name: String) { _deliveryDriverName.value = name }

    val allDeliveryOrders: StateFlow<List<WebOrderEntity>> = repository.allDeliveryOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val readyDeliveryOrders: StateFlow<List<WebOrderEntity>> = repository.readyDeliveryOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val inTransitDeliveryOrders: StateFlow<List<WebOrderEntity>> = repository.inTransitDeliveryOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val completedDeliveryOrders: StateFlow<List<WebOrderEntity>> = repository.completedDeliveryOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val incidentDeliveryOrders: StateFlow<List<WebOrderEntity>> = repository.incidentDeliveryOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allDeliverySettlements: StateFlow<List<DeliverySettlementEntity>> = repository.allDeliverySettlements
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val todayDeliverySettlements: StateFlow<List<DeliverySettlementEntity>> = repository.todayDeliverySettlements
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun startDelivery(webOrderId: Long, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            val driver = currentUser.value?.name ?: _deliveryDriverName.value
            repository.startDelivery(webOrderId, driver)
            onSuccess()
        }
    }

    fun completeDelivery(webOrderId: Long, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            val driver = currentUser.value?.name ?: _deliveryDriverName.value
            repository.completeDelivery(webOrderId, driver)
            onSuccess()
        }
    }

    fun reportDeliveryIncident(webOrderId: Long, incidentNote: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            val driver = currentUser.value?.name ?: _deliveryDriverName.value
            repository.reportDeliveryIncident(webOrderId, driver, incidentNote)
            onSuccess()
        }
    }
}
