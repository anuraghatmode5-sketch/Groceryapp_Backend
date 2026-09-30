package in.anurag.groceryapi.service;

import in.anurag.groceryapi.dto.AddressDetailsDto;
import in.anurag.groceryapi.dto.AddressListResponse;
import in.anurag.groceryapi.model.Address;
import in.anurag.groceryapi.dto.AddressResponse;
import in.anurag.groceryapi.model.Address;
import in.anurag.groceryapi.model.User;
import in.anurag.groceryapi.repository.AddressRepository;
import in.anurag.groceryapi.repository.UserRepository;
import in.anurag.groceryapi.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public AddressResponse addAddress(String userId, Address addressDto) {

        // Verify user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create Address object from DTO
        Address address = new Address();

        address.setUserId(userId);
        address.setFirstName(addressDto.getFirstName());
        address.setLastName(addressDto.getLastName());
        address.setEmail(addressDto.getEmail());
        address.setStreet(addressDto.getStreet());
        address.setCity(addressDto.getCity());
        address.setState(addressDto.getState());
        address.setZipcode(addressDto.getZipcode());
        address.setCountry(addressDto.getCountry());
        address.setPhone(addressDto.getPhone());

        // Save address to addresses collection
        addressRepository.save(address);

        return new AddressResponse(true, "Address added successfully");
    }

    public AddressListResponse getUserAddresses(String userId) {

        // Get all addresses for the user
        List<Address> addresses = addressRepository.findAllByUserId(userId);

        // Convert to DTOs
        List<AddressDetailsDto> addressDtos = addresses.stream()
                .map(address -> {
                    AddressDetailsDto dto = new AddressDetailsDto();

                    dto.setId(address.getId());
                    dto.setUserId(address.getUserId());
                    dto.setFirstName(address.getFirstName());
                    dto.setLastName(address.getLastName());
                    dto.setEmail(address.getEmail());
                    dto.setStreet(address.getStreet());
                    dto.setCity(address.getCity());
                    dto.setState(address.getState());
                    dto.setZipcode(address.getZipcode());
                    dto.setCountry(address.getCountry());
                    dto.setPhone(address.getPhone());

                    return dto;
                })
                .collect(Collectors.toList());

        return new AddressListResponse(true, addressDtos);
    }
}