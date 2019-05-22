package tk.jonathancowling.inventorytracker.inventorylist.services

import dagger.Component
import tk.jonathancowling.inventorytracker.settings.SettingsComponent
import javax.inject.Provider

@InventoryListScope
@Component(modules = [InventoryListModule::class], dependencies = [SettingsComponent::class])
interface InventoryListComponent {
    fun service(): Provider<InventoryListService>
}