# 🔥 Spark Launcher

> A sleek, anime-themed Minecraft launcher for Android
> **Author:** SparkyNox | **Part of the Yori Ecosystem**

[![Build](https://github.com/sparkynox/Spark-Launcher/actions/workflows/build.yml/badge.svg)](https://github.com/sparkynox/spark-launcher/actions)
![Platform](https://img.shields.io/badge/platform-Android%2026%2B-green)
![License](https://img.shields.io/badge/license-GPL--3.0-blue)

---

## ✨ Features

### 🎨 Themes (All Built-in)
| Theme | Style |
|-------|-------|
| **Cyber Waifu** *(default)* | Neon cyberpunk anime |
| **Sakura Night** | Cherry blossom pink |
| **Miku Teal** | Vocaloid teal & aqua |
| **Demon Slayer** | Dark crimson warrior |
| **Pastel Idol** | Soft idol concert pastels |
| **Dark Spark** | Standard dark + orange |
| **Minecraft Classic** | Dirt & grass blocks |
| **Nether Realm** | Lava & netherrack |
| **The End** | Ender purple void |
| **Deep Ocean** | Deep sea blue |

### 🎮 Game Support
- ✅ **Cracked / Offline** login (any username)
- ✅ **Microsoft / Official** login (MSAL OAuth)
- ✅ Multiple game instances with version management
- ✅ Fabric, Forge, Quilt, NeoForge, Vanilla support
- ✅ Minecraft 1.7.10 → 1.21.x+

### 🛒 Integrated Mod Store
- 🟢 **Modrinth** — mods, resource packs, shaders, modpacks
- 🟠 **CurseForge** — mods, resource packs, shaders, modpacks
- Select which **instance** to install mods into
- Background downloads with progress notification

### ⚙️ All Settings from PojavLauncher + Mojo Launcher
- **Java:** RAM allocation, Java version (8/11/17/21), GC type, custom JVM args
- **Renderer:** OpenGL ES 2/3, Vulkan via Zink, VirGL
- **Controls:** Mouse speed, gyroscope, haptic feedback, virtual mouse, profiles
- **Performance:** FPS cap, performance mode, game scale, background limits
- **Download:** Thread count, auto-dependency install

### 📱 Technical
- Java 17 / Kotlin — optimized for mobile
- Hilt dependency injection
- Room database for instances
- WorkManager for background downloads
- Glide for low-memory image loading
- ProGuard + R8 minification on release
- MultiDex support (32/64-bit)

---

## 🏗️ Building

### Requirements
- Android Studio Hedgehog+
- JDK 17
- Android SDK 34

### Debug build
```bash
./gradlew assembleDebug
```

### Release build
```bash
./gradlew assembleRelease
```
APKs are split by ABI automatically: `arm64-v8a`, `armeabi-v7a`, `x86_64`, `x86`, plus universal.

---

## 🚀 CI/CD (GitHub Actions)

| Trigger | Result |
|---------|--------|
| Push to `main`/`dev` | Debug APK artifact |
| `workflow_dispatch` | Debug or Release APK |
| Push tag `v*.*.*` | Release APKs + GitHub Release + Discord notify |

See [SECRETS.md](SECRETS.md) for required secrets setup.

---

## 📁 Project Structure

```
SparkLauncher/
├── app/src/main/
│   ├── java/com/sparkynox/sparklauncher/
│   │   ├── data/
│   │   │   ├── api/          # Modrinth + CurseForge Retrofit services
│   │   │   ├── models/       # Unified data models
│   │   │   ├── repository/   # Auth, Instance, Mod repositories
│   │   │   └── services/     # DownloadService (foreground)
│   │   ├── di/               # Hilt modules (Network, Database)
│   │   ├── theme/            # ThemeManager (10 built-in themes)
│   │   ├── ui/
│   │   │   ├── activities/   # Splash, Auth, Main, Settings, Game
│   │   │   ├── adapters/     # Instance, Mod, Theme adapters
│   │   │   ├── fragments/    # Home, ModStore, Settings sub-pages
│   │   │   └── viewmodels/   # Auth, Home, ModStore ViewModels
│   │   └── utils/            # PreferencesManager, CrashHandler
│   └── res/
│       ├── drawable/         # Theme backgrounds, icons
│       ├── layout/           # All XML layouts
│       ├── raw/              # MSAL config, Lottie animations
│       └── values/           # Colors, strings, themes
├── .github/workflows/
│   └── build.yml             # CI/CD: Android APK + iOS IPA
└── SECRETS.md                # GitHub secrets documentation
```

---

## 🙏 Credits

- [PojavLauncher](https://github.com/PojavLauncherTeam/PojavLauncher) — Java runtime bridge
- [Mojo Launcher](https://github.com/MojoLauncher) — UI inspiration
- [Modrinth API](https://docs.modrinth.com) — Open mod platform
- [CurseForge API](https://console.curseforge.com) — Mod platform

---

*Spark Launcher is not affiliated with Mojang Studios or Microsoft.*
