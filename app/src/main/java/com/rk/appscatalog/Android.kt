package com.rk.appscatalog

data class Android(val apiLevel: Int, val version: String, val name: String) {
    companion object {
        private val androids = listOf(
            Android(29, "10", "Android10"),
            Android(28, "9", "Pie"),
            Android(27, "8.1.0", "Oreo"),
            Android(26, "8.0.0", "Oreo"),
            Android(24, "7.0", "Nougat"),
            Android(25, "7.1", "Nougat"),
            Android(23, "6.0", "Marshmallow"),
            Android(22, "5.1", "Lollipop"),
            Android(21, "5.0", "Lollipop"),
            Android(19, "4.4 - 4.4.4", "KitKat"),
            Android(18, "4.3.x", "Jelly Bean"),
            Android(17, "4.2.x", "Jelly Bean"),
            Android(16, "4.1.x", "Jelly Bean"),
            Android(15, "4.0.3 - 4.0.4", "Ice Cream Sandwich"),
            Android(14, "4.0.1 - 4.0.2", "Ice Cream Sandwich"),
            Android(13, "3.2.x", "Honeycomb"),
            Android(12, "3.1", "Honeycomb"),
            Android(11, "3.0", "Honeycomb"),
            Android(10, "2.3.3 - 2.3.7", "Gingerbread"),
            Android(9, "2.3 - 2.3.2", "Gingerbread"),
            Android(8, "2.2.x", "Froyo"),
            Android(7, "2.1", "Eclair"),
            Android(6, "2.0.1", "Eclair"),
            Android(5, "2.0", "Eclair"),
            Android(4, "1.6", "Donut"),
            Android(3, "1.5", "Cupcake"),
            Android(2, "1.1", "(no codename)"),
            Android(1, "1.0", "(no codename)")
        )

        fun getDescriptionLabelFor(apiLevel: Int): String {
            return androids.find { it.apiLevel == apiLevel }?.description ?: "API $apiLevel"
        }
    }

    val description: String
        get() = "$name ($version) [API $apiLevel]"

}


