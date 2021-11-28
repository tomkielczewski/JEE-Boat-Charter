package pl.edu.pg.eti.kask.boatcharter.user.observer;

import lombok.extern.java.Log;
import pl.edu.pg.eti.kask.boatcharter.push.context.PushMessageContext;
import pl.edu.pg.eti.kask.boatcharter.push.dto.Message;
import pl.edu.pg.eti.kask.boatcharter.user.observer.event.BroadcastMessageEvent;
import pl.edu.pg.eti.kask.boatcharter.user.observer.event.model.DirectMessage;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;

/**
 * Observer implementation for adding new profession. It will notify all users about it.
 */
@ApplicationScoped
@Log
public class BroadcastMessageObserver {

    /**
     * Context for sending push messages.
     */
    private PushMessageContext pushMessageContext;


    @Inject
    public void setPushMessageContext(PushMessageContext pushMessageContext) {
        this.pushMessageContext = pushMessageContext;
    }

    public void processNewMessage(@Observes @BroadcastMessageEvent DirectMessage dm) {

        pushMessageContext.notifyAll(Message.builder()
                .from(dm.getFromUsername())
                .content(dm.getMessage())
                .build());
    }

}
