package pl.polsl.laboratorioweobecnosci.preferences

enum class AuthorizationMode(val value: Int) {
    NONE(0),
    PIN(1),
    FINGERPRINT(2);

    companion object {
        fun fromInt(value: Int) = values().first { it.value == value }
    }
}