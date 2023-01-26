/*
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at http://live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

package com.live2d.demo.minimum;

import android.app.Activity;
import android.opengl.GLES20;
import android.os.Build;
import com.live2d.demo.LAppDefine;
import com.live2d.demo.LAppDefine.ModelDir;
import com.live2d.sdk.cubism.framework.CubismFramework;

import static android.opengl.GLES20.*;

public class LAppMinimumDelegate {
    public static LAppMinimumDelegate getInstance() {
        if (s_instance == null) {
            s_instance = new LAppMinimumDelegate();
        }
        return s_instance;
    }

    /**
     * クラスのインスタンス（シングルトン）を解放する。
     */
    public static void releaseInstance() {
        if (s_instance != null) {
            s_instance = null;
        }
    }

    public void onStart(Activity activity) {
        textureManager = new LAppMinimumTextureManager();
        view = new LAppMinimumView();

        LAppMinimumPal.updateTime();
        this.activity = activity;
    }

    public void onStop() {
        view = null;
        textureManager = null;

        LAppMinimumLive2DManager.releaseInstance();
        CubismFramework.dispose();
    }

    public void onPause() {
        currentModel = LAppMinimumLive2DManager.getInstance().getCurrentModel();
    }

    public void onDestroy() {
        releaseInstance();
    }

    public void onSurfaceCreated() {
        // テクスチャサンプリング設定
        GLES20.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        GLES20.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        // 透過設定
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // Initialize Cubism SDK framework
        CubismFramework.initialize();

        // シェーダーの初期化
        view.initializeShader();
    }

    public void onSurfaceChanged(int width, int height) {
        // 描画範囲指定
        GLES20.glViewport(0, 0, width, height);
        windowWidth = width;
        windowHeight = height;

        // AppViewの初期化
        view.initialize();
        view.initializeSprite();

        isActive = true;
    }

    public void run() {
        // 時間更新
        LAppMinimumPal.updateTime();

        // 画面初期化
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearDepthf(1.0f);

        if (view != null) {
            view.render();
        }

        // アプリケーションを非アクティブにする
        if (!isActive) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.finishAndRemoveTask();
            }
            System.exit(0);
        }
    }

    public void onTouchBegan(float x, float y) {
        mouseX = x;
        mouseY = y;

        if (view != null) {
            isCaptured = true;
            view.onTouchesBegan(mouseX, mouseY);
        }
    }

    public void onTouchEnd(float x, float y) {
        mouseX = x;
        mouseY = y;

        if (view != null) {
            isCaptured = false;
            view.onTouchesEnded(mouseX, mouseY);
        }
    }

    public void onTouchMoved(float x, float y) {
        mouseX = x;
        mouseY = y;

        if (isCaptured && view != null) {
            view.onTouchesMoved(mouseX, mouseY);
        }
    }

    // シェーダーを登録する
    public int createShader() {
        int vertexShaderId = glCreateShader(GLES20.GL_VERTEX_SHADER);
        final String vertexShader =
            "attribute vec3 position;" +
                "attribute vec2 uv;" +
                "varying vec2 vuv;" +
                "void main(void){" +
                "gl_Position = vec4(position, 1.0);" +
                "vuv = uv;" +
                "}";

        GLES20.glShaderSource(vertexShaderId, vertexShader);
        GLES20.glCompileShader(vertexShaderId);

        // フラグメントシェーダのコンパイル
        int fragmentShaderId = glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        final String fragmentShader =
            "precision mediump float;" +
                "uniform sampler2D texture;" +
                "varying vec2 vuv;" +
                "uniform vec4 baseColor;" +
                "void main(void){" +
                "gl_FragColor = texture2D(texture, vuv);" +
                "}";

        GLES20.glShaderSource(fragmentShaderId, fragmentShader);
        GLES20.glCompileShader(fragmentShaderId);

        // プログラムオブジェクトの作成
        int programId = GLES20.glCreateProgram();

        // Programのシェーダーを設定
        GLES20.glAttachShader(programId, vertexShaderId);
        GLES20.glAttachShader(programId, fragmentShaderId);

        GLES20.glLinkProgram(programId);

        GLES20.glUseProgram(programId);

        return programId;
    }

    // getter, setter群
    public LAppMinimumTextureManager getTextureManager() {
        return textureManager;
    }

    public LAppMinimumView getView() {
        return view;
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    public Activity getActivity() {
        return activity;
    }


    private LAppMinimumDelegate() {
        currentModel = ModelDir.values()[0];

        // Set up Cubism SDK framework.
        cubismOption.logFunction = new LAppMinimumPal.PrintLogFunction();
        cubismOption.loggingLevel = LAppDefine.cubismLoggingLevel;

        CubismFramework.cleanUp();
        CubismFramework.startUp(cubismOption);
    }

    private static LAppMinimumDelegate s_instance;
    private Activity activity;

    private final CubismFramework.Option cubismOption = new CubismFramework.Option();

    private LAppMinimumTextureManager textureManager;
    private LAppMinimumView view;
    private int windowWidth;
    private int windowHeight;
    private boolean isActive;

    /**
     * モデルシーンインデックス
     */
    private ModelDir currentModel;

    /**
     * クリックしているか
     */
    private boolean isCaptured;
    /**
     * マウスのX座標
     */
    private float mouseX;
    /**
     * マウスのY座標
     */
    private float mouseY;
}
