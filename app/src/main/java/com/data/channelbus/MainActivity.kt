package com.data.channelbus

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.pritam.channelbus.ChannelBus
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onResume() {
        super.onResume()
        ChannelBus.registerEvent(this.javaClass.simpleName, Dispatchers.Main, ChangedTextData::class.java) { event ->
            tvResult.text = event.text
        }
    }

    @ExperimentalCoroutinesApi
    fun onSendEvent(v: View) {
        val data = etText.text.toString()
        ChannelBus.send(ChangedTextData(data))
    }

    override fun onStop() {
        super.onStop()
        ChannelBus.unregisterAllEvents()
    }
}

data class ChangedTextData(val text: String)