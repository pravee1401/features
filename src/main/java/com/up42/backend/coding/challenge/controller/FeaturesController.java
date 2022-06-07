package com.up42.backend.coding.challenge.controller;

import com.up42.backend.coding.challenge.model.Feature;
import com.up42.backend.coding.challenge.service.FeaturesService;
import java.io.IOException;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class FeaturesController {

  private final FeaturesService featuresService;

  /**
   * API to get all features metadata
   *
   * @return
   * @throws IOException
   */
  @GetMapping("/features")
  public ResponseEntity<List<Feature>> getFeatures() throws IOException {
    List<Feature> features = featuresService.getFeatures();
    if (!features.isEmpty()) {
      return new ResponseEntity<>(features, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
  }

  /**
   * API to retrieve an image of a feature id
   *
   * @param featureId
   * @return
   * @throws IOException
   */
  @GetMapping(value = "/features/{featureId}/quicklook", produces = "image/png")
  public byte[] getFeatureImage(@PathVariable("featureId") String featureId) throws IOException {
    return featuresService.getFeatureImage(featureId);
  }
}
