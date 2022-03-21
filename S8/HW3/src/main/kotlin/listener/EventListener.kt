package listener

import event.Event

abstract class EventListener {
    abstract fun notifyEvent(event: Event)
}