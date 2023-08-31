package ru.researchser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import ru.researchser.parserApplication.configs.ParserApplicationContextConfiguration;

@SpringBootApplication
@Import(ParserApplicationContextConfiguration.class)
public class ResearchserApplication {
	public static void main(String[] args) {
		SpringApplication.run(ResearchserApplication.class, args);
	}
}
