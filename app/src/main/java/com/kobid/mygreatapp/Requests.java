package com.kobid.mygreatapp;

class Requests {
    private String requestId;
    private String requestUserId;
    private String requestTitle;
    private String requestDescription;
    private String requestArea;
    private String requestStatus="opened";
    private String requestSolvedBy="";

    public Requests() {
    }

    public Requests(String requestId, String requestUserId, String requestTitle, String requestDescription, String requestArea) {
        this.requestId = requestId;
        this.requestTitle = requestTitle;
        this.requestDescription = requestDescription;
        this.requestArea = requestArea;
        this.requestStatus = requestStatus;
        this.requestSolvedBy = requestSolvedBy;
    }

    /*public Requests(Map req) {
        for (Object entry : req.entrySet()){
            this.requestId=req.values();
        }


    }*/


}
