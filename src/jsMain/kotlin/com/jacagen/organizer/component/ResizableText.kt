package com.jacagen.organizer.component

import io.kvision.core.Container
import io.kvision.core.onInputLaunch
import io.kvision.form.text.Text
import io.kvision.state.ObservableValue
import io.kvision.state.bindTo
import io.kvision.utils.ch

class ResizableText(textVal: String) : Text() {
    init {
        val store = ObservableValue(textVal)
        bindTo(store)
        onInputLaunch { doResize() }
//        onEvent {   // TODO onEventLaunch?
//            keydown = { e ->
//                if (e.key == "Enter") {
//                    console.log(e)
//                    e.preventDefault()
//                    e.stopPropagation()
//                }
//            }
//        }
       doResize()
    }

    private fun doResize() {   // TODO make private-er
        width = (value!!.length).ch
    }

}

// TODO Resizing is far from perfect
fun Container.resizableText(value: String): ResizableText {
    val t = ResizableText(value)
    add(t)
    return t
}