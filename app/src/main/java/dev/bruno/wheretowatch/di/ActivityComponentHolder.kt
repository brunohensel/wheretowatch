package dev.bruno.wheretowatch.di

object ActivityComponentHolder {
    val components = mutableSetOf<ActivityComponent>()

    fun getComponent(): ActivityComponent = components.single()
}

operator fun MutableSet<ActivityComponent>.plusAssign(element: ActivityComponent) {
    this.clear()
    this.add(element)
}
