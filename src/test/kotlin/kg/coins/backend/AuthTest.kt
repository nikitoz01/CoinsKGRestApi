package kg.coins.backend

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.slot
import kg.coins.backend.config.SecurityConfig
import kg.coins.backend.handler.CategoryHandler
import kg.coins.backend.handler.CoinHandler
import kg.coins.backend.model.Category
import kg.coins.backend.model.Coin
import kg.coins.backend.repository.CategoryRepository
import kg.coins.backend.repository.CoinRepository
import kg.coins.backend.routing.CategoryRouter
import kg.coins.backend.routing.CoinRouter
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient


@WebFluxTest
@Import(CategoryRouter::class, CategoryHandler::class,
CoinRouter::class, CoinHandler::class,
SecurityConfig::class)
class AuthTest(
    @Autowired val client: WebTestClient
) {
    @MockkBean
    private lateinit var categoryRepository: CategoryRepository

    @MockkBean
    private lateinit var coinRepository: CoinRepository

    private fun testCoin() = Coin(1, null , "CoinTest", null, "No-Image.jpg", null, null,
        null, null, null, null ,null ,null ,null ,null,
            null, 0, true)

    private fun testCategory1() = Category(1,"CategoryTest1", 2, null, null,
        null,1, true)

    private fun testCategory2() = Category(2,"CategoryTest2", null, null, null,
        null,1, true)

    @Test
    fun `auth test1`() {

        coEvery {
            categoryRepository.findById(any())
        } coAnswers {
            testCategory1()
        }

        coEvery {
            coinRepository.findAllByCategoryId(any())
        } returns flow {
           emit(testCoin())
        }

        client
            .get()
            .uri ("/coins?category=1")
            .exchange()
            .expectStatus()
            .isOk
    }

    @Test
    fun `auth test2`() {

        coEvery {
            categoryRepository.findById(any())
        } coAnswers {
            testCategory1()
        }

        coEvery {
            coinRepository.findAllByCategoryId(any())
        } returns flow {
            emit(testCoin())
        }

        client
            .get()
            .uri ("/categories/2?mode=parent")
            .headers{httpHeaders -> httpHeaders.setBasicAuth("nikita", "123456")}
            .exchange()
            .expectStatus()
            .isOk
    }

    @Test
    fun `auth test3`() {
        client
            .post()
            .uri ("/categories/1")
            //.headers{httpHeaders -> httpHeaders.setBasicAuth("nikita", "123456")}
            .exchange()
            .expectStatus()
            .isUnauthorized
    }

    @Test
    fun `auth test4`() {
        client
            .post()
            .uri ("/categories/1")
            .headers{httpHeaders -> httpHeaders.setBasicAuth("artem", "123456")}
            .exchange()
            .expectStatus()
            .isUnauthorized
    }

    @Test
    fun `auth test5`() {

        coEvery {
            categoryRepository.findById(any())
        } coAnswers {
            testCategory1()
        }

        val savedCat = slot<Category>()

        coEvery {
            categoryRepository.save(capture(savedCat))
        } coAnswers {
            savedCat.captured
        }

        client
            .post()
            .uri ("/categories/1")
            .headers{httpHeaders -> httpHeaders.setBasicAuth("nikita", "123456")}
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(testCategory2())
            .exchange()
            .expectStatus()
            .isOk
    }


}
