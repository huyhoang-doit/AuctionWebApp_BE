package vn.webapp.backend.auction.service.transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.webapp.backend.auction.dto.UserTransactionResponse;
import vn.webapp.backend.auction.enums.TransactionState;
import vn.webapp.backend.auction.enums.TransactionType;
import vn.webapp.backend.auction.model.Transaction;
import vn.webapp.backend.auction.model.User;

import java.util.List;
import java.util.Optional;

public interface TransactionService {
    List<Transaction> getAll();

    Transaction getTransactionById(Integer id);

    Transaction getWinnerTransactionByAuctionId(Integer auctionId);

    UserTransactionResponse getTransactionsDashboardByUsername(String username);

    void setTransactionState(Integer id, String state);

    void setTransactionStateWithCode(Integer id, String state, String transactionCode, String bankCode);


    void setTransactionMethod(Integer id, String method);

    Page<Transaction> getTransactionsByUsername (String username, String assetName, Pageable pageable);

    User createTransactionForWinner(Integer auctionId);

    User createTransactionForSeller(Integer auctionId);

    Page<Transaction> getTransactionByTypeAndState (TransactionType typename, String userName,TransactionState state, Pageable pageable);

    Page<Transaction> getTransactionHandover (TransactionType typename, String jewelryName, String category, Pageable pageable);

    List<Transaction> createTransactionForWinnerIfNotExists(Integer userId);

    Page<Transaction> getOverdueTransactions(String userName,Pageable pageable);

    void setTransactionAfterPaySuccess(Integer transactionId, String transactionCode, String bankCode);
}
