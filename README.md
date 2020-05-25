# ChannelBus
This library is based on [Kotlin Coroutines Channel](https://kotlinlang.org/docs/reference/coroutines/channels.html).

###### Implemet the library in your app build.gradle:

- Register event class:
```
ChannelBus.registerEvent(contextName: String, eventDispatcher: CoroutineDispatcher, eventClass: Class<T>, eventCallback: (T) -> Unit)
```
- And unregister into `onDestroy` or `onStop` by your logic:
```
ChannelBus.unregisterAllEvents() - for all Events

or

ChannelBus.unregisterByContext(contextName: String)
```
- Send event:
```
ChannelBus.send(event: Any, delaySend: Long = 0)
```
