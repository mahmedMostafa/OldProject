package com.reload.petsstore.settings.editprofile;

import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

public class CountingRequestBody extends RequestBody {

    protected RequestBody delegate;
    protected Listener listener;

    protected CountingSink countingSink;

    public CountingRequestBody(RequestBody delegate, Listener listener)
    {
        this.delegate = delegate;
        this.listener = listener;
    }

    @Override
    public MediaType contentType() {
        Log.i("okhttp", "contentType: " + delegate.contentType());
        return delegate.contentType();
    }

    @Override
    public long contentLength() {
        try {
            return delegate.contentLength();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return -1;
    }

    /*
     * Remove HttpLoggingInterceptor Object in your Api Client which will not execute writeTo() function twice.
     * Basically , HttpLoggingInterceptor loads the data buffer first ( for internal logging purpose )
     * by calling writeTo() and then again calls writeTo() for uploading the data to server.*/

    @Override
    public void writeTo(BufferedSink sink) throws IOException
    {

        countingSink = new CountingSink(sink);
        BufferedSink bufferedSink = Okio.buffer(countingSink);

        delegate.writeTo(bufferedSink);

        bufferedSink.flush();
    }

    protected final class CountingSink extends ForwardingSink
    {

        private long bytesWritten = 0;

        public CountingSink(Sink delegate)
        {
            super(delegate);
        }

        @Override
        public void write(Buffer source, long byteCount) throws IOException
        {
            super.write(source, byteCount);

            bytesWritten += byteCount;
            listener.onRequestProgress(bytesWritten, contentLength());
        }

    }

    public static interface Listener
    {
        public void onRequestProgress(long bytesWritten, long contentLength);
    }

}
