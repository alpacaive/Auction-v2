package alpacaive.auctionv2.product;

import alpacaive.auctionv2.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface ProductDao extends JpaRepository<Product, Integer> {
    // 카테고리로 검색
    ArrayList<Product> findByCategories(String Categories);

    // 이름으로 검색
    ArrayList<Product> findByNameLike(String name);

    // 판매자로 검색
    ArrayList<Product> findBySeller(Member seller);
}
