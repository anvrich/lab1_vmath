package org.example

import java.util.Scanner

fun main() {
    val scn = Scanner(System.`in`)
    val handler = InputHandler(scn)
    val data = handler.getInputData()

    if (data != null) {
        if (MatrixUtils.quickSingularityCheck(data.matrix, data.size)) {
            println("\nВНИМАНИЕ: Матрица может быть вырожденной или плохо обусловленной!")
        }

        GaussSolver(data).solve()
    } else {
        println("Данные не получены")
    }
    scn.close()
}