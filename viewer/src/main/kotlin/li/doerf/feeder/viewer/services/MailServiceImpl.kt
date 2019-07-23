package li.doerf.feeder.viewer.services

import li.doerf.feeder.common.util.getLogger
import li.doerf.feeder.viewer.entities.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import java.text.SimpleDateFormat
import java.util.*

@Service
class MailServiceImpl @Autowired constructor(
        private val templateEngine: TemplateEngine,
        private val mailgunService: MailgunServiceImpl
) : MailService {

    @Value("\${feedscraper.baseUrl:https://feedscraper.bytes.li}")
    private lateinit var applBaseUrl: String
    @Value("\${feedscraper.mailsender:feedscraper@bytes.li}")
    private lateinit var mailSender: String


    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    override suspend fun sendSignupMail(user: User) {
        log.debug("sending signup email to $user")
        val ctx = Context()
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm")
        ctx.setVariable("email", user.username)
        ctx.setVariable("link", "$applBaseUrl/#/confirmation/${user.token}")
        ctx.setVariable("validUntil", dateFormat.format(Date.from(user.tokenExpiration)))

        val content = templateEngine.process("signup.txt", ctx)

        mailgunService.sendEmail(
                mailSender,
                user.username,
                "Confirm sign up to feedscraper",
                content)
    }

    override suspend fun sendSignupReminderMail(user: User) {
        log.debug("sending signup reminder email to $user")
        val ctx = Context()
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm")
        ctx.setVariable("email", user.username)
        ctx.setVariable("link", "$applBaseUrl/#/confirmation/${user.token}")
        ctx.setVariable("validUntil", dateFormat.format(Date.from(user.tokenExpiration)))

        val content = templateEngine.process("signup_reminder.txt", ctx)

        mailgunService.sendEmail(
                mailSender,
                user.username,
                "Remember to confirm sign up to feedscraper",
                content)
    }

    override suspend fun sendSignupAccountExistsMail(user: User) {
        log.debug("sending signup with existing email to $user")
        val ctx = Context()
        ctx.setVariable("email", user.username)

        val content = templateEngine.process("signup_account_exists.txt", ctx)

        mailgunService.sendEmail(
                mailSender,
                user.username,
                "You feedscraper account already exists",
                content)
        log.debug("email sent")
    }

    override suspend fun sendPasswordResetMail(user: User) {
        log.debug("sending password reset mail to $user")
        val ctx = Context()
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm")
        ctx.setVariable("email", user.username)
        ctx.setVariable("link", "$applBaseUrl/#/resetPassword/${user.token}")
        ctx.setVariable("validUntil", dateFormat.format(Date.from(user.tokenExpiration)))

        val content = templateEngine.process("passwordReset.txt", ctx)

        mailgunService.sendEmail(
                mailSender,
                user.username,
                "Reset your password for feedscraper",
                content)
    }
}