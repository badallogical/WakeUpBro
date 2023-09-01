package com.example.wakeupbro.math

import android.util.Log
import kotlin.random.Random

/*
    This provide the random math programs of adding.
 */
class MathApi(private val level : Int = 0 ) {

    private var numberOfProblems = 3
    private var maxOperandValue = 20
    private var problemsList : MutableList<String> = mutableListOf()
    private var index = 0
    var over = false


    init {
        generator()
    }


    fun getQuestions():Int {
        return problemsList.size
    }

    fun hasNext(): Boolean {
        return index < problemsList.size
    }

    fun next(): String {
        if (!hasNext()) {
            throw NoSuchElementException()
        }
        return problemsList[index++]
    }

    fun generator(){
        problemsList = mutableListOf()

        when(level){
            1 -> {
                numberOfProblems = 5
                maxOperandValue = 50
            }
            2 -> {
                numberOfProblems = 7
                maxOperandValue = 80
            }
            else -> {
                numberOfProblems = 3
                maxOperandValue = 20
            }
        }

        for (i in 1..numberOfProblems) {
            val operand1 = Random.nextInt(maxOperandValue + 2)
            val operand2 = Random.nextInt(maxOperandValue + 2)
            val operand3 = Random.nextInt(maxOperandValue + 2)

            val result = operand1 + operand2 + operand3

            val problem = "$operand1 + $operand2 + $operand3"

            Log.d("MathAPI", problem )

            problemsList.add("$problem:$result")
        }
    }
}