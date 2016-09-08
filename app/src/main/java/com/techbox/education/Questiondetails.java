package com.techbox.education;


/**
 * Created by Chinni on 15-03-2016.
 */
public class Questiondetails {
    String id,correct,question,answer1,answer2,answer3,reference,youtube,position,quecount;
    public Questiondetails(String id, String correct, String question, String answer1, String answer2, String answer3, String answer4, String reference, String youtube,String position,String quecount){
        this.id=id;
        this.question=question;
        this.quecount=quecount;
        this.answer1=answer1;
        this.answer2=answer2;
        this.answer3=answer3;
        this.correct=correct;
        this.position = position;
        this.reference=reference;
        this.youtube=youtube;

    }
}
