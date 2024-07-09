package alpacaive.auctionv2.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;

public class MyFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException exception) throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpSession session=request.getSession();
		session.setAttribute("msg", "gang");
		setDefaultFailureUrl("/loginform");
		request.setAttribute("msg", "wrong id or password");
		super.onAuthenticationFailure(request, response, exception);
	}
	
}
