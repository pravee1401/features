package com.up42.backend.coding.challenge.util;

import java.io.FileReader;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/**
 * Features helper, mostly used to isolate or extract concerns related to Time/Random/IO, so that these concerns could be
 * mocked while testing
 * Also could be used for Features related utility functions
 */
@Component
public class FeaturesHelper {

  @Value("classpath:source-data.json")
  private Resource resourceFile;

  public FileReader getFeaturesFileReader() throws IOException {
    return new FileReader(resourceFile.getFile());
  }
}
