package de.aittr.g_52_shop.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Application Shop",
                description = "Application for various operations with Customers and Products",
                version = "1.0.0",
                contact = @Contact(
                        name = "Dmytro Luts",
                        email = "lutsdm@gmail.com",
                        url = "https://www.linkedin.com/in/dmytro-luts-a901b72b3/"
                )
        )
)
public class SwaggerConfig {
}
