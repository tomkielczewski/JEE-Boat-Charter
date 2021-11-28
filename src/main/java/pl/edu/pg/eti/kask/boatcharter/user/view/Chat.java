package pl.edu.pg.eti.kask.boatcharter.user.view;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import pl.edu.pg.eti.kask.boatcharter.boat.model.BoatCreateModel;
import pl.edu.pg.eti.kask.boatcharter.user.notification.service.NewsletterService;
import pl.edu.pg.eti.kask.boatcharter.user.observer.event.BroadcastMessageEvent;
import pl.edu.pg.eti.kask.boatcharter.user.observer.event.DirectMessageEvent;
import pl.edu.pg.eti.kask.boatcharter.user.observer.event.model.DirectMessage;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.Password;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.Serializable;

import static javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters.withParams;

@RequestScoped
@Named
@Log
public class Chat implements Serializable {

    private NewsletterService newsletterService;

    private SecurityContext securityContext;

    private Event<DirectMessage> broadcastMessageEvent;

    private Event<DirectMessage> directMessageEvent;


    @Inject
    public Chat(@BroadcastMessageEvent Event<DirectMessage> broadcastMessageEvent,
                @DirectMessageEvent Event<DirectMessage> directMessageEvent,
                SecurityContext securityContext) {
        this.broadcastMessageEvent = broadcastMessageEvent;
        this.directMessageEvent = directMessageEvent;
        this.securityContext = securityContext;
    }


    @Getter
    @Setter
    private String message;

    @Getter
    @Setter
    private String receiver;


    public void sendBroadCast() {
        broadcastMessageEvent.fire(DirectMessage.builder()
                .message(message)
                .fromUsername(securityContext.getCallerPrincipal().getName())
                .build());
        this.message = null;
    }

    public void send() {
        directMessageEvent.fire(DirectMessage.builder()
                .message(message)
                .fromUsername(securityContext.getCallerPrincipal().getName())
                .toUsername(receiver)
                .build());
        directMessageEvent.fire(DirectMessage.builder()
                .message(message)
                .fromUsername(securityContext.getCallerPrincipal().getName())
                .toUsername(securityContext.getCallerPrincipal().getName())
                .build());
        this.message = null;
        this.receiver = null;
    }
}
