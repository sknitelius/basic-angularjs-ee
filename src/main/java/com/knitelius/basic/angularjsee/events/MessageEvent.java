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
package com.knitelius.basic.angularjsee.events;

import com.knitelius.basic.angularjsee.model.Message;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 *
 * @author Stephan Knitelius <stephan@knitelius.com>
 */
@JsonSerialize
public class MessageEvent {

    public enum EventType {

        DELETE,
        CREATE,
        UPDATE;
    }

    private EventType eventType;

    private Message message;

    public MessageEvent(EventType eventType, Message message) {
        this.eventType = eventType;
        this.message = message;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
