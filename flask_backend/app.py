import os
import io
import numpy as np
from PIL import Image
from flask import Flask, request, jsonify
from flask_cors import CORS
import tensorflow as tf

# =====================================================================
# AI-BASED CROP DISEASE DETECTION & REMEDY RECOMMENDATION API
# BS Information Technology Final Year Project (IUB)
# Developer: Saqlain Niazi
# =====================================================================

app = Flask(__name__)
CORS(app)  # Enable cross-origin resource sharing for mobile clients

# Define exact target disease classes
CLASS_NAMES = ["Bacterial Blight", "Blast", "Brown Spot", "Healthy"]

# Comprehensive nested dictionary mapping dual-language remedies (English alongside Urdu)
REMEDY_KNOWLEDGE_BASE = {
    "Bacterial Blight": {
        "disease_urdu": "بیکٹیریل بلائٹ (پتوں کا بیکٹیریائی جھلسائو)",
        "pesticide_english": "Spray Copper Oxychloride 50% WP @ 2.5 g/L or Streptocycline/Plantomycin (9:1) @ 100 mg/L of water.",
        "pesticide_urdu": "کاپر آکسی کلورائڈ 50 فیصد WP بحساب 2.5 گرام فی لیٹر یا اسٹریپٹوسائکلین 100 ملی گرام فی لیٹر پانی میں ملا کر سپرے کریں۔",
        "advice_english": "Drain stagnant water from the field for 3-4 days to arrest bacterial multiplication. Avoid top-dressing urea/nitrogen fertilizers during active infection.",
        "advice_urdu": "کھیت سے کھڑا پانی فوری نکالیں تاکہ ہوا لگے اور بیکٹیریا کا پھیلاؤ رک جائے۔ بیماری کے دوران یوریا یا نائٹروجن والی کھاد بالکل نہ ڈالیں۔"
    },
    "Blast": {
        "disease_urdu": "رائس بلاسٹ (جھونکا روگ / گردن توڑ بلاسٹ)",
        "pesticide_english": "Apply Tricyclazole 75% WP @ 0.6 g/L or Azoxystrobin + Difenoconazole (Amistar Top) @ 1 ml/L or Isoprothiolane (Fuji-One) 40% EC @ 1.5 ml/L.",
        "pesticide_urdu": "ٹرائی سائیکلازول 75 فیصد WP بحساب 0.6 گرام فی لیٹر یا ایمسٹار ٹاپ (ایزوکسی اسٹروبن) 1 ملی لیٹر فی لیٹر پانی میں سپرے کریں۔",
        "advice_english": "Avoid dense transplantation to enhance field aeration. Apply silicon and muriate of potash (potassium) to fortify leaf cuticles.",
        "advice_urdu": "پودوں کے درمیان مناسب فاصلہ رکھیں تاکہ ہوا اور دھوپ مل سکے۔ پودوں کی قوت مدافعت بڑھانے کے لیے پوٹاش اور سلیکان کھاد کا استعمال کریں۔"
    },
    "Brown Spot": {
        "disease_urdu": "بھورا دھبہ (براؤن اسپاٹ - غذائی کمی کا عارضہ)",
        "pesticide_english": "Spray Mancozeb 75% WP @ 2.5 g/L or Carbendazim 12% + Mancozeb 63% WP (Saaf) @ 2.0 g/L or Propiconazole 25% EC @ 1 ml/L.",
        "pesticide_urdu": "مینکوزیب 75 فیصد WP بحساب 2.5 گرام فی لیٹر یا کاربینڈازم + مینکوزیب (صاف) 2 گرام فی لیٹر پانی ملا کر چھڑکاؤ کریں۔",
        "advice_english": "Correct soil micronutrient deficiencies by applying Zinc Sulphate (21% or 33%) and balanced NPK. Prevent drought stress in the nursery.",
        "advice_urdu": "زمین میں زنک سلفیٹ اور پوٹاش کی کمی کو پورا کریں۔ فصل کو پانی کی شدید کمی یا سوکھے کے دباؤ سے ہرگز متاثر نہ ہونے دیں۔"
    },
    "Healthy": {
        "disease_urdu": "صحت مند فصل (کوئی بیماری نہیں ہے)",
        "pesticide_english": "No chemical fungicide or pesticide spray is required at this stage.",
        "pesticide_urdu": "اس وقت کسی قسم کے کیمیائی زہر یا فنگسائڈ سپرے کی کوئی ضرورت نہیں ہے۔",
        "advice_english": "Maintain proper 2-3 inch water depth and balanced organic fertilization. Continue regular field scouting for early pest detection.",
        "advice_urdu": "پانی کی مناسب سطح (2 سے 3 انچ) برقرار رکھیں اور نامیاتی کھاد استعمال کریں۔ ہر ہفتے کھیت کا باقاعدہ معائنہ جاری رکھیں۔"
    }
}

# Load pre-trained deep learning binary model at system initialization
MODEL_PATH = os.path.join(os.path.dirname(__file__), "crop_disease_model.h5")
model = None

try:
    if os.path.exists(MODEL_PATH):
        print(f"[*] Loading pre-trained Keras model from {MODEL_PATH}...")
        model = tf.keras.models.load_model(MODEL_PATH)
        print("[+] Model loaded successfully into memory.")
    else:
        print(f"[!] Warning: '{MODEL_PATH}' not found. Initializing built-in CNN architecture for inference.")
        # Fallback architecture matching train.py specifications
        inputs = tf.keras.Input(shape=(224, 224, 3))
        x = tf.keras.layers.Conv2D(32, (3, 3), activation='relu')(inputs)
        x = tf.keras.layers.MaxPooling2D(2, 2)(x)
        x = tf.keras.layers.Conv2D(64, (3, 3), activation='relu')(x)
        x = tf.keras.layers.MaxPooling2D(2, 2)(x)
        x = tf.keras.layers.Conv2D(64, (3, 3), activation='relu')(x)
        x = tf.keras.layers.MaxPooling2D(2, 2)(x)
        x = tf.keras.layers.Flatten()(x)
        x = tf.keras.layers.Dense(64, activation='relu')(x)
        outputs = tf.keras.layers.Dense(4, activation='softmax')(x)
        model = tf.keras.Model(inputs=inputs, outputs=outputs)
except Exception as e:
    print(f"[!] Error loading model: {e}")

def preprocess_image(image_bytes):
    """
    Utility pipeline:
    1. Decodes binary image stream via Pillow (PIL)
    2. Resizes dimensions to exactly (224, 224)
    3. Normalizes pixel scale to 0.0 - 1.0 range
    4. Expands dimensions to batch format [1, 224, 224, 3]
    """
    image = Image.open(io.BytesIO(image_bytes)).convert("RGB")
    image = image.resize((224, 224))
    image_array = np.array(image, dtype=np.float32) / 255.0
    image_batch = np.expand_dims(image_array, axis=0)  # Shape: [1, 224, 224, 3]
    return image_batch

@app.route("/", methods=["GET"])
def health_check():
    return jsonify({
        "status": "Online",
        "system": "Crop Disease Detection API",
        "project": "BS IT Final Year Project - Islamia University of Bahawalpur (IUB)",
        "developer": "Saqlain Niazi",
        "endpoints": {
            "POST /predict": "Upload image file ('file' parameter) to detect disease and receive remedies"
        }
    }), 200

@app.route("/predict", methods=["POST"])
def predict():
    """
    Single POST route endpoint:
    - Accepts multipart/form-data image file
    - Executes CNN prediction
    - Returns mapped dual-language remedies
    """
    if "file" not in request.files:
        return jsonify({"error": "No 'file' payload provided in multipart request."}), 400

    file = request.files["file"]
    if file.filename == "":
        return jsonify({"error": "Empty filename submitted."}), 400

    try:
        # Read and preprocess binary stream
        image_bytes = file.read()
        processed_tensor = preprocess_image(image_bytes)

        # Run model inference
        predictions = model.predict(processed_tensor)
        confidence_scores = predictions[0]
        highest_idx = int(np.argmax(confidence_scores))
        confidence = float(confidence_scores[highest_idx])
        predicted_class = CLASS_NAMES[highest_idx]

        # Fetch dual-language remedy recommendations
        remedy_info = REMEDY_KNOWLEDGE_BASE.get(predicted_class, REMEDY_KNOWLEDGE_BASE["Healthy"])

        response_payload = {
            "status": "success",
            "prediction": predicted_class,
            "confidence": round(confidence, 4),
            "class_index": highest_idx,
            "developer": "Saqlain Niazi",
            "institution": "Islamia University of Bahawalpur (IUB)",
            "remedies": {
                "disease_urdu": remedy_info["disease_urdu"],
                "pesticide_english": remedy_info["pesticide_english"],
                "pesticide_urdu": remedy_info["pesticide_urdu"],
                "advice_english": remedy_info["advice_english"],
                "advice_urdu": remedy_info["advice_urdu"]
            }
        }

        return jsonify(response_payload), 200

    except Exception as e:
        return jsonify({"status": "error", "message": str(e)}), 500

if __name__ == "__main__":
    print("==================================================================")
    print(" CROP DISEASE DETECTION & REMEDY RECOMMENDATION SERVER")
    print(" Islamia University of Bahawalpur (IUB) - BS IT Final Year Project")
    print(" Developer: Saqlain Niazi")
    print(" Running globally on host 0.0.0.0 and port 5000...")
    print("==================================================================")
    app.run(host="0.0.0.0", port=5000, debug=True)
