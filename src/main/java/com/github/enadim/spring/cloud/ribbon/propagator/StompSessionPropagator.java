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

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

/**
 * {@link StompSession} propagator
 */
@Slf4j
public class StompSessionPropagator extends AbstractAttributesPropagator<StompHeaders> implements StompSession {
    private final StompSession delegate;

    public StompSessionPropagator(StompSession delegate, Set<String> attributesToPropagate) {
        super(attributesToPropagate, StompHeaders::set);
        this.delegate = delegate;
    }

    @Override
    public String getSessionId() {
        return delegate.getSessionId();
    }

    @Override
    public boolean isConnected() {
        return delegate.isConnected();
    }

    @Override
    public void setAutoReceipt(boolean enabled) {
        delegate.setAutoReceipt(enabled);
    }

    @Override
    public Receiptable send(String destination, Object payload) {
        StompHeaders headers = new StompHeaders();
        headers.setDestination(destination);
        return send(headers, payload);
    }

    @Override
    public Receiptable send(StompHeaders headers, Object payload) {
        List<Entry<String, String>> entries = propagate(headers);
        log.trace("propagated {}", entries);
        return delegate.send(headers, payload);
    }

    @Override
    public Subscription subscribe(String destination, StompFrameHandler handler) {
        return delegate.subscribe(destination, new StompFramePropagator(handler, getAttributesToPropagate()));
    }

    @Override
    public Subscription subscribe(StompHeaders headers, StompFrameHandler handler) {
        return delegate.subscribe(headers, new StompFramePropagator(handler, getAttributesToPropagate()));
    }

    @Override
    public Receiptable acknowledge(String messageId, boolean consumed) {
        return delegate.acknowledge(messageId, consumed);
    }

    @Override
    public void disconnect() {
        delegate.disconnect();
    }

}
