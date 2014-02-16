package com.hongqiang.shop.modules.content.service;

import java.util.List;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.entity.Product;
import com.hongqiang.shop.modules.entity.Review;

public interface ReviewService {
	public List<Review> findList(Member member, Long productId,
			Review.Type type, Boolean isShow, Integer count,
			List<Filter> filters, List<Order> orders);
	
	public List<Review> findList(Member member, Long productId,
			Review.Type type, Boolean isShow, Integer count,
			List<Filter> filters, List<Order> orders, String cacheRegion);
	
	public List<Review> findList(Member member, Product product,
			Review.Type type, Boolean isShow, Integer count,
			List<Filter> filters, List<Order> orders);

	public List<Review> findList(Member member, Product product,
			Review.Type type, Boolean isShow, Integer count,
			List<Filter> filters, List<Order> orders, String cacheRegion);

	public Page<Review> findPage(Member member, Product product,
			Review.Type type, Boolean isShow, Pageable pageable);

	public Long count(Member member, Product product, Review.Type type,
			Boolean isShow);

	public boolean isReviewed(Member member, Product product);

	public Review find(Long id);

	public void save(Review review);

	public Review update(Review review);

	public Review update(Review review, String[] ignoreProperties);

	public void delete(Long id);

	public void delete(Long[] ids);

	public void delete(Review review);
}