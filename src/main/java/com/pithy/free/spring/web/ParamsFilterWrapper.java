package com.pithy.free.spring.web;

import com.pithy.free.spring.session.SubjectUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.StandardCharsets;

// 重写request请求
public class ParamsFilterWrapper extends HttpServletRequestWrapper {

    private final byte[] body;

    public ParamsFilterWrapper(HttpServletRequest request) {
        super(request);
        String bodyString = getBodyString(request);
        body = bodyString.getBytes(StandardCharsets.UTF_8);
    }

    // 获取请求Body
    public String getBodyString(final ServletRequest request) {
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = cloneInputStream(request.getInputStream());
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    // 复制输入流
    public InputStream cloneInputStream(ServletInputStream inputStream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buffer)) > -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            byteArrayOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        Object data_byte = super.getAttribute(SubjectUtils.CTX_SESSION_VALID);
        if (data_byte == null) {
            final ByteArrayInputStream bais = new ByteArrayInputStream(body);
            return new AuthServletInputStream(bais);
        } else {
            final ByteArrayInputStream bais = new ByteArrayInputStream((byte[]) data_byte);
            return new AuthServletInputStream(bais);
        }
    }

    class AuthServletInputStream extends ServletInputStream {

        private ByteArrayInputStream bis;

        public AuthServletInputStream(ByteArrayInputStream bis) {
            this.bis = bis;
        }

        @Override
        public int read() throws IOException {
            return bis.read();
        }

        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
        }
    }
}