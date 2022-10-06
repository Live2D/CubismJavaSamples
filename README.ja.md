[English](README.md) / [日本語](README.ja.md)

---

# Cubism Java Samples

Live2D Cubism 4 Editor で出力したモデルを表示するアプリケーションのサンプル実装です。
Cubism Java Framework および Live2D Cubism Core Javaと組み合わせて使用します。

このリポジトリは**alpha版**となっています。バグ報告やご提案がありましたら、GitHubの機能で[issue](https://github.com/Live2D/CubismJavaSamples/issues)を立てて弊社までお寄せください。

## ライセンス

本 SDK を使用する前に[ライセンス](LICENSE.md)をご確認ください。

## お知らせ

本 SDK を使用する前に [お知らせ](NOTICE.ja.md)をご確認ください。

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

ダウンロードするには[こちら](https://creatorsforum.live2d.com/t/topic/1110)のページを参照ください。
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
| Android Studio | Dolphin 2021.3.1 |
| IntelliJ IDEA  | 2022.1.4         |
| CMake          | 3.1.0            |
| Gradle         | 6.9              |

### Android

| Android SDK tools | バージョン        |
| --- |--------------|
| Android NDK | 21.4.7075529 |
| Android SDK | 30.0.0       |
| CMake | 3.1.0        |

## 動作確認環境

### Java

本サンプルアプリケーションは**Java SE 6**以上のJavaバージョンで動作します。

### Android

| バージョン | デバイス              | Tegra |
|-------|-------------------|-------|
| 12    | Redmi Note 10 Pro |  |
| 7.1.1 | Nexus 9           | ✔ |
| 4.1   | Pixel 4a          |  |

本サンプルアプリケーションは**Android API 16**以上のAndroidバージョンで動作します。


## コミュニティ

ユーザー同士でCubism SDKの活用方法の提案や質問をしたい場合は、是非コミュニティをご活用ください。

- [Live2D 公式コミュニティ](https://creatorsforum.live2d.com/)
- [Live2D community(English)](http://community.live2d.com/)
