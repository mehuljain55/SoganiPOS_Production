package com.Soganis.Model;

import com.Soganis.Entity.User;
import org.springframework.web.multipart.MultipartFile;

public class FileUploadModel {
    private MultipartFile file;
    private User user;

    public FileUploadModel(MultipartFile file, User user) {
        this.file = file;
        this.user = user;
    }

    public FileUploadModel() {
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
