package com.api.orderingsystemwithmessaging.repositories;

import com.api.orderingsystemwithmessaging.models.OrderModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<OrderModel, UUID> {
    Optional<OrderModel> findById(UUID id);

    @Query(value = "SELECT * FROM tb_order WHERE status = 0" , nativeQuery = true)
    List<OrderModel> getAllOrdersInProgress();

    @Modifying
    @Query(value = "update tb_order set status = 1 where id = :reference" , nativeQuery = true)
    void endOrderById(@Param("reference") UUID id);
}
