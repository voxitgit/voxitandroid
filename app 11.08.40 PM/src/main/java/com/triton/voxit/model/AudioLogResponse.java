package com.triton.voxit.model;

public class AudioLogResponse {

    /**
     * Response : {"id":2,"jockey_id":"373","audio_id":"211","create_date":"9/27/2019","create_time":"5:17:33 PM","listened_time":"0","listened_count":"2"}
     * status : Success
     * code : 200
     */

    private ResponseBean Response;
    private String status;
    private int code;

    public ResponseBean getResponse() {
        return Response;
    }

    public void setResponse(ResponseBean Response) {
        this.Response = Response;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static class ResponseBean {
        /**
         * id : 2
         * jockey_id : 373
         * audio_id : 211
         * create_date : 9/27/2019
         * create_time : 5:17:33 PM
         * listened_time : 0
         * listened_count : 2
         */

        private int id;
        private String jockey_id;
        private String audio_id;
        private String create_date;
        private String create_time;
        private String listened_time;
        private String listened_count;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getJockey_id() {
            return jockey_id;
        }

        public void setJockey_id(String jockey_id) {
            this.jockey_id = jockey_id;
        }

        public String getAudio_id() {
            return audio_id;
        }

        public void setAudio_id(String audio_id) {
            this.audio_id = audio_id;
        }

        public String getCreate_date() {
            return create_date;
        }

        public void setCreate_date(String create_date) {
            this.create_date = create_date;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getListened_time() {
            return listened_time;
        }

        public void setListened_time(String listened_time) {
            this.listened_time = listened_time;
        }

        public String getListened_count() {
            return listened_count;
        }

        public void setListened_count(String listened_count) {
            this.listened_count = listened_count;
        }
    }
}
