package li.doerf.feeder.viewer.services

//import kotlinx.coroutines.GlobalScope
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Response
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.util.ReflectionTestUtils

@ExtendWith(SpringExtension::class, MockitoExtension::class)
@Import(MailgunService::class, Fuel::class)
class MailgunServiceTest {

    @Autowired
    private lateinit var mailgunService: MailgunService


    @BeforeEach
    fun setup() {
        ReflectionTestUtils.setField(mailgunService, "baseUrl", "http://foo.com/domain")
        ReflectionTestUtils.setField(mailgunService, "apiKey", "abcdefgh")
    }

    @Test
    fun testSendMail() {
        runBlocking {
            val responseMock = Mockito.mock(Response::class.java)
            Mockito.doReturn(200).`when`(responseMock).statusCode

            val serviceSpy = Mockito.spy(mailgunService)
            Mockito.doReturn(responseMock).`when`(serviceSpy).sendRequest(Mockito.anyString(), Mockito.anyList())

            serviceSpy.sendEmail(
                    "sender@mail.com",
                    "recipient@mail.com",
                    "this is the subject",
                    "this is the body")

            Mockito.verify(serviceSpy).sendRequest(Mockito.anyString(), Mockito.anyList())
        }
    }

}