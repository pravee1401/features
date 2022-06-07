package com.up42.backend.coding.challenge.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Feature {

  private String id;
  private Long timestamp;
  private Long beginViewingDate;
  private Long endViewingDate;
  private String missionName;
}
