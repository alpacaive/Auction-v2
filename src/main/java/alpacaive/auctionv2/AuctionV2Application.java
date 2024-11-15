package alpacaive.auctionv2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AuctionV2Application {

    public static void main(String[] args) {
        SpringApplication.run(AuctionV2Application.class, args);
    }

}
