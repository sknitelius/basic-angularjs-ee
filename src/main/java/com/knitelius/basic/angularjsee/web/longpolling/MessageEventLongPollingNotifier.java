/*
 * Copyright 2014 Stephan Knitelius <stephan@knitelius.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.knitelius.basic.angularjsee.web.longpolling;

import com.knitelius.basic.angularjsee.model.MessageEvent;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletOutputStream;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Notifies all registered long polling requests about the Observed
 * MessageEvents.
 *
 * @author Stephan Knitelius <stephan@knitelius.com>
 */
@Singleton
public class MessageEventLongPollingNotifier {

    private final Queue<AsyncContext> peers = new ConcurrentLinkedQueue();

    /**
     * Notifies the Clients that a new Message has been recieved.
     *
     * @param msgEvent
     */
    public void notifyClientsAboutNewMessage(@Observes MessageEvent msgEvent) {
        for (final AsyncContext ac : peers) {
            try {
                final ServletOutputStream os = ac.getResponse().getOutputStream();
                new ObjectMapper().writeValue(os, msgEvent);
                ac.complete();
            } catch (IOException ex) {
                //connection was most likely closed by client, no further action required.
            } finally {
                peers.remove(ac);
            }
        }
    }

    public void registerNewAsyncContext(final AsyncContext ac) {
        ac.addListener(new AsyncListener() {
            @Override
            public void onComplete(AsyncEvent event) throws IOException {
                peers.remove(ac);
            }

            @Override
            public void onTimeout(AsyncEvent event) throws IOException {
                peers.remove(ac);
            }

            @Override
            public void onError(AsyncEvent event) throws IOException {
                peers.remove(ac);
            }

            @Override
            public void onStartAsync(AsyncEvent event) throws IOException {
            }
        });
        peers.add(ac);
    }

}
