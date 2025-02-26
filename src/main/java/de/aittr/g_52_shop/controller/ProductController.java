package de.aittr.g_52_shop.controller;

import de.aittr.g_52_shop.domain.dto.ProductDto;
import de.aittr.g_52_shop.service.interfaces.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/*
Аннотация @RestController позволяет Спрингу понять, что этот класс
является именно РЕСТ-контроллером. А также благодаря этой аннотации
на старте приложения Спринг сам создаст объект этого класса
и поместит его в Спринг-контекст. Джава-объекты, которые создаются
Спрингом и хранятся в Спринг-контексте называются Спринг-бинами.

Аннотация @RequestMapping со значением атрибута "/products" говорит
Спрингу о том, что все http-запросы, которые пришли на энд-поинт /products
нужно адресовать именно этому контроллеру.
Когда на наше приложение придёт http-запрос на /products, Спринг будет
сам вызывать нужные методы у этого контроллера.
IoC - Inversion of Control (инверсия контроля) - этот принцип говорит о том,
что мы только пишем методы, а управление этими методами ложится на плечи фреймворка.
Мы только пишем методы. А фреймворк создаёт сам нужные объекты наших классов
и сам в нужный момент вызывает их методы.
 */
@RestController
@RequestMapping("/products")
@Tag(name = "Product controller", description = "Controller for various operations with Products")
public class ProductController {

    // Это поле будет содержать объект сервиса продуктов,
    // чтобы мы могли в коде контроллера вызывать его методы.
    private final ProductService service;

    /*
    При старте приложения Спринг будет создавать объект данного контроллера.
    При создании объекта контроллера Спринг вызовет этот конструктор, потому
    что других вариантов создать объект больше нет, конструктор у нас один.
    Этот конструктор требует на вход объект сервиса продуктов.
    Поэтому Спринг обратится к Спринг контексту, найдёт там объект сервиса
    и передаст его в это поле.
    Поэтому, чтобы всё отработало, нам нужно обеспечить, чтобы объект сервиса
    вообще был в этом контексте.
     */
    public ProductController(ProductService service) {
        this.service = service;
    }

    // Разработаем REST-API для нашего приложения.
    // Разработать REST-API - это значит определить, на какие энд-поинты
    // должен обращаться клиент, чтобы выполнить те или иные операции.

//    Сохранить продукт в базе данных (при сохранении продукт автоматически считается активным).
    // POST -> http://12.34.56.78:8080/products (продукт передаётся в теле в виде JSON)

    /*
    Аннотация @PostMapping говорит Спрингу о том, что когда для данного контроллера
    придёт POST-запрос на ресурс /products, то нужно вызвать именно этот метод.
    Аннотация @RequestBody говорит Спрингу о том, что он должен прочитать JSON, который
    пришёл в теле запроса, этот JSON преобразовать в Джава-объект при помощи
    встроенного Jackson, и получившийся Джава-объект передать в параметр product.
     */
    @PostMapping
    public ProductDto save(
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Instance of a Product")
            ProductDto product
    ) {
        return service.save(product);
    }

    //    Вернуть все продукты из базы данных (активные).
    // GET -> http://12.34.56.78:8080/products/all
    @GetMapping("/all")
    @Operation(
            summary = "Get all products",
            description = "Getting all products that exist in the database"
    )
    public List<ProductDto> getAll() {
        return service.getAllActiveProducts();
    }

    //    Вернуть один продукт из базы данных по его идентификатору (если он активен).
    // GET -> http://12.34.56.78:8080/products?id=5 - вариант при помощи указания параметра
    // GET -> http://12.34.56.78:8080/products/5 - вариант при помощи подстроки запроса
    /*
    Аннотация @PathVariable говорит Спрингу о том, что значение для этого параметра
    нужно получить из самой строки запроса. А из какой именно части нужно получить
    это значение - на это указывает атрибут "/{id}" аннотации @GetMapping
     */
//    @GetMapping("/{id}/test/{title}") - пример с несколькими параметрами
    @GetMapping("/{id}")
    public ProductDto getById(
            @PathVariable
            @Parameter(description = "Product unique identifier")
            Long id
    ) {
        return service.getById(id);
    }

    //    Изменить один продукт в базе данных по его идентификатору.
    // PUT -> http://12.34.56.78:8080/products (идентификатор будем отправлять в теле)
    @PutMapping
    public void update(@RequestBody ProductDto product) {
        service.update(product);
    }

    //    Удалить продукт из базы данных по его идентификатору.
    // DELETE -> http://12.34.56.78:8080/products/5
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        service.deleteById(id);
    }

    //    Удалить продукт из базы данных по его наименованию.
    // DELETE -> http://12.34.56.78:8080/products/by-title/banana - вариант 1
    @DeleteMapping("/by-title/{title}")
    public void deleteByTitle(@PathVariable String title) {
        service.deleteByTitle(title);
    }

    // DELETE -> http://12.34.56.78:8080/products?title=banana - вариант 2
    // При таком варианте мы бы использовали аннотацию @RequestParam

    //    Восстановить удалённый продукт в базе данных по его идентификатору.
    // PUT -> http://12.34.56.78:8080/products/restore/5
    @PutMapping("/{id}")
    public void restoreById(@PathVariable Long id) {
        service.restoreById(id);
    }

    //    Вернуть общее количество продуктов в базе данных (активных).
    // GET -> http://12.34.56.78:8080/products/quantity
    @GetMapping("/quantity")
    public long getProductsQuantity() {
        return service.getAllActiveProductsCount();
    }

    //    Вернуть суммарную стоимость всех продуктов в базе данных (активных).
    // GET -> http://12.34.56.78:8080/products/total-cost
    @GetMapping("/total-cost")
    public BigDecimal getProductsTotalCost() {
        return service.getAllActiveProductsTotalCost();
    }

    //    Вернуть среднюю стоимость продукта в базе данных (из активных).
    // GET -> http://12.34.56.78:8080/products/avg-price
    @GetMapping("/avg-price")
    public BigDecimal getProductAveragePrice() {
        return service.getAllActiveProductsAveragePrice();
    }
}
