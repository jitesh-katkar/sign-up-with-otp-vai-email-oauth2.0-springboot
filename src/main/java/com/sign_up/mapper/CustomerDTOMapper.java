package com.sign_up.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.sign_up.entity.Customer;
import com.sign_up.response.CustomerDetailsRepsonseDTO;

public class CustomerDTOMapper {
	
	public static CustomerDetailsRepsonseDTO toDTO(Customer entity) {
        if (entity == null) {
            return null;
        }

        CustomerDetailsRepsonseDTO dto = new CustomerDetailsRepsonseDTO();
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setEmailId(entity.getEmailId());
        return dto;
    }
	
	
	 public static List<CustomerDetailsRepsonseDTO> toDTOList(List<Customer> entities) {
	        if (entities == null || entities.isEmpty()) {
	            return List.of();
	        }

	        return entities.stream()
	                       .map(CustomerDTOMapper::toDTO)
	                       .collect(Collectors.toList());
	    }

}
