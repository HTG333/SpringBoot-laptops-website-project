package com.electrxh.laptop_website.dto;

import java.time.LocalDateTime;



import com.electrxh.laptop_website.model.UserEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LaptopDto {

private int lapId;

@NotBlank(message = "Brand is Required")
private String brand;

@NotBlank(message = "Model Name is Required")
private String model;
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
private String notes;
private String type;

@JsonIgnore
private UserEntity createdBy;
private LocalDateTime createdOn;
private LocalDateTime updatedOn;

}
