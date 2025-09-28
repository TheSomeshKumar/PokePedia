package com.thesomeshkumar.pokepedia.core.presentation

import java.awt.Desktop
import java.net.URI

/**
 * Created by SixFlags on 23 September, 2025.
 * Copyright ©2025 SixFlags. All rights reserved.
 */

class JvmUrlLauncher : UrlLauncher {

    override suspend fun openUrl(url: String): Boolean {
        return try {
            if (Desktop.isDesktopSupported()) {
                val desktop = Desktop.getDesktop()
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(URI(url))
                    true
                } else {
                    // Fallback for Linux systems without desktop integration
                    val runtime = Runtime.getRuntime()
                    val commands = arrayOf(
                        "xdg-open",
                        url
                    )
                    runtime.exec(commands)
                    true
                }
            } else {
                false
            }
        } catch (e: Exception) {
            // Try alternative methods
            try {
                val runtime = Runtime.getRuntime()
                val os = System
                    .getProperty("os.name")
                    .lowercase()
                when {
                    os.contains("win") -> {
                        runtime.exec(
                            arrayOf(
                                "rundll32",
                                "url.dll,FileProtocolHandler",
                                url
                            )
                        )
                    }

                    os.contains("mac") -> {
                        runtime.exec(
                            arrayOf(
                                "open",
                                url
                            )
                        )
                    }

                    else -> {
                        runtime.exec(
                            arrayOf(
                                "xdg-open",
                                url
                            )
                        )
                    }
                }
                true
            } catch (e2: Exception) {
                false
            }
        }
    }

    override suspend fun openYouTubeVideo(videoKey: String): Boolean {
        // On desktop, always open in browser
        return openUrl("https://www.youtube.com/watch?v=$videoKey")
    }
}

actual fun getUrlLauncher(): UrlLauncher = JvmUrlLauncher()
