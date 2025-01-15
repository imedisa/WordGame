# Word Game (بازی کلمه‌سازی) 🎮

## Developer Information
- **Developer:** Madisa Hamzeh (مدیسا حمزه)
- **Project Type:** Android Game Application
- **lecturer:** Mr.Ahmadpanah
- **Version:** 1.0.0

## Project Overview 📱

An engaging word puzzle game developed for Android platforms where players must unscramble letters to form correct words. The game features progressive difficulty levels, a lives system, and Firebase integration for progress tracking.

### Key Features 🌟

- Multiple game stages with increasing difficulty
- Stage-based progression system
- Lives system (5 lives per stage)
- Progress saving functionality
- Scrambled letter mechanics
- Clean and intuitive UI
- Firebase backend integration
- Score tracking system

## Technical Specifications 🛠

### Development Environment
- Language: Kotlin
- Platform: Android
- Minimum SDK: API 21 (Android 5.0)
- Target SDK: API 34 (Android 14)
- Database: Firebase Realtime Database

### Project Structure
```
app/
├── src/
│   ├── main/
│   │   ├── java/com/example/myapplication/
│   │   │   ├── GameActivity.kt
│   │   │   ├── MainActivity.kt
│   │   │   ├── StagesActivity.kt
│   │   │   └── StageManager.kt
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   ├── activity_main.xml
│   │   │   │   ├── activity_game.xml
│   │   │   │   └── activity_stages.xml
│   │   │   └── values/
│   │   │       ├── strings.xml
│   │   │       ├── colors.xml
│   │   │       └── themes.xml
│   │   └── AndroidManifest.xml
└── build.gradle
```

## Game Mechanics 🎯

1. **Stage Selection:**
   - Players start from Stage 1
   - New stages unlock upon completing previous stages
   - 10 stages per section

2. **Gameplay:**
   - Players receive scrambled letters
   - Must arrange letters to form correct words
   - 5 lives per stage
   - Wrong attempts reduce lives
   - Stage completion unlocks next level

3. **Progress System:**
   - Automatic progress saving
   - Firebase integration for cross-device sync
   - Stage unlock tracking

## Installation Guide 📥

1. Clone the repository:
```bash
git clone [repository-url]
```

2. Open in Android Studio

3. Configure Firebase:
   - Add your `google-services.json`
   - Update Firebase dependencies
   - Enable Realtime Database

4. Build and run the project



## Persian Documentation (مستندات فارسی) 📝

### معرفی پروژه
این پروژه یک بازی کلمه‌سازی اندروید است که در آن بازیکنان باید با استفاده از حروف بهم‌ریخته، کلمات صحیح را بسازند. بازی دارای مراحل مختلف با سختی افزایشی است و از سیستم امتیازدهی و ذخیره‌سازی پیشرفت بهره می‌برد.

### ویژگی‌های اصلی
- دارای مراحل متعدد با سختی افزایشی
- سیستم قفل مراحل
- سیستم جان (5 جان در هر مرحله)
- ذخیره‌سازی خودکار پیشرفت
- رابط کاربری ساده و جذاب
- اتصال به فایربیس
- سیستم امتیازدهی

### نحوه اجرای پروژه
1. دریافت کد از مخزن
2. باز کردن پروژه در Android Studio
3. تنظیم فایربیس
4. اجرای پروژه

### ساختار کد
پروژه شامل سه اکتیویتی اصلی است:
- `MainActivity`: صفحه اصلی بازی
- `StagesActivity`: صفحه انتخاب مراحل
- `GameActivity`: صفحه بازی

### نیازمندی‌های سیستم
- اندروید 5.0 و بالاتر
- دسترسی به اینترنت برای ذخیره‌سازی پیشرفت

## Contact 📫
- Email: medisayep@gmail.com


---
Made with ❤️ by Madisa Hamzeh
