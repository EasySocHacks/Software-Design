import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver


@Configuration
@EnableWebMvc
@ComponentScan(basePackages = ["easy.soc.hacks.hw4"])
class WebConfig : WebMvcConfigurer {
   @Bean
   fun freemarkerViewResolver(): FreeMarkerViewResolver {
      val resolver = FreeMarkerViewResolver()
      resolver.isCache = true
      resolver.setSuffix(".ftlh")
      return resolver
   }
}