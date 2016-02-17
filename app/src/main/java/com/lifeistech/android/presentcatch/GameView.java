package com.lifeistech.android.presentcatch;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AsyncPlayer;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

/**
 * Created by fukai on 16/02/17.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback, Runnable{

    static final long FPS = 30;
    static final long FRAMW_TIME = 1000/ FPS;

    SurfaceHolder surfaceHolder;
    Thread thread;

    Present present[];
    Bitmap presentImage[];

    present [] = new Present[2];
    presentImage [] = new Bitmap [2];


    int screenWidth, screenHeight;

    Player player;
    Bitmap playerImage;
    Bitmap backgroundImage;

    int score = 0;
    int life = 10;

//    String img_name [] = {"img_present0", "img_present1"};
//    for(int n = 0; n<2; n++){
//        String imge_name[n] = "" + n;
//        //String img_name[i] = "img_present" + String.valueOf(i);
//    }

    public GameView(Context context){
        super(context);
        getHolder().addCallback(this);

        Resources resources = context.getResources();

        presentImage[0] = BitmapFactory.decodeResource(resources, R.drawable.img_present0);
        presentImage[1] = BitmapFactory.decodeResource(resources, R.drawable.img_present1);

//        for(int i = 0; i<2; i++) {
//            presentImage[i] = BitmapFactory.decodeResource(resources, R.drawable.);
//// 画像の名前から取得できないものか
////            presentImage[i] = BitmapFactory.decodeResource(resources, R.drawable.("img_present"+i));
//        }

        playerImage = BitmapFactory.decodeResource(resources, R.drawable.img_player);
        backgroundImage = BitmapFactory.decodeResource(resources, R.drawable.img_background);
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
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        screenWidth = width;
        screenHeight = height;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        thread = null;
    }

    class Present{
        private static final int WIDTH = 20;
        private static final int HEIGHT = 20;

        float x, y;

        public Present(){
            Random random = new Random();
            x = random.nextInt(screenWidth - WIDTH);
            y = 0;
        }

        public void update(){
            y += 15.0f;
        }

        public void reset(){
            Random random = new Random();
            x = random.nextInt(screenHeight - WIDTH);
            y = 0;
        }
    }

    class Player{
        final int WIDTH = 50;
        final int HEIGHT = 50;

        float x, y;

        public Player(){
            x = 0;
            y = screenHeight - HEIGHT;
        }

        public boolean isEnter(Present present){
            if(present.x + Present.WIDTH > x && present.x < x + WIDTH &&
                    present.y + Present.HEIGHT > y && present.y < y + HEIGHT){
                return true;
            }else {
                return false;
            }
        }

        public void move(float diffX){
            this.x += diffX;
            this.x = Math.max(0, x);
            this.x = Math.min(screenWidth - WIDTH, x);
        }
    }


    public void run(){

        player = new Player();

        for(int i =0; i<2; i++){
            present[i] = new Present();
        }

        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setFakeBoldText(true);
        textPaint.setTextSize(100);
        backgroundImage = Bitmap.createScaledBitmap(backgroundImage, screenWidth, screenHeight, true);

        while(thread != null) {
            Canvas canvas = surfaceHolder.lockCanvas();


            canvas.drawBitmap(backgroundImage, 0, 0, null);

            canvas.drawBitmap(playerImage, player.x, player.y, null);

            canvas.drawText("Score: " + score, 50, 150, textPaint);
            canvas.drawText("LIFE: " + life, 50, 300, textPaint);

            for(int i =0; i<2; i++) {

                canvas.drawBitmap(presentImage[i], present[i].x, present[i].y, null);

                if (player.isEnter(present[i])){
                    present[i].reset();
                    score += 10;
                }else if (present[i].y > screenHeight) {
                    present[i].reset();
                    life--;
                } else {
                    present[i].update();
                }

                if(life <= 0){
                    canvas.drawText("Game Over", screenWidth / 3, screenHeight / 2, textPaint);
                    surfaceHolder.unlockCanvasAndPost(canvas);
                    break;
                }
            }

            surfaceHolder.unlockCanvasAndPost(canvas);

            try {
                Thread.sleep(FRAMW_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
