package id.dev.core.data.themes

import id.dev.core.domain.ThemesInfo

fun ThemesInfo.toThemesInfoSerializable(): ThemesInfoSerializable {
    return ThemesInfoSerializable(
        isDarkMode = isDarkMode
    )
}

fun ThemesInfoSerializable.toThemesInfo(): ThemesInfo {
    return ThemesInfo(
        isDarkMode = isDarkMode
    )
}