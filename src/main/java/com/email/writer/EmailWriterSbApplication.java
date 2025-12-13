package com.email.writer;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EmailWriterSbApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		dotenv.entries().forEach(e ->
				System.setProperty(e.getKey(), e.getValue())
		);

		SpringApplication.run(EmailWriterSbApplication.class, args);
	}

}
