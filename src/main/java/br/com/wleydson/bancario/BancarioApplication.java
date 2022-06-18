package br.com.wleydson.bancario;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "BANCARIO API", version = "1.0", description = "Api para simular sistema de banco"))
public class BancarioApplication {

	public static void main(String[] args) {
		SpringApplication.run(BancarioApplication.class, args);
	}

}
