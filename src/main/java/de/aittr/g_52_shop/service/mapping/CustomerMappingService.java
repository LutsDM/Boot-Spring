package de.aittr.g_52_shop.service.mapping;

import de.aittr.g_52_shop.domain.dto.CustomerDto;
import de.aittr.g_52_shop.domain.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMappingService {
    CustomerDto mapEntityToDto(Customer entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", constant = "true")
    Customer mapDtoToEntity(CustomerDto dto);
}
