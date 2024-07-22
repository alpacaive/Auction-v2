package alpacaive.auctionv2.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MemberScheduler {

	@Autowired
	private MemberService service;
	
	@Scheduled(cron = "0 0 0 1 * *") // 매월 1일 02:00에 실행
	public void setBenefit() {
		service.getRanker();
	}
}
