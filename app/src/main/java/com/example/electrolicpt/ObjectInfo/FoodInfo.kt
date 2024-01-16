package com.example.electrolicpt.ObjectInfor

class FoodInfo {
    var id = 0
    @JvmField
    var name_food: String? = null
    @JvmField
    var img_food: String? = null
    @JvmField
    var kcal_food = 0
    var effect_food: String? = null
    var effect_e: String? = null
    var calories = 0
    var protein = 0f
    var carb = 0f
    var sugars = 0f
    var fat = 0f

    constructor()
    constructor(
        id: Int,
        name_food: String?,
        img_food: String?,
        kcal_food: Int,
        effect_food: String?,
        effect_e: String?,
        calories: Int,
        protein: Float,
        carb: Float,
        sugars: Float,
        fat: Float,

    ) {
        this.id = id
        this.name_food = name_food
        this.img_food = img_food
        this.kcal_food = kcal_food
        this.effect_food = effect_food
        this.effect_e = effect_e
        this.calories = calories
        this.protein = protein
        this.carb = carb
        this.sugars = sugars
        this.fat = fat

    }
}