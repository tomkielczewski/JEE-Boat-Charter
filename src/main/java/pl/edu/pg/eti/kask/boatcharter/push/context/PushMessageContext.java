package pl.edu.pg.eti.kask.boatcharter.push.context;

import lombok.extern.java.Log;
import pl.edu.pg.eti.kask.boatcharter.push.dto.Message;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.push.Push;
import javax.faces.push.PushContext;
import javax.inject.Inject;

/**
 * CDI bean realizing push messages. Two channels are available, broadcast (sending to all sessions) and user (sending
 * to sessions started by specified user). At this moment those to channels are using the same handling JS function
 * in main.xhtml JSF template but those can be associated with different ones.
 */
@ApplicationScoped
@Log
public class PushMessageContext {

    /**
     * Channel for sending message to all active sessions.
     */
    @Inject
    @Push(channel = "broadcastChannel")
    private PushContext broadcastChannel;

    /**
     * Channel for sending message to sessions opened by specified user.
     */
    @Inject
    @Push
    private PushContext userChannel;

    /**
     * Send push message to all users.
     *
     * @param message message to be sent
     */
    public void notifyAll(Message message) {
        broadcastChannel.send(message);
    }

    /**
     * Send push message to specified user.
     *
     * @param message  message to be sent
     * @param username message receiver
     */
    public void notifyUser(Message message, String username) {
        userChannel.send(message, username);
    }
}
