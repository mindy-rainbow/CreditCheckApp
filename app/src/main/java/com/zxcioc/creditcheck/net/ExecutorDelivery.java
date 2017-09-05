package com.zxcioc.creditcheck.net;

import android.os.Handler;

import java.util.concurrent.Executor;

/**
 * Created by zw on 16/8/3.
 */
public class ExecutorDelivery {
    /**
     * Used for posting responses, typically to the main thread.
     */
    private final Executor mResponsePoster;

    /**
     * Creates a new response delivery interface.
     *
     * @param handler {@link Handler} to post responses on
     */
    public ExecutorDelivery(final Handler handler) {
        // Make an Executor that just wraps the handler.
        mResponsePoster = new Executor() {
            @Override
            public void execute(Runnable command) {
                handler.post(command);
            }
        };
    }

    public ExecutorDelivery(Executor executor) {
        mResponsePoster = executor;
    }


    public void postResponse(HttpCallback httpCallback, BaseRequest request, HttpResponse response, Exception e) {
        mResponsePoster.execute(ResponseDeliveryRunnable2.response(httpCallback, request, response, e));
    }


    private static class ResponseDeliveryRunnable2 implements Runnable {
        private final BaseRequest request;
        private final Exception e;
        private HttpResponse httpResponse;
        private HttpCallback callback;

        private ResponseDeliveryRunnable2(HttpCallback callback, BaseRequest request, HttpResponse response, Exception e) {
            this.callback = callback;
            this.request = request;
            this.e = null;
            this.httpResponse = response;
        }


        public static ExecutorDelivery.ResponseDeliveryRunnable2 response(HttpCallback modelCallback, BaseRequest request, HttpResponse response, Exception e) {
            return new ExecutorDelivery.ResponseDeliveryRunnable2(modelCallback, request, response, e);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void run() {
            if (e == null && (httpResponse.code == null || httpResponse.code != ResponseCode.NET_ERROR)) {
                this.callback.onResponse(request, httpResponse.data);
            } else {
                this.callback.onFailure(request, e, httpResponse.code, httpResponse.message);
            }

        }
    }


}
