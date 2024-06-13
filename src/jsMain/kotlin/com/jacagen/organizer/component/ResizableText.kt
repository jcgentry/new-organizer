package com.jacagen.organizer.component

import com.jacagen.organizer.Task
import io.kvision.core.Container
import io.kvision.core.onEvent
import io.kvision.core.onEventLaunch
import io.kvision.core.onInputLaunch
import io.kvision.form.text.Text
import io.kvision.state.ObservableValue
import io.kvision.state.bindTo
import io.kvision.utils.ch
import kotlin.math.max
import kotlin.math.min

class ResizableText(textVal: String) : Text() {
    init {
        val store = ObservableValue(textVal)
        bindTo(store)
        onInputLaunch { doResize() }
        doResize()

        onEvent() {   // TODO onEventLaunch?
            keydown = { e ->
                if (e.key == "Enter") {
                    e.preventDefault()
                }
            }
        }
    }

    private fun doResize() {   // TODO make private-er
        if (value == null)
            width = 10.ch
        else
            width = max(10, value!!.length).ch
    }

}

// TODO Resizing is far from perfect
fun Container.resizableText(value: String): ResizableText {
    val t = ResizableText(value)
    add(t)
    return t
}