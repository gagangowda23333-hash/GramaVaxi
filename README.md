# 🐄 Grama-Vaxi  
### Smart Livestock Health & Vaccine Alert System

Grama-Vaxi is a modern Android application designed to help rural livestock farmers digitally manage animal healthcare, vaccination schedules, and disease reporting.

The app focuses on preventing livestock deaths caused by missed government vaccination camp announcements by providing smart reminders, digital animal records, and health alerts.

---

# 📱 Features

## ✅ Animal Management
- Register livestock animals
- Add breed, age, gender, notes
- Edit/Delete animal records
- Digital livestock health cards

## ✅ Smart Vaccine Scheduling
- Auto-generated vaccine schedules
- Species-based vaccine recommendations
- Upcoming vaccine reminders
- Overdue vaccine tracking

## ✅ Vaccination Calendar
- Timeline-based vaccine tracking
- Color-coded status indicators
- Upcoming and overdue schedules

## ✅ Notifications
- WorkManager-based vaccine reminders
- Notifications even when app is closed
- Vaccination camp alerts

## ✅ Disease Reporting
- Report sick animals
- Add symptoms and severity
- AI-based health suggestions (simulated)

## ✅ Firebase Backend
- Firebase Authentication
- Firebase Firestore database
- Real-time cloud sync

## ✅ Modern UI
- Material 3 design
- Responsive dashboard
- Smooth animations
- Dark/Light theme support

## ✅ Multilingual Support
- English
- Kannada

---

# 🧠 Problem Statement

In rural India, livestock farmers often miss government vaccination camp announcements because information is communicated through village loudspeakers.

Missing vaccinations can lead to:
- Disease outbreaks
- Animal deaths
- Major financial losses for farming families

Grama-Vaxi solves this problem by digitizing livestock healthcare management and delivering proactive health reminders.

---

# 🏗️ Tech Stack

## Frontend
- Kotlin
- Jetpack Compose
- Material 3
- Navigation Compose

## Backend
- Firebase Authentication
- Firebase Firestore

## Android Components
- WorkManager
- NotificationManager
- StateFlow
- MVVM Architecture

## Libraries
- Coil
- Firebase SDK
- Android Jetpack

---

# 📂 Project Structure

```bash
com.example.gramavaxi
│
├── data
│   ├── model
│   ├── repository
│   ├── remote
│
├── navigation
├── ui
│   ├── screens
│   ├── components
│   ├── theme
│
├── utils
├── viewmodel
├── worker
```

---

# 🔥 Firebase Setup

## 1. Create Firebase Project
Go to:
https://console.firebase.google.com

## 2. Add Android App
Package Name:
```bash
com.example.gramavaxi
```

## 3. Download google-services.json
Place file inside:
```bash
app/google-services.json
```

## 4. Enable Services
- Authentication → Anonymous Login
- Firestore Database
- Firebase Storage (optional)

---

# ⚙️ Gradle Dependencies

```gradle
implementation(platform("com.google.firebase:firebase-bom:33.1.0"))

implementation("com.google.firebase:firebase-auth-ktx")
implementation("com.google.firebase:firebase-firestore-ktx")

implementation("androidx.navigation:navigation-compose:2.7.7")

implementation("androidx.work:work-runtime-ktx:2.9.0")

implementation("io.coil-kt:coil-compose:2.6.0")
```

---

# 🚀 How to Run

1. Clone the project
2. Open in Android Studio
3. Add Firebase `google-services.json`
4. Sync Gradle
5. Run the app on emulator/device

---

# 📸 Main Screens

- Splash Screen
- Dashboard
- Animal Ledger
- Vaccine Calendar
- Disease Report Screen
- Profile Screen

---

# 🔔 Notification System

The app uses WorkManager for:
- Vaccine reminders
- Vaccination camp alerts
- Background scheduling

Notifications work even when the app is closed.

---

# 🌐 Offline Support

Core functionality works offline using local state handling and Firestore sync.

---

# 🎯 Future Improvements

- Real veterinarian portal
- GPS camp tracking
- AI disease prediction
- SMS alerts
- WhatsApp integration
- Government API integration

---

# 👨‍💻 Developed By

**Gagan H N**  
MindMatrix VTU Internship Program

---
