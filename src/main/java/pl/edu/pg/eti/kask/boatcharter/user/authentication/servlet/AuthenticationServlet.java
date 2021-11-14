package pl.edu.pg.eti.kask.boatcharter.user.authentication.servlet;


import pl.edu.pg.eti.kask.boatcharter.user.authentication.service.AuthenticationService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet for user form login. Whereas {@link pl.edu.pg.eti.kask.boatcharter.user.authentication.filter.AuthenticationFilter}
 * is enough for REST web services, form login methods can be used for web browser users. Servlet uses multipart
 * configuration because using hTML5 {FormData} causes that request is encodes as 'multipart/form-data' instead of
 * 'application/x-www-form-urlencoded'.
 */
@MultipartConfig
@WebServlet(urlPatterns = {
        AuthenticationServlet.Paths.LOGIN,
        AuthenticationServlet.Paths.LOGOUT
})
public class AuthenticationServlet extends HttpServlet {


    /**
     * Service for authentication methods.
     */
    private AuthenticationService service;

    @Inject
    public AuthenticationServlet(AuthenticationService service) {
        this.service = service;
    }

    /**
     * Definition of paths supported by this servlet. Separate inner class provides composition for static fields.
     */
    public static class Paths {

        /**
         * Login action.
         */
        public static final String LOGIN = "/api/user/login";

        /**
         * Logout action.
         */
        public static final String LOGOUT = "/api/user/logout";

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        if (Paths.LOGIN.equals(servletPath)) {
            if (!service.authenticate(request.getParameter("login"),
                    request.getParameter("password"))) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } else if (Paths.LOGOUT.equals(servletPath)) {
            request.getSession().invalidate();
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

}
