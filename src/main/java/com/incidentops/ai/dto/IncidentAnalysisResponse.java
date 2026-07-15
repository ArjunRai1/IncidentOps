package com.incidentops.ai.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class IncidentAnalysisResponse {
   private String rootCause;
   private String confidence;
   private List<String> evidence;
   private List<String> immediateActions;
   private List<String> preventiveRecommendation;
}
