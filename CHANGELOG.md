# Changelog — Spark Launcher

## [1.0.0] — Initial Release
**Author:** SparkyNox

### Added
- 10 built-in themes (5 anime, 5 standard) — no external imports
- Cracked (offline) + Microsoft (official) account support
- Integrated Mod Store: Modrinth + CurseForge
  - Mods, resource packs, shaders, modpacks
  - Per-instance installation selector
  - Shimmer loading states
- Game instance management (create, edit, delete, select)
- Full settings coverage:
  - Java/JVM: RAM, version, GC type, custom args
  - Renderer: OpenGL ES 2/3, Vulkan Zink, VirGL
  - Controls: mouse speed, gyro, haptic, profiles
  - Performance: FPS cap, game scale, background limits
  - Download: thread count, auto-deps
- GitHub Actions CI/CD:
  - Android APK (arm64, armv7, x86_64, x86, universal)
  - iOS IPA via Capacitor (release tags only)
  - Discord release notifications
- Crash logging to external storage
- Animated splash screen
- Anime mascot characters per theme (GIF)
- ProGuard + R8 release minification
- MultiDex 32/64-bit support
