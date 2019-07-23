package li.doerf.feeder.viewer.services

import li.doerf.feeder.viewer.entities.User

interface UserService {
    fun signin(username: String, password: String): String
    fun signup(username: String, password: String)
    fun confirm(token: String): String
    fun confirmUserToken(user: User)
    fun requestPasswordReset(username: String)
    fun resetPassword(token: String, password: String)
}