package dev.franke.felipe.job_applications.domain;

import java.time.LocalDateTime;

public record JobApplication(String jobName, String companyName, String url, LocalDateTime applicationTime) {
}
