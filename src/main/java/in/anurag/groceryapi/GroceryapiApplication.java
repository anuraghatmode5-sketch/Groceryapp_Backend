package in.anurag.groceryapi;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootApplication
@EnableMongoAuditing
public class GroceryapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(GroceryapiApplication.class, args);
	}

}