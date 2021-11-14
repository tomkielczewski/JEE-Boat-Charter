package pl.edu.pg.eti.kask.boatcharter.boat.servlet;

import pl.edu.pg.eti.kask.boatcharter.boat.entity.Boat;
import pl.edu.pg.eti.kask.boatcharter.boat.service.BoatService;
import pl.edu.pg.eti.kask.boatcharter.servlet.HttpHeaders;
import pl.edu.pg.eti.kask.boatcharter.servlet.MimeTypes;
import pl.edu.pg.eti.kask.boatcharter.servlet.ServletUtility;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Optional;

/**
 * Servlet for serving and uploading boats' pictures i raster image format.
 */
@WebServlet(urlPatterns = PictureServlet.Paths.PICTURES + "/*")
@MultipartConfig(maxFileSize = 200 * 1024)
public class PictureServlet extends HttpServlet {

    /**
     * Definition of paths supported by this servlet. Separate inner class provides composition for static fields.
     */
    public static class Paths {

        /**
         * Specified picture for download and upload.
         */
        public static final String PICTURES = "/api/pictures";

    }

    /**
     * Definition of regular expression patterns supported by this servlet. Separate inner class provides composition
     * for static fields. Whereas servlet activation path can be compared to {@link Paths} the path info (denoted by
     * wildcard in paths) can be compared using regular expressions.
     */
    public static class Patterns {

        /**
         * Specified picture (for download).
         */
        public static final String PICTURE = "^/[0-9]+/?$";

    }

    /**
     * Request parameters (both query params and request parts) which can be sent by the client.
     */
    public static class Parameters {

        /**
         * Picture image part.
         */
        public static final String PICTURE = "picture";

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = ServletUtility.parseRequestPath(request);
        String servletPath = request.getServletPath();
        if (Paths.PICTURES.equals(servletPath)) {
            if (path.matches(Patterns.PICTURE)) {
                getPicture(request, response);
                return;
            }
        }
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = ServletUtility.parseRequestPath(request);
        String servletPath = request.getServletPath();
        if (Paths.PICTURES.equals(servletPath)) {
            if (path.matches(Patterns.PICTURE)) {
                putPicture(request, response);
                return;
            }
        }
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    /**
     * Updates boat's picture. Receives picture bytes from request and stores them in the data storage.
     *
     * @param request  http request
     * @param response http response
     * @throws IOException      if any input or output exception occurred
     * @throws ServletException if this request is not of type multipart/form-data
     */
    private void putPicture(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        BoatService service = (BoatService) request.getServletContext().getAttribute("boatService");

        //Parsed request path is valid with boat pattern and can contain starting and ending '/'.
        Long id = Long.parseLong(ServletUtility.parseRequestPath(request).replaceAll("/", ""));
        Optional<Boat> boat = service.find(id);

        if (boat.isPresent()) {
            Part picture = request.getPart(Parameters.PICTURE);
            if (picture != null) {
                service.updatePicture(id, picture.getInputStream());
            }
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /**
     * Fetches picture as byte array from data storage and sends is through http protocol.
     *
     * @param request  http request
     * @param response http response
     * @throws IOException if any input or output exception occurred
     */
    private void getPicture(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BoatService service = (BoatService) request.getServletContext().getAttribute("boatService");

        //Parsed request path is valid with boat pattern and can contain starting and ending '/'.
        Long id = Long.parseLong(ServletUtility.parseRequestPath(request).replaceAll("/", ""));
        Optional<Boat> boat = service.find(id);

        if (boat.isPresent()) {
            //Type should be stored in the database but in this project we assume everything to be png.
            response.addHeader(HttpHeaders.CONTENT_TYPE, MimeTypes.IMAGE_PNG);
            response.setContentLength(boat.get().getPicture().length);
            response.getOutputStream().write(boat.get().getPicture());
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

}
