package alpacaive.auctionv2.bid;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import alpacaive.auctionv2.auction.Auction;
import alpacaive.auctionv2.member.Member;

@Repository
public interface BidDao extends JpaRepository<Bid, Integer> {

	ArrayList<Bid>findByParentOrderByPrice(Auction parent);
	ArrayList<Bid>findByParentOrderByNumDesc(Auction parent);
	@Query("select b from Bid b where b.parent.num = :parent order by b.price desc")
	ArrayList<Bid> findByBuyerByPrice(int parent);
	@Query("SELECT b FROM Bid b WHERE b.price = (SELECT MAX(b2.price) FROM Bid b2 WHERE b2.parent.num = :parent)")
	Bid findMaxValue(@Param("parent") int parent);
	ArrayList<Bid> findByBuyerOrderByNumDesc(Member buyer);
}
