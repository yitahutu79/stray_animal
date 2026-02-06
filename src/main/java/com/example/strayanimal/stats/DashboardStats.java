package com.example.strayanimal.stats;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardStats {

    private long totalAnimals;
    private long animalsWaiting;
    private long animalsAdopted;

    private long totalApplications;
    private long approvedApplications;

    private long totalDonations;
    private double totalMoneyAmount;
}

