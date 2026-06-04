/*
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at http://live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

package com.live2d.demo.minimum;

import com.live2d.demo.LAppDefine;
import com.live2d.sdk.cubism.framework.CubismDefaultParameterId;
import com.live2d.sdk.cubism.framework.CubismFramework;
import com.live2d.sdk.cubism.framework.CubismModelSettingJson;
import com.live2d.sdk.cubism.framework.ICubismModelSetting;
import com.live2d.sdk.cubism.framework.effect.CubismLook;
import com.live2d.sdk.cubism.framework.id.CubismId;
import com.live2d.sdk.cubism.framework.id.CubismIdManager;
import com.live2d.sdk.cubism.framework.math.CubismMatrix44;
import com.live2d.sdk.cubism.framework.model.CubismMoc;
import com.live2d.sdk.cubism.framework.model.CubismUserModel;
import com.live2d.sdk.cubism.framework.motion.ACubismMotion;
import com.live2d.sdk.cubism.framework.motion.CubismExpressionMotion;
import com.live2d.sdk.cubism.framework.motion.CubismExpressionUpdater;
import com.live2d.sdk.cubism.framework.motion.CubismLookUpdater;
import com.live2d.sdk.cubism.framework.motion.CubismMotion;
import com.live2d.sdk.cubism.framework.motion.CubismPhysicsUpdater;
import com.live2d.sdk.cubism.framework.motion.CubismPoseUpdater;
import com.live2d.sdk.cubism.framework.motion.IBeganMotionCallback;
import com.live2d.sdk.cubism.framework.motion.IFinishedMotionCallback;
import com.live2d.sdk.cubism.framework.rendering.CubismRenderer;
import com.live2d.sdk.cubism.framework.rendering.android.CubismRenderTargetAndroid;
import com.live2d.sdk.cubism.framework.rendering.android.CubismRendererAndroid;
import com.live2d.sdk.cubism.framework.utils.CubismDebug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LAppMinimumModel extends CubismUserModel {
    public LAppMinimumModel(String modelDirName) {
        CubismIdManager idManager = CubismFramework.getIdManager();

        idParamAngleX = idManager.getId(CubismDefaultParameterId.ParameterId.ANGLE_X.getId());
        idParamAngleY = idManager.getId(CubismDefaultParameterId.ParameterId.ANGLE_Y.getId());
        idParamAngleZ = idManager.getId(CubismDefaultParameterId.ParameterId.ANGLE_Z.getId());
        idParamBodyAngleX = idManager.getId(CubismDefaultParameterId.ParameterId.BODY_ANGLE_X.getId());
        idParamEyeBallX = idManager.getId(CubismDefaultParameterId.ParameterId.EYE_BALL_X.getId());
        idParamEyeBallY = idManager.getId(CubismDefaultParameterId.ParameterId.EYE_BALL_Y.getId());

        modelHomeDirectory = modelDirName;
    }

    public void loadAssets(final String dir, final String fileName) {
        modelHomeDirectory = dir;
        String filePath = modelHomeDirectory + fileName;

        // Setup model
        setupModel(filePath);

        // Setup renderer.
        CubismRenderer renderer = CubismRendererAndroid.create(
            LAppMinimumDelegate.getInstance().getWindowWidth(),
            LAppMinimumDelegate.getInstance().getWindowHeight()
        );
        setupRenderer(renderer);

        setupTextures();
    }

    /**
     * Delete the model which LAppModel has.
     */
    public void deleteModel() {
        delete();
    }

    /**
     * レンダラとテクスチャを再構築する。GLコンテキストが破棄された場合に呼び出す。
     */
    public void reloadRenderer() {
        deleteRenderer();

        CubismRenderer renderer = CubismRendererAndroid.create(
            LAppMinimumDelegate.getInstance().getWindowWidth(),
            LAppMinimumDelegate.getInstance().getWindowHeight()
        );
        setupRenderer(renderer);

        setupTextures();
    }

    /**
     * モデルの更新処理。モデルのパラメーターから描画状態を決定する
     */
    public void update() {
        isUpdated(false);

        final float deltaTimeSeconds = LAppMinimumPal.getDeltaTime();
        _userTimeSeconds += deltaTimeSeconds;

        // モーションによるパラメーター更新の有無
        motionUpdated = false;

        // 前回セーブされた状態をロード
        model.loadParameters();

        // モーションの再生がない場合、待機モーションの中からランダムで再生する
        if (motionManager.isFinished()) {
            startMotion(LAppDefine.MotionGroup.IDLE.getId(), 0, LAppDefine.Priority.IDLE.getPriority());
        } else {
            // モーションを更新
            motionUpdated = motionManager.updateMotion(model, deltaTimeSeconds);
        }

        // モデルの状態を保存
        model.saveParameters();

        // 各種パラメーターの更新（表情・物理・ポーズ・ドラッグ追従など）
        updateScheduler.onLateUpdate(model, deltaTimeSeconds);

        model.update();

        isUpdated(true);
    }

    /**
     * 引数で指定したモーションの再生を開始する。
     *
     * @param group    モーショングループ名
     * @param number   グループ内の番号
     * @param priority 優先度
     * @return 開始したモーションの識別番号を返す。個別のモーションが終了したか否かを判別するisFinished()の引数で使用する。開始できない時は「-1」
     */
    public int startMotion(final String group, int number, int priority) {
        if (priority == LAppDefine.Priority.FORCE.getPriority()) {
            motionManager.setReservationPriority(priority);
        } else if (!motionManager.reserveMotion(priority)) {
            if (LAppDefine.DEBUG_LOG_ENABLE) {
                CubismFramework.coreLogFunction("[APP] cannot start motion.");
            }
            return -1;
        }

        final String fileName = modelSetting.getMotionFileName(group, number);

        // ex) idle_0
        String name = group + "_" + number;

        CubismMotion motion = (CubismMotion) motions.get(name);

        if (motion == null) {
            if (fileName.equals("")) {
                String path = modelHomeDirectory + fileName;

                byte[] buffer;
                buffer = LAppMinimumPal.loadFileAsBytes(path);

                CubismMotion tmpMotion = loadMotion(buffer);
                if (tmpMotion != null) {
                    motion = (CubismMotion) tmpMotion;

                    float fadeInTime = modelSetting.getMotionFadeInTimeValue(group, number);
                    if (fadeInTime != -1.0f) {
                        motion.setFadeInTime(fadeInTime);
                    }

                    float fadeOutTime = modelSetting.getMotionFadeOutTimeValue(group, number);
                    if (fadeOutTime != -1.0f) {
                        motion.setFadeOutTime(fadeOutTime);
                    }
                }
            }
        }

        if (LAppDefine.DEBUG_LOG_ENABLE) {
            CubismFramework.coreLogFunction("[APP] start motion: " + group + "_" + number);
        }

        return motionManager.startMotionPriority(motion, priority);
    }

    public void draw(CubismMatrix44 matrix) {
        if (model == null) {
            LAppMinimumDelegate.getInstance().getActivity().finish();
        }

        // キャッシュ変数の定義を避けるために、multiplyByMatrix()ではなく、multiply()を使用する。
        CubismMatrix44.multiply(
            modelMatrix.getArray(),
            matrix.getArray(),
            matrix.getArray()
        );

        this.<CubismRendererAndroid>getRenderer().setMvpMatrix(matrix);
        this.<CubismRendererAndroid>getRenderer().drawModel();
    }

    public CubismRenderTargetAndroid getRenderingBuffer() {
        return renderingBuffer;
    }

    /**
     * .moc3ファイルの整合性をチェックする。
     *
     * @param mocFileName MOC3ファイル名
     * @return MOC3に整合性があるかどうか。整合性があればtrue。
     */
    public boolean hasMocConsistencyFromFile(String mocFileName) {
        assert mocFileName != null && !mocFileName.isEmpty();

        String path = mocFileName;
        path = modelHomeDirectory + path;

        byte[] buffer = LAppMinimumPal.loadFileAsBytes(path);
        boolean consistency = CubismMoc.hasMocConsistency(buffer);

        if (!consistency) {
            CubismDebug.cubismLogInfo("Inconsistent MOC3.");
        } else {
            CubismDebug.cubismLogInfo("Consistent MOC3.");
        }

        return consistency;
    }

    // model3.jsonからモデルを生成する
    private boolean setupModel(String model3JsonPath) {
        byte[] model3Json = LAppMinimumPal.loadFileAsBytes(model3JsonPath);

        CubismModelSettingJson modelSetting = null;
        modelSetting = new CubismModelSettingJson(model3Json);

        if (modelSetting != null) {
            this.modelSetting = modelSetting;
        }

        // model3.jsonが上手く読み込まれていない場合終了する
        if (this.modelSetting.getJson() == null) {
            if (LAppDefine.DEBUG_LOG_ENABLE) {
                CubismFramework.coreLogFunction("[ERROR]model3.json is not found");
            }
            LAppMinimumDelegate.getInstance().getActivity().finish();
        }

        // Load Cubism Model
        {
            String path = this.modelSetting.getModelFileName();
            if (!path.equals("")) {
                String modelPath = modelHomeDirectory + path;
                byte[] buffer = LAppMinimumPal.loadFileAsBytes(modelPath);

                loadModel(buffer, mocConsistency);
            }
        }

        // load expression files(.exp3.json)
        // 表情モーションの読み込み
        if (this.modelSetting.getExpressionCount() > 0) {
            final int count = this.modelSetting.getExpressionCount();

            for (int i = 0; i < count; i++) {
                String name = this.modelSetting.getExpressionName(i);

                String path = this.modelSetting.getExpressionFileName(i);
                String modelPath = modelHomeDirectory + path;

                byte[] buffer = LAppMinimumPal.loadFileAsBytes(modelPath);

                CubismExpressionMotion motion = loadExpression(buffer);

                expressions.put(name, motion);
            }

            updateScheduler.addUpdatableList(new CubismExpressionUpdater(expressionManager));
        }

        // Pose
        {
            String path = this.modelSetting.getPoseFileName();
            if (!path.equals("")) {
                String modelPath = modelHomeDirectory + path;

                byte[] buffer = LAppMinimumPal.loadFileAsBytes(modelPath);

                loadPose(buffer);
            }
            if (pose != null) {
                updateScheduler.addUpdatableList(new CubismPoseUpdater(pose));
            }
        }

        // Physics
        {
            String path = this.modelSetting.getPhysicsFileName();
            if (!path.equals("")) {
                String modelPath = modelHomeDirectory + path;
                byte[] buffer = LAppMinimumPal.loadFileAsBytes(modelPath);

                loadPhysics(buffer);
            }
            if (physics != null) {
                updateScheduler.addUpdatableList(new CubismPhysicsUpdater(physics));
            }
        }

        // Load UserData
        {
            String path = this.modelSetting.getUserDataFile();
            if (!path.equals("")) {
                String modelPath = modelHomeDirectory + path;
                byte[] buffer = LAppMinimumPal.loadFileAsBytes(modelPath);

                loadUserData(buffer);
            }
        }

        // Look
        {
            look = CubismLook.create();

            List<CubismLook.LookParameterData> lookParameters = new ArrayList<CubismLook.LookParameterData>();
            lookParameters.add(new CubismLook.LookParameterData(idParamAngleX, 30.0f));
            lookParameters.add(new CubismLook.LookParameterData(idParamAngleY, 0.0f, 30.0f));
            lookParameters.add(new CubismLook.LookParameterData(idParamAngleZ, 0.0f, 0.0f, -30.0f));
            lookParameters.add(new CubismLook.LookParameterData(idParamBodyAngleX, 10.0f));
            lookParameters.add(new CubismLook.LookParameterData(idParamEyeBallX, 1.0f));
            lookParameters.add(new CubismLook.LookParameterData(idParamEyeBallY, 0.0f, 1.0f));

            look.setParameters(lookParameters);

            updateScheduler.addUpdatableList(new CubismLookUpdater(look, dragManager));
        }

        updateScheduler.sortUpdatableList();


        // Set layout
        Map<String, Float> layout = new HashMap<String, Float>();
        this.modelSetting.getLayoutMap(layout);

        // If layout information exists, the model matrix is set up from it.
        if (this.modelSetting.getLayoutMap(layout)) {
            modelMatrix.setupFromLayout(layout);
        }

        model.saveParameters();

        // Load motions
        for (int i = 0; i < modelSetting.getMotionGroupCount(); i++) {
            String group = modelSetting.getMotionGroupName(i);
            preLoadMotionGroup(group);
        }

        motionManager.stopAllMotions();

        return true;
    }

    /**
     * モーションデータをグループ名から一括でロードする。
     * モーションデータの名前はModelSettingから取得する。
     *
     * @param group モーションデータのグループ名
     **/
    private void preLoadMotionGroup(final String group) {
        final int count = modelSetting.getMotionCount(group);

        for (int i = 0; i < count; i++) {
            // ex) idle_0
            String name = group + "_" + i;

            String path = modelSetting.getMotionFileName(group, i);
            if (!path.equals("")) {
                String modelPath = modelHomeDirectory + path;

                if (LAppDefine.DEBUG_LOG_ENABLE) {
                    CubismFramework.coreLogFunction("[APP]load motion: " + path + " ==>[" + group + "_" + i + "]");
                }

                byte[] buffer;
                buffer = LAppMinimumPal.loadFileAsBytes(modelPath);

                CubismMotion tmp = loadMotion(buffer);
                if (tmp == null) {
                    continue;
                }
                CubismMotion motion = tmp;

                final float fadeInTime = modelSetting.getMotionFadeInTimeValue(group, i);
                if (fadeInTime != -1.0f) {
                    motion.setFadeInTime(fadeInTime);
                }

                final float fadeOutTime = modelSetting.getMotionFadeOutTimeValue(group, i);
                if (fadeOutTime != -1.0f) {
                    motion.setFadeOutTime(fadeOutTime);
                }

                motion.setEffectIds(eyeBlinkIds, lipSyncIds);

                motions.put(name, motion);
            }
        }
    }

    /**
     * OpenGLのテクスチャユニットにテクスチャをロードする
     */
    private void setupTextures() {
        for (int modelTextureNumber = 0; modelTextureNumber < modelSetting.getTextureCount(); modelTextureNumber++) {
            // テクスチャ名が空文字だった場合はロード・バインド処理をスキップ
            if (modelSetting.getTextureFileName(modelTextureNumber).equals("")) {
                continue;
            }

            // OpenGL ESのテクスチャユニットにテクスチャをロードする
            String texturePath = modelSetting.getTextureFileName(modelTextureNumber);
            texturePath = modelHomeDirectory + texturePath;

            LAppMinimumTextureManager.TextureInfo texture =
                LAppMinimumDelegate.getInstance()
                    .getTextureManager()
                    .createTextureFromPngFile(texturePath);
            final int glTextureNumber = texture.id;

            ((CubismRendererAndroid) getRenderer()).bindTexture(modelTextureNumber, glTextureNumber);

            if (LAppDefine.PREMULTIPLIED_ALPHA_ENABLE) {
                this.<CubismRendererAndroid>getRenderer().isPremultipliedAlpha(true);
            } else {
                this.<CubismRendererAndroid>getRenderer().isPremultipliedAlpha(false);
            }
        }
    }

    private ICubismModelSetting modelSetting;
    /**
     * モデルのホームディレクトリ
     */
    private String modelHomeDirectory;
    /**
     * デルタ時間の積算値[秒]
     */
    private float _userTimeSeconds;

    private final List<CubismId> eyeBlinkIds = new ArrayList<CubismId>();
    private final List<CubismId> lipSyncIds = new ArrayList<CubismId>();
    /**
     * 読み込まれているモーションのマップ
     */
    private final Map<String, ACubismMotion> motions = new HashMap<String, ACubismMotion>();
    /**
     * 読み込まれている表情のマップ
     */
    private final Map<String, ACubismMotion> expressions = new HashMap<String, ACubismMotion>();

    /**
     * パラメーターID: ParamAngleX
     */
    private final CubismId idParamAngleX;
    /**
     * パラメーターID: ParamAngleY
     */
    private final CubismId idParamAngleY;
    /**
     * パラメーターID: ParamAngleZ
     */
    private final CubismId idParamAngleZ;
    /**
     * パラメーターID: ParamBodyAngleX
     */
    private final CubismId idParamBodyAngleX;
    /**
     * パラメーターID: ParamEyeBallX
     */
    private final CubismId idParamEyeBallX;
    /**
     * パラメーターID: ParamEyeBallY
     */
    private final CubismId idParamEyeBallY;
    /**
     * 現フレームでメインモーションがパラメーターを更新したか
     */
    private boolean motionUpdated;

    /**
     * フレームバッファ以外の描画先
     */
    private CubismRenderTargetAndroid renderingBuffer = new CubismRenderTargetAndroid();
}
