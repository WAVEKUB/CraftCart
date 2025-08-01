package com.cart.cartcraft.service.product;

import com.cart.cartcraft.dto.ProductDto;
import com.cart.cartcraft.model.Product;
import com.cart.cartcraft.request.AddProductRequest;
import com.cart.cartcraft.request.ProductUpdateRequest;

import java.util.List;

public interface IProductService {
     Product addProduct(AddProductRequest product);
     Product getProductById(Long id);
     void deleteProduct(Long id);
     Product updateProduct(ProductUpdateRequest existingProduct, Long productId);

     List<Product> getAllProduct();
     List<Product> getAllProductByCategory(String category);
     List<Product> getAllProductByBrand(String brand);
     List<Product> getAllProductByName(String name);
     List<Product> getAllProductByBrandAndName(String brand, String name);
     Long countProductByBrandAndName(String brand, String name);

     List<ProductDto> getConvertedProducts(List<Product> products);

     ProductDto covertToDto(Product product);
}
