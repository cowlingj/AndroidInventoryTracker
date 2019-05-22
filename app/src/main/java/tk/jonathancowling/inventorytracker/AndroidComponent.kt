package tk.jonathancowling.inventorytracker

import android.content.Context
import dagger.BindsInstance
import dagger.Component

@Component
interface AndroidComponent {

    fun context(): Context

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(ctx: Context): Builder
        fun build(): AndroidComponent
    }
}