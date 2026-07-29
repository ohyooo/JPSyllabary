package com.ohyooo.shared

import org.jetbrains.compose.resources.FontResource

actual fun getPlatformName() = "iOS Main"

actual fun openGitHub() {
    println(getPlatformName())
}

actual fun getFont(): FontResource? = null
