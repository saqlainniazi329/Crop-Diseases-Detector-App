package com.example.model

/**
 * Data model for crop disease diagnostic results and dual-language remedies
 * (English and localized Urdu) for agricultural extension and farmer guidance.
 */
data class CropDiseaseInfo(
    val diseaseName: String,
    val diseaseNameUrdu: String,
    val cropType: String = "Rice (دھان / چاول)",
    val confidence: Float,
    val severity: String,
    val severityUrdu: String,
    val symptomsEnglish: List<String>,
    val symptomsUrdu: List<String>,
    val chemicalRemediesEnglish: List<String>,
    val chemicalRemediesUrdu: List<String>,
    val organicRemediesEnglish: List<String>,
    val organicRemediesUrdu: List<String>,
    val preventiveMeasuresEnglish: String,
    val preventiveMeasuresUrdu: String
)

object CropDiseaseKnowledgeBase {
    val CLASS_NAMES = listOf("Bacterial Blight", "Blast", "Brown Spot", "Healthy")

    val KNOWLEDGE = mapOf(
        "Bacterial Blight" to CropDiseaseInfo(
            diseaseName = "Bacterial Blight",
            diseaseNameUrdu = "بیکٹیریل بلائٹ (پتوں کا جھلسائو)",
            confidence = 0.96f,
            severity = "High (خطرناک)",
            severityUrdu = "شدید / فوری توجہ طلب",
            symptomsEnglish = listOf(
                "Water-soaked lesions on leaf margins turning yellow to grayish-white.",
                "Leaves wilt and roll up rapidly under humid conditions (kresek stage).",
                "Milky bacterial ooze drops visible on young lesions early morning."
            ),
            symptomsUrdu = listOf(
                "پتوں کے کناروں پر پانی کے دھبے جو زرد اور بعد میں سرمئی سفید ہو جاتے ہیں۔",
                "زیادہ نمی کے دوران پتے سوکھ کر مڑ جاتے ہیں (کریکٹ مرحلہ)۔",
                "صبح سویرے تازہ دھبوں پر دودھیا بیکٹیریا کے قطرے دیکھے جا سکتے ہیں۔"
            ),
            chemicalRemediesEnglish = listOf(
                "Copper Oxychloride 50% WP @ 2.5 g/liter of water.",
                "Streptocycline / Plantomycin (9:1 antibiotic blend) @ 100 mg/liter.",
                "Bismerthiazol (Kocide) 20% WP spray during early leaf emergence."
            ),
            chemicalRemediesUrdu = listOf(
                "کاپر آکسی کلورائڈ 50 فیصد WP بحساب 2.5 گرام فی لیٹر پانی سپرے کریں۔",
                "اسٹریپٹومائسن اور پلانٹومائسن ملاوٹ 100 ملی گرام فی لیٹر پانی ملا کر چھڑکاؤ کریں۔",
                "بیماری کے ابتدائی حملے کی صورت میں کوسائڈ یا کاپر ہائیڈرو آکسائیڈ کا سپرے دہرائیں۔"
            ),
            organicRemediesEnglish = listOf(
                "Drain standing water from the field for 3-4 days to arrest bacterial spread.",
                "Reduce excessive nitrogen/urea fertilization; supplement with potassium (MOP).",
                "Spray fresh neem seed kernel extract (NSKE 5%) or garlic bulb extract."
            ),
            organicRemediesUrdu = listOf(
                "کھیت سے اضافی کھڑا پانی 3 سے 4 دن کے لیے نکال کر ہوا لگوائیں۔",
                "یوریا اور نائٹروجن والی کھادوں کا غیر ضروری استعمال فوری روکیں اور پوٹاش دیں۔",
                "نیم کے بیجوں کا عرق (5 فیصد) یا لہسن کا پانی قدرتی اینٹی بیکٹیریل کے طور پر چھڑکیں۔"
            ),
            preventiveMeasuresEnglish = "Treat certified seeds with Thiram or Carbendazim before sowing. Avoid mechanical injury during transplantation.",
            preventiveMeasuresUrdu = "بوائی سے پہلے مصدقہ بیج کو زہر آلود کریں۔ پنیری منتقل کرتے وقت پتوں کو زخمی ہونے سے بچائیں۔"
        ),

        "Blast" to CropDiseaseInfo(
            diseaseName = "Blast",
            diseaseNameUrdu = "رائس بلاسٹ (جھونکا روگ)",
            confidence = 0.94f,
            severity = "Critical (انتہائی شدید)",
            severityUrdu = "انتہائی شدید / دانوں کا نقصان",
            symptomsEnglish = listOf(
                "Spindle-shaped elliptical lesions with gray/whitish centers and dark brown margins.",
                "Neck rot causing complete panicle breakage and empty grain heads (neck blast).",
                "Rapid lesion coalescence leading to total leaf desiccation."
            ),
            symptomsUrdu = listOf(
                "پتوں پر بیضوی تکیہ نما دھبے جن کا درمیان سرمئی اور کنارے گہرے بھورے ہوتے ہیں۔",
                "سٹے کی گردن کالی ہو کر ٹوٹ جاتی ہے اور دانے خالی رہ جاتے ہیں (گردن توڑ بلاسٹ)۔",
                "دھبے آپس میں مل کر پورے پتے کو تیزی سے جلا دیتے ہیں۔"
            ),
            chemicalRemediesEnglish = listOf(
                "Tricyclazole 75% WP @ 0.6 g/liter (most potent preventative fungicide).",
                "Azoxystrobin 18.2% + Difenoconazole 11.4% SC (Amistar Top) @ 1 ml/liter.",
                "Isoprothiolane (Fuji-One) 40% EC @ 1.5 ml/liter water."
            ),
            chemicalRemediesUrdu = listOf(
                "ٹرائی سائیکلازول 75 فیصد WP بحساب 0.6 گرام فی لیٹر پانی فوری سپرے کریں۔",
                "ایزوکسی اسٹروبن + ڈائیفینوکونازول (ایمسٹار ٹاپ) 1 ملی لیٹر فی لیٹر پانی چھڑکیں۔",
                "فوجی ون (Isoprothiolane) 40 فیصد EC بحساب 1.5 ملی لیٹر فی لیٹر سپرے کریں۔"
            ),
            organicRemediesEnglish = listOf(
                "Avoid late transplantation and maintain optimal plant-to-plant spacing for airflow.",
                "Spray Pseudomonas fluorescens bacterial suspension @ 5 g/liter.",
                "Apply silicon fertilizers to thicken epidermal leaf walls against fungal penetration."
            ),
            organicRemediesUrdu = listOf(
                "پنیری کی تاخیر سے منتقلی سے گریز کریں اور پودوں کے درمیان مناسب فاصلہ رکھیں۔",
                "سوڈوموناس فلوروسینس بائیو فنگسائڈ 5 گرام فی لیٹر ملا کر چھڑکیں۔",
                "پودوں کی جلد مضبوط بنانے کے لیے سلیکان والی کھاد استعمال کریں۔"
            ),
            preventiveMeasuresEnglish = "Use resistant seed varieties (e.g. Super Basmati certified seed). Avoid application of high urea doses during cloudy weather.",
            preventiveMeasuresUrdu = "بیماری کے خلاف قوت مدافعت رکھنے والی اقسام کاشت کریں۔ ابر آلود موسم میں یوریا کا زیادہ استعمال نہ کریں۔"
        ),

        "Brown Spot" to CropDiseaseInfo(
            diseaseName = "Brown Spot",
            diseaseNameUrdu = "بھورا دھبہ (براؤن اسپاٹ)",
            confidence = 0.91f,
            severity = "Moderate to High",
            severityUrdu = "درمیانہ تا شدید (غذائی قلت)",
            symptomsEnglish = listOf(
                "Isolated circular to oval dark brown spots scattered on leaves and glumes.",
                "Spots surrounded by a characteristic bright yellow chlorotic halo.",
                "Infected grains show black discoloration, leading to reduced milling quality."
            ),
            symptomsUrdu = listOf(
                "پتوں پر گول یا بیضوی گہرے بھورے دھبے بکھرے ہوئے نمودار ہوتے ہیں۔",
                "دھبوں کے گرد نمایاں زرد رنگ کا ہالہ (Yellow Halo) بن جاتا ہے۔",
                "متاثرہ دانوں پر سیاہ نشانات پڑتے ہیں جس سے چاول کی کوالٹی خراب ہوتی ہے۔"
            ),
            chemicalRemediesEnglish = listOf(
                "Mancozeb 75% WP @ 2.5 g/liter of clean water.",
                "Carbendazim 12% + Mancozeb 63% WP (Saaf) @ 2 g/liter.",
                "Propiconazole (Tilt) 25% EC @ 1 ml/liter during panicle emergence."
            ),
            chemicalRemediesUrdu = listOf(
                "مینکوزیب 75 فیصد WP بحساب 2.5 گرام فی لیٹر پانی سپرے کریں۔",
                "کاربینڈازم + مینکوزیب (صاف) 2 گرام فی لیٹر پانی ملا کر چھڑکاؤ کریں۔",
                "پروپیکونازول (ٹلٹ) 25 فیصد EC بحساب 1 ملی لیٹر فی لیٹر پانی سپرے کریں۔"
            ),
            organicRemediesEnglish = listOf(
                "Correct soil nutrient deficiencies by applying balanced Zinc Sulphate and Potassium.",
                "Incorporate composted farmyard manure (FYM) to improve soil water retention.",
                "Seed soaking in Cow urine extract (10% solution) before nursery sowing."
            ),
            organicRemediesUrdu = listOf(
                "زمین میں زنک سلفیٹ اور پوٹاش کی کمی کو متوازن کھادوں سے فوری پورا کریں۔",
                "زمین کی زرخیزی اور نمی برقرار رکھنے کے لیے نامیاتی گوبر کی کھاد ڈالیں۔",
                "نرسری کی بوائی سے قبل بیجوں کا قدرتی اینٹی فنگل علاج کریں۔"
            ),
            preventiveMeasuresEnglish = "Ensure continuous shallow irrigation. Do not allow the field to dry out completely during vegetative growth.",
            preventiveMeasuresUrdu = "فصل کو پانی کی شدید کمی (سوکھے) سے بچائیں اور نشوونما کے دوران مناسب وتر برقرار رکھیں۔"
        ),

        "Healthy" to CropDiseaseInfo(
            diseaseName = "Healthy",
            diseaseNameUrdu = "صحت مند فصل (کوئی بیماری نہیں)",
            confidence = 0.98f,
            severity = "None (پوری طرح صحت مند)",
            severityUrdu = "لاجواب صحت اور نشوونما",
            symptomsEnglish = listOf(
                "Vibrant emerald green foliage with strong, erect leaf blades.",
                "No necrotic lesions, wilting, or yellowing spots detected.",
                "Healthy root system and optimal tillering performance."
            ),
            symptomsUrdu = listOf(
                "سبز شاداب، چمکدار اور سیدھے مضبوط پتے۔",
                "پتوں پر کسی قسم کا دھبہ، جھلساؤ یا پیلا پن موجود نہیں ہے۔",
                "جڑوں کا نظام مضبوط اور پودوں میں شگوفے بھرپور بن رہے ہیں۔"
            ),
            chemicalRemediesEnglish = listOf(
                "No fungicide or pesticide application required.",
                "Maintain scheduled prophylactic micronutrient sprays (Zinc + Boron)."
            ),
            chemicalRemediesUrdu = listOf(
                "کسی کیمیائی زہر یا فنگسائڈ سپرے کی کوئی ضرورت نہیں ہے۔",
                "صحت برقراری کے لیے زنک اور بوران کا متوازن سپرے شیڈول کے مطابق جاری رکھیں۔"
            ),
            organicRemediesEnglish = listOf(
                "Continue standard weed management and water level regulation (2-3 inches).",
                "Apply balanced organic bio-fertilizers to sustain microbial soil flora."
            ),
            organicRemediesUrdu = listOf(
                "جڑی بوٹیوں کی باقاعدہ تلفی اور پانی کی 2 تا 3 انچ سطح برقرار رکھیں۔",
                "زمین کی ساخت بہتر رکھنے کے لیے بائیو فرٹیلائزر کا استعمال جاری رکھیں۔"
            ),
            preventiveMeasuresEnglish = "Regular weekly field scouting to catch any early pathogen spores. Follow recommended crop rotation.",
            preventiveMeasuresUrdu = "ہفتہ وار بنیادوں پر فصل کا معائنہ کرتے رہیں تاکہ بیماری کے کسی بھی ممکنہ حملے کو بروقت روکا جا سکے۔"
        )
    )
}
