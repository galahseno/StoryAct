package id.dev.core.database.di

import androidx.room.Room
import id.dev.core.database.StoryDb
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    single {
        val passphrase: ByteArray = SQLiteDatabase.getBytes("storyAct".toCharArray())
        val factory = SupportFactory(passphrase)

        Room.databaseBuilder(
            androidApplication(),
            StoryDb::class.java,
            "story.db"
        )
            .openHelperFactory(factory)
            .build()
    }
    single { get<StoryDb>().storyDao() }
    single { get<StoryDb>().remoteKeysDao() }
    single { get<StoryDb>().favoriteDao() }
}