package li.doerf.feeder.viewer.services

interface MailgunService {
    suspend fun sendEmail(sender: String, recipient: String, subject: String, text: String)
}