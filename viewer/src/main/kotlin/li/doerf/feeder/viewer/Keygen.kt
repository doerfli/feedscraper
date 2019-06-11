package li.doerf.feeder.viewer

import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys

fun main() {
    for (b in Keys.secretKeyFor(SignatureAlgorithm.HS256).encoded) {
        print(String.format("%02X", b))
    }
    println()
}