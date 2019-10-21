package com.triton.voxit.requestpojo;

import java.util.List;

public class CreateQuizAnswersRequest {


    private List<AnswerBean> answer;

    public List<AnswerBean> getAnswer() {
        return answer;
    }

    public void setAnswer(List<AnswerBean> answer) {
        this.answer = answer;
    }

    public static class AnswerBean {
        /**
         * jockey_id : 373
         * vId : 3
         * Qid : 1
         * answers : [{"id":1,"value":"586 BC"}]
         */

        private int jockey_id;
        private int vId;
        private int Qid;
        private List<AnswersBean> answers;

        public int getJockey_id() {
            return jockey_id;
        }

        public void setJockey_id(int jockey_id) {
            this.jockey_id = jockey_id;
        }

        public int getVId() {
            return vId;
        }

        public void setVId(int vId) {
            this.vId = vId;
        }

        public int getQid() {
            return Qid;
        }

        public void setQid(int Qid) {
            this.Qid = Qid;
        }

        public List<AnswersBean> getAnswers() {
            return answers;
        }

        public void setAnswers(List<AnswersBean> answers) {
            this.answers = answers;
        }

        public static class AnswersBean {
            /**
             * id : 1
             * value : 586 BC
             */

            private int id;
            private String value;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }
    }
}
