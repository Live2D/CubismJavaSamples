/*
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at http://live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

package com.live2d.demo.minimum;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

public class MainActivityMinimum extends Activity {
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _glSurfaceView = new GLSurfaceView(this);
        _glSurfaceView.setEGLContextClientVersion(2);       // OpenGL ES 2.0を利用

        GLRendererMinimum _glRenderer = new GLRendererMinimum();

        _glSurfaceView.setRenderer(_glRenderer);
        _glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        _glSurfaceView.setPreserveEGLContextOnPause(true);

        setContentView(_glSurfaceView);

        // システムバーの表示制御を行うコントローラーを取得する
        insetsController = WindowCompat.getInsetsController(
            getWindow(),
            getWindow().getDecorView()
        );

        if (insetsController != null) {
            // スワイプで一時表示し、自動で再び隠す（没入モード）
            insetsController.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        }

        hideSystemBars();

        _glSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, final MotionEvent event) {
                final float pointX = event.getX();
                final float pointY = event.getY();

                // GLSurfaceViewのイベント処理キューにタッチイベントを追加する。
                _glSurfaceView.queueEvent(
                    new Runnable() {
                        @Override
                        public void run() {
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    LAppMinimumDelegate.getInstance().onTouchBegan(pointX, pointY);
                                    break;
                                case MotionEvent.ACTION_UP:
                                    LAppMinimumDelegate.getInstance().onTouchEnd(pointX, pointY);
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    LAppMinimumDelegate.getInstance().onTouchMoved(pointX, pointY);
                                    break;
                            }
                        }
                    }
                );
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        LAppMinimumDelegate.getInstance().onStart(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        _glSurfaceView.onResume();

        hideSystemBars();
    }

    @Override
    protected void onPause() {
        super.onPause();

        _glSurfaceView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        LAppMinimumDelegate.getInstance().onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        LAppMinimumDelegate.getInstance().onDestroy();
    }

    /**
     * Hide the system bars (status bar and navigation bar).
     */
    private void hideSystemBars() {
        insetsController.hide(
            WindowInsetsCompat.Type.navigationBars()
            | WindowInsetsCompat.Type.statusBars()
        );
    }

    private GLSurfaceView _glSurfaceView;

    /**
     * Reused across lifecycle methods to preserve bar behavior settings.
     */
    private WindowInsetsControllerCompat insetsController;
}
