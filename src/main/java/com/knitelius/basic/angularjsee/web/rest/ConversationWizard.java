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
package com.knitelius.basic.angularjsee.web.rest;

import java.io.Serializable;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Allows the management of CDI Conversations via HTTP GET and DELETE.
 *
 * @author Stephan Knitelius <stephan@knitelius.com>
 */
@ConversationScoped
@Path("/conversationwizard")
public class ConversationWizard implements Serializable {

    @Inject
    private Conversation conversation;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String startConversation() {
        if (conversation.isTransient()) {
            conversation.begin();
        }
        return conversation.getId();
    }

    @DELETE
    public void endConversation() {
        conversation.end();
    }
}
