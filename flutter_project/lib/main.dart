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
      title: 'CropDoc AI - Crop Disease Detection',
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

    // Automated transition to Home Dashboard Screen after 2.5 seconds
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
                  color: Colors.white.withOpacity(0.18),
                  shape: BoxShape.circle,
                  boxShadow: [
                    BoxShadow(
                      color: Colors.black.withOpacity(0.2),
                      blurRadius: 18,
                      offset: const Offset(0, 8),
                    )
                  ],
                ),
                child: const Icon(
                  Icons.eco_rounded,
                  size: 80,
                  color: Colors.white,
                ),
              ),
            ),
            const SizedBox(height: 28),
            Container(
              padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 6),
              decoration: BoxDecoration(
                color: Colors.white.withOpacity(0.15),
                borderRadius: BorderRadius.circular(20),
              ),
              child: const Text(
                'ISLAMIA UNIVERSITY OF BAHAWALPUR (IUB)',
                style: TextStyle(
                  color: Color(0xFFFFD54F),
                  fontSize: 12,
                  fontWeight: FontWeight.bold,
                  letterSpacing: 1.2,
                ),
              ),
            ),
            const SizedBox(height: 12),
            const Text(
              'CropDoc AI',
              style: TextStyle(
                color: Colors.white,
                fontSize: 32,
                fontWeight: FontWeight.bold,
                letterSpacing: 0.5,
              ),
            ),
            const SizedBox(height: 8),
            const Text(
              'AI-Based Crop Disease Detection &\nRemedy Recommendation System',
              textAlign: TextAlign.center,
              style: TextStyle(
                color: Color(0xFFE8F5E9),
                fontSize: 16,
                fontWeight: FontWeight.w400,
                height: 1.3,
              ),
            ),
            const SizedBox(height: 40),
            const CircularProgressIndicator(
              valueColor: AlwaysStoppedAnimation<Color>(Color(0xFFFFD54F)),
              strokeWidth: 3,
            ),
            const SizedBox(height: 24),
            const Text(
              'BS Information Technology Final Year Project',
              style: TextStyle(color: Colors.white70, fontSize: 13),
            ),
            const SizedBox(height: 6),
            const Text(
              'Developer: Saqlain Niazi',
              style: TextStyle(
                color: Color(0xFFFFD54F),
                fontSize: 15,
                fontWeight: FontWeight.bold,
              ),
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

  /// Picks an image via Camera or Gallery and triggers multipart HTTP POST transmission
  Future<void> _pickAndUploadImage(ImageSource source) async {
    try {
      final XFile? pickedFile = await _picker.pickImage(
        source: source,
        maxWidth: 1024,
        maxHeight: 1024,
        imageQuality: 90,
      );

      if (pickedFile == null) return; // User cancelled

      setState(() => _isLoading = true);

      // Perform Multipart HTTP POST request to Flask server
      final File imageFile = File(pickedFile.path);
      final uri = Uri.parse(_backendUrl);

      final request = http.MultipartRequest('POST', uri);
      request.files.add(
        await http.MultipartFile.fromPath('file', imageFile.path),
      );

      final streamedResponse = await request.send().timeout(
        const Duration(seconds: 12),
        onTimeout: () {
          throw Exception('Connection timed out. Ensure Flask backend is running on http://10.0.2.2:5000');
        },
      );

      final response = await http.Response.fromStream(streamedResponse);

      setState(() => _isLoading = false);

      if (response.statusCode == 200) {
        final Map<String, dynamic> resultJson = jsonDecode(response.body);

        if (!mounted) return;
        Navigator.of(context).push(
          MaterialPageRoute(
            builder: (context) => ResultScreen(
              imageFile: imageFile,
              predictionData: resultJson,
            ),
          ),
        );
      } else {
        _showErrorSnackBar('Server error (${response.statusCode}): ${response.body}');
      }
    } catch (e) {
      setState(() => _isLoading = false);
      _showErrorDialog(e.toString());
    }
  }

  void _showErrorSnackBar(String message) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        content: Text(message),
        backgroundColor: Colors.red.shade700,
        behavior: SnackBarBehavior.floating,
      ),
    );
  }

  void _showErrorDialog(String error) {
    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Row(
          children: [
            Icon(Icons.error_outline, color: Colors.red),
            SizedBox(width: 8),
            Text('Analysis Failed'),
          ],
        ),
        content: Text(
          'Could not reach Flask backend ($error).\n\nPlease make sure:\n1. Your Flask server (app.py) is running on 0.0.0.0:5000\n2. For Android Emulator, use http://10.0.2.2:5000/predict\n3. For Physical Phone, use your PC Wi-Fi IP (e.g., http://192.168.1.X:5000/predict).',
          style: const TextStyle(fontSize: 13),
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

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFFF7FBF4),
      appBar: AppBar(
        title: const Row(
          mainAxisSize: MainAxisSize.min,
          children: [
            Icon(Icons.eco, color: Colors.white, size: 24),
            SizedBox(width: 8),
            Text(
              'CropDoc AI',
              style: TextStyle(fontWeight: FontWeight.bold, fontSize: 20),
            ),
          ],
        ),
      ),
      body: Stack(
        children: [
          Column(
            children: [
              Expanded(
                child: SingleChildScrollView(
                  padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.stretch,
                    children: [
                      // Hero Banner Card
                      Container(
                        padding: const EdgeInsets.all(20),
                        decoration: BoxDecoration(
                          gradient: const LinearGradient(
                            colors: [Color(0xFF1B5E20), Color(0xFF2E7D32)],
                            begin: Alignment.topLeft,
                            end: Alignment.bottomRight,
                          ),
                          borderRadius: BorderRadius.circular(20),
                          boxShadow: [
                            BoxShadow(
                              color: const Color(0xFF1B5E20).withOpacity(0.25),
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
                                color: const Color(0xFFFFD54F).withOpacity(0.2),
                                borderRadius: BorderRadius.circular(10),
                              ),
                              child: const Text(
                                'CNN Model Architecture • 224x224 RGB',
                                style: TextStyle(
                                  color: Color(0xFFFFD54F),
                                  fontSize: 11,
                                  fontWeight: FontWeight.bold,
                                ),
                              ),
                            ),
                            const SizedBox(height: 12),
                            const Text(
                              'AI-Based Crop Health Scanner',
                              style: TextStyle(
                                color: Colors.white,
                                fontSize: 20,
                                fontWeight: FontWeight.bold,
                              ),
                            ),
                            const SizedBox(height: 6),
                            const Text(
                              'Diagnose Rice Crop Leaf Diseases instantly with English & localized Urdu remedy recommendations.',
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
                        'Select Crop Leaf Image',
                        style: TextStyle(
                          fontSize: 16,
                          fontWeight: FontWeight.bold,
                          color: Color(0xFF1E6F3D),
                        ),
                      ),
                      const SizedBox(height: 4),
                      const Text(
                        'Capture an infected leaf directly with the camera or choose a photo from the gallery.',
                        style: TextStyle(fontSize: 12, color: Colors.grey),
                      ),

                      const SizedBox(height: 14),

                      // TWO PROMINENT RESPONSIVE BUTTONS
                      Row(
                        children: [
                          // 1. Camera Capture Button
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
                          // 2. Gallery Upload Button
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

                      // Diagnosable Rice Classes Overview
                      const Text(
                        'Diagnosable Crop Classes',
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
                        childAspectRatio: 2.1,
                        children: const [
                          _ClassChip(titleEn: 'Bacterial Blight', titleUr: 'بیکٹیریل بلائٹ', color: Colors.red),
                          _ClassChip(titleEn: 'Blast', titleUr: 'رائس بلاسٹ', color: Colors.purple),
                          _ClassChip(titleEn: 'Brown Spot', titleUr: 'بھورا دھبہ', color: Colors.orange),
                          _ClassChip(titleEn: 'Healthy', titleUr: 'صحت مند فصل', color: Colors.green),
                        ],
                      ),

                      const SizedBox(height: 16),

                      // Flask Endpoint Info Card
                      Container(
                        padding: const EdgeInsets.all(12),
                        decoration: BoxDecoration(
                          color: Colors.white,
                          borderRadius: BorderRadius.circular(12),
                          border: Border.all(color: Colors.green.shade100),
                        ),
                        child: Row(
                          children: [
                            const Icon(Icons.hub_outlined, color: Color(0xFF1E6F3D), size: 20),
                            const SizedBox(width: 8),
                            Expanded(
                              child: Text(
                                'Backend Target: $_backendUrl',
                                style: const TextStyle(fontSize: 11, color: Colors.black87),
                              ),
                            ),
                          ],
                        ),
                      ),
                      const SizedBox(height: 8),
                    ],
                  ),
                ),
              ),

              // MANDATORY VISUAL CONSTRAINT:
              // "On the Home Screen/Front Page, at the very bottom area inside a styled Footer
              // or BottomNavigationBar widget, display clear and elegant text: "Developer: Saqlain Niazi"."
              Container(
                width: double.infinity,
                decoration: BoxDecoration(
                  color: Colors.white,
                  boxShadow: [
                    BoxShadow(
                      color: Colors.black.withOpacity(0.06),
                      blurRadius: 8,
                      offset: const Offset(0, -2),
                    )
                  ],
                ),
                padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Container(
                      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 6),
                      decoration: BoxDecoration(
                        color: const Color(0xFF1E6F3D).withOpacity(0.12),
                        borderRadius: BorderRadius.circular(20),
                        border: Border.all(color: const Color(0xFF1E6F3D).withOpacity(0.3)),
                      ),
                      child: const Row(
                        mainAxisSize: MainAxisSize.min,
                        children: [
                          Icon(Icons.eco, size: 16, color: Color(0xFF1E6F3D)),
                          SizedBox(width: 6),
                          Text(
                            'Developer: Saqlain Niazi',
                            style: TextStyle(
                              fontSize: 14,
                              fontWeight: FontWeight.bold,
                              color: Color(0xFF1E6F3D),
                              letterSpacing: 0.5,
                            ),
                          ),
                        ],
                      ),
                    ),
                    const SizedBox(height: 4),
                    const Text(
                      'BS Information Technology • Islamia University of Bahawalpur (IUB)',
                      style: TextStyle(
                        fontSize: 11,
                        color: Colors.black54,
                        fontWeight: FontWeight.w500,
                      ),
                    ),
                  ],
                ),
              ),
            ],
          ),

          // Loading Progress Overlay
          if (_isLoading)
            Container(
              color: Colors.black45,
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
                        'Analyzing crop leaf sample...\nSending tensor to Flask CNN Model',
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
                  style: TextStyle(fontSize: 11, color: Colors.grey.shade600),
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
// 3. ELEGANT RESULT VIEW SCREEN
// ==========================================
class ResultScreen extends StatelessWidget {
  final File imageFile;
  final Map<String, dynamic> predictionData;

  const ResultScreen({
    super.key,
    required this.imageFile,
    required this.predictionData,
  });

  @override
  Widget build(BuildContext context) {
    final String prediction = predictionData['prediction'] ?? 'Unknown Disease';
    final double confidence = (predictionData['confidence'] ?? 0.0) * 100;
    final Map<String, dynamic> remedies = predictionData['remedies'] ?? {};

    final String adviceEn = remedies['advice_english'] ?? 'No English advice provided.';
    final String adviceUr = remedies['advice_urdu'] ?? 'کوئی اردو ہدایات دستیاب نہیں ہیں۔';
    final String pesticideEn = remedies['pesticide_english'] ?? 'No chemical pesticide required.';
    final String pesticideUr = remedies['pesticide_urdu'] ?? 'کسی کیمیائی سپرے کی ضرورت نہیں ہے۔';

    final bool isHealthy = prediction.toLowerCase() == 'healthy';
    final Color diseaseColor = isHealthy ? Colors.green.shade700 : const Color(0xFFD32F2F);

    return Scaffold(
      backgroundColor: const Color(0xFFF7FBF4),
      appBar: AppBar(
        title: const Text('Diagnosis & Remedy Result'),
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
            // Preview Image Container
            ClipRRect(
              borderRadius: BorderRadius.circular(16),
              child: Image.file(
                imageFile,
                height: 220,
                width: double.infinity,
                fit: BoxFit.cover,
              ),
            ),

            const SizedBox(height: 16),

            // Prediction & Confidence Card
            Container(
              padding: const EdgeInsets.all(18),
              decoration: BoxDecoration(
                color: isHealthy ? Colors.green.shade50 : Colors.red.shade50,
                borderRadius: BorderRadius.circular(16),
                border: Border.all(color: diseaseColor.withOpacity(0.4), width: 1.5),
              ),
              child: Column(
                children: [
                  const Text(
                    'DETECTED CROP STATUS',
                    style: TextStyle(
                      fontSize: 11,
                      fontWeight: FontWeight.bold,
                      letterSpacing: 1.2,
                      color: Colors.black54,
                    ),
                  ),
                  const SizedBox(height: 4),

                  // DYNAMICALLY RENDERED IN BOLD RED (as required)
                  Text(
                    prediction,
                    textAlign: TextAlign.center,
                    style: TextStyle(
                      fontSize: 26,
                      fontWeight: FontWeight.bold,
                      color: diseaseColor, // Rendered in bold red for diseases
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

            const SizedBox(height: 20),

            // DUAL-LANGUAGE REMEDY ACTIONS: STACKED ENGLISH & LOCALIZED URDU
            const Text(
              'Recommended Pesticides & Remedies',
              style: TextStyle(
                fontSize: 16,
                fontWeight: FontWeight.bold,
                color: Color(0xFF1E6F3D),
              ),
            ),
            const SizedBox(height: 10),

            // 1. Recommended Pesticide Card (Dual English & Urdu)
            _buildRemedyCard(
              title: 'Prescribed Chemical Pesticide (زرعی سپرے)',
              icon: Icons.sanitizer_outlined,
              englishContent: pesticideEn,
              urduContent: pesticideUr,
              accentColor: Colors.deepOrange.shade800,
            ),

            const SizedBox(height: 14),

            // 2. Cultural / Agricultural Advice (Dual English & Urdu)
            _buildRemedyCard(
              title: 'Agronomic Guidance & Protection (حفاظتی تدابیر)',
              icon: Icons.shield_outlined,
              englishContent: adviceEn,
              urduContent: adviceUr,
              accentColor: const Color(0xFF1E6F3D),
            ),

            const SizedBox(height: 24),

            // Retake / Scan Another Button
            ElevatedButton.icon(
              onPressed: () => Navigator.of(context).pop(),
              icon: const Icon(Icons.refresh, color: Colors.white),
              label: const Text(
                'Scan Another Crop Leaf (نیا پودا چیک کریں)',
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

  Widget _buildRemedyCard({
    required String title,
    required IconData icon,
    required String englishContent,
    required String urduContent,
    required Color accentColor,
  }) {
    return Container(
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(14),
        border: Border.all(color: Colors.grey.shade200),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.04),
            blurRadius: 6,
            offset: const Offset(0, 2),
          ),
        ],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          // Card Header
          Container(
            padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 10),
            decoration: BoxDecoration(
              color: accentColor.withOpacity(0.08),
              borderRadius: const BorderRadius.only(
                topLeft: Radius.circular(14),
                topRight: Radius.circular(14),
              ),
            ),
            child: Row(
              children: [
                Icon(icon, size: 20, color: accentColor),
                const SizedBox(width: 8),
                Expanded(
                  child: Text(
                    title,
                    style: TextStyle(
                      fontSize: 13,
                      fontWeight: FontWeight.bold,
                      color: accentColor,
                    ),
                  ),
                ),
              ],
            ),
          ),

          // Stacked English and Urdu Text
          Padding(
            padding: const EdgeInsets.all(14),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: [
                // English Advice
                Row(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    const Text('🇬🇧 ', style: TextStyle(fontSize: 14)),
                    Expanded(
                      child: Text(
                        englishContent,
                        style: const TextStyle(
                          fontSize: 13,
                          color: Colors.black87,
                          height: 1.35,
                          fontWeight: FontWeight.w500,
                        ),
                      ),
                    ),
                  ],
                ),
                const Padding(
                  padding: EdgeInsets.symmetric(vertical: 8),
                  child: Divider(height: 1),
                ),
                // Localized Urdu Advice
                Row(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    const Text('🇵🇰 ', style: TextStyle(fontSize: 14)),
                    Expanded(
                      child: Text(
                        urduContent,
                        textAlign: TextAlign.right,
                        style: const TextStyle(
                          fontSize: 14,
                          color: Color(0xFF1B5E20),
                          height: 1.5,
                          fontWeight: FontWeight.w600,
                        ),
                      ),
                    ),
                  ],
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
