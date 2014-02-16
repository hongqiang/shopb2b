package com.hongqiang.shop.modules.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.hongqiang.shop.common.utils.DateUtils;
import com.hongqiang.shop.common.utils.Setting;
import com.hongqiang.shop.common.utils.SettingUtils;

//购物车
@Entity
@Table(name="hq_cart")
public class Cart extends BaseEntity
{
  private static final long serialVersionUID = -6565967051825794561L;
  public static final int TIMEOUT = 604800;
  public static final Integer MAX_PRODUCT_COUNT = Integer.valueOf(100);
  public static final String ID_COOKIE_NAME = "cartId";
  public static final String KEY_COOKIE_NAME = "cartKey";
  private String key;
  private Member member;
  private Set<CartItem> cartItems = new HashSet<CartItem>();

  @Column(name="cart_key", nullable=false, updatable=false)
  public String getKey()
  {
    return this.key;
  }

  public void setKey(String key)
  {
    this.key = key;
  }

  @OneToOne(fetch=FetchType.LAZY)
  public Member getMember()
  {
    return this.member;
  }

  public void setMember(Member member)
  {
    this.member = member;
  }

  @OneToMany(mappedBy="cart", fetch=FetchType.LAZY, cascade={javax.persistence.CascadeType.REMOVE})
  public Set<CartItem> getCartItems()
  {
    return this.cartItems;
  }

  public void setCartItems(Set<CartItem> cartItems)
  {
    this.cartItems = cartItems;
  }

  @Transient
  public int getPoint()
  {
    int i = 0;
    Object localObject;
    if (getCartItems() != null)
    {
      Iterator<CartItem> localIterator = getCartItems().iterator();
      while (localIterator.hasNext())
      {
        localObject = (CartItem)localIterator.next();
        if (localObject == null)
          continue;
        i = (int)(i + ((CartItem)localObject).getPoint());
      }
    }
    Iterator<Promotion> localIterator = getPromotions().iterator();
    while (localIterator.hasNext())
    {
      localObject = (Promotion)localIterator.next();
      i = ((Promotion)localObject).calculatePoint(Integer.valueOf(i)).intValue();
    }
    return i;
  }

  @Transient
  public int getWeight()
  {
    int i = 0;
    if (getCartItems() != null)
    {
      Iterator<CartItem> localIterator = getCartItems().iterator();
      while (localIterator.hasNext())
      {
        CartItem localCartItem = (CartItem)localIterator.next();
        if (localCartItem == null)
          continue;
        i += localCartItem.getWeight();
      }
    }
    return i;
  }

  @Transient
  public int getQuantity()
  {
    int i = 0;
    if (getCartItems() != null)
    {
      Iterator<CartItem> localIterator = getCartItems().iterator();
      while (localIterator.hasNext())
      {
        CartItem localCartItem = (CartItem)localIterator.next();
        if ((localCartItem == null) || (localCartItem.getQuantity() == null))
          continue;
        i += localCartItem.getQuantity().intValue();
      }
    }
    return i;
  }

  @Transient
  public BigDecimal getPrice()
  {
    BigDecimal localBigDecimal = new BigDecimal(0);
    if (getCartItems() != null)
    {
      Iterator<CartItem> localIterator = getCartItems().iterator();
      while (localIterator.hasNext())
      {
        CartItem localCartItem = (CartItem)localIterator.next();
        if ((localCartItem == null) || (localCartItem.getSubtotal() == null))
          continue;
        localBigDecimal = localBigDecimal.add(localCartItem.getSubtotal());
      }
    }
    return localBigDecimal;
  }

  @Transient
  public BigDecimal getAmount()
  {
    Setting localSetting = SettingUtils.get();
    BigDecimal localBigDecimal = getPrice();
    Iterator<Promotion> localIterator = getPromotions().iterator();
    while (localIterator.hasNext())
    {
      Promotion localPromotion = (Promotion)localIterator.next();
      localBigDecimal = localPromotion.calculatePrice(localBigDecimal);
    }
    return localSetting.setScale(localBigDecimal);
  }

  @Transient
  public BigDecimal getDiscount()
  {
    BigDecimal localBigDecimal = getPrice().subtract(getAmount());
    return localBigDecimal.compareTo(new BigDecimal(0)) > 0 ? localBigDecimal : new BigDecimal(0);
  }

  @Transient
  public Set<GiftItem> getGiftItems()
  {
    HashSet<GiftItem> localHashSet = new HashSet<GiftItem>();
    Iterator<Promotion> localIterator1 = getPromotions().iterator();
    while (localIterator1.hasNext())
    {
      Promotion localPromotion = (Promotion)localIterator1.next();
      if (localPromotion.getGiftItems() == null)
        continue;
      Iterator<GiftItem> localIterator2 = localPromotion.getGiftItems().iterator();
      while (localIterator2.hasNext())
      {
    	GiftItem localGiftItem = null;
        GiftItem localGiftItem1 = (GiftItem)localIterator2.next();
        for (GiftItem giftItem : localHashSet) {
        	if (giftItem.getGift().equals(localGiftItem1.getGift())) {			
				localGiftItem = giftItem;
				break;
			}
		}
        if (localGiftItem != null) {
        	localGiftItem.setQuantity(Integer.valueOf(localGiftItem.getQuantity().intValue() + localGiftItem1.getQuantity().intValue()));
		}
        else {
        	localHashSet.add(localGiftItem1);
		}
      }
    }
    return localHashSet;
  }

  @Transient
  public Set<Promotion> getPromotions()
  {
    HashSet<Promotion> localHashSet = new HashSet<Promotion>();
    if (getCartItems() != null)
    {
    Iterator<CartItem> localObject2 = getCartItems().iterator();
      while (localObject2.hasNext())
      {
    	  CartItem localObject1 = (CartItem)(localObject2).next();
        if ((localObject1 == null) || (((CartItem)localObject1).getProduct() == null))
          continue;
        localHashSet.addAll(((CartItem)localObject1).getProduct().getValidPromotions());
      }
    }
    Set<Promotion> localObject1 = new TreeSet<Promotion>();
    Iterator<Promotion> localIterator = localHashSet.iterator();
    while (localIterator.hasNext())
    {
    	Promotion localObject2 = (Promotion)localIterator.next();
      if (!key(localObject2))
        continue;
      localObject1.add(localObject2);
    }
    return localObject1;
  }

  @Transient
  private boolean key(Promotion paramPromotion)
  {
    if ((paramPromotion == null) || (!paramPromotion.hasBegun()) || (paramPromotion.hasEnded()))
      return false;
    if ((paramPromotion.getMemberRanks() == null) || (getMember() == null) || (getMember().getMemberRank() == null) || (!paramPromotion.getMemberRanks().contains(getMember().getMemberRank())))
      return false;
    BigDecimal localBigDecimal = new BigDecimal(0);
    if (getCartItems() != null)
    {
      Iterator<CartItem> localIterator = getCartItems().iterator();
      while (localIterator.hasNext())
      {
        CartItem localCartItem = (CartItem)localIterator.next();
        if (localCartItem == null)
          continue;
        Product localProduct = localCartItem.getProduct();
        if (localProduct == null)
          continue;
        if ((localProduct.getPromotions() != null) && (localProduct.getPromotions().contains(paramPromotion)))
        {
          localBigDecimal = localBigDecimal.add(localCartItem.getSubtotal());
        }
        else if ((localProduct.getProductCategory() != null) && (localProduct.getProductCategory().getPromotions().contains(paramPromotion)))
        {
          localBigDecimal = localBigDecimal.add(localCartItem.getSubtotal());
        }
        else
        {
          if ((localProduct.getBrand() == null) || (!localProduct.getBrand().getPromotions().contains(paramPromotion)))
            continue;
          localBigDecimal = localBigDecimal.add(localCartItem.getSubtotal());
        }
      }
    }
    return ((paramPromotion.getStartPrice() == null) || (paramPromotion.getStartPrice().compareTo(localBigDecimal) <= 0)) && ((paramPromotion.getEndPrice() == null) || (paramPromotion.getEndPrice().compareTo(localBigDecimal) >= 0));
  }

  @Transient
  public boolean isValid(Coupon coupon)
  {
    if ((coupon == null) || (!coupon.getIsEnabled().booleanValue()) || (!coupon.hasBegun()) || (coupon.hasExpired()))
      return false;
    return ((coupon.getStartPrice() == null) || (coupon.getStartPrice().compareTo(getAmount()) <= 0)) && ((coupon.getEndPrice() == null) || (coupon.getEndPrice().compareTo(getAmount()) >= 0));
  }

  @Transient
  public CartItem getCartItem(Product product)
  {
    if ((product != null) && (getCartItems() != null))
    {
      Iterator<CartItem> localIterator = getCartItems().iterator();
      while (localIterator.hasNext())
      {
        CartItem localCartItem = (CartItem)localIterator.next();
        if ((localCartItem != null) && (localCartItem.getProduct() == product))
          return localCartItem;
      }
    }
    return null;
  }

  @Transient
  public boolean contains(Product product)
  {
    if ((product != null) && (getCartItems() != null))
    {
      Iterator<CartItem> localIterator = getCartItems().iterator();
      while (localIterator.hasNext())
      {
        CartItem localCartItem = (CartItem)localIterator.next();
        if ((localCartItem != null) && (localCartItem.getProduct() == product))
          return true;
      }
    }
    return false;
  }

  @Transient
  public String getToken()
  {
    HashCodeBuilder localHashCodeBuilder = new HashCodeBuilder(17, 37).append(getKey());
    if (getCartItems() != null)
    {
      Iterator<CartItem> localIterator = getCartItems().iterator();
      while (localIterator.hasNext())
      {
        CartItem localCartItem = (CartItem)localIterator.next();
        localHashCodeBuilder.append(localCartItem.getProduct()).append(localCartItem.getQuantity()).append(localCartItem.getUnitPrice());
      }
    }
    return DigestUtils.md5Hex(String.valueOf(localHashCodeBuilder.toHashCode()));
  }

  @Transient
  public boolean getIsLowStock()
  {
    if (getCartItems() != null)
    {
      Iterator<CartItem> localIterator = getCartItems().iterator();
      while (localIterator.hasNext())
      {
        CartItem localCartItem = (CartItem)localIterator.next();
        if ((localCartItem != null) && (localCartItem.getIsLowStock()))
          return true;
      }
    }
    return false;
  }

  @Transient
  public boolean hasExpired()
  {
    return new Date().after(DateUtils.addSeconds(getUpdateDate(), 604800));
  }

  @Transient
  public boolean isCouponAllowed()
  {
    Iterator<Promotion> localIterator = getPromotions().iterator();
    while (localIterator.hasNext())
    {
      Promotion localPromotion = (Promotion)localIterator.next();
      if ((localPromotion != null) && (!localPromotion.getIsCouponAllowed().booleanValue()))
        return false;
    }
    return true;
  }

  @Transient
  public boolean isEmpty()
  {
    return (getCartItems() == null) || (getCartItems().isEmpty());
  }
}