package com.example.electrolicpt.ObjectInfor

class MealNightInfo {
    var id = 0
    var img_food: String? = null
    var name_food: String? = null
    var quantity = 0
    var totalKcal = 0

    constructor()
    constructor(id: Int, img_food: String?, name_food: String?, quantity: Int, kcal_food: Int) {
        this.id = id
        this.img_food = img_food
        this.name_food = name_food
        this.quantity = quantity
        this.totalKcal = kcal_food
    }

    override fun toString(): String {
        return "MealNightModel(id=$id, img_food=$img_food, name_food=$name_food, quantity=$quantity, totalKcal=$totalKcal)"
    }
}


