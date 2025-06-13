package com.millenialzdev.logindanregistervolleymysql;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import android.webkit.MimeTypeMap; // Import ini

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MultipartRequest extends Request<String> {
    private final Response.Listener<String> mListener;
    private final Response.ErrorListener mErrorListener;
    private final Map<String, String> mHeaders;
    private final String mMimeType = "multipart/form-data;boundary=" + BOUNDARY;

    private static final String PROTOCOL_CHARSET = "utf-8";
    private static final String BOUNDARY = "apiclient-" + System.currentTimeMillis();
    private static final String LINE_FEED = "\r\n";

    private File mFilePart;
    private String mFilePartName; // Tambahkan ini: nama parameter untuk file di form data
    private Map<String, String> mStringParts;

    /**
     * Konstruktor untuk upload file.
     * @param url URL to fetch the string contents from
     * @param listener Listener to receive the String response
     * @param errorListener Error listener, or null to ignore errors
     * @param filePart The file to upload
     * @param filePartName The name of the file part in the form data (e.g., "file_berkas", "profile_photo")
     * @param stringParts Additional String parameters to send
     */
    public MultipartRequest(String url, Response.Listener<String> listener,
                            Response.ErrorListener errorListener, File filePart,
                            String filePartName, // <-- BARU
                            Map<String, String> stringParts) {
        super(Method.POST, url, errorListener);
        this.mListener = listener;
        this.mErrorListener = errorListener;
        this.mFilePart = filePart;
        this.mFilePartName = filePartName; // <-- Inisialisasi BARU
        this.mStringParts = stringParts;
        this.mHeaders = Collections.emptyMap();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders;
    }

    @Override
    public String getBodyContentType() {
        return mMimeType;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            // Add string parts
            for (Map.Entry<String, String> entry : mStringParts.entrySet()) {
                buildStringPart(bos, entry.getKey(), entry.getValue());
            }

            // Add file part
            if (mFilePart != null) {
                // Gunakan mFilePartName di sini
                buildFilePart(bos, mFilePart, mFilePartName);
            }

            // Add closing boundary
            bos.write(("--" + BOUNDARY + "--" + LINE_FEED).getBytes());

        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    private void buildStringPart(ByteArrayOutputStream bos, String parameterName, String parameterValue) throws IOException {
        bos.write(("--" + BOUNDARY + LINE_FEED).getBytes());
        bos.write(("Content-Disposition: form-data; name=\"" + parameterName + "\"" + LINE_FEED).getBytes());
        bos.write(("Content-Type: text/plain; charset=" + PROTOCOL_CHARSET + LINE_FEED).getBytes());
        bos.write(LINE_FEED.getBytes());
        bos.write(parameterValue.getBytes(PROTOCOL_CHARSET));
        bos.write(LINE_FEED.getBytes());
    }

    private void buildFilePart(ByteArrayOutputStream bos, File file, String fileName) throws IOException {
        bos.write(("--" + BOUNDARY + LINE_FEED).getBytes());
        bos.write(("Content-Disposition: form-data; name=\"" + fileName + "\"; filename=\"" + file.getName() + "\"" + LINE_FEED).getBytes());
        bos.write(("Content-Type: " + getMimeType(file.getAbsolutePath()) + LINE_FEED).getBytes());
        bos.write(("Content-Transfer-Encoding: binary" + LINE_FEED).getBytes());
        bos.write(LINE_FEED.getBytes());
        byte[] fileBytes = readFileBytes(file);
        bos.write(fileBytes);
        bos.write(LINE_FEED.getBytes());
    }

    private byte[] readFileBytes(File file) throws IOException {
        ByteArrayOutputStream ous = null;
        java.io.InputStream ios = null;
        try {
            byte[] buffer = new byte[4096];
            ous = new ByteArrayOutputStream();
            ios = new java.io.FileInputStream(file);
            int read;
            while ((read = ios.read(buffer)) != -1) {
                ous.write(buffer, 0, read);
            }
        } finally {
            try {
                if (ous != null)
                    ous.close();
            } catch (IOException e) {
            }
            try {
                if (ios != null)
                    ios.close();
            } catch (IOException e) {
            }
        }
        return ous.toByteArray();
    }

    private String getMimeType(String filePath) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(filePath);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type != null ? type : "application/octet-stream";
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(jsonString, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new com.android.volley.ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }
}