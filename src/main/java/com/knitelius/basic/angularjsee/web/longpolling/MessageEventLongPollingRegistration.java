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

import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

/**
 * Long Polling registration, this HTTP Servlet allows the client to register
 * its long polling request.
 *
 * @author Stephan Knitelius <stephan@knitelius.com>
 */
@WebServlet(urlPatterns = {"/msgnotification"}, asyncSupported = true, loadOnStartup = 1)
public class MessageEventLongPollingRegistration extends HttpServlet {

    @EJB
    private MessageEventLongPollingNotifier notifier;

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
        notifier.registerNewAsyncContext(ac);
    }
}
