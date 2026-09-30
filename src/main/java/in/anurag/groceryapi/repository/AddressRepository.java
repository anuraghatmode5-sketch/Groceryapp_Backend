package in.anurag.groceryapi.repository;

import in.anurag.groceryapi.model.Address;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends MongoRepository<Address, String> {

    Optional<Address> findByUserId(String userId);
    List<Address> findAllByUserId(String userId);
}