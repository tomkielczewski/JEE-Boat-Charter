package pl.edu.pg.eti.kask.boatcharter.user.servlet;

import pl.edu.pg.eti.kask.boatcharter.user.context.UserContext;
import pl.edu.pg.eti.kask.boatcharter.user.dto.GetUserResponse;
import pl.edu.pg.eti.kask.boatcharter.user.dto.GetUsersResponse;
import pl.edu.pg.eti.kask.boatcharter.user.entity.User;
import pl.edu.pg.eti.kask.boatcharter.user.service.UserService;

import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Servlet for returning all users info.
 */
@WebServlet(urlPatterns = UsersServlet.Paths.USERS)
public class UsersServlet extends HttpServlet {

    /**
     * Service for user entity operations.
     */
    private UserService service;

    @Inject
    public UsersServlet(UserService service, UserContext context) {
        this.service = service;
    }

    /**
     * Definition of paths supported by this servlet. Separate inner class provides composition for static fields.
     */
    public static class Paths {

        /**
         * Specified portrait for download and upload.
         */
        public static final String USERS = "/api_v1/users";

    }

    /**
     * JSON-B mapping object. According to open liberty documentation creating those is expensive. The JSON-B is only
     * one of many solutions. JSON strings can be build by hand {@link StringBuilder} or with JSON-P API. Both JSON-B
     * and JSON-P are part of Jakarta EE whereas JSON-B is newer standard.
     */
    private final Jsonb jsonb = JsonbBuilder.create();


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<User> users = service.findAll();
            response.getWriter().write(jsonb.toJson(GetUsersResponse.entityToDtoMapper().apply(users)));
            return;
        }catch (NumberFormatException e){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
        response.getWriter().write("{}");
    }
}
