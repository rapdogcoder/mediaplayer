package com.example.mediaplayer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.view.View
import com.example.mediaplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    var mService: MusicPlayerService? = null

    val mServiceConnection = object : ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mService = (service as MusicPlayerService.MusicPlayerBinder).
            getService()
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            mService = null
        }
    }

    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setContentView(R.layout.activity_main)

        binding.btnPlay.setOnClickListener{
            play()
        }
        binding.btnPause.setOnClickListener{
            pause()
        }
        binding.btnExit.setOnClickListener{
            stop()
        }
    }


    override fun onResume() {
        super.onResume()

        if (mService==null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                startForegroundService(
                    Intent(this,
                MusicPlayerService::class.java)
                )
            } else {
                startService(Intent(applicationContext,
                MusicPlayerService::class.java))
            }

            val intent = Intent(this, MusicPlayerService::class.java)
            bindService(intent,mServiceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onPause() {
        super.onPause()
        if(mService!=null){
            if(!mService!!.isPlaying()){
                mService!!.stopSelf()
            }
            unbindService(mServiceConnection)
            mService = null
        }
    }

    private fun play() {
        mService?.play()
    }
    private fun pause(){
        mService?.pause()
    }
    private fun stop(){
        mService?.stop()
    }

}