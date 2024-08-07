package vn.webapp.backend.auction.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.webapp.backend.auction.enums.TransactionState;
import vn.webapp.backend.auction.enums.TransactionType;
import vn.webapp.backend.auction.model.Transaction;
import vn.webapp.backend.auction.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    @Query("SELECT t FROM Transaction t WHERE t.user.username = :username AND t.state != 'HIDDEN' AND (:assetName IS NULL OR t.auction.name LIKE %:assetName%)")
    Page<Transaction> findTransactionsByUsername(@Param("username") String username,@Param("assetName") String assetName, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE t.type = :typename " +
            "AND (:userName IS NULL OR CONCAT(t.user.firstName, ' ', t.user.lastName) LIKE %:userName%) " +
            "AND ((:state IS NULL AND t.state <> 'HIDDEN') OR t.state = :state)")
    Page<Transaction> findTransactionByTypeAndState(
            @Param("typename") TransactionType typename,
            @Param("userName") String userName,
            @Param("state") TransactionState state,
            Pageable pageable
    );
    @Query("SELECT t FROM Transaction t WHERE t.paymentMethod IS NOT NULL AND t.type = :typename "+
            "AND (:jewelryName IS NULL OR t.auction.jewelry.name LIKE %:jewelryName%) "+
            "AND t.auction.jewelry.state = 'AUCTION' AND t.auction.jewelry.isHolding= true "+
            "AND (:category IS NULL OR t.auction.jewelry.category.name = :category)")
    Page<Transaction> findTransactionHandover(
            @Param("typename") TransactionType typename ,
            @Param("jewelryName") String jewelryName,
            @Param("category") String category,
            Pageable pageable);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.type = 'REGISTRATION' AND t.user.username = :username")
    Integer getCountTransactionsRegistrationByUsername(@Param("username") String username);

    @Query("SELECT SUM(t.totalPrice * 0.08) FROM Transaction t WHERE t.type = 'PAYMENT_TO_WINNER' AND t.state = 'SUCCEED' AND YEAR(t.paymentTime) = :year")
    Double getTotalCommissionRevenueByYear(@Param("year") Integer year);

    @Query("SELECT SUM(t.auction.participationFee) FROM Transaction t WHERE t.type = 'REGISTRATION' AND t.state = 'SUCCEED' AND YEAR(t.paymentTime) = :year")
    Double getTotalRegistrationFeeRevenueByYear(@Param("year") Integer year);

    @Query("SELECT COALESCE(SUM(t.totalPrice * 0.08), 0) " +
            "FROM Transaction t " +
            "WHERE t.type = 'PAYMENT_TO_WINNER' " +
            "AND t.state = 'SUCCEED' " +
            "AND YEAR(t.paymentTime) = :year " +
            "AND MONTH(t.paymentTime) = :month")
    Double getTotalRevenueByMonthAndYear(@Param("month") Integer month, @Param("year") Integer year);

    @Query("SELECT SUM(t.auction.participationFee) " +
            "FROM Transaction t " +
            "WHERE t.type = 'REGISTRATION' " +
            "AND t.state = 'SUCCEED' " +
            "AND YEAR(t.paymentTime) = :year " +
            "AND MONTH(t.paymentTime) = :month")
    Double getTotalRegistrationFeeRevenueByMonthAndYear(@Param("month") Integer month, @Param("year") Integer year);

    @Query("SELECT COALESCE(SUM(t.totalPrice * 0.08), 0) " +
            "FROM Transaction t " +
            "WHERE t.type = 'PAYMENT_TO_WINNER' " +
            "AND t.state = 'SUCCEED' " +
            "AND t.paymentTime >= :startOfDay " +
            "AND t.paymentTime < :startOfNextDay")
    Double getTotalRevenueToday(@Param("startOfDay") LocalDateTime startOfDay, @Param("startOfNextDay") LocalDateTime startOfNextDay);

    @Query("SELECT COALESCE(SUM(t.auction.participationFee), 0) " +
            "FROM Transaction t " +
            "WHERE t.type = 'REGISTRATION' " +
            "AND t.state = 'SUCCEED' " +
            "AND t.paymentTime >= :startOfDay " +
            "AND t.paymentTime < :startOfNextDay")
    Double getTotalRegistrationFeeRevenueToday(@Param("startOfDay") LocalDateTime startOfDay, @Param("startOfNextDay") LocalDateTime startOfNextDay);

    @Query("SELECT t FROM Transaction t " +
            "WHERE t.type = 'PAYMENT_TO_WINNER' " +
            "AND t.state != 'SUCCEED' " +
            "AND t.state != 'FAILED' " +
            "AND t.state != 'HIDDEN' " +
            "AND t.createDate < :sevenDaysAgo " +
            "AND (:userName IS NULL OR CONCAT(t.user.firstName, ' ', t.user.lastName) LIKE %:userName%)")
    Page<Transaction> findOverdueTransactions(@Param("userName") String userName,
                                              @Param("sevenDaysAgo") LocalDateTime sevenDaysAgo,
                                              Pageable pageable);

    @Query("SELECT SUM(t.totalPrice) FROM Transaction t WHERE t.type = 'PAYMENT_TO_WINNER' AND t.user.username = :username")
    Double getTotalPriceJewelryWonByUsername(@Param("username") String username);

    @Query("SELECT SUM(t.totalPrice) FROM Transaction t WHERE t.type = 'PAYMENT_TO_WINNER' AND t.user.username = :username AND t.state = 'SUCCEED'")
    Double getTotalPriceJewelryWonByUsernameAndAlreadyPay(@Param("username") String username);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.type = 'PAYMENT_TO_WINNER' AND t.user.username = :username")
    Integer getTotalJewelryWon(@Param("username") String username);

    @Query("SELECT t FROM Transaction t WHERE t.type = 'PAYMENT_TO_WINNER' AND t.auction.id = :auctionId AND t.user.id = :userId AND t.state != 'FAILED'")
    Optional<Transaction> findTransactionByAuctionIdAndUserId(@Param("auctionId") Integer auctionId, @Param("userId") Integer userId);

    @Query("SELECT t FROM Transaction t WHERE t.type = 'PAYMENT_TO_WINNER' AND t.auction.id = :auctionId")
    Transaction findWinnerTransactionByAuctionId(@Param("auctionId") Integer auctionId);
}
