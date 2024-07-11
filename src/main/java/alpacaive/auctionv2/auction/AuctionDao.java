package alpacaive.auctionv2.auction;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import alpacaive.auctionv2.member.Member;
import alpacaive.auctionv2.product.Product;
import jakarta.persistence.LockModeType;

@Repository
public interface AuctionDao extends JpaRepository<Auction, Integer> {
	
	@Lock(LockModeType.PESSIMISTIC_READ)
	Optional<Auction> findById(Integer id);
	
	// 판매자로 찾기
	ArrayList<Auction> findBySellerOrderByNumDesc(Member seller);
	// 상품번호로 찾기
	ArrayList<Auction> findByProduct(Product product);
	//비드 량으로 정열
	ArrayList<Auction> findByStatusOrderByBcntDesc(String status);
	//상태와 타입으로 찾기
	ArrayList<Auction> findByStatusAndTypeOrderByNumDesc(String status,Auction.Type type);

	
	// 경매 타입별 목록
	ArrayList<Auction> findByType(Auction.Type type);
	// 경매 상태별 목록
	ArrayList<Auction> findByStatusOrderByNumDesc(String status);
	ArrayList<Auction> findAllByOrderByNumDesc();
	
	
	
	
	
	@Query("SELECT a FROM Auction a JOIN a.product p WHERE p.name LIKE %:productName%")
    ArrayList<Auction> findByProductNameLike(@Param("productName") String productName);
	
	@Query("SELECT a FROM Auction a JOIN a.product p WHERE p.categories =:prodcategories")
    ArrayList<Auction> findByProductCategories(@Param("prodcategories") Product.Categories prodcategories);
	
}
