# ChannelBus
[![](https://jitpack.io/v/tintinscorpion/ChannelBus.svg)](https://jitpack.io/#tintinscorpion/ChannelBus)

<br>
This library is based on [Kotlin Coroutines Channel](https://kotlinlang.org/docs/reference/coroutines/channels.html).

###### Implementation:
```groovy
App Level:
implementation 'com.github.tintinscorpion:ChannelBus:{latest_version}'
```
```groovy
Project Level:
maven { url 'https://jitpack.io' }
```

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
