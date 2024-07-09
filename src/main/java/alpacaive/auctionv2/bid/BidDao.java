package alpacaive.auctionv2.bid;

import alpacaive.auctionv2.auction.Auction;
import alpacaive.auctionv2.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface BidDao extends JpaRepository<Bid, Integer> {

	ArrayList<Bid>findByParentOrderByPrice(Auction parent);
	ArrayList<Bid>findByParentOrderByNumDesc(Auction parent);

	@Query("select b from Bid b where b.parent.num = :parent order by b.price desc")
	ArrayList<Bid> findByBuyerByPrice(int parent);

	ArrayList<Bid> findByBuyerOrderByNumDesc(Member buyer);
}
