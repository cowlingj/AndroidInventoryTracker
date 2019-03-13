package tk.jonathancowling.inventorytracker.util

import java.lang.RuntimeException

abstract class ValidationException(reason : String = "validation failed") : RuntimeException(reason)