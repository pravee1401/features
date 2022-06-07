package com.up42.backend.coding.challenge.service;

import com.up42.backend.coding.challenge.model.Feature;
import java.io.IOException;
import java.util.List;

public interface FeaturesService {

  List<Feature> getFeatures() throws IOException;

  byte[] getFeatureImage(String featureId) throws IOException;
}
