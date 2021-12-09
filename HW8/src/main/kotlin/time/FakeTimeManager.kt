package time

import java.util.*

class FakeTimeManager : TimeManager {
    private val calendar: Calendar

    constructor() {
        this.calendar = GregorianCalendar()
    }

    constructor(calendar: Calendar) {
        this.calendar = calendar.clone() as Calendar
    }

    override fun now(): Date {
        return calendar.time
    }

    fun setThis(field: Int, value: Int): TimeManager {
        calendar.set(field, value)

        return this
    }

    override fun set(field: Int, value: Int): TimeManager = withFrozenTime(calendar) {
        setThis(field, value)
    }

    fun setThis(date: Date): TimeManager {
        calendar.time = date

        return this
    }

    override fun set(date: Date): TimeManager = withFrozenTime(calendar) {
        setThis(date)
    }

    fun addThis(field: Int, value: Int): TimeManager {
        calendar.add(field, value)

        return this
    }

    override fun add(field: Int, value: Int): TimeManager = withFrozenTime(calendar) {
        addThis(field, value)
    }

    override fun get(field: Int): Int {
        return calendar.get(field)
    }

    fun clone(): TimeManager = withFrozenTime(calendar.clone() as Calendar) {
        this
    }

    companion object {
        fun <R> withFrozenTime(body: FakeTimeManager.() -> R): R = with(FakeTimeManager(), body)

        fun <R> withFrozenTime(calendar: Calendar, body: FakeTimeManager.() -> R): R =
            with(FakeTimeManager(calendar), body)
    }
}