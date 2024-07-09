package alpacaive.auctionv2.scrap;

import alpacaive.auctionv2.auction.Auction;
import alpacaive.auctionv2.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface ScrapDao extends JpaRepository<Scrap, Integer> {
    Scrap findByMemberAndAuction(Member member, Auction auction);
    ArrayList<Scrap> findByMember(Member member);
}
