package alpacaive.auctionv2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import alpacaive.auctionv2.auction.Auction;
import alpacaive.auctionv2.auction.AuctionDao;
import alpacaive.auctionv2.auction.AuctionDto;
import alpacaive.auctionv2.auction.AuctionService;
import alpacaive.auctionv2.bid.BidAddDto;
import alpacaive.auctionv2.bid.BidDao;
import alpacaive.auctionv2.member.Member;
import alpacaive.auctionv2.member.MemberDao;
import alpacaive.auctionv2.product.Product;
import alpacaive.auctionv2.product.ProductDao;

@SpringBootTest
public class AuctionServiceConcurrencyTests {
  
    @Autowired
    private AuctionDao dao;


    @Autowired
    private AuctionService auctionService;

    @Test
    public void testConcurrentBidding() throws InterruptedException {
        Auction auction = dao.findById(21).orElse(null);
//        System.out.println(auction);
        BidAddDto bidDto = new BidAddDto(0,21,"aaa",4,null); // 유효한 입찰 정보
        BidAddDto bidDto2 = new BidAddDto(0,21,"ccc",4,null); // 유효한 입찰 정보

        // 동시성 테스트를 위한 ExecutorService 설정
        CountDownLatch latch = new CountDownLatch(2);

        // 동시에 호출될 메서드
        // 동시에 두 개의 스레드 생성
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.submit(() -> {
        	try {
            auctionService.normalBid(bidDto);
        	}catch(Exception e) {
        		System.out.println(e);
        	}
            latch.countDown();
        });

        // 두 번째 스레드: 사용자 B가 입찰
        executorService.submit(() -> {
            try{auctionService.normalBid(bidDto2);
            }catch(Exception e) {
            	System.out.println(e);
            }
            latch.countDown();
        });
        // 모든 스레드가 완료될 때까지 대기
        latch.await();

        // 최종 확인
        Auction updatedAuction = dao.findById(auction.getNum()).orElse(null);
        assertEquals(4, updatedAuction.getMax()); // 

        executorService.shutdown();
    }

    // 다른 동시성 시나리오에 대한 추가 테스트 케이스를 작성할 수 있습니다.
}
