package pl.edu.pg.eti.kask.boatcharter.boat.servlet;

import pl.edu.pg.eti.kask.boatcharter.boat.dto.CreateBoatRequest;
import pl.edu.pg.eti.kask.boatcharter.boat.dto.GetBoatResponse;
import pl.edu.pg.eti.kask.boatcharter.boat.dto.GetBoatsResponse;
import pl.edu.pg.eti.kask.boatcharter.boat.entity.Boat;
import pl.edu.pg.eti.kask.boatcharter.boat.service.BoatService;
import pl.edu.pg.eti.kask.boatcharter.boatType.service.BoatTypeService;
import pl.edu.pg.eti.kask.boatcharter.servlet.HttpHeaders;
import pl.edu.pg.eti.kask.boatcharter.servlet.MimeTypes;
import pl.edu.pg.eti.kask.boatcharter.servlet.ServletUtility;
import pl.edu.pg.eti.kask.boatcharter.servlet.UrlFactory;
import pl.edu.pg.eti.kask.boatcharter.user.entity.User;
import pl.edu.pg.eti.kask.boatcharter.user.service.UserService;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * Servlet for handling HTTP requests considering boats operations. Servlet API does not allow named path
 * parameters so wildcard is used.
 */
@WebServlet(urlPatterns = {
        BoatServlet.Paths.BOATS + "/*",
        BoatServlet.Paths.USER_BOATS + "/*"
})
public class BoatServlet extends HttpServlet {

    /**
     * JSON-B mapping object. According to open liberty documentation creating those is expensive. The JSON-B is only
     * one of many solutions. JSON strings can be build by hand {@link StringBuilder} or with JSON-P API. Both JSON-B
     * and JSON-P are part of Jakarta EE whereas JSON-B is newer standard.
     */
    private final Jsonb jsonb = JsonbBuilder.create();

    /**
     * Definition of paths supported by this servlet. Separate inner class provides composition for static fields.
     */
    public static class Paths {

        /**
         * All boats or specified boat.
         */
        public static final String BOATS = "/api_v1/boats";

        /**
         * All boats belonging to the caller principal or specified boat of the caller principal.
         */
        public static final String USER_BOATS = "/api_v1/user/boats";

    }

    /**
     * Definition of regular expression patterns supported by this servlet. Separate inner class provides composition
     * for static fields. Whereas servlet activation path can be compared to {@link Paths} the path info (denoted by
     * wildcard in paths) can be compared using regular expressions.
     */
    public static class Patterns {

        /**
         * All boats.
         */
        public static final String BOATS = "^/?$";

        /**
         * Specified boat.
         */
        public static final String BOAT = "^/[0-9]+/?$";

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = ServletUtility.parseRequestPath(request);
        String servletPath = request.getServletPath();
        if (Paths.BOATS.equals(servletPath)) {
            if (path.matches(Patterns.BOAT)) {
                getboat(request, response);
                return;
            } else if (path.matches(Patterns.BOATS)) {
                getBoats(request, response);
                return;
            }
        } else if (Paths.USER_BOATS.equals(servletPath)) {
            if (path.matches(Patterns.BOAT)) {
                getUserBoat(request, response);
                return;
            } else if (path.matches(Patterns.BOATS)) {
                getUserBoats(request, response);
                return;
            }
        }
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = ServletUtility.parseRequestPath(request);
        if (Paths.USER_BOATS.equals(request.getServletPath())) {
            if (path.matches(Patterns.BOATS)) {
                postUserBoat(request, response);
                return;
            }
        }
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

//    @Override
//    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String path = ServletUtility.parseRequestPath(request);
//        if (Paths.USER_BOATS.equals(request.getServletPath())) {
//            if (path.matches(Patterns.BOAT)) {
//                putUserBoat(request, response);
//                return;
//            }
//        }
//        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
//    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = ServletUtility.parseRequestPath(request);
        if (Paths.USER_BOATS.equals(request.getServletPath())) {
            if (path.matches(Patterns.BOAT)) {
                deleteUserBoat(request, response);
                return;
            }
        }
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    /**
     * Sends single boat or 404 error if not found.
     *
     * @param request  http request
     * @param response http response
     * @throws IOException if any input or output exception occurred
     */
    private void getboat(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BoatService service = (BoatService) request.getServletContext().getAttribute("boatService");

        //Parsed request path is valid with boat pattern and can contain starting and ending '/'.
        Long id = Long.parseLong(ServletUtility.parseRequestPath(request).replaceAll("/", ""));
        Optional<Boat> boat = service.find(id);

        if (boat.isPresent()) {
            response.setContentType(MimeTypes.APPLICATION_JSON);
            response.getWriter()
                    .write(jsonb.toJson(GetBoatResponse.entityToDtoMapper().apply(boat.get())));
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /**
     * Sends all boats as JSON.
     *
     * @param request  http request
     * @param response http response
     * @throws IOException if an input or output exception occurred
     */
    private void getBoats(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BoatService service = (BoatService) request.getServletContext().getAttribute("boatService");

        response.setContentType(MimeTypes.APPLICATION_JSON);
        response.getWriter()
                .write(jsonb.toJson(GetBoatsResponse.entityToDtoMapper().apply(service.findAll())));
    }

    /**
     * Sends single caller principal's boat or 404 error if not found.  Caller principal should be stored in
     * session context.
     *
     * @param request  http request
     * @param response http response
     * @throws IOException if any input or output exception occurred
     */
    private void getUserBoat(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BoatService boatService = (BoatService) request.getServletContext().getAttribute("boatService");
        UserService userService = (UserService) request.getServletContext().getAttribute("userService");
        String principal = (String) request.getSession().getAttribute("principal");

        Optional<User> user = userService.find(principal);

        if (user.isPresent()) {
            //Parsed request path is valid with boat pattern and can contain starting and ending '/'.
            Long id = Long.parseLong(ServletUtility.parseRequestPath(request).replaceAll("/", ""));
            Optional<Boat> boat = boatService.find(user.get(), id);

            if (boat.isPresent()) {
                response.setContentType(MimeTypes.APPLICATION_JSON);
                response.getWriter()
                        .write(jsonb.toJson(GetBoatResponse.entityToDtoMapper().apply(boat.get())));
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            //This should not happen as servlet is after authorization filter.
            throw new IllegalStateException(String.format("No user %s found", principal));
        }
    }

    /**
     * Sends all call principal's boats as JSON. Caller principal should be stored in session context.
     *
     * @param request  http request
     * @param response http response
     * @throws IOException if an input or output exception occurred
     */
    private void getUserBoats(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BoatService boatService = (BoatService) request.getServletContext().getAttribute("boatService");
        UserService userService = (UserService) request.getServletContext().getAttribute("userService");
        String principal = (String) request.getSession().getAttribute("principal");

        Optional<User> user = userService.find(principal);

        if (user.isPresent()) {
            response.setContentType(MimeTypes.APPLICATION_JSON);
            response.getWriter()
                    .write(jsonb.toJson(GetBoatsResponse.entityToDtoMapper()
                            .apply(boatService.findAll(user.get()))));
        } else {
            //This should not happen as servlet is after authorization filter.
            throw new IllegalStateException(String.format("No user %s found", principal));
        }
    }

    /**
     * Decodes JSON request and stores new boat.
     *
     * @param request  http request
     * @param response http response
     * @throws IOException if an input or output exception occurred
     */
    private void postUserBoat(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BoatService boatService = (BoatService) request.getServletContext().getAttribute("boatService");
        UserService userService = (UserService) request.getServletContext().getAttribute("userService");
        BoatTypeService boatTypeService = (BoatTypeService) request.getServletContext().getAttribute("boatTypeService");
        String principal = (String) request.getSession().getAttribute("principal");

        Optional<User> user = userService.find(principal);

        if (user.isPresent()) {
            CreateBoatRequest requestBody = jsonb.fromJson(request.getInputStream(), CreateBoatRequest.class);

            Boat boat = CreateBoatRequest
//                    .dtoToEntityMapper(name -> boatTypeService.find(name).orElse(null), user::get)
                    .dtoToEntityMapper()
                    .apply(requestBody);

            try {
                boatService.create(boat);
                //When creating new resource, its location should be returned.
                response.addHeader(HttpHeaders.LOCATION,
                        UrlFactory.createUrl(request, Paths.USER_BOATS, boat.getId().toString()));
                //When creating new resource, appropriate code should be set.
                response.setStatus(HttpServletResponse.SC_CREATED);
            } catch (IllegalArgumentException ex) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            //This should not happen as servlet is after authorization filter.
            throw new IllegalStateException(String.format("No user %s found", principal));
        }
    }

    /**
     * Deletes existing boat denoted by path param.
     *
     * @param request  http request
     * @param response http response
     * @throws IOException if an input or output exception occurred
     */
    private void deleteUserBoat(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BoatService boatService = (BoatService) request.getServletContext().getAttribute("boatService");
        UserService userService = (UserService) request.getServletContext().getAttribute("userService");
        String principal = (String) request.getSession().getAttribute("principal");

        Optional<User> user = userService.find(principal);

        if (user.isPresent()) {
            //Parsed request path is valid with boat pattern and can contain starting and ending '/'.
            Long id = Long.parseLong(ServletUtility.parseRequestPath(request).replaceAll("/", ""));
            Optional<Boat> boat = boatService.find(user.get(), id);

            if (boat.isPresent()) {
                boatService.delete(boat.get().getId());
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            //This should not happen as servlet is after authorization filter.
            throw new IllegalStateException(String.format("No user %s found", principal));
        }
    }

}
