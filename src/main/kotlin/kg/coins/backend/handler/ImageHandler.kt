package kg.coins.backend.handler

import kg.coins.backend.repository.ImageRepository
import org.springframework.core.io.ResourceLoader
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*

@Component
class ImageHandler (private val imageRepository: ImageRepository,
private val resourceLoader: ResourceLoader) {

    val dirUrl = "/image"

    suspend fun getImage(req: ServerRequest): ServerResponse {
        val imageName = req.queryParam("imageName").get()
        return ServerResponse
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .bodyValueAndAwait(resourceLoader.getResource("classpath:$dirUrl/$imageName"))
    }

    suspend fun getById(req: ServerRequest): ServerResponse {
        val id = Integer.parseInt(req.pathVariable("id"))
        return imageRepository.findById(id)?.let {
            ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValueAndAwait(it)
        } ?: ServerResponse.notFound().buildAndAwait()
    }

    suspend fun getAll(req: ServerRequest): ServerResponse {
        return ServerResponse
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyAndAwait(imageRepository.findAll())
    }

    suspend fun getByUpdateTime(req: ServerRequest): ServerResponse {
        val updateTime = req.queryParam("updateTime").get().toLong()
        return ServerResponse
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyAndAwait(imageRepository.findByUpdateTime(updateTime))
    }

    suspend fun getImagesWithParam(req: ServerRequest): ServerResponse {
        return when (req.queryParam("imageName").orElseGet { null }) {
            null -> when (req.queryParam("updateTime").orElseGet { null }) {
                null -> getAll(req)
                else -> getByUpdateTime(req)
            }
            else -> getImage(req)
        }
    }
}
