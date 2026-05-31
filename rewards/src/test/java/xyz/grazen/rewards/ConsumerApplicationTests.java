package xyz.grazen.rewards;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class ConsumerApplicationTests {

	@Test
	void contextLoads() {
		// This empty method tests that the application starts.
	}

}
