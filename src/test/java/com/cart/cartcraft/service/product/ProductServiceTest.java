package com.cart.cartcraft.service.product;

import com.cart.cartcraft.dto.ProductDto;
import com.cart.cartcraft.exception.AlreadyExistsException;
import com.cart.cartcraft.exception.ResourceNotFoundException;
import com.cart.cartcraft.model.Category;
import com.cart.cartcraft.model.Product;
import com.cart.cartcraft.repository.CategoryRepository;
import com.cart.cartcraft.repository.ImageRepository;
import com.cart.cartcraft.repository.ProductRepository;
import com.cart.cartcraft.request.AddProductRequest;
import com.cart.cartcraft.request.ProductUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private Category category;
    private AddProductRequest addProductRequest;
    private ProductUpdateRequest productUpdateRequest;

    @BeforeEach
    void setUp() {
        category = new Category("Electronics");
        category.setId(1L);

        product = new Product("Smartphone", "BrandX", new BigDecimal("1000.00"), 10, "Description", category);
        product.setId(1L);

        addProductRequest = new AddProductRequest();
        addProductRequest.setName("Smartphone");
        addProductRequest.setBrand("BrandX");
        addProductRequest.setPrice(new BigDecimal("1000.00"));
        addProductRequest.setQuantity(10);
        addProductRequest.setDescription("Description");
        addProductRequest.setCategory(category);

        productUpdateRequest = new ProductUpdateRequest();
        productUpdateRequest.setName("Smartphone Updated");
        productUpdateRequest.setBrand("BrandX");
        productUpdateRequest.setPrice(new BigDecimal("1200.00"));
        productUpdateRequest.setQuantity(15);
        productUpdateRequest.setDescription("Updated Description");
        productUpdateRequest.setCategory(category);
    }

    @Test
    void addProduct_Success() {
        when(productRepository.existsByNameAndBrand(addProductRequest.getName(), addProductRequest.getBrand())).thenReturn(false);
        when(categoryRepository.findByName(addProductRequest.getCategory().getName())).thenReturn(category);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product createdProduct = productService.addProduct(addProductRequest);

        assertNotNull(createdProduct);
        assertEquals(product.getName(), createdProduct.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void addProduct_AlreadyExists() {
        when(productRepository.existsByNameAndBrand(addProductRequest.getName(), addProductRequest.getBrand())).thenReturn(true);

        assertThrows(AlreadyExistsException.class, () -> productService.addProduct(addProductRequest));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void getProductById_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product foundProduct = productService.getProductById(1L);

        assertNotNull(foundProduct);
        assertEquals(product.getId(), foundProduct.getId());
    }

    @Test
    void getProductById_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(1L));
    }

    @Test
    void deleteProduct_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).delete(product);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).delete(product);
    }

    @Test
    void deleteProduct_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.deleteProduct(1L));
        verify(productRepository, never()).delete(any(Product.class));
    }

    @Test
    void updateProduct_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(categoryRepository.findByName(productUpdateRequest.getCategory().getName())).thenReturn(category);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product updatedProduct = productService.updateProduct(productUpdateRequest, 1L);

        assertNotNull(updatedProduct);
        assertEquals(productUpdateRequest.getName(), updatedProduct.getName());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void updateProduct_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.updateProduct(productUpdateRequest, 1L));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void getAllProduct_Success() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(product));

        List<Product> products = productService.getAllProduct();

        assertNotNull(products);
        assertEquals(1, products.size());
    }
}
