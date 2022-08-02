package com.usu.satu.dto;

import java.lang.reflect.Array;
import java.time.LocalTime;
import java.util.Arrays;

public class DetailCourseSchedule {

    private String lectureNip;
    private String lectureName;
    private String lectureFrontDegree;
    private String lectureBehindDegree;
    private String courseName;
    private String courseCode;
    private String className;
    private String classCode;
    private String roomName;
    private String roomCode;
    private LocalTime classStart;
    private LocalTime classEnd;
//    private String[] classTypes;
    private Integer credit;

    public DetailCourseSchedule(String lectureNip, String lectureName, String lectureFrontDegree, String lectureBehindDegree, String courseName, String courseCode, String className, String classCode, String roomName, String roomCode, LocalTime classStart, LocalTime classEnd, Integer credit) {
        this.lectureNip = lectureNip;
        this.lectureName = lectureName;
        this.lectureFrontDegree = lectureFrontDegree;
        this.lectureBehindDegree = lectureBehindDegree;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.className = className;
        this.classCode = classCode;
        this.roomName = roomName;
        this.roomCode = roomCode;
        this.classStart = classStart;
        this.classEnd = classEnd;
        this.credit = credit;
    }

    public DetailCourseSchedule() {
    }

    public String getLectureNip() {
        return lectureNip;
    }

    public void setLectureNip(String lectureNip) {
        this.lectureNip = lectureNip;
    }

    public String getLectureName() {
        return lectureName;
    }

    public void setLectureName(String lectureName) {
        this.lectureName = lectureName;
    }

    public String getLectureFrontDegree() {
        return lectureFrontDegree;
    }

    public void setLectureFrontDegree(String lectureFrontDegree) {
        this.lectureFrontDegree = lectureFrontDegree;
    }

    public String getLectureBehindDegree() {
        return lectureBehindDegree;
    }

    public void setLectureBehindDegree(String lectureBehindDegree) {
        this.lectureBehindDegree = lectureBehindDegree;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public LocalTime getClassStart() {
        return classStart;
    }

    public void setClassStart(LocalTime classStart) {
        this.classStart = classStart;
    }

    public LocalTime getClassEnd() {
        return classEnd;
    }

    public void setClassEnd(LocalTime classEnd) {
        this.classEnd = classEnd;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    @Override
    public String toString() {
        return "DetailCourse{" +
                "lectureNip='" + lectureNip + '\'' +
                ", lectureName='" + lectureName + '\'' +
                ", lectureFrontDegree='" + lectureFrontDegree + '\'' +
                ", lectureBehindDegree='" + lectureBehindDegree + '\'' +
                ", courseName='" + courseName + '\'' +
                ", courseCode='" + courseCode + '\'' +
                ", className='" + className + '\'' +
                ", classCode='" + classCode + '\'' +
                ", roomName='" + roomName + '\'' +
                ", roomCode='" + roomCode + '\'' +
                ", classStart=" + classStart +
                ", classEnd=" + classEnd +
                ", credit=" + credit +
                '}';
    }
}
