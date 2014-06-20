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
package com.knitelius.basic.angularjsee.model;

import java.io.Serializable;
import javax.json.Json;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Stephan Knitelius <stephan@knitelius.com>
 */
@XmlRootElement
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String subject;

    private String content;

    public Message() {
    }

    public Message(String subject, String content) {
        this.subject = subject;
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String toJson() {
        return Json.createObjectBuilder()
                .add("id", id)
                .add("subject", StringUtils.defaultString(subject))
                .add("content", StringUtils.defaultString(content))
                .build().toString();
    }
}
