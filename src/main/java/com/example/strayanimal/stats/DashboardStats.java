package com.example.strayanimal.stats;

public class DashboardStats {

    private long totalAnimals;
    private long animalsWaiting;
    private long animalsAdopted;
    private long totalApplications;
    private long approvedApplications;
    private long totalDonations;
    private double totalMoneyAmount;

    public DashboardStats(long totalAnimals,
                          long animalsWaiting,
                          long animalsAdopted,
                          long totalApplications,
                          long approvedApplications,
                          long totalDonations,
                          double totalMoneyAmount) {
        this.totalAnimals = totalAnimals;
        this.animalsWaiting = animalsWaiting;
        this.animalsAdopted = animalsAdopted;
        this.totalApplications = totalApplications;
        this.approvedApplications = approvedApplications;
        this.totalDonations = totalDonations;
        this.totalMoneyAmount = totalMoneyAmount;
    }

    public long getTotalAnimals() {
        return totalAnimals;
    }

    public void setTotalAnimals(long totalAnimals) {
        this.totalAnimals = totalAnimals;
    }

    public long getAnimalsWaiting() {
        return animalsWaiting;
    }

    public void setAnimalsWaiting(long animalsWaiting) {
        this.animalsWaiting = animalsWaiting;
    }

    public long getAnimalsAdopted() {
        return animalsAdopted;
    }

    public void setAnimalsAdopted(long animalsAdopted) {
        this.animalsAdopted = animalsAdopted;
    }

    public long getTotalApplications() {
        return totalApplications;
    }

    public void setTotalApplications(long totalApplications) {
        this.totalApplications = totalApplications;
    }

    public long getApprovedApplications() {
        return approvedApplications;
    }

    public void setApprovedApplications(long approvedApplications) {
        this.approvedApplications = approvedApplications;
    }

    public long getTotalDonations() {
        return totalDonations;
    }

    public void setTotalDonations(long totalDonations) {
        this.totalDonations = totalDonations;
    }

    public double getTotalMoneyAmount() {
        return totalMoneyAmount;
    }

    public void setTotalMoneyAmount(double totalMoneyAmount) {
        this.totalMoneyAmount = totalMoneyAmount;
    }
}
