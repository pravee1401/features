package com.up42.backend.coding.challenge.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.up42.backend.coding.challenge.exception.FeatureNotFoundException;
import com.up42.backend.coding.challenge.model.Feature;
import com.up42.backend.coding.challenge.util.FeaturesHelper;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class FeaturesServiceImpl implements FeaturesService {

  private final FeaturesHelper featuresHelper;

  private Map<String, String> imageCache;

  /**
   * Fetches and returns metadata of all the list of features available
   *
   * @return
   * @throws IOException
   */
  @Override
  public List<Feature> getFeatures() throws IOException {

    List<Feature> featuresList = new ArrayList<>();

    getFeaturesArray().forEach(featureObject -> {

      ((JsonObject) featureObject).get("features").getAsJsonArray().forEach(feature -> {
        JsonObject properties = feature.getAsJsonObject().get("properties").getAsJsonObject();
        JsonObject acquisition = properties.get("acquisition").getAsJsonObject();

        featuresList.add(Feature.builder()
            .id(properties.get("id").getAsString())
            .timestamp(properties.get("timestamp").getAsLong())
            .beginViewingDate(acquisition.get("beginViewingDate").getAsLong())
            .endViewingDate(acquisition.get("endViewingDate").getAsLong())
            .missionName(acquisition.get("mission").getAsString())
            .build());
      });
    });
    return featuresList;
  }

  /**
   * Returns a particular image corresponding to the feature id provided
   *
   * @param featureId
   * @return
   * @throws IOException
   */
  @Override
  public byte[] getFeatureImage(String featureId) throws IOException {
    if(imageCache.isEmpty()){
      loadFeatureImagesCache();
    }
    String image = imageCache.getOrDefault(featureId, null);
    if(image == null){
      throw new FeatureNotFoundException(String.format("Feature %s not found", featureId));
    }
    return Base64.getDecoder().decode(image);
  }

  /**
   * Preload the local cache with feature ids and its images, to provide quick access to the images
   *
   * @throws IOException
   */
  private void loadFeatureImagesCache() throws IOException {
    imageCache = new HashMap<>();
    getFeaturesArray().forEach(featureObject -> {
      ((JsonObject) featureObject).get("features").getAsJsonArray().forEach(feature -> {
        JsonObject properties = feature.getAsJsonObject().get("properties").getAsJsonObject();
        if(properties.has("id") && properties.has("quicklook")) {
          imageCache.put(properties.get("id").getAsString(), properties.get("quicklook").getAsString());
        }
      });
    });
  }

  /**
   * Provides a feature array, reading it from the file system
   *
   * @return
   * @throws IOException
   */
  private JsonArray getFeaturesArray() throws IOException {
    FileReader fileReader = featuresHelper.getFeaturesFileReader();
    return (JsonArray) new JsonParser().parse(fileReader);
  }

}
