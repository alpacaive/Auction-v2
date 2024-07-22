package alpacaive.auctionv2.member.exception;

public class MemberNotFoundException extends IllegalArgumentException {
    public MemberNotFoundException(final String message) {
        super(message);
    }
}
