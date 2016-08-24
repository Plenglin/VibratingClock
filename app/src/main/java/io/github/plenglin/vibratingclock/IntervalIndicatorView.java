package io.github.plenglin.vibratingclock;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

public class IntervalIndicatorView extends View {

    public enum Indicator {
        A, B, C
    }

    private Paint borderPaint, innerPaint, unfilledPaint, aPaint, bPaint, cPaint, pointerPaint;

    private Comparator<Paint> paintComparator = new Comparator<Paint>() {
        @Override
        public int compare(Paint lhs, Paint rhs) {
            return (int) (paintIntervals.get(lhs) - paintIntervals.get(rhs));
        }
    };

    private Map<Paint, Long> paintIntervals = new HashMap<>();

    private Timer timer = new Timer();

    List<Paint> orderedPaints = new ArrayList<>();

    public IntervalIndicatorView(Context context, AttributeSet attributes) {
        super(context, attributes);

        TypedArray array = context.getTheme().obtainStyledAttributes(
                attributes,
                R.styleable.IntervalIndicatorView,
                0, 0
        );

        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        innerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        unfilledPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pointerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        aPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        cPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        Resources r = getResources();

        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(50f);
        borderPaint.setColor(r.getColor(R.color.clockBorder));

        innerPaint.setStyle(Paint.Style.FILL);
        innerPaint.setColor(r.getColor(R.color.clockBorder));

        pointerPaint.setStyle(Paint.Style.STROKE);
        pointerPaint.setStrokeWidth(10f);
        pointerPaint.setStrokeCap(Paint.Cap.ROUND);
        pointerPaint.setColor(r.getColor(R.color.clockPointer));

        unfilledPaint.setStyle(Paint.Style.FILL);
        unfilledPaint.setStrokeWidth(1f);
        unfilledPaint.setColor(r.getColor(R.color.clockUnfilled));

        aPaint.setStyle(Paint.Style.FILL);
        aPaint.setStrokeWidth(5f);
        aPaint.setColor(r.getColor(R.color.clockRed));

        bPaint.setStyle(Paint.Style.FILL);
        bPaint.setStrokeWidth(5f);
        bPaint.setColor(r.getColor(R.color.clockYellow));

        cPaint.setStyle(Paint.Style.FILL);
        cPaint.setStrokeWidth(5f);
        cPaint.setColor(r.getColor(R.color.clockBlue));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float w = getWidth(), h = getHeight();
        float cx = w / 2f, cy = h / 2f;
        float rad = .4f * w;

        orderedPaints.clear();
        orderedPaints.addAll(paintIntervals.keySet());
        Collections.sort(orderedPaints, paintComparator);

        // Draw the interval marks
        for (int m=0; m < 60; m++) {
            // Draw the unfilled line
            canvas.drawLine(
                    cx, cy,
                    (float) (rad * Math.sin(Math.toRadians(6 * m))) + cx,
                    (float) (-rad * Math.cos(Math.toRadians(6 * m))) + cy,
                    unfilledPaint);
            for (Paint p: orderedPaints) {
                long interval = paintIntervals.get(p);
                if (interval > 0 && m % interval == 0) {
                    canvas.drawLine(
                            cx, cy,
                            (float) (rad * Math.sin(Math.toRadians(6 * m))) + cx,
                            (float) (-rad * Math.cos(Math.toRadians(6 * m))) + cy,
                            p);
                }
            }
        }

        // Draw the border
        canvas.drawCircle(cx, cy, .4f * w, borderPaint);

        canvas.drawCircle(cx, cy, .3f * w, innerPaint);

        // Draw the current time
        double currentMinuteAngle = (System.currentTimeMillis() / 10000d) % 360d;
        canvas.drawLine(
                cx, cy,
                (float) (rad * Math.sin(Math.toRadians(currentMinuteAngle))) + cx,
                (float) (-rad * Math.cos(Math.toRadians(currentMinuteAngle))) + cy,
                pointerPaint
        );

    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        timer.schedule(new ViewInvalidatorTask(this), 5000, 5000);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        timer.cancel();
    }

    public void setInterval(Indicator type, long length) {
        invalidate();
        switch (type) {
            case A:
                paintIntervals.put(aPaint, length);
                break;
            case B:
                paintIntervals.put(bPaint, length);
                break;
            case C:
                paintIntervals.put(cPaint, length);
        }
    }

}
