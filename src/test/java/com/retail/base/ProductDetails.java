package com.retail.base;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductDetails {
   private Map<String, List<Product>> productDetails;
}
