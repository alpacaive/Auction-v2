package alpacaive.auctionv2.auction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import alpacaive.auctionv2.bid.Bid;
import alpacaive.auctionv2.bid.BidAddDto;
import alpacaive.auctionv2.bid.BidDao;
import alpacaive.auctionv2.bid.BidDto;
import alpacaive.auctionv2.member.Member;
import alpacaive.auctionv2.member.MemberDao;
import alpacaive.auctionv2.product.Product;

@Service
public class AuctionService {

	@Autowired
	private AuctionDao dao;
	@Autowired
	private BidDao bdao;
	@Autowired
	private MemberDao mdao;

	public void save(AuctionDto dto) {
		dao.save(Auction.create(dto));
	}

	// end 타임 설정
	public void setTime(AuctionDto dto, int t) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dto.getStart_time());
		int time = cal.get(Calendar.MINUTE);
		cal.set(Calendar.MINUTE, t + time);
//		int time = cal.get(Calendar.HOUR);
//		cal.set(Calendar.HOUR, t + time);
		dto.setEnd_time(cal.getTime());
	}

	// 번호로 삭제
	public void delete(int num) {
		dao.deleteById(num);
	}

	// 번호로 찾기
	public AuctionDto get(int num) {
		Auction a = dao.findById(num).orElse(null);
		if (a == null) {
			return null;
		}
		return AuctionDto.create(a);
	}

	// 전체목록
	public ArrayList<AuctionDto> getAll() {
		List<Auction> l = dao.findAllByOrderByNumDesc();
		ArrayList<AuctionDto> list = new ArrayList<>();
		for (Auction a : l) {
			list.add(AuctionDto.create(a));
		}
		return list;

	}
	
	// 전체목록
		public ArrayList<AuctionDto> getlatestAuction() {
			List<Auction> l = dao.findByStatusOrderByNumDesc("경매중");
			ArrayList<AuctionDto> list = new ArrayList<>();
			for (Auction a : l) {
				list.add(AuctionDto.create(a));
			}
			return list;
		}
	
	
	
	public ArrayList<AuctionDto> getAll(Auction.Type type){
		ArrayList<Auction> l=dao.findByStatusAndTypeOrderByNumDesc("경매중", type);
		ArrayList<AuctionDto> list=new ArrayList<>();
		for(Auction a :l) {
			list.add(AuctionDto.create(a));
		}
		return list;
	}

	// 전체목록
	public ArrayList<AuctionDto> getAllByBids(String status) {
		List<Auction> l = dao.findByStatusOrderByBcntDesc(status);
		ArrayList<AuctionDto> list = new ArrayList<>();
		for (Auction a : l) {
			list.add(AuctionDto.create(a));
		}
		return list;

	}
	
	public ArrayList<AuctionDto> getByTypeIndex(Auction.Type type ){
		ArrayList<AuctionDto>list=getAll();
		ArrayList<AuctionDto> list2= new ArrayList<>();
		for(int i=0;i<list.size();i++) {
			if(list.get(i).getType().equals(type) && list.get(i).getStatus().equals("경매중")) {
				list2.add(list.get(i));				
				}
				if(list2.size()>5) {
					break;
				}
		}
		return list2;
	}
	

	// 판매자로 찾기
	public ArrayList<AuctionDto> getBySeller(String seller) {
		List<Auction> l = dao.findBySellerOrderByNumDesc(new Member(seller, "", "", "", null, 0, "", 0, ""));
		ArrayList<AuctionDto> list = new ArrayList<>();
		for (Auction a : l) {
			list.add(AuctionDto.create(a));
		}
		return list;

	}

	// 상품으로 찾기
	public ArrayList<AuctionDto> getByProduct(Product product) {
		List<Auction> l = dao.findByProduct(product);
		ArrayList<AuctionDto> list = new ArrayList<>();
		for (Auction a : l) {
			list.add(AuctionDto.create(a));
		}
		return list;

	}

	// 타입으로 찾기
	public ArrayList<AuctionDto> getByType(Auction.Type type) {
		List<Auction> l = dao.findByType(type);
		ArrayList<AuctionDto> list = new ArrayList<>();
		for (Auction a : l) {
			list.add(AuctionDto.create(a));
		}
		return list;

	}

	// 상태로 찾기
	public ArrayList<AuctionDto> getByStatus(String status) {
		List<Auction> l = dao.findByStatusOrderByNumDesc(status);
		ArrayList<AuctionDto> list = new ArrayList<>();
		for (Auction a : l) {
			list.add(AuctionDto.create(a));
		}
		return list;

	}

	// 상품명으로 찾기
	public ArrayList<AuctionDto> getByProdName(String Name) {
		List<Auction> l = dao.findByProductNameLike(Name);
		ArrayList<AuctionDto> list = new ArrayList<>();
		for (Auction a : l) {
			list.add(AuctionDto.create(a));
		}
		return list;

	}

	// 카테고리로 찾기
	public ArrayList<AuctionDto> getByProdCategories(Product.Categories categories) {
		List<Auction> l = dao.findByProductCategories(categories);
		ArrayList<AuctionDto> list = new ArrayList<>();
		for (Auction a : l) {
			list.add(AuctionDto.create(a));
		}
		return list;

	}

	public boolean stopAuction(int num) {
		Auction a = dao.findById(num).orElse(null);
		AuctionDto auction = AuctionDto.create(a);
		if (a == null) {
			return false;
		}
		auction.setStatus("경매마감");
		switch (auction.getType()) {
		case BLIND:
			ArrayList<Bid> blist = bdao.findByParentOrderByNumDesc(a);
			for (Bid bid : blist) {
				Member buyer = bid.getBuyer();
				buyer.setPoint(buyer.getPoint() + bid.getPrice());
				try {
					mdao.save(buyer);
				} catch (Exception e) {
					return false;
				}
			}
		case NORMAL: {
			blist = bdao.findByParentOrderByNumDesc(a);
			Member buyer = blist.get(0).getBuyer();
			buyer.setPoint(buyer.getPoint() + blist.get(0).getPrice());
			try {
				mdao.save(buyer);
			} catch (Exception e) {
				return false;
			}
		}
		case EVENT: {
			blist = bdao.findByParentOrderByNumDesc(a);
			for (Bid bid : blist) {
				Member buyer = bid.getBuyer();
				buyer.setPoint(buyer.getPoint() + bid.getPrice());
				try {
					mdao.save(buyer);
				} catch (Exception e) {
					return false;
				}
			}
		}
		}
		return true;
	}

	public int bid(BidAddDto b) {
		Member buyer = mdao.findById(b.getBuyer()).orElse(null);
		Auction auction = dao.findById(b.getParent()).orElse(null);
		AuctionDto adto = AuctionDto.create(auction);
		BidDto dto = new BidDto(b.getNum(), auction, buyer, b.getPrice(), new Date());
		if (dto.getBidtime().after(auction.getEnd_time())) {
			return 0;
		}
		if (buyer.getPoint() < b.getPrice()) {
			return -1;
		}
		Bid bid = bdao.findMaxValue(b.getParent());
		if (auction.getType().equals(Auction.Type.NORMAL) && bid==null) {
			if(b.getPrice()<adto.getMax()) {
				return -2;
			}
			int getPoint = bid.getPrice();
			Member pbuyer = mdao.findById(bid.getBuyer().getId()).orElse(null);
			pbuyer.setPoint(pbuyer.getPoint() + getPoint);
			mdao.save(pbuyer);
		}
		buyer.setPoint(buyer.getPoint() - b.getPrice());
		bdao.save(Bid.create(dto));
		adto.setBcnt(auction.getBcnt() + 1);
		if ((auction.getType().equals(Auction.Type.EVENT))) {
			adto.setMax(auction.getMax() + b.getPrice());
		} else {
			adto.setMax(b.getPrice());
		}
		save(adto);
		mdao.save(buyer);
		return adto.getMax();
	}

}
