package com.example.easypass.intro;

public class IntroScreen {
    private String introDescription;
    private int introImage;

    public IntroScreen(String introDescription, int introImage) {
        this.introDescription = introDescription;
        this.introImage = introImage;
    }

    public void setIntroDescription(String introDescription) {
        this.introDescription = introDescription;
    }

    public void setIntroImage(int introImage) {
        this.introImage = introImage;
    }

    public String getIntroDescription() {
        return introDescription;
    }

    public int getIntroImage() {
        return introImage;
    }
}
