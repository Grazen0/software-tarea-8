package xyz.grazen.restaurant;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class RestaurantApplicationTests {

	@Test
	void contextLoads() {
	}

}
