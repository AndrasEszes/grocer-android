package io.grocer.app.data

object SampleCatalog {
    val products = listOf(
        Product("apple-gala", "Gala apples", Category.Fruit, 299, "1 kg", "🍎", "Crisp and sweet, from Hungarian orchards."),
        Product("banana", "Bananas", Category.Fruit, 189, "1 kg", "🍌", "Fairtrade bananas, ripe in two days."),
        Product("strawberry", "Strawberries", Category.Fruit, 449, "500 g", "🍓", "Seasonal strawberries, picked yesterday."),
        Product("carrot", "Carrots", Category.Vegetables, 129, "1 kg", "🥕", "Unwashed organic carrots."),
        Product("tomato", "Vine tomatoes", Category.Vegetables, 349, "500 g", "🍅", "Ripened on the vine."),
        Product("avocado", "Avocado", Category.Vegetables, 199, "1 pc", "🥑", "Ready to eat."),
        Product("milk", "Whole milk", Category.Dairy, 139, "1 l", "🥛", "3.5% fat, pasteurised."),
        Product("cheese", "Aged cheddar", Category.Dairy, 599, "200 g", "🧀", "Matured for 12 months."),
        Product("yogurt", "Greek yogurt", Category.Dairy, 249, "400 g", "🥣", "Strained, 10% fat."),
        Product("sourdough", "Sourdough loaf", Category.Bakery, 450, "750 g", "🍞", "Baked every morning."),
        Product("croissant", "Butter croissant", Category.Bakery, 125, "1 pc", "🥐", "Laminated with French butter."),
        Product("olive-oil", "Olive oil", Category.Pantry, 1099, "750 ml", "🫒", "Extra virgin, cold pressed."),
        Product("pasta", "Spaghetti", Category.Pantry, 199, "500 g", "🍝", "Bronze-cut durum wheat."),
        Product("coffee", "Coffee beans", Category.Pantry, 1250, "500 g", "☕", "Medium roast, single origin."),
    )

    fun byId(id: String): Product? = products.firstOrNull { it.id == id }
}
