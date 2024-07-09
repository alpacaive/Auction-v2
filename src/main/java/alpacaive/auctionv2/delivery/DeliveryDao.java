package alpacaive.auctionv2.delivery;

import alpacaive.auctionv2.auction.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryDao extends JpaRepository<Delivery, String> {
    Delivery findByAuction(Auction auction);
}
