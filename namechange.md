# DUNE Name & Link Audit

Complete audit of all brand names, app names, and links throughout the codebase.

---

## 1. App Name References (DUNE / Dune / dune)

### Build Configuration (`app/build.gradle.kts`)
| Line | Value | Context |
|------|-------|---------|
| 24 | `applicationId = "dune.enhanced.tv"` | Default application ID |
| 66 | `applicationIdSuffix = ".debug"` | Debug suffix |
| 81 | `resValue("string", "app_id", "dune.enhanced.tv")` | Standard flavor |
| 82 | `resValue("string", "app_search_suggest_authority", "dune.enhanced.tv.content")` | Standard flavor |
| 83 | `resValue("string", "app_search_suggest_intent_data", "content://dune.enhanced.tv.content/intent")` | Standard flavor |
| 88 | `applicationId = "Dune.enhanced.tv"` | Enhanced flavor |
| 94 | `resValue("string", "app_name_release", "DUNE")` | Enhanced flavor display name |
| 118 | `output.outputFileName = "Dune.androidtv-0.1.1.apk"` | APK filename |
| 120 | `output.outputFileName = "Dune.androidtv-${versionName}.apk"` | APK filename template |
| 143 | `description = "Builds the enhanced version with package ID: Dune.enhanced.tv"` | Task description |
| 147 | `println("Package ID: Dune.enhanced.tv")` | Build output |
| 149 | `println("App Name: DUNE")` | Build output |
| 150 | `println("Filename: Dune.androidtv-0.1.1.apk")` | Build output |

### Source Code
| File | Line | Value | Context |
|------|------|-------|---------|
| `util/AppUpdater.kt` | 178 | `"DUNE_${version}.apk"` | Downloaded APK filename |
| `telemetry/TelemetryService.kt` | 96 | `"client: Dune,a Jellyfin Client for Android TV"` | Telemetry client ID |
| `di/AppModule.kt` | 93 | `ClientInfo("Dune Android TV", ...)` | SDK client info |
| `util/profile/deviceProfileReport.kt` | 49 | `"client: Dune for Android TV"` | Device profile |
| `di/PlaybackModule.kt` | 85 | `R.drawable.dune_icon` | Notification icon |
| `ui/preference/screen/AboutScreen.kt` | 54 | `R.drawable.dune_logo` | About screen logo |
| `ui/preference/screen/AuthPreferencesScreen.kt` | 188 | `R.drawable.dune_logo` | Auth screen logo |
| `ui/preference/screen/UserPreferencesScreen.kt` | 435 | `R.drawable.dune_logo` | Preferences logo |

### String Resources (`app/src/main/res/values/strings.xml`)
| Line | Key | Value |
|------|-----|-------|
| 5 | `app_name` | `DUNE` |
| 6 | `app_name_release` | `DUNE` |
| 7 | `app_name_debug` | `DUNE Debug` |
| 153 | `searchable_hint` | `Search Dune` |
| 810 | `login_help_description` | `Dune requires a server...` |
| 841 | `welcome_title` | `Welcome to Dune!` |
| 892 | `no_network_permissions` | `Dune requires network permissions...` |
| 956 | `exit_app_title` | `Exit DUNE` |

### Localized Strings (languages with DUNE/Dune references)
- `values-ar/` - Arabic
- `values-ca/` - Catalan
- `values-de/` - German
- `values-en-rGB/` - English (GB)
- `values-es/` - Spanish
- `values-fr/` - French
- `values-it/` - Italian
- `values-nb/` - Norwegian
- `values-nl/` - Dutch
- `values-sv/` - Swedish

### Layout / Resource Files
| File | Line | Value | Context |
|------|------|-------|---------|
| `res/layout/fragment_select_server.xml` | 53 | `tools:text="DUNE-androidtv 1.0.0 debug"` | Example text |
| `res/layout/view_button_server.xml` | 18 | `@drawable/dune_icon` | Server button icon |
| `res/drawable/dune_icon.png` | - | - | App icon (PNG) |
| `res/drawable/dune_logo.xml` | - | - | App logo (Vector XML) |

### Documentation
| File | Line | Value |
|------|------|-------|
| `README.md` | 2 | Image alt text "DUNE" |
| `README.md` | 5 | `# DUNE - Jellyfin Android TV Client` |
| `README.md` | 14 | `"DUNE Screenshot"` |
| `README.md` | 19 | `**DUNE** is a modified version...` |
| `README.md` | 66 | `cd DUNE-main` |
| `README.md` | 84 | `package ID Dune.enhanced.tv` |
| `CONTRIBUTING.md` | 1 | `# Contributing to Dune` |
| `CONTRIBUTING.md` | 3 | `contributing to Dune!` |
| `NOTICE` | 1 | `Dune - Jellyfin Android TV Client` |

### .gitignore
| Line | Value |
|------|-------|
| 130 | `Dune.androidtv-0.0.8-enhanced-release.dm` |
| 132 | `Dune.androidtv-0.0.8-enhanced-release.dm` |

---

## 2. Jellyfin References

### Package Namespace
- `org.jellyfin.androidtv` - Main app namespace (`app/build.gradle.kts:16`)
- `org.jellyfin.preference` - Preference module (`preference/build.gradle.kts:8`)
- `org.jellyfin.playback.*` - Playback modules
- `"jellyfin-androidtv-Enhanced"` - Root project name (`settings.gradle.kts:3`)

### Deep Link Scheme
- `jellyfin://` - Custom URI scheme (`AndroidManifest.xml:196, 209`)

### SharedPreferences
- `"org.jellyfin.androidtv.preferences"` (`JellyfinApplication.kt:162`)

### Application Class
- `android:name=".JellyfinApplication"` (`AndroidManifest.xml:63`)

### Theme
- `@style/Theme.Jellyfin` (`AndroidManifest.xml:75`)
- `@style/Theme.Jellyfin.Preferences` (`AndroidManifest.xml:216`)
- `res/values/theme_jellyfin.xml`

### Drawable Resources
- `res/drawable/jellyfin_button.xml`
- `res/drawable/jellyfin_button_minimal.xml`
- `res/drawable/qr_jellyfin_docs.xml`

### Dependencies (`gradle/libs.versions.toml`)
- `jellyfin-sdk` (line 53)
- `jellyfin-androidx-media3-ffmpeg-decoder` (line 100)
- Jellyfin media/SDK/apiclient versions (lines 29-31)

### Localized Strings
- Nearly every `values-*/strings.xml` contains `searchable_hint` referencing "Jellyfin"
- Login help descriptions reference `docs.jellyfin.org` in 30+ languages

---

## 3. GitHub URLs

### This Project (Sam42a/DUNE)
| File | Line | URL | Context |
|------|------|-----|---------|
| `README.md` | 8 | `https://github.com/Sam42a/DUNE/releases/latest` | Release badge link |
| `README.md` | 9 | `https://github.com/Sam42a/DUNE/stargazers` | Stars badge link |
| `README.md` | 65 | `https://github.com/Sam42a/DUNE.git` | Clone command |
| `CONTRIBUTING.md` | 14 | `https://github.com/your-username/DUNE.git` | Fork example |
| `util/AppUpdater.kt` | 36 | `https://api.github.com/repos/Sam42a/DUNE/releases/latest` | Auto-update API |
| `telemetry/TelemetryService.kt` | 98 | `https://github.com/Sam42a/DUNE` | Telemetry report |
| `util/profile/deviceProfileReport.kt` | 51 | `https://github.com/Sam42a/DUNE` | Device profile |

### Upstream Jellyfin
| File | Line | URL |
|------|------|-----|
| `README.md` | 21 | `https://github.com/jellyfin/jellyfin-androidtv` |
| `README.md` | 35 | `https://github.com/LitCastVlog/jellyfin-androidtv-OLED` |
| `README.md` | 90 | `https://github.com/jellyfin/sdk-kotlin` |
| `NOTICE` | 14 | `https://github.com/jellyfin/jellyfin-androidtv` |
| `NOTICE` | 36 | `https://github.com/jellyfin/sdk-kotlin` |
| `CONTRIBUTING.md` | 7 | `https://github.com/jellyfin/.github/blob/master/CODE_OF_CONDUCT.md` |
| `AppTheme.kt` | 14 | `https://github.com/LitCastVlog/jellyfin-androidtv-OLED` |
| `renovate.json` | 4 | `github>jellyfin/.github//renovate-presets/gradle` |

### Third-Party Libraries
| File | URL |
|------|-----|
| `README.md` | `https://github.com/Kotlin/kotlinx.coroutines` |
| `README.md` | `https://github.com/JakeWharton/timber` |
| `README.md` | `https://github.com/ACRA/acra` |
| `NOTICE` | `https://github.com/noties/Markwon` |

### Contributors (`CONTRIBUTORS.md`)
GitHub profiles: bfayers, dkanada, thornbill, grafixeyehero, BnMcG, Letroll, AndreasGB, joern-h, nielsvanvelzen, GodTamIt, sparky3387, mohd-akram, 3l0w, MajMongoose, Olaren15, dtrexler, ebr11

---

## 4. Other External URLs

### Image Hosting
| File | URL | Context |
|------|-----|---------|
| `README.md` | `https://files.catbox.moe/jqk9rl.jpg` | Banner image |
| `README.md` | `https://i.imgur.com/4Oe1APd.jpeg` | Screenshot |

### Badges (shields.io)
- License badge, release badge, stars badge, support badge in `README.md`

### Donation
| File | URL |
|------|-----|
| `README.md` | `https://coff.ee/sam42` |

### Jellyfin Services
| File | URL | Context |
|------|-----|---------|
| `README.md` | `https://jellyfin.org/` | Main site |
| `README.md` | `https://translate.jellyfin.org/...` | Weblate translations |
| Various strings.xml | `docs.jellyfin.org` | Login help docs (30+ languages) |

### YouTube
| File | Line | URL / Value |
|------|------|-------------|
| `AndroidManifest.xml` | 57 | `youtube.com` (intent filter host) |
| `TrailerUtils.kt` | 9 | `YOUTUBE_HOST = "youtube.com"` |
| `TrailerUtils.kt` | 11 | `YOUTUBE_URL = "https://youtube.com/watch?v="` |
| `FullDetailsFragmentHelper.kt` | 176 | `https://www.youtube.com/results?search_query=` |

### Build Repositories
| File | URL |
|------|-----|
| `settings.gradle.kts` | `https://s01.oss.sonatype.org/content/repositories/snapshots/` |

### Documentation Sites
- `https://developer.android.com/jetpack/androidx`
- `https://insert-koin.io/`
- `https://coil-kt.github.io/coil/`
- `https://noties.io/Markwon/`
- `https://kotest.io/`
- `https://mockk.io/`
- `https://exoplayer.dev/`
- `https://kotlinlang.org/docs/coding-conventions.html`
- `https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html`

---

## 5. Application IDs Summary

| Variant | Application ID | Debug ID |
|---------|---------------|----------|
| Standard | `dune.enhanced.tv` | `dune.enhanced.tv.debug` |
| Enhanced | `Dune.enhanced.tv` | `Dune.enhanced.tv.debug` |

### Content Providers (per variant)
- `${applicationId}.fileprovider`
- `${applicationId}.content`
- `${applicationId}.androidx-startup`
- `${applicationId}.integration.provider.ImageProvider`

---

## Statistics

| Category | Count |
|----------|-------|
| DUNE/Dune/dune occurrences | 70+ |
| Jellyfin references | 1000+ (package names, imports, strings) |
| GitHub URLs | 35+ distinct |
| Other external URLs | 50+ |
| Localized string files with brand refs | 30+ languages |
| Drawable resources with brand names | 4 files |
