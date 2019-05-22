package tk.jonathancowling.inventorytracker.settings

import dagger.Component
import tk.jonathancowling.inventorytracker.AndroidComponent
import tk.jonathancowling.inventorytracker.authentication.services.AuthComponent

@SettingsScope
@Component(modules = [SettingsModule::class], dependencies = [AndroidComponent::class, AuthComponent::class])
interface SettingsComponent {
    fun repository(): SettingsRepository
}