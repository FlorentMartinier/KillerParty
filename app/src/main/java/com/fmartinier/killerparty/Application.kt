package com.fmartinier.killerparty

import android.app.Application
import com.fmartinier.killerparty.module.killerBackModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Application : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@Application)
            modules(killerBackModule)
        }
    }
}