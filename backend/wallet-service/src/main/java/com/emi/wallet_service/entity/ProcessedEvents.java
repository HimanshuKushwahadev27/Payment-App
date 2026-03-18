package com.emi.wallet_service.entity;


import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="processed_events")
@NoArgsConstructor
@Data
public class ProcessedEvents {

	private UUID id ;
}
