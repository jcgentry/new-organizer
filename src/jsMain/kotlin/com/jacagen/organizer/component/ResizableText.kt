package com.jacagen.organizer.component

import io.kvision.core.Container
import io.kvision.core.onEvent
import io.kvision.core.onEventLaunch
import io.kvision.core.onInputLaunch
import io.kvision.form.text.Text
import io.kvision.utils.ch

class ResizableText(value: String) : Text(value = value) {
    fun doResize() {   // TODO make private-er
        width = (value!!.length).ch
    }
}

// TODO Resizing is far from perfect
fun Container.resizableText(value: String, onEnter: () -> Unit): ResizableText {
    val t = ResizableText(value).apply {
        readonly = true
    }
    t.onInputLaunch { doResize() }
    t.onEvent {
        keydown = { e ->
            if (e.key == "Enter") {
                console.log(e)
                e.preventDefault()
                e.stopPropagation()
                onEnter()
            }
        }
    }
    add(t)
    t.doResize()
    return t
}