package kg.coins.backend.model

import org.jetbrains.annotations.NotNull
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("category")
data class Category(
    @Id
    val id: Int,
    @NotNull
    var name: String,
    var parentId: Int?,
    var period: String?,
    var description: String?,
    var detailURL: String?,
    var updateTime: Long,
    var isActive: Boolean
)

