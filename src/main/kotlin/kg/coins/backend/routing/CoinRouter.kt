package kg.coins.backend.routing

import kg.coins.backend.handler.CoinHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class CoinRouter (private val coinHandler: CoinHandler){
    @Bean
    fun coinEndpoints(): RouterFunction<ServerResponse> {
        return coRouter {
            "/coins".nest {
                accept(MediaType.APPLICATION_JSON).nest {
                    GET("",
                        queryParam("category") { true } and
                                queryParam("mode") { true } and
                                queryParam("updateTime") { true }
                        , coinHandler::getCoinWithParam)
                    GET("", coinHandler::getCoinWithParam)

                    contentType(MediaType.APPLICATION_JSON).nest {
                        POST("", coinHandler::add)
                    }


                    "/{id}".nest {
                        GET("",coinHandler::getById)
                        DELETE("", coinHandler::delete)
                        POST("", coinHandler::update)
                    }
                }
            }
        }
    }
}