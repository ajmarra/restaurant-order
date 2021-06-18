package com.ibm.order.endpoint;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import com.ibm.order.model.MenuItem;
import com.ibm.order.rest.OrderController;

@Component
public class MenuEndpointImpl implements MenuEndpoint {
	
	private RestTemplate restTemplate;
	
	private final Logger logger = LoggerFactory.getLogger(OrderController.class);
	
	@Value("${menu.endpoint}")
	private String menuServiceEndpoint;
	
	public MenuEndpointImpl() {
		this.restTemplate = new RestTemplate();
	}
	
	@Override
	   @HystrixCommand(fallbackMethod = "getMenuItem_fallBack", commandKey = "endpointGetMenuItem", commandProperties = {
	      @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000"),
	      @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "4"),
	      @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),
	      @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "75") })
	public MenuItem getMenuItem(String menuItemNumber) {
		logger.info("******** Entered MenuEndpointImpl.getMenuItem() menuItemNumber=[" + menuItemNumber + "]");
		
		MenuItem menuItem = null;
		
		// Test item
//		menuItem = new MenuItem("M123", "Entree", "Salmon Dinner", "Salmon with vegetables and rice", 8, 12.95);
		
		String menuServiceURL = "http://" + menuServiceEndpoint + "/menu/menu/" + menuItemNumber;
		menuItem = this.restTemplate.getForObject(menuServiceURL, MenuItem.class);
		
		return menuItem;
	}
	
	public MenuItem getMenuItem_fallBack(String menuItemNumber) {
	      logger.warn("!!!!!!!!!! IN FALLBACK.  getMenuItem_fallBack menuItemNumber=" + menuItemNumber);
	      MenuItem menuItem = new MenuItem("M123", "Entree", "Salmon Dinner", "Salmon with vegetables and rice", 8, 12.95);
	      return menuItem;
	}
}
