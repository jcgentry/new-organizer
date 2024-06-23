package com.jacagen.organizer.component

import io.kvision.core.Container
import io.kvision.core.onInputLaunch
import io.kvision.form.text.Text
import io.kvision.state.ObservableValue
import io.kvision.state.bindTo
import io.kvision.utils.ch
import kotlin.math.max

class ResizableText(textVal: String) : Text() {
    init {
        val store = ObservableValue(textVal)
        bindTo(store)
        onInputLaunch { doResize() }
        doResize()
    }

    private fun doResize() {
        // TODO Resizing is far from perfect
        width = if (value == null)
            10.ch
        else
            max(10, value!!.length).ch
    }
}

fun Container.resizableText(value: String): ResizableText {
    val t = ResizableText(value)
    add(t)
    return t
}