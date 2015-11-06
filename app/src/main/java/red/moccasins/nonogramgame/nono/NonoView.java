package red.moccasins.nonogramgame.nono;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Автор: Левшин Николай, 707 группа.
 * Дата создания: 02.11.2015.
 */


public class NonoView extends View {

    protected boolean measurementChanged = false;

    private Nonogram nonogram;

    private GestureDetector gestureDetector;

    private int inputMode = 0;

    public NonoView(Context context) {
        super(context);
        init();
    }

    public NonoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        gestureDetector = new GestureDetector(getContext(), gestureListener);
//        scaleGestureDetector = new ScaleGestureDetector(getContext(), scaleGestureListener);
//        scroller = new OverScroller(getContext());
    }

    private GestureDetector.OnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
    };

    public void InitEmptyNonogram(int width, int height) {
        nonogram = new Nonogram(width, height);
    }

    public void InitNewNonogram( String task ) {
        nonogram = new Nonogram(task);
    }


    @Override
    protected void onDraw(Canvas canvas) {

        if (isInEditMode()) return;

        if (measurementChanged) {
            measurementChanged = false;
            nonogram.initializeTable(getMeasuredWidth(), getMeasuredHeight(), getContext());
        }

        nonogram.draw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measurementChanged = true;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
        boolean touch;
        switch(action) {
            case (MotionEvent.ACTION_DOWN):
                touch = nonogram.StartSelectCell(event.getX(), event.getY(), inputMode);
                if (touch) {
                    invalidate();
                    return true;
                } else {
                    return super.onTouchEvent(event);
                }
            case (MotionEvent.ACTION_MOVE):
                touch = nonogram.KeepSelectCell(event.getX(), event.getY(), inputMode);
                if (touch) {
                    invalidate();
                    return true;
                } else {
                    return super.onTouchEvent(event);
                }
            case (MotionEvent.ACTION_UP):
                touch = nonogram.EndSelectCell(event.getX(), event.getY(), inputMode);
                if (touch) {
                    invalidate();
                    return true;
                } else {
                    return super.onTouchEvent(event);
                }
            case (MotionEvent.ACTION_CANCEL):
                return super.onTouchEvent(event);
            default:
                return super.onTouchEvent(event) || gestureDetector.onTouchEvent(event);
        }
    }

    public void setInputMode(int mode) {
        inputMode = mode;
    }

    public void Undo() {
        nonogram.Undo();
        invalidate();
    }

    public void Redo() {
        nonogram.Redo();
        invalidate();
    }
}
