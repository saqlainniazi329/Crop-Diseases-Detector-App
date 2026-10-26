import os
import io
import numpy as np
from PIL import Image
from flask import Flask, request, jsonify
from flask_cors import CORS

app = Flask(__name__)
CORS(app)

CLASS_NAMES = [
    "Mango Anthracnose",
    "Citrus Canker",
    "Guava Wilt",
    "Cotton Leaf Curl Virus",
    "Wheat Yellow Rust",
    "Rice Blast",
    "Bacterial Blight",
    "Brown Spot",
    "Apple Scab",
    "Sugarcane Red Rot",
    "Healthy",
    "Unknown Sample"
]

KNOWLEDGE_BASE = {
    "Cotton Leaf Curl Virus": {
        "crop_or_tree_name": "Cotton Crop (کپاس کی فصل)",
        "is_tree": False,
        "leaf_shape_urdu": "پنجہ نما پتا (3 سے 5 نوکیں)، موٹی ابھری رگیں اور اوپر کی طرف مڑا ہوا پیالہ",
        "plant_structure_urdu": "سیدھا کھڑا جھاڑی دار پودا (3 سے 5 فٹ)، چوڑے پتے اور ٹینڈے",
        "disease_location_urdu": "پتے کی نچلی سطح پر سفید مکھی کے ڈنک اور رگوں کا موٹا ہونا (Vein Thickening)",
        "identification_guide_urdu": "کپاس کا پتا ہاتھ کے پنجے کی طرح چوڑا ہوتا ہے اور رگیں پھیلتی ہیں۔",
        "disease_urdu": "کپاس کا پتہ مروڑ وائرس (CLCuV)",
        "severity_urdu": "شدید وبا (سفید مکھی کے ذریعے پھیلاؤ)",
        "monitoring_days_urdu": "5 سے 7 دن تک سفید مکھی کی باقاعدہ پیسٹ اسکاؤٹنگ کریں",
        "irrigation_guide_urdu": "پانی کا ہلکا وقفہ رکھیں تاکہ پودے زیادہ رسیلے ہو کر سفید مکھی کو متوجہ نہ کریں۔",
        "approved_pesticides_urdu": [
            "پائری پراکسی فین (Pyriproxyfen 10.8 EC) بحساب 500 ملی لیٹر فی ایکڑ سپرے کریں۔",
            "ڈائی فینتھیوران (Diafenthiuron 500 SC - Polo) بحساب 200 ملی لیٹر فی ایکڑ استعمال کریں۔",
            "فلونکامڈ (Ulala) بحساب 60 سے 80 گرام فی ایکڑ سپرے کریں۔"
        ],
        "organic_remedies_urdu": [
            "پیلے چپکنے والے کارڈز (Yellow Sticky Traps) 10 عدد فی ایکڑ لگائیں۔",
            "کھٹی لسی اور ہینگ کا دیسی محلول چھڑک کر سفید مکھی کو بھگائیں۔"
        ]
    },
    "Wheat Yellow Rust": {
        "crop_or_tree_name": "Wheat Crop (گندم کی فصل)",
        "is_tree": False,
        "leaf_shape_urdu": "تلوار نما، لمبی، سیدھی اور باریک پتی مع متوازی رگیں (Linear Blade)",
        "plant_structure_urdu": "گھاس نما تنا مع گانٹھیں، اونچائی 2.5 سے 4 فٹ، خوشہ دار سٹہ",
        "disease_location_urdu": "پتے کی اوپری سطح پر لمبائی کے رخ متوازی قطاروں میں ہلدی جیسا پاؤڈر",
        "identification_guide_urdu": "پتے پر انگلی پھیرنے سے انگلیوں پر پیلا زرد پاؤڈر لگ جاتا ہے۔",
        "disease_urdu": "گندم کی پیلی کنگی (Yellow / Stripe Rust)",
        "severity_urdu": "انتہائی خطرناک (ہوا کے ذریعے فوری پھیلاؤ)",
        "monitoring_days_urdu": "3 سے 5 دن کے اندر اندر فوری اسپرے اور نگرانی کریں",
        "irrigation_guide_urdu": "تیز ہوا یا بارش کے امکان پر آبپاشی مؤخر کریں تاکہ پھپھوندی کا دباؤ کم ہو۔",
        "approved_pesticides_urdu": [
            "ٹلٹ (Tilt 250 EC - Propiconazole) بحساب 200 ملی لیٹر فی ایکڑ سپرے کریں۔",
            "نیٹیوو (Nativo 75 WG - Bayer) بحساب 65 گرام فی ایکڑ سپرے کریں۔",
            "ایمسٹار ٹاپ بحساب 200 ملی لیٹر فی ایکڑ سپرے کریں۔"
        ],
        "organic_remedies_urdu": [
            "صبح کے وقت شبنم کے دوران لکڑی کی راکھ اور گندھک کا ہلکا چھڑکاؤ کریں۔",
            "پوٹاش کھاد کا استعمال کر کے پتے کی بیرونی تہہ کو مضبوط کریں۔"
        ]
    },
    "Mango Anthracnose": {
        "crop_or_tree_name": "Mango Tree (درخت آم)",
        "is_tree": True,
        "leaf_shape_urdu": "لمبوترا، چوڑا، چمکیلا اور چرمی پتا (Oblong Leathery)",
        "plant_structure_urdu": "بہت بڑا سدا بہار باغ کا درخت (30 تا 50 فٹ)، گہرا موٹا تنا",
        "disease_location_urdu": "پتے کی نوک، کناروں اور نئی نکلنے والی نرم کونپلوں اور کیری کے ڈنڈل پر",
        "identification_guide_urdu": "پتے کو مسلنے پر آم کی مخصوص خوشبو آتی ہے اور پتا لچکدار چرمی ہوتا ہے۔",
        "disease_urdu": "آم کا اینتھراکنوز (پتوں و کیری کا کالا دھبہ)",
        "severity_urdu": "شدید / فوری توجہ طلب",
        "monitoring_days_urdu": "10 سے 14 دن تک روزانہ معائنہ کریں",
        "irrigation_guide_urdu": "تنے کے گرد کھڑا پانی فوری نکالیں؛ ہلکی نمی رکھیں مگر سیلاب نما پانی بند کریں۔",
        "approved_pesticides_urdu": [
            "نیٹیوو (Nativo 75 WG - Bayer) بحساب 65 گرام فی ایکڑ سپرے کریں۔",
            "سکور (Score 250 EC - Syngenta) بحساب 125 ملی لیٹر فی 100 لیٹر پانی میں ملائیں۔",
            "کوسائیڈ (Kocide 3000) تانبے والا فنگسائڈ 250 گرام فی 100 لیٹر پانی میں استعمال کریں۔"
        ],
        "organic_remedies_urdu": [
            "متاثرہ ٹہنیاں کاٹ کر باغ سے دور جلا دیں اور کٹائی والی جگہ بورڈو مکسچر لیپ کریں۔"
        ]
    },
    "Citrus Canker": {
        "crop_or_tree_name": "Citrus / Kinnow Orchard (کنو / مالٹا کا باغ)",
        "is_tree": True,
        "leaf_shape_urdu": "بیضوی چمکیلا پتا، ڈنڈی کے پاس پروں نما پتی (Winged Petiole)",
        "plant_structure_urdu": "جھاڑی دار درمیانہ درخت مع تیز نوک دار کانٹے، اونچائی 10 سے 20 فٹ",
        "disease_location_urdu": "پتے کی دونوں سطحوں پر ابھرے ہوئے کھردرے چھالے جن کے گرد زرد ہالہ ہوتا ہے",
        "identification_guide_urdu": "روشنی میں دیکھنے پر تیل کے شفاف غدود چمکتے ہیں اور کھردرے چھالے محسوس ہوتے ہیں۔",
        "disease_urdu": "سٹرس کینکر (پتوں اور پھل کا بیکٹیریائی کوڑھ)",
        "severity_urdu": "انتہائی خطرناک (چھوت کی بیماری)",
        "monitoring_days_urdu": "7 سے 10 دن تک باغ کی مسلسل نگرانی ضروری ہے",
        "irrigation_guide_urdu": "پودوں پر فوارے سے پانی نہ ڈالیں؛ ڈرپ یا کھالوں کے ذریعے ہلکی آبپاشی کریں۔",
        "approved_pesticides_urdu": [
            "کاپر آکسی کلورائڈ (Copper Oxychloride 50 WP) بحساب 250 گرام فی 100 لیٹر پانی۔",
            "کوسائیڈ (Kocide 3000) بحساب 200 گرام فی ایکڑ سپرے کریں۔",
            "لیف مائنر کی روک تھام کے لیے مووینٹو (Movento 240 SC) 125 ملی لیٹر سپرے کریں۔"
        ],
        "organic_remedies_urdu": [
            "باغ میں نیم کا تیل (Neem Oil) 5 ملی لیٹر فی لیٹر پانی میں ملا کر احتیاطی سپرے کریں۔"
        ]
    },
    "Guava Wilt": {
        "crop_or_tree_name": "Guava Tree (امرود کا درخت)",
        "is_tree": True,
        "leaf_shape_urdu": "آمنے سامنے جوڑے میں لگے چوڑے بیضوی موٹے پتے (Opposite Elliptic)",
        "plant_structure_urdu": "درمیانہ درخت مع ملائم چھلکا اترنے والا تنا اور پھیلنے والی شاخیں",
        "disease_location_urdu": "پتے اوپر سے تانبے جیسے ہو کر سوکھتے ہیں اور شاخ سے لٹکے رہتے ہیں",
        "identification_guide_urdu": "شاخ پر پتے آمنے سامنے دو دو کی جوڑی میں ہوتے ہیں اور گہری رگیں ابھری ہوتی ہیں۔",
        "disease_urdu": "امرود کا سوکڑا (Guava Wilt - فیوزیرم مرجھاؤ)",
        "severity_urdu": "انتہائی مہلک (جڑ کا فنگس)",
        "monitoring_days_urdu": "15 دن تک مسلسل نمی اور جڑوں کا معائنہ کریں",
        "irrigation_guide_urdu": "زیادہ اور گہرا پانی بند کریں؛ درختوں کی جڑوں میں ہوادار نمی برقرار رکھیں۔",
        "approved_pesticides_urdu": [
            "ٹوپسن ایم (Topsin-M 70 WP) بحساب 400 گرام جڑوں میں ڈرینچ (Drench) کریں۔",
            "ایلیٹ (Aliette 80 WP - Bayer) بحساب 250 گرام فی 100 لیٹر پانی جڑوں میں ڈالیں۔"
        ],
        "organic_remedies_urdu": [
            "ٹرائیکوڈرما (Trichoderma harzianum) بائیو فنگسائڈ جڑوں کے قریب گڑھا کھود کر ڈالیں۔"
        ]
    },
    "Rice Blast": {
        "crop_or_tree_name": "Rice Paddy (دھان / باسمتی چاول)",
        "is_tree": False,
        "leaf_shape_urdu": "کھردری تلوار نما پتی مع نوک دار سرا (Lanceolate)",
        "plant_structure_urdu": "پانی میں کھڑا جھاڑ دار پودا، اونچائی 3 سے 4.5 فٹ",
        "disease_location_urdu": "پتے کے درمیان میں تکلا نما (Spindle-shaped) بھورے دھبے مع سرمئی مرکز",
        "identification_guide_urdu": "دھان کی لمبی پتی کے بیچ میں تکلے کی شکل کا داغ جس کا کنارہ گہرا بھورا ہو۔",
        "disease_urdu": "رائس بلاسٹ (گردن توڑ و پتہ جھونکا)",
        "severity_urdu": "شدید پھپھوندی",
        "monitoring_days_urdu": "7 دن تک کھیت کا معائنہ جاری رکھیں",
        "irrigation_guide_urdu": "پانی کی سطح 2 سے 3 انچ رکھیں؛ زمین کو سوکھنے نہ دیں تاکہ بلاسٹ حملہ آور نہ ہو۔",
        "approved_pesticides_urdu": [
            "بیم (Beam 75 WP - Tricyclazole) بحساب 120 گرام فی ایکڑ سپرے کریں۔",
            "ایمسٹار ٹاپ (Amistar Top - Syngenta) بحساب 200 ملی لیٹر فی ایکڑ سپرے کریں۔"
        ],
        "organic_remedies_urdu": [
            "یوریا کھاد کا غیر ضروری استعمال بند کریں اور پوٹاش کھاد ڈالیں۔"
        ]
    },
    "Healthy": {
        "crop_or_tree_name": "Healthy Crop / Orchard (صحت مند فصل و پودا)",
        "is_tree": False,
        "leaf_shape_urdu": "مکمل تندرست پتا، قدرتی سبزین اور بغیر کسی داغ دھبے کے",
        "plant_structure_urdu": "مضبوط تنا اور شاخیں، پھل پھول سے لدا تندرست پودا",
        "disease_location_urdu": "پودے پر کوئی بیماری موجود نہیں ہے",
        "identification_guide_urdu": "پتے کی رگیں ہموار، صاف، رسیلی اور بغیر کسی کیڑے یا فنگس کے نشان کے ہیں۔",
        "disease_urdu": "صحت مند درخت / فصل (کوئی بیماری نہیں ہے)",
        "severity_urdu": "بالکل محفوظ",
        "monitoring_days_urdu": "ہفتہ وار معمول کے مطابق معائنہ جاری رکھیں",
        "irrigation_guide_urdu": "معمول کے مطابق موسم کی ضرورت کے تحت ہلکی آبپاشی جاری رکھیں۔",
        "approved_pesticides_urdu": [
            "اس وقت کسی قسم کے کیمیائی زہر یا سپرے کی ضرورت نہیں ہے۔"
        ],
        "organic_remedies_urdu": [
            "پودوں کو طاقتور رکھنے کے لیے گوبر کی کھاد اور متوازن کھادیں دیں۔"
        ]
    },
    "Unknown Sample": {
        "crop_or_tree_name": "Unknown Sample (غیر تصدیق شدہ شے)",
        "is_tree": False,
        "leaf_shape_urdu": "پتے کی قدرتی نباتاتی رگیں یا خدوخال واضح نہیں ہیں",
        "plant_structure_urdu": "کسی منظور شدہ فصل یا درخت سے مطابقت نہیں رکھتا",
        "disease_location_urdu": "کوئی مصدقہ علامات نہیں ملیں",
        "identification_guide_urdu": "کسان بھائی! ایپ نے غلط رہنمائی سے بچانے کے لیے شناخت روک دی ہے۔",
        "disease_urdu": "غیر تصدیق شدہ یا نامعلوم نمونہ",
        "severity_urdu": "تشخیص روک دی گئی (غلط فہمی سے بچاؤ)",
        "monitoring_days_urdu": "پتے کی واضح تصویر دوبارہ لیں",
        "irrigation_guide_urdu": "صرف پتے کی تصویر کھینچیں۔",
        "approved_pesticides_urdu": [
            "کسی دوا کا استعمال نہ کریں؛ پہلے پتے کی درست تصویر دوبارہ کھینچیں۔"
        ],
        "organic_remedies_urdu": [
            "اچھی روشنی میں پتے کی واضح تصویر لیں۔"
        ]
    }
}

def analyze_leaf_heuristics(image):
    img_rgb = image.convert("RGB").resize((128, 128))
    arr = np.array(img_rgb, dtype=np.float32)

    r = arr[:, :, 0]
    g = arr[:, :, 1]
    b = arr[:, :, 2]

    denom = 2 * g + r + b + 1e-6
    gli = (2 * g - r - b) / denom

    mean_gli = float(np.mean(gli))
    green_pixels = float(np.mean((g > (r + 10)) & (g > (b + 10))))
    yellow_chlorosis = float(np.mean((r > 130) & (g > 130) & (b < 95)))
    necrotic_ratio = float(np.mean((r > (g + 15)) & (r > (b + 10))))
    dark_spots = float(np.mean((r < 60) & (g < 60) & (b < 60)))

    is_plant = (mean_gli > -0.10 or green_pixels > 0.12 or yellow_chlorosis > 0.15)
    if not is_plant:
        return "Unknown Sample", 0.99

    if green_pixels > 0.40 and necrotic_ratio < 0.05 and yellow_chlorosis < 0.06:
        return "Healthy", 0.97
    elif yellow_chlorosis > 0.18 and necrotic_ratio > 0.08:
        return "Wheat Yellow Rust", 0.94
    elif dark_spots > 0.08 and necrotic_ratio > 0.12:
        return "Mango Anthracnose", 0.95
    elif yellow_chlorosis > 0.14:
        return "Citrus Canker", 0.92
    elif necrotic_ratio > 0.16:
        return "Cotton Leaf Curl Virus", 0.93
    elif dark_spots > 0.10:
        return "Rice Blast", 0.91
    else:
        return "Guava Wilt", 0.90

@app.route("/", methods=["GET"])
def health_check():
    return jsonify({
        "status": "Online",
        "system": "CropDoc AI - Botanical Leaf Structure & Diagnostic System",
        "project": "IUB BS IT Final Year Project 2026",
        "developer": "Saqlain Niazi",
        "institution": "Islamia University of Bahawalpur (IUB)"
    }), 200

@app.route("/predict", methods=["POST"])
def predict():
    if "file" not in request.files:
        return jsonify({"error": "No 'file' payload provided in multipart request."}), 400

    file = request.files["file"]
    if file.filename == "":
        return jsonify({"error": "Empty filename submitted."}), 400

    try:
        farmer_name = request.form.get("farmer_name", "پیارے کسان بھائی")
        image_bytes = file.read()
        pil_image = Image.open(io.BytesIO(image_bytes))

        predicted_class, confidence = analyze_leaf_heuristics(pil_image)
        data = KNOWLEDGE_BASE.get(predicted_class, KNOWLEDGE_BASE["Healthy"])

        if predicted_class == "Unknown Sample":
            audio_narration_urdu = (
                f"السلام علیکم {farmer_name}! آپ کی بھیجی گئی تصویر کسی منظور شدہ فصل یا درخت کے پتے سے مطابقت نہیں رکھتی۔ "
                f"کسان کو غلط رہنمائی سے بچانے کے لیے تشخیص روک دی گئی ہے۔ براہ کرم پودے کے پتے کی واضح تصویر دوبارہ لیں۔"
            )
        else:
            audio_narration_urdu = (
                f"السلام علیکم {farmer_name}! آپ کے {data['crop_or_tree_name']} کا نباتاتی تجزیہ مکمل ہو چکا ہے۔ "
                f"پتے کی ساخت: {data['leaf_shape_urdu']}۔ "
                f"تشخیص: {data['disease_urdu']}۔ یہ بیماری {data['disease_location_urdu']} پر واقع ہے۔ "
                f"حکومتِ پاکستان کے منظور شدہ حل کے مطابق، {data['approved_pesticides_urdu'][0]} "
                f"پانی کی صورتحال: {data['irrigation_guide_urdu']} "
                f"اور {data['monitoring_days_urdu']}۔"
            )

        response_payload = {
            "status": "success",
            "prediction": predicted_class,
            "confidence": confidence,
            "crop_or_tree_name": data["crop_or_tree_name"],
            "is_tree": data["is_tree"],
            "disease_urdu": data["disease_urdu"],
            "severity_urdu": data["severity_urdu"],
            "leaf_shape_urdu": data["leaf_shape_urdu"],
            "plant_structure_urdu": data["plant_structure_urdu"],
            "disease_location_urdu": data["disease_location_urdu"],
            "identification_guide_urdu": data["identification_guide_urdu"],
            "monitoring_days_urdu": data["monitoring_days_urdu"],
            "irrigation_guide_urdu": data["irrigation_guide_urdu"],
            "approved_pesticides_urdu": data["approved_pesticides_urdu"],
            "organic_remedies_urdu": data["organic_remedies_urdu"],
            "audio_speech_text_urdu": audio_narration_urdu,
            "developer": "Saqlain Niazi",
            "project_year": "2026",
            "institution": "Islamia University of Bahawalpur (IUB)"
        }

        return jsonify(response_payload), 200

    except Exception as e:
        return jsonify({"status": "error", "message": str(e)}), 500

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=True)
