package fr.abes.periscope.batch;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BatchApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void splitToChunk() {
		int partitionSize = 1000;
		Integer maxSize = 17429964;

		for (Integer i = 0; i < maxSize; i += partitionSize) {
			System.out.println(i + Math.min(i + partitionSize, maxSize));
		}
	}

}
