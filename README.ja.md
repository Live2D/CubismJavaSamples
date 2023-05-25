[English](README.md) / [日本語](README.ja.md)

---

# Cubism Java Samples

Live2D Cubism 4 Editor で出力したモデルを表示するアプリケーションのサンプル実装です。
Cubism Java Framework および Live2D Cubism Core Javaと組み合わせて使用します。

## ライセンス

本 SDK を使用する前に[ライセンス](LICENSE.md)をご確認ください。

## ディレクトリ構成

```
.
├─ Core             # Live2D Cubism Core Javaが含まれるディレクトリ
├─ Framework        # レンダリングやアニメーション機能などのソースコードが含まれるディレクトリ
└─ Samples
  ├─ full           # フルバージョンのサンプルアプリ
  ├─ main           # full, minimumに共通するコード
        └─ assets   # モデルのファイルや画像などのリソースが含まれるディレクトリ
  └─ minimum        # 機能を減らしシンプルにしたサンプルアプリ
```

## Cubism Java Framework

モデルを表示、操作するための各種機能を提供します。

[Cubism Java Framework] は、当リポジトリのサブモジュールとして含まれています。
当リポジトリをクローンした後、サブモジュールのクローンを実行することでファイルが追加されます。

[Cubism Java Framework]: (https://github.com/Live2D/CubismJavaFramework)

## Live2D Cubism Core for Java

モデルをロードするためのライブラリです。
当リポジトリには Live2D Cubism Core for Java は同梱されていません。

ダウンロードするには[こちら](https://www.live2d.com/download/cubism-sdk/download-java/)のページを参照ください。
ダウンロードした Zip ファイルの中身を当リポジトリの `Core` ディレクトリにコピーし、プログラムにリンクさせてください。

## ビルド方法

本サンプルではビルドツールにGradleを採用しています。任意のIDEで開いた後プロジェクト全体のビルドを実行してください。

Android Studioでプロジェクトを開きビルドすることを推奨します。それ以外のIDEではlocal.propertiesファイルが生成されないため自分で作成する必要があります。

## SDKマニュアル

[Cubism SDK Manual](https://docs.live2d.com/cubism-sdk-manual/top/)

## 変更履歴

当リポジトリの変更履歴については [CHANGELOG.md](CHANGELOG.md) を参照ください。

## 開発環境

| 開発ツール          | バージョン            |
|----------------|------------------|
| Android Studio | Flamingo 2022.2.1 Patch 1 |
| IntelliJ IDEA  | 2023.1.1         |
| CMake          | 3.1.0            |
| Gradle         | 6.9              |

### Android

| Android SDK tools | バージョン        |
| --- |--------------|
| Android NDK | 21.4.7075529 |
| Android SDK | 33.0.0       |
| CMake | 3.1.0        |

## 動作確認環境

### Java

本サンプルアプリケーションは**Java SE 6**以上のJavaバージョンで動作します。

### Android
| バージョン | デバイス     | Tegra |
|-------|----------|-------|
| 13    | Pixel 6a |  |
| 7.1.1 | Nexus 9  | ✔ |
| 4.1   | Pixel 5  |  |

本サンプルアプリケーションは**Android API 16**以上のAndroidバージョンで動作します。

## プロジェクトへの貢献

プロジェクトに貢献する方法はたくさんあります。バグのログの記録、このGitHubでのプルリクエストの送信、Live2Dコミュニティでの問題の報告と提案の作成です。

### フォークとプルリクエスト

修正、改善、さらには新機能をもたらすかどうかにかかわらず、プルリクエストに感謝します。ただし、ラッパーは可能な限り軽量で浅くなるように設計されているため、バグ修正とメモリ/パフォーマンスの改善のみを行う必要があることに注意してください。メインリポジトリを可能な限りクリーンに保つために、必要に応じて個人用フォークと機能ブランチを作成してください。

### バグ

Live2Dコミュニティでは、問題のレポートと機能リクエストを定期的にチェックしています。バグレポートを提出する前に、Live2Dコミュニティで検索して、問題のレポートまたは機能リクエストがすでに投稿されているかどうかを確認してください。問題がすでに存在する場合は、関連するコメントを追記してください。

### 提案

SDKの将来についてのフィードバックにも関心があります。Live2Dコミュニティで提案や機能のリクエストを送信できます。このプロセスをより効果的にするために、それらをより明確に定義するのに役立つより多くの情報を含めるようお願いしています。

## コミュニティ

ユーザー同士でCubism SDKの活用方法の提案や質問をしたい場合は、是非コミュニティをご活用ください。

- [Live2D 公式コミュニティ](https://creatorsforum.live2d.com/)
- [Live2D community(English)](http://community.live2d.com/)
