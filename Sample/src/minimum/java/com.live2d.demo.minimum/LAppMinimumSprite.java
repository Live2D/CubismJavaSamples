/*
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at http://live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

package com.live2d.demo.minimum;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TEXTURE_2D;

public class LAppMinimumSprite {
    public LAppMinimumSprite(
        float x,
        float y,
        float width,
        float height,
        int textureId,
        int programId
    ) {
        rect.left = x - width * 0.5f;
        rect.right = x + width * 0.5f;
        rect.up = y + height * 0.5f;
        rect.down = y - height * 0.5f;

        this.textureId = textureId;

        // 何番目のattribute変数か
        positionLocation = GLES20.glGetAttribLocation(programId, "position");
        uvLocation = GLES20.glGetAttribLocation(programId, "uv");
        textureLocation = GLES20.glGetUniformLocation(programId, "texture");
        colorLocation = GLES20.glGetUniformLocation(programId, "baseColor");

        spriteColor[0] = 1.0f;
        spriteColor[1] = 1.0f;
        spriteColor[2] = 1.0f;
        spriteColor[3] = 1.0f;
    }

    /**
     * テクスチャIDを指定して描画する
     *
     * @param textureId テクスチャID
     * @param uvVertex uv頂点座標
     */
    public void renderImmediate(int textureId, final float[] uvVertex) {
        // attribute属性を有効にする
        GLES20.glEnableVertexAttribArray(positionLocation);
        GLES20.glEnableVertexAttribArray(uvLocation);

        // uniform属性の登録
        GLES20.glUniform1i(textureLocation, 0);

        // 頂点データ
        float[] positionVertex = {
            (rect.right - maxWidth * 0.5f) / (maxWidth * 0.5f), (rect.up - maxHeight * 0.5f) / (maxHeight * 0.5f),
            (rect.left - maxWidth * 0.5f) / (maxWidth * 0.5f), (rect.up - maxHeight * 0.5f) / (maxHeight * 0.5f),
            (rect.left - maxWidth * 0.5f) / (maxWidth * 0.5f), (rect.down - maxHeight * 0.5f) / (maxHeight * 0.5f),
            (rect.right - maxWidth * 0.5f) / (maxWidth * 0.5f), (rect.down - maxHeight * 0.5f) / (maxHeight * 0.5f)
        };

        // attribute属性を登録
        {
            ByteBuffer bb = ByteBuffer.allocateDirect(positionVertex.length * 4);
            bb.order(ByteOrder.nativeOrder());
            FloatBuffer buffer = bb.asFloatBuffer();
            buffer.put(positionVertex);
            buffer.position(0);

            GLES20.glVertexAttribPointer(positionLocation, 2, GL_FLOAT, false, 0, buffer);
        }
        {
            ByteBuffer bb = ByteBuffer.allocateDirect(uvVertex.length * 4);
            bb.order(ByteOrder.nativeOrder());
            FloatBuffer buffer = bb.asFloatBuffer();
            buffer.put(uvVertex);
            buffer.position(0);

            GLES20.glVertexAttribPointer(uvLocation, 2, GL_FLOAT, false, 0, buffer);
        }

        GLES20.glUniform4f(colorLocation, spriteColor[0], spriteColor[1], spriteColor[2], spriteColor[3]);

        // モデルの描画
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);
    }

    // リサイズする
    public void resize(float x, float y, float width, float height) {
        rect.left = x - width * 0.5f;
        rect.right = x + width * 0.5f;
        rect.up = y + height * 0.5f;
        rect.down = y - height * 0.5f;
    }

    public void setColor(float r, float g, float b, float a) {
        spriteColor[0] = r;
        spriteColor[1] = g;
        spriteColor[2] = b;
        spriteColor[3] = a;
    }

    /**
     * ウィンドウサイズを設定する。
     *
     * @param width 横幅
     * @param height 高さ
     */
    public void setWindowSize(int width, int height) {
        maxWidth = width;
        maxHeight = height;
    }

    /**
     * Rectクラス
     */
    private static final class Rect {
        //左辺
        public float left;
        //右辺
        public float right;
        //上辺
        public float up;
        // 下辺
        public float down;
    }

    private final Rect rect = new Rect();
    private final int textureId;

    private final int positionLocation;  // 位置アトリビュート
    private final int uvLocation; // UVアトリビュート
    private final int textureLocation;   // テクスチャアトリビュート
    private final int colorLocation;     // カラーアトリビュート
    private final float[] spriteColor = new float[4];   // 表示カラー

    private int maxWidth;   // ウィンドウ幅
    private int maxHeight;  // ウィンドウ高さ
}
