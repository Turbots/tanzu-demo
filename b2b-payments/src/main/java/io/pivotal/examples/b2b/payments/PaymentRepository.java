package io.pivotal.examples.b2b.payments;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Modifying
    @Query("update Payment p set p.paymentStatus = :status where p.paymentId = :id")
    void confirmPayment(@Param("id") String paymentId, @Param("status") PaymentStatus status);
}
