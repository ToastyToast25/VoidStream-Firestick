<p align="center">
  <img src="VOIDSTREAM (2).png" alt="VOIDSTREAM" width="100%">
</p>

# VOIDSTREAM - Jellyfin Android TV Client

[![License: GPL v2](https://img.shields.io/badge/License-GPL_v2-blue?labelColor=555555&style=for-the-badge)](https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html)
[![Latest Release](https://img.shields.io/github/v/release/ToastyToast25/VoidStream-Firestick?label=Latest%20Release&labelColor=555555&style=for-the-badge)](https://github.com/ToastyToast25/VoidStream-Firestick/releases/latest)
[![GitHub Stars](https://img.shields.io/github/stars/ToastyToast25/VoidStream-Firestick?label=Stars&labelColor=555555&style=for-the-badge)](https://github.com/ToastyToast25/VoidStream-Firestick/stargazers)
[![Support Me](https://img.shields.io/badge/Support_Me-Buy_a_Coffee-orange?labelColor=555555&style=for-the-badge)](https://coff.ee/sam42)

## About

**VOIDSTREAM** is a modified version of the official [Jellyfin](https://jellyfin.org/) Android TV client with enhanced UI/UX and additional customization options.

> **Note**: This is an unofficial fork not affiliated with the Jellyfin project. The official Jellyfin Android TV client can be found at [jellyfin/jellyfin-androidtv](https://github.com/jellyfin/jellyfin-androidtv).

## Install on Amazon Fire TV / Firestick

### Method 1: Downloader App (Easiest)

1. On your Firestick, go to **Settings > My Fire TV > Developer Options**
2. Enable **Install unknown apps** (or **Apps from Unknown Sources**)
3. Install the **Downloader** app from the Amazon Appstore
4. Open Downloader and enter the APK URL from the [latest release](https://github.com/ToastyToast25/VoidStream-Firestick/releases/latest)
5. The APK will download and prompt you to install — tap **Install**
6. Once installed, find VoidStream in your apps list

### Method 2: ADB Sideload (From PC)

1. On your Firestick, go to **Settings > My Fire TV > Developer Options**
2. Enable **ADB Debugging** and **Install unknown apps**
3. Note your Firestick's IP address from **Settings > My Fire TV > About > Network**
4. On your PC, download the APK from the [latest release](https://github.com/ToastyToast25/VoidStream-Firestick/releases/latest)
5. Connect via ADB and install:
```bash
adb connect <firestick-ip>:5555
adb install VoidStream-v*.apk
```

### Method 3: USB Drive

1. Download the APK to a USB drive from the [latest release](https://github.com/ToastyToast25/VoidStream-Firestick/releases/latest)
2. Plug the USB drive into your Firestick (requires OTG adapter for Fire TV Stick)
3. Use a file manager app (e.g. **File Commander** from Amazon Appstore) to browse the USB drive
4. Tap the APK file and select **Install**

> **Tip**: The enhanced version (`VoidStream.enhanced.tv`) can be installed alongside the official Jellyfin app without conflicts.

## Key Features

### Visual & Interface
**Modernized UI Framework**
- Redesigned home screen with improved content hierarchy
- Enhanced login experience with visual feedback
- Default avatars for users without profile images
- Intuitive search interface with voice input
- Multiple theme options including OLED-optimized dark mode, based on [Jellyfin Android TV OLED](https://github.com/LitCastVlog/jellyfin-androidtv-OLED)

### Customization
**Library Presentation**
- Toggle between classic and modern layouts
- Dynamic backdrops from media artwork
- Customizable homescreen rows (genres, favorites, collections)
- Filter which libraries appear in the home carousel

### Media Experience
**Enhanced Playback**
- Advanced subtitle controls with opacity slider
- D-pad left/right seeking during playback
- Extended playback speed options (0.25x - 3.0x)
- Configurable "still watching" prompt for binge sessions
- Smarter intro/outro skip handling during binge watching
- Customizable background effects

### Technical Improvements
- Reduced memory usage
- Faster app startup
- Improved pagination for large libraries
- WebSocket auto-reconnect after standby
- Side by side installation alongside official client
- Built in automatic updates

## Translating

This project uses the same translation system as the original Jellyfin Android TV client. If you'd like to help, please contribute to the [official Jellyfin Weblate instance](https://translate.jellyfin.org/projects/jellyfin-android/jellyfin-androidtv).

## Building from Source

### Requirements
- Android Studio Giraffe (2022.3.1+)
- Android SDK (API 35)
- OpenJDK 21+

### Build Instructions
```bash
# Clone repository
git clone https://github.com/ToastyToast25/VoidStream-Firestick.git
cd VOIDSTREAM-main

# Build standard version
./gradlew assembleStandardRelease

# Or build enhanced version (coexists with official app)
./gradlew assembleEnhancedRelease
```

### Install on Device
```bash
# Install debug version
./gradlew installStandardDebug

# Install enhanced release
./gradlew installEnhancedRelease
```

**Note:** The enhanced version uses package ID `VoidStream.enhanced.tv` which allows it to be installed alongside the original Jellyfin app.

## Third-Party Libraries

This project uses the following third-party libraries:

- **Jellyfin SDK** - [GPL-2.0](https://github.com/jellyfin/sdk-kotlin)
- **AndroidX Libraries** - [Apache-2.0](https://developer.android.com/jetpack/androidx)
- **Kotlin Coroutines** - [Apache-2.0](https://github.com/Kotlin/kotlinx.coroutines)
- **Koin** - [Apache-2.0](https://insert-koin.io/)
- **Coil** - [Apache-2.0](https://coil-kt.github.io/coil/)
- **Markwon** - [Apache-2.0](https://noties.io/Markwon/)
- **Timber** - [Apache-2.0](https://github.com/JakeWharton/timber)
- **ACRA** - [Apache-2.0](https://github.com/ACRA/acra)
- **Kotest** - [Apache-2.0](https://kotest.io/)
- **MockK** - [Apache-2.0](https://mockk.io/)

## Acknowledgments

This project is based on the work of the Jellyfin Contributors. Special thanks to all the developers and community members who have contributed to the Jellyfin Android TV project.

## License

This project is licensed under the **GNU General Public License v2.0 (GPL-2.0)**. See the [LICENSE](LICENSE) file for details.
