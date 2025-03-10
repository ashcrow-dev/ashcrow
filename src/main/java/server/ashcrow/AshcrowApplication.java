package server.ashcrow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class AshcrowApplication {
	public static void main(String[] args) {
		SpringApplication.run(AshcrowApplication.class, args);
	}
}
