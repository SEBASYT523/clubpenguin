package co.edu.unbosque.backclubpenguin.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI customOpenAPI() {

		io.swagger.v3.oas.models.security.SecurityScheme securityScheme = new io.swagger.v3.oas.models.security.SecurityScheme()
				.type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT");

		return new OpenAPI()

				.components(new Components().addSecuritySchemes("bearerAuth", securityScheme).addResponses(
						"UnauthorizedError",
						new ApiResponse().description("No autenticado - Token JWT inválido o expirado")
								.content(new Content().addMediaType("application/json",
										new MediaType().addExamples("error",
												new Example()
														.value("{\"error\": \"No autorizado\", \"mensaje\":"
																+ " \"Token inválido o expirado\"}")))))
						.addResponses("ForbiddenError", new ApiResponse()
								.description("Acceso prohibido - No tienes permisos suficientes")
								.content(new Content().addMediaType("application/json",
										new MediaType().addExamples("error",
												new Example().value("{\"error\": \"Acceso prohibido\", \"mensaje\":"
														+ " \"No tienes permisos para esta" + " operación\"}")))))
						.addResponses("NotFoundError", new ApiResponse().description("Recurso no encontrado")
								.content(new Content().addMediaType("application/json",
										new MediaType().addExamples("error",
												new Example().value("{\"error\": \"No encontrado\", \"mensaje\":"
														+ " \"El recurso solicitado no" + " existe\"}"))))));
	}
}
