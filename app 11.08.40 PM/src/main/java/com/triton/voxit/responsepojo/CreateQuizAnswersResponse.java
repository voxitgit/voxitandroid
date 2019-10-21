package com.triton.voxit.responsepojo;

public class CreateQuizAnswersResponse {


    /**
     * Response : {"id":4,"jockey_id":"373","vId":"3","totalQuestionsCount":"3","correctAnswersCount":"0","pointsGained":"0","createDate":"10/21/2019","createTime":"3:52:08 PM"}
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
         * id : 4
         * jockey_id : 373
         * vId : 3
         * totalQuestionsCount : 3
         * correctAnswersCount : 0
         * pointsGained : 0
         * createDate : 10/21/2019
         * createTime : 3:52:08 PM
         */

        private int id;
        private String jockey_id;
        private String vId;
        private String totalQuestionsCount;
        private String correctAnswersCount;
        private String pointsGained;
        private String createDate;
        private String createTime;

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

        public String getVId() {
            return vId;
        }

        public void setVId(String vId) {
            this.vId = vId;
        }

        public String getTotalQuestionsCount() {
            return totalQuestionsCount;
        }

        public void setTotalQuestionsCount(String totalQuestionsCount) {
            this.totalQuestionsCount = totalQuestionsCount;
        }

        public String getCorrectAnswersCount() {
            return correctAnswersCount;
        }

        public void setCorrectAnswersCount(String correctAnswersCount) {
            this.correctAnswersCount = correctAnswersCount;
        }

        public String getPointsGained() {
            return pointsGained;
        }

        public void setPointsGained(String pointsGained) {
            this.pointsGained = pointsGained;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }
}


