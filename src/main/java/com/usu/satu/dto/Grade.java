package com.usu.satu.dto;

import org.springframework.data.mongodb.core.mapping.Field;

public class Grade {
    @Field("final_test")
    private float finalTest;

    @Field("mid_test")
    private float midTest;

    @Field("quiz")
    private float quiz;

    @Field("case_study")
    private float caseStudy;

    @Field("face_to_face")
    private float faceToFace;

    @Field("field_practical")
    private float fieldPractical;

    private float practical;

    private float simulation;

    private float discussion;

    private String result;

    public Grade() {
    }

    public Grade(float finalTest, float midTest, float quiz, float caseStudy, float faceToFace, float fieldPractical, float practical, float simulation, float discussion, String result) {
        this.finalTest = finalTest;
        this.midTest = midTest;
        this.quiz = quiz;
        this.caseStudy = caseStudy;
        this.faceToFace = faceToFace;
        this.fieldPractical = fieldPractical;
        this.practical = practical;
        this.simulation = simulation;
        this.discussion = discussion;
        this.result = result;
    }

    public float getFinalTest() {
        return finalTest;
    }

    public void setFinalTest(float finalTest) {
        this.finalTest = finalTest;
    }

    public float getMidTest() {
        return midTest;
    }

    public void setMidTest(float midTest) {
        this.midTest = midTest;
    }

    public float getQuiz() {
        return quiz;
    }

    public void setQuiz(float quiz) {
        this.quiz = quiz;
    }

    public float getCaseStudy() {
        return caseStudy;
    }

    public void setCaseStudy(float caseStudy) {
        this.caseStudy = caseStudy;
    }

    public float getFaceToFace() {
        return faceToFace;
    }

    public void setFaceToFace(float faceToFace) {
        this.faceToFace = faceToFace;
    }

    public float getFieldPractical() {
        return fieldPractical;
    }

    public void setFieldPractical(float fieldPractical) {
        this.fieldPractical = fieldPractical;
    }

    public float getPractical() {
        return practical;
    }

    public void setPractical(float practical) {
        this.practical = practical;
    }

    public float getSimulation() {
        return simulation;
    }

    public void setSimulation(float simulation) {
        this.simulation = simulation;
    }

    public float getDiscussion() {
        return discussion;
    }

    public void setDiscussion(float discussion) {
        this.discussion = discussion;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "finalTest=" + finalTest +
                ", midTest=" + midTest +
                ", quiz=" + quiz +
                ", caseStudy=" + caseStudy +
                ", faceToFace=" + faceToFace +
                ", fieldPractical=" + fieldPractical +
                ", practical=" + practical +
                ", simulation=" + simulation +
                ", discussion=" + discussion +
                ", result='" + result + '\'' +
                '}';
    }
}
