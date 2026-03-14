package org.example

import org.example.MatrixUtils.MAX_SIZE
import java.io.File
import java.io.FileNotFoundException
import java.util.InputMismatchException
import java.util.Scanner


class InputHandler(private val sc: Scanner) {
    fun getInputData(): InputData? {
        printWelcomeMsg()

        while (true) {
            val choice = getChoice()
            when (choice) {
                0 -> {
                    println("Завершение программы.")
                    return null
                }
                1 -> {
                    val result = readFromKeyboard()
                    if (result != null) return result
                }
                2 -> {
                    val result = readFromFile()
                    if (result != null) return result
                    print("Возврат к выбору:")
                }
            }
        }
    }

    private fun readFromFile(): InputData? {
        while (true) {
            print("Введите путь к файлу (или 'exit' для выхода): ")
            val path = sc.next()
            if (path == "exit") return null

            try {
                val flScan = Scanner(File(path))
                val size = flScan.nextInt()
                if (size !in 1..MAX_SIZE) {
                    println("Размер должен быть от 1 до $MAX_SIZE")
                    flScan.close()
                    continue
                }
                val matrix = Array(size) { DoubleArray(size) }
                val vector = DoubleArray(size)

                for (i in 0 until size) {
                    for (j in 0 until size) {
                        matrix[i][j] = flScan.nextDouble()
                    }
                }
                for (i in 0 until size) {
                    vector[i] = flScan.nextDouble()
                }
                flScan.close()
                return InputData(size, matrix, vector)
            } catch (e: FileNotFoundException) {
                println("Файл не найден: $path")
                println("Попробуйте ещё раз")
            } catch (e: InputMismatchException) {
                println("Ошибка формата данных в файле:")
                println("   ${e.message}")
                println("Попробуйте ещё раз")
            }
        }
    }

    private fun readFromKeyboard(): InputData? {
        val size = getMaxSize()
        val matrix = Array(size) { DoubleArray(size) }
        val vector = DoubleArray(size)
        for (i in 0 until size) {
            print("Строка ${i + 1} (введите $size чисел через пробел): ")
            for (j in 0 until size) {
                matrix[i][j] = readDoubleSafely()
            }
        }
        for (i in 0 until size) {
            print("B[${i + 1}]: ")
            vector[i] = readDoubleSafely()
        }
        return InputData(size, matrix, vector)
    }

    private fun readDoubleSafely(): Double {
        while (true) {
            try {
                return sc.nextDouble()
            } catch (e: InputMismatchException) {
                print("Ошибка: введите число. Повторите: ")
                sc.next()
            }
        }
    }

    private fun getMaxSize(): Int {
        while (true) {
            try {
                print("Введите размерность матрицы (1-$MAX_SIZE): ")
                val n = sc.nextInt()
                if (n in 1..MAX_SIZE) {
                    return n
                }
                println("Ошибка: размер должен быть от 1 до $MAX_SIZE")
            } catch (e: InputMismatchException) {
                println("Ошибка: введите целое число")
                sc.next()
            }
        }
    }

    private fun getChoice(): Int {
        while (true) {
            try {
                print("\nВаш выбор (1, 2 или 0 для выхода): ")
                val choice = sc.nextInt()
                if (choice in 0..2) {
                    return choice
                }
                println("Ошибка: введите 0, 1 или 2")
            } catch (e: InputMismatchException) {
                println("Ошибка: введите число")
                sc.next()
            }
        }
    }

    private fun printWelcomeMsg() {
        println("=".repeat(70))
        println("   РЕШЕНИЕ СИСТЕМ ЛИНЕЙНЫХ УРАВНЕНИЙ МЕТОДОМ ГАУССА")
        println("=".repeat(70))
        println("Выберите способ ввода данных:")
        println("  1 - Ввести матрицу с консоли")
        println("  2 - Ввести матрицу с файла")
        println("  0 - Выход")
    }
}