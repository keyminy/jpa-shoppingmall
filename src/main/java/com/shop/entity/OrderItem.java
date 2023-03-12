package com.shop.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.shop.utils.entity.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class OrderItem extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="order_id")
	private Order order;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="item_id")
	private Item item;

	
	private int orderPrice;//주문가격
	
	private int count;//수량
	
	 /* 주문은 언제 주문했고, 언제 주문 변경이 일어 났는지가 중요하다*/
    //private LocalDateTime regTime;
    
    //private LocalDateTime updateTime;
	
	public static OrderItem createOrderItem(Item item,int count) {
		OrderItem orderItem = new OrderItem();
		orderItem.setItem(item);
		orderItem.setCount(count);
		orderItem.setOrderPrice(item.getPrice());
		item.removeStock(count);//주문 수량만큼 Item엔티티의 재고 차감
		return orderItem;
	}
	
	//주문 가격과 주문 수량을 곱해서 해당 상품을 주문한 총 가격을 계산하는 메소드
	public int getTotalPrice() {
		return orderPrice * count;
	}
}
