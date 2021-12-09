package time

import time.FakeTimeManager.Companion.withFrozenTime
import java.util.*

class RealTimeManager : TimeManager {
    override fun now(): Date {
        return GregorianCalendar().time
    }

    override fun set(field: Int, value: Int): TimeManager {
        return withFrozenTime {
            set(field, value)

            this
        }
    }

    override fun set(date: Date): TimeManager {
        return withFrozenTime {
            set(date)

            this
        }
    }

    override fun add(field: Int, value: Int): TimeManager {
        return withFrozenTime {
            add(field, value)

            this
        }
    }

    override fun get(field: Int): Int {
        return GregorianCalendar().get(field)
    }
}