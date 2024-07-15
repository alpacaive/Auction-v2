package alpacaive.auctionv2.auction;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import alpacaive.auctionv2.bid.Bid;
import alpacaive.auctionv2.bid.BidAddDto;
import alpacaive.auctionv2.bid.BidDao;
import alpacaive.auctionv2.bid.BidDto;
import alpacaive.auctionv2.bid.BidService;
import alpacaive.auctionv2.member.Member;
import alpacaive.auctionv2.member.MemberDao;
import alpacaive.auctionv2.notification.Notification;
import alpacaive.auctionv2.notification.repository.NotificationRepository;
import alpacaive.auctionv2.product.Product;

@Service
public class AuctionService {

	@Autowired
	private AuctionDao dao;
	@Autowired
	private BidDao bdao;
	@Autowired
	private MemberDao mdao;
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	@Autowired
	private NotificationRepository notificationRepository;
	@Autowired
	private BidService bservice;
	@Autowired
	private ZSetOperations<String, Object> zSetOperations;

	public synchronized void save(AuctionDto dto) {
		dao.save(Auction.create(dto));
	}

	// end 타임 설정
	public void setTime(AuctionDto dto, int t) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dto.getStart_time());
//		int time = cal.get(Calendar.MINUTE);
//		cal.set(Calendar.MINUTE, t + time);
		int time = cal.get(Calendar.HOUR);
		cal.set(Calendar.HOUR, t + time);
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
		int i = 0;
		ArrayList<AuctionDto> list = new ArrayList<>();
		for (Auction a : l) {
			if (i > 2) {
				break;
			}
			list.add(AuctionDto.create(a));
			i++;
		}
		return list;
	}

	public ArrayList<AuctionDto> getAll(Auction.Type type) {
		ArrayList<Auction> l = dao.findByStatusAndTypeOrderByNumDesc("경매중", type);
		ArrayList<AuctionDto> list = new ArrayList<>();
		for (Auction a : l) {
			list.add(AuctionDto.create(a));
		}
		return list;
	}

	// 전체목록
	public ArrayList<AuctionDto> getAllByBids(String status) {
		List<Auction> l = dao.findByStatusOrderByBcntDesc(status);
		ArrayList<AuctionDto> list = new ArrayList<>();
		for (Auction a : l) {
			AuctionDto dto = AuctionDto.create(a);
			if (dto.getType().equals(Auction.Type.BLIND)) {
				dto.setMax(dto.getMin());
			}
			list.add(dto);
		}
		return list;

	}

	public ArrayList<AuctionDto> getByTypeIndex(Auction.Type type) {
		ArrayList<AuctionDto> list = getAll();
		ArrayList<AuctionDto> list2 = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getType().equals(type) && list.get(i).getStatus().equals("경매중")) {
				list2.add(list.get(i));
			}
			if (list2.size() > 5) {
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
	
	public synchronized String preBidsave(int parent) {
		AuctionDto auction=get(parent);
		String bider=null;
		Set<Object> prelist =zSetOperations.range(String.valueOf(parent), 0, -1);
		Object[] array=prelist.toArray();
		if(array.length<1) {
			return null;
		}
		BidAddDto maxbid=(BidAddDto)array[0];
		int max=maxbid.getPrice();
		for(Object bid:array) {
			BidAddDto rbid=(BidAddDto)bid;
			if(max<rbid.getPrice()) {
				System.out.println(max);
				maxbid=rbid;  
			}
			zSetOperations.remove(String.valueOf(parent), rbid);
		}
		bservice.save(BidDto.create(maxbid));
		auction.setBcnt(auction.getBcnt() + 1);
		auction.setMax(maxbid.getPrice());
		save(auction);
		bider=maxbid.getBuyer();
		return bider;
	}
	public int normalBid(BidAddDto b) {
		Member buyer = mdao.findById(b.getBuyer()).orElse(null); // 입찰자 검색
		Auction auction = dao.findById(b.getParent()).orElse(null); // 경매 검색
		AuctionDto adto = AuctionDto.create(auction); // 경매 Dto생성
		BidDto dto = new BidDto(b.getNum(), auction, buyer, b.getPrice(), new Date()); // 입찰 dto 생성
		long timestamp = Instant.now().toEpochMilli();
		zSetOperations.add(String.valueOf(dto.getParent().getNum()),BidAddDto.create(Bid.create(dto)), timestamp);
		if (dto.getBidtime().after(auction.getEnd_time())) { // 경매 마감일시 입찰 취소
			return 0;
		}
		if (buyer.getPoint() < b.getPrice()) { // 일찰자의 보유 포인트 보다 입찰가가 더 클시 입찰 취소
			return -1;
		}
		if(auction.getMax()>=b.getPrice()) {
			return 0;
		}
		int getPoint=0;
		BidDto maxValue=null;
		try {
			maxValue = bservice.getMaxByParent(b.getParent()); // 현재입찰정보 검색
			getPoint=maxValue.getPrice();
		}catch(Exception e) {
			System.out.println(e);
		}
		if(maxValue!=null) {
			Member pbuyer = mdao.findById(maxValue.getBuyer().getId()).orElse(null);
			pbuyer.setPoint(pbuyer.getPoint() + getPoint);
			mdao.save(pbuyer);
			Notification notification = Notification.create(pbuyer.getId(), auction.getTitle(), "입찰을 뺏겼습니다"); // 전 입찰자에게 알림
			notificationRepository.save(notification); // redis 저장
			messagingTemplate.convertAndSend("/sub/notice/list/" + pbuyer.getId(),
			notificationRepository.findByName(pbuyer.getId())); // websocket으로 전달
		}
		String bider=preBidsave(auction.getNum());
		if(bider!=null) {
			buyer=mdao.findById(bider).orElse(null);
			buyer.setPoint(buyer.getPoint()-b.getPrice());
			mdao.save(buyer);
			auction = dao.findById(b.getParent()).orElse(null);
			return auction.getMax();
		}		
		return 0;
	}
 
	public int blindBid(BidAddDto b) {
		Member buyer = mdao.findById(b.getBuyer()).orElse(null); // 입찰자 검색
		Auction auction = dao.findById(b.getParent()).orElse(null); // 경매 검색
		AuctionDto adto = AuctionDto.create(auction); // 경매 Dto생성
		BidDto dto = new BidDto(b.getNum(), auction, buyer, b.getPrice(), new Date()); // 입찰 dto 생성
		if (dto.getBidtime().after(auction.getEnd_time())) { // 경매 마감일시 입찰 취소
			return 0;
		}
		if (buyer.getPoint() < b.getPrice()) { // 일차자의 보유 포인트 보다 입찰가가 더 클시 입찰 취소
			return -1;
		}
		buyer.setPoint(buyer.getPoint() - b.getPrice());
		adto.setBcnt(auction.getBcnt() + 1);
		if (b.getPrice() > adto.getMax()) {
			adto.setMax(b.getPrice());
		}
		bdao.save(Bid.create(dto));
		mdao.save(buyer);
		dao.save(Auction.create(adto));
		return adto.getMin();
	}

	public int eventBid(BidAddDto b) {
		Member buyer = mdao.findById(b.getBuyer()).orElse(null); // 입찰자 검색
		Auction auction = dao.findById(b.getParent()).orElse(null); // 경매 검색
		AuctionDto adto = AuctionDto.create(auction); // 경매 Dto생성
		BidDto dto = new BidDto(b.getNum(), auction, buyer, b.getPrice(), new Date()); // 입찰 dto 생성
		if (dto.getBidtime().after(auction.getEnd_time())) { // 경매 마감일시 입찰 취소
			return 0;
		}
		if (buyer.getPoint() < b.getPrice()) { // 일차자의 보유 포인트 보다 입찰가가 더 클시 입찰 취소
			return -1;
		}
		buyer.setPoint(buyer.getPoint() - b.getPrice());
		adto.setBcnt(auction.getBcnt() + 1);
		adto.setMax(adto.getMax() + b.getPrice());
		bdao.save(Bid.create(dto));
		mdao.save(buyer);
		dao.save(Auction.create(adto));
		return adto.getMax();
	}

}
