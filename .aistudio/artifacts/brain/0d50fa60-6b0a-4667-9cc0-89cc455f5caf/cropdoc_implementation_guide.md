# CropDoc AI — AI-Based Crop & Orchard Tree Botanical Disease Detection and Remedy Recommendation System
### BS Information Technology Final Year Project 2026
**The Islamia University of Bahawalpur (IUB)**  
**Developer:** Saqlain Niazi

---

## 🌟 Upgraded 2026 Botanical & Anti-Misguidance Capabilities

1. **Two-Stage Hierarchical Botanical Vision Pipeline:**
   - **Stage 1 (Anti-Misguidance Quality Guard):**
     - Automatically verifies whether the input image contains authentic agricultural leaf foliage and vegetative chlorophyll signals (Green Leaf Index).
     - If non-leaf objects (human faces, floors, inanimate objects, or random images) are provided, the system prevents diagnosis to protect the farmer:
       > *"⚠️ غیر تصدیق شدہ یا نامعلوم نمونہ: کسان کو کسی غلط تشخیص یا غیر ضروری دوائی کے اخراجات سے بچانے کے لیے اسکین کا نتیجہ بلاک کر دیا گیا ہے۔"*
   - **Stage 2 (Botanical Leaf Morphology & Plant Architecture):**
     - Identifies the specific leaf structure before diagnosing pathology:
       - **کپاس (Cotton):** Palmate 3-5 lobed leaf, reticulate venation.
       - **گندم (Wheat):** Linear elongated blade, parallel veins.
       - **دھان (Rice):** Rough lanceolate blade with central keel.
       - **آم (Mango):** Oblong waxy leathery leaf with alternating pinnate veins.
       - **کنو (Citrus/Kinnow):** Glossy oval leaf with distinct winged petiole and oil glands.
       - **امرود (Guava):** Opposite paired elliptic leaves with prominent raised ribs.
       - **کماد (Sugarcane):** Wide arching leaf with thick white midrib.
       - **سیب (Apple):** Serrated ovate leaves.

2. **Precise Disease Location on Plant (بیماری پودے کے کس حصے پر واقع ہے):**
   - Directly maps where the pathogen attacks:
     - Cotton CLCuV: Underside of leaf (پتے کی نچلی سطح پر سفید مکھی کے ڈنک اور رگوں کا موٹا ہونا)
     - Wheat Yellow Rust: Upper leaf surface in linear parallel stripe rows (پتے کی اوپری سطح پر لمبائی کے رخ پیلی دھاریاں)
     - Mango Anthracnose: Leaf tip and margin necrotic dieback (پتے کی نوک، کناروں اور کیری کے ڈنڈل پر)
     - Citrus Canker: Both upper and lower leaf surfaces with corky raised pustules and yellow halos
     - Guava Wilt: Terminal branch leaf bronzing and vascular root browning
     - Rice Blast: Spindle-shaped eye spots on leaf blade and panicle neck

3. **Pakistani Farmer Visual Asset (کسان کی تصویر اور رہنمائی):**
   - High-fidelity stylized Pakistani farmer hero graphic (`ic_pakistani_farmer`) integrated on the home dashboard with warm greetings ("خوش آمدید پیارے کسان بھائی!").

4. **Automated Natural Urdu Voice Assistant:**
   - Mentions the farmer's name, speaks the botanical leaf structure, describes where the disease is located, prescribes Government of Pakistan (DPP & PARC) approved brands and dosages, gives irrigation guidance, and states the monitoring period.

5. **Updated 2026 Academic Title:**
   - Top Bar: **"IUB BS IT • Final Year Project 2026"**
   - Bottom Footer: **"Developer: Saqlain Niazi (BS Information Technology • Islamia University of Bahawalpur - IUB 2026)"**

---

## 📋 Deliverable Files in Artifacts

| File | Description |
|------|-------------|
| **`flutter_main.dart`** | Complete, non-placeholder Flutter app (`lib/main.dart`) with farmer hero, leaf morphology guide, and 2026 branding |
| **`flask_backend_app.py`** | Python Flask REST API (`app.py`) on `0.0.0.0:5000` with 2-stage botanical vision heuristics and anti-misguidance quality guard |
| **`cnn_model_train.py`** | Standalone TensorFlow/Keras CNN training pipeline (`train.py`) |
| **`cropdoc_implementation_guide.md`** | Master documentation and installation manual |
| **`flutter_pubspec.yaml`** | Complete `pubspec.yaml` with all required dependencies |

---

## 🏗️ Multi-Tier Architecture

```
[ Crop Leaf Photo Taken in Field ]
               │
               ▼
┌────────────────────────────────────────────────────────┐
│  Stage 1: Anti-Misguidance Quality Guard               │
│  • Vegetative Chlorophyll / GLI Spectral Check         │
│  • Rejects Non-Foliage Objects (Protects Farmer)       │
└──────────────────────────────┬─────────────────────────┘
                               │ (Valid Leaf Verified)
                               ▼
┌────────────────────────────────────────────────────────┐
│  Stage 2: Botanical Leaf Morphology Classifier         │
│  • Palmate (Cotton) vs Linear (Wheat/Rice)             │
│  • Oblong Waxy (Mango) vs Winged Petiole (Citrus)      │
│  • Opposite Elliptic (Guava)                           │
└──────────────────────────────┬─────────────────────────┘
                               │
                               ▼
┌────────────────────────────────────────────────────────┐
│  Stage 3: Pathology Spectrum & Disease Symptom Locator │
│  • Pinpoints Disease Location on Plant & Foliage       │
│  • Pakistan DPP & PARC Approved Medicines + Dosages    │
│  • Water/Irrigation Directives & Monitoring Days       │
└──────────────────────────────┬─────────────────────────┘
                               │
                               ▼
┌────────────────────────────────────────────────────────┐
│  Stage 4: Automated Urdu Voice Assistant (TTS)         │
│  • Speaks to Farmer by Name with Complete Treatment    │
└────────────────────────────────────────────────────────┘
```

---

## 🚀 Step-by-Step Running Instructions

### 1. Running the Flask Backend:
```bash
pip install flask flask-cors pillow numpy tensorflow
python app.py
```
Server starts on `http://0.0.0.0:5000`.

### 2. Running the Flutter Frontend:
```bash
flutter pub get
flutter run
```

### 3. Developer & Academic Credits:
- **Project Title:** AI-Based Crop & Orchard Tree Disease Detection and Remedy Recommendation Mobile Application
- **Year:** 2026
- **Developer:** Saqlain Niazi
- **Degree:** BS Information Technology
- **Institution:** The Islamia University of Bahawalpur (IUB), Punjab, Pakistan
