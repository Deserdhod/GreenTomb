package com.example.bgk_0_000.greentomb;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.widget.Toast;

public class DrawThread extends Thread {

    private SurfaceHolder surfaceHolder;
    private Sprite playerBird;
    private Sprite enemyBird;

    private Paint backgroundPaint = new Paint();
    private Bitmap bitmap;
    private int towardPointX;
    private int towardPointY;

    int w;
    int h;
    volatile int points;

    {
        backgroundPaint.setColor(Color.GRAY);
        backgroundPaint.setStyle(Paint.Style.FILL);
    }


    private volatile boolean running = true;//флаг для остановки потока

    public DrawThread(Context context, SurfaceHolder surfaceHolder) {

        points = 40;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite);

        w = bitmap.getWidth()/5;
        h = bitmap.getHeight()/3;

        Rect firstFrame = new Rect(0, 0, w, h);

        playerBird = new Sprite(firstFrame, bitmap);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                if (i == 2 && j == 3) {
                    continue;
                }
                playerBird.addFrame(new Rect(j * w, i * h, j * w + w, i * w + w));
            }
        }

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ensprite);

        enemyBird = new Sprite(firstFrame, bitmap);

        for (int i = 0; i < 3; i++) {
            for (int j = 4; j >= 0; j--) {
                if (i ==0 && j == 4) {
                    continue;
                }
                if (i ==2 && j == 0) {
                    continue;
                }
                enemyBird.addFrame(new Rect(j*w, i*h, j*w+w, i*w+w));

            }
        }

        this.surfaceHolder = surfaceHolder;
    }



    public void requestStop() {
        running = false;
    }

    public void setTowardPoint(int x, int y) {
        towardPointX = x;
        towardPointY = y;

    }

    private void teleportEnemy (Canvas canvas) {
        enemyBird.x = canvas.getWidth() + Math.random() * 500;
        enemyBird.y = Math.random() * (canvas.getHeight() - enemyBird.getFrameHeight());
    }


    @Override
    public void run() {

        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setTextSize(55.0f);
        p.setColor(Color.WHITE);

        while (running) {
            Canvas canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                try {
                    canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgroundPaint);


                    playerBird.draw(canvas);
                    playerBird.update(10);

                    enemyBird.draw(canvas);
                    enemyBird.update(10);

                    enemyBird.x -= 3;

                    if (playerBird.y + playerBird.getFrameHeight() > canvas.getHeight()) {
                        playerBird.y = canvas.getHeight() - playerBird.getFrameHeight();
                    }
                    else if (playerBird.y < 0) {
                        playerBird.y = 0;
                    }

                    if (playerBird.x + playerBird.getFrameWidth() / 2 < towardPointX) playerBird.x += 10;
                    if (playerBird.x + playerBird.getFrameWidth() / 2 > towardPointX) playerBird.x -= 10;
                    if (playerBird.y + playerBird.getFrameHeight() / 2 < towardPointY) playerBird.y += 10;
                    if (playerBird.y + playerBird.getFrameHeight() / 2 > towardPointY) playerBird.y -= 10;

                    if (enemyBird.x < - enemyBird.getFrameWidth()) {
                        teleportEnemy(canvas);
                        points +=20;
                    }

                    if (enemyBird.intersect(playerBird)) {
                        teleportEnemy (canvas);
                        points -= 40;
                    }

                    canvas.drawText(points+"", canvas.getWidth() - 100, 70, p);

                    if (points >= 100){

                    }

                } finally {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}

