package com.live2d.demo.minimum;

import android.opengl.GLES20;

import com.live2d.demo.LAppDefine;
import com.live2d.sdk.cubism.framework.utils.CubismDebug;

/**
 * スプライト用のシェーダー設定を保持するクラス
 */
public class LAppMinimumSpriteShader implements AutoCloseable {
    /**
     * コンストラクタ
     */
    public LAppMinimumSpriteShader() {
        programId = createShader();
    }

    @Override
    public void close() {
        GLES20.glDeleteShader(programId);
    }

    /**
     * シェーダーIDを取得する。
     *
     * @return シェーダーID
     */
    public int getShaderId() {
        return programId;
    }

    /**
     * シェーダーを作成する。
     *
     * @return シェーダーID。正常に作成できなかった場合は0を返す。
     */
    private int createShader() {
        // シェーダーのパスの作成
        String vertShaderFile = LAppDefine.ResourcePath.SHADER_ROOT.getPath();
        vertShaderFile += ("/" + LAppDefine.ResourcePath.VERT_SHADER.getPath());

        String fragShaderFile = LAppDefine.ResourcePath.SHADER_ROOT.getPath();
        fragShaderFile += ("/" + LAppDefine.ResourcePath.FRAG_SHADER.getPath());

        // シェーダーのコンパイル
        int vertexShaderId = compileShader(vertShaderFile, GLES20.GL_VERTEX_SHADER);
        int fragmentShaderId = compileShader(fragShaderFile, GLES20.GL_FRAGMENT_SHADER);

        // プログラムオブジェクトの作成
        int programId = GLES20.glCreateProgram();

        // Programのシェーダーを設定
        GLES20.glAttachShader(programId, vertexShaderId);
        GLES20.glAttachShader(programId, fragmentShaderId);

        GLES20.glLinkProgram(programId);
        GLES20.glUseProgram(programId);

        // 不要になったシェーダーオブジェクトの削除
        GLES20.glDeleteShader(vertexShaderId);
        GLES20.glDeleteShader(fragmentShaderId);

        return programId;
    }

    /**
     * シェーダーをコンパイルする。
     * コンパイルに成功したら0を返す。
     *
     * @param fileName シェーダーファイル名
     * @param shaderType 作成するシェーダーの種類
     * @return シェーダーID。正常に作成できなかった場合は0を返す。
     */
    private int compileShader(String fileName, int shaderType) {
        // ファイル読み込み
        byte[] shaderBuffer = LAppMinimumPal.loadFileAsBytes(fileName);

        // コンパイル
        int shaderId = GLES20.glCreateShader(shaderType);
        GLES20.glShaderSource(shaderId, new String(shaderBuffer));
        GLES20.glCompileShader(shaderId);

        return shaderId;
    }

    private final int programId; // シェーダーID
}
