package li.doerf.feeder.viewer.websocket.controller

import li.doerf.feeder.common.util.getLogger
import li.doerf.feeder.viewer.util.UserUtils
import li.doerf.feeder.viewer.websocket.messages.HelloMessage
import li.doerf.feeder.viewer.websocket.messages.NewFeedsMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import java.security.Principal

@Controller
class FeedMessagesController @Autowired constructor(
        val userUtils: UserUtils
) {

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    @MessageMapping("/hello")
    @SendTo("/topic/feeds")
    fun feeds(hello: HelloMessage, principal: Principal): NewFeedsMessage {
        log.debug("user ${principal}")
        log.debug(hello.name)
        return NewFeedsMessage("dididi")
    }

}