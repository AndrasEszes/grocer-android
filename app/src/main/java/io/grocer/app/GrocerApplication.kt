package io.grocer.app

import android.app.Application

class GrocerApplication : Application() {
    val container: AppContainer by lazy { AppContainer() }
}
