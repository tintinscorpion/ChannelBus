package com.pritam.channelbus

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ChannelBus private constructor() : CoroutineScope {

    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext = Dispatchers.IO + job

    companion object {

        const val TAG = "ChannelBus"

        private var instance: ChannelBus? = null
            get() = if (field == null) {
                field = ChannelBus()
                field
            } else {
                field
            }

        fun <T> registerEvent(
            contextName: String,
            eventDispatcher: CoroutineDispatcher,
            eventClass: Class<T>,
            eventCallback: (T) -> Unit
        ) {
            instance?.registerEvent(contextName, eventDispatcher, eventClass, eventCallback)
        }

        @ExperimentalCoroutinesApi
        fun send(event: Any, delaySend: Long = 0) {
            if (delaySend > 0) {
                instance?.launch {
                    delay(delaySend)
                    instance?.send(event)
                }
            } else {
                instance?.send(event)
            }
        }

        fun unregisterAllEvents() {
            instance?.unregisterAllEvent()
        }

        fun unregisterByContext(contextName: String) {
            instance?.unregisterByContext(contextName)
        }
    }

    private val contextList = mutableMapOf<String, MutableMap<Class<*>, ChannelData<*>>>()

    @ExperimentalCoroutinesApi
    private fun send(event: Any) {
        val cloneContextList = mutableMapOf<String, MutableMap<Class<*>, ChannelData<*>>>()
        cloneContextList.putAll(contextList)
        for ((_, queue) in cloneContextList) {
            queue.keys.firstOrNull { it == event.javaClass || it == event.javaClass.superclass }
                .let { key ->
                    queue[key]?.send(event)
                }
        }
    }

    private fun <T> registerEvent(
        contextName: String,
        eventDispatcher: CoroutineDispatcher,
        eventClass: Class<T>,
        eventCallback: (T) -> Unit
    ) {
        val queueList = if (contextList.containsKey(contextName)) {
            contextList[contextName]!!
        } else {
            val eventQueue = mutableMapOf<Class<*>, ChannelData<*>>()
            contextList[contextName] = eventQueue
            eventQueue
        }
        val eventData = ChannelData(
            this,
            eventDispatcher,
            eventCallback
        )
        queueList[eventClass] = eventData
    }

    private fun unregisterAllEvent() {
        if (BuildConfig.DEBUG) {
            println("$TAG: unregisterAllEvent()")
        }
        coroutineContext.cancelChildren()
        for ((_, queue) in contextList) {
            queue.values.forEach { it.cancel() }
            queue.clear()
        }
        contextList.clear()

    }

    private fun unregisterByContext(contextName: String) {
        val cloneContextList = mutableMapOf<String, MutableMap<Class<*>, ChannelData<*>>>()
        cloneContextList.putAll(contextList)
        val queuesInContext = cloneContextList.filter { it.key == contextName }
        for ((_, queue) in queuesInContext) {
            queue.values.forEach { it.cancel() }
            queue.clear()
        }
        contextList.remove(contextName)
    }
}