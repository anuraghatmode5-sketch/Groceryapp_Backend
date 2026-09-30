package in.anurag.groceryapi.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Override
    protected String getDatabaseName() {
        return "Groceryapp";
    }

    @Bean
    @Override
    public MongoClient mongoClient() {
        return MongoClients.create(System.getenv("MONGODB_URI"));
    }
}