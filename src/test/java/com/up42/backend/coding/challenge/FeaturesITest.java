package com.up42.backend.coding.challenge;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.up42.backend.coding.challenge.util.FeaturesHelper;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class FeaturesITest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private FeaturesHelper featuresHelper;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void givenListOfFeatures_whenGetAllFeatures_thenReturnFeaturesList() throws Exception {
    // given - precondition or setup
    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(Objects.requireNonNull(classLoader.getResource("source-data-test.json")).getFile());
    FileReader fileReader = new FileReader(file);
    given(featuresHelper.getFeaturesFileReader()).willReturn(fileReader);

    // when -  action or the behaviour that we are going test
    ResultActions response = mockMvc.perform(get("/features"));

    // then - verify the output
    response.andExpect(status().isOk())
        //.andDo(print())
        .andExpect(jsonPath("$.size()", is(3)))
        .andExpect(jsonPath("$[*].id", containsInAnyOrder("39c2f29e-c0f8-4a39-a98b-deed547d6aea",
            "cf5dbe37-ab95-4af1-97ad-2637aec4ddf0",
            "ca81d759-0b8c-4b3f-a00a-0908a3ddd655")))
        .andExpect(jsonPath("$[*].missionName", containsInAnyOrder("Sentinel-1", "Sentinel-1", "Sentinel-1")));
  }

  @Test
  public void givenIOExceptionWhileGettingListOfFeatures_whenGetAllFeatures_thenReturnError() throws Exception {
    // given - precondition or setup
    given(featuresHelper.getFeaturesFileReader()).willThrow(new IOException());

    // when -  action or the behaviour that we are going test
    ResultActions response = mockMvc.perform(get("/features"));

    // then - verify the output
    response.andExpect(status().is5xxServerError())
        .andDo(print())
        .andExpect(jsonPath("$.message", containsString("FILE_SYSTEM_ERROR")));
  }

  @Test
  public void givenListOfFeatures_whenGetFeatureImage_thenReturnFeatureImage() throws Exception {
    // given - precondition or setup
    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(Objects.requireNonNull(classLoader.getResource("source-data-test.json")).getFile());
    FileReader fileReader = new FileReader(file);
    given(featuresHelper.getFeaturesFileReader()).willReturn(fileReader);

    // when -  action or the behaviour that we are going test
    ResultActions response = mockMvc.perform(get("/features/39c2f29e-c0f8-4a39-a98b-deed547d6aea/quicklook"));

    // then - verify the output
    response.andExpect(status().isOk())
        //.andDo(print())
        .andExpect(content().contentType("image/png"))
        .andExpect(jsonPath("$").exists());

    assert response.andReturn().getResponse().getContentAsString().length() > 0;
  }

  @Test
  public void givenListOfFeatures_whenGetInvalidFeatureImage_thenReturnFeatureImageNotFound() throws Exception {
    // given - precondition or setup
    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(Objects.requireNonNull(classLoader.getResource("source-data-test.json")).getFile());
    FileReader fileReader = new FileReader(file);
    given(featuresHelper.getFeaturesFileReader()).willReturn(fileReader);

    // when -  action or the behaviour that we are going test
    ResultActions response = mockMvc.perform(get("/features/invalid-feature-id/quicklook"));

    // then - verify the output
    response.andExpect(status().isNotFound())
        .andDo(print())
        .andExpect(jsonPath("$.message", containsString("INCORRECT_REQUEST")))
        .andExpect(jsonPath("$.details", containsInAnyOrder("Feature invalid-feature-id not found")));
  }
}
