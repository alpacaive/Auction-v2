package alpacaive.auctionv2.auth;

import alpacaive.auctionv2.member.Member;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Setter
@Getter
public class MyUserDetails implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//인증값 객체를 final로 정의
	private final Member u;

	public MyUserDetails(Member u) {
		this.u = u;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		List<GrantedAuthority> list = new ArrayList<>();
		String role = "";
		if (u.getType().equals("admin")) {
			role = "ROLE_ADMIN";
		} else if (u.getType().equals("member")) {
			role = "ROLE_MEMBER";
		}
		list.add(new SimpleGrantedAuthority(role));
		return list;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		System.out.println("MyUserDetails:" + u.getPwd());
		return u.getPwd();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return u.getId();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
