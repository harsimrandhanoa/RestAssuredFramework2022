package com.retail.base;

import java.util.List;
import java.util.Map;

public class ProductPojo {

	Map<String, List<Product>> productDetails;

	public Map<String, List<Product>> getProductDetails() {
		return productDetails;
	}

	public void setProductDetails(Map<String, List<Product>> productDetails) {
		this.productDetails = productDetails;
	}

}
