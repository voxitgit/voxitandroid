package com.triton.voxit.model;

import java.util.List;

public class ResponseBeanYuvi {

    /**
     * TopThreePerformers : [{"Rank":1,"Name":"TinTin","Image":"https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcTL7saqA1GZ8wUb9TUTGZzPm3aGpR_hNXJOSwsGAzxjRo0a0fPL"},{"Rank":2,"Name":"Naruto","Image":"https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcR5xcspxX_ZZjNxKSchevclRGrXCN_GCqdqqfsAyeopSoIlf2cx"},{"Rank":3,"Name":"Ichigo","Image":"https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQA1wmiaCqoHXt4stSP_CB5BOwL2FtCbPPYMJQf9OqEQ3LMdOQy"}]
     * Response : [{"vId":3,"eventTime":"10","eventType":"Quiz 1","eventName":"Diwali - 1","startDate":"18/10/2019","startTime":"5:39:17 PM","starts":"18/10/2019 5:39:17 PM","endDate":"28/10/2019","endTime":"6:39:17 PM","ends":"28/10/2019 6:39:17 PM","isClosed":false,"Event":[{"Qid":1,"title":"When was Lord Buddha born?","options":[{"id":1,"value":"586 BC"},{"id":2,"value":"1000 BC"},{"id":3,"value":"560 BC"},{"id":4,"value":"750 BC"}],"quizType":"SelectOne","audio_path":"http://tritontutebox.com/voxit/audio_upload/19 Century Train man.mp3","audio_id":"677","QPatternId":"3"},{"Qid":2,"title":"When was Lord Buddha born?","options":[{"id":1,"value":"586 BC"},{"id":2,"value":"1000 BC"},{"id":3,"value":"560 BC"},{"id":4,"value":"750 BC"}],"quizType":"SelectOne","audio_path":"http://tritontutebox.com/voxit/audio_upload/19 Century Train man.mp3","audio_id":"677","QPatternId":"3"},{"Qid":3,"title":"When was Lord Buddha born?","options":[{"id":1,"value":"Yes"},{"id":2,"value":"No"}],"quizType":"TrueOrFalse","audio_path":"http://tritontutebox.com/voxit/audio_upload/19 Century Train man.mp3","audio_id":"677","QPatternId":"3"}]},{"vId":4,"eventTime":"10","eventType":"Quiz 2","eventName":"Diwali - 2","startDate":"27/10/2019","startTime":"5:39:17 PM","starts":"27/10/2019 5:39:17 PM","endDate":"05/11/2019","endTime":"5:39:17 PM","ends":"05/11/2019 5:39:17 PM","isClosed":false,"Event":[{"Qid":4,"title":"When was Lord Buddha born?","options":[{"id":1,"value":"Yes"},{"id":2,"value":"No"}],"quizType":"TrueOrFalse","audio_path":"http://tritontutebox.com/voxit/audio_upload/19 Century Train man.mp3","audio_id":"677","QPatternId":"4"},{"Qid":5,"title":"When was Lord Buddha born?","options":[{"id":1,"value":"Sillimanite, nickel, petroleum"},{"id":2,"value":"Oil, coal, manganese"},{"id":3,"value":"Iron, lime, bauxite"},{"id":4,"value":"None of the above"}],"quizType":"TrueOrFalse","audio_path":"http://tritontutebox.com/voxit/audio_upload/19 Century Train man.mp3","audio_id":"677","QPatternId":"4"},{"Qid":6,"title":"When was Lord Buddha born?","options":[{"id":1,"value":"Sillimanite, nickel, petroleum"},{"id":2,"value":"Oil, coal, manganese"},{"id":3,"value":"Iron, lime, bauxite"},{"id":4,"value":"None of the above"}],"quizType":"TrueOrFalse","audio_path":"http://tritontutebox.com/voxit/audio_upload/19 Century Train man.mp3","audio_id":"677","QPatternId":"4"}]}]
     * status : Success
     * code : 200
     */

    private String status;
    private int code;
    private List<TopThreePerformersBean> TopThreePerformers;
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

    public List<TopThreePerformersBean> getTopThreePerformers() {
        return TopThreePerformers;
    }

    public void setTopThreePerformers(List<TopThreePerformersBean> TopThreePerformers) {
        this.TopThreePerformers = TopThreePerformers;
    }

    public List<ResponseBean> getResponse() {
        return Response;
    }

    public void setResponse(List<ResponseBean> Response) {
        this.Response = Response;
    }

    public static class TopThreePerformersBean {
        /**
         * Rank : 1
         * Name : TinTin
         * Image : https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcTL7saqA1GZ8wUb9TUTGZzPm3aGpR_hNXJOSwsGAzxjRo0a0fPL
         */

        private int Rank;
        private String Name;
        private String Image;

        public int getRank() {
            return Rank;
        }

        public void setRank(int Rank) {
            this.Rank = Rank;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getImage() {
            return Image;
        }

        public void setImage(String Image) {
            this.Image = Image;
        }
    }

    public static class ResponseBean {
        /**
         * vId : 3
         * eventTime : 10
         * eventType : Quiz 1
         * eventName : Diwali - 1
         * startDate : 18/10/2019
         * startTime : 5:39:17 PM
         * starts : 18/10/2019 5:39:17 PM
         * endDate : 28/10/2019
         * endTime : 6:39:17 PM
         * ends : 28/10/2019 6:39:17 PM
         * isClosed : false
         * Event : [{"Qid":1,"title":"When was Lord Buddha born?","options":[{"id":1,"value":"586 BC"},{"id":2,"value":"1000 BC"},{"id":3,"value":"560 BC"},{"id":4,"value":"750 BC"}],"quizType":"SelectOne","audio_path":"http://tritontutebox.com/voxit/audio_upload/19 Century Train man.mp3","audio_id":"677","QPatternId":"3"},{"Qid":2,"title":"When was Lord Buddha born?","options":[{"id":1,"value":"586 BC"},{"id":2,"value":"1000 BC"},{"id":3,"value":"560 BC"},{"id":4,"value":"750 BC"}],"quizType":"SelectOne","audio_path":"http://tritontutebox.com/voxit/audio_upload/19 Century Train man.mp3","audio_id":"677","QPatternId":"3"},{"Qid":3,"title":"When was Lord Buddha born?","options":[{"id":1,"value":"Yes"},{"id":2,"value":"No"}],"quizType":"TrueOrFalse","audio_path":"http://tritontutebox.com/voxit/audio_upload/19 Century Train man.mp3","audio_id":"677","QPatternId":"3"}]
         */

        private int vId;
        private String eventTime;
        private String eventType;
        private String eventName;
        private String startDate;
        private String startTime;
        private String starts;
        private String endDate;
        private String endTime;
        private String ends;
        private boolean isClosed;
        private List<EventBean> Event;

        public int getVId() {
            return vId;
        }

        public void setVId(int vId) {
            this.vId = vId;
        }

        public String getEventTime() {
            return eventTime;
        }

        public void setEventTime(String eventTime) {
            this.eventTime = eventTime;
        }

        public String getEventType() {
            return eventType;
        }

        public void setEventType(String eventType) {
            this.eventType = eventType;
        }

        public String getEventName() {
            return eventName;
        }

        public void setEventName(String eventName) {
            this.eventName = eventName;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getStarts() {
            return starts;
        }

        public void setStarts(String starts) {
            this.starts = starts;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getEnds() {
            return ends;
        }

        public void setEnds(String ends) {
            this.ends = ends;
        }

        public boolean isIsClosed() {
            return isClosed;
        }

        public void setIsClosed(boolean isClosed) {
            this.isClosed = isClosed;
        }

        public List<EventBean> getEvent() {
            return Event;
        }

        public void setEvent(List<EventBean> Event) {
            this.Event = Event;
        }

        public static class EventBean {
            /**
             * Qid : 1
             * title : When was Lord Buddha born?
             * options : [{"id":1,"value":"586 BC"},{"id":2,"value":"1000 BC"},{"id":3,"value":"560 BC"},{"id":4,"value":"750 BC"}]
             * quizType : SelectOne
             * audio_path : http://tritontutebox.com/voxit/audio_upload/19 Century Train man.mp3
             * audio_id : 677
             * QPatternId : 3
             */

            private int Qid;
            private String title;
            private String quizType;
            private String audio_path;
            private String audio_id;
            private String QPatternId;
            private List<OptionsBean> options;

            public int getQid() {
                return Qid;
            }

            public void setQid(int Qid) {
                this.Qid = Qid;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getQuizType() {
                return quizType;
            }

            public void setQuizType(String quizType) {
                this.quizType = quizType;
            }

            public String getAudio_path() {
                return audio_path;
            }

            public void setAudio_path(String audio_path) {
                this.audio_path = audio_path;
            }

            public String getAudio_id() {
                return audio_id;
            }

            public void setAudio_id(String audio_id) {
                this.audio_id = audio_id;
            }

            public String getQPatternId() {
                return QPatternId;
            }

            public void setQPatternId(String QPatternId) {
                this.QPatternId = QPatternId;
            }

            public List<OptionsBean> getOptions() {
                return options;
            }

            public void setOptions(List<OptionsBean> options) {
                this.options = options;
            }

            public static class OptionsBean {
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
}
