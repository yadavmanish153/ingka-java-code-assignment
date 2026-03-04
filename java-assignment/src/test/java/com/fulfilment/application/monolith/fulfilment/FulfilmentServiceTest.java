package com.fulfilment.application.monolith.fulfilment;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@QuarkusTest
public class FulfilmentServiceTest {

  @Inject FulfilmentService fulfilmentService;

  @Test
  @Transactional
  public void shouldLimitProductToTwoWarehousesPerStore() {
    // Uses initial data from import.sql: store id 1, product id 1, warehouses 1..3
    fulfilmentService.assignWarehouseToProductInStore(1L, 1L, 1L);
    fulfilmentService.assignWarehouseToProductInStore(1L, 1L, 2L);

    assertThrows(
        IllegalStateException.class,
        () -> fulfilmentService.assignWarehouseToProductInStore(1L, 1L, 3L));
  }
}
