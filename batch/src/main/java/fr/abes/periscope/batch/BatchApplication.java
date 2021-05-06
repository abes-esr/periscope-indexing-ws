package fr.abes.periscope.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class BatchApplication {

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		SpringApplication.run(BatchApplication.class, args);
		long endTime = System.currentTimeMillis();
		log.debug("Timing " + (endTime-startTime) +" ms");
		System.exit(0);
	}

}
