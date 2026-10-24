# AI-Based Crop Disease Detection and Remedy Recommendation Mobile Application
### Final Year Project (FYP) — BS Information Technology
**Islamia University of Bahawalpur (IUB)**  
**Developer:** Saqlain Niazi

---

## 📌 Project Overview
This project presents an intelligent, deep-learning-driven mobile and cloud backend solution designed to diagnose major rice crop diseases (`Bacterial Blight`, `Blast`, `Brown Spot`, and identify `Healthy` crops) and automatically prescribe dual-language pesticide and organic remedy guidance in both **English** and localized **Urdu (اردو)**.

---

## 🏗️ Architecture & Tech Stack
- **Mobile Frontend (Flutter):** Dart, Material Design 3, `image_picker`, `http`.
- **Backend API Bridge (Flask):** Python 3, Flask, CORS, PIL (Pillow), NumPy.
- **Machine Learning (CNN):** TensorFlow / Keras Sequential Convolutional Neural Network (Conv2D × 3, MaxPooling2D, Dense 64, Softmax 4).
- **Native Android Prototype (Jetpack Compose):** Kotlin, Material 3, Edge-to-Edge, FileProvider Camera, PickVisualMedia Photo Picker.

---

## 🚀 Quick Setup & Installation Guide

### 1. Python Backend Setup (`flask_backend/`)
In your terminal or VS Code terminal:
```bash
# Navigate to backend folder
cd flask_backend

# (Optional but recommended) Create virtual environment
python -m venv venv
# On Windows:
venv\Scripts\activate
# On Linux / macOS:
source venv/bin/activate

# Install required dependencies
pip install -r requirements.txt

# (Optional) Generate/Train the model binary
python train.py

# Start the Flask API Server on host 0.0.0.0 and port 5000
python app.py
```
The server will start listening at: `http://0.0.0.0:5000`  
Endpoint: `POST http://10.0.2.2:5000/predict` (for Android Emulator) or `http://<your-pc-ip>:5000/predict` (for physical phones).

---

### 2. Flutter Mobile Frontend Setup (`flutter_project/`)
```bash
# Navigate to Flutter project
cd flutter_project

# Fetch dependencies
flutter pub get

# Check connected devices
flutter devices

# Run the Flutter App
flutter run
```

---

## 🔍 Network Endpoint Specification

### `POST /predict`
- **Content-Type:** `multipart/form-data`
- **Form Key:** `file` (binary image stream: `.jpg`, `.jpeg`, `.png`)
- **Image Pipeline:** Resized to `(224, 224)`, normalized `0.0 - 1.0`, batched to `[1, 224, 224, 3]`.

#### Sample JSON Response:
```json
{
  "status": "success",
  "prediction": "Bacterial Blight",
  "confidence": 0.9624,
  "class_index": 0,
  "developer": "Saqlain Niazi",
  "institution": "Islamia University of Bahawalpur (IUB)",
  "remedies": {
    "disease_urdu": "بیکٹیریل بلائٹ (پتوں کا بیکٹیریائی جھلسائو)",
    "pesticide_english": "Spray Copper Oxychloride 50% WP @ 2.5 g/L or Streptocycline/Plantomycin (9:1) @ 100 mg/L of water.",
    "pesticide_urdu": "کاپر آکسی کلورائڈ 50 فیصد WP بحساب 2.5 گرام فی لیٹر یا اسٹریپٹوسائکلین 100 ملی گرام فی لیٹر پانی میں ملا کر سپرے کریں۔",
    "advice_english": "Drain stagnant water from the field for 3-4 days to arrest bacterial multiplication. Avoid top-dressing urea/nitrogen fertilizers during active infection.",
    "advice_urdu": "کھیت سے کھڑا پانی فوری نکالیں تاکہ ہوا لگے اور بیکٹیریا کا پھیلاؤ رک جائے۔ بیماری کے دوران یوریا یا نائٹروجن والی کھاد بالکل نہ ڈالیں۔"
  }
}
```

---

## 👨‍💻 Developer Attribution
- **Student:** Saqlain Niazi
- **Degree:** BS Information Technology (BS IT)
- **Institution:** The Islamia University of Bahawalpur (IUB)
