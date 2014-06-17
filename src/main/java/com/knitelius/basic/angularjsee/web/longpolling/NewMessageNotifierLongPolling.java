/*
 * Copyright 2014 Stephan Knitelius.
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

import com.knitelius.basic.angularjsee.model.Message;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.event.Observes;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

/**
 * Long Polling allows the Server to push notifications to the connected peers.
 * This is necesssary because WebSocket has only become available from JEE 7
 * onwards.
 *
 * @author Stephan Knitelius <stephan@knitelius.com>
 */
@WebServlet(urlPatterns = {"/newmsg"}, asyncSupported = true)
public class NewMessageNotifierLongPolling extends HttpServlet {

    private static final Queue<AsyncContext> peers = new ConcurrentLinkedQueue();

    /**
     * Notifies the Clients that a new Message has been recieved.
     *
     * @param msg Message
     */
    public void notifyClientsAboutNewMessage(@Observes Message msg) {
        for (final AsyncContext ac : peers) {
            try {
                final ServletOutputStream os = ac.getResponse().getOutputStream();
                os.println(msg.getSubject());
                ac.getResponse().flushBuffer();
            } catch (IOException ex) {
                Logger.getLogger(NewMessageNotifierLongPolling.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Register new long polling peers via get request.
     *
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(MediaType.TEXT_HTML);
        response.setStatus(202);
        response.setHeader("Pragma", "no-cache");
        response.setCharacterEncoding("UTF-8");
        response.flushBuffer();

        final AsyncContext ac = request.startAsync(request, response);
        ac.setTimeout(35 * 1000);
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
