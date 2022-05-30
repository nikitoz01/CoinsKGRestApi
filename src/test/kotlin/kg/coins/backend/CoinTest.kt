package kg.coins.backend

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.slot
import kg.coins.backend.config.SecurityConfig
import kg.coins.backend.handler.CoinHandler
import kg.coins.backend.model.Coin
import kg.coins.backend.model.Image
import kg.coins.backend.repository.CategoryRepository
import kg.coins.backend.repository.CoinRepository
import kg.coins.backend.routing.CoinRouter
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest

import org.springframework.context.annotation.Import
import org.springframework.http.MediaType

import org.springframework.test.web.reactive.server.WebTestClient



@WebFluxTest
@Import(CoinRouter::class, CoinHandler::class,
    SecurityConfig::class)
class CoinTest(
    @Autowired private val client: WebTestClient
) {
    @MockkBean
    private lateinit var coinRepository: CoinRepository

    @MockkBean
    private lateinit var categoryRepository: CategoryRepository

    private fun testCoin1() = Coin(1, 1 , "Да Ли Юань Бао", 295000, "No-Image.jpg", null, null,
        null, "766–780", null, null ,"AE" ,null ,null ,null,
        null, 0, true)

    private fun testCoin2() = Coin(2, 1 , "Да Ли Юань Бао 2", 295000, "No-Image.jpg", null, null,
        null, "766–780", null, null ,"AE" ,null ,null ,null,
        null, 0, true)

    private fun testImage() = Image(1, "olololo", 1)

    @Test
    fun `coin test1`() {

        val savedCoin = slot<Coin>()

        coEvery {
            coinRepository.findById(any())
        } coAnswers {
            testCoin1()
        }

        coEvery {
            coinRepository.save(capture(savedCoin))
        } coAnswers {
            savedCoin.captured
        }

        client
            .post()
            .uri("/coins")
            .headers { httpHeaders -> httpHeaders.setBasicAuth("nikita", "123456") }
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(testCoin1())
            .exchange()
            .expectStatus()
            .isOk
    }

    @Test
    fun `coin test2`() {

        val savedCoin = slot<Coin>()

        coEvery {
            coinRepository.findById(any())
        } coAnswers {
            testCoin1()
        }

        coEvery {
            coinRepository.save(capture(savedCoin))
        } coAnswers {
            savedCoin.captured
        }

        client
            .post()
            .uri("/coins/1")
            .headers { httpHeaders -> httpHeaders.setBasicAuth("nikita", "123456") }
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(testCoin2())
            .exchange()
            .expectStatus()
            .isOk
    }

    @Test
    fun `coin test3`() {

        val savedCoin = slot<Coin>()

        coEvery {
            coinRepository.findById(any())
        } coAnswers {
            null
        }

        coEvery {
            coinRepository.save(capture(savedCoin))
        } coAnswers {
            savedCoin.captured
        }

        client
            .post()
            .uri("/categories/1")
            .headers { httpHeaders -> httpHeaders.setBasicAuth("nikita", "123456") }
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(testCoin1())
            .exchange()
            .expectStatus()
            .isNotFound
    }

    @Test
    fun `coin test4`() {

        val savedCoin = slot<Coin>()

        coEvery {
            coinRepository.findById(any())
        } coAnswers {
            testCoin1()
        }

        coEvery {
            coinRepository.save(capture(savedCoin))
        } coAnswers {
            savedCoin.captured
        }

        client
            .post()
            .uri("/coins/1")
            .headers { httpHeaders -> httpHeaders.setBasicAuth("nikita", "123456") }
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(testImage())
            .exchange()
            .expectStatus()
            .isBadRequest

    }

    @Test
    fun `coin test5`() {

        coEvery {
            coinRepository.existsById(any())
        } coAnswers {
            true
        }

        client
            .delete()
            .uri("/coins/1")
            .headers { httpHeaders -> httpHeaders.setBasicAuth("nikita", "123456") }
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isBadRequest
    }
}
