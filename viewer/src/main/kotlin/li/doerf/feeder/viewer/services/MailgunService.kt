package li.doerf.feeder.viewer.services

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Method
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.coroutines.awaitStringResponseResult
import li.doerf.feeder.common.util.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class MailgunService @Autowired constructor(
        val fuel: Fuel
){

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    @Value("\${mailgun.apiKey:MYAPIKEY}")
    private lateinit var apiKey: String
    @Value("\${mailgun.baseUrl:https://api.mailgun.net/v3/DOMAIN}")
    private lateinit var baseUrl: String


    suspend fun sendEmail(sender: String, recipient: String, subject: String, text: String) {
        val body= listOf(
                "from" to sender,
                "to" to recipient,
                "subject" to subject,
                "text" to text
        )

        val url = "$baseUrl/messages"
        log.trace("posting to url $url")
        val response = sendRequest(url, body)
        log.debug("mail sent - response statusCode ${response.statusCode}")
        if (response.statusCode != 200) {
            throw IllegalStateException("request to send email not successful")
        }
    }

    // visible for testing
    suspend fun sendRequest(url: String, body: List<Pair<String, String>>): Response {
        val result = fuel.upload(url, Method.POST, body)
                .authentication()
                .basic("api", apiKey)
                .awaitStringResponseResult()
        return result.second
    }

}