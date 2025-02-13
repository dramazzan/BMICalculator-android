package com.example.bmicalculator

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.bmicalculator.R
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.pow

class MainActivity : AppCompatActivity() {
    private lateinit var weightEditText: EditText
    private lateinit var heightEditText: EditText
    private lateinit var resultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weightEditText = findViewById(R.id.weightEditText)
        heightEditText = findViewById(R.id.heightEditText)
        resultTextView = findViewById(R.id.resultTextView)
        val calculateButton: Button = findViewById(R.id.calculateButton)
        val clearButton: Button = findViewById(R.id.clearButton)

        calculateButton.setOnClickListener { calculateBMI() }
        clearButton.setOnClickListener { clearFields() }
    }

    private fun calculateBMI() {
        // Get input values
        val weightStr = weightEditText.text.toString()
        val heightStr = heightEditText.text.toString()

        // Validate inputs
        if (weightStr.isEmpty()) {
            weightEditText.error = "Введите вес"
            return
        }
        if (heightStr.isEmpty()) {
            heightEditText.error = "Введите рост"
            return
        }

        try {
            // Parse values
            val weight = weightStr.toDouble()
            val height = heightStr.toDouble()

            // Validate positive values
            if (weight <= 0) {
                weightEditText.error = "Weight must be positive"
                return
            }
            if (height <= 0) {
                heightEditText.error = "Height must be positive"
                return
            }

            // Calculate BMI
            val bmi = weight / ((height / 100).pow(2))
            val bmiRounded = BigDecimal(bmi).setScale(2, RoundingMode.HALF_UP).toDouble()

            // Determine category and color
            val (category, colorRes) = when {
                bmiRounded < 18.5 -> Pair("Недостаточный вес\n", R.color.underweight)
                bmiRounded <= 24.9 -> Pair("Нормальный вес\n", R.color.normal)
                bmiRounded <= 29.9 -> Pair("Избыточный вес\n", R.color.overweight)
                else -> Pair("Ожирение\n", R.color.obese)
            }

            // Display result
            resultTextView.text = "BMI: $bmiRounded - $category"
            resultTextView.setTextColor(ContextCompat.getColor(this, colorRes))

        } catch (e: NumberFormatException) {
            // Handle invalid number format
            weightEditText.error = "Invalid number"
            heightEditText.error = "Invalid number"
        }
    }

    private fun clearFields() {
        weightEditText.text.clear()
        heightEditText.text.clear()
        resultTextView.text = ""
        weightEditText.error = null
        heightEditText.error = null
    }
}