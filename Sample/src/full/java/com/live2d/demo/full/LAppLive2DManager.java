/*
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at http://live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

package com.live2d.demo.full;

import com.live2d.sdk.cubism.framework.math.CubismMatrix44;
import com.live2d.sdk.cubism.framework.motion.ACubismMotion;
import com.live2d.sdk.cubism.framework.motion.IBeganMotionCallback;
import com.live2d.sdk.cubism.framework.motion.IFinishedMotionCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.res.AssetManager;

import static com.live2d.demo.LAppDefine.*;

/**
 * サンプルアプリケーションにおいてCubismModelを管理するクラス。
 * モデル生成と破棄、タップイベントの処理、モデル切り替えを行う。
 */
public class LAppLive2DManager {
    public static LAppLive2DManager getInstance() {
        if (s_instance == null) {
            s_instance = new LAppLive2DManager();
        }
        return s_instance;
    }

    public static void releaseInstance() {
        s_instance = null;
    }

    /**
     * 現在のシーンで保持している全てのモデルを解放する
     */
    public void releaseAllModel() {
        for (LAppModel model : models) {
            model.deleteModel();
        }
        models.clear();
    }

    /**
     * assets フォルダにあるモデルフォルダ名をセットする
     */
    public void setUpModel() {
        // assetsフォルダの中にあるフォルダ名を全てクロールし、モデルが存在するフォルダを定義する。
        // フォルダはあるが同名の.model3.jsonが見つからなかった場合はリストに含めない。
        modelDir.clear();

        final AssetManager assets = LAppDelegate.getInstance().getActivity().getResources().getAssets();
        try {
            String[] root = assets.list("");
            for (String subdir: root) {
                String[] files = assets.list(subdir);
                String target = subdir + ".model3.json";
                // フォルダと同名の.model3.jsonがあるか探索する
                for (String file : files) {
                    if (file.equals(target)) {
                        modelDir.add(subdir);
                        break;
                    }
                }
            }
            Collections.sort(modelDir);
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    // モデル更新処理及び描画処理を行う
    public void onUpdate() {
        int width = LAppDelegate.getInstance().getWindowWidth();
        int height = LAppDelegate.getInstance().getWindowHeight();

        for (int i = 0; i < models.size(); i++) {
            LAppModel model = models.get(i);

            if (model.getModel() == null) {
                LAppPal.printLog("Failed to model.getModel().");
                continue;
            }

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
                viewMatrix.multiplyByMatrix(projection);
            }

            // モデル1体描画前コール
            LAppDelegate.getInstance().getView().preModelDraw(model);

            model.update();

            model.draw(projection);     // 参照渡しなのでprojectionは変質する

            // モデル1体描画後コール
            LAppDelegate.getInstance().getView().postModelDraw(model);
        }
    }

    /**
     * 画面をドラッグした時の処理
     *
     * @param x 画面のx座標
     * @param y 画面のy座標
     */
    public void onDrag(float x, float y) {
        for (int i = 0; i < models.size(); i++) {
            LAppModel model = getModel(i);
            model.setDragging(x, y);
        }
    }

    /**
     * 画面をタップした時の処理
     *
     * @param x 画面のx座標
     * @param y 画面のy座標
     */
    public void onTap(float x, float y) {
        if (DEBUG_LOG_ENABLE) {
            LAppPal.printLog("tap point: {" + x + ", y: " + y);
        }

        for (int i = 0; i < models.size(); i++) {
            LAppModel model = models.get(i);

            // 頭をタップした場合表情をランダムで再生する
            if (model.hitTest(HitAreaName.HEAD.getId(), x, y)) {
                if (DEBUG_LOG_ENABLE) {
                    LAppPal.printLog("hit area: " + HitAreaName.HEAD.getId());
                }
                model.setRandomExpression();
            }
            // 体をタップした場合ランダムモーションを開始する
            else if (model.hitTest(HitAreaName.BODY.getId(), x, y)) {
                if (DEBUG_LOG_ENABLE) {
                    LAppPal.printLog("hit area: " + HitAreaName.HEAD.getId());
                }

                model.startRandomMotion(MotionGroup.TAP_BODY.getId(), Priority.NORMAL.getPriority(), finishedMotion, beganMotion);
            }
        }
    }

    /**
     * 次のシーンに切り替える
     * サンプルアプリケーションではモデルセットの切り替えを行う
     */
    public void nextScene() {
        final int number = (currentModel + 1) % modelDir.size();

        changeScene(number);
    }

    /**
     * シーンを切り替える
     *
     * @param index 切り替えるシーンインデックス
     */
    public void changeScene(int index) {
        currentModel = index;
        if (DEBUG_LOG_ENABLE) {
            LAppPal.printLog("model index: " + currentModel);
        }

        String modelDirName = modelDir.get(index);

        String modelPath = ResourcePath.ROOT.getPath() + modelDirName + "/";
        String modelJsonName = modelDirName + ".model3.json";

        releaseAllModel();

        models.add(new LAppModel());
        models.get(0).loadAssets(modelPath, modelJsonName);

        /*
         * モデル半透明表示を行うサンプルを提示する。
         * ここでUSE_RENDER_TARGET、USE_MODEL_RENDER_TARGETが定義されている場合
         * 別のレンダリングターゲットにモデルを描画し、描画結果をテクスチャとして別のスプライトに張り付ける。
         */
        LAppView.RenderingTarget useRenderingTarget;
        if (USE_RENDER_TARGET) {
            // LAppViewの持つターゲットに描画を行う場合こちらを選択
            useRenderingTarget = LAppView.RenderingTarget.VIEW_FRAME_BUFFER;
        } else if (USE_MODEL_RENDER_TARGET) {
            // 各LAppModelの持つターゲットに描画を行う場合こちらを選択
            useRenderingTarget = LAppView.RenderingTarget.MODEL_FRAME_BUFFER;
        } else {
            // デフォルトのメインフレームバッファへレンダリングする(通常)
            useRenderingTarget = LAppView.RenderingTarget.NONE;
        }

        if (USE_RENDER_TARGET || USE_MODEL_RENDER_TARGET) {
            // モデル個別にαを付けるサンプルとして、もう1体モデルを作成し少し位置をずらす。
            models.add(new LAppModel());
            models.get(1).loadAssets(modelPath, modelJsonName);
            models.get(1).getModelMatrix().translateX(0.2f);
        }

        // レンダリングターゲットを切り替える
        LAppDelegate.getInstance().getView().switchRenderingTarget(useRenderingTarget);

        // 別レンダリング先を選択した際の背景クリア色
        float[] clearColor = {0.0f, 0.0f, 0.0f};
        LAppDelegate.getInstance().getView().setRenderingTargetClearColor(clearColor[0], clearColor[1], clearColor[2]);
    }

    /**
     * 現在のシーンで保持しているモデルを返す
     *
     * @param number モデルリストのインデックス値
     * @return モデルのインスタンスを返す。インデックス値が範囲外の場合はnullを返す
     */
    public LAppModel getModel(int number) {
        if (number < models.size()) {
            return models.get(number);
        }
        return null;
    }

    /**
     * シーンインデックスを返す
     *
     * @return シーンインデックス
     */
    public int getCurrentModel() {
        return currentModel;
    }

    /**
     * Return the number of models in this LAppLive2DManager instance has.
     *
     * @return number fo models in this LAppLive2DManager instance has. If models list is null, return 0.
     */
    public int getModelNum() {
        if (models == null) {
            return 0;
        }
        return models.size();
    }

    /**
     * モーション再生時に実行されるコールバック関数
     */
    private static class BeganMotion implements IBeganMotionCallback {
        @Override
        public void execute(ACubismMotion motion) {
            LAppPal.printLog("Motion Began: " + motion);
        }
    }

    private static final BeganMotion beganMotion = new BeganMotion();

    /**
     * モーション終了時に実行されるコールバック関数
     */
    private static class FinishedMotion implements IFinishedMotionCallback {
        @Override
        public void execute(ACubismMotion motion) {
            LAppPal.printLog("Motion Finished: " + motion);
        }
    }

    private static final FinishedMotion finishedMotion = new FinishedMotion();

    /**
     * シングルトンインスタンス
     */
    private static LAppLive2DManager s_instance;

    private LAppLive2DManager() {
        setUpModel();
        changeScene(0);
    }

    private final List<LAppModel> models = new ArrayList<>();

    /**
     * 表示するシーンのインデックス値
     */
    private int currentModel;

    /**
     * モデルディレクトリ名
     */
    private final List<String> modelDir = new ArrayList<>();

    // onUpdateメソッドで使用されるキャッシュ変数
    private final CubismMatrix44 viewMatrix = CubismMatrix44.create();
    private final CubismMatrix44 projection = CubismMatrix44.create();
}
