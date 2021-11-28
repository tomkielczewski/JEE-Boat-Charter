package pl.edu.pg.eti.kask.boatcharter.boat.service.interceptor;

import pl.edu.pg.eti.kask.boatcharter.boat.entity.Boat;
import pl.edu.pg.eti.kask.boatcharter.boat.service.BoatService;
import pl.edu.pg.eti.kask.boatcharter.boat.service.interceptor.binding.CrudLogger;

import javax.annotation.Priority;
import javax.ejb.EJBAccessException;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.security.enterprise.SecurityContext;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

@Interceptor
@CrudLogger
@Priority(10)
public class CrudLoggerInterceptor implements Serializable {

    /**
     * Security context.
     */
    private SecurityContext securityContext;

//    private BoatService boatService;

    @Inject
    public void setSecurityContext(SecurityContext securityContext) {
        this.securityContext = securityContext;
    }

//    @Inject
//    public void setBoatService(BoatService boatService) {
//        this.boatService = boatService;
//    }

//    private static final Logger log = Logger.getLogger( this.boatService.class.getName() );


//    @AroundInvoke
//    public Object invoke(InvocationContext context) throws Exception {
//        if (authorized(context)) {
//            return context.proceed();
//        } else {
//            throw new EJBAccessException("Authorization failed for user " + securityContext.getCallerPrincipal().getName());
//
//        }
//    }

    @AroundInvoke
    public Object proccessMethod(InvocationContext context) throws Exception {

//        org.apache.logging.log4j.Logger logger =
//                LogManager.getLogger(context.getTarget().getClass());

        Logger logger = Logger.getLogger( context.getTarget().getClass().getName());

        try {
//            logger.debug("Called: " + context.getMethod());
            StringBuilder sb = new StringBuilder("");
            for (Object obj : context.getParameters()) {

                if (obj.getClass().getName().equals("pl.edu.pg.eti.kask.boatcharter.boat.entity.Boat")){
                    sb.append(((Boat) obj).getId());
                }
                else{
                    sb.append(obj.toString());
                }
//                sb.append("Name:" + obj.getClass().getName() + ":::  ");
//                sb.append("IsEqual:" + obj.getClass().getName().equals("pl.edu.pg.eti.kask.boatcharter.boat.entity.Boat") + ":::  ");
//                sb.append(obj.toString());
//                sb.append(", ");
            }
//            sb.append("]");
            logger.log(Level.INFO,
                    "Called: {0} by user: {1}, Boat ID: {2} ",
                    new Object[] { context.getMethod().getName(), securityContext.getCallerPrincipal().getName(),
                            sb.toString() });
            return context.proceed();
        }
        catch(Exception e){
            treatException(e, context);
            throw e;

        }

    }

    private void treatException(Exception e, InvocationContext context){

        Logger logger = Logger.getLogger( context.getTarget().getClass().getName());

        logger.log(Level.WARNING, e.toString());

    }


}
