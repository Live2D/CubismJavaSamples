/*
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at http://live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

package com.live2d.demo.minimum;

import com.live2d.demo.LAppDefine;
import com.live2d.demo.TouchManager;
import com.live2d.sdk.cubism.framework.math.CubismMatrix44;
import com.live2d.sdk.cubism.framework.math.CubismViewMatrix;
import com.live2d.sdk.cubism.framework.rendering.android.CubismOffscreenSurfaceAndroid;

public class LAppMinimumView {
    public LAppMinimumView() {
        clearColor[0] = 1.0f;
        clearColor[1] = 1.0f;
        clearColor[2] = 1.0f;
        clearColor[3] = 0.0f;
    }

    // シェーダーを初期化する
    public void initializeShader() {
        programId = LAppMinimumDelegate.getInstance().createShader();
    }

    // ビューを初期化する
    public void initialize() {
        int width = LAppMinimumDelegate.getInstance().getWindowWidth();
        int height = LAppMinimumDelegate.getInstance().getWindowHeight();

        float ratio = (float) width / (float) height;
        float left = -ratio;
        float right = ratio;
        float bottom = LAppDefine.LogicalView.LEFT.getValue();
        float top = LAppDefine.LogicalView.RIGHT.getValue();

        // デバイスに対応する画面範囲。Xの左端、Xの右端、Yの下端、Yの上端
        viewMatrix.setScreenRect(left, right, bottom, top);
        viewMatrix.scale(LAppDefine.Scale.DEFAULT.getValue(), LAppDefine.Scale.DEFAULT.getValue());

        // 単位行列に初期化
        deviceToScreen.loadIdentity();

        if (width > height) {
            float screenW = Math.abs(right - left);
            deviceToScreen.scaleRelative(screenW / width, -screenW / width);
        } else {
            float screenH = Math.abs(top - bottom);
            deviceToScreen.scaleRelative(screenH / height, -screenH / height);
        }
        deviceToScreen.translateRelative(-width * 0.5f, -height * 0.5f);

        // 表示範囲の設定
        viewMatrix.setMaxScale(LAppDefine.Scale.MAX.getValue());   // 限界拡大率
        viewMatrix.setMinScale(LAppDefine.Scale.MIN.getValue());   // 限界縮小率

        // 表示できる最大範囲
        viewMatrix.setMaxScreenRect(
            LAppDefine.LogicalView.LEFT.getValue(),
            LAppDefine.LogicalView.RIGHT.getValue(),
            LAppDefine.LogicalView.BOTTOM.getValue(),
            LAppDefine.MaxLogicalView.TOP.getValue()
        );
    }

    // 画像を初期化する
    public void initializeSprite() {
        int windowWidth = LAppMinimumDelegate.getInstance().getWindowWidth();
        int windowHeight = LAppMinimumDelegate.getInstance().getWindowHeight();

        // 画面全体を覆うサイズ
        float x = windowWidth * 0.5f;
        float y = windowHeight * 0.5f;

        if (renderingSprite == null) {
            renderingSprite = new LAppMinimumSprite(x, y, windowWidth, windowHeight, 0, programId);
        } else {
            renderingSprite.resize(x, y, windowWidth, windowHeight);
        }
    }

    // 描画する
    public void render() {
        // モデルの描画
        LAppMinimumLive2DManager.getInstance().onUpdate();
    }

    /**
     * タッチされたときに呼ばれる
     *
     * @param pointX スクリーンX座標
     * @param pointY スクリーンY座標
     */
    public void onTouchesBegan(float pointX, float pointY) {
        touchManager.touchesBegan(pointX, pointY);
    }

    /**
     * タッチしているときにポインターが動いたら呼ばれる
     *
     * @param pointX スクリーンX座標
     * @param pointY スクリーンY座標
     */
    public void onTouchesMoved(float pointX, float pointY) {
        float viewX = transformViewX(touchManager.getLastX());
        float viewY = transformViewY(touchManager.getLastY());

        touchManager.touchesMoved(pointX, pointY);

        LAppMinimumLive2DManager.getInstance().onDrag(viewX, viewY);
    }

    /**
     * タッチが終了したら呼ばれる
     *
     * @param pointX スクリーンX座標
     * @param pointY スクリーンY座標
     */
    public void onTouchesEnded(float pointX, float pointY) {
        // タッチ終了
        LAppMinimumLive2DManager live2DManager = LAppMinimumLive2DManager.getInstance();
        live2DManager.onDrag(0.0f, 0.0f);

        // シングルタップ
        // 論理座標変換した座標を取得
        float x = deviceToScreen.transformX(touchManager.getLastX());
        // 論理座標変換した座標を取得
        float y = deviceToScreen.transformY(touchManager.getLastY());
    }

    /**
     * X座標をView座標に変換する
     *
     * @param deviceX デバイスX座標
     * @return ViewX座標
     */
    public float transformViewX(float deviceX) {
        // 論理座標変換した座標を取得
        float screenX = deviceToScreen.transformX(deviceX);
        // 拡大、縮小、移動後の値
        return viewMatrix.invertTransformX(screenX);
    }

    /**
     * Y座標をView座標に変換する
     *
     * @param deviceY デバイスY座標
     * @return ViewY座標
     */
    public float transformViewY(float deviceY) {
        // 論理座標変換した座標を取得
        float screenY = deviceToScreen.transformY(deviceY);
        // 拡大、縮小、移動後の値
        return viewMatrix.invertTransformX(screenY);
    }

    private final CubismMatrix44 deviceToScreen = CubismMatrix44.create(); // デバイス座標からスクリーン座標に変換するための行列
    private final CubismViewMatrix viewMatrix = new CubismViewMatrix();   // 画面表示の拡縮や移動の変換を行う行列
    private int programId;

    /**
     * レンダリングターゲットのクリアカラー
     */
    private final float[] clearColor = new float[4];

    private CubismOffscreenSurfaceAndroid renderingBuffer;

    private LAppMinimumSprite renderingSprite;

    private final TouchManager touchManager = new TouchManager();
}
