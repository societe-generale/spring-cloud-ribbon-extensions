/*
 * Copyright (c) 2017 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.enadim.spring.cloud.ribbon.propagator;

import com.github.enadim.spring.cloud.ribbon.api.RibbonRuleContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.MessagePostProcessor;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

/**
 * {@link RibbonRuleContext} Propagator to jms message properties.
 *
 * @author Nadim Benabdenbi
 */
@Slf4j
public class PropagationJmsMessagePostProcessor extends AbstractAttributesPropagator<Message>
        implements MessagePostProcessor {

    public PropagationJmsMessagePostProcessor(Set<String> attributesToPropagate) {
        super(attributesToPropagate, (x, y, z) -> x.setStringProperty(y, z));
    }

    @Override
    public Message postProcessMessage(Message message) throws JMSException {
        List<Entry<String, String>> propagated = propagate(message);
        log.trace("propagated {}", propagated);
        return message;
    }


}
