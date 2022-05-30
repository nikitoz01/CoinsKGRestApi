package kg.coins.backend

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.slot
import kg.coins.backend.config.SecurityConfig
import kg.coins.backend.handler.CategoryHandler
import kg.coins.backend.model.Category
import kg.coins.backend.model.Image
import kg.coins.backend.repository.CategoryRepository
import kg.coins.backend.routing.CategoryRouter
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest

import org.springframework.context.annotation.Import
import org.springframework.http.MediaType

import org.springframework.test.web.reactive.server.WebTestClient



@WebFluxTest
@Import(CategoryRouter::class, CategoryHandler::class,
    SecurityConfig::class)
class CategoryTest(
    @Autowired private val client: WebTestClient
) {
    @MockkBean
    private lateinit var categoryRepository: CategoryRepository

    private fun testCategory1() = Category(
        1, "Карлукский Каганат", null, "756 — 940", null,
        null, 1, true
    )

    private fun testCategory2() = Category(
        1, "newКарлукский Каганат", null, "756 — 940", null,
        null, 1, true
    )

    private fun testImage() = Image(1, "olololo", 1)

    @Test
    fun `category test1`() {

        val savedCat = slot<Category>()

        coEvery {
            categoryRepository.findById(any())
        } coAnswers {
            testCategory1()
        }

        coEvery {
            categoryRepository.save(capture(savedCat))
        } coAnswers {
            savedCat.captured
        }

        client
            .post()
            .uri("/categories")
            .headers { httpHeaders -> httpHeaders.setBasicAuth("nikita", "123456") }
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(testCategory1())
            .exchange()
            .expectStatus()
            .isOk
    }

    @Test
    fun `category test2`() {

        val savedCat = slot<Category>()

        coEvery {
            categoryRepository.findById(any())
        } coAnswers {
            testCategory1().apply { parentId = 1 }
        }

        coEvery {
            categoryRepository.save(capture(savedCat))
        } coAnswers {
            savedCat.captured
        }

        client
            .post()
            .uri("/categories")
            .headers { httpHeaders -> httpHeaders.setBasicAuth("nikita", "123456") }
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(testCategory1().apply { parentId = 1 })
            .exchange()
            .expectStatus()
            .isBadRequest
    }

    @Test
    fun `category test3`() {

        val savedCat = slot<Category>()

        coEvery {
            categoryRepository.findById(any())
        } coAnswers {
            null
        }

        coEvery {
            categoryRepository.save(capture(savedCat))
        } coAnswers {
            savedCat.captured
        }

        client
            .post()
            .uri("/categories/1")
            .headers { httpHeaders -> httpHeaders.setBasicAuth("nikita", "123456") }
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(testCategory1())
            .exchange()
            .expectStatus()
            .isNotFound
    }

    @Test
    fun `category test4`() {

        val savedCat = slot<Category>()

        coEvery {
            categoryRepository.findById(any())
        } coAnswers {
            testCategory1()
        }

        coEvery {
            categoryRepository.save(capture(savedCat))
        } coAnswers {
            savedCat.captured
        }

        client
            .post()
            .uri("/categories/1")
            .headers { httpHeaders -> httpHeaders.setBasicAuth("nikita", "123456") }
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(testImage())
            .exchange()
            .expectStatus()
            .isBadRequest

    }

    @Test
    fun `category test5`() {

        coEvery {
            categoryRepository.existsById(any())
        } coAnswers {
            true
        }

        client
            .delete()
            .uri("/categories/1")
            .headers { httpHeaders -> httpHeaders.setBasicAuth("nikita", "123456") }
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isBadRequest

    }
}
