[English](README.md) / [日本語](README.ja.md)

---

# Cubism Java Samples

This is a sample implementation of an application that displays models output by the Live2D Cubism Editor.

It is used in conjunction with Cubism Java Framework and Live2D Cubism Core Java.

## License

Please check the [license](LICENSE.md) before using this SDK.


## Compatibility with Cubism 5 new features and previous Cubism SDK versions

This SDK is compatible with Cubism 5.
For SDK compatibility with new features in Cubism 5 Editor, please refer to [here](https://docs.live2d.com/en/cubism-sdk-manual/cubism-5-new-functions/).
For compatibility with previous versions of Cubism SDK, please refer to [here](https://docs.live2d.com/en/cubism-sdk-manual/compatibility-with-cubism-5/).


## Directory structure

```
.
├─ Core             # Directory containing Live2D Cubism Core Java
├─ Framework        # Directory containing source code for rendering and animation functions
└─ Sample/src
   ├─ full          # Full version of sample application
   ├─ main          # Common code for full and minimum
      └─ assets     # Directory containing resources such as model files and images
   └─ minimum       # Sample application with reduced and simplified functions
```

## Cubism Java Framework

Provides various functions for displaying and manipulating the model.

[Cubism Java Framework] is included as a submodule of this repository.
After cloning this repository, the files will be added by cloning the submodule.

[Cubism Java Framework]: (https://github.com/Live2D/CubismJavaFramework)

## Live2D Cubism Core for Java

A library for loading the model.
Live2D Cubism Core for Java is not included in this repository.

To download, please refer to [this](https://www.live2d.com/download/cubism-sdk/download-java/) page. Copy the contents of the downloaded ZIP file to the 'Core' directory of this repository and link it to the program.

## How to build

This sample uses Gradle as the build tool. Please build the entire project after opening it in any IDE.

It is recommended to open and build the project in Android Studio. Other IDEs do not generate a 'local.properties' file, so you will need to create it yourself.

## SDK manual

[Cubism SDK Manual](https://docs.live2d.com/cubism-sdk-manual/top/)

## Changelog

Samples : [CHANGELOG.md](CHANGELOG.md)

Framework : [CHANGELOG.md](Framework/CHANGELOG.md)

Core : [CHANGELOG.md](Core/CHANGELOG.md)

## Development environment

| Development Tools | Version |
|-------------------|--|
| Android Studio | Meerkat Feature Drop 2024.3.2 |
| Gradle | 8.7 |
| Android Gradle Plugin | 8.6.1 |
| Gradle JDK | 21.0.7 |
| Android SDK | 35.0.0 |

## Operation environment

### Java

This sample application runs with **Java SE 7** or higher Java versions.

### Android

| Version | Device   | Emulator |
|---------|----------|:--------:|
| 15      | Pixel 7a |          |
| 5.0     | Pixel 7a |    ✔     |

This sample application runs with **Android API 21** or higher Android versions.

## Contributing

There are many ways to contribute to the project: logging bugs, submitting pull requests on this GitHub, and reporting issues and making suggestions in Live2D Community.

### Forking And Pull Requests

We very much appreciate your pull requests, whether they bring fixes, improvements, or even new features. To keep the main repository as clean as possible, create a personal fork and feature branches there as needed.

### Bugs

We are regularly checking issue-reports and feature requests at Live2D Community. Before filing a bug report, please do a search in Live2D Community to see if the issue-report or feature request has already been posted. If you find your issue already exists, make relevant comments and add your reaction.

### Suggestions

We're also interested in your feedback for the future of the SDK. You can submit a suggestion or feature request at Live2D Community. To make this process more effective, we're asking that you include more information to help define them more clearly.

## Forum

If you want to suggest or ask questions about how to use the Cubism SDK between users, please use the forum.

- [Live2D Creator's Forum](https://community.live2d.com/)
- [Live2D 公式クリエイターズフォーラム (Japanese)](https://creatorsforum.live2d.com/)
