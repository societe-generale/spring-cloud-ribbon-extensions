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
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import static com.github.enadim.spring.cloud.ribbon.api.RibbonRuleContextHolder.current;
import static com.github.enadim.spring.cloud.ribbon.api.RibbonRuleContextHolder.remove;

/**
 * {@link StompFrameHandler} propagator
 *
 * @author Nadim Benabdenbi
 */
@Slf4j
public class StompFramePropagator implements StompFrameHandler {
    private final StompFrameHandler delegate;
    private final Set<String> attributesToPropagate;

    public StompFramePropagator(@NotNull StompFrameHandler delegate, @NotNull Set<String> attributesToPropagate) {
        this.delegate = delegate;
        this.attributesToPropagate = attributesToPropagate;
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return delegate.getPayloadType(headers);
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        RibbonRuleContext context = current();
        List<Entry<String, String>> entries = headers.toSingleValueMap().entrySet().stream()
                .filter(x -> attributesToPropagate.contains(x.getKey()))
                .collect(Collectors.toList());
        entries.forEach(x -> context.put(x.getKey(), x.getValue()));
        log.trace("propagated {}", entries);
        delegate.handleFrame(headers, payload);
        remove();
    }
}
