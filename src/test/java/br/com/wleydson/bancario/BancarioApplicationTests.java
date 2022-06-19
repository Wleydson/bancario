package br.com.wleydson.bancario;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BancarioApplicationTests {

	//Esse método nunca lança exceção
	private void doNotThrowException() {
	}

	@Test
	void contextLoads() {
		Assertions.assertDoesNotThrow(this::doNotThrowException);
	}

	@Test
	void applicationContextTest() {
		BancarioApplication.main(new String[]{});
		Assertions.assertDoesNotThrow(this::doNotThrowException);
	}

}
