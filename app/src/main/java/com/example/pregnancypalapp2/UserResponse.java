package com.example.pregnancypalapp2;

public class UserResponse {
    public String fullName, emailAddress, dateOfBirth, expectedDueDate;
    public int numberOfPregnancies;
    public String previousComplications, currentHealthConditions, medications, allergies;
    public String dietaryPreferences, exerciseHabits, supportSystem;
    public int stressLevel;
    public String informationPreferences, pregnancyGoals, majorConcerns, learningPreferences;
    public String genderPreference;
    public boolean interestedInClasses;
    public String interestedFeatures;

    public UserResponse() {
        // Default constructor required for calls to DataSnapshot.getValue(UserResponse.class)
    }

    // Constructor with all fields
    public UserResponse(String fullName, String emailAddress, String dateOfBirth, String expectedDueDate,
                        int numberOfPregnancies, String previousComplications, String currentHealthConditions,
                        String medications, String allergies, String dietaryPreferences, String exerciseHabits,
                        String supportSystem, int stressLevel, String informationPreferences, String pregnancyGoals,
                        String majorConcerns, String learningPreferences, String genderPreference, boolean interestedInClasses,
                        String interestedFeatures) {
        this.fullName = fullName;
        this.emailAddress = emailAddress;
        this.dateOfBirth = dateOfBirth;
        this.expectedDueDate = expectedDueDate;
        this.numberOfPregnancies = numberOfPregnancies;
        this.previousComplications = previousComplications;
        this.currentHealthConditions = currentHealthConditions;
        this.medications = medications;
        this.allergies = allergies;
        this.dietaryPreferences = dietaryPreferences;
        this.exerciseHabits = exerciseHabits;
        this.supportSystem = supportSystem;
        this.stressLevel = stressLevel;
        this.informationPreferences = informationPreferences;
        this.pregnancyGoals = pregnancyGoals;
        this.majorConcerns = majorConcerns;
        this.learningPreferences = learningPreferences;
        this.genderPreference = genderPreference;
        this.interestedInClasses = interestedInClasses;
        this.interestedFeatures = interestedFeatures;
    }
}

