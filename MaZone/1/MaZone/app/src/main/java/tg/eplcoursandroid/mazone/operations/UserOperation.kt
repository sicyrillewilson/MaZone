package tg.eplcoursandroid.mazone.operations

import tg.eplcoursandroid.mazone.entites.User

class UserOperation {
    private val users = mutableListOf<User>()

    fun addUser(user: User) {
        users.add(user)
    }

    fun getAllUsers(): List<User> = users
}