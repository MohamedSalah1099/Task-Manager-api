package com.mohamedsalah.taskmanager.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Enables JPA auditing for automatic entity timestamp tracking.
 */
@Configuration
@EnableJpaAuditing
public class AuditorConfig {
}
