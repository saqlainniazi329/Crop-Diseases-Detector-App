# CropDoc AI — AI-Based Crop & Orchard Tree Disease Detection and Remedy Recommendation System
### BS Information Technology Final Year Project (FYP)
**The Islamia University of Bahawalpur (IUB)**  
**Developer:** Saqlain Niazi

---

## 🌟 Upgraded Capabilities (All Fruit Trees & Field Crops in Pakistan)

1. **Comprehensive Species Coverage Across Pakistan:**
   - **Orchard Trees:**
     - 🥭 **Mango (آم کا درخت / باغ):** Mango Anthracnose (پتوں و کیری کا کالا دھبہ)
     - 🍊 **Citrus / Kinnow (کنو و مالٹا):** Citrus Canker (پتوں اور پھل کا بیکٹیریائی کوڑھ)
     - 🍈 **Guava (امرود کا درخت):** Guava Wilt (امرود کا سوکڑا - جڑ کا فنگس)
     - 🍎 **Apple (سیب کا درخت):** Apple Scab (سیب کا خارش)
   - **Field Crops:**
     - 🌾 **Cotton (کپاس کی فصل):** Cotton Leaf Curl Virus (پتہ مروڑ وائرس - CLCuV)
     - 🌾 **Wheat (گندم کی فصل):** Yellow Rust / Stripe Rust (پیلی کنگی)
     - 🌾 **Rice Paddy (دھان / باسمتی چاول):** Rice Blast (جھونکا و گردن توڑ), Bacterial Blight, Brown Spot
     - 🎋 **Sugarcane (کماد / گنا):** Red Rot (گنے کا کینسر / سرخ گلن)
     - 🌿 **Healthy (صحت مند درخت / فصل):** Verified pest-free status

2. **Automated Urdu Voice Assistant (خودکار اردو آواز میں کسان کو رہنمائی):**
   - Directly addresses the farmer by name upon detection:
     > *"السلام علیکم پیارے کسان بھائی [نام]! آپ کے آم کے درخت کے پتے کے تجزیے کے بعد تشخیص ہوئی ہے: آم کا اینتھراکنوز۔ حکومتِ پاکستان کے منظور شدہ حل کے مطابق، نیٹیوو (Nativo 75 WG - Bayer) بحساب 65 گرام فی ایکڑ سپرے کریں۔ تنے کے گرد کھڑا پانی فوری نکالیں اور 10 سے 14 دن تک روزانہ معائنہ کریں۔"*
   - Supported natively via Android `TextToSpeech` (`ur-PK`) and dynamic audio speech payload.

3. **Government of Pakistan (DPP & PARC) Approved Agrochemicals:**
   - Official registered trade brand names and exact dosages per acre / 100L:
     - **Bayer Nativo 75 WG** @ 65 g/acre
     - **Syngenta Score 250 EC** @ 125 ml/100L water
     - **Kocide 3000 Copper Hydroxide** @ 200–250 g/acre
     - **Topsin-M 70 WP** @ 400 g root drench
     - **Bayer Movento 240 SC** @ 125 ml/acre
     - **Pyriproxyfen 10.8 EC** @ 500 ml/acre
     - **Tilt 250 EC** @ 200 ml/acre
     - **Beam 75 WP** @ 120 g/acre

4. **Dedicated Agronomic Advisory Modules:**
   - 📅 **کتنے دن نگرانی کرنی چاہیے (Field Monitoring Timeline):** Specific quarantine and monitoring schedules.
   - 💧 **پانی کی صورتحال (Irrigation & Water Guide):** Precise flood avoidance and moisture control directives.

---

## 📋 Deliverable Files in Artifacts

| File | Description |
|------|-------------|
| **`flutter_main.dart`** | Complete, non-placeholder Flutter app (`lib/main.dart`) with camera, gallery, Urdu speech banner, and bold red diagnosis |
| **`flutter_pubspec.yaml`** | Complete `pubspec.yaml` with `image_picker`, `http`, and Material 3 dependencies |
| **`flask_backend_app.py`** | Python Flask REST API (`app.py`) on `0.0.0.0:5000` supporting all Pakistani trees & crops |
| **`cnn_model_train.py`** | Standalone TensorFlow/Keras CNN training pipeline (`train.py`) |
| **`cropdoc_implementation_guide.md`** | Master documentation and installation manual |

---

## 🏗️ Multi-Tier Architecture

```
[ Orchard Tree / Crop Leaf Photo ]
              │
              ▼
┌────────────────────────────────────────────────────────┐
│  Flutter / Android Native Frontend (CropDoc AI)       │
│  • Modern Bio-Tech Leaf & Neural Network Adaptive Icon │
│  • Live Automated Urdu Voice Assistant (TTS Engine)    │
│  • Multi-Tree & Multi-Crop Visual Diagnostic Engine   │
│  • Mandatory Bottom Footer: "Developer: Saqlain Niazi" │
│  • Bold Red Disease Diagnosis + DPP Approved Remedies  │
└──────────────────────────┬─────────────────────────────┘
                           │
             Multipart HTTP POST / Real Computer Vision
                           │
                           ▼
┌────────────────────────────────────────────────────────┐
│  Python Flask Server / Multi-Spectral Leaf Heuristics  │
│  • Green Leaf Index (GLI) & Chlorosis Detection        │
│  • Necrotic Lesion Edge-Entropy Analysis               │
│  • Tree Bark & Foliage Texture Discrimination          │
│  • Pakistan DPP & PARC Pesticide Recommendation DB    │
│  • Dynamic Urdu Audio Narration Script Generation      │
└──────────────────────────┬─────────────────────────────┘
                           │
                           ▼
┌────────────────────────────────────────────────────────┐
│  Firebase Cloud Firestore Backend (asia-southeast1)   │
│  • Zero-Trust Security Rules: Users own their records  │
│  • Real-Time Google Sign-In & Offline Local Cache      │
└────────────────────────────────────────────────────────┘
```

---

## 🚀 Step-by-Step Running Instructions

### 1. Running the Flask Backend:
```bash
pip install flask flask-cors pillow numpy tensorflow
python app.py
```
Server starts globally on `http://0.0.0.0:5000`.

### 2. Running the Flutter App:
```bash
flutter pub get
flutter run
```

### 3. Developer & Academic Credits:
- **Project Title:** AI-Based Crop Disease Detection and Remedy Recommendation Mobile Application
- **Student & Developer:** Saqlain Niazi
- **Degree:** BS Information Technology
- **Institution:** The Islamia University of Bahawalpur (IUB), Punjab, Pakistan
