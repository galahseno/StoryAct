package id.dev.storyact

import android.app.Application
import android.content.Context
import com.google.android.play.core.splitcompat.SplitCompat
import id.dev.auth.data.di.authDataModule
import id.dev.auth.presentation.di.authViewModelModule
import id.dev.core.data.di.coreDataModule
import id.dev.core.database.di.databaseModule
import id.dev.maps.presentation.di.mapsViewModelModule
import id.dev.post_story.data.di.postStoryDataModule
import id.dev.post_story.presentation.di.postStoryViewModelModule
import id.dev.profile.di.profileDataModule
import id.dev.profile.presentation.di.profileViewModelModule
import id.dev.story.data.di.storyDataModule
import id.dev.story.presentation.di.storyViewModelModule
import id.dev.storyact.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class StoryActApp: Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@StoryActApp)
            modules(
                appModule,
                authViewModelModule,
                authDataModule,
                coreDataModule,
                databaseModule,
                storyDataModule,
                storyViewModelModule,
                profileDataModule,
                profileViewModelModule,
                postStoryDataModule,
                postStoryViewModelModule,
                mapsViewModelModule,
            )
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        SplitCompat.install(this)
    }
}