package com.gdtel.eshore.androidframework.common.util.imageUpload;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;

public class ProgressOutHttpEntity extends HttpEntityWrapper {
	/**进度监听对象**/
    private final ProgressListener listener;
    public ProgressOutHttpEntity(final HttpEntity entity,final ProgressListener listener) {
        super(entity);
        this.listener = listener;
    }

    public static class CountingOutputStream extends FilterOutputStream {

        private final ProgressListener listener;
        private long transferred;

        CountingOutputStream(final OutputStream out,
                final ProgressListener listener) {
            super(out);
            this.listener = listener;
            this.transferred = 0;
        }

        @Override
        public void write(final byte[] b, final int off, final int len)
                throws IOException {
            out.write(b, off, len);
            this.transferred += len;
            this.listener.transferred(this.transferred);
        }

        @Override
        public void write(final int b) throws IOException {
            out.write(b);
            this.transferred++;
            this.listener.transferred(this.transferred);
        }

    }
    
    @Override
    public void writeTo(final OutputStream out) throws IOException {
        this.wrappedEntity.writeTo(out instanceof CountingOutputStream ? out
                : new CountingOutputStream(out, this.listener));
    }
    /**
     * 进度监听器接口
     */
    public interface ProgressListener {
        public void transferred(long transferedBytes);
    }
}