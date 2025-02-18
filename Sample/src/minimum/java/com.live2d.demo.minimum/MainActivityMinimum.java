/*
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at http://live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

package com.live2d.demo.minimum;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;

public class MainActivityMinimum extends Activity {
    private GLSurfaceView _glSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _glSurfaceView = new GLSurfaceView(this);
        _glSurfaceView.setEGLContextClientVersion(2);       // OpenGL ES 2.0を利用

        GLRendererMinimum _glRenderer = new GLRendererMinimum();

        _glSurfaceView.setRenderer(_glRenderer);
        _glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        setContentView(_glSurfaceView);
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

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
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
        return super.onTouchEvent(event);
    }
}
