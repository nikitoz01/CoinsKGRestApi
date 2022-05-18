package kg.coins.backend.handler

import kg.coins.backend.model.Category
import kg.coins.backend.repository.CategoryRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
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


    suspend fun upperCheck(categoryId: Int, categoryParentId: Int? = null): Boolean{
        val set = mutableSetOf(categoryId)
        categoryParentId?.let { set.add(it) } ?: run{null}

        while(true){
            val parentCategory = categoryRepository.findById(categoryParentId!!)?.parentId
            println("$parentCategory")
            if (parentCategory == null) break else {
            if(!set.contains(parentCategory)) set.add(parentCategory)
            else return false }
        }
        return true
    }

    suspend fun lowerCheck(categoryId:Int, set: MutableSet<Int>): Boolean{
        val childCategory = categoryRepository.findAllByParentId(categoryId)
        var b = true
        childCategory.map {

            println("b"+it.id)

            if (set.contains(it.id)){
                b = false
                return@map
            } else{
                set.add(it.id)
                b = lowerCheck(it.id,set)
                if (!b) return@map
            }
        }.collect()
        return b
    }

    suspend fun test(req: ServerRequest): ServerResponse{
        val id = req.pathVariable("id").toInt()
        var b = true
        val set: MutableSet<Int> = mutableSetOf()
        b = upperCheck(id)
        if (b) b = lowerCheck(id, set)

        return if (b) ServerResponse.ok().buildAndAwait()
        else ServerResponse.badRequest().buildAndAwait()
    }


    suspend fun add(req: ServerRequest): ServerResponse {
        val receivedCat = req.awaitBodyOrNull(Category::class)
            ?: return ServerResponse.badRequest().buildAndAwait()

        return if ( upperCheck(receivedCat.id, receivedCat.parentId)) receivedCat.let {
            ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValueAndAwait(
                    categoryRepository
                        .save(it)
                )
        }
        else ServerResponse.badRequest().buildAndAwait()
    }

    suspend fun update(req: ServerRequest): ServerResponse {
        val id = req.pathVariable("id")

        val receivedCat = req.awaitBodyOrNull(Category::class)
            ?: return ServerResponse.badRequest().buildAndAwait()

        val existingCat = categoryRepository.findById(id.toInt())
            ?: return ServerResponse.notFound().buildAndAwait()

        return if (upperCheck(id.toInt(), receivedCat.parentId)) ServerResponse
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValueAndAwait(
                categoryRepository.save(
                    receivedCat.copy(id = existingCat.id)
                )
            )
        else ServerResponse.badRequest().buildAndAwait()
    }

    suspend fun delete(req: ServerRequest): ServerResponse {
        val id = req.pathVariable("id")
        return if (categoryRepository.existsById(id.toInt())) {
            try {
                categoryRepository.deleteById(id.toInt())
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