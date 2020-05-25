package com.pritam.channelbus
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import java.util.concurrent.TimeUnit

@UseExperimental(ObsoleteCoroutinesApi::class)
data class ChannelData<T>(val coroutineScope: CoroutineScope,
                          val eventDispatcher: CoroutineDispatcher,
                          val onEvent: (T) -> Unit) {
    private val channel = Channel<T>()

    init {
        coroutineScope.launch {
            channel.consumeEach { launch(eventDispatcher) { onEvent(it) } }
        }
    }

    @ExperimentalCoroutinesApi
    fun send(event: Any) {
        if (!channel.isClosedForSend) {
            TimeUnit.MILLISECONDS.sleep(1)
            coroutineScope.launch { channel.send(event as T) }
        } else {
            System.err.println("${ChannelBus.TAG}: Channel is closed for send")
        }
    }

    fun cancel() {
        channel.cancel()
    }
}