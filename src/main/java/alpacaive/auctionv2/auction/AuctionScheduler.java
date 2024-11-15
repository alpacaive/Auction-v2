package alpacaive.auctionv2.auction;

import alpacaive.auctionv2.bid.BidDto;
import alpacaive.auctionv2.bid.BidService;
import alpacaive.auctionv2.chat.domain.ChatRoom;
import alpacaive.auctionv2.chat.service.ChatRoomService;
import alpacaive.auctionv2.member.Member;
import alpacaive.auctionv2.member.MemberDto;
import alpacaive.auctionv2.member.MemberService;
import alpacaive.auctionv2.notification.Notification;
import alpacaive.auctionv2.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuctionScheduler {

	private final AuctionService service;
	private final BidService bidService;
	private final ChatRoomService chatRoomService;
	private final SimpMessagingTemplate messagingTemplate;
	private final NotificationRepository notificationRepository;
	private final MemberService mservice;
	private final EventSocket event;


//	@Scheduled(cron = "0 0/1 * * * *") // 매 1분에 실행
	@Scheduled(fixedRate = 1000000000)
	public void setStatus() {
		Date date = new Date();
		ArrayList<AuctionDto> list = service.getByStatus("경매중"); // 경매중인 list
		for (AuctionDto auction : list) {
			if (auction.getEnd_time().before(date)) {
				auction.setStatus("경매 마감");  // 경매 마감 auction 상태변경
				Member seller = auction.getSeller(); // 마감 경매 seller
				Notification notification = Notification.create(auction.getSeller().getId(), auction.getTitle(), "경매 마감되었습니다✅"); //판매자에게 알림
				notificationRepository.save(notification); // redis 저장
				messagingTemplate.convertAndSend("/sub/notice/list/"+auction.getSeller().getId(), notificationRepository.findByName(auction.getSeller().getId()));  // websocket으로 전달
				try {
					BidDto bid = bidService.getByBuyer(auction.getNum()); // 마감된 경매의 최고 입잘가의 입찰정보
					if (auction.getType().equals(Auction.Type.BLIND)) { // 블라인드 경매시
						boolean flag = true;
						ArrayList<BidDto> buyList = bidService.getByParent2(auction.getNum()); // 옥션 넘버로 찾아서 순서대로 정렬한 리스트
						for (BidDto dto : buyList) {
							if (dto.getPrice() != auction.getMax() && flag) {  // 리스트 중에서 auction의 최종낙찰가와 같으면서 flag true인 입찰을 찾음
								Member loser = dto.getBuyer();   // 경매에서 입찰 받지 못한 loser들
								loser.updatePoint(loser.getPoint() + dto.getPrice()); //의 포인트 반환
							}
							bid.setBuyer(dto.getBuyer());  //최종 낙찰자설정
							flag = false; // 최종 낙찰자 설정시 flag를 flase로 만들어 같은 가격으로 입찰한 사람들에게 포인트 반환하기 위한 코드
						}
					}
					if (auction.getType().equals(Auction.Type.EVENT)) {  // 이벤트 경매시 
						ArrayList<BidDto> buyList = bidService.getByParent(auction.getNum()); // 옥션 넘버로 찾아서 가격순으로 정렬한 리스트
						Map<Member, Object> map = new HashMap(); // 입찰자들의 최종 입찰가를 저장하기 위한 코드
						int total = 0;
						for (BidDto bDto : buyList) {
							map.put(bDto.getBuyer(), 0); // map에 입찰자의 정보를 key 값 으로 일단 0원씩 넣어줌
							total += bDto.getPrice(); // total 입찰가
						}
						for (BidDto bDto : buyList) {
							int price = (int) map.get(bDto.getBuyer()); // 입찰자의 최종 입찰가를 계산하기 위한 코드
							map.put(bDto.getBuyer(), price + bDto.getPrice()); // 입찰자의 최종 입찰가
						}
						int total2 = total;
						Map<Member, Double> map2 = new HashMap<>(); // 입찰자들의 확률을 계산하기 위한 맵
						map.forEach((key, value) -> {
							int price = (int) value;
							double chance = (double) price / total2;
							System.out.println(key.getId()+"의 확률은:" +chance +" ( "+price+"/"+total2+")");
							map2.put(key, chance); // 입찰자의 최종 당첨 확률
						});
						Random random = new Random();
						double randomNumber = random.nextDouble(); // 0부터 1 사이의 랜덤 숫자 생성
						double cumulativeProbability = 0.0;
						Member winner = null;
						for (Map.Entry<Member, Double> entry : map2.entrySet()) {
						    cumulativeProbability += entry.getValue();				    
						    if (randomNumber < cumulativeProbability) {
						        winner = entry.getKey();
						        break;
						    }
						}
						bid.setBuyer(winner);

						Map mapent=new HashMap();
						mapent.put("mino", winner.getId());
						mapent.put("parent", auction.getNum());
						mapent.put("msg","end");
						event.sendMessage("/sub/bid", mapent);
					}
					auction.setMino(bid.getBuyer());  // 최종 낙찰자를 aution table에 넣기 위한 코드
					Notification mino = Notification.create(auction.getMino().getId(), auction.getTitle(),"낙찰되었습니다!"); // 최종 낙찰자에게 알림
					notificationRepository.save(mino); // 최종 낙찰자 저장
					messagingTemplate.convertAndSend("/sub/notice/list/"+auction.getMino().getId(), notificationRepository.findByName(auction.getMino().getId())); // 최종 낙찰자에게 알림
					seller.updatePoint(seller.getPoint() + auction.getMax()); // 판매자에게 낙찰금 전달
					mservice.edit(seller); // 판매자 포인트 db에 저장하기 위한 코드
					service.save(auction); //모든 경매 정보 db에 저장
					Set<Object> byName = chatRoomService.findByName(auction.getMino().getId());
					if (byName.isEmpty()) {
						chatRoomService.createChatRoom(String.valueOf(auction.getNum()), bid.getBuyer().getId(),
								seller.getId());
						return;
					}
					for (Object obj : byName) {
						if (obj instanceof ChatRoom) {
							ChatRoom chatRoom = (ChatRoom) obj;
							String chatRoomSeller = chatRoom.getSeller();
                            chatRoomService.createChatRoom(String.valueOf(auction.getNum()),
                                    bid.getBuyer().getId(), seller.getId());// Get seller from
						}
					}
				} catch (Exception e) {
					service.save(auction);
				}
			}
		}
	}
}