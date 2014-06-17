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
package com.knitelius.basic.angularjsee.web.rest;

import com.knitelius.basic.angularjsee.model.Message;
import com.knitelius.basic.angularjsee.service.MessageBoundary;
import java.util.Collection;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Stephan Knitelius <stephan@knitelius.com>
 */
@Path("/message")
public class MessageFacadeRest {

    @EJB
    private MessageBoundary messageBoundary;

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Message getMessageById(@PathParam("id") Integer id) {
        return messageBoundary.getMessageById(id);
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void addMessage(final Message message) {
        messageBoundary.addMessage(message);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void updateMessage(final Message message) {
        messageBoundary.changeMessage(message);
    }

    @DELETE
    @Path("{id}")
    public void removeMessage(@PathParam("id") Integer id) {
        messageBoundary.removeMessage(id);
    }

    @GET
    @Path("/all")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Collection<Message> getAllMessages() {
        return messageBoundary.getMessages();
    }
}
