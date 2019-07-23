package li.doerf.feeder.viewer.services

import li.doerf.feeder.viewer.entities.User

interface MailService {
    suspend fun sendSignupMail(user: User)

    suspend fun sendSignupReminderMail(user: User)

    suspend fun sendSignupAccountExistsMail(user: User)

    suspend fun sendPasswordResetMail(user: User)
}