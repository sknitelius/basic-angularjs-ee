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
package com.knitelius.basic.angularjsee.service;

import com.knitelius.basic.angularjsee.model.Message;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javax.ejb.Singleton;
import javax.enterprise.event.Event;
import javax.inject.Inject;

/**
 *
 * @author Stephan Knitelius <stephan@knitelius.com>
 */
@Singleton
public class MessageBoundary {

    private final Map<Integer, Message> messageStore = new HashMap<>();

    private final AtomicInteger nextId = new AtomicInteger();

    @Inject
    private Event<Message> newMsgEvent;

    public void addMessage(final Message msg) {
        msg.setId(nextId.getAndIncrement());
        messageStore.put(msg.getId(), msg);
        newMsgEvent.fire(msg);
    }

    public Message getMessageById(final Integer id) {
        return messageStore.get(id);
    }

    public Collection<Message> getMessages() {
        return messageStore.values();
    }

    public void changeMessage(final Message msg) {
        messageStore.put(msg.getId(), msg);
    }

    public void removeMessage(final Integer id) {
        messageStore.remove(id);
    }

}
