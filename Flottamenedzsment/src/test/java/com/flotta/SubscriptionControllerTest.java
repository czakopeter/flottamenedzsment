package com.flotta;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import com.flotta.controller.SubscriptionController;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SubscriptionControllerTest {

  @Autowired
  private SubscriptionController subscriptionController;

  @Autowired
  private TestRestTemplate restTemplate;
  
  @LocalServerPort
  private int PORT;
  
  @Test
  void contexLoads() throws Exception {
    assertThat(subscriptionController).isNotNull();
  }
  
//  @Test
//  void allSubscription() {
//    assertThat(this.restTemplate.getForObject(
//        "http://localhost:" + PORT + "/subscription/all",
//        String.class)).contains("subscription_templates/subscriptionAll");
//  }

}
 