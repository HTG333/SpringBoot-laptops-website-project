package com.electrxh.laptop_website.model;

import java.time.LocalDateTime;


import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "laptop_data")
public class Laptop{
	
	@Id
	@SequenceGenerator(name = "lap_id_seq",sequenceName = "lap_id_seq",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO,generator = "lap_id_seq")
	private int lapId;
	
	@NotBlank(message = "Brand is Required")
	private String brand;
	
	@NotBlank(message = "Model Name is Required")
	private String model;

    @Column(length = 550)
	private String devPhotoUrl;
	
	@NotBlank(message = "Ram Information is Required")
	private String ram;
	private String hdd;
	private String ssd;
	
	@NotBlank(message = "CPU Information is Required")
	private String cpu;
	private String display;
	
	@NotBlank(message = "Graphics Card Information is Required")
	private String graphics;
	private String budget;
	private String rate;

    @Column(length = 500)
	private String notes;

	private String type;
	@CreationTimestamp
	@Column(updatable =false)
	private LocalDateTime createdOn;
	@UpdateTimestamp
	private LocalDateTime updatedOn;
	
	
	@ManyToOne
	@JoinColumn(name="created_by",nullable = false)
	private UserEntity createdBy;
	

}
