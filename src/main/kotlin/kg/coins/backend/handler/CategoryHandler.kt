package kg.coins.backend.handler

import kg.coins.backend.repository.CategoryRepository
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*

@Component
class CategoryHandler(
    private val categoryRepository: CategoryRepository
) {
    suspend fun getAll(req: ServerRequest): ServerResponse {
        return ServerResponse
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyAndAwait(categoryRepository.findAll())
    }

    suspend fun getById(req: ServerRequest): ServerResponse {
        val id = Integer.parseInt(req.pathVariable("id"))

        return   categoryRepository.findById(id)?.let {
            ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValueAndAwait(it)
        } ?: ServerResponse.notFound().buildAndAwait()
    }

    suspend fun getCategoriesWithParam(req: ServerRequest): ServerResponse {
        return when(req.queryParam("mode").orElseGet { null }) {
            "main" ->  getMain(req)
            null -> when(req.queryParam("updateTime").orElseGet { null }){
                null -> getAll(req)
                else -> getByUpdateTime(req)
            }
            else -> ServerResponse.badRequest().buildAndAwait()
        }
    }

    suspend fun getByUpdateTime(req: ServerRequest): ServerResponse {
        val updateTime = req.queryParam("updateTime").get().toLong()
        return ServerResponse
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyAndAwait(categoryRepository.findByUpdateTime(updateTime))
    }


    suspend fun getCategoryByIdWithParam(req: ServerRequest): ServerResponse {
        return when(req.queryParam("mode").orElseGet { null }) {
            "child" ->  getChildById(req)
            "parent" ->  getParentById(req)
            null -> getById(req)
            else -> ServerResponse.badRequest().buildAndAwait()
        }
    }

    suspend fun getMain(req: ServerRequest): ServerResponse {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyAndAwait(categoryRepository.findAllMain())
    }


    suspend fun getParentById(req: ServerRequest): ServerResponse {
        val id = req.pathVariable("id").toInt()
        return categoryRepository.findById(id)?.let {
            it.parentId?.let { parentId ->
                categoryRepository.findById(parentId)?.let { parent ->
                    ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValueAndAwait(parent)
                }
            } ?: ServerResponse.notFound().buildAndAwait()
        } ?: ServerResponse.notFound().buildAndAwait()
    }

    suspend fun getChildById(req: ServerRequest): ServerResponse {
        val id = req.pathVariable("id").toInt()
        return categoryRepository.findById(id)?.let {
             ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyAndAwait(categoryRepository.findAllByParentId(id))
        } ?: ServerResponse.notFound().buildAndAwait()
    }



//
//    suspend fun add(req: ServerRequest): ServerResponse {
//        val receivedCat = req.awaitBodyOrNull(CatDto::class)
//
//        return receivedCat?.let {
//            ServerResponse
//                .ok()
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValueAndAwait(
//                    categoryRepository
//                        .save(it.toEntity())
//                        .toDto()
//                )
//        } ?: ServerResponse.badRequest().buildAndAwait()
//    }
//
//    suspend fun update(req: ServerRequest): ServerResponse {
//        val id = req.pathVariable("id")
//
//        val receivedCat = req.awaitBodyOrNull(CatDto::class)
//            ?: return ServerResponse.badRequest().buildAndAwait()
//
//        val existingCat = categoryRepository.findById(id.toLong())
//            ?: return ServerResponse.notFound().buildAndAwait()
//
//        return ServerResponse
//            .ok()
//            .contentType(MediaType.APPLICATION_JSON)
//            .bodyValueAndAwait(
//                categoryRepository.save(
//                    receivedCat.toEntity().copy(id = existingCat.id)
//                ).toDto()
//            )
//    }
//
//    suspend fun delete(req: ServerRequest): ServerResponse {
//        val id = req.pathVariable("id")
//
//        return if (categoryRepository.existsById(id.toLong())) {
//            categoryRepository.deleteById(id.toLong())
//            ServerResponse.noContent().buildAndAwait()
//        } else {
//            ServerResponse.notFound().buildAndAwait()
//        }
//    }
}