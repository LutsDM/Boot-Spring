package de.aittr.g_52_shop.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.Objects;

@Schema(description = "Class that describes Product DTO")
public class ProductDto {

    @Schema(
            description = "Product unique identifier",
            example = "777",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Schema(description = "Product title", example = "Banana")
    private String title;

    @Schema(description = "Product price", example = "190.00")
    private BigDecimal price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProductDto that = (ProductDto) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, price);
    }

    @Override
    public String toString() {
        return String.format("Продукт: ИД - %d, наименование - %s, цена - %.2f.",
                id, title, price);
    }
}
