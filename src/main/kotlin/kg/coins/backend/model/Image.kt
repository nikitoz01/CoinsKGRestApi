package kg.coins.backend.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("image")
data class Image(
    @Id
    val id:Int,
    var url: String?,
    var updateTime: Long
)