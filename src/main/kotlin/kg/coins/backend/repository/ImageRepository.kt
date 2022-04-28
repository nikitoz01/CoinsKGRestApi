package kg.coins.backend.repository

import kg.coins.backend.model.Image
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ImageRepository: CoroutineCrudRepository<Image, Int> {
    @Query("select *\n" +
            "from image where update_time > :updateTime ;")
    suspend fun findByUpdateTime(updateTime: Long): Flow<Image>
}