package kg.coins.backend.model

import org.jetbrains.annotations.NotNull
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("coin")
data class Coin(
    @Id
    val id:Int,
    var categoryId: Int?,
    @NotNull
    var name: String,
    var zenoId: Int?,
    var imageUrl: String,
    var rarity: String?,
    var estimate: String?,
    var auctionURL: String?,
    var year: String?,
    var size: String?,
    var weight: String?,
    var metal: String?,
    var mint: String?,
    var denomination: String?,
    var description: String?,
    var descriptionDetailURL:String?,
    var updateTime: Long,
    var isActive: Boolean
    )
