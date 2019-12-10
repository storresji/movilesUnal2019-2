package co.edu.aiteaching.drawords;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class PaintView extends View {
    private Path mpath;
    private Paint mpaint;
    private int scolor;
    private int swidth;
    private float x;
    private float y;
    private boolean start;
    private Bitmap mBitmap;
    private Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);

    private Canvas mCanvas;
    private int WIDTH;
    private int HEIGHT;

    public PaintView(Context context) {
        this(context, null);
    }
    public PaintView(Context context, AttributeSet attrs) {
        super(context,attrs);
        mpaint = new Paint();
        mpath = new Path();
        swidth = 15;
        start = true;
        scolor = Color.GREEN;
    }
    public void init() {
        this.WIDTH = this.getLayoutParams().width;
        this.HEIGHT = this.getLayoutParams().height;
        mBitmap = Bitmap.createBitmap(WIDTH, HEIGHT , Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

    }

    public void setSwidth(int swidth) {
        this.swidth = swidth;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mpaint.setStyle(Paint.Style.STROKE);
        mpaint.setStrokeWidth(swidth);
        mpaint.setColor(scolor);
        mpaint.setStyle(Paint.Style.STROKE);
        mpaint.setStrokeJoin(Paint.Join.ROUND);
        mpaint.setStrokeCap(Paint.Cap.ROUND);

        canvas.drawPath(mpath,mpaint);
        mCanvas.drawPath(mpath,mpaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        x = event.getX();
        y = event.getY();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mpath.moveTo(x, y);
            start = true;
            //Log.i("touch", "start event");
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            mpath.lineTo(x, y);
            start = false;
            Log.i("touch", "x:"+x+" y:"+y);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            //Log.i("touch", "up event");
            if (start) {
                mpath.lineTo(x + swidth, y);
            }

        }
        invalidate();
        return true;

        //return super.onTouchEvent(event);
    }

    public void clearView() {
        mpath.reset();
        mBitmap.eraseColor(Color.TRANSPARENT);
        invalidate();

    }
    public Bitmap getmBitmap() {
        return this.mBitmap;
    }

    // scale given bitmap by a factor and return a new bitmap
    public Bitmap scaleBitmap(int scaleFactor, Bitmap bitmap) {
        return Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth()*scaleFactor), (int)(bitmap.getHeight()*scaleFactor), true);
    }

    // scale original bitmap down to network size (28x28)
    public Bitmap getNormalizedBitmap() {
        //float scaleFactor = ImageClassifier.DIM_IMG_SIZE_HEIGHT / (float) mBitmap.getHeight();
        float scaleFactor =  28 / (float) mBitmap.getHeight();
        // todo: cut empty space around sketch
        return Bitmap.createScaledBitmap(mBitmap, (int)(mBitmap.getWidth()*scaleFactor), (int)(mBitmap.getHeight()*scaleFactor), true);

    }

}
