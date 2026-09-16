package top.productivitytools.fitness.exercises.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.flyway.autoconfigure.FlywayAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, FlywayAutoConfiguration.class})
public class ExercisesApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExercisesApiApplication.class, args);
	}

}
