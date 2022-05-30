package kg.coins.backend.routing

import kg.coins.backend.handler.CategoryHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class CategoryRouter (
    private val categoryHandler: CategoryHandler
    ) {
    @Bean
    fun categoryEndpoints() = coRouter {
        "/categories".nest {
            accept(APPLICATION_JSON).nest {
                GET("", queryParam("mode") { true } and
                        queryParam("updateTime") { true }, categoryHandler::getCategoriesWithParam)
                GET("", categoryHandler::getCategoriesWithParam)

                contentType(APPLICATION_JSON).nest {
                    POST("", categoryHandler::add)
                }
                "/test/{id}".nest {
                    GET("", categoryHandler::test)
                }

                "/{id}".nest {
                    GET("", queryParam("mode") { true }, categoryHandler::getCategoryByIdWithParam)
                    GET("", categoryHandler::getById)
                    DELETE("", categoryHandler::delete)
                    POST("", categoryHandler::update)
                }
            }
        }
    }
}


