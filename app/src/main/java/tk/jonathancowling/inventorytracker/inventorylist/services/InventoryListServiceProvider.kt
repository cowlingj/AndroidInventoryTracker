package tk.jonathancowling.inventorytracker.inventorylist.services

import io.reactivex.disposables.Disposable
import tk.jonathancowling.inventorytracker.settings.SettingsRepository

internal class InventoryListServiceProvider(settings: SettingsRepository,
                                            private val choices: Map<Choice, (settings: SettingsRepository.SettingsModel)->InventoryListService>,
                                            private val default: ()->InventoryListService) {

    private var currentSettings: SettingsRepository.SettingsModel? = null
    private val disposable: Disposable

    init {
        disposable = settings.settings.subscribe({
            currentSettings = it
        }, {
            currentSettings = null
        })
    }

    fun get(): InventoryListService = (currentSettings?.let{
        choices[choose(it)]?.invoke(it)
    }?:default())


    private fun choose(settings: SettingsRepository.SettingsModel): Choice {
        return if (settings.useCloudSync) {
            Choice.API
        } else {
            Choice.EPHEMERAL
        }
    }

    fun close() {
        disposable.dispose()
    }

    enum class Choice {
        API,
        EPHEMERAL
    }
}