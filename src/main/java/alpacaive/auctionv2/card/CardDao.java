package alpacaive.auctionv2.card;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import alpacaive.auctionv2.card.Card.Type;

@Repository
public interface CardDao extends JpaRepository<Card, String> {
    Card findCardByCardnumAndValidDateAndCvcAndPwdAndType(String cardnum, int validDate, int cvc, int password, Type type);
}
