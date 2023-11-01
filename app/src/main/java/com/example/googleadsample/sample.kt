package com.example.googleadsample

/**
 * You can edit, run, and share this code.
 * play.kotlinlang.org
 */
fun main() {
    val number = arrayOf(1, 3, 2, 5, 4, 7, 6, 8, 9, 10)
    var dumNumber = mutableListOf<Int>()
    var dumNumber1 = mutableListOf<Int>()
    var result = ""
    for (i in number.distinct()) {
        result += i
        println(i)

    }
    println("number-> ${result}")

    var result1 = ""
    var result2 = ""
    for (i in number) {
        result1 = i.toString()
        for (j in 1..i) {
            result2 += j
            dumNumber.add(j)
        }/*  println(result2.toSet().joinToString())
          println(result1.toSet().joinToString())*/
        result2 += result1
    }

    for (i in result2) {
        println("- " + i)
    }
    println(result2.toSet().joinToString())
    println(result1.toSet().joinToString())

    println("------")
    println("dumNumber->" + dumNumber.joinToString())
    var numdum = ""
    for (i in dumNumber) {
        for (j in 0..i) {
            if (i == j) {
                dumNumber1.add(j)
                dumNumber1.distinct()
            }
        }
        numdum = dumNumber1.distinct().joinToString()
    }
    println(numdum)
    println("dumnumber1->" + dumNumber1.distinct().joinToString())


}