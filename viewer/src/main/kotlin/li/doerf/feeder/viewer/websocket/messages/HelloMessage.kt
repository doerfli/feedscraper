package li.doerf.feeder.viewer.websocket.messages

import li.doerf.feeder.common.util.NoArg

@NoArg
data class HelloMessage(
        val name: String
)