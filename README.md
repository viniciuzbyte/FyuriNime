# Fyuri Nime 🦊

Aplikasi Android (Kotlin + Jetpack Compose) untuk menjelajahi dan melacak progres menonton anime,
dengan sistem XP, Level, Rank, dan Achievement — tema dark "soft clean" dengan loading animation
bergaya Crunchyroll (rotating ring + pulsing dot).

## ⚠️ Catatan penting soal sumber data

Aplikasi ini **tidak** terhubung ke `anime.razul.tech` atau situs streaming anime tidak berlisensi
lainnya. Situs tersebut mendistribusikan ulang konten berhak cipta tanpa izin dan memakai sistem
anti-bot (nonce/counter verification) yang jelas menandakan akses otomatis tidak diizinkan.

Sebagai gantinya, metadata anime (judul, sinopsis, rating, cover, genre) diambil dari
**[Jikan API](https://jikan.moe)** — wrapper publik, gratis, dan legal di atas data MyAnimeList.
Slot pemutaran video (`Media3/ExoPlayer`) sudah disiapkan di dependency, tapi **belum** disambungkan
ke sumber video manapun — silakan sambungkan ke video milikmu sendiri atau provider streaming
berlisensi resmi.

## Stack

- Kotlin + Jetpack Compose (Material 3)
- Hilt (DI)
- Room (progress XP/level/rank, achievement, watch history — lokal di device)
- DataStore Preferences (sesi login & onboarding)
- Retrofit + Moshi → Jikan API
- Coil (image loading)
- Media3/ExoPlayer (siap pakai untuk pemutaran video legal)

## Alur Aplikasi

1. **Splash** — animasi loading ala Crunchyroll (ring berputar + dot berdenyut)
2. **Login / Register** — akun lokal on-device (belum ada backend remote)
3. **Onboarding** — "Kamu tahu Fyuri Nime dari mana?" (Teman, Google, Twitch, Discord, dll.)
4. **Home** — trending & musim ini, data dari Jikan API
5. **Profile** — XP, Level, Rank (Rookie → Otaku → Veteran → Sensei → Legend), daftar Achievement

## Sistem Gamifikasi

- 1 menit nonton = 1 XP
- Level naik mengikuti kurva: `xp = 50 × level × (level-1)`
- Achievement contoh: *First Step* (40 menit), *Binge Starter* (5 jam), *Dedicated Fan* (20 jam), *Marathoner* (50 jam)
- Panggil `UserRepository.recordWatchSession(animeId, title, minutesWatched)` dari layar player untuk update XP & achievement otomatis

## Build lokal

Project ini tidak menyertakan Gradle Wrapper binary (`gradle-wrapper.jar`) karena dibuat tanpa akses
jaringan. Install Gradle 8.9 di mesinmu lalu jalankan:

```bash
gradle assembleDebug
```

Atau generate wrapper dulu (sekali saja, butuh koneksi internet):

```bash
gradle wrapper --gradle-version 8.9
./gradlew assembleDebug
```

## CI/CD (GitHub Actions)

Workflow `.github/workflows/android-ci.yml` sudah siap: setiap push ke `main`/`master` akan
build debug APK dan upload sebagai artifact, tanpa butuh wrapper jar (pakai `gradle/actions/setup-gradle`).

## TODO lanjutan

- [ ] Sambungkan layar detail anime & player (Media3) ke sumber video legal
- [ ] Ganti akun lokal dengan backend auth sungguhan (Firebase Auth / API sendiri) kalau perlu multi-device
- [ ] Tambah search & filter genre di Home
- [ ] Tambah lebih banyak achievement & leaderboard komunitas
# FyuriNime
