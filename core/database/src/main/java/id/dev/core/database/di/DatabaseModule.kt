package id.dev.core.database.di

import androidx.room.Room
import id.dev.core.database.StoryDb
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            StoryDb::class.java,
            "story.db"
        ).build()
    }
    single { get<StoryDb>().storyDao() }
    single { get<StoryDb>().remoteKeysDao() }
}