package de.aittr.g_52_shop.domain.dto;


import de.aittr.g_52_shop.domain.entity.Customer;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.Objects;

@Schema(description = "Class that describes Customer DTO")
public class CustomerDto {

    @Schema(
            description = "Customer unique identifier",
            example = "100",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Schema(description = "Customer name", example = "Dmytro Luts")
    private String name;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CustomerDto that = (CustomerDto) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return String.format("Покупатель: ИД - %d, имя - %s, активен - %s.",
                id, name);
    }



}
