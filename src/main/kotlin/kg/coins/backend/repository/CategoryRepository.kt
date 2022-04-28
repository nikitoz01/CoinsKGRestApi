package kg.coins.backend.repository

import kg.coins.backend.model.Category
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository: CoroutineCrudRepository<Category,Int> {
    suspend fun findAllByParentId(parentId: Int): Flow<Category>

    @Query("select *\n" +
            "from category where ISNULL(parent_id) ;")
    suspend fun findAllMain(): Flow<Category>

    @Query("select *\n" +
            "from category where update_time > :updateTime ;")
    suspend fun findByUpdateTime(updateTime: Long): Flow<Category>
}