package com.ohyooo.shared.compose

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DrawerValue
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.ChangeHistory
import androidx.compose.material.icons.filled.DataObject
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.ohyooo.shared.common.Text
import com.ohyooo.shared.generated.resources.Res
import com.ohyooo.shared.generated.resources.*
import com.ohyooo.shared.model.Route
import com.ohyooo.shared.viewmodel.AppIntent
import com.ohyooo.shared.viewmodel.AppStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * App shell Composable.
 *
 * It owns the navigation drawer UI and connects it to [AppStore]. Screen content
 * is selected by rendering [Nav] from the current app route state.
 */
@Composable
fun Main(store: AppStore = rememberAppStore()) {
    val uiState by store.state.collectAsState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scaffoldState = rememberScaffoldState(drawerState)
    val scope = rememberCoroutineScope()

    Scaffold(
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(64.dp))
                DrawerNavItem(stringRes = Res.string.single, selected = uiState.route == Route.SINGLE, icon = Icons.Filled.Home, scope, scaffoldState) {
                    store.dispatch(AppIntent.SelectRoute(Route.SINGLE))
                }
                DrawerNavItem(stringRes = Res.string.table, selected = uiState.route == Route.TABLE, icon = Icons.AutoMirrored.Filled.ListAlt, scope, scaffoldState) {
                    store.dispatch(AppIntent.SelectRoute(Route.TABLE))
                }
                DrawerNavItem(stringRes = Res.string.twister, selected = uiState.route == Route.Twister, icon = Icons.Filled.ChangeHistory, scope, scaffoldState) {
                    store.dispatch(AppIntent.SelectRoute(Route.Twister))
                }
                DrawerNavItem(stringRes = Res.string.source_code, selected = uiState.route == Route.SOURCE, icon = Icons.Filled.DataObject, scope, scaffoldState) {
                    store.dispatch(AppIntent.SelectRoute(Route.SOURCE))
                }
            }
        },
        drawerGesturesEnabled = false,
        drawerShape = MaterialTheme.shapes.extraSmall,
        drawerElevation = 2.dp,
        scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.colorScheme.background,
        drawerBackgroundColor = Color.Transparent,
    ) { innerPaddingModifier ->

        Nav(uiState.route, modifier = Modifier.padding(innerPaddingModifier)) {
            scope.launch { drawerState.open() }
        }

        // Nav(navController = navController, modifier = Modifier.padding(innerPaddingModifier)) {
        //     scope.launch { drawerState.open() }
        // }

    }
}

/**
 * Creates the app-level MVI store for this composition and closes it when the
 * app shell leaves composition.
 */
@Composable
private fun rememberAppStore(): AppStore {
    val store = remember { AppStore() }
    DisposableEffect(store) {
        onDispose { store.close() }
    }
    return store
}

/**
 * Drawer row used to select a top-level [Route].
 *
 * Callers pass [selected] from app state and use [onClick] to dispatch the route
 * selection intent.
 */
@Composable
fun DrawerNavItem(
    stringRes: StringResource,
    selected: Boolean,
    icon: ImageVector,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    onClick: () -> Unit
) {
    // val selected = navController.currentBackStackEntryAsState().value?.destination?.route == route.value

    NavigationDrawerItem(
        label = { Text(text = stringResource(stringRes), color = MaterialTheme.colorScheme.inverseSurface) },
        icon = { Icon(imageVector = icon, contentDescription = stringResource(stringRes), tint = MaterialTheme.colorScheme.inverseSurface) },
        selected = selected,
        onClick = {
            // navController.navigate(route.value)
            scope.launch { scaffoldState.drawerState.close() }
            onClick()
        },
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
}

/**
 * Renders the active top-level screen for [route].
 *
 * The menu callback is passed down so every screen can open the same drawer
 * without owning drawer state.
 */
@Composable
fun Nav(route: Route, modifier: Modifier, onMenuClick: () -> Unit = {}) {
    when (route) {
        Route.SINGLE -> Single(onMenuClick)
        Route.TABLE -> Fragment(onMenuClick)
        Route.Twister -> Twister(onMenuClick)
        Route.SOURCE -> Github(onMenuClick)
    }
}
