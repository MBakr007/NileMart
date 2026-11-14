package org.route.nilemartspringboot.mapper;

import org.mapstruct.Mapper;
import org.route.nilemartspringboot.dto.product.AddProductRequest;
import org.route.nilemartspringboot.dto.product.ProductResponse;
import org.route.nilemartspringboot.model.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductResponse toProductResponse(Product product);

    Product toProduct(AddProductRequest request);
}
