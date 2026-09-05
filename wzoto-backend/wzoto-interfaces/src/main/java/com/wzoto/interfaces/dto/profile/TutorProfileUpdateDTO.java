package com.wzoto.interfaces.dto.profile;

import lombok.Data;

@Data
public class TutorProfileUpdateDTO {

    private String university;
    private String major;
    private String grade;
    private String subjects;
    private Integer hourlyRate;
    private String bio;
    private String experience;
    private String districts;
    private String availableTimes;
}