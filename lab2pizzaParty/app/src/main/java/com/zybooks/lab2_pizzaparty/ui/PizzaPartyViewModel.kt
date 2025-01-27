package com.zybooks.lab2_pizzaparty.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.zybooks.lab2_pizzaparty.HungerLevel
import com.zybooks.lab2_pizzaparty.PizzaCalculator

class PizzaPartyViewModel : ViewModel() {
    var numPeopleInput by mutableStateOf("")

    var hungerLevel by mutableStateOf(HungerLevel.MEDIUM)

    var totalPizzas by mutableIntStateOf(0)
        private set

    fun calculateNumPizzas() {
        val calc = PizzaCalculator(numPeopleInput.toIntOrNull() ?: 0, hungerLevel)
        totalPizzas = calc.totalPizzas
    }
}

