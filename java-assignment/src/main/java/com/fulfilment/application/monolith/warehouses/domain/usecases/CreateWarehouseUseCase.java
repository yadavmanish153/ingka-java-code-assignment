package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.CreateWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class CreateWarehouseUseCase implements CreateWarehouseOperation {

  private final WarehouseStore warehouseStore;
  private final LocationResolver locationResolver;

  public CreateWarehouseUseCase(WarehouseStore warehouseStore, LocationResolver locationResolver) {
    this.warehouseStore = warehouseStore;
    this.locationResolver = locationResolver;
  }

  @Override
  public void create(Warehouse warehouse) {
    if (warehouse == null) {
      throw new IllegalArgumentException("Warehouse must be provided");
    }

    if (warehouse.businessUnitCode == null || warehouse.businessUnitCode.isBlank()) {
      throw new IllegalArgumentException("Business unit code must be provided");
    }

    if (warehouse.location == null || warehouse.location.isBlank()) {
      throw new IllegalArgumentException("Location must be provided");
    }

    if (warehouse.capacity == null || warehouse.capacity <= 0) {
      throw new IllegalArgumentException("Capacity must be a positive number");
    }

    if (warehouse.stock == null || warehouse.stock < 0) {
      throw new IllegalArgumentException("Stock must be zero or a positive number");
    }

    if (warehouse.capacity < warehouse.stock) {
      throw new IllegalStateException("Warehouse capacity can not be less than current stock");
    }

    Warehouse existing = warehouseStore.findByBusinessUnitCode(warehouse.businessUnitCode);
    if (existing != null) {
      throw new IllegalStateException(
          "Warehouse with business unit code '" + warehouse.businessUnitCode + "' already exists");
    }

    Location location = locationResolver.resolveByIdentifier(warehouse.location);

    List<Warehouse> allWarehouses = warehouseStore.getAll();

    List<Warehouse> activeAtLocation =
        allWarehouses.stream()
            .filter(
                w ->
                    warehouse.location.equals(w.location)
                        && (w.archivedAt == null))
            .toList();

    if (activeAtLocation.size() >= location.maxNumberOfWarehouses) {
      throw new IllegalStateException(
          "Maximum number of warehouses reached for location " + warehouse.location);
    }

    int currentCapacity =
        activeAtLocation.stream().mapToInt(w -> w.capacity != null ? w.capacity : 0).sum();

    int newTotalCapacity = currentCapacity + warehouse.capacity;

    if (newTotalCapacity > location.maxCapacity) {
      throw new IllegalStateException(
          "Total capacity for location "
              + warehouse.location
              + " would exceed maximum of "
              + location.maxCapacity);
    }

    warehouse.createdAt = LocalDateTime.now();
    warehouse.archivedAt = null;

    warehouseStore.create(warehouse);
  }
}
