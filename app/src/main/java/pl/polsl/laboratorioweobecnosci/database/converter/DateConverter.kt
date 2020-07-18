package pl.polsl.laboratorioweobecnosci.database.converter

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

/**
 * Klasa konwertująca datę w bazie
 */
class DateConverter {
    companion object {
        /**
         * Funkcja tworząca datę z timestampu
         * @return datę utworzoną z timestampu
         */
        @TypeConverter
        @JvmStatic
        fun fromTimestamp(value: Long?): Date? {
            return when (value) {
                null -> null
                else -> Date(value)
            }
        }

        /**
         * Funkcja tworząca timestamp z daty
         * @return timestamp utworzony z daty
         */
        @TypeConverter
        @JvmStatic
        fun toTimestamp(date: Date?): Long? {
            return when (date) {
                null -> null
                else -> date.time
            }
        }
    }
}
