package li.doerf.feeder.viewer.services

import li.doerf.feeder.common.util.getLogger
import li.doerf.feeder.viewer.entities.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import java.text.SimpleDateFormat
import java.util.*

@Service
class MailService @Autowired constructor(
        private val templateEngine: TemplateEngine,
        private val mailgunService: MailgunService,
        private val applBaseUrl: String
){

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    suspend fun sendSignupMail(user: User) {
        log.debug("sending signup email to $user")
        val ctx = Context()
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm")
        ctx.setVariable("email", user.username)
        // TODO make link dynamic
        ctx.setVariable("link", "$applBaseUrl/confirmation/${user.token}")
        ctx.setVariable("validUntil", dateFormat.format(Date.from(user.tokenExpiration)))

        val content = templateEngine.process("signup.txt", ctx)

        mailgunService.sendEmail(
                "feedscraper@bytes.li",
                user.username,
                "Confirm sign up to feedscraper",
                content)
    }

    suspend fun sendSignupReminderMail(user: User) {
        log.debug("sending signup reminder email to $user")
        val ctx = Context()
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm")
        ctx.setVariable("email", user.username)
        // TODO make link dynamic
        ctx.setVariable("link", "$applBaseUrl/confirmation/${user.token}")
        ctx.setVariable("validUntil", dateFormat.format(Date.from(user.tokenExpiration)))

        val content = templateEngine.process("signup_reminder.txt", ctx)

        mailgunService.sendEmail(
                "feedscraper@bytes.li",
                user.username,
                "Remember to confirm sign up to feedscraper",
                content)
    }

    suspend fun sendSignupAccountExistsMail(user: User) {
        log.debug("sending signup with existing email to $user")
        val ctx = Context()
        ctx.setVariable("email", user.username)

        val content = templateEngine.process("signup_account_exists.txt", ctx)

        mailgunService.sendEmail(
                "feedscraper@bytes.li",
                user.username,
                "You feedscraper account already exists",
                content)
        log.debug("email sent")
    }
}