package com.example.chscommunityprototype1;
//class for qna post
public class PostDataClass {
    private String dataTopic;
    private String dataQuestion;
    private String dataImage;
    private String useremail;
    private String key;

    public PostDataClass(String dataTopic, String useremail, String dataQuestion, String dataImage) {
        this.useremail = useremail;
        this.dataTopic = dataTopic;
        this.dataQuestion = dataQuestion;
        this.dataImage = dataImage;
    }

    public PostDataClass(){

    }

    public String getKey(){
        return key;
    }

    public void setKey(String key){
        this.key = key;
    }

    public String getUseremail(){
        return useremail;
    }

    public String getDataTopic() {
        return dataTopic;
    }

    public String getDataQuestion() {
        return dataQuestion;
    }

    public String getDataImage() {
        return dataImage;
    }
}
