package pl.polsl.laboratorioweobecnosci.preferences

/**
 * Typy autoryzacji
 * @param value wartość przypisana do danego typu
 */
enum class AuthorizationMode(val value: Int) {
    NONE(0),
    PIN(1),
    FINGERPRINT(2),
    PASSWORD(3);

    companion object {
        /**
         * funkcja ustawiająca odpowieni typ autoryzacji
         * @param value wartość przypisana do danego typu
         */
        fun fromInt(value: Int) = values().first { it.value == value }
    }
}