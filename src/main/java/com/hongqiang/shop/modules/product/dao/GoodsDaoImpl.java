package com.hongqiang.shop.modules.product.dao;

import org.springframework.stereotype.Repository;

import com.hongqiang.shop.common.base.persistence.BaseDaoImpl;
import com.hongqiang.shop.modules.entity.Goods;

@Repository
class GoodsDaoImpl extends BaseDaoImpl<Goods,Long> implements GoodsDaoCustom {

}