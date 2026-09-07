package frederico.borges.dropzone;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableScheduling
public class DropzoneApplication {

	public static void main(String[] args) {

        SpringApplication.run(DropzoneApplication.class, args);
	}

}
