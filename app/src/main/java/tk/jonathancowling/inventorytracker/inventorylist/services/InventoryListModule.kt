package tk.jonathancowling.inventorytracker.inventorylist.services

import dagger.Module
import dagger.Provides
import tk.jonathancowling.inventorytracker.settings.SettingsRepository
import javax.inject.Provider

@Module
class InventoryListModule {

    @Module
    companion object {

        private val ephemeralService = EphemeralInventoryListService()

        @JvmStatic
        @Provides
        fun service(settingsRepository: Provider<SettingsRepository>): InventoryListService {
            return InventoryListServiceProvider(settingsRepository.get(), mapOf(
                InventoryListServiceProvider.Choice.API to { it ->
                    ApiInventoryListService(it.baseUrl.get(), it.apiKey.get())
                },
                InventoryListServiceProvider.Choice.EPHEMERAL to { _ ->
                    ephemeralService
                }
            )) { ephemeralService }.get()
        }
    }
}