package ru.sber.streams

import io.mockk.InternalPlatformDsl.toArray


// 1. Используя withIndex() посчитать сумму элементов листа, индекс которых кратен 3. (нулевой индекс тоже входит)
fun getSumWithIndexDivisibleByThree(list: List<Long>): Long = list.withIndex().filter { it.index % 3 == 0 }.sumOf { it.value }

// 2. Используя функцию generateSequence() создать последовательность, возвращающую числа Фибоначчи.
fun generateFibonacciSequence(): Sequence<Int> = generateSequence(Pair(0, 1)) { Pair(it.second, it.first + it.second) }.map { it.first }

// 3. Получить города, в которых есть покупатели.
fun Shop.getCustomersCities(): Set<City> = customers.map { it.city }.toSet()

// 4. Получить все когда-либо заказанные продукты.
fun Shop.allOrderedProducts(): Set<Product> = customers.flatMap { customer -> customer.orders.flatMap { it.products } }.toSet()

// 5. Получить покупателя, который сделал больше всего заказов.
fun Shop.getCustomerWithMaximumNumberOfOrders(): Customer? = customers.maxByOrNull { it.orders.size }!!

// 6. Получить самый дорогой продукт, когда-либо приобретенный покупателем.
fun Customer.getMostExpensiveProduct(): Product? = orders.map { order -> order.products.maxByOrNull { it.price } }.maxByOrNull { it!!.price }

// 7. Получить соответствие в мапе: город - количество заказанных и доставленных продуктов в данный город.
fun Shop.getNumberOfDeliveredProductByCity(): Map<City, Int> = customers.map { customer ->
    Pair(customer.city,
    customer.orders.sumOf { if (it.isDelivered) it.products.size else 0 }) }
    .groupBy { it.first }
    .map { entry -> Pair(entry.key, entry.value.sumOf { it.second }) }
    .toMap()

// 8. Получить соответствие в мапе: город - самый популярный продукт в городе.
fun Shop.getMostPopularProductInCity(): Map<City, Product> = customers.map { customer ->
    Pair(customer.city,
        customer.orders.map { it.products }.flatten()) }
    .groupBy { it.first }
    .map { entry -> Pair(entry.key, entry.value.flatMap { it.second }.groupingBy { it }.eachCount().maxByOrNull { it.value }!!.key) }
    .toMap()

// 9. Получить набор товаров, которые заказывали все покупатели.
fun Shop.getProductsOrderedByAll(): Set<Product> = customers.asSequence().map { customer ->
    Pair(customer, customer.orders) }
    .map { pair -> Pair(pair.first, pair.second.map { it.products }.flatMap { it.distinct() }.distinct()) }
    .groupBy { it.first }
    .map { entry -> entry.value.flatMap { it.second }.toSet() }
    .distinct()
    .reduce{ acc, set ->  acc.intersect(set)}

