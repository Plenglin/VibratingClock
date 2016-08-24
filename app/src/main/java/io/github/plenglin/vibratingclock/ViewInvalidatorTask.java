package io.github.plenglin.vibratingclock;

import android.view.View;

import java.util.TimerTask;

public class ViewInvalidatorTask extends TimerTask {

    private View view;

    public ViewInvalidatorTask(View view) {
        this.view = view;
    }

    @Override
    public void run() {
        view.postInvalidate();
    }
}
