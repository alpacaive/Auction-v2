package alpacaive.auctionv2;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import alpacaive.auctionv2.auction.Auction;
import alpacaive.auctionv2.auction.AuctionDao;
import alpacaive.auctionv2.bid.Bid;
import alpacaive.auctionv2.bid.BidDao;

@SpringBootTest
class AuctionV2ApplicationTests {
	@Autowired
	private BidDao dao;
	@Autowired
	private AuctionDao adao;
    @Test
    void contextLoads() {
//    	Bid b = dao.findMaxValue(1);
//    	System.out.println(b);
    }
    @Test
    void 옥션Dao쿼리테스트() {
    	ArrayList<Auction> list = adao.findByStatusAndTypeOrderByNumDesc("경매중", Auction.Type.NORMAL);
    	System.out.println(list.get(0));
    }


}
