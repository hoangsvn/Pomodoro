package com.pomodoro;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.pomodoro.dal.DatabaseHelper;

public class TimerAT extends AsyncTask<Void, Integer, Void> {
    private MainActivity activity;
    private int timer, remainingSecond;
    private CircularProgressIndicator bar;
    private TextView tv;

    public TimerAT(MainActivity activity, int timer) {
        this.activity = activity;
        this.timer = timer;
        this.remainingSecond = activity.remainingSeconds;
        bar = activity.findViewById(R.id.progress);
        tv = activity.findViewById(R.id.tvTimer);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Button btnStart = activity.findViewById(R.id.btnStart);
        Button btnStop = activity.findViewById(R.id.btnStop);
        btnStart.setVisibility(View.GONE);
        btnStop.setVisibility(View.VISIBLE);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        while(remainingSecond > 0){
            if(isCancelled()) break;
            remainingSecond--;
            publishProgress(remainingSecond);
            activity.remainingSeconds = remainingSecond;
            SystemClock.sleep(1000);
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        int remaining = values[0];
        int minutes = remaining/60;
        int seconds = remaining%60;
        tv.setText(String.format("%02d : %02d", minutes, seconds));
        bar.setProgress(remaining);
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        Button btnStart = activity.findViewById(R.id.btnStart);
        Button btnStop = activity.findViewById(R.id.btnStop);
        btnStart.setVisibility(View.VISIBLE);
        btnStop.setVisibility(View.GONE);
        if(activity.mode == 0 && activity.task_select != null){
            TextView tvEst = activity.findViewById(R.id.tvEst);
            activity.task_select.setCount(activity.task_select.getCount()+1);
            tvEst.setText(activity.task_select.getCount()+"/"+activity.task_select.getEst());
            DatabaseHelper dbHelper = new DatabaseHelper(activity.getBaseContext());
            dbHelper.updateTask(activity.task_select);
        }
        ImageButton btnSkip = activity.findViewById(R.id.btnSkip);
        btnSkip.callOnClick();
    }
}
