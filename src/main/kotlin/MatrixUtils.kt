package org.example

import java.text.DecimalFormat
import kotlin.math.abs

object MatrixUtils {
    const val EPSILON = 1e-15
    private val DEFAULT_FORMATTER = DecimalFormat("#0.0000")
    const val MAX_SIZE = 20


    fun format(value: Double): String {
        return if (abs(value) < 1e-4 && value != 0.0) {
            "%.2e".format(value)
        } else {
            DEFAULT_FORMATTER.format(value)
        }
    }

    fun quickSingularityCheck(matrix: Array<DoubleArray>, size: Int): Boolean {
        for (i in 0 until size) {
            var zeroRow = true
            var zeroCol = true
            for (j in 0 until size) {
                if (abs(matrix[i][j]) >= EPSILON) zeroRow = false
                if (abs(matrix[j][i]) >= EPSILON) zeroCol = false
            }
            if (zeroRow || zeroCol) return true
        }
        return false
    }

    fun printMatrix(title: String, matrix: Array<DoubleArray>, size: Int) {
        println(title)
        for (i in 0 until size) {
            print("| ")
            for (j in 0 until size) {
                print(String.format("%8s", format(matrix[i][j])))
            }
            print(" | ")
            print(String.format("%8s", format(matrix[i][size])))
            println(" |")
        }
    }
}