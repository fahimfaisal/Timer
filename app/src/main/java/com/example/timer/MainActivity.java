package com.example.timer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    TextView timeShow;
    ImageButton play;
    ImageButton pause;
    ImageButton stop;
    Timer timer;
    EditText exerciseName;
    TextView workoutType;
    TextView info;

    Boolean TimerStarted = false;
    Long time = 0L;
    TimerTask timerTask;
    String TIME = "myTime";
    String STATE = "myBool";
    Boolean isPaused = false;

    String FIRST= "myFirst";
    String SAVE = "mySave";
    String PAUSE = "myPause";
    String EXERCISE = "myExercise";
    Long save = 0L;
    Long savedtime = 0L;
    String name="myName";
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timeShow = (TextView) findViewById(R.id.timeShow);
        workoutType = (TextView) findViewById(R.id.workoutType);
        info = (TextView) findViewById(R.id.info);
        exerciseName = (EditText) findViewById(R.id.exerciseName);
        pause = (ImageButton) findViewById(R.id.pauseButton);
        play = (ImageButton) findViewById(R.id.playButton);
        stop = (ImageButton) findViewById(R.id.stopButton);
        timer = new Timer();
        sharedPreferences= getSharedPreferences("com.example.timer",MODE_PRIVATE);
        name = sharedPreferences.getString(EXERCISE, "pushup");
        savedtime = sharedPreferences.getLong(SAVE, 0);

        info.setText("You have spent " + TimerText(savedtime) + " on " + name + " last time");



    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);





        TimerStarted= savedInstanceState.getBoolean(STATE);
        time = savedInstanceState.getLong(TIME);
        save = savedInstanceState.getLong(SAVE);
        isPaused = savedInstanceState.getBoolean(PAUSE);

        timeShow.setText(TimerText(time));

        if(savedInstanceState == null)
        {
            info.setText("You have spent " + TimerText(savedtime) + " on " + name + " last time");
        }




        if (TimerStarted && !isPaused)
        {
            Startimer();
        }



    }



    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(STATE, TimerStarted);
        outState.putLong(TIME, time);
        outState.putLong(SAVE, save);
        outState.putBoolean(PAUSE, isPaused);



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
                        timeShow.setText(TimerText(time));
                    }
                });

            }
        };
        timer.scheduleAtFixedRate(timerTask, 0,1000);
    }



    private  String  TimerText(Long time)
    {

        Long seconds = TimeUnit.SECONDS.toSeconds(time) - (TimeUnit.SECONDS.toMinutes(time) *60);
        Long minutes = TimeUnit.SECONDS.toMinutes(time) - (TimeUnit.SECONDS.toHours(time)* 60);

        return  formatTime(seconds, minutes);

    }

    private  String formatTime(Long seconds,Long minutes)
    {
        return  String.format("%02d",minutes) + ":" + String.format("%02d", seconds);
    }

    public  void Setinfo(Long time)
    {
        info.setText("You have spent " + TimerText(time) + " on " + exerciseName.getText() + " last time");
    }


    public void Start()
    {
        if (exerciseName.getText().toString().matches(""))
        {
            Toast.makeText(getApplicationContext(),"Enter an Exercise Name",Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (!TimerStarted || isPaused)
            {
                TimerStarted = true;
                isPaused = false;

                Startimer();

            }
            else
            {
                Toast.makeText(getApplicationContext(),"Timer Already Started",Toast.LENGTH_SHORT).show();
            }

        }


    }


    public void Pause()
    {

        if(TimerStarted || !isPaused)
        {
            timerTask.cancel();

            isPaused = true;


        }



    }



    public void Stop()
    {

        if(TimerStarted)
        {
            if (!isPaused)
            {
                timerTask.cancel();

            }




            isPaused = false;
            TimerStarted = false;

            save =  time;
            Setinfo(time);
            time=0L;
            timeShow.setText(TimerText(time));




            SharedPreferences.Editor editor =sharedPreferences.edit();
            editor.putString(EXERCISE, exerciseName.getText().toString());
            editor.putLong(SAVE,save);
            editor.apply();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Timer hasn't started",Toast.LENGTH_SHORT).show();
        }



    }


}