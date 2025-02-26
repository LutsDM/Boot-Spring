package de.aittr.g_52_shop.service.mapping;

import de.aittr.g_52_shop.domain.dto.ProductDto;
import de.aittr.g_52_shop.domain.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMappingService {

    ProductDto mapEntityToDto(Product entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", constant = "true")
    Product mapDtoToEntity(ProductDto dto);

    // Вариант ручного кода:
//    public ProductDto mapEntityToDto(Product entity) {
//        ProductDto dto = new ProductDto();
//        dto.setId(entity.getId());
//        dto.setTitle(entity.getTitle());
//        dto.setPrice(entity.getPrice());
//        return dto;
//    }
//
//    public Product mapDtoToEntity(ProductDto dto) {
//        Product entity = new Product();
//        entity.setId(dto.getId());
//        entity.setTitle(dto.getTitle());
//        entity.setPrice(dto.getPrice());
//        entity.setActive(true);
//        return entity;
//    }
}
