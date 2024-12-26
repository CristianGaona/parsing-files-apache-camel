package com.crisda24.neoris.parsingfilesapachecamel.routes;

import org.apache.camel.spi.CamelEvent;
import org.apache.camel.support.EventNotifierSupport;
import org.smooks.Smooks;
import org.smooks.SmooksFactory;
import org.smooks.engine.DefaultApplicationContextBuilder;

import java.io.InputStream;
import java.util.concurrent.CountDownLatch;

public class StartStopEventNotifier extends EventNotifierSupport {
    private final CountDownLatch exchangeCompletedEventCountDownLatch = new CountDownLatch(1);

    @Override
    public void notify(CamelEvent event) {
        if (event instanceof CamelEvent.CamelContextInitializedEvent) {
            getCamelContext().getRegistry().bind(SmooksFactory.class.getName(), new SmooksFactory() {
                @Override
                public Smooks createInstance() {
                    return new Smooks(new DefaultApplicationContextBuilder().withClassLoader(getClass().getClassLoader()).build());
                }

                @Override
                public Smooks createInstance(InputStream config) {
                    return null;
                }

                @Override
                public Smooks createInstance(String config) {
                    return null;
                }
            });
        } else if (event instanceof CamelEvent.ExchangeCompletedEvent) {
            exchangeCompletedEventCountDownLatch.countDown();
        }
    }

    public void waitUntilFinish() throws InterruptedException {
        exchangeCompletedEventCountDownLatch.await();
    }
}
