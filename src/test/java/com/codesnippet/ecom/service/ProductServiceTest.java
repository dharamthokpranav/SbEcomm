package com.codesnippet.ecom.service;

import com.codesnippet.ecom.Entity.Product;
import com.codesnippet.ecom.Repository.ProductRepository;
import com.codesnippet.ecom.Service.ProductService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductService productService;

    private static Product product = null;

    @BeforeAll
    public static void init(){
        System.out.println("Product creation");
        product= new Product();
        product.setId(1);
        product.setName("book");
        product.setDescription("Pirates");
        product.setPrice(25000);

    }
    @Test
    void deleteProductTest(){
        System.out.println("deleteProduct should delete product successfully");
        doNothing().when(productRepository).deleteById(1);
        productService.deleteProduct(1);
        verify(productRepository, times(1)).deleteById(1);

    }

    @Test
    void testPrivateMethod_ValidatteProductName() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method validateProductMethod= ProductService.class.getDeclaredMethod("validateProductName", String.class);
        validateProductMethod.setAccessible(true);
        boolean isSuccess = (boolean) validateProductMethod.invoke(productService, "Book");
        assertTrue(isSuccess);

    }
    @Test
    void addProductTest(){
        System.out.println("Add product should add product");
        when(productRepository.save(product)).thenReturn(product);

        Product addedProduct=productService.addProduct(product);

        assertEquals(product.getId(), addedProduct.getId());
        assertEquals(product.getPrice(), addedProduct.getPrice());
        assertNotNull(addedProduct);
        assertTrue(product.getId()==1);

    }


    @Test
    void addProductTestException(){

        System.out.println("Add product should add product");

        product.setName("");
        RuntimeException runtimeException=assertThrows(RuntimeException.class, ()->{
            productService.addProduct(product);
        });

        assertEquals("Invalid Name Of Product",runtimeException.getMessage());
        verify(productRepository,never()).save(any(Product.class));

    }
}
