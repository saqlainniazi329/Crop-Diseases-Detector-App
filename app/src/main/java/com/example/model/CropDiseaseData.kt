package com.example.model

/**
 * Data model for Crop and Orchard Tree diagnosis with Government of Pakistan
 * (Department of Plant Protection - DPP & PARC) approved agrochemical remedies,
 * irrigation requirements, and monitoring timelines in bilingual Urdu & English.
 */
data class CropDiseaseInfo(
    val diseaseName: String,
    val diseaseNameUrdu: String,
    val cropOrTreeName: String,
    val cropOrTreeNameUrdu: String,
    val isTree: Boolean = false,
    val confidence: Float,
    val severity: String,
    val severityUrdu: String,
    val monitoringPeriod: String,
    val monitoringPeriodUrdu: String,
    val irrigationGuideEnglish: String,
    val irrigationGuideUrdu: String,
    val approvedPesticidesEnglish: List<String>,
    val approvedPesticidesUrdu: List<String>,
    val symptomsEnglish: List<String>,
    val symptomsUrdu: List<String>,
    val organicRemediesEnglish: List<String>,
    val organicRemediesUrdu: List<String>,
    val preventiveMeasuresEnglish: String,
    val preventiveMeasuresUrdu: String,
    val audioScriptUrdu: String = ""
)

object CropDiseaseKnowledgeBase {

    val TARGET_SPECIES = listOf(
        "Mango (آم)",
        "Citrus / Kinnow (کنو / مالٹا)",
        "Guava (امرود)",
        "Apple (سیب)",
        "Cotton (کپاس)",
        "Wheat (گندم)",
        "Rice (دھان)",
        "Sugarcane (گنا)"
    )

    val KNOWLEDGE = mapOf(
        // ==========================================
        // 1. MANGO TREE (آم کا درخت) - Anthracnose
        // ==========================================
        "Mango Anthracnose" to CropDiseaseInfo(
            diseaseName = "Mango Anthracnose",
            diseaseNameUrdu = "آم کا اینتھراکنوز (پتوں و کیری کا کالا دھبہ)",
            cropOrTreeName = "Mango Tree (درخت آم)",
            cropOrTreeNameUrdu = "آم کا باغ / درخت",
            isTree = true,
            confidence = 0.96f,
            severity = "High (شدید خطرہ)",
            severityUrdu = "شدید / فوری توجہ طلب",
            monitoringPeriod = "10 to 14 Days continuous field inspection",
            monitoringPeriodUrdu = "10 سے 14 دن تک روزانہ معائنہ کریں",
            irrigationGuideEnglish = "Halt flood irrigation; keep soil moist but prevent stagnant water around tree trunks.",
            irrigationGuideUrdu = "درخت کے تنے کے قریب کھڑا پانی فوری نکالیں۔ صرف وتر سوکھنے پر ہلکا پانی دیں۔",
            approvedPesticidesEnglish = listOf(
                "Nativo 75% WG (Tebuconazole + Trifloxystrobin - Bayer) @ 65 g per 100 Liters of water (DPP Reg).",
                "Score 250 EC (Difenoconazole - Syngenta) @ 50 ml per 100 Liters water.",
                "Cuprocaffaro / Copper Oxychloride 50% WP @ 250 g per 100 Liters water as preventative cover."
            ),
            approvedPesticidesUrdu = listOf(
                "بائیر کا نیٹیوو (Nativo 75% WG) بحساب 65 گرام فی 100 لیٹر پانی سپرے کریں (وزارت زراعت پاکستان منظور شدہ)۔",
                "سنجینٹا کا اسکور (Score 250 EC) بحساب 50 تا 60 ملی لیٹر فی 100 لیٹر پانی چھڑکاؤ کریں۔",
                "کاپر آکسی کلورائڈ 50 فیصد ڈبلیو پی بحساب 250 گرام فی 100 لیٹر پانی برائے حفاظتی کوٹنگ۔"
            ),
            symptomsEnglish = listOf(
                "Irregular dark brown to black necrotic spots on foliage and blossom clusters.",
                "Tear-stain streaks on young mango fruits leading to fruit drop.",
                "Blighted twig dieback in high humidity conditions."
            ),
            symptomsUrdu = listOf(
                "پتوں اور بور پر بے ترتیب گہرے سیاہ اور بھورے دھبے جو آپس میں مل جاتے ہیں۔",
                "چھوٹی کیری پر آنسو نما سیاہ دھاریاں پڑنا جس سے پھل گر جاتا ہے۔",
                "نمی کے موسم میں شاخوں کے سرے سوکھنا (ڈائی بیک)۔"
            ),
            organicRemediesEnglish = listOf(
                "Prune diseased dried twigs 5 cm below infected area and paint with Bordeaux paste.",
                "Spray 5% concentrated Neem Seed Kernel Extract (NSKE) during non-flowering period."
            ),
            organicRemediesUrdu = listOf(
                "متاثرہ سوکھی شاخیں کاٹ کر باغ سے باہر جلائیں اور کٹائی پر بورڈو مکسچر کا لیپ لگائیں۔",
                "نیم کے تیل کا 5 فیصد محلول ملا کر قدرتی اینٹی فنگل سپرے کریں۔"
            ),
            preventiveMeasuresEnglish = "Maintain 30x30 feet canopy spacing for sun exposure. Spray immediately after pre-monsoon showers.",
            preventiveMeasuresUrdu = "باغ میں ہوا اور دھوپ کی نکاسی برقرار رکھیں۔ مون سون کی پہلی بارش کے بعد فوری سپرے دہرائیں۔"
        ),

        // ==========================================
        // 2. CITRUS / KINNOW TREE (کنو اور مالٹا) - Citrus Canker
        // ==========================================
        "Citrus Canker" to CropDiseaseInfo(
            diseaseName = "Citrus Canker",
            diseaseNameUrdu = "سٹرس کینکر (کنو و مالٹے کا بیکٹیریل ناسور)",
            cropOrTreeName = "Citrus / Kinnow (کنو کا درخت)",
            cropOrTreeNameUrdu = "کنو اور مالٹے کا باغ",
            isTree = true,
            confidence = 0.95f,
            severity = "Critical (انتہائی شدید)",
            severityUrdu = "انتہائی شدید / چھال و پھل کی خرابی",
            monitoringPeriod = "7 to 10 Days post-treatment",
            monitoringPeriodUrdu = "7 سے 10 دن تک باغ کا باقاعدہ معائنہ",
            irrigationGuideEnglish = "Avoid overhead irrigation; use drip or ring basin method so water does not splash bacteria onto leaves.",
            irrigationGuideUrdu = "فوارہ آبپاشی سے پرہیز کریں۔ رنگ برنگ (گھیرے والی) آبپاشی کریں تاکہ بیکٹیریا اچھل کر پتوں پر نہ جائے۔",
            approvedPesticidesEnglish = listOf(
                "Kocide 2000 (Copper Hydroxide 54% DF) @ 200 g per 100 Liters of water (DPP Approved).",
                "Kasugamycin 2% + Copper Oxychloride 45% WP (Kasumin) @ 200 g per 100 L water.",
                "Control Citrus Leaf Miner vector with Movento (Spirotetramat) @ 125 ml per acre."
            ),
            approvedPesticidesUrdu = listOf(
                "کوسائیڈ 2000 (Copper Hydroxide 54% DF) بحساب 200 گرام فی 100 لیٹر پانی چھڑکیں (حکومتِ پاکستان منظور شدہ)۔",
                "کاسوگا مائسن ملاوٹ (Kasumin WP) بحساب 200 گرام فی 100 لیٹر پانی سپرے کریں۔",
                "پتے کا کیڑا (Citrus Leaf Miner) کنٹرول کرنے کے لیے مووینٹو 125 ملی لیٹر فی ایکڑ سپرے کریں۔"
            ),
            symptomsEnglish = listOf(
                "Raised corky crater-like pustules on both leaf surfaces surrounded by oily yellow halos.",
                "Scabby brown lesions on Kinnow rind rendering fruit unmarketable.",
                "Premature defoliation and severe fruit drop."
            ),
            symptomsUrdu = listOf(
                "پتوں کی دونوں اطراف ابھرے ہوئے کھردرے چھالے نما دھبے جن کے گرد زرد ہالہ ہوتا ہے۔",
                "کنو کے چھلکے پر بھورے مسے نما داغ جن سے پھل ایکسپورٹ کے قابل نہیں رہتا۔",
                "پتوں اور چھوٹے پھلوں کا قبل از وقت جھڑ جانا۔"
            ),
            organicRemediesEnglish = listOf(
                "Disinfect pruning shears with 10% sodium hypochlorite bleach between trees.",
                "Apply windbreaks (Sheesham or Jantar) around citrus orchards to prevent wind-borne bacterial spread."
            ),
            organicRemediesUrdu = listOf(
                "کانٹ چھانٹ کرنے والے آلات کو کلورین والے پانی سے ہر پودے کے بعد صاف کریں۔",
                "باغ کے گرد جنتر یا شیشم کی ونڈ بریک باڑ لگائیں تاکہ آندھی سے بیکٹیریا نہ پھیلے۔"
            ),
            preventiveMeasuresEnglish = "Spray copper bactericide at flush emergence (March-April and August-September).",
            preventiveMeasuresUrdu = "بہار اور برسات کے نئے شگوفوں پر فوری حفاظتی کاپر سپرے کا معمول بنائیں۔"
        ),

        // ==========================================
        // 3. GUAVA TREE (امرود) - Guava Wilt
        // ==========================================
        "Guava Wilt" to CropDiseaseInfo(
            diseaseName = "Guava Wilt",
            diseaseNameUrdu = "امرود کا سوکا / مرجھائو (فیوزیرم ولٹ)",
            cropOrTreeName = "Guava Tree (امرود کا پودا)",
            cropOrTreeNameUrdu = "امرود کا درخت",
            isTree = true,
            confidence = 0.93f,
            severity = "Severe (سوکھنے کا خطرہ)",
            severityUrdu = "خطرناک / پودا سوکھنے کا اندیشہ",
            monitoringPeriod = "15 to 20 Days",
            monitoringPeriodUrdu = "15 سے 20 دن تک جڑوں کی نگرانی",
            irrigationGuideEnglish = "Reduce watering; loosen soil around drip line to ensure good root oxygenation. Never flood infected rows.",
            irrigationGuideUrdu = "پانی کی فراہمی کم کریں۔ پودے کے گھیرے کی گوڈی کر کے زمین کو خشک اور ہوادار بنائیں۔",
            approvedPesticidesEnglish = listOf(
                "Topsin-M 70% WP (Thiophanate-Methyl - Arysta) @ 2.5 g per Liter root soil drenching (PARC standard).",
                "Aliette 80% WG (Fosetyl-Aluminium - Bayer) @ 250 g per 100 Liters for vascular health.",
                "Carbendazim 50% WP (Derosal) @ 2 g/L soil trench application."
            ),
            approvedPesticidesUrdu = listOf(
                "ٹاپسن ایم (Topsin-M 70% WP) بحساب 2.5 گرام فی لیٹر پانی جڑوں کے گرد ڈرینچنگ کریں (PARC منظور شدہ)۔",
                "ایلیٹ (Aliette 80% WG) بحساب 250 گرام فی 100 لیٹر پانی تنے اور جڑوں میں ڈالیں۔",
                "کاربینڈازم 50 فیصد پاؤڈر بحساب 2 گرام فی لیٹر پانی ملا کر جڑوں میں تر کریں۔"
            ),
            symptomsEnglish = listOf(
                "Sudden yellowing and drooping of foliage starting from upper branches.",
                "Internal xylem vascular browning seen when bark is shaved at trunk base.",
                "Total tree desiccation within 15-20 days."
            ),
            symptomsUrdu = listOf(
                "اوپری شاخوں کے پتے اچانک زرد ہو کر نیچے لٹک جاتے ہیں۔",
                "تنے کی چھال اتارنے پر اندرونی رگیں بھوری یا سیاہ نظر آتی ہیں۔",
                "چند ہی ہفتوں میں پورا درخت خشک ہو جاتا ہے۔"
            ),
            organicRemediesEnglish = listOf(
                "Soil incorporation of Trichoderma harzianum bio-fungicide @ 50 g per tree with compost.",
                "Apply 5 kg well-rotted farmyard manure mixed with 250 g neem cake per tree."
            ),
            organicRemediesUrdu = listOf(
                "ٹرائیکوڈھرما بائیو فنگسائڈ 50 گرام فی درخت نامیاتی کھاد میں ملا کر جڑوں میں ڈالیں۔",
                "گوبر کی گلی سڑی کھاد اور نیم کی کھلی جڑوں کے گرد گوڈی کر کے ملائیں۔"
            ),
            preventiveMeasuresEnglish = "Ensure orchard drainage during heavy monsoon rains. Avoid planting in nematode-infested soils.",
            preventiveMeasuresUrdu = "مون سون میں نکاسی کا مناسب انتظام رکھیں اور بیمار پودوں کو فوراً جڑ سے اکھاڑ کر تلف کریں۔"
        ),

        // ==========================================
        // 4. COTTON CROP (کپاس) - Leaf Curl Virus
        // ==========================================
        "Cotton Leaf Curl Virus" to CropDiseaseInfo(
            diseaseName = "Cotton Leaf Curl Virus",
            diseaseNameUrdu = "کپاس کا پتا مروڑ وائرس (سی ایل سی یو وی)",
            cropOrTreeName = "Cotton (کپاس / چاندی)",
            cropOrTreeNameUrdu = "کپاس کی فصل",
            isTree = false,
            confidence = 0.97f,
            severity = "Critical (پیداوار میں کمی)",
            severityUrdu = "انتہائی خطرناک / سفید مکھی کا حملہ",
            monitoringPeriod = "5 to 7 Days interval scouting",
            monitoringPeriodUrdu = "5 سے 7 دن بعد باقاعدہ پیسٹ اسکاؤٹنگ",
            irrigationGuideEnglish = "Regulate irrigation every 10-12 days; prevent plant water-stress which attracts whitefly swarms.",
            irrigationGuideUrdu = "10 سے 12 دن کے وقفے پر ہلکا پانی دیں۔ فصل کو شدید پیاس نہ لگنے دیں ورنہ سفید مکھی کا حملہ بڑھے گا۔",
            approvedPesticidesEnglish = listOf(
                "Pyriproxyfen 10.8% EC (Lano - FMC) @ 400 ml per acre for Whitefly egg/nymph control.",
                "Diafenthiuron 50% SC (Polo - Syngenta) @ 250 ml per acre for adult whiteflies.",
                "Spirotetramat 240 SC (Movento) @ 125 ml/acre for systemic nymph knockdown."
            ),
            approvedPesticidesUrdu = listOf(
                "ایف ایم سی کا پائری پروکسیفن (Pyriproxyfen 10.8% EC) بحساب 400 ملی لیٹر فی ایکڑ (انڈے اور بچے تلف کرنے کے لیے)۔",
                "سنجینٹا کا پولو (Diafenthiuron 50% SC) بحساب 250 ملی لیٹر فی ایکڑ برائے بالغ سفید مکھی۔",
                "مووینٹو 240 ایس سی بحساب 125 ملی لیٹر فی ایکڑ کا بروقت سپرے۔"
            ),
            symptomsEnglish = listOf(
                "Upward or downward leaf curling with prominent vein thickening.",
                "Small enations (cup-shaped leaf-like growths) on the underside of leaves.",
                "Stunted plant growth and drastic reduction in boll formation."
            ),
            symptomsUrdu = listOf(
                "پتوں کا اوپر یا نیچے کی طرف الٹ کٹورے کی طرح مڑ جانا اور رگوں کا موٹا ہونا۔",
                "پتے کی نچلی سطح پر اضافی چھوٹے پتوں (اینیشنز) کا بن جانا۔",
                "پودے کا قد رک جانا اور ٹینڈے نہ بننا۔"
            ),
            organicRemediesEnglish = listOf(
                "Install yellow sticky traps (20 traps per acre) to trap whitefly vectors.",
                "Foliar spray of 2% Potassium Sopahate + 1% Zinc to boost foliar resistance."
            ),
            organicRemediesUrdu = listOf(
                "کھیت میں پیلے چپکنے والے کارڈز (20 ٹریپس فی ایکڑ) لگائیں تاکہ سفید مکھی پھنس جائے۔",
                "پوٹاش اور زنک کا فولیئر سپرے کر کے پودے کی قوت مدافعت مضبوط بنائیں۔"
            ),
            preventiveMeasuresEnglish = "Eradicate alternate weed hosts (Peeli Booti - Abutilon indicum) on watercourses.",
            preventiveMeasuresUrdu = "کھالوں اور وٹوں پر اگی ہوئی جڑی بوٹیوں (خاص طور پر پیلی بوٹی) کو فوراً تلف کریں۔"
        ),

        // ==========================================
        // 5. WHEAT CROP (گندم) - Yellow / Stripe Rust
        // ==========================================
        "Wheat Yellow Rust" to CropDiseaseInfo(
            diseaseName = "Wheat Yellow Rust",
            diseaseNameUrdu = "گندم کی پیلی کنگی / زرد زنگ (اسٹرائپ رسٹ)",
            cropOrTreeName = "Wheat (گندم / کنک)",
            cropOrTreeNameUrdu = "گندم کی فصل",
            isTree = false,
            confidence = 0.95f,
            severity = "Emergency (فوری سپرے درکار)",
            severityUrdu = "ہنگامی نوعیت / دانوں کا سکڑائو",
            monitoringPeriod = "5 to 7 Days",
            monitoringPeriodUrdu = "5 سے 7 دن تک کھیت کا بغور معائنہ",
            irrigationGuideEnglish = "Do not irrigate during windy weather to avoid lodging. Maintain moist soil condition during grain filling.",
            irrigationGuideUrdu = "تیز ہوا میں پانی ہرگز نہ لگائیں تاکہ گندم گرے نہ۔ دانہ بنتے وقت وتر سوکھنے نہ دیں۔",
            approvedPesticidesEnglish = listOf(
                "Tilt 250 EC (Propiconazole - Syngenta) @ 200 ml per acre in 100 L water (PARC Wheat Board Recommended).",
                "Amistar Top (Azoxystrobin + Difenoconazole) @ 200 ml per acre.",
                "Tebuconazole 250 EC (Folicur - Bayer) @ 200 ml per acre."
            ),
            approvedPesticidesUrdu = listOf(
                "سنجینٹا کا ٹلٹ (Tilt 250 EC) بحساب 200 ملی لیٹر فی ایکڑ 100 لیٹر پانی میں ملا کر سپرے کریں (PARC منظور شدہ)۔",
                "ایمسٹار ٹاپ بحساب 200 ملی لیٹر فی ایکڑ چھڑکیں۔",
                "بائیر کا فولیکور (Tebuconazole) بحساب 200 ملی لیٹر فی ایکڑ سپرے کریں۔"
            ),
            symptomsEnglish = listOf(
                "Parallel yellow-orange powdery pustules arranged in linear stripes on leaf blades.",
                "Yellow dust rubbing off onto fingers when touched.",
                "Premature leaf drying leading to shriveled grains."
            ),
            symptomsUrdu = listOf(
                "پتوں پر پیلے اور نارنجی رنگ کے پاؤڈر نما دھبے لمبی لکیروں کی شکل میں۔",
                "ہاتھ لگانے پر زرد سفوف یا پاؤڈر انگلیوں پر لگ جانا۔",
                "پتے سوکھ کر جل جاتے ہیں اور سٹے کے دانے باریک رہ جاتے ہیں۔"
            ),
            organicRemediesEnglish = listOf(
                "Spray sour butter-milk (Lassi) fermented with copper plate @ 10% dilution as traditional rust inhibitor.",
                "Apply balanced bio-sulfur spray early in the morning."
            ),
            organicRemediesUrdu = listOf(
                "کھٹی لسی میں تانبے کا برتن رکھ کر تیار کردہ 10 فیصد محلول کا روایتی دیسی چھڑکاؤ کریں۔",
                "گندھک (سلفر) کا ہلکا دھوڑا یا سپرے کریں۔"
            ),
            preventiveMeasuresEnglish = "Cultivate certified rust-resistant wheat varieties (Akbar-19, Dilkash-20, Subhani-21).",
            preventiveMeasuresUrdu = "کنگی کے خلاف قوت مدافعت رکھنے والی تصدیق شدہ اقسام (اکبر 19، دلکش 20، سبحانی 21) کاشت کریں۔"
        ),

        // ==========================================
        // 6. RICE CROP (دھان) - Rice Blast
        // ==========================================
        "Rice Blast" to CropDiseaseInfo(
            diseaseName = "Rice Blast",
            diseaseNameUrdu = "دھان کا بلاسٹ (جھونکا روگ / گردن توڑ)",
            cropOrTreeName = "Rice (دھان / چاول)",
            cropOrTreeNameUrdu = "دھان کی فصل",
            isTree = false,
            confidence = 0.96f,
            severity = "Critical (سٹے کے نقصان کا خطرہ)",
            severityUrdu = "انتہائی شدید / سٹا کالا ہو کر ٹوٹنا",
            monitoringPeriod = "7 to 10 Days",
            monitoringPeriodUrdu = "7 سے 10 دن تک مسلسل معائنہ",
            irrigationGuideEnglish = "Maintain 2-3 inches shallow standing water; never allow paddy field to suffer moisture stress.",
            irrigationGuideUrdu = "کھیت میں 2 سے 3 انچ پانی کی سطح برقرار رکھیں، فصل کو وتر سوکھنے کا جھٹکا نہ لگنے دیں۔",
            approvedPesticidesEnglish = listOf(
                "Beam 75% WP (Tricyclazole - Corteva) @ 120 g per acre in 100 L water (National Rice Commission).",
                "Fuji-One 40% EC (Isoprothiolane) @ 400 ml per acre.",
                "Nativo 75% WG (Tebuconazole + Trifloxystrobin) @ 65 g per acre at boot stage."
            ),
            approvedPesticidesUrdu = listOf(
                "کورٹیوا کا بیم (Beam 75% WP - Tricyclazole) بحساب 120 گرام فی ایکڑ 100 لیٹر پانی میں سپرے کریں۔",
                "فوجی ون (Fuji-One 40% EC) بحساب 400 ملی لیٹر فی ایکڑ سپرے کریں۔",
                "نیٹیوو 75 ڈبلیو جی بحساب 65 گرام فی ایکڑ گوبھ کی حالت پر سپرے کریں۔"
            ),
            symptomsEnglish = listOf(
                "Spindle-shaped elliptical lesions with gray-white centers and dark brown margins.",
                "Rotting and breakage of panicle neck causing empty chaffy heads.",
                "Rapid burning of upper flag leaves."
            ),
            symptomsUrdu = listOf(
                "پتوں پر بیضوی تکیہ نما دھبے جن کا درمیان سرمئی اور کنارے گہرے بھورے ہوتے ہیں۔",
                "سٹے کی گردن کالی ہو کر ٹوٹ جاتی ہے اور دانے خالی رہ جاتے ہیں (گردن توڑ بلاسٹ)۔",
                "جھنڈا پتا تیزی سے جل کر سوکھ جاتا ہے۔"
            ),
            organicRemediesEnglish = listOf(
                "Apply silicon nutrient fertilizers to harden leaf outer cuticle against fungus hyphae.",
                "Incorporate bio-agent Pseudomonas fluorescens @ 1 kg per acre."
            ),
            organicRemediesUrdu = listOf(
                "پودوں کی جلد مضبوط بنانے کے لیے سلیکان کھاد کا استعمال کریں۔",
                "سوڈوموناس بائیو ایجنٹ کو پانی میں ملا کر زمین میں چلائیں۔"
            ),
            preventiveMeasuresEnglish = "Treat seed with fungicide before sowing; split urea doses instead of heavy single application.",
            preventiveMeasuresUrdu = "بوائی سے پہلے بیج کا زہر آلود علاج کریں اور نائٹروجن کی کھاد قسطوں میں دیں۔"
        ),

        // ==========================================
        // 7. RICE CROP (دھان) - Bacterial Blight
        // ==========================================
        "Rice Bacterial Blight" to CropDiseaseInfo(
            diseaseName = "Rice Bacterial Blight",
            diseaseNameUrdu = "دھان کا بیکٹیریل بلائٹ (پتوں کا جھلسائو)",
            cropOrTreeName = "Rice (دھان / چاول)",
            cropOrTreeNameUrdu = "دھان کی فصل",
            isTree = false,
            confidence = 0.94f,
            severity = "High (پتوں کا جھلساؤ)",
            severityUrdu = "شدید جھلساؤ / فوری نکاسی آب",
            monitoringPeriod = "7 Days",
            monitoringPeriodUrdu = "7 دن تک مسلسل مانیٹرنگ",
            irrigationGuideEnglish = "Drain stagnant water from the field for 3-4 days to let roots aerate and halt bacterial multiplication.",
            irrigationGuideUrdu = "کھیت سے کھڑا پانی فوری طور پر 3 سے 4 دن کے لیے نکال کر دھوپ اور ہوا لگوائیں۔",
            approvedPesticidesEnglish = listOf(
                "Copper Oxychloride 50% WP @ 2.5 g/L of water.",
                "Streptocycline + Plantomycin antibiotic blend @ 100 mg/L.",
                "Bismerthiazol (Kocide) 20% WP spray during early symptoms."
            ),
            approvedPesticidesUrdu = listOf(
                "کاپر آکسی کلورائڈ 50 فیصد WP بحساب 2.5 گرام فی لیٹر پانی سپرے کریں۔",
                "اسٹریپٹومائسن اور پلانٹومائسن مکسچر 100 ملی گرام فی لیٹر پانی ملا کر چھڑکاؤ کریں۔",
                "بیماری کے ابتدائی حملے میں کوسائیڈ کا سپرے دہرائیں۔"
            ),
            symptomsEnglish = listOf(
                "Water-soaked lesions on leaf margins turning yellow to grayish-white.",
                "Leaves wilt and roll up rapidly under humid conditions.",
                "Milky bacterial ooze drops visible on young lesions early morning."
            ),
            symptomsUrdu = listOf(
                "پتوں کے کناروں پر پانی کے دھبے جو زرد اور بعد میں سرمئی سفید ہو جاتے ہیں۔",
                "زیادہ نمی کے دوران پتے سوکھ کر مڑ جاتے ہیں۔",
                "صبح سویرے تازہ دھبوں پر بیکٹیریا کے قطرے دیکھے جا سکتے ہیں۔"
            ),
            organicRemediesEnglish = listOf(
                "Reduce excessive nitrogen/urea fertilization; supplement with potassium (MOP).",
                "Spray fresh neem seed kernel extract (NSKE 5%) or garlic bulb extract."
            ),
            organicRemediesUrdu = listOf(
                "یوریا کا استعمال فوری روکیں اور پوٹاش کھاد ڈالیں۔",
                "نیم کے بیجوں کا عرق یا لہسن کا پانی قدرتی اینٹی بیکٹیریل کے طور پر چھڑکیں۔"
            ),
            preventiveMeasuresEnglish = "Avoid deep water standing and mechanical injury during nursery transplantation.",
            preventiveMeasuresUrdu = "پنیری کی منتقلی کے وقت پتوں کو چوٹ لگنے سے بچائیں اور پانی کا اعتدال رکھیں۔"
        ),

        // ==========================================
        // 8. HEALTHY CROP / TREE (صحت مند فصل یا درخت)
        // ==========================================
        "Healthy Crop" to CropDiseaseInfo(
            diseaseName = "Healthy Crop",
            diseaseNameUrdu = "صحت مند فصل / درخت (کوئی بیماری نہیں)",
            cropOrTreeName = "Agricultural Tree / Crop (صحت مند پودا)",
            cropOrTreeNameUrdu = "صحت مند فصل یا باغ",
            isTree = false,
            confidence = 0.98f,
            severity = "None (پوری طرح صحت مند)",
            severityUrdu = "لاجواب صحت اور شادابی",
            monitoringPeriod = "Standard weekly scouting",
            monitoringPeriodUrdu = "ہفتہ وار معمول کی پیسٹ اسکاؤٹنگ جاری رکھیں",
            irrigationGuideEnglish = "Maintain optimal scheduled irrigation based on crop growth stage.",
            irrigationGuideUrdu = "موسم اور ضرورت کے مطابق متوازن پانی دیں۔ وتر قائم رکھیں۔",
            approvedPesticidesEnglish = listOf(
                "No chemical pesticide or fungicide spray required at this stage.",
                "Apply prophylactic balanced micronutrients (Zinc + Boron + Magnesium)."
            ),
            approvedPesticidesUrdu = listOf(
                "اس وقت کسی زہر یا کیمیائی سپرے کی قطعاً کوئی ضرورت نہیں ہے۔",
                "پودے کی تندرستی برقرار رکھنے کے لیے زنک اور بوران کا غذائی اسپرے کر سکتے ہیں۔"
            ),
            symptomsEnglish = listOf(
                "Vibrant emerald foliage with vigorous vegetative growth.",
                "Clean leaf margins without lesions, spots, or chlorotic halos."
            ),
            symptomsUrdu = listOf(
                "سبز شاداب، چمکدار اور سیدھے مضبوط پتے۔",
                "کسی قسم کا داغ، کیڑے یا فنگس کا حملہ موجود نہیں ہے۔"
            ),
            organicRemediesEnglish = listOf(
                "Continue organic soil conditioning with farmyard compost and bio-fertilizers."
            ),
            organicRemediesUrdu = listOf(
                "زمین کی زرخیزی برقرار رکھنے کے لیے نامیاتی گوبر کی کھاد استعمال کرتے رہیں۔"
            ),
            preventiveMeasuresEnglish = "Conduct routine weekly scouting to detect any airborne spores early.",
            preventiveMeasuresUrdu = "ہفتہ وار معائنہ جاری رکھیں تاکہ فصل ہر قسم کے کیڑوں سے محفوظ رہے۔"
        )
    )

    // Fallback alias for legacy keys
    val CLASS_NAMES = listOf("Bacterial Blight", "Blast", "Brown Spot", "Healthy")
}
