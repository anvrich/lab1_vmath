package org.example

import org.example.MatrixUtils.EPSILON
import org.example.MatrixUtils.FORMATTER
import org.apache.commons.math3.linear.*
import kotlin.math.abs
import kotlin.math.max

class GaussSolver(data: InputData) {
    private val matrixA = data.matrix
    private val vectorB = data.vector
    private val size = data.size
    private val solution = DoubleArray(size)
    private var determinant = 1.0
    private val residual = DoubleArray(size)
    private val augmentedMatrix = Array(size) { DoubleArray(size + 1) }


    private fun createAugmentedMatrix() {
        for (i in 0 until size) {
            System.arraycopy(matrixA[i], 0, augmentedMatrix[i], 0, size)
            augmentedMatrix[i][size] = vectorB[i]
        }
    }

    private fun zeroBelow(col: Int) {
        var mainEl = augmentedMatrix[col][col]
        for (row in col + 1 until size) {
            var koef = augmentedMatrix[row][col] / mainEl
            for (j in col..size) {
                augmentedMatrix[row][j] -= koef * augmentedMatrix[col][j]
            }
        }
    }

    private fun doIt(): Boolean {
        for (i in 0 until size) {
            if (abs(augmentedMatrix[i][i]) < EPSILON) {
                println("Нулевой элемент на диагонали в столбце ${i + 1}")
                return false
            }
            zeroBelow(i)
        }
        return true
    }

    private fun solveBackwards() {
        for (i in size - 1 downTo 0) {
            var sum = 0.0
            for (j in i + 1 until size) {
                sum += augmentedMatrix[i][j] * solution[j]
            }
            solution[i] = (augmentedMatrix[i][size] - sum) / augmentedMatrix[i][i]
        }
    }

    private fun findDet() {
        for (i in 0 until size) {
            determinant *= augmentedMatrix[i][i]
        }
    }

    private fun findResidual() {
        for (i in 0 until size) {
            var sum = 0.0
            for (j in 0 until size) {
                sum += matrixA[i][j] * solution[j]
            }
            residual[i] = sum - vectorB[i]
        }
    }


    private fun printSolution() {
        println("\n РЕШЕНИЕ:")
        for (i in 0 until size) {
            println("  x[${i + 1}] = ${FORMATTER.format(solution[i])}")
        }
    }

    private fun printResidual() {
        println("\n ВЕКТОР НЕВЯЗКИ (r = A·x - b):")
        var maxResidual = 0.0
        for (i in 0 until size) {
            println("  r[${i + 1}] = ${FORMATTER.format(residual[i])}")
            maxResidual = Math.max(maxResidual, Math.abs(residual[i]))
        }
        println("  Максимальная невязка: ${FORMATTER.format(maxResidual)}")
    }


    fun solve() {
        createAugmentedMatrix()
        MatrixUtils.printMatrix("Расширенная матрица [A|B]:", augmentedMatrix, size)

        if (!doIt()) {
            println("Система не имеет единственного решения")
            return
        }

        MatrixUtils.printMatrix("\nТреугольная матрица после прямого хода:", augmentedMatrix, size)

        solveBackwards()
        printSolution()

        findDet()
        println("\n Определитель матрицы A: ${FORMATTER.format(determinant)}")

        findResidual()
        printResidual()


        compareWithLibrary()
    }

    private fun compareWithLibrary() {
        try {
            val mA = Array2DRowRealMatrix(matrixA)
            val vB = ArrayRealVector(vectorB)

            val lu = LUDecomposition(mA)
            val xLib = lu.solver.solve(vB)
            val detLib = lu.determinant

            println("\n РЕШЕНИЕ БИБЛИОТЕЧНЫМ МЕТОДОМ (Apache Commons Math):")
            for (i in 0 until size) {
                println("  x${i + 1}_lib = ${FORMATTER.format(xLib.getEntry(i))}")
            }
            println("  det_lib(A) = ${FORMATTER.format(detLib)}")

            println("\n СРАВНЕНИЕ РЕЗУЛЬТАТОВ:")
            var maxDiff = 0.0
            for (i in 0 until size) {
                val diff = abs(solution[i] - xLib.getEntry(i))
                maxDiff = max(maxDiff, diff)
                println("  |x${i + 1} - x${i + 1}_lib| = ${"%.2e".format(diff)}")
            }
            val detDiff = abs(determinant - detLib)
            println("  |det - det_lib| = ${"%.2e".format(detDiff)}")

            if (maxDiff < 1e-10 && detDiff < 1e-10) {
                println(" Результаты совпадают с высокой точностью")
            }
        } catch (e: Exception) {
            println(" Ошибка при вычислении библиотечным методом: ${e.message}")
        }
    }
}