package project.movein.backend

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn( ViewModelComponent :: class )
abstract class AppBackendModule {
    @Binds
    abstract fun bindAppBackend(backendImp: BackendImp):AppBackend
}