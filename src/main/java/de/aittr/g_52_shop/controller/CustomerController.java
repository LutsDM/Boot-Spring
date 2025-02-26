package de.aittr.g_52_shop.controller;

import de.aittr.g_52_shop.domain.dto.CustomerDto;
import de.aittr.g_52_shop.domain.dto.ProductDto;
import de.aittr.g_52_shop.domain.entity.Customer;
import de.aittr.g_52_shop.domain.entity.Product;
import de.aittr.g_52_shop.service.interfaces.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping("/customer")
@Tag(name = "Customer controller", description = "Controller for various operations with Customer")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    // Сохранить покупателя в базе данных (при сохранении покупатель автоматически считается активным).
    @PostMapping
    public CustomerDto save(@RequestBody
                            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Instance of a Product")
                            CustomerDto customer) {
        return service.save(customer);
    }

    // Вернуть всех покупателей из базы данных (активных).
    @GetMapping("/all")
    @Operation(
            summary = "Get all customer",
            description = "Getting all customer that exist in the database"
    )
    public List<CustomerDto> getAll() {
        return service.getAllActiveCustomers();
    }

    // Вернуть одного покупателя из базы данных по его идентификатору (если он активен).
    @GetMapping("/{id}")
    public CustomerDto getById(@PathVariable
                            @Parameter(description = "Customer unique identifier")
                            Long id
    ) {
        return service.getById(id);
    }

    //Изменить одного покупателя в базе данных по его идентификатору.
    @PutMapping
    public void update(@RequestBody CustomerDto customer) {
        service.update(customer);
    }

    //Удалить покупателя из базы данных по его идентификатору.
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        service.deleteById(id);
    }

    // Удалить покупателя из базы данных по его имени.
    @DeleteMapping("/by-name/{name}")
    public void deleteByName(@PathVariable String name) {
        service.deleteByName(name);
    }

    //Восстановить удалённого покупателя в базе данных по его идентификатору.
    @PutMapping("/{id}/restore")
    public void restoreById(@PathVariable Long id) {
        service.restoreById(id);
    }

    //Вернуть общее количество покупателей в базе данных (активных).
    @GetMapping("/count")
    public long getCustomerCount() {
        return service.getAllActiveCustomerTotalCount();
    }

    //Вернуть стоимость корзины покупателя по его идентификатору (если он активен).
    @GetMapping("{id}/cart/total-cost")
    public BigDecimal getTotalCostById(@PathVariable Long id) {
        return service.getTotalPriceInCartByProductId(id);
    }

    //Вернуть среднюю стоимость продукта в корзине покупателя по его идентификатору (если он активен)
    @GetMapping("{id}/cart/avg-cost")
    public BigDecimal getAvgCostById() {
        return service.getAveragePriceInCartByProductId();
    }

    //Добавить товар в корзину покупателя по их идентификаторам (если оба активны)
    @PostMapping("{customerId}/cart/product/{productId}")
    public Product addProductToCart(@PathVariable Long customerId, @PathVariable Long productId) {
        return service.saveProductByProductIdInCartByCustomerId(productId, customerId);
    }

    //Удалить товар из корзины покупателя по их идентификаторам
    @DeleteMapping("{customerId}/cart/product/{productId}")
    public void deleteProductFromCartByIdCustomer(@PathVariable Long customerId, @PathVariable Long productId) {
        service.deleteProductByProductIdFromCartByCustomerId(productId, customerId);

    }

    //Полностью очистить корзину покупателя по его идентификатору (если он активен)*/
    @DeleteMapping("{id}/cart/product/all")
    public void deleteProductFromCartByIdCustomer(@PathVariable Long id) {
        service.deleteAllFromCartByCustomerId(id);
    }

}