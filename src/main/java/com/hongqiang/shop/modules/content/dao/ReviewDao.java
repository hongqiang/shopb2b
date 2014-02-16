package com.hongqiang.shop.modules.content.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.hongqiang.shop.common.base.persistence.BaseDao;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.entity.Product;
import com.hongqiang.shop.modules.entity.Review;

public interface ReviewDao extends ReviewDaoCustom, CrudRepository<Review, Long> {

}

/**
 * DAO自定义接口
 * 
 * @author Jack
 * 
 */
interface ReviewDaoCustom extends BaseDao<Review,Long> {

   public List<Review> findList(Member member, Product product, Review.Type type, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders);

  public Page<Review> findPage(Member member, Product product, Review.Type type, Boolean isShow, Pageable pageable);

  public Long count(Member member, Product product, Review.Type type, Boolean isShow);

  public boolean isReviewed(Member member, Product product);

  public long calculateTotalScore(Product paramProduct);

  public long calculateScoreCount(Product paramProduct);
}