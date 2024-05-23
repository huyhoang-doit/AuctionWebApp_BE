package vn.webapp.backend.auction.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.webapp.backend.auction.enums.AuctionState;
import vn.webapp.backend.auction.model.Auction;

import java.sql.Timestamp;
import java.util.List;

public interface IAuctionService {
    List<Auction> getAll();
    Auction getAuctionById(Integer id);
    void deleteAuction(Integer id);
    List<Auction> findAuctionSortByBetweenStartdayAndEndday(Timestamp startDay, Timestamp endDay);
    List<Auction> findAuctionByName(String name);
    Page<Auction> getAllAuctions(AuctionState state, Pageable pageable, Integer categoryId);
    void setAuctionState(Integer id, String state);
    List<Auction> findTodayAuctions();
}
