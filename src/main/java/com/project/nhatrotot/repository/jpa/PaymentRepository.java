package com.project.nhatrotot.repository.jpa;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.nhatrotot.model.Payment;
import com.project.nhatrotot.model.PaymentState;
import com.project.nhatrotot.model.PaymentType;
import com.project.nhatrotot.model.statistics.PurchaseStatisticsByDate;
import com.project.nhatrotot.model.statistics.Top10MostPurchased;
import com.project.nhatrotot.model.statistics.PurchaseStatisticsByMonth;

import java.util.List;

import org.springframework.data.domain.Page;

public interface PaymentRepository extends JpaRepository<Payment, String> {
        Page<Payment> findByType_IdEqualsAndUser_UserIdEquals(Integer id, String userId, Pageable pageable);

        Page<Payment> findByUser_UserIdEquals(String userId, Pageable pageable);

        Page<Payment> findByUser_EmailContainingAndPaymentStateContainingAllIgnoreCase(String userEmail,
                        String paymentStatus, Pageable pageable);

        Page<Payment> findByType_IdEqualsAndUser_EmailContainingAndPaymentStateContainingAllIgnoreCase(Integer id,
                        String userEmail, String paymentStatus, Pageable pageable);

        @Query("SELECT" +
                        " new com.project.nhatrotot.model.statistics.PurchaseStatisticsByDate(DATE(p.createdAt), SUM(p.total), COUNT(p.paymentId)) "
                        +
                        "FROM Payment p " +
                        "WHERE p.paymentState = ?1 AND DATE(p.createdAt) between DATE(?2) and DATE(?3) " +
                        "GROUP BY DATE(p.createdAt) ORDER BY DATE(p.createdAt) DESC")
        List<PurchaseStatisticsByDate> findPurchaseStatisticsByDate(PaymentState state, String dateFrom, String dateTo);

        @Query("SELECT" +
                        " new com.project.nhatrotot.model.statistics.PurchaseStatisticsByDate(DATE(p.createdAt), SUM(p.total), COUNT(p.paymentId)) "
                        +
                        "FROM Payment p " +
                        "WHERE p.paymentState = ?1 AND p.type = ?4 AND DATE(p.createdAt) between DATE(?2) and DATE(?3) "
                        +
                        "GROUP BY DATE(p.createdAt) ORDER BY DATE(p.createdAt) DESC")
        List<PurchaseStatisticsByDate> findPurchaseStatisticsByDateWithType(PaymentState state, String dateFrom,
                        String dateTo, PaymentType type);

        @Query("SELECT" +
                        " new com.project.nhatrotot.model.statistics.PurchaseStatisticsByMonth(YEAR(p.createdAt), MONTH(p.createdAt), SUM(p.total), COUNT(p.paymentId)) "
                        +
                        "FROM Payment p " +
                        "WHERE p.paymentState = ?1 AND DATE(p.createdAt) between DATE(?2) and DATE(?3) " +
                        "GROUP BY YEAR(p.createdAt), MONTH(p.createdAt) ORDER BY YEAR(p.createdAt) DESC, MONTH(p.createdAt) DESC")
        List<PurchaseStatisticsByMonth> findPurchaseStatisticsByMonth(PaymentState state, String dateFrom,
                        String dateTo);

        @Query("SELECT" +
                        " new com.project.nhatrotot.model.statistics.PurchaseStatisticsByMonth(YEAR(p.createdAt), MONTH(p.createdAt), SUM(p.total), COUNT(p.paymentId)) "
                        +
                        "FROM Payment p " +
                        "WHERE p.paymentState = ?1 AND DATE(p.createdAt) between DATE(?2) AND DATE(?3) AND p.type = ?4 "
                        +
                        "GROUP BY YEAR(p.createdAt), MONTH(p.createdAt) ORDER BY YEAR(p.createdAt) DESC, MONTH(p.createdAt) DESC")
        List<PurchaseStatisticsByMonth> findPurchaseStatisticsByMonthWithType(PaymentState state, String dateFrom,
                        String dateTo, PaymentType type);

        @Query("SELECT" +
                        " new com.project.nhatrotot.model.statistics.Top10MostPurchased(COUNT(p.paymentId), SUM(p.total), p.user.userId, p.user.email) "
                        +
                        "FROM Payment p " +
                        "WHERE p.paymentState = ?1 AND DATE(p.createdAt) between DATE(?2) and DATE(?3) " +
                        "GROUP BY p.user.userId ORDER BY SUM(p.total) DESC")
        List<Top10MostPurchased> findTop10MostPurchased(PaymentState state, String dateFrom, String dateTo);
}
