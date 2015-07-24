package com.feifan.bp.net;

import android.content.Context;

/**
 * Created by maning on 15/7/28.
 */
public class HttpEngine {

    private BaseRequest mRequest;
    private HttpProcessor mProcessor;

    public void setRequest(BaseRequest request) {
        mRequest = request;
    }

    public void setProcessor(HttpProcessor processor) {
        mProcessor = processor;
    }

    public void start() {
        mProcessor.process(mRequest);
    }

    public static class Builder {
        private HttpEngine mEngine;
        public static Builder newInstance(Context context) {
            return new Builder(context);
        }
        private Builder(Context context) {
            mEngine = new HttpEngine();
            mEngine.setProcessor(DefaultHttpProcessor.instance(context));
        }

        public Builder setRequest(BaseRequest request) {
            mEngine.setRequest(request);
            return this;
        }

        public Builder setProcessor(DefaultHttpProcessor processor) {
            mEngine.setProcessor(processor);
            return this;
        }

        public HttpEngine build() {
            return mEngine;
        }
    }
}
