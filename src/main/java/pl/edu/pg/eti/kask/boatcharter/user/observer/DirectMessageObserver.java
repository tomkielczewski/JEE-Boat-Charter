package pl.edu.pg.eti.kask.boatcharter.user.observer;

import lombok.extern.java.Log;
import pl.edu.pg.eti.kask.boatcharter.push.context.PushMessageContext;
import pl.edu.pg.eti.kask.boatcharter.push.dto.Message;
import pl.edu.pg.eti.kask.boatcharter.user.observer.event.DirectMessageEvent;
import pl.edu.pg.eti.kask.boatcharter.user.observer.event.model.DirectMessage;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

/**
 * Observer implementation for transferring character between users. It will notify receiving user.
 */
@ApplicationScoped
@Log
public class DirectMessageObserver {

    /**
     * Context for sending push messages.
     */
    private PushMessageContext pushMessageContext;

    @Inject
    public void setPushMessageContext(PushMessageContext pushMessageContext) {
        this.pushMessageContext = pushMessageContext;
    }

    public void processNewMessage(@Observes @DirectMessageEvent DirectMessage dm) {

        pushMessageContext.notifyUser(Message.builder()
                .from(dm.getFromUsername())
                .content(dm.getMessage())
                .build(), dm.getToUsername());
    }

}
