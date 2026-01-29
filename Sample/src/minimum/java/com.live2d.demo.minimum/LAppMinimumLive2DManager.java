/*
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at http://live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

package com.live2d.demo.minimum;

import com.live2d.sdk.cubism.framework.math.CubismMatrix44;
import com.live2d.sdk.cubism.framework.rendering.android.CubismOffscreenManagerAndroid;

/**
 * サンプルアプリケーションにおいてCubismModelを管理するクラス。
 * モデル生成と破棄、タップイベントの処理、モデル切り替えを行う。
 */
public class LAppMinimumLive2DManager {
    public static LAppMinimumLive2DManager getInstance() {
        if (s_instance == null) {
            s_instance = new LAppMinimumLive2DManager();
        }
        return s_instance;
    }

    public static void releaseInstance() {
        if (s_instance != null) {
            CubismOffscreenManagerAndroid.releaseInstance();
        }
        s_instance = null;
    }

    public void loadModel(String modelDirectoryName) {
        String dir = modelDirectoryName + "/";
        model = new LAppMinimumModel(dir);
        model.loadAssets(dir, modelDirectoryName + ".model3.json");
    }

    // モデル更新処理及び描画処理を行う
    public void onUpdate() {
        int width = LAppMinimumDelegate.getInstance().getWindowWidth();
        int height = LAppMinimumDelegate.getInstance().getWindowHeight();
        float aspectRatio = (float) width / (float) height;
        float displayRatio = (float) height / (float) width;

        // モデルで使用するオフスクリーン管理の開始処理
        CubismOffscreenManagerAndroid.getInstance().beginFrameProcess();

        projection.loadIdentity();

        float canvasRatio = model.getModel().getCanvasHeight() / model.getModel().getCanvasWidth();

        if (canvasRatio < displayRatio) {
            // 横長モデルを幅に合わせて縦方向のスケールを調整
            model.getModelMatrix().setWidth(2.0f);
            projection.scale(1.0f, aspectRatio);
        } else {
            // 縦長モデルを高さに合わせて横方向のスケールを調整
            model.getModelMatrix().setHeight(2.0f);
            projection.scale(1.0f / aspectRatio, 1.0f);
        }

        // 必要があればここで乗算する
        if (viewMatrix != null) {
            viewMatrix.multiplyByMatrix(projection);
        }

        // 描画前コール
        LAppMinimumDelegate.getInstance().getView().preModelDraw(model);

        model.update();
        model.draw(projection);     // 参照渡しなのでprojectionは変質する

        // 描画後コール
        LAppMinimumDelegate.getInstance().getView().postModelDraw(model);

        // モデルで使用するオフスクリーン管理の終了処理
        CubismOffscreenManagerAndroid.getInstance().endFrameProcess();
        // もし余っているオフスクリーンのリソースを解放したい場合行う処理
        CubismOffscreenManagerAndroid.getInstance().releaseStaleRenderTextures();
    }

    /**
     * 画面をドラッグした時の処理
     *
     * @param x 画面のx座標
     * @param y 画面のy座標
     */
    public void onDrag(float x, float y) {
        model.setDragging(x, y);
    }

    /**
     * 現在のシーンで保持しているモデルを返す
     *
     * @param number モデルリストのインデックス値
     * @return モデルのインスタンスを返す。インデックス値が範囲外の場合はnullを返す
     */
    public LAppMinimumModel getModel(int number) {
        return model;
    }

    /**
     * モデルのオフスクリーンのサイズを設定する。
     *
     * @param width  ウィンドウの幅
     * @param height ウィンドウの高さ
     */
    public void setRenderTargetSize(int width, int height) {
        if (model != null) {
            model.setRenderTargetSize(width, height);
        }
    }

    /**
     * シングルトンインスタンス
     */
    private static LAppMinimumLive2DManager s_instance;

    private LAppMinimumLive2DManager() {
        loadModel("Hiyori");
    }

    private LAppMinimumModel model;

    private final CubismMatrix44 viewMatrix = CubismMatrix44.create();
    private final CubismMatrix44 projection = CubismMatrix44.create();
}

