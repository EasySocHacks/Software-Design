package time

import java.util.*

interface TimeManager {
    fun now(): Date

    fun set(field: Int, value: Int): TimeManager

    fun set(date: Date): TimeManager

    fun add(field: Int, value: Int): TimeManager

    fun get(field: Int): Int
}