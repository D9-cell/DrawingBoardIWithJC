package com.example.drawingboardiwithjc

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel

class CharacterViewModel(application: Application) : AndroidViewModel(application) {

    private val _predictionResult = mutableStateOf<String?>(null)
    val predictionResult: State<String?> = _predictionResult

    private val classifier = TFLiteClassifier(application.applicationContext)

    fun predictCharacter(features: List<Double>) {
        try {
            val predictedIndex = classifier.predict(features)
            val labelClasses = listOf(
                "A/अ/অ", "AA/आ/আ", "ADA/ढ़/ঢ", "AN/ं/ং",
                "BA/ब/ব", "BHA/भ/ভ", "BI/ः/ঃ",
                "C/च/চ", "CH/छ/ছ", "CN/ँ/ঁ",
                "DA/द/দ", "DDA/ड/ড", "DDH/ढ/ঢ", "DHA/ध/ধ", "DRA/ड़/ড়",
                "E/ए/এ", "EN/ञ/ঞ",
                "G/ग/গ", "GH/घ/ঘ",
                "HA/ह/হ",
                "I/इ/ই", "II/ई/ঈ",
                "JA/ज/জ", "JH/झ/ঝ",
                "K/क/ক", "KH/ख/খ", "KT/त্/ৎ",
                "LA/ल/ল",
                "MA/म/ম", "MN/ण/ণ", "MSA/ष/ষ",
                "NA/न/ন",
                "O/ओ/ও", "OI/ऐ/ঐ", "OU/औ/ঔ",
                "PA/प/প", "PHA/फ/ফ",
                "RA/र/র", "RI/ऋ/ঋ",
                "S/स/স", "SA/श/শ",
                "T/ट/ট", "TA/त/ত", "THA/थ/থ", "TTA/ठ/ঠ",
                "U/उ/উ", "UN/ङ/ঙ", "UU/ऊ/ঊ",
                "Y/य़/য়", "YA/य/য"
            )
            _predictionResult.value = labelClasses[predictedIndex]
        } catch (e: Exception) {
            _predictionResult.value = null
        }
    }

    fun clearPrediction() {
        _predictionResult.value = null
    }
}
