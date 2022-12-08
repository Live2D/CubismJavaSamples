/*
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at http://live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

package com.live2d.demo.minimum;

import com.live2d.demo.LAppDefine;
import com.live2d.sdk.cubism.framework.math.CubismMatrix44;

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
        s_instance = null;
    }

    public void loadModel(LAppDefine.ModelDir modelDirectoryName) {
        String dir = modelDirectoryName.getDirName() + "/";
        model = new LAppMinimumModel(dir);
        model.loadAssets(dir, modelDirectoryName.getDirName() + ".model3.json");
    }

    // モデル更新処理及び描画処理を行う
    public void onUpdate() {
        int width = LAppMinimumDelegate.getInstance().getWindowWidth();
        int height = LAppMinimumDelegate.getInstance().getWindowHeight();

        projection.loadIdentity();

        if (model.getModel().getCanvasWidth() > 1.0f && width < height) {
            // 横に長いモデルを縦長ウィンドウに表示する際モデルの横サイズでscaleを算出する
            model.getModelMatrix().setWidth(2.0f);
            projection.scale(1.0f, (float) width / (float) height);
        } else {
            projection.scale((float) height / (float) width, 1.0f);
        }

        // 必要があればここで乗算する
        if (viewMatrix != null) {
            projection.multiplyByMatrix(viewMatrix);
        }

        model.update();
        model.draw(projection);     // 参照渡しなのでprojectionは変質する
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
     * シーンインデックスを返す
     *
     * @return シーンインデックス
     */
    public LAppDefine.ModelDir getCurrentModel() {
        return currentModel;
    }

    /**
     * シングルトンインスタンス
     */
    private static LAppMinimumLive2DManager s_instance;

    private LAppMinimumLive2DManager() {
        currentModel = LAppDefine.ModelDir.values()[1];

        loadModel(currentModel);
    }

    private LAppMinimumModel model;
    /**
     * 表示するシーンのインデックス値
     */
    private final LAppDefine.ModelDir currentModel;
    private final CubismMatrix44 viewMatrix = CubismMatrix44.create();
    private final CubismMatrix44 projection = CubismMatrix44.create();
}

