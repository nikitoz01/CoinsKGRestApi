package kg.coins.backend.routing

import kg.coins.backend.handler.ImageHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.IMAGE_JPEG
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class ImageRouter(private val imageHandler: ImageHandler) {
    @Bean
    fun imageEndpoints(): RouterFunction<ServerResponse> {
        return coRouter {
            "/images".nest {
                accept(APPLICATION_JSON).nest {
                    GET("", queryParam("updateTime") { true } ,imageHandler::getImagesWithParam)
                    GET("",imageHandler::getImagesWithParam)
//                    contentType(APPLICATION_JSON).nest {
//                        POST("", categoryHandler::add)
//                    }

                    "/{id}".nest {
                        GET("",imageHandler::getById)
                    }
                }
                accept(IMAGE_JPEG).nest{
                    GET("", queryParam("imageName") { true },imageHandler::getImagesWithParam)
                }
            }
        }
    }
}