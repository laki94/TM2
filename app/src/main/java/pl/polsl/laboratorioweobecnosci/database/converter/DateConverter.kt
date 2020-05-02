package pl.polsl.laboratorioweobecnosci.database.converter

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*


class DateConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromTimestamp(value: Long?): Date? {
            val dateFormat = SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault()
            )
            return when (value) {
                null -> null
                else -> Date(value)
            }
        }

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
