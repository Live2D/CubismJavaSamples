# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

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

[4-r.1-beta.2]: https://github.com/Live2D/CubismJavaSamples/compare/4-r.1-beta.1...4-r.1-beta.2
[4-r.1-beta.1]: https://github.com/Live2D/CubismJavaSamples/compare/4-r.1-alpha.1...4-r.1-beta.1
