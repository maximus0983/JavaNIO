package com.epam.task1;

public class Women extends Person {
    private String education;
    private transient int brestSize;

    public Women(String name, String surname, int age) {
        super(name, surname, age);
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public int getBrestSize() {
        return brestSize;
    }

    public void setBrestSize(int brestSize) {
        this.brestSize = brestSize;
    }
}
