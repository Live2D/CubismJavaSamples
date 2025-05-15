# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).


## [5-r.4] - 2025-05-15

### Added

* Add a flag to enable the function that verifies the consistency when loading `motion3.json`.

### Changed

* Change the blend mode when using the `USE_RENDER_TARGET` or `USE_MODEL_RENDER_TARGET` flags in `LAppDefine`, and apply Premultiplied Alpha to the color settings of rendering targets.
Also adjust the return value of the getSpriteAlpha function.


## [5-r.3] - 2025-02-18

### Fixed

* Fix a bug where the application crashes when tapping the model repeatedly.
* Fix a bug where the priority was not reset if the motion was not read correctly.

### Changed

* Change the compile and target SDK version of Android OS to 15.0 (API 35).
  * Upgrade the version of Android Gradle Plugin from 8.1.1 to 8.6.1.
  * Upgrade the version of Gradle from 8.2 to 8.7.
  * Change the minimum version of Android Studio to Ladybug(2024.2.1).
* Change the default JDK version for compilation to 17 using Gradle's Java toolchain.


## [5-r.2] - 2024-11-07

### Added

* Add a function to notify when motion playback starts.
* Add right orientation to the fixed orientations in the Android app.
* Add a feature to change render target to the minimum sample.

### Fixed

* Fix a hang-up caused by an improper function call in the application termination process.
* Fix a bug that caused white edge-like objects to be drawn when enabling the `USE_RENDER_TARGET` or `USE_MODEL_RENDER_TARGET` flag in `LAppDefine`.
* Fix a bug where the method of obtaining color location differs between full sample and minimum sample.

### Removed

* Remove armeabi-v7a from architecture support.
* Remove the `startMotion()` method that receives a callback in the minimum sample.
  * If you want to use the callback feature, please check the Full sample.


## [5-r.1] - 2024-03-26

### Added

* Add a OpenGL shader setup class.

### Changed

* Change so that `LAppSprite` does not depend on `LAppDelegate`.
* Change to read shader source codes from files.

### Fixed

* Fix a problem where two application icons are generated when installing the Minimum sample application on a device.

### Removed

* Remove description of CMake from development environment in README files.
  * CMake is not actually used.
* Remove the unnecessary `if` branch due to raising the minimum API version.
* Remove the description of NDK version.
  * Because of not using NDK.
* Remove the unnecessary variables in the `LAppSprite` class.


## [5-r.1-beta.3] - 2024-01-18

### Changed

* Change the compile and target SDK version of Android OS to 14.0 (API 34).
  * Upgrade the version of Android Gradle Plugin from 8.0.2 to 8.1.1.
  * Upgrade the version of Gradle from 8.1.1 to 8.2.
  * Change the minimum version of Android Studio to Hedgehog(2023.1.1).

### Fixed

* Fix a problem where the result loaded exp3.json is put into map even if it was null.
* Replace deprecated notation in `build.gradle` and `AndroidManifest.xml`.


## [5-r.1-beta.2] - 2023-09-28

### Changed

* Replace the sample model `Mao` with the updated version that is compatible with Cubism 5.0.

### Fixed

* Fix a problem where the matrix used to draw a model was calculated incorrectly.


## [5-r.1-beta.1] - 2023-08-17

### Added

* Add Wankoromochi as a model bundled with SDK.

### Changed

* Change the minimum support version of Android OS to 5.0 (API 21).
* Unify Offscreen drawing-related terminology with `OffscreenSurface`.
* Adjust to automatically search and use models in the assets folder.


## [4-r.1] - 2023-05-25

### Added

* Add some functions for checking consistency of MOC3 files.
  * Add the function of checking consistency in `setupModel()` in `LAppModel` and `LAppMinimumModel`.
  * Add the function of checking consistency before loading a model. (`hasMocConsistencyFromFile()` in `LAppModel` and `LAppMinimumModel`)
  * This feature is enabled by default. Please see the following manual for more information.
    * https://docs.live2d.com/cubism-sdk-manual/moc3-consistency/

### Changed

* Change so that when `USE_MODEL_RENDER_TARGET` is defined, one model will apply the opacity obtained from the motion.

### Removed

* Remove unnecessary files in the assets folder.

### Fixed

* Fix a problem in which `haru` motion and voice were incorrect combination.
* Fix a problem in `LAppWavFileHandler`class that some audio could not be played correctly because the sampling rate was the fixed value.

## [4-r.1-beta.4] - 2023-03-16

### Fixed

* Fix some problems related to Cubism Core.
  * See `CHANGELOG.md` in Core.

## [4-r.1-beta.3] - 2023-03-10

### Added

* Add funciton to validate MOC3 files.
  * See `CHANGELOG.md` in Core and Framework.

## [4-r.1-beta.2] - 2023-01-26

### Changed

* Change Android SDK API level from 31 (Android 12) to 33 (Android 13).
* Change the import statement of `LAppDefine` class to reflect the changes in CubismFramework.
* Change the processing where `getMotionMap` function in `CubismModelSettingJson` was used.
* Change setup code of a renderer in `loadAssets` function in `LAppModel` and `LAppMinimumModel` classes to match CubismFramework changes.
* Change the key type of Map used in `setupModel` function in `LAppModel` and `LAppMinimumModel` that store model layout information to match the CubismFramework changes.

### Fixed

* Fix `onDestroy` function in `LAppDelegate` and `LAppMinimumDelegate` to release a singleton instance.

### Removed

* Remove dependencies not to be used.
* Remove unnecessary commenting lines.
* Remove unused variables defined in `startMotion` function of `LAppModel` and `LAppMinimumModel` classes.

## [4-r.1-beta.1] - 2022-12-08

### Fixed

* Fix `resize` function in `LAppSprite` because resizing calculation is wrong.
* Remove the glEnable(GL_TEXTURE_2D) instruction, which was not valid.
* Fix `onUpdate` function in `LAppLive2DManager` not to create an instance of CubismMatrix44.

## 4-r.1-alpha.1 - 2022-10-06

### Added

* New released!


[5-r.4]: https://github.com/Live2D/CubismJavaSamples/compare/5-r.3...5-r.4
[5-r.3]: https://github.com/Live2D/CubismJavaSamples/compare/5-r.2...5-r.3
[5-r.2]: https://github.com/Live2D/CubismJavaSamples/compare/5-r.1...5-r.2
[5-r.1]: https://github.com/Live2D/CubismJavaSamples/compare/5-r.1-beta.3...5-r.1
[5-r.1-beta.3]: https://github.com/Live2D/CubismJavaSamples/compare/5-r.1-beta.2...5-r.1-beta.3
[5-r.1-beta.2]: https://github.com/Live2D/CubismJavaSamples/compare/5-r.1-beta.1...5-r.1-beta.2
[5-r.1-beta.1]: https://github.com/Live2D/CubismJavaSamples/compare/4-r.1...5-r.1-beta.1
[4-r.1]: https://github.com/Live2D/CubismJavaSamples/compare/4-r.1-beta.4...4-r.1
[4-r.1-beta.4]: https://github.com/Live2D/CubismJavaSamples/compare/4-r.1-beta.3...4-r.1-beta.4
[4-r.1-beta.3]: https://github.com/Live2D/CubismJavaSamples/compare/4-r.1-beta.2...4-r.1-beta.3
[4-r.1-beta.2]: https://github.com/Live2D/CubismJavaSamples/compare/4-r.1-beta.1...4-r.1-beta.2
[4-r.1-beta.1]: https://github.com/Live2D/CubismJavaSamples/compare/4-r.1-alpha.1...4-r.1-beta.1
