package com.hongqiang.shop.common.utils;

import java.math.BigDecimal;

import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.builtin.NumericFieldBridge;

public class BigDecimalNumericFieldBridge extends NumericFieldBridge{
	  public Object get(String name, Document document)
	  {
	    return new BigDecimal(document.getFieldable(name).stringValue());
	  }

	  public void set(String name, Object value, Document document, LuceneOptions luceneOptions)
	  {
	    if (value != null)
	    {
	      BigDecimal localBigDecimal = (BigDecimal)value;
	      luceneOptions.addNumericFieldToDocument(name, Double.valueOf(localBigDecimal.doubleValue()), document);
	    }
	  }
}
