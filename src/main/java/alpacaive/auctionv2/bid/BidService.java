package alpacaive.auctionv2.bid;

import alpacaive.auctionv2.auction.Auction;
import alpacaive.auctionv2.member.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BidService {

	@Autowired
	private BidDao dao;
	
	@Transactional
	public synchronized String save(BidDto dto) {
		Bid b=dao.save(Bid.create(dto));
		return b.toString();
	}
	public BidDto get(int num) {
		Bid b=dao.findById(num).orElse(null);
		if(b==null) {
			return null;
		}
		return BidDto.create(b);
	}
	public ArrayList<BidDto> getByParent(int parent){
		ArrayList<Bid> l=dao.findByParentOrderByPrice(new Auction(parent,null,0,0,null,"",null,null,null,"","",0,0,null));
		ArrayList<BidDto> list=new ArrayList<>();
		for(Bid b:l) {
			list.add(BidDto.create(b));
		}
		return list;
		
	}
	public ArrayList<BidDto> getByParent2(int parent){
		ArrayList<Bid> l=dao.findByParentOrderByNumDesc(new Auction(parent,null,0,0,null,"",null,null,null,"","",0,0,null));
		ArrayList<BidDto> list=new ArrayList<>();
		for(Bid b:l) {
			list.add(BidDto.create(b));
		}
		return list;
		
	}
	
	public BidDto getByBuyer(int auction) {
		ArrayList<Bid> byBuyerByPrice = dao.findByBuyerByPrice(auction);
		return BidDto.create(byBuyerByPrice.get(0));
	}

	public BidDto getMyBidInfo(int auction, String id) {
		ArrayList<Bid> gang = dao.findByParentAndBuyerOrderByPriceDesc(new Auction(auction), new Member(id));
		if(gang.size()==0) {
			return null;
		}
		return BidDto.create(gang.get(0));
	}

	public ArrayList<BidDto> getByBuyer2(String buyer) {
		List<Bid> l = dao.findByBuyerOrderByNumDesc(new Member(buyer, null, null, null, null, 0, null, 0, null));
		ArrayList<BidDto> list = new ArrayList<>();
		for(Bid b:l) {
			list.add(BidDto.create(b));
		}
		return list;
	}
	@Transactional
	public synchronized BidDto getMaxByParent(int parent) {
		Bid max=null;
		try {
			max = dao.findMaxValue(parent);
		}catch(Exception e) {
			System.err.println(e);
		}
		BidDto re=BidDto.create(max);
		return re;
	}
}
