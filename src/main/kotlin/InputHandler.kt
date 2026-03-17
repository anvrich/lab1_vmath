package org.example

import org.example.MatrixUtils.MAX_SIZE
import java.io.File
import java.io.FileNotFoundException
import java.util.InputMismatchException
import java.util.Scanner
import kotlin.random.Random


class InputHandler(private val sc: Scanner) {
    fun getInputData(): InputData? {
        printWelcomeMsg()
        try {
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

                    3 -> {
                        val result = generateRandom()
                        if (result != null) return result
                    }
                }
            }
        } catch (e: NoSuchElementException) {
            println("\nЗавершение программы.")
            return null
        }
    }

    private fun generateRandom(): InputData? {
        val size = getMaxSize()
        val random = Random.Default
        val matrix = Array(size) { DoubleArray(size) { random.nextDouble() * 200 - 100 } }
        val vector = DoubleArray(size) { random.nextDouble() * 200 - 100 }
        println("Сгенерирована матрица ${size}x${size} со случайными числами от -100 до 100")
        return InputData(size, matrix, vector)
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
                        matrix[i][j] = parseDouble(flScan.next())
                    }
                }
                for (i in 0 until size) {
                    vector[i] = parseDouble(flScan.next())
                }
                flScan.close()
                return InputData(size, matrix, vector)
            } catch (e: FileNotFoundException) {
                println("Файл не найден: $path")
                println("Попробуйте ещё раз")
            } catch (e: NoSuchElementException) {
                println("Файл пуст или данные в нём неполные")
                println("Попробуйте ещё раз")
            } catch (e: InputMismatchException) {
                println("Ошибка формата данных в файле:")
//                println("   ${e.message}")
                println("Попробуйте ещё раз")
            } catch (e: NumberFormatException) {
                println("Ошибка формата данных в файле: ${e.message}")
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
                return parseDouble(sc.next())
            } catch (e: NumberFormatException) {
                println("Ошибка формата данных в файле: ${e.message}")
                println("Попробуйте ещё раз")
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
                print("\nВаш выбор (1, 2 ,3 или 0 для выхода): ")
                val choice = sc.nextInt()
                if (choice in 0..3) {
                    return choice
                }
                println("Ошибка: введите 0, 1, 2 или 3")
            } catch (e: InputMismatchException) {
                println("Ошибка: введите число")
                sc.next()
            } catch (e: NoSuchElementException) {
                println("\nЗавершение программы.")
                return 0
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
        println("  3 - Сгенерировать случайную матрицу")
        println("  0 - Выход")
    }

    private fun parseDouble(input: String): Double {
        return input.replace(',', '.').toDouble()
    }
}