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
package com.knitelius.basic.angularjsee.web.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.jboss.weld.Container;
import org.jboss.weld.context.http.HttpConversationContext;

/**
 * Initiates ConversationScope for RESTful AJAX calls.
 *
 *
 * @author Stephan Knitelius <stephan@knitelius.com>
 */
public class AjaxWeldContextFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final String[] cids = (String[]) request.getParameterMap().get("cid");

        final HttpConversationContext conversationContext = getHttpConversationContext();
        conversationContext.associate((HttpServletRequest) request);

        if (cids == null || cids.length == 0 || cids[0] == null || cids[0].length() == 0) {
            //Activate new conversation.
            conversationContext.activate();
        } else {
            //Reactivate existing conversation.
            conversationContext.activate(cids[0]);
        }

        chain.doFilter(request, response);
        conversationContext.invalidate();
        conversationContext.deactivate();
    }

    private static HttpConversationContext getHttpConversationContext() {
        return Container.instance().deploymentManager().instance().select(HttpConversationContext.class).get();
    }

    @Override
    public void destroy() {
    }

}
