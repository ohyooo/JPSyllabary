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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.ohyooo.shared.common.Text
import com.ohyooo.shared.generated.resources.Res
import com.ohyooo.shared.generated.resources.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun Main() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scaffoldState = rememberScaffoldState(drawerState)
    val scope = rememberCoroutineScope()

    var route by rememberSaveable { mutableStateOf(Route.SINGLE) }

    Scaffold(
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(64.dp))
                DrawerNavItem(stringRes = Res.string.single, route = Route.SINGLE, icon = Icons.Filled.Home, scope, scaffoldState) { route = Route.SINGLE }
                DrawerNavItem(stringRes = Res.string.table, route = Route.TABLE, icon = Icons.AutoMirrored.Filled.ListAlt, scope, scaffoldState) { route = Route.TABLE }
                DrawerNavItem(stringRes = Res.string.twister, route = Route.Twister, icon = Icons.Filled.ChangeHistory, scope, scaffoldState) { route = Route.Twister }
                DrawerNavItem(stringRes = Res.string.source_code, route = Route.SOURCE, icon = Icons.Filled.DataObject, scope, scaffoldState) { route = Route.SOURCE }
            }
        },
        drawerGesturesEnabled = false,
        drawerShape = MaterialTheme.shapes.extraSmall,
        drawerElevation = 2.dp,
        scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.colorScheme.background,
        drawerBackgroundColor = Color.Transparent,
    ) { innerPaddingModifier ->

        Nav(route, modifier = Modifier.padding(innerPaddingModifier)) {
            scope.launch { drawerState.open() }
        }

        // Nav(navController = navController, modifier = Modifier.padding(innerPaddingModifier)) {
        //     scope.launch { drawerState.open() }
        // }

    }
}

@Composable
fun DrawerNavItem(
    stringRes: StringResource,
    route: Route,
    icon: ImageVector,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    onClick: () -> Unit
) {
    // val selected = navController.currentBackStackEntryAsState().value?.destination?.route == route.value

    NavigationDrawerItem(
        label = { Text(text = stringResource(stringRes), color = MaterialTheme.colorScheme.inverseSurface) },
        icon = { Icon(imageVector = icon, contentDescription = stringResource(stringRes), tint = MaterialTheme.colorScheme.inverseSurface) },
        selected = false,
        onClick = {
            // navController.navigate(route.value)
            scope.launch { scaffoldState.drawerState.close() }
            onClick()
        },
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
}

@Composable
fun Nav(route: Route, modifier: Modifier, onMenuClick: () -> Unit = {}) {
    when (route) {
        Route.SINGLE -> Single(onMenuClick)
        Route.TABLE -> Fragment(onMenuClick)
        Route.Twister -> Twister(onMenuClick)
        Route.SOURCE -> Github(onMenuClick)
    }
}

enum class Route(val value: String) {
    SINGLE("Single"),
    TABLE("Table"),
    Twister("Twister"),
    SOURCE("Source"),
}