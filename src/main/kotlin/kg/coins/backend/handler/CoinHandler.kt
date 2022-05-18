package kg.coins.backend.handler

import kg.coins.backend.model.Coin
import kg.coins.backend.repository.CategoryRepository
import kg.coins.backend.repository.CoinRepository
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*

@Component
class CoinHandler(
    private val coinRepository: CoinRepository,
    private val categoryRepository: CategoryRepository
) {
    suspend fun getAll(req: ServerRequest): ServerResponse {
        return ServerResponse
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyAndAwait(coinRepository.findAll())
    }

    suspend fun getById(req: ServerRequest): ServerResponse {
        val id = Integer.parseInt(req.pathVariable("id"))

        return   coinRepository.findById(id)?.let {
            ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValueAndAwait(it)
        } ?: ServerResponse.notFound().buildAndAwait()
    }

//    suspend fun getCategoryWithParam(req: ServerRequest): ServerResponse {
//        return when {
//            req.queryParam("category").isPresent -> getCoinByCategory(req)
//            else -> getAll(req)
//        }
//    }
    suspend fun getCoinWithParam(req: ServerRequest): ServerResponse {
        return when(req.queryParam("mode").orElseGet { null }) {
            "main" ->  getMain(req)
             else -> when(req.queryParam("category").orElseGet { null }){
                 null -> when(req.queryParam("updateTime").orElseGet { null }) {
                     null -> getAll(req)
                     else -> getByUpdateTime(req)
                 }
                 else ->  getCoinByCategory(req)
             }
        }
    }

    suspend fun getByUpdateTime(req: ServerRequest): ServerResponse {
        val updateTime = req.queryParam("updateTime").get().toLong()
        return ServerResponse
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyAndAwait(coinRepository.findByUpdateTime(updateTime))
    }


    suspend fun getCoinByCategory(req: ServerRequest): ServerResponse {
        return try {
            val id = req.queryParam("category").get().toInt()
            categoryRepository.findById(id)?.let {
                ServerResponse
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyAndAwait(coinRepository.findAllByCategoryId(id))
            } ?: ServerResponse.notFound().buildAndAwait()
        }
        catch (e: NumberFormatException) {
             ServerResponse.badRequest().buildAndAwait()
        }
    }

    suspend fun getMain(req: ServerRequest): ServerResponse {
           return ServerResponse
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyAndAwait(  coinRepository.findAllMain() )
    }

    suspend fun add(req: ServerRequest): ServerResponse {
        val receivedCoin = req.awaitBodyOrNull(Coin::class)

        return receivedCoin?.let {
            ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValueAndAwait(
                    coinRepository
                        .save(it)
                )
        } ?: ServerResponse.badRequest().buildAndAwait()
    }

    suspend fun update(req: ServerRequest): ServerResponse {
        val id = req.pathVariable("id")

        val receivedCoin = req.awaitBodyOrNull(Coin::class)
            ?: return ServerResponse.badRequest().buildAndAwait()

        val existingCoin = coinRepository.findById(id.toInt())
            ?: return ServerResponse.notFound().buildAndAwait()

        return ServerResponse
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValueAndAwait(
                coinRepository.save(
                    receivedCoin.copy(id = existingCoin.id)
                )
            )
    }

    suspend fun delete(req: ServerRequest): ServerResponse {
        val id = req.pathVariable("id")

        return if (coinRepository.existsById(id.toInt())) {
            try {
                coinRepository.deleteById(id.toInt())
                ServerResponse.noContent().buildAndAwait()
            }
            catch (e: Exception) {
                ServerResponse.badRequest().buildAndAwait()
            }
        } else {
            ServerResponse.notFound().buildAndAwait()
        }
    }
}