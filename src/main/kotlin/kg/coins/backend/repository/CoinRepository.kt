package kg.coins.backend.repository

import kg.coins.backend.model.Coin
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CoinRepository: CoroutineCrudRepository<Coin, Int> {

    suspend fun findAllByCategoryId(categoryId: Int): Flow<Coin>


    @Query("select *\n" +
            "from coin where ISNULL(category_id) ;")
    suspend fun findAllMain(): Flow<Coin>

    @Query("select *\n" +
            "from coin where update_time > :updateTime ;")
    suspend fun findByUpdateTime(updateTime: Long): Flow<Coin>
}