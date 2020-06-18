package com.johnny.meet_kotlin.test;

import java.util.concurrent.Callable;

/**
 * @author Johnny
 */
public class Main {

    public static void main(String[] args) {
        Thread thread = new Thread(new RunnableImpl());
        thread.start();
    }

    private static class CallableImpl implements Callable<String> {

        @Override
        public String call() throws Exception {
            return null;
        }
    }


    private static class RunnableImpl implements Runnable {

        @Override
        public void run() {
            // ...
        }
    }
}
