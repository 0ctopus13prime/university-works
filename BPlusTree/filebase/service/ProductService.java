package fakecoupangsystem.service;

import fakecoupangsystem.domain.Product;

/**
 * Created by buzz on 2015. 12. 26..
 */
public interface ProductService {
	Product getProduct(int id);
	Product newProduct(Product product);
}
