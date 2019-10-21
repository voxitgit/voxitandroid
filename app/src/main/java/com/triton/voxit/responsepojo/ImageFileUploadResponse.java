package com.triton.voxit.responsepojo;

import java.util.List;

public class ImageFileUploadResponse {


    private List<ResponseBean> response;

    public List<ResponseBean> getResponse() {
        return response;
    }

    public void setResponse(List<ResponseBean> response) {
        this.response = response;
    }

    public static class ResponseBean {
        /**
         * status : 1
         * message : Uploaded successfully
         * base_path : http://tritontutebox.com/voxit/image_upload/
         * full_path : http://tritontutebox.com/voxit/image_upload/1.png
         */

        private int status;
        private String message;
        private String base_path;
        private String full_path;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getBase_path() {
            return base_path;
        }

        public void setBase_path(String base_path) {
            this.base_path = base_path;
        }

        public String getFull_path() {
            return full_path;
        }

        public void setFull_path(String full_path) {
            this.full_path = full_path;
        }
    }
}
