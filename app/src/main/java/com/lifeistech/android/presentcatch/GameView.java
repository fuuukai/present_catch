package com.lifeistech.android.presentcatch;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by fukai on 16/02/17.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback{

    static final long FPS = 30;
    static final long FRAMW_TIME = 1000/ FPS;

    SurfaceHolder surfaceHolder;
    Thread thread;

    Present present;
    Bitmap presentImage;

    public GameView(Context context){
        super(context);
        getHolder().addCallback(this);

        Resources resources = context.getResources();
        presentImage = BitmapFactory.decodeResource(resources, R.drawable.img_present0);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
//        Paint paint = new Paint();
//        paint.setColor(Color.RED);
//        paint.setStyle(Paint.Style.FILL);
//        Canvas canvas = holder.lockCanvas();
//        canvas.drawColor(Color.WHITE);
//        canvas.drawCircle(100, 200, 50, paint);

//        Canvas canvas = holder.lockCanvas();
//        canvas.drawColor(Color.WHITE);
//        canvas.drawBitmap(presentImage, 100, 200, null);
//        holder.unlockCanvasAndPost(canvas);

        surfaceHolder = holder;
        thread = new Thread((Runnable) this);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        thread = null;
    }

    class Present{
        private static final int WIDTH = 100;
        private static final int HEIGHT = 100;

        float x, y;

        public Present(){
            x = 0;
            y = 0;
        }

        public void update(){
            y += 15.0f;
        }
    }

    public void run(){

        present = new Present();

        while(thread != null) {
            Canvas canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.WHITE);

            canvas.drawBitmap(presentImage, present.x, present.y, null);

            present.update();

            surfaceHolder.unlockCanvasAndPost(canvas);

            try {
                Thread.sleep(FRAMW_TIME);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }

    }
}
