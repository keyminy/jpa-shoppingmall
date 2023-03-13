package com.shop.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shop.dto.CartItemDto;
import com.shop.entity.Cart;
import com.shop.entity.CartItem;
import com.shop.entity.Item;
import com.shop.entity.Member;
import com.shop.repository.CartItemRepository;
import com.shop.repository.CartRepository;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderService orderService;
    
    public Long addCart(CartItemDto cartItemDto, String email){
    	//장바구니에 담을 상품 엔티티 조회
    	Item item = itemRepository.findById(cartItemDto.getItemId())
    			  .orElseThrow(EntityNotFoundException::new);
    	//현재 로그인한 회원 엔티티 조회
    	Member member = memberRepository.findByEmail(email);
    	//현재 로그인한 회원의 장바구니 엔티티 조회
    	Cart cart = cartRepository.findByMemberId(member.getId());
    	//상품을 처음으로 담음(cart가 null일때, "해당" 회원의 장바구니 엔티티 생성)
    	if(cart == null) {
    		//cart.setMember(member); return cart
    		cart = Cart.createCart(member);
    		cartRepository.save(cart);
    	}

    	//현재 상품이 장바구니에 이미 들어가 있는지 조회
    	CartItem savedCartItem =
    			cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

    	if(savedCartItem != null){
    		 System.out.println("이미존재");
    		//장바구니에 이미 존재하는 상품일 경우, 기존 수량에 현재 장바구니에 담을 수량만큼 더함
             savedCartItem.addCount(cartItemDto.getCount());
             return savedCartItem.getId();
        } else {
        	 System.out.println("없음");
             CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());
             cartItemRepository.save(cartItem);
             return cartItem.getId();
        }
    }
}