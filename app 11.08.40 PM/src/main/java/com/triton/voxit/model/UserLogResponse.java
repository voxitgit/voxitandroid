package com.triton.voxit.model;

import java.util.List;

public class UserLogResponse {

    /**
     * Response : [{"jockey_id":"373","date":"9/27/2019","login_count":"1","id":13,"in_time":"4:56:31 PM","out_time":"4:56:56 PM"}]
     * status : Success
     * code : 200
     */

    private String status;
    private int code;
    private List<ResponseBean> Response;

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

    public List<ResponseBean> getResponse() {
        return Response;
    }

    public void setResponse(List<ResponseBean> Response) {
        this.Response = Response;
    }

    public static class ResponseBean {
        /**
         * jockey_id : 373
         * date : 9/27/2019
         * login_count : 1
         * id : 13
         * in_time : 4:56:31 PM
         * out_time : 4:56:56 PM
         */

        private String jockey_id;
        private String date;
        private String login_count;
        private int id;
        private String in_time;
        private String out_time;

        public String getJockey_id() {
            return jockey_id;
        }

        public void setJockey_id(String jockey_id) {
            this.jockey_id = jockey_id;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getLogin_count() {
            return login_count;
        }

        public void setLogin_count(String login_count) {
            this.login_count = login_count;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getIn_time() {
            return in_time;
        }

        public void setIn_time(String in_time) {
            this.in_time = in_time;
        }

        public String getOut_time() {
            return out_time;
        }

        public void setOut_time(String out_time) {
            this.out_time = out_time;
        }
    }
}
