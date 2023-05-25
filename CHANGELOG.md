# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

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

[4-r.1]: https://github.com/Live2D/CubismJavaSamples/compare/4-r.1-beta.4...4-r.1
[4-r.1-beta.4]: https://github.com/Live2D/CubismJavaSamples/compare/4-r.1-beta.3...4-r.1-beta.4
[4-r.1-beta.3]: https://github.com/Live2D/CubismJavaSamples/compare/4-r.1-beta.2...4-r.1-beta.3
[4-r.1-beta.2]: https://github.com/Live2D/CubismJavaSamples/compare/4-r.1-beta.1...4-r.1-beta.2
[4-r.1-beta.1]: https://github.com/Live2D/CubismJavaSamples/compare/4-r.1-alpha.1...4-r.1-beta.1
