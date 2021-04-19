package com.example.timer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    TextView timeShow;
    Button play;
    Button pause;
    Button stop;
    Timer timer;
    boolean TimerStarted = false;
    Long time = 0L;
    TimerTask timerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timeShow = (TextView) findViewById(R.id.timeShow);
        pause = (Button) findViewById(R.id.pauseButton);
        play = (Button) findViewById(R.id.playButton);
        stop = (Button) findViewById(R.id.stopButton);
        timer = new Timer();
    }

    public void Buttonevent(View view)
    {
        switch (view.getId()) {
            case R.id.playButton:
                Start();
                break;
            case R.id.pauseButton:
                Pause();
                break;
            case R.id.stopButton:
                Stop();
                break;
        }
    }

    public  void Startimer()
    {
        timerTask = new TimerTask()
        {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time++;
                        timeShow.setText(TimerText());
                    }
                });

            }
        };
        timer.scheduleAtFixedRate(timerTask, 0,1000);
    }

    public void Start()
    {
        if(TimerStarted == false)
        {
            TimerStarted = true;
            Startimer();

        }
        else {
            //show error
        }

    }
    public void Pause()
    {
        if(TimerStarted == true)
        {
            timerTask.cancel();
            TimerStarted= false;

        }
        else
        {
            //Show error
        }


    }

    public void Stop()
    {
        timerTask.cancel();
        timeShow.setText(formatTime(0L,0L));
        TimerStarted = false;
        time=0L;
    }

    private  String  TimerText()
    {

        Long seconds = TimeUnit.SECONDS.toSeconds(time) - (TimeUnit.SECONDS.toMinutes(time) *60);
        Long minutes = TimeUnit.SECONDS.toMinutes(time) - (TimeUnit.SECONDS.toHours(time)* 60);

        return  formatTime(seconds, minutes);

    }

    private  String formatTime(Long seconds,Long minutes)
    {
        return  String.format("%02d",minutes) + ":" + String.format("%02d", seconds);
    }
}