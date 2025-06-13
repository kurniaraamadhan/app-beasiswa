package com.millenialzdev.logindanregistervolleymysql;

import android.net.Uri;

public class SelectedFile {
    private String fileName;
    private Uri fileUri;

    public SelectedFile(String fileName, Uri fileUri) {
        this.fileName = fileName;
        this.fileUri = fileUri;
    }

    public String getFileName() {
        return fileName;
    }

    public Uri getFileUri() {
        return fileUri;
    }
}