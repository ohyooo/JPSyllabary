package com.ohyooo.shared.model

/**
 * Top-level destinations shown in the drawer.
 *
 * [value] is a stable route name that can be reused if a navigation library is
 * introduced later.
 */
enum class Route(val value: String) {
    SINGLE("Single"),
    TABLE("Table"),
    Twister("Twister"),
    SOURCE("Source"),
}
