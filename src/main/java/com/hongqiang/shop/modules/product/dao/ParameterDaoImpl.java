package com.hongqiang.shop.modules.product.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.hongqiang.shop.common.base.persistence.BaseDaoImpl;
import com.hongqiang.shop.modules.entity.Parameter;
import com.hongqiang.shop.modules.entity.ParameterGroup;

@Repository
public class ParameterDaoImpl extends BaseDaoImpl<Parameter,Long>
  implements ParameterDaoCustom
{
  public List<Parameter> findList(ParameterGroup parameterGroup, Set<Parameter> excludes)
  {
	  String sqlString = "select parameter from Parameter parameter where 1=1 ";
	  List<Object> params = new ArrayList<Object>();
	  if (parameterGroup != null){
		  sqlString += " and parameter.parameterGroup = ? ";
		  params.add(parameterGroup);
	  }
	  if ((excludes != null) && (!excludes.isEmpty())){
//			for (Parameter op : excludes) {
//				params.add(op.getId().intValue());
//			}
			sqlString += " and parameter not in (";
			for (Parameter parameter : excludes) {
					sqlString += " ?, ";
					params.add(parameter);
			}
			sqlString = sqlString.substring(0,sqlString.length()-2);
			sqlString +=")";
//		  sqlString += " and parameter.id not in (?)";
////		  params.add(excludes);
	  }
	  return super.findList(sqlString, params, null, null, null, null);
  }
}