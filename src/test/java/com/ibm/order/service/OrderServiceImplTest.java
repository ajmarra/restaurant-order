package com.ibm.order.service;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import com.ibm.order.model.Order;
import com.ibm.order.model.OrderMenuItem;
import com.ibm.order.repo.OrderRepo;

class OrderServiceImplTest {
	@Mock
	private OrderRepo orderRepo;
	
	@InjectMocks
	private OrderServiceImpl orderService;
	
	@BeforeEach
	public void init() {
		MockitoAnnotations.initMocks(this);
	}
	
	@DisplayName("Test OrderService getOrder with valid order number")
	@Test
	void testGetOrderExistingOrderNumber() {
		// Given - set the parameter values and mock the methods for this test case
		String orderNumber = "0123";
		List<OrderMenuItem> orderMenuItems = new ArrayList<OrderMenuItem>();
		Double orderPrice = 12.12;
		Order orderMocked = new Order("C12345", orderMenuItems, orderPrice);
		when(orderRepo.findByOrderNumber(orderNumber)).thenReturn(orderMocked);
		
		// When - call the method being tested and save the response
		Order order = orderService.getOrder(orderNumber);
		
		// Then - check that the results are valid (and that the expected mocked methods are called)
		assertNotNull(order, "Order should not be null");
		assertEquals(order, orderMocked, "order should be the same as" + orderMocked);
		
		verify(orderRepo).findByOrderNumber(orderNumber);
	}

}
