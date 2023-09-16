package org.lesnoy.product;

import org.lesnoy.exeptions.WebApiExeption;

import java.util.List;

public class ProductService {

    private final ProductWebService webService = new ProductWebService();
    public List<ProductDTO> findAllProductsByType(int ordinal) throws WebApiExeption {
        return webService.findAllProductsByType(ordinal);
    }

    public List<ProductDTO> findAllProductsByOwnerAndType(String userName, int ordinal) throws WebApiExeption {
        return webService.findAllProductsByOwnerAndType(userName, ordinal);
    }
}
