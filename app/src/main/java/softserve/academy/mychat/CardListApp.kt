package softserve.academy.mychat

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

class CardListApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CardListApp)
            modules(appModule)
        }
    }
}

val appModule = module {
    single<StorageService> { FileStorageService(androidContext()) }
    viewModel { CardListViewModel(get()) }
}