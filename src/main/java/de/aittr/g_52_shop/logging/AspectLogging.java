package de.aittr.g_52_shop.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

// Аннотация @Aspect определяет, что этот класс является аспектом,
// то есть классом, который содержит адвайсы.
// Адвайсы - это методы, код которых внедряется в исходные методы приложения.
// Аннотация @Component говорит Спрингу о том, что нужно создать объект этого
// класса и сделать его бином, поместив в Спринг-контекст.
@Aspect
@Component
public class AspectLogging {

    private Logger logger = LoggerFactory.getLogger(AspectLogging.class);

    // Аннотация @Pointcut служит для задания среза - то есть правил, описывающих,
    // куда именно будет внедряться дополнительный код
    @Pointcut("execution(* de.aittr.g_52_shop.service.ProductServiceImpl.save(de.aittr.g_52_shop.domain.dto.ProductDto))")
    public void saveProduct() {}

    // Аннотация @Before говорит о том, что этот метод является before-адвайсом,
    // то есть он будет отрабатывать до основного кода
    // При этом при помощи "saveProduct()" мы указываем, к какому именно
    // поинт-кату мы привязываем этот адвайс.
    // joinPoint - это специальный объект, который создаётся фреймворком, и в него
    // закладывается вся информация о вызванном целевом методом
    @Before("saveProduct()")
    public void beforeSavingProduct(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        logger.info("Method save of the class ProductServiceImpl called with argument {}", args[0]);
    }

    @After("saveProduct()")
    public void afterSavingProduct() {
        logger.info("Method save of the class ProductServiceImpl finished its work");
    }

    @Pointcut("execution(* de.aittr.g_52_shop.service.ProductServiceImpl.getById(Long))")
    public void getProductById() {}

    @AfterReturning(
            pointcut = "getProductById()",
            returning = "result"
    )
    public void afterReturningProductById(Object result) {
        logger.info("Method getById of the ProductServiceImpl successfully returned product: {}", result);
    }

    @AfterThrowing(
            pointcut = "getProductById()",
            throwing = "e"
    )
    public void afterThrowingExceptionWhileGettingProduct(Exception e) {
        logger.warn("Method getById of the ProductServiceImpl threw an exception: {}", e.getMessage());
    }
}
