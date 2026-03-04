package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.ReplaceWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class ReplaceWarehouseUseCase implements ReplaceWarehouseOperation {

  private final WarehouseStore warehouseStore;
  private final LocationResolver locationResolver;

  public ReplaceWarehouseUseCase(WarehouseStore warehouseStore, LocationResolver locationResolver) {
    this.warehouseStore = warehouseStore;
    this.locationResolver = locationResolver;
  }

  @Override
  public void replace(Warehouse newWarehouse) {
    if (newWarehouse == null) {
      throw new IllegalArgumentException("New warehouse must be provided");
    }

    if (newWarehouse.businessUnitCode == null || newWarehouse.businessUnitCode.isBlank()) {
      throw new IllegalArgumentException("Business unit code must be provided");
    }

    if (newWarehouse.location == null || newWarehouse.location.isBlank()) {
      throw new IllegalArgumentException("Location must be provided");
    }

    if (newWarehouse.capacity == null || newWarehouse.capacity <= 0) {
      throw new IllegalArgumentException("Capacity must be a positive number");
    }

    if (newWarehouse.stock == null || newWarehouse.stock < 0) {
      throw new IllegalArgumentException("Stock must be zero or a positive number");
    }

    Warehouse existing = warehouseStore.findByBusinessUnitCode(newWarehouse.businessUnitCode);

    if (existing == null || existing.archivedAt != null) {
      throw new IllegalArgumentException(
          "No active warehouse found for business unit code '"
              + newWarehouse.businessUnitCode
              + "'");
    }

    if (!existing.location.equals(newWarehouse.location)) {
      Location location = locationResolver.resolveByIdentifier(newWarehouse.location);

      List<Warehouse> allWarehouses = warehouseStore.getAll();

      List<Warehouse> activeAtNewLocation =
          allWarehouses.stream()
              .filter(
                  w ->
                      newWarehouse.location.equals(w.location)
                          && (w.archivedAt == null)
                          && !w.businessUnitCode.equals(existing.businessUnitCode))
              .toList();

      if (activeAtNewLocation.size() >= location.maxNumberOfWarehouses) {
        throw new IllegalStateException(
            "Maximum number of warehouses reached for location " + newWarehouse.location);
      }

      int currentCapacityAtNewLocation =
          activeAtNewLocation.stream().mapToInt(w -> w.capacity != null ? w.capacity : 0).sum();

      int totalCapacityWithNew = currentCapacityAtNewLocation + newWarehouse.capacity;

      if (totalCapacityWithNew > location.maxCapacity) {
        throw new IllegalStateException(
            "Total capacity for location "
                + newWarehouse.location
                + " would exceed maximum of "
                + location.maxCapacity);
      }
    }

    if (newWarehouse.capacity < existing.stock) {
      throw new IllegalStateException(
          "New warehouse capacity must accommodate the stock of the warehouse being replaced");
    }

    if (!existing.stock.equals(newWarehouse.stock)) {
      throw new IllegalStateException(
          "New warehouse stock must match the stock of the warehouse being replaced");
    }

    if (newWarehouse.capacity < newWarehouse.stock) {
      throw new IllegalStateException("Warehouse capacity can not be less than current stock");
    }

    existing.archivedAt = LocalDateTime.now();
    warehouseStore.update(existing);

    newWarehouse.createdAt = LocalDateTime.now();
    newWarehouse.archivedAt = null;
    warehouseStore.create(newWarehouse);
  }
}
