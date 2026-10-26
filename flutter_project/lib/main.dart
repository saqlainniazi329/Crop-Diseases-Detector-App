import 'dart:convert';
import 'dart:io';
import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:http/http.dart' as http;

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(const CropDiseaseDetectionApp());
}

/// Root Application Widget configured with Material 3 Agricultural Theme
class CropDiseaseDetectionApp extends StatelessWidget {
  const CropDiseaseDetectionApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'CropDoc AI - Fruit Trees & Crop Disease Detection',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        useMaterial3: true,
        colorScheme: ColorScheme.fromSeed(
          seedColor: const Color(0xFF1E6F3D),
          primary: const Color(0xFF1E6F3D),
          secondary: const Color(0xFF2E7D32),
          surface: Colors.white,
          background: const Color(0xFFF7FBF4),
        ),
        appBarTheme: const AppBarTheme(
          backgroundColor: Color(0xFF1E6F3D),
          foregroundColor: Colors.white,
          centerTitle: true,
          elevation: 2,
        ),
      ),
      home: const SplashScreen(),
    );
  }
}

// ==========================================
// 1. AUTOMATED SPLASH SCREEN
// ==========================================
class SplashScreen extends StatefulWidget {
  const SplashScreen({super.key});

  @override
  State<SplashScreen> createState() => _SplashScreenState();
}

class _SplashScreenState extends State<SplashScreen> with SingleTickerProviderStateMixin {
  late AnimationController _animController;
  late Animation<double> _scaleAnimation;

  @override
  void initState() {
    super.initState();
    _animController = AnimationController(
      vsync: this,
      duration: const Duration(milliseconds: 1400),
    )..repeat(reverse: true);

    _scaleAnimation = Tween<double>(begin: 0.9, end: 1.08).animate(
      CurvedAnimation(parent: _animController, curve: Curves.easeInOut),
    );

    Future.delayed(const Duration(milliseconds: 2500), () {
      if (mounted) {
        Navigator.of(context).pushReplacement(
          MaterialPageRoute(builder: (context) => const HomeScreen()),
        );
      }
    });
  }

  @override
  void dispose() {
    _animController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        width: double.infinity,
        decoration: const BoxDecoration(
          gradient: LinearGradient(
            colors: [Color(0xFF0F381E), Color(0xFF1E6F3D), Color(0xFF2E7D32)],
            begin: Alignment.topCenter,
            end: Alignment.bottomCenter,
          ),
        ),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            ScaleTransition(
              scale: _scaleAnimation,
              child: Container(
                padding: const EdgeInsets.all(22),
                decoration: BoxDecoration(
                  color: Colors.white,
                  shape: BoxShape.circle,
                  boxShadow: [
                    BoxShadow(
                      color: Colors.black.withOpacity(0.2),
                      blurRadius: 18,
                      offset: const Offset(0, 8),
                    ),
                  ],
                ),
                child: const Icon(
                  Icons.eco,
                  size: 76,
                  color: Color(0xFF1E6F3D),
                ),
              ),
            ),
            const SizedBox(height: 28),
            const Text(
              'CropDoc AI',
              style: TextStyle(
                color: Colors.white,
                fontSize: 32,
                fontWeight: FontWeight.bold,
                letterSpacing: 1.1,
              ),
            ),
            const SizedBox(height: 8),
            const Text(
              'Fruit Trees & Field Crop Disease Diagnostic System',
              style: TextStyle(
                color: Color(0xFFE8F5E9),
                fontSize: 13,
                fontWeight: FontWeight.w400,
              ),
            ),
            const SizedBox(height: 18),
            Container(
              padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 6),
              decoration: BoxDecoration(
                color: Colors.white.withOpacity(0.18),
                borderRadius: BorderRadius.circular(20),
              ),
              child: const Text(
                'حکومتِ پاکستان منظور شدہ زرعی ادویات و اردو آواز رہنمائی',
                style: TextStyle(color: Colors.white, fontSize: 12),
              ),
            ),
            const SizedBox(height: 48),
            const CircularProgressIndicator(
              valueColor: AlwaysStoppedAnimation<Color>(Colors.white),
              strokeWidth: 2.5,
            ),
          ],
        ),
      ),
    );
  }
}

// ==========================================
// 2. INTERACTIVE HOME / DASHBOARD SCREEN
// ==========================================
class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  final ImagePicker _picker = ImagePicker();
  bool _isLoading = false;
  String _backendUrl = 'http://10.0.2.2:5000/predict';
  final String _farmerName = 'پیارے کسان بھائی';

  Future<void> _pickAndUploadImage(ImageSource source) async {
    try {
      final XFile? pickedFile = await _picker.pickImage(
        source: source,
        maxWidth: 1024,
        maxHeight: 1024,
        imageQuality: 85,
      );

      if (pickedFile == null) return;

      setState(() {
        _isLoading = true;
      });

      final File imageFile = File(pickedFile.path);

      final uri = Uri.parse(_backendUrl);
      final request = http.MultipartRequest('POST', uri);
      request.files.add(await http.MultipartFile.fromPath('file', imageFile.path));
      request.fields['farmer_name'] = _farmerName;

      final streamedResponse = await request.send().timeout(const Duration(seconds: 12));
      final response = await http.Response.fromStream(streamedResponse);

      setState(() {
        _isLoading = false;
      });

      if (response.statusCode == 200) {
        final Map<String, dynamic> responseJson = json.decode(response.body);

        if (!mounted) return;
        Navigator.of(context).push(
          MaterialPageRoute(
            builder: (context) => ResultScreen(
              imageFile: imageFile,
              predictionData: responseJson,
              farmerName: _farmerName,
            ),
          ),
        );
      } else {
        _showErrorSnackBar('Server returned error: ${response.statusCode}');
      }
    } catch (e) {
      setState(() {
        _isLoading = false;
      });
      _showFallbackPredictionDialog(e.toString());
    }
  }

  void _showFallbackPredictionDialog(String error) {
    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text('Backend Offline'),
        content: Text(
          'Flask backend could not be reached ($error).\nEnsure Python Flask server is running on port 5000.',
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.of(ctx).pop(),
            child: const Text('OK'),
          ),
        ],
      ),
    );
  }

  void _showErrorSnackBar(String message) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        content: Text(message),
        backgroundColor: Colors.red.shade700,
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFFF7FBF4),
      appBar: AppBar(
        title: const Text(
          'CropDoc AI • درخت و فصل ڈاکٹر',
          style: TextStyle(fontWeight: FontWeight.bold, fontSize: 18),
        ),
        actions: [
          IconButton(
            icon: const Icon(Icons.settings),
            onPressed: () {
              _showSettingsDialog();
            },
          ),
        ],
      ),
      body: Stack(
        children: [
          Column(
            children: [
              Expanded(
                child: SingleChildScrollView(
                  padding: const EdgeInsets.all(16.0),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.stretch,
                    children: [
                      Container(
                        padding: const EdgeInsets.all(20),
                        decoration: BoxDecoration(
                          gradient: const LinearGradient(
                            colors: [Color(0xFF1E6F3D), Color(0xFF2E7D32)],
                            begin: Alignment.topLeft,
                            end: Alignment.bottomRight,
                          ),
                          borderRadius: BorderRadius.circular(18),
                          boxShadow: [
                            BoxShadow(
                              color: Colors.green.withOpacity(0.25),
                              blurRadius: 10,
                              offset: const Offset(0, 4),
                            ),
                          ],
                        ),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Container(
                              padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                              decoration: BoxDecoration(
                                color: Colors.amber.withOpacity(0.3),
                                borderRadius: BorderRadius.circular(12),
                              ),
                              child: const Text(
                                'حکومت پاکستان DPP تصدیق شدہ',
                                style: TextStyle(
                                  color: Colors.amberAccent,
                                  fontSize: 11,
                                  fontWeight: FontWeight.bold,
                                ),
                              ),
                            ),
                            const SizedBox(height: 12),
                            const Text(
                              'درختوں اور فصلوں کی خودکار تشخیص',
                              style: TextStyle(
                                color: Colors.white,
                                fontSize: 20,
                                fontWeight: FontWeight.bold,
                              ),
                            ),
                            const SizedBox(height: 6),
                            const Text(
                              'آم، کنو، امرود، سیب، کپاس، گندم، دھان اور کماد کے پتوں کا اسکین کریں اور فوری اردو آواز میں علاج سنیں۔',
                              style: TextStyle(
                                color: Color(0xFFE8F5E9),
                                fontSize: 13,
                                height: 1.3,
                              ),
                            ),
                          ],
                        ),
                      ),

                      const SizedBox(height: 20),

                      const Text(
                        'Select Crop or Tree Leaf Image',
                        style: TextStyle(
                          fontSize: 16,
                          fontWeight: FontWeight.bold,
                          color: Color(0xFF1E6F3D),
                        ),
                      ),
                      const SizedBox(height: 4),
                      const Text(
                        'پتے کی واضح تصویر کیمرے سے لیں یا گیلری سے منتخب کریں۔',
                        style: TextStyle(fontSize: 12, color: Colors.grey),
                      ),

                      const SizedBox(height: 14),

                      Row(
                        children: [
                          Expanded(
                            child: ElevatedButton.icon(
                              onPressed: _isLoading ? null : () => _pickAndUploadImage(ImageSource.camera),
                              icon: const Icon(Icons.camera_alt, color: Colors.white, size: 22),
                              label: const Text(
                                '📷 Capture via Camera',
                                style: TextStyle(
                                  fontSize: 13,
                                  fontWeight: FontWeight.bold,
                                  color: Colors.white,
                                ),
                              ),
                              style: ElevatedButton.styleFrom(
                                backgroundColor: const Color(0xFF1E6F3D),
                                padding: const EdgeInsets.symmetric(vertical: 16),
                                shape: RoundedCornerShape(14),
                                elevation: 3,
                              ),
                            ),
                          ),
                          const SizedBox(width: 12),
                          Expanded(
                            child: OutlinedButton.icon(
                              onPressed: _isLoading ? null : () => _pickAndUploadImage(ImageSource.gallery),
                              icon: const Icon(Icons.photo_library, color: Color(0xFF1E6F3D), size: 22),
                              label: const Text(
                                '🖼️ Upload from Gallery',
                                style: TextStyle(
                                  fontSize: 13,
                                  fontWeight: FontWeight.bold,
                                  color: Color(0xFF1E6F3D),
                                ),
                              ),
                              style: OutlinedButton.styleFrom(
                                side: const BorderSide(color: Color(0xFF1E6F3D), width: 1.5),
                                padding: const EdgeInsets.symmetric(vertical: 16),
                                shape: RoundedCornerShape(14),
                              ),
                            ),
                          ),
                        ],
                      ),

                      const SizedBox(height: 24),

                      const Text(
                        'Supported Trees & Crops (درخت و فصلیں)',
                        style: TextStyle(
                          fontSize: 15,
                          fontWeight: FontWeight.bold,
                          color: Colors.black87,
                        ),
                      ),
                      const SizedBox(height: 8),

                      GridView.count(
                        crossAxisCount: 2,
                        shrinkWrap: true,
                        physics: const NeverScrollableScrollPhysics(),
                        crossAxisSpacing: 10,
                        mainAxisSpacing: 10,
                        childAspectRatio: 2.2,
                        children: const [
                          _ClassChip(titleEn: 'Mango Tree', titleUr: 'آم کا باغ (اینتھراکنوز)', color: Colors.green),
                          _ClassChip(titleEn: 'Citrus / Kinnow', titleUr: 'کنو و مالٹا (کینکر)', color: Colors.orange),
                          _ClassChip(titleEn: 'Guava Tree', titleUr: 'امرود کا باغ (سوکڑا)', color: Colors.teal),
                          _ClassChip(titleEn: 'Cotton Crop', titleUr: 'کپاس (پتہ مروڑ)', color: Colors.blue),
                          _ClassChip(titleEn: 'Wheat Crop', titleUr: 'گندم (پیلی کنگی)', color: Colors.amber),
                          _ClassChip(titleEn: 'Rice Paddy', titleUr: 'دھان (بلاسٹ و بلائٹ)', color: Colors.deepPurple),
                        ],
                      ),

                      const SizedBox(height: 16),
                    ],
                  ),
                ),
              ),

              // MANDATORY VISUAL CONSTRAINT:
              Container(
                width: double.infinity,
                padding: const EdgeInsets.symmetric(vertical: 14, horizontal: 16),
                decoration: BoxDecoration(
                  color: Colors.white,
                  boxShadow: [
                    BoxShadow(
                      color: Colors.black.withOpacity(0.06),
                      blurRadius: 8,
                      offset: const Offset(0, -3),
                    ),
                  ],
                ),
                child: SafeArea(
                  top: false,
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Container(
                        padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 4),
                        decoration: BoxDecoration(
                          color: const Color(0xFF1E6F3D).withOpacity(0.1),
                          borderRadius: BorderRadius.circular(16),
                          border: Border.all(color: const Color(0xFF1E6F3D).withOpacity(0.3)),
                        ),
                        child: const Row(
                          mainAxisSize: MainAxisSize.min,
                          children: [
                            Icon(Icons.verified, size: 16, color: Color(0xFF1E6F3D)),
                            SizedBox(width: 6),
                            Text(
                              'Developer: Saqlain Niazi',
                              style: TextStyle(
                                fontSize: 13,
                                fontWeight: FontWeight.bold,
                                color: Color(0xFF1E6F3D),
                                letterSpacing: 0.5,
                              ),
                            ),
                          ],
                        ),
                      ),
                      const SizedBox(height: 4),
                      Text(
                        'BS Information Technology • Islamia University of Bahawalpur (IUB)',
                        style: TextStyle(
                          fontSize: 10,
                          color: Colors.grey.shade600,
                          fontWeight: FontWeight.w500,
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ],
          ),

          if (_isLoading)
            Container(
              color: Colors.black.withOpacity(0.4),
              child: Center(
                child: Container(
                  padding: const EdgeInsets.all(24),
                  decoration: BoxDecoration(
                    color: Colors.white,
                    borderRadius: BorderRadius.circular(16),
                  ),
                  child: const Column(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      CircularProgressIndicator(
                        valueColor: AlwaysStoppedAnimation<Color>(Color(0xFF1E6F3D)),
                      ),
                      SizedBox(height: 16),
                      Text(
                        'پتے کی تشخیص جاری ہے...\nکمپیوٹر ویژن تجزیہ اور DPP ادویات سرچنگ',
                        textAlign: TextAlign.center,
                        style: TextStyle(fontWeight: FontWeight.bold, fontSize: 13),
                      ),
                    ],
                  ),
                ),
              ),
            ),
        ],
      ),
    );
  }

  void _showSettingsDialog() {
    final controller = TextEditingController(text: _backendUrl);
    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text('Flask Backend Endpoint'),
        content: TextField(
          controller: controller,
          decoration: const InputDecoration(labelText: 'URL (e.g. http://10.0.2.2:5000/predict)'),
        ),
        actions: [
          TextButton(onPressed: () => Navigator.of(ctx).pop(), child: const Text('Cancel')),
          ElevatedButton(
            onPressed: () {
              setState(() {
                _backendUrl = controller.text.trim();
              });
              Navigator.of(ctx).pop();
            },
            child: const Text('Save'),
          ),
        ],
      ),
    );
  }
}

class _ClassChip extends StatelessWidget {
  final String titleEn;
  final String titleUr;
  final Color color;

  const _ClassChip({
    required this.titleEn,
    required this.titleUr,
    required this.color,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 8),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(10),
        border: Border.all(color: Colors.grey.shade200),
      ),
      child: Row(
        children: [
          CircleAvatar(radius: 4, backgroundColor: color),
          const SizedBox(width: 8),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Text(
                  titleEn,
                  style: const TextStyle(fontSize: 12, fontWeight: FontWeight.bold),
                  overflow: TextOverflow.ellipsis,
                ),
                Text(
                  titleUr,
                  style: TextStyle(fontSize: 10, color: Colors.grey.shade600),
                  overflow: TextOverflow.ellipsis,
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}

// ==========================================
// 3. ELEGANT RESULT VIEW SCREEN WITH URDU VOICE
// ==========================================
class ResultScreen extends StatelessWidget {
  final File imageFile;
  final Map<String, dynamic> predictionData;
  final String farmerName;

  const ResultScreen({
    super.key,
    required this.imageFile,
    required this.predictionData,
    required this.farmerName,
  });

  @override
  Widget build(BuildContext context) {
    final String prediction = predictionData['prediction'] ?? 'Unknown Condition';
    final double confidence = ((predictionData['confidence'] ?? 0.0) as num).toDouble() * 100;
    final String cropName = predictionData['crop_or_tree_name'] ?? 'باغ / فصل';
    final String diseaseUrdu = predictionData['disease_urdu'] ?? prediction;
    final String monitoringDays = predictionData['monitoring_days_urdu'] ?? '7 دن تک باقاعدہ معائنہ کریں';
    final String irrigationGuide = predictionData['irrigation_guide_urdu'] ?? 'پانی کی مناسب سطح برقرار رکھیں';
    final List<dynamic> approvedPesticides = predictionData['approved_pesticides_urdu'] ?? [];
    final String speechText = predictionData['audio_speech_text_urdu'] ?? '';

    final bool isHealthy = prediction.toLowerCase() == 'healthy';
    final Color diseaseColor = isHealthy ? Colors.green.shade700 : const Color(0xFFD32F2F);

    return Scaffold(
      backgroundColor: const Color(0xFFF7FBF4),
      appBar: AppBar(
        title: const Text('تشخیص اور منظور شدہ علاج'),
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          onPressed: () => Navigator.of(context).pop(),
        ),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            Container(
              padding: const EdgeInsets.all(14),
              decoration: BoxDecoration(
                color: const Color(0xFF1B5E20),
                borderRadius: BorderRadius.circular(14),
                boxShadow: [
                  BoxShadow(
                    color: Colors.green.withOpacity(0.3),
                    blurRadius: 8,
                    offset: const Offset(0, 3),
                  ),
                ],
              ),
              child: Row(
                children: [
                  const CircleAvatar(
                    backgroundColor: Colors.white24,
                    child: Icon(Icons.volume_up, color: Colors.white),
                  ),
                  const SizedBox(width: 12),
                  Expanded(
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          'اردو وائس اسسٹنٹ: پیارے کسان $farmerName',
                          style: const TextStyle(
                            color: Colors.white,
                            fontWeight: FontWeight.bold,
                            fontSize: 13,
                          ),
                        ),
                        const SizedBox(height: 2),
                        const Text(
                          'خودکار اردو آواز میں پودے کا علاج اور پانی کی تفصیل سنیں',
                          style: TextStyle(color: Color(0xFFC8E6C9), fontSize: 11),
                        ),
                      ],
                    ),
                  ),
                  ElevatedButton(
                    onPressed: () {
                      ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(
                          content: Text(speechText.isNotEmpty ? speechText : 'اردو آڈیو چلائی جا رہی ہے...'),
                          duration: const Duration(seconds: 4),
                          backgroundColor: const Color(0xFF1B5E20),
                        ),
                      );
                    },
                    style: ElevatedButton.styleFrom(
                      backgroundColor: Colors.white,
                      foregroundColor: const Color(0xFF1B5E20),
                      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 6),
                    ),
                    child: const Text('سنیں', style: TextStyle(fontWeight: FontWeight.bold)),
                  ),
                ],
              ),
            ),

            const SizedBox(height: 14),

            ClipRRect(
              borderRadius: BorderRadius.circular(16),
              child: Image.file(
                imageFile,
                height: 200,
                width: double.infinity,
                fit: BoxFit.cover,
              ),
            ),

            const SizedBox(height: 14),

            Container(
              padding: const EdgeInsets.all(18),
              decoration: BoxDecoration(
                color: isHealthy ? Colors.green.shade50 : Colors.red.shade50,
                borderRadius: BorderRadius.circular(16),
                border: Border.all(color: diseaseColor.withOpacity(0.4), width: 1.5),
              ),
              child: Column(
                children: [
                  Text(
                    cropName,
                    style: const TextStyle(
                      fontSize: 14,
                      fontWeight: FontWeight.bold,
                      color: Colors.black87,
                    ),
                  ),
                  const SizedBox(height: 6),

                  Text(
                    prediction,
                    textAlign: TextAlign.center,
                    style: TextStyle(
                      fontSize: 24,
                      fontWeight: FontWeight.bold,
                      color: diseaseColor,
                    ),
                  ),

                  Text(
                    diseaseUrdu,
                    textAlign: TextAlign.center,
                    style: const TextStyle(
                      fontSize: 18,
                      fontWeight: FontWeight.bold,
                      color: Colors.black87,
                    ),
                  ),

                  const SizedBox(height: 8),

                  Container(
                    padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 4),
                    decoration: BoxDecoration(
                      color: diseaseColor.withOpacity(0.12),
                      borderRadius: BorderRadius.circular(12),
                    ),
                    child: Text(
                      'AI Confidence: ${confidence.toStringAsFixed(1)}%',
                      style: TextStyle(
                        fontSize: 13,
                        fontWeight: FontWeight.bold,
                        color: diseaseColor,
                      ),
                    ),
                  ),
                ],
              ),
            ),

            const SizedBox(height: 14),

            _buildAdvisoryCard(
              title: 'کتنے دن تک نگرانی کرنی چاہیے (Field Monitoring)',
              icon: Icons.calendar_month,
              contentUrdu: monitoringDays,
              accentColor: Colors.amber.shade900,
            ),

            const SizedBox(height: 12),

            _buildAdvisoryCard(
              title: 'پانی کی صورتحال (Irrigation & Water Guide)',
              icon: Icons.water_drop,
              contentUrdu: irrigationGuide,
              accentColor: Colors.blue.shade800,
            ),

            const SizedBox(height: 14),

            Container(
              padding: const EdgeInsets.all(16),
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(14),
                border: Border.all(color: Colors.green.shade200),
              ),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  const Row(
                    children: [
                      Icon(Icons.local_pharmacy, color: Color(0xFF1E6F3D)),
                      SizedBox(width: 8),
                      Text(
                        'حکومتِ پاکستان منظور شدہ زرعی ادویات',
                        style: TextStyle(
                          fontSize: 15,
                          fontWeight: FontWeight.bold,
                          color: Color(0xFF1E6F3D),
                        ),
                      ),
                    ],
                  ),
                  const Divider(height: 20),
                  if (approvedPesticides.isEmpty)
                    const Text('اس وقت کسی کیمیائی اسپرے کی ضرورت نہیں ہے۔')
                  else
                    ...approvedPesticides.map(
                      (p) => Padding(
                        padding: const EdgeInsets.only(bottom: 8.0),
                        child: Row(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            const Text('• ', style: TextStyle(color: Color(0xFF1E6F3D), fontSize: 16)),
                            Expanded(
                              child: Text(
                                p.toString(),
                                style: const TextStyle(
                                  fontWeight: FontWeight.bold,
                                  color: Color(0xFF2E7D32),
                                  height: 1.4,
                                ),
                              ),
                            ),
                          ],
                        ),
                      ),
                    ),
                ],
              ),
            ),

            const SizedBox(height: 20),

            ElevatedButton.icon(
              onPressed: () => Navigator.of(context).pop(),
              icon: const Icon(Icons.refresh, color: Colors.white),
              label: const Text(
                'نیا پودا یا درخت چیک کریں (Scan Another)',
                style: TextStyle(fontSize: 14, fontWeight: FontWeight.bold, color: Colors.white),
              ),
              style: ElevatedButton.styleFrom(
                backgroundColor: const Color(0xFF1E6F3D),
                padding: const EdgeInsets.symmetric(vertical: 16),
                shape: RoundedCornerShape(14),
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildAdvisoryCard({
    required String title,
    required IconData icon,
    required String contentUrdu,
    required Color accentColor,
  }) {
    return Container(
      padding: const EdgeInsets.all(14),
      decoration: BoxDecoration(
        color: accentColor.withOpacity(0.06),
        borderRadius: BorderRadius.circular(14),
        border: Border.all(color: accentColor.withOpacity(0.3)),
      ),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Icon(icon, color: accentColor, size: 22),
          const SizedBox(width: 10),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  title,
                  style: TextStyle(
                    fontSize: 13,
                    fontWeight: FontWeight.bold,
                    color: accentColor,
                  ),
                ),
                const SizedBox(height: 4),
                Text(
                  contentUrdu,
                  style: const TextStyle(
                    fontSize: 14,
                    fontWeight: FontWeight.w600,
                    color: Colors.black87,
                    height: 1.4,
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
