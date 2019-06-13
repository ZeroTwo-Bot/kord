@file:Suppress("EXPERIMENTAL_API_USAGE")

package json

import com.gitlab.hopebaron.websocket.entity.Snowflake
import com.gitlab.hopebaron.websocket.entity.User
import kotlinx.serialization.json.Json
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

private fun file(name: String): String {
    val loader = ChannelTest::class.java.classLoader
    return loader.getResource("json/user/$name.json").readText()
}

class UserTest : Spek({

    describe("user") {
        it("is deserialized correctly") {
            val user = Json.parse(User.serializer(), file("user"))

            with(user) {
                id shouldBe Snowflake("80351110224678912")
                username shouldBe "Nelly"
                discriminator shouldBe "1337"
                avatar shouldBe "8342729096ea3675442027381ff50dfe"
                verified shouldBe true
                email shouldBe "nelly@discordapp.com"
                flags shouldBe 64
                premiumType!!.code shouldBe 1
            }

        }
    }

})