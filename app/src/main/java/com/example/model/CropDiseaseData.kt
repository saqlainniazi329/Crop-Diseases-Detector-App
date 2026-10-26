package com.example.model

/**
 * Data model for Crop and Orchard Tree diagnosis with Botanical Leaf Morphology,
 * Plant Architecture, Symptom Location, Government of Pakistan (DPP & PARC)
 * approved agrochemical remedies, and automated Urdu voice narration.
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

    // Botanical Morphology & Architecture
    val leafShapeEnglish: String,
    val leafShapeUrdu: String,             // پتے کی ساخت و شکل
    val plantStructureUrdu: String,        // پودے اور تنے کا سٹرکچر
    val diseaseLocationUrdu: String,       // بیماری پودے کے کس حصے پر لگتی ہے
    val identificationGuideUrdu: String,   // کسان کیسے پہچانے کہ پتا اسی فصل کا ہے

    // Agronomic Management
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
    val audioScriptUrdu: String = "",

    // Quality Guard (Prevents misguiding farmers on non-leaf images)
    val isUnknownOrRejected: Boolean = false,
    val rejectionReasonUrdu: String = ""
)

/**
 * Botanical Specimen Specification for Plant & Leaf Anatomy Guide
 */
data class PlantBotanicalProfile(
    val id: String,
    val nameEnglish: String,
    val nameUrdu: String,
    val categoryUrdu: String, // باغ کا درخت یا کھیت کی فصل
    val leafTypeUrdu: String,
    val leafVenationUrdu: String,
    val plantHeightUrdu: String,
    val whereDiseaseAttacksUrdu: String,
    val identifyingFeaturesUrdu: String,
    val primaryDiseaseUrdu: String,
    val isTree: Boolean
)

object CropDiseaseKnowledgeBase {

    val BOTANICAL_CATALOG = listOf(
        PlantBotanicalProfile(
            id = "cotton",
            nameEnglish = "Cotton (کپاس)",
            nameUrdu = "کپاس کا پودا",
            categoryUrdu = "کھیت کی اہم نقد آور فصل",
            leafTypeUrdu = "پنجہ نما پتا (Palmate Lobed) جس کے 3 سے 5 گہرے دندانے دار حصے ہوتے ہیں",
            leafVenationUrdu = "پھیلی ہوئی جال دار رگیں (Reticulate Venation)",
            plantHeightUrdu = "جھاڑی نما پودا، اونچائی 3 سے 5 فٹ، مضبوط شاخیں",
            whereDiseaseAttacksUrdu = "پتے کی نچلی سطح پر سفید مکھی رس چوستی ہے جس سے پتے اوپر کی طرف پیالے نما مڑ جاتے ہیں اور رگیں موٹی ہو جاتی ہیں",
            identifyingFeaturesUrdu = "چوڑا پنجہ نما پتا، سبز زردی مائل رنگ، ڈنڈی کے پاس گہری رگیں",
            primaryDiseaseUrdu = "کپاس کا پتہ مروڑ وائرس (Cotton Leaf Curl Virus - CLCuV)",
            isTree = false
        ),
        PlantBotanicalProfile(
            id = "wheat",
            nameEnglish = "Wheat (گندم)",
            nameUrdu = "گندم کی فصل",
            categoryUrdu = "غذائی اناج کی بنیادی فصل",
            leafTypeUrdu = "تلوار نما، لمبی، سیدھی اور باریک پتی (Linear Leaf Blade)",
            leafVenationUrdu = "سیدھی متوازی رگیں (Parallel Venation)",
            plantHeightUrdu = "گھاس نما تنا، اونچائی 2.5 سے 4 فٹ، خوشہ دار سٹہ",
            whereDiseaseAttacksUrdu = "پتے کی اوپری سطح پر لمبائی کے رخ سیدھی پیلی دھاریاں اور پاؤڈر نما مسام نمودار ہوتے ہیں",
            identifyingFeaturesUrdu = "پتلی لمبی نوک دار پتی، ہموار سطح، درمیانی ہلکی لکیر",
            primaryDiseaseUrdu = "گندم کی پیلی کنگی (Yellow / Stripe Rust)",
            isTree = false
        ),
        PlantBotanicalProfile(
            id = "mango",
            nameEnglish = "Mango (آم کا درخت)",
            nameUrdu = "آم کا سدا بہار درخت / باغ",
            categoryUrdu = "پھلوں کا بادشاہ - باغ کا سدا بہار درخت",
            leafTypeUrdu = "لمبوترا، چوڑا، چمکیلا اور چرمی پتا (Oblong Lanceolate, Leathery Waxy)",
            leafVenationUrdu = "مرکزی موٹی رگ سے دائیں بائیں نکلتی شاخ دار رگیں (Pinnate Venation)",
            plantHeightUrdu = "بہت بڑا سایہ دار درخت، اونچائی 30 سے 60 فٹ، گہرا موٹا تنا",
            whereDiseaseAttacksUrdu = "پتے کی نوک، کناروں اور نئی نکلنے والی کونپلوں پر گہرے کالے دھبے اور گنڈی کا جھلسائو",
            identifyingFeaturesUrdu = "گہرا سبز چمکیلا رنگ، مسلنے پر مخصوص خوشبو، موٹی چھال",
            primaryDiseaseUrdu = "آم کا اینتھراکنوز (Mango Anthracnose - کالا دھبہ)",
            isTree = true
        ),
        PlantBotanicalProfile(
            id = "citrus",
            nameEnglish = "Citrus / Kinnow (کنو / مالٹا)",
            nameUrdu = "کنو، مالٹا و سٹرس باغات",
            categoryUrdu = "ترشاوہ پھلوں کا باغیچہ",
            leafTypeUrdu = "بیضوی چمکیلا پتا جس کی ڈنڈی پر پروں نما چھوٹی پتیاں ہوتی ہیں (Winged Petiole)",
            leafVenationUrdu = "جال دار رگیں اور پتے میں تیل کے باریک شفاف غدود (Oil Glands)",
            plantHeightUrdu = "جھاڑی دار درمیانہ درخت، کانٹے دار شاخیں، اونچائی 10 سے 20 فٹ",
            whereDiseaseAttacksUrdu = "پتے کی دونوں سطحوں پر ابھرے ہوئے کھردرے چھالے جن کے گرد زرد رنگ کا ہالہ ہوتا ہے",
            identifyingFeaturesUrdu = "روشنی میں دیکھنے پر تیل کے شفاف نقطے، بیضوی خوشبودار پتا",
            primaryDiseaseUrdu = "سٹرس کینکر (Citrus Canker - بیکٹیریائی کوڑھ)",
            isTree = true
        ),
        PlantBotanicalProfile(
            id = "rice",
            nameEnglish = "Rice (دھان / باسمتی چاول)",
            nameUrdu = "دھان کی فصل",
            categoryUrdu = "پانی میں اگنے والی خریف کی فصل",
            leafTypeUrdu = "لمبی کھردری تلوار نما پتی مع نوک دار سرا (Lanceolate Blade)",
            leafVenationUrdu = "متوازی رگیں مع نمایاں درمیانی رگ (Midrib Keel)",
            plantHeightUrdu = "شاخداری والا جھاڑ دار پودا، اونچائی 3 سے 4.5 فٹ",
            whereDiseaseAttacksUrdu = "پتے کے درمیان میں تکلا نما (Spindle-shaped) بھورے دھبے جن کے کنارے سرمئی ہوتے ہیں",
            identifyingFeaturesUrdu = "پتے پر باریک روئیں، پانی میں اگنے کی صلاحیت، تلوار نما رخ",
            primaryDiseaseUrdu = "رائس بلاسٹ (Rice Blast) اور بیکٹیریل بلائٹ",
            isTree = false
        ),
        PlantBotanicalProfile(
            id = "guava",
            nameEnglish = "Guava (امرود کا درخت)",
            nameUrdu = "امرود کا باغ / درخت",
            categoryUrdu = "پھل دار درخت",
            leafTypeUrdu = "آمنے سامنے جوڑے میں لگے چوڑے بیضوی موٹے پتے (Opposite Elliptic)",
            leafVenationUrdu = "پتے کی پشت پر گہری ابھری ہوئی خمدار رگیں (Prominent Ribs)",
            plantHeightUrdu = "پھیلاؤ والا درمیانہ درخت، چھلکا اتارنے والا ہموار ملائم تنا",
            whereDiseaseAttacksUrdu = "پتے زرد ہو کر تانبے کے رنگ میں بدلتے ہیں، اوپر سے سوکھ کر گرتے ہیں اور جڑیں کالی ہو جاتی ہیں",
            identifyingFeaturesUrdu = "شا پر ایک دوسرے کے سامنے دو دو پتے، موٹی ابھری رگیں",
            primaryDiseaseUrdu = "امرود کا سوکڑا (Guava Wilt - فیوزیرم مرجھاؤ)",
            isTree = true
        ),
        PlantBotanicalProfile(
            id = "sugarcane",
            nameEnglish = "Sugarcane (کماد / گنا)",
            nameUrdu = "کماد / گنے کی فصل",
            categoryUrdu = "صنعتی و نقد آور فصل",
            leafTypeUrdu = "انتہائی چوڑی، لمبی، خمدار پتی جس کے کنارے تیز چبھنے والے ہوتے ہیں",
            leafVenationUrdu = "متوازی رگیں مع درمیان میں موٹی سفید رگ (Prominent White Midrib)",
            plantHeightUrdu = "گانٹھوں والا سخت موٹا تنا، اونچائی 8 سے 14 فٹ",
            whereDiseaseAttacksUrdu = "پتے کی درمیانی سفید رگ سرخ ہو جاتی ہے اور اندر سے تنا گل کر شراب جیسی بو دیتا ہے",
            identifyingFeaturesUrdu = "درمیان میں واضح چوڑی سفید رگ، چبھنے والے کنارے",
            primaryDiseaseUrdu = "گنے کا کینسر (Sugarcane Red Rot - سرخ گلن)",
            isTree = false
        ),
        PlantBotanicalProfile(
            id = "apple",
            nameEnglish = "Apple (سیب کا درخت)",
            nameUrdu = "سیب کا پہاڑی باغ",
            categoryUrdu = "سرد علاقوں کا پھل دار درخت",
            leafTypeUrdu = "چھوٹا بیضوی پتا جس کے کنارے باریک دندانے دار ہوتے ہیں (Serrated Ovate)",
            leafVenationUrdu = "شاخ در شاخ جال دار رگیں (Pinnate Reticulate)",
            plantHeightUrdu = "پت جھڑ والا مضبوط درخت، اونچائی 15 سے 25 فٹ",
            whereDiseaseAttacksUrdu = "پتے کی اوپری سطح پر زیتونی سبز مخملی گول دھبے جو بعد میں کالے خارش جیسے ہو جاتے ہیں",
            identifyingFeaturesUrdu = "دندانے دار کنارے، نچلی سطح پر ہلکا رواں",
            primaryDiseaseUrdu = "سیب کا خارش (Apple Scab)",
            isTree = true
        ),
        PlantBotanicalProfile(
            id = "safeda",
            nameEnglish = "Eucalyptus / Safeda (سفیدہ کا درخت)",
            nameUrdu = "سفیدہ کا قدآور درخت",
            categoryUrdu = "جنگلاتی و لکڑی کا تیز رفتار درخت",
            leafTypeUrdu = "درانتی نما لمبوترا خمدار پتا مع تیز مہک (Sickle-shaped Falcate Leaf)",
            leafVenationUrdu = "پھیلی باریک رگیں مع پتے کے کنارے پر مسلسل رگ (Intramarginal Vein)",
            plantHeightUrdu = "انتہائی لمبا سیدھا درخت، اونچائی 40 سے 90 فٹ، سفید و سرمئی چھلکا",
            whereDiseaseAttacksUrdu = "پتے کی دونوں سطحوں پر چھوٹے ابھرے ہوئے گانٹھ نما چھالے (Gall) اور بھورے داغ",
            identifyingFeaturesUrdu = "مسلنے پر کافور و یوکلپٹس کی تیز خوشبو، لمبی درانتی جیسی شکل",
            primaryDiseaseUrdu = "سفیدہ کا گال ویسپ اور پتا جھلسائو (Eucalyptus Gall & Leaf Blight)",
            isTree = true
        ),
        PlantBotanicalProfile(
            id = "pomegranate",
            nameEnglish = "Pomegranate / Anaar (انار کا درخت)",
            nameUrdu = "انار کا باغبانی درخت",
            categoryUrdu = "پھل دار درخت",
            leafTypeUrdu = "چھوٹا، چمکدار بیضوی پتا مع ملائم کنارے (Glossy Narrow Oblong)",
            leafVenationUrdu = "باریک جال دار رگیں (Reticulate)",
            plantHeightUrdu = "جھاڑی نما چھوٹا درخت، اونچائی 12 سے 20 فٹ، سرخی مائل ٹہنیاں",
            whereDiseaseAttacksUrdu = "پتے پر تیل کی طرح چمکتے چھوٹے گہرے بھورے پانی والے دھبے جو بعد میں کالے ہو جاتے ہیں (تیلیا روگ)",
            identifyingFeaturesUrdu = "چھوٹے چمکدار پتے، تیکھی چھوٹی شاخیں، سرخ پھول",
            primaryDiseaseUrdu = "انار کا بیکٹیریل بلائٹ / تیلیا روگ (Bacterial Blight / Teliya)",
            isTree = true
        ),
        PlantBotanicalProfile(
            id = "datepalm",
            nameEnglish = "Date Palm / Khajoor (کھجور کا درخت)",
            nameUrdu = "کھجور کا صحرائی و نہری درخت",
            categoryUrdu = "نخلستان کا نقد آور درخت",
            leafTypeUrdu = "دیو قامت پنکھ نما لمبا پتا جس میں سینکڑوں سخت تیز نوک دار پتیاں ہوتی ہیں (Pinnate Frond)",
            leafVenationUrdu = "ہر چھوٹی پتی پر سخت متوازی رگ مع نوکیلا کانٹا",
            plantHeightUrdu = "سیدھا تنے والا درخت، اونچائی 30 سے 60 فٹ، کھردرا تنا",
            whereDiseaseAttacksUrdu = "پتوں کی دونوں سطحوں پر سخت کالے مسام اور زرد حلقے (False Smut)",
            identifyingFeaturesUrdu = "نوک دار سخت پتیاں، بنیاد پر کانٹے، چھتری نما تاج",
            primaryDiseaseUrdu = "کھجور کا فالس سمٹ (Graphiola False Smut / کالا دھبہ)",
            isTree = true
        ),
        PlantBotanicalProfile(
            id = "maize",
            nameEnglish = "Maize / Corn (مکئی کی فصل)",
            nameUrdu = "مکئی کی فصل",
            categoryUrdu = "اناج و چارہ جات کی اہم فصل",
            leafTypeUrdu = "چوڑی لمبی لہراتی تلوار نما پتی (Broad Linear Lanceolate Blade)",
            leafVenationUrdu = "متوازی رگیں مع درمیان میں موٹی خمدار رگ",
            plantHeightUrdu = "موٹا گانٹھوں والا تنہا تنا، اونچائی 6 سے 9 فٹ، اوپر کلفی (Tassel)",
            whereDiseaseAttacksUrdu = "پتے کے درمیان میں فال آرمی ورم کا سوراخ کرنا اور ٹرکم بلائٹ کے لمبے کشتی نما بھورے دھبے",
            identifyingFeaturesUrdu = "پتے کی لہر دار کنارے، درمیان میں گہرا گھونگھٹ (Whorl)",
            primaryDiseaseUrdu = "مکئی کا پتا جھلسائو و فال آرمی ورم (Turcicum Blight & Armyworm)",
            isTree = false
        )
    )

    val KNOWLEDGE = mapOf(
        // 1. MANGO TREE - Anthracnose
        "Mango Anthracnose" to CropDiseaseInfo(
            diseaseName = "Mango Anthracnose",
            diseaseNameUrdu = "آم کا اینتھراکنوز (پتوں و کیری کا کالا دھبہ)",
            cropOrTreeName = "Mango Tree (درخت آم)",
            cropOrTreeNameUrdu = "آم کا باغ / درخت",
            isTree = true,
            confidence = 0.96f,
            severity = "High (شدید خطرہ)",
            severityUrdu = "شدید / فوری توجہ طلب",
            leafShapeEnglish = "Oblong-lanceolate leathery leaf with dark necrotic lesions",
            leafShapeUrdu = "لمبوترا چمکیلا چرمی پتا، کالی نوک اور گول کٹاؤ والے داغ",
            plantStructureUrdu = "بہت بڑا سدا بہار باغ کا درخت (30 تا 50 فٹ)، پھیلنے والی موٹی چھتری نما شاخیں",
            diseaseLocationUrdu = "پتے کی نوک اور کناروں پر، نئی پھوٹنے والی نرم کونپلوں پر اور آم کی کیری کے ڈنڈل پر",
            identificationGuideUrdu = "پتے کو ہاتھ لگانے پر چمڑا محسوس ہوتا ہے، مسلنے پر آم کی مخصوص خوشبو آتی ہے اور کنارے لہر دار ہوتے ہیں۔",
            monitoringPeriod = "10 to 14 Days continuous field inspection",
            monitoringPeriodUrdu = "10 سے 14 دن تک روزانہ معائنہ کریں",
            irrigationGuideEnglish = "Halt flood irrigation; keep soil moist but prevent stagnant water around tree trunks.",
            irrigationGuideUrdu = "تنے کے گرد کھڑا پانی فوری نکالیں؛ ہلکی نمی رکھیں مگر سیلاب نما پانی بند کریں۔",
            approvedPesticidesEnglish = listOf(
                "Nativo 75 WG (Tebuconazole + Trifloxystrobin - Bayer) @ 65 g/acre.",
                "Score 250 EC (Difenoconazole - Syngenta) @ 125 ml/100 L water.",
                "Kocide 3000 (Copper Hydroxide) @ 250 g/100 L water."
            ),
            approvedPesticidesUrdu = listOf(
                "نیٹیوو (Nativo 75 WG - Bayer) بحساب 65 گرام فی ایکڑ سپرے کریں۔",
                "سکور (Score 250 EC - Syngenta) بحساب 125 ملی لیٹر فی 100 لیٹر پانی میں ملائیں۔",
                "کوسائیڈ (Kocide 3000) تانبے والا فنگسائڈ 250 گرام فی 100 لیٹر پانی میں استعمال کریں۔"
            ),
            symptomsEnglish = listOf(
                "Dark brown to black irregular necrotic spots with yellow chlorotic halos on leaf blades.",
                "Dieback of tender terminal twigs and blossom blight on inflorescences.",
                "Premature dropping of young fruitlets with sunken black pepper-spot lesions."
            ),
            symptomsUrdu = listOf(
                "پتے کے کناروں اور نوک پر سیاہ گہرے بے قاعدہ دھبے جن کے گرد زرد ہالہ ہوتا ہے۔",
                "نئی شاخیں اوپر سے نیچے کی طرف سوکھنا شروع ہوتی ہیں (ڈائی بیک)۔",
                "آم کا بور اور چھوٹی کیریاں کالی ہو کر گرنے لگتی ہیں۔"
            ),
            organicRemediesEnglish = listOf(
                "Prune dead twigs 4 inches below infected zone and apply Bordeaux paste.",
                "Spray bio-fungicide Trichoderma harzianum @ 5 g/L during vegetative flush."
            ),
            organicRemediesUrdu = listOf(
                "متاثرہ ٹہنیاں تندرست حصے سے 4 انچ نیچے سے کاٹ کر تلف کریں اور کٹائی پر بورڈو پیسٹ لگائیں۔",
                "باغ میں گری ہوئی سڑی ہوئی کیریاں اور پتے جمع کر کے دفن کریں۔"
            ),
            preventiveMeasuresEnglish = "Maintain 80% open tree canopy via post-harvest pruning for sunlight penetration.",
            preventiveMeasuresUrdu = "پھل اترنے کے بعد درختوں کی مناسب کٹائی کریں تاکہ روشنی اور ہوا اندر تک پہنچ سکے۔"
        ),

        // 2. CITRUS ORCHARD - Canker
        "Citrus Canker" to CropDiseaseInfo(
            diseaseName = "Citrus Canker",
            diseaseNameUrdu = "سٹرس کینکر (پتوں اور پھل کا بیکٹیریائی کوڑھ)",
            cropOrTreeName = "Citrus / Kinnow (کنو / مالٹا)",
            cropOrTreeNameUrdu = "کنو و مالٹا کا باغ",
            isTree = true,
            confidence = 0.94f,
            severity = "Severe / Contagious (انتہائی خطرناک)",
            severityUrdu = "انتہائی خطرناک (چھوت کی بیماری)",
            leafShapeEnglish = "Glossy oval leaf with winged petiole and raised corky pustules",
            leafShapeUrdu = "بیضوی چمکیلا پتا، ڈنڈی کے پاس پروں نما پتی، ابھرے ہوئے کھردرے چھالے",
            plantStructureUrdu = "جھاڑی دار درمیانہ درخت مع تیز نوک دار کانٹے، سدا بہار شاخیں",
            diseaseLocationUrdu = "پتے کی دونوں اطراف (اوپر اور نیچے)، سبز ٹہنیوں پر اور پھل کی بالائی سطح پر",
            identificationGuideUrdu = "پتے کو روشنی کے سامنے کریں تو تیل کے باریک مسام چمکتے ہیں اور کھردرے چھالے ناخن سے محسوس ہوتے ہیں۔",
            monitoringPeriod = "7 to 10 Days continuous orchard inspection",
            monitoringPeriodUrdu = "7 سے 10 دن تک باغ کی مسلسل نگرانی ضروری ہے",
            irrigationGuideEnglish = "Avoid overhead sprinklers. Use drip or furrow irrigation to minimize canopy moisture.",
            irrigationGuideUrdu = "پودوں پر فوارے سے پانی نہ ڈالیں؛ ڈرپ یا کھالوں کے ذریعے ہلکی آبپاشی کریں۔",
            approvedPesticidesEnglish = listOf(
                "Copper Oxychloride 50 WP @ 250 g/100 L water.",
                "Kocide 3000 (Copper Hydroxide) @ 200 g/acre.",
                "Movento 240 SC (Spirotetramat - Bayer) @ 125 ml/acre to control leaf miner vectors."
            ),
            approvedPesticidesUrdu = listOf(
                "کاپر آکسی کلورائڈ (Copper Oxychloride 50 WP) بحساب 250 گرام فی 100 لیٹر پانی۔",
                "کوسائیڈ (Kocide 3000) بحساب 200 گرام فی ایکڑ سپرے کریں۔",
                "سٹرس لیف مائنر سنڈی کے خاتمے کے لیے مووینٹو (Movento 240 SC) 125 ملی لیٹر سپرے کریں۔"
            ),
            symptomsEnglish = listOf(
                "Raised blister-like corky lesions with crater-like depressions and yellow halo.",
                "Lesions appear on both upper and lower leaf surfaces.",
                "Severe defoliation and fruit drop with unmarketable blemished rinds."
            ),
            symptomsUrdu = listOf(
                "پتے کی دونوں اطراف ابھرے ہوئے کھردرے چھالے جن کے گرد زرد رنگ کا دائرہ ہوتا ہے۔",
                "پتوں اور پھلوں پر کینکر کے داغ بنتے ہیں جس سے پھل کا معیار خراب ہو جاتا ہے۔",
                "شدید حملے کی صورت میں پتے وقت سے پہلے زرد ہو کر گر جاتے ہیں۔"
            ),
            organicRemediesEnglish = listOf(
                "Spray Neem seed kernel extract (NSKE 5%) or Neem oil @ 5 ml/L.",
                "Erect tall windbreak shelterbelts around orchard boundaries."
            ),
            organicRemediesUrdu = listOf(
                "باغ میں نیم کا تیل (Neem Oil) 5 ملی لیٹر فی لیٹر پانی میں ملا کر سپرے کریں۔",
                "باغ کے کناروں پر شیشم یا جنتر کے ہوا روک باڑ لگائیں۔"
            ),
            preventiveMeasuresEnglish = "Sterilize pruning shears with 10% sodium hypochlorite between tree cuts.",
            preventiveMeasuresUrdu = "کٹائی کے اوزاروں کو جراثیم کش محلول میں دھو کر ایک درخت سے دوسرے درخت پر استعمال کریں۔"
        ),

        // 3. COTTON CROP - Leaf Curl Virus (CLCuV)
        "Cotton Leaf Curl Virus" to CropDiseaseInfo(
            diseaseName = "Cotton Leaf Curl Virus (CLCuV)",
            diseaseNameUrdu = "کپاس کا پتہ مروڑ وائرس (CLCuV)",
            cropOrTreeName = "Cotton Crop (کپاس کی فصل)",
            cropOrTreeNameUrdu = "کپاس کی فصل",
            isTree = false,
            confidence = 0.95f,
            severity = "Epidemic Threat (شدید خطرہ)",
            severityUrdu = "شدید وبا (سفید مکھی کے ذریعے پھیلاؤ)",
            leafShapeEnglish = "Palmate lobed leaf (3-5 lobes) with thickened veins and upward curling",
            leafShapeUrdu = "پنجہ نما پتا (3 سے 5 نوکیں)، موٹی رگیں اور پیالے کی طرح اوپر مڑا ہوا",
            plantStructureUrdu = "سیدھا کھڑا جھاڑی دار پودا (3 سے 5 فٹ)، چوڑے پتے اور ٹینڈے (Bolls)",
            diseaseLocationUrdu = "پتے کی نچلی سطح پر سفید مکھی کے ڈنک، رگوں کا موٹا ہونا اور پتے کا پیالہ نما مڑنا",
            identificationGuideUrdu = "پتے کا پنجہ نما ڈیزائن، رگوں کا موٹا ہونا اور الٹی سطح پر چھوٹے چھوٹے پتوں جیسے ابھار (Enation)۔",
            monitoringPeriod = "5 to 7 Days rigorous whitefly pest scouting",
            monitoringPeriodUrdu = "5 سے 7 دن تک سفید مکھی کی باقاعدہ پیسٹ اسکاؤٹنگ کریں",
            irrigationGuideEnglish = "Maintain light, frequent water applications. Avoid water stress or over-irrigation.",
            irrigationGuideUrdu = "پانی کا ہلکا وقفہ رکھیں تاکہ پودے زیادہ رسیلے ہو کر سفید مکھی کو متوجہ نہ کریں۔",
            approvedPesticidesEnglish = listOf(
                "Pyriproxyfen 10.8 EC @ 500 ml/acre against whitefly eggs and nymphs.",
                "Diafenthiuron 500 SC (Polo - Syngenta) @ 200 ml/acre for adult whitefly.",
                "Flonicamid 50 WG (Ulala) @ 60-80 g/acre."
            ),
            approvedPesticidesUrdu = listOf(
                "سفید مکھی کے انڈوں اور بچوں کے خاتمے کے لیے پائری پراکسی فین 10.8 EC بحساب 500 ملی لیٹر سپرے کریں۔",
                "بالغ سفید مکھی کے لیے ڈائی فینتھیوران (Polo) بحساب 200 ملی لیٹر فی ایکڑ استعمال کریں۔",
                "فلونکامڈ (Ulala) بحساب 60 سے 80 گرام فی ایکڑ سپرے کریں۔"
            ),
            symptomsEnglish = listOf(
                "Upward or downward cupping of leaf margins with thickened vein network.",
                "Enations (small leaf-like outgrowths) on the undersurface of leaves.",
                "Severe stunting of plant height and reduction in boll formation."
            ),
            symptomsUrdu = listOf(
                "پتوں کے کنارے پیالے کی طرح اوپر یا نیچے مڑ جاتے ہیں اور رگیں ابھر جاتی ہیں۔",
                "پتے کی نچلی سطح پر چھوٹی چھوٹی پتیوں نما ابھار (Enation) بن جاتے ہیں۔",
                "پودے کا قد چھوٹا رہ جاتا ہے اور ٹینڈے نہیں بنتے۔"
            ),
            organicRemediesEnglish = listOf(
                "Install yellow sticky traps @ 10-12 traps per acre.",
                "Spray hing (asafoetida) + sour buttermilk or neem oil as repellent."
            ),
            organicRemediesUrdu = listOf(
                "کھیت میں پیلے چپکنے والے کارڈز (Yellow Sticky Traps) 10 عدد فی ایکڑ لگائیں۔",
                "کھٹی لسی اور ہینگ کا دیسی محلول چھڑک کر سفید مکھی کو بھگائیں۔"
            ),
            preventiveMeasuresEnglish = "Eradicate alternate weed hosts (Peeli Booti - Abutilon indicum) along water channels.",
            preventiveMeasuresUrdu = "کھیت کے کناروں اور کھالوں سے پیلی بوٹی اور جڑی بوٹیاں تلف کریں۔"
        ),

        // 4. WHEAT CROP - Yellow Rust
        "Wheat Yellow Rust" to CropDiseaseInfo(
            diseaseName = "Wheat Yellow Rust",
            diseaseNameUrdu = "گندم کی پیلی کنگی (Yellow / Stripe Rust)",
            cropOrTreeName = "Wheat Crop (گندم کی فصل)",
            cropOrTreeNameUrdu = "گندم کی فصل",
            isTree = false,
            confidence = 0.95f,
            severity = "Severe Airborne Epidemic (انتہائی خطرناک)",
            severityUrdu = "انتہائی خطرناک (ہوا کے ذریعے فوری پھیلاؤ)",
            leafShapeEnglish = "Linear leaf blade with parallel yellow pustules in stripes",
            leafShapeUrdu = "تلوار نما لمبی باریک پتی، متوازی پیلی دھاریاں اور ہلدی جیسا پاؤڈر",
            plantStructureUrdu = "گھاس نما تنا مع گانٹھیں، اونچائی 3 سے 4 فٹ، خوشہ دار سٹہ",
            diseaseLocationUrdu = "پتے کی اوپری سطح پر لمبائی کے رخ متوازی دھاریوں میں",
            identificationGuideUrdu = "پتے پر ہاتھ پھیرنے سے انگلیوں پر پیلا زرد پاؤڈر لگ جاتا ہے جیسے ہلدی لگی ہو۔",
            monitoringPeriod = "3 to 5 Days daily inspection during cool humid weather",
            monitoringPeriodUrdu = "3 سے 5 دن کے اندر اندر فوری اسپرے اور نگرانی کریں",
            irrigationGuideEnglish = "Halt immediate irrigation during cold humid winds to lower canopy moisture.",
            irrigationGuideUrdu = "تیز ہوا یا بارش کے امکان پر آبپاشی مؤخر کریں تاکہ پھپھوندی کا دباؤ کم ہو۔",
            approvedPesticidesEnglish = listOf(
                "Tilt 250 EC (Propiconazole - Syngenta) @ 200 ml/acre.",
                "Nativo 75 WG (Tebuconazole + Trifloxystrobin - Bayer) @ 65 g/acre.",
                "Amistar Top @ 200 ml/acre."
            ),
            approvedPesticidesUrdu = listOf(
                "ٹلٹ (Tilt 250 EC - Propiconazole) بحساب 200 ملی لیٹر فی ایکڑ سپرے کریں۔",
                "نیٹیوو (Nativo 75 WG - Bayer) بحساب 65 گرام فی ایکڑ سپرے کریں۔",
                "ایمسٹار ٹاپ بحساب 200 ملی لیٹر فی ایکڑ سپرے کریں۔"
            ),
            symptomsEnglish = listOf(
                "Linear rows of bright yellow powdery pustules running parallel to leaf veins.",
                "Leaves turn chlorotic, dry out, and curl prematurely.",
                "Grain shriveling and drastic reduction in thousand-grain weight."
            ),
            symptomsUrdu = listOf(
                "پتے کی لمبائی کے رخ متوازی قطاروں میں زرد پیلا پاؤڈر نما مسام نمودار ہوتے ہیں۔",
                "پتے سوکھ کر جلنے لگتے ہیں اور پودا خوراک نہیں بنا پاتا۔",
                "سٹے میں دانہ باریک اور پچک جاتا ہے۔"
            ),
            organicRemediesEnglish = listOf(
                "Broadcast wood ash mixed with sulphur in early morning dew.",
                "Apply balanced muriate of potash to enhance epidermal cell wall thickness."
            ),
            organicRemediesUrdu = listOf(
                "صبح کے وقت شبنم کے دوران لکڑی کی راکھ اور گندھک کا ہلکا چھڑکاؤ کریں۔",
                "پوٹاش کھاد کا استعمال کر کے پتے کی بیرونی تہہ کو مضبوط کریں۔"
            ),
            preventiveMeasuresEnglish = "Cultivate approved rust-resistant varieties certified by PARC/Ayub Agri Research.",
            preventiveMeasuresUrdu = "محکمہ زراعت کی تصدیق شدہ کنگی کی مدافعت رکھنے والی اقسام (جیسے نشان 21، اکبر 19) کاشت کریں۔"
        ),

        // 5. GUAVA ORCHARD - Wilt
        "Guava Wilt" to CropDiseaseInfo(
            diseaseName = "Guava Wilt",
            diseaseNameUrdu = "امرود کا سوکڑا (Guava Wilt - فیوزیرم مرجھاؤ)",
            cropOrTreeName = "Guava Tree (امرود کا درخت)",
            cropOrTreeNameUrdu = "امرود کا باغ / درخت",
            isTree = true,
            confidence = 0.93f,
            severity = "Lethal Soil-Borne Disease (مہلک بیماری)",
            severityUrdu = "انتہائی مہلک (جڑ کا فنگس)",
            leafShapeEnglish = "Broad elliptic opposite leaf pair with sunken veins turning bronzed",
            leafShapeUrdu = "آمنے سامنے لگے چوڑے بیضوی موٹے پتے، ابھری رگیں اور تانبے جیسا سوکھا پتا",
            plantStructureUrdu = "درمیانہ درخت مع ملائم چھلکا اترنے والا تنا اور پھیلنے والی گھنی شاخیں",
            diseaseLocationUrdu = "شاخوں کے سرے سے نیچے کی طرف پتے زرد ہو کر سوکھتے ہیں اور جڑوں کے اندر کالی رگیں",
            identificationGuideUrdu = "امرود کی شاخ پر پتے آمنے سامنے جوڑوں میں لگے ہوتے ہیں اور سوکھنے پر لٹک جاتے ہیں۔",
            monitoringPeriod = "15 Days root and soil inspection",
            monitoringPeriodUrdu = "15 دن تک مسلسل نمی اور جڑوں کا معائنہ کریں",
            irrigationGuideEnglish = "Cease excess watering. Excavate drainage trenches between orchard rows.",
            irrigationGuideUrdu = "زیادہ اور گہرا پانی بند کریں؛ درختوں کی جڑوں میں ہوادار نمی برقرار رکھیں۔",
            approvedPesticidesEnglish = listOf(
                "Topsin-M 70 WP (Thiophanate-Methyl) @ 400 g/acre drench around root zone.",
                "Aliette 80 WP (Fosetyl-Al - Bayer) @ 250 g/100 L water drench."
            ),
            approvedPesticidesUrdu = listOf(
                "ٹوپسن ایم (Topsin-M 70 WP) بحساب 400 گرام جڑوں میں ڈرینچ (Drench) کریں۔",
                "ایلیٹ (Aliette 80 WP - Bayer) بحساب 250 گرام فی 100 لیٹر پانی جڑوں میں ڈالیں۔"
            ),
            symptomsEnglish = listOf(
                "Yellowing of foliage turning to a dull copper/bronze color before drying.",
                "Leaves droop and cling to dead branches without falling.",
                "Dark vascular discoloration inside split branch stems and tap roots."
            ),
            symptomsUrdu = listOf(
                "پتے پہلے زرد ہوتے ہیں پھر تانبے کے رنگ کے ہو کر سوکھ جاتے ہیں۔",
                "سوکھے پتے شاخوں کے ساتھ ہی لٹکے رہتے ہیں اور گرتے نہیں۔",
                "جڑیں اور تنے کا اندرونی حصہ کالا یا بھورا ہو جاتا ہے۔"
            ),
            organicRemediesEnglish = listOf(
                "Incorporate Trichoderma harzianum bio-fertilizer with enriched farmyard manure.",
                "Apply 5 kg neem cake per tree base."
            ),
            organicRemediesUrdu = listOf(
                "گوبر کی کھاد میں ٹرائیکوڈرما بائیو فنگسائڈ ملا کر درخت کے گرد گڑھے میں ڈالیں۔",
                "ہر درخت کی جڑوں کے قریب 5 کلو نیم کھلی ڈالیں۔"
            ),
            preventiveMeasuresEnglish = "Maintain soil pH around 6.5-7.5. Avoid root injury during inter-culture operations.",
            preventiveMeasuresUrdu = "ہل چلاتے وقت درخت کی موٹی جڑوں کو زخمی ہونے سے بچائیں۔"
        ),

        // 6. RICE PADDY - Blast
        "Rice Blast" to CropDiseaseInfo(
            diseaseName = "Rice Blast",
            diseaseNameUrdu = "رائس بلاسٹ (جھونکا روگ / گردن توڑ)",
            cropOrTreeName = "Rice Crop (دھان کی فصل)",
            cropOrTreeNameUrdu = "دھان / باسمتی چاول",
            isTree = false,
            confidence = 0.94f,
            severity = "Severe Fungal Blast (شدید خطرہ)",
            severityUrdu = "شدید پھپھوندی (جھونکا و گردن توڑ)",
            leafShapeEnglish = "Lanceolate leaf blade with spindle-shaped necrotic blast spots",
            leafShapeUrdu = "کھردری تلوار نما پتی مع درمیان میں تکلا نما (Spindle) بھورے داغ",
            plantStructureUrdu = "پانی میں کھڑا جھاڑی نما پودا، نچلی گانٹھیں اور اوپر خوشہ نما منجر",
            diseaseLocationUrdu = "پتے کے درمیان میں، پتوں کے جوڑ (Collar) پر اور منجر کی گردن پر",
            identificationGuideUrdu = "دھان کی لمبی پتی کے بیچ میں آنکھ یا تکلے کی شکل کا داغ جس کا مرکز سرمئی اور کنارہ گہرا بھورا ہو۔",
            monitoringPeriod = "7 Days continuous field scouting",
            monitoringPeriodUrdu = "7 دن تک کھیت کا معائنہ جاری رکھیں",
            irrigationGuideEnglish = "Maintain 2-3 inches standing water. Never allow paddy soil to dry out completely.",
            irrigationGuideUrdu = "پانی کی سطح 2 سے 3 انچ رکھیں؛ زمین کو سوکھنے نہ دیں تاکہ بلاسٹ حملہ آور نہ ہو۔",
            approvedPesticidesEnglish = listOf(
                "Beam 75 WP (Tricyclazole) @ 120 g/acre.",
                "Amistar Top (Azoxystrobin + Difenoconazole) @ 200 ml/acre.",
                "Kasugamycin 2% (Kasumin) @ 400 ml/acre."
            ),
            approvedPesticidesUrdu = listOf(
                "بیم (Beam 75 WP - Tricyclazole) بحساب 120 گرام فی ایکڑ سپرے کریں۔",
                "ایمسٹار ٹاپ (Amistar Top - Syngenta) بحساب 200 ملی لیٹر فی ایکڑ سپرے کریں۔",
                "کاسوگامائسن (Kasumin) بحساب 400 ملی لیٹر فی ایکڑ چھڑکاؤ کریں۔"
            ),
            symptomsEnglish = listOf(
                "Spindle-shaped (elliptical) spots with gray or whitish centers and brown margins.",
                "Lesions enlarge and coalesce, causing entire leaf blades to wither and blast.",
                "Rotting of panicle neck (Neck Blast), turning heads grayish and grain empty."
            ),
            symptomsUrdu = listOf(
                "پتے کے درمیان میں تکلا نما دھبے جن کا درمیان سفید سرمئی اور کنارہ بھورا ہوتا ہے۔",
                "دھبے آپس میں مل کر پورے پتے کو جھلسا دیتے ہیں۔",
                "منجر کی گردن کالی ہو جاتی ہے جس سے سٹہ خالی اور سفید ہو کر ٹوٹ جاتا ہے۔"
            ),
            organicRemediesEnglish = listOf(
                "Apply potassium silicate @ 2 kg/acre to strengthen silica cell layer.",
                "Spray fermented cow urine diluted with water (1:10)."
            ),
            organicRemediesUrdu = listOf(
                "سلیکان اور پوٹاش کھاد ڈالیں تاکہ پتے پر فنگس کا حملہ نہ ہو سکے۔",
                "نائٹروجن کھاد کی مقدار کم کر کے فاسفورس اور پوٹاش بڑھائیں۔"
            ),
            preventiveMeasuresEnglish = "Treat seed with fungicide (Thiophanate-Methyl @ 2.5 g/kg) before nursery sowing.",
            preventiveMeasuresUrdu = "پنیری بیجنے سے پہلے بیج کو محکمہ زراعت کی تجویز کردہ زہر سے ضرور شربت کریں۔"
        ),

        // 7. HEALTHY SPECIMEN (STRICT ZERO-HALLUCINATION)
        "Healthy Crop" to CropDiseaseInfo(
            diseaseName = "This plant is completely healthy",
            diseaseNameUrdu = "یہ پودا/پتہ بالکل صحت مند ہے",
            cropOrTreeName = "Healthy Specimen (صحت مند نمونہ)",
            cropOrTreeNameUrdu = "صحت مند پودا / باغ",
            isTree = false,
            confidence = 0.99f,
            severity = "Optimal Health (مکمل محفوظ)",
            severityUrdu = "بالکل محفوظ / صحتمند",
            leafShapeEnglish = "Intact vegetative leaf blade with uniform chlorophyll distribution",
            leafShapeUrdu = "مکمل تندرست پتا، یکساں گہرا سبز رنگ اور بغیر کسی داغ دھبے کے",
            plantStructureUrdu = "مضبوط تنا اور شاخیں، پھل پھول سے لدا پودا",
            diseaseLocationUrdu = "پودے پر کوئی بیماری موجود نہیں ہے",
            identificationGuideUrdu = "پتے کی رگیں ہموار، صاف، رسیلی اور بغیر کسی کیڑے یا فنگس کے نشان کے ہیں۔",
            monitoringPeriod = "Routine weekly field scouting",
            monitoringPeriodUrdu = "ہفتہ وار معمول کے مطابق معائنہ جاری رکھیں",
            irrigationGuideEnglish = "Continue regular seasonal irrigation based on crop water requirements.",
            irrigationGuideUrdu = "معمول کے مطابق موسم کی ضرورت کے تحت ہلکی آبپاشی جاری رکھیں۔",
            approvedPesticidesEnglish = listOf("No chemical pesticide or fungicide spray required. Plant is 100% healthy."),
            approvedPesticidesUrdu = listOf("یہ پودا/پتہ بالکل صحت مند ہے۔ کسی کیمیائی زہر یا سپرے کی ضرورت نہیں ہے۔"),
            symptomsEnglish = listOf("Uniform green foliage with optimal turgidity and no visible pathology."),
            symptomsUrdu = listOf("پتے بالکل تندرست، چمکدار سبز اور کسی بیماری، داغ یا جھلسائو سے پاک ہیں۔"),
            organicRemediesEnglish = listOf("Apply compost, balanced NPK, and micronutrients to maintain vigor."),
            organicRemediesUrdu = listOf("پودوں کو طاقتور رکھنے کے لیے گوبر کی کھاد اور متوازن کھادیں دیں۔"),
            preventiveMeasuresEnglish = "Keep field borders clean of weeds and inspect weekly.",
            preventiveMeasuresUrdu = "کھیت کے کناروں کو صاف رکھیں اور ہر ہفتے معائنہ کرتے رہیں۔"
        ),

        // 8. EUCALYPTUS / SAFEDA TREE - Gall & Blight
        "Eucalyptus Blight" to CropDiseaseInfo(
            diseaseName = "Eucalyptus Gall Wasp & Leaf Blight",
            diseaseNameUrdu = "سفیدہ کا گال ویسپ اور پتا جھلسائو",
            cropOrTreeName = "Eucalyptus Tree (سفیدہ کا درخت)",
            cropOrTreeNameUrdu = "سفیدہ کا قدآور درخت",
            isTree = true,
            confidence = 0.95f,
            severity = "Moderate to High (شدید خطرہ)",
            severityUrdu = "درمیانہ تا شدید (پتے سوکھنا)",
            leafShapeEnglish = "Falcate lanceolate sickle-shaped leaf with swollen pustular galls",
            leafShapeUrdu = "درانتی نما خمدار پتا مع گانٹھ نما گول ابھار اور سروں کا سوکھنا",
            plantStructureUrdu = "انتہائی قدآور لکڑی کا درخت مع سفید ہموار چھال",
            diseaseLocationUrdu = "نئی نرم کونپلوں اور پتوں کی رگوں پر ابھرے ہوئے گالز اور چھالے",
            identificationGuideUrdu = "پتے کی درانتی نما شکل اور مسلنے پر یوکلپٹس تیل کی تیز خوشبو۔",
            monitoringPeriod = "10 Days inspection for gall wasps",
            monitoringPeriodUrdu = "10 دن تک نئی نکلنے والی کونپلوں کا معائنہ کریں",
            irrigationGuideEnglish = "Eucalyptus requires deep rooting moisture; avoid surface waterlogging.",
            irrigationGuideUrdu = "جڑوں کے قریب پانی کھڑا نہ ہونے دیں؛ خشک سالی میں مناسب نمی دیں۔",
            approvedPesticidesEnglish = listOf(
                "Confidor 200 SL (Imidacloprid - Bayer) @ 250 ml/acre against gall wasps.",
                "Nativo 75 WG (Tebuconazole + Trifloxystrobin) @ 65 g/acre for leaf spot blight."
            ),
            approvedPesticidesUrdu = listOf(
                "گال ویسپ کیڑے کے خاتمے کے لیے کانفیڈور (Confidor 200 SL) بحساب 250 ملی لیٹر سپرے کریں۔",
                "پھپھوندی کے داغوں کے لیے نیٹیوو (Nativo 75 WG) 65 گرام فی ایکڑ اسپرے کریں۔"
            ),
            symptomsEnglish = listOf(
                "Small spherical pimple-like woody galls on petioles and leaf veins.",
                "Premature leaf drop and drying of juvenile shoots."
            ),
            symptomsUrdu = listOf(
                "پتوں کی رگوں اور ڈنڈیوں پر ابھری ہوئی سخت گانٹھیں اور مسام۔",
                "پتے سوکھ کر جلدی جھڑ جاتے ہیں اور درخت کی بڑھوتری رک جاتی ہے۔"
            ),
            organicRemediesEnglish = listOf("Prune heavily infested twigs during autumn; apply neem oil @ 5 ml/L."),
            organicRemediesUrdu = listOf("متاثرہ چھوٹی شاخیں کاٹ کر جلائیں اور نیم کے تیل کا سپرے کریں۔"),
            preventiveMeasuresEnglish = "Plant gall-resistant eucalyptus clones recommended by Punjab Forestry Dept.",
            preventiveMeasuresUrdu = "محکمہ جنگلات کی تجویز کردہ مدافعت رکھنے والی سفیدہ کی پنیری لگائیں۔"
        ),

        // 9. POMEGRANATE / ANAAR - Bacterial Blight (Teliya)
        "Pomegranate Blight" to CropDiseaseInfo(
            diseaseName = "Pomegranate Bacterial Blight (Teliya Rog)",
            diseaseNameUrdu = "انار کا بیکٹیریل بلائٹ / تیلیا روگ",
            cropOrTreeName = "Pomegranate Tree (انار کا درخت)",
            cropOrTreeNameUrdu = "انار کا باغبانی درخت",
            isTree = true,
            confidence = 0.96f,
            severity = "Severe Destructive Disease (انتہائی خطرناک)",
            severityUrdu = "انتہائی خطرناک (تیلیا روگ)",
            leafShapeEnglish = "Glossy narrow oblong leaves with oily water-soaked angular spots",
            leafShapeUrdu = "چھوٹا چمکدار بیضوی پتا مع تیل جیسے چکنے سیاہ تکونی داغ",
            plantStructureUrdu = "جھاڑی نما پھیلنے والا چھوٹا درخت مع کانٹے دار سرخی مائل باریک شاخیں",
            diseaseLocationUrdu = "پتوں کی اوپری سطح پر اور انار کے پھل پر پھٹنے والے داغ",
            identificationGuideUrdu = "پتے پر تیل کے قطروں جیسے چمکتے داغ اور الٹے رخ پر کالا پن۔",
            monitoringPeriod = "7 Days continuous inspection",
            monitoringPeriodUrdu = "7 دن تک مسلسل نگرانی اور داغوں کی جانچ کریں",
            irrigationGuideEnglish = "Avoid sprinkler or high-humidity flood irrigation. Use controlled drip rings.",
            irrigationGuideUrdu = "پودے کی چھتری پر پانی چھڑکنے سے گریز کریں؛ تنے سے دور گول دائرے میں پانی دیں۔",
            approvedPesticidesEnglish = listOf(
                "Kocide 3000 (Copper Hydroxide) @ 200 g/100 L water.",
                "Kasumin (Kasugamycin) @ 250 ml/100 L water."
            ),
            approvedPesticidesUrdu = listOf(
                "کوسائیڈ (Kocide 3000) تانبے والا زہر بحساب 200 گرام فی 100 لیٹر پانی سپرے کریں۔",
                "کاسوگامائسن (Kasumin) اینٹی بائیوٹک بحساب 250 ملی لیٹر فی 100 لیٹر پانی استعمال کریں۔"
            ),
            symptomsEnglish = listOf(
                "Dark oily angular water-soaked spots on leaves surrounded by yellow halos.",
                "Y-shaped cracking on pomegranate fruit rind with brown necrotic arils."
            ),
            symptomsUrdu = listOf(
                "پتوں پر تیل جیسے چمکدار پانی والے تکونی دھبے جن کے گرد زرد چھلا ہوتا ہے۔",
                "پھل کی جلد کالی ہو کر انگریزی حرف Y کی شکل میں پھٹ جاتی ہے۔"
            ),
            organicRemediesEnglish = listOf("Apply Bordeaux mixture (1%) after annual pruning and paste tree trunks."),
            organicRemediesUrdu = listOf("کٹائی کے بعد بورڈو مکسچر کا لیپ تنے پر لگائیں اور زمین پر چونے کا چھڑکاؤ کریں۔"),
            preventiveMeasuresEnglish = "Sterilize all pruning shears and remove fallen infected mummified fruits.",
            preventiveMeasuresUrdu = "کٹائی کے قینچی اور اوزاروں کو جراثیم کش دوا سے صاف کر کے استعمال کریں۔"
        ),

        // 10. DATE PALM / KHAJOOR - False Smut
        "Date Palm Smut" to CropDiseaseInfo(
            diseaseName = "Date Palm False Smut (Graphiola)",
            diseaseNameUrdu = "کھجور کا فالس سمٹ (کالا مسام و پتا جھلسائو)",
            cropOrTreeName = "Date Palm Tree (کھجور کا درخت)",
            cropOrTreeNameUrdu = "کھجور کا درخت / نخلستان",
            isTree = true,
            confidence = 0.94f,
            severity = "Chronic Foliar Infection (دائمی بیماری)",
            severityUrdu = "دائمی پھپھوندی (مسام دار داغ)",
            leafShapeEnglish = "Pinnate fronds with hard black pustules bearing yellow spore dust",
            leafShapeUrdu = "دیو قامت پنکھ نما پتا، سخت کانٹے دار نوک اور کالے ابھرے ہوئے مسام",
            plantStructureUrdu = "سیدھا اونچا کھردرا تنا اور اوپر چھتری نما گھنے پتے",
            diseaseLocationUrdu = "پرانے پتوں کی دونوں اطراف پر چھوٹے سخت مسام جن سے پیلا پاؤڈر نکلتا ہے",
            identificationGuideUrdu = "پتے کی سخت چھڑی اور نوک دار کانٹے، کالے ابھرے ہوئے داغ جن سے ہاتھ کالے ہوتے ہیں۔",
            monitoringPeriod = "14 Days seasonal scouting",
            monitoringPeriodUrdu = "14 دن تک کھجور کے پرانے پتوں کا جائزہ لیں",
            irrigationGuideEnglish = "Date palms tolerate deep watering; maintain basin irrigation without submerging trunk crown.",
            irrigationGuideUrdu = "کھجور کو گہرا پانی پسند ہے مگر تنے کے تاج پر پانی کھڑا نہ ہونے دیں۔",
            approvedPesticidesEnglish = listOf(
                "Copper Oxychloride 50 WP @ 250 g/100 L water.",
                "Score 250 EC (Difenoconazole - Syngenta) @ 100 ml/100 L water."
            ),
            approvedPesticidesUrdu = listOf(
                "کاپر آکسی کلورائڈ (Copper Oxychloride 50 WP) بحساب 250 گرام فی 100 لیٹر پانی سپرے کریں۔",
                "سکور (Score 250 EC) بحساب 100 ملی لیٹر فی 100 لیٹر پانی میں ملا کر پتوں پر دھوئیں۔"
            ),
            symptomsEnglish = listOf(
                "Small, hard, black, cup-shaped pustules erupted through the epidermis of both leaf surfaces.",
                "Yellow filaments protruding from pustules releasing powdery spores."
            ),
            symptomsUrdu = listOf(
                "پتوں کی دونوں سطحوں پر سخت کالے ابھرے ہوئے مسام بن جاتے ہیں۔",
                "مساموں سے زرد پیلے دھاگے اور پاؤڈر نکل کر دوسرے پتوں کو متاثر کرتا ہے۔"
            ),
            organicRemediesEnglish = listOf("Cut and burn heavily infected lower senescent fronds during winter pruning."),
            organicRemediesUrdu = listOf("سردیوں میں نیچے والے شدید بیمار اور سوکھے پتے کاٹ کر جلا دیں۔"),
            preventiveMeasuresEnglish = "Thin out dense frond clusters to allow sun aeration inside palm crown.",
            preventiveMeasuresUrdu = "کھجور کے تاج میں دھوپ اور ہوا کی آمدورفت کے لیے پتے مناسب فاصلے پر رکھیں۔"
        ),

        // 11. MAIZE / CORN - Leaf Blight & Armyworm
        "Maize Leaf Blight" to CropDiseaseInfo(
            diseaseName = "Maize Turcicum Leaf Blight & Armyworm",
            diseaseNameUrdu = "مکئی کا پتا جھلسائو و فال آرمی ورم",
            cropOrTreeName = "Maize Crop (مکئی کی فصل)",
            cropOrTreeNameUrdu = "مکئی کی فصل",
            isTree = false,
            confidence = 0.95f,
            severity = "Severe Yield Destroyer (شدید نقصان دہ)",
            severityUrdu = "شدید خطرہ (فصل تباہ کن)",
            leafShapeEnglish = "Broad linear lanceolate wavy blade with large elliptical boat-shaped lesions",
            leafShapeUrdu = "چوڑی لمبی لہراتی تلوار نما پتی مع لمبے کشتی نما بھورے دھبے اور چھلنی سوراخ",
            plantStructureUrdu = "گانٹھ دار مضبوط تنہا تنا مع اوپر کلفی اور سائیڈوں پر چھلی (Cob)",
            diseaseLocationUrdu = "پتے کے درمیانی گھونگھٹ میں سنڈی کا حملہ اور پتے کی سطح پر لمبے سرمئی داغ",
            identificationGuideUrdu = "پتے کی چوڑی شکل، لہر دار کنارے اور تنے کے بیچ گھونگھٹ میں لکڑی کے برادے جیسا مواد۔",
            monitoringPeriod = "4 to 6 Days rigorous whorl scouting",
            monitoringPeriodUrdu = "4 سے 6 دن تک مکئی کے گھونگھٹ اور پتوں کا روزانہ معائنہ کریں",
            irrigationGuideEnglish = "Critical water stages are flowering and cob development; avoid drought stress.",
            irrigationGuideUrdu = "چھلی بننے کے دوران پانی کی کمی نہ آنے دیں؛ زمین میں تر وتر برقرار رکھیں۔",
            approvedPesticidesEnglish = listOf(
                "Coragen 20 SC (Chlorantraniliprole) @ 50 ml/acre in the whorl for armyworm.",
                "Tilt 250 EC (Propiconazole - Syngenta) @ 200 ml/acre for Turcicum leaf blight."
            ),
            approvedPesticidesUrdu = listOf(
                "فال آرمی ورم سنڈی کے لیے کوراجن (Coragen 20 SC) بحساب 50 ملی لیٹر گھونگھٹ میں اسپرے کریں۔",
                "پتا جھلسائو فنگس کے خاتمے کے لیے ٹلٹ (Tilt 250 EC) 200 ملی لیٹر فی ایکڑ سپرے کریں۔"
            ),
            symptomsEnglish = listOf(
                "Large, elongated, spindle/boat-shaped grayish-green to tan lesions on leaves.",
                "Extensive windowpaning, shot holes, and fecal frass inside plant whorls from armyworm feeding."
            ),
            symptomsUrdu = listOf(
                "پتوں پر لمبے کشتی نما سرمئی سبز اور بھورے داغ جو پورے پتے کو سکھا دیتے ہیں۔",
                "سنڈی کے کھانے سے پتوں میں چھلنی سوراخ اور گھونگھٹ میں لکڑی کے برادے جیسا فضلہ۔"
            ),
            organicRemediesEnglish = listOf(
                "Apply dry river sand mixed with lime or ash into whorls to suffocate larvae.",
                "Erect bird perches across field borders."
            ),
            organicRemediesUrdu = listOf(
                "مکئی کے گھونگھٹ میں راکھ یا باریک ریت ڈالیں تاکہ سنڈی کا دم گھٹ جائے۔",
                "کھیت میں پرندوں کے بیٹھنے کے لیے لکڑی کے اڈے لگائیں۔"
            ),
            preventiveMeasuresEnglish = "Synchronize planting dates and avoid staggered maize planting in adjacent fields.",
            preventiveMeasuresUrdu = "علاقے کے کسانوں کے ساتھ مل کر مکئی کی ہم وقتی کاشت کریں تاکہ سنڈی منتقل نہ ہو۔"
        ),

        // 12. QUALITY GUARD: UNKNOWN / NON-LEAF SAMPLE (Anti-misguidance protection)
        "Unknown Sample" to CropDiseaseInfo(
            diseaseName = "Unrecognized Plant Sample",
            diseaseNameUrdu = "غیر تصدیق شدہ یا نامعلوم نمونہ",
            cropOrTreeName = "Unknown Object / Leaf (نامعلوم شے)",
            cropOrTreeNameUrdu = "غیر تصدیق شدہ پتا",
            isTree = false,
            confidence = 0.99f,
            severity = "Diagnosis Prevented (تشخیص روک دی گئی)",
            severityUrdu = "غلط فہمی سے بچاؤ کے لیے محفوظ",
            leafShapeEnglish = "Irregular / Non-botanical texture detected",
            leafShapeUrdu = "پتے کی قدرتی نباتاتی رگیں یا سبزین انڈیکس واضح نہیں ہے",
            plantStructureUrdu = "کسی منظور شدہ درخت یا فصل کے سٹرکچر سے مطابقت نہیں رکھتا",
            diseaseLocationUrdu = "کوئی مستند علامات نہیں ملیں",
            identificationGuideUrdu = "کسان بھائی! ایپ نے غلط رہنمائی سے بچانے کے لیے شناخت روک دی ہے۔ براہ کرم پودے کے پتے کی واضح تصویر لیں۔",
            monitoringPeriod = "Rescan required",
            monitoringPeriodUrdu = "براہ کرم پتے کی قریب سے صاف تصویر دوبارہ لیں",
            irrigationGuideEnglish = "N/A - Please scan a clear leaf photograph.",
            irrigationGuideUrdu = "صرف پودے کے پتے کی تصویر کھینچیں۔",
            approvedPesticidesEnglish = listOf("No pesticide prescribed. Re-scan required."),
            approvedPesticidesUrdu = listOf("کسی دوا کا استعمال نہ کریں؛ پہلے پتے کی درست تصویر دوبارہ کھینچیں۔"),
            symptomsEnglish = listOf("Image does not match agricultural leaf morphology."),
            symptomsUrdu = listOf("تصویر میں کسی منظور شدہ فصل یا درخت کے پتے کے نباتاتی خدوخال نہیں ملے۔"),
            organicRemediesEnglish = listOf("Capture close-up photo in good daylight."),
            organicRemediesUrdu = listOf("اچھی روشنی میں پتے کی واضح تصویر لیں۔"),
            preventiveMeasuresEnglish = "Ensure camera lens is focused on the infected foliage.",
            preventiveMeasuresUrdu = "کیمرے کو پتے کے بالکل قریب لا کر فوکس کریں۔",
            isUnknownOrRejected = true,
            rejectionReasonUrdu = "یہ تصویر کسی منظور شدہ پودے یا درخت کے پتے سے مماثلت نہیں رکھتی۔ کسان کو کسی بھی غلط فہمی یا غلط دوائی کے استعمال سے بچانے کے لیے سسٹم نے غیر مصدقہ نتیجے کو بلاک کر دیا ہے۔"
        ),

        // 13. AUTO-RECOVERY PROTOCOL: UNCLEAR / DARK / OUT-OF-FOCUS IMAGE
        "Unclear Image" to CropDiseaseInfo(
            diseaseName = "Unclear / Dark Image",
            diseaseNameUrdu = "غیر واضح یا تاریک تصویر",
            cropOrTreeName = "Unclear Leaf (غیر واضح پتا)",
            cropOrTreeNameUrdu = "غیر واضح پتا",
            isTree = false,
            confidence = 0.99f,
            severity = "Auto-Recovery Triggered",
            severityUrdu = "دوبارہ تصویر کی ضرورت",
            leafShapeEnglish = "Blurry / low contrast / inadequate illumination",
            leafShapeUrdu = "دھندلی تصویر یا کم روشنی کے باعث پتے کی رگیں اور ساخت ظاہر نہیں ہو رہی ہیں",
            plantStructureUrdu = "تصویر دھندلی ہونے کی وجہ سے ساخت کی تصدیق نہیں ہو سکی",
            diseaseLocationUrdu = "علامات غیر واضح ہیں",
            identificationGuideUrdu = "تصویر واضح نہیں ہے۔ براہ کرم دوبارہ صاف تصویر کھینچیں۔",
            monitoringPeriod = "Immediate Re-scan",
            monitoringPeriodUrdu = "فوری طور پر صاف تصویر دوبارہ کھینچیں",
            irrigationGuideEnglish = "N/A - Please recapture clear leaf photo.",
            irrigationGuideUrdu = "پودے کی اچھی روشنی میں صاف تصویر کھینچیں۔",
            approvedPesticidesEnglish = listOf("No chemicals prescribed. Retake photo first."),
            approvedPesticidesUrdu = listOf("کوئی اسپرے نہ کریں، پہلے صاف تصویر دوبارہ لیں۔"),
            symptomsEnglish = listOf("Low pixel contrast or optical blur detected."),
            symptomsUrdu = listOf("تصویر کا فوکس آؤٹ ہے یا روشنی بہت کم ہے۔"),
            organicRemediesEnglish = listOf("Clean phone camera lens and use daylight."),
            organicRemediesUrdu = listOf("موبائل کیمرے کا لینز صاف کریں اور دن کی روشنی میں تصویر بنائیں۔"),
            preventiveMeasuresEnglish = "Hold camera steady 15-20cm from the leaf surface.",
            preventiveMeasuresUrdu = "کیمرے کو ہلائے بغیر پتے سے 15 تا 20 سینٹی میٹر فاصلے پر رکھ کر فوکس کریں۔",
            isUnknownOrRejected = true,
            rejectionReasonUrdu = "تصویر واضح نہیں ہے۔ براہ کرم دوبارہ صاف تصویر کھینچیں۔"
        )
    )
}
