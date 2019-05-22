package tk.jonathancowling.inventorytracker.authentication.models

class ValidationException(reason : String = "validation failed") : RuntimeException(reason)