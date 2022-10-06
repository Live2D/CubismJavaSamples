[English](README.md) / [日本語](README.ja.md)

---

# Cubism Java Samples

This is a sample implementation of an application that displays models output by the Live2D Cubism 4 Editor.

It is used in conjunction with Cubism Java Framework and Live2D Cubism Core Java.

This repository is an **alpha version**. If you have any bug reports or suggestions, please send them to us by creating an [issue](https://github.com/Live2D/CubismJavaSamples/issues) using the GitHub feature.

## License

Please check the [license](LICENSE.md) before using this SDK.

## Notices

Please check the [notices](NOTICE.md) before using this SDK.

## Directory structure

```
.
├─ Core             # Directory containing Live2D Cubism Core Java
├─ Framework        # Directory containing source code for rendering and animation functions
└─ Samples
   ├─ full      # Full version of sample application
   ├─ main      # Common code for full and minimum
      └─ assets # Directory containing resources such as model files and images
   ├─ minimum   # Sample application with reduced and simplified functions
```

## Cubism Java Framework

Provides various functions for displaying and manipulating the model.

[Cubism Java Framework] is included as a submodule of this repository.
After cloning this repository, the files will be added by cloning the submodule.

[Cubism Java Framework]: (https://github.com/Live2D/CubismJavaFramework)

## Live2D Cubism Core for Java

A library for loading the model.
Live2D Cubism Core for Java is not included in this repository.

To download, please refer to [this](https://community.live2d.com/discussion/1480/download-cubism-sdk-for-java-alpha/) page. Copy the contents of the downloaded ZIP file to the 'Core' directory of this repository and link it to the program.

## How to build

This sample uses Gradle as the build tool. Please build the entire project after opening it in any IDE.

It is recommended to open and build the project in Android Studio. Other IDEs do not generate a 'local.properties' file, so you will need to create it yourself.

## SDK manual

[Cubism SDK Manual](https://docs.live2d.com/cubism-sdk-manual/top/)

## Changelog

Please refer to [CHANGELOG.md](CHANGELOG.md) for the changelog of this repository.

## Development environment

| Development Tools | Version          |
|-------------------|------------------|
| Android Studio    | Dolphin 2021.3.1 |
| IntelliJ IDEA     | 2022.1.4         |
| CMake             | 3.1.0            |
| Gradle            | 6.9              |

### Android

| Android SDK tools | Version      |
| --- |--------------|
| Android NDK | 21.4.7075529 |
| Android SDK | 30.0.0       |
| CMake | 3.1.0        |

## Operation environment

### Java
This sample application runs with **Java SE 6** or higher Java versions.

### Android

| Version | Device            | Tegra |
|---------|-------------------| --- |
| 12      | Redmi Note 10 Pro ||
| 7.1.1   | Nexus 9           | ✔︎ |
| 4.1   | Pixel 4a          ||

This sample application runs with **Android API 16** or higher Android versions.

## Community

If you would like to make suggestions or ask questions about how to use Cubism SDK among users, please use the community.

- [Live2D community](http://community.live2d.com/)
- [Live2D 公式コミュニティ (Japanese)](https://creatorsforum.live2d.com/)



