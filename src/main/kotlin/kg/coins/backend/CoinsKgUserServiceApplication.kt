package kg.coins.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories


@SpringBootApplication
@EnableR2dbcRepositories
class CoinsKgUserServiceApplication

fun main(args: Array<String>) {
    runApplication<CoinsKgUserServiceApplication>(*args)
}
