package li.doerf.feeder.viewer.websocket.controller

import li.doerf.feeder.common.util.getLogger
import li.doerf.feeder.viewer.websocket.messages.HelloMessage
import li.doerf.feeder.viewer.websocket.messages.NewFeedsMessage
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller

@Controller
class FeedMessagesController {

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    @MessageMapping("/hello")
    @SendTo("/topic/feeds")
    fun feeds(hello: HelloMessage): NewFeedsMessage {
        log.debug(hello.name)
        Thread.sleep(1000)
        return NewFeedsMessage("dididi")
    }

}