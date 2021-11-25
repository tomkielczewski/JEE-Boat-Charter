package pl.edu.pg.eti.kask.boatcharter.user.authentication;

import javax.enterprise.context.ApplicationScoped;
import javax.security.enterprise.authentication.mechanism.http.BasicAuthenticationMechanismDefinition;
import javax.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition;
import javax.security.enterprise.authentication.mechanism.http.FormAuthenticationMechanismDefinition;
import javax.security.enterprise.authentication.mechanism.http.LoginToContinue;
import javax.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;

/**
 * Configuration class for security context. There are three authentication mechanism and only one can be used at a time:
 * <ul>
 *     <li>{@link BasicAuthenticationMechanismDefinition} every secured resource is secured with basic auth mechanism,
 *     ideal for REST services (JAX-RS and Servlet). No login forms work but rest services can be used from clients.</li>
 *     <li>{@link FormAuthenticationMechanismDefinition} every secured resource is secured with form auth mechanism,
 *     ideal for HTML web pages (JSF). REST services can not be called from clients as form auth is required.</li>
 *     <li>{@link CustomFormAuthenticationMechanismDefinition} every secured resource is secured with form auth mechanism,
 *     auth form can used custom backend bean method.</li>
 * </ul>
 */
@ApplicationScoped
//@BasicAuthenticationMechanismDefinition(realmName = "Boat Charter")
@FormAuthenticationMechanismDefinition(
        loginToContinue = @LoginToContinue(
                loginPage = "/authentication/form/login.xhtml",
                errorPage = "/authentication/form/login_error.xhtml"
        )
)
//@CustomFormAuthenticationMechanismDefinition(
//        loginToContinue = @LoginToContinue(
//                loginPage = "/authentication/custom/login.xhtml",
//                errorPage = "/authentication/custom/login_error.xhtml"
//        )
//)
@DatabaseIdentityStoreDefinition(
        dataSourceLookup = "jdbc/BoatCharter",
        callerQuery = "select password from users where login = ?",
        groupsQuery = "select role from users_roles where user = ?",
        hashAlgorithm = Pbkdf2PasswordHash.class
)
public class Config {
}
