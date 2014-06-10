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
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;

/**
 * @author Stephan Knitelius <stephan@knitelius.com>
 */
@Singleton
public class DummyMessageGenerator {

    @EJB
    private MessageBoundary MessageBoundary;

    @Schedule(second = "*/10", minute = "*", hour = "*", persistent = false)
    public void blah() {
        MessageBoundary.addMessage(new Message(new Date().toString(), "blah"));
    }
}
