package com.example.demo.integrationTests;

import com.example.demo.dto.response.UserResponseDTO;
import com.example.demo.persistance.entity.User;
import com.example.demo.persistance.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
public class UserControllerIntegrationTest extends IntegrationTestBase {

    @LocalServerPort
    private int port; // The random port assigned to your application during the test

    private MockMvc mockMvc;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MongoTemplate mongoTemplate;


    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) throws IOException {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
        mongoTemplate.dropCollection(User.class);
        ObjectMapper objectMapper = new ObjectMapper();
        List<User> users = objectMapper.readValue(Paths.get("src/test/resources/users.json").toFile(), new TypeReference<List<User>>(){});
        mongoTemplate.insertAll(users);
    }

    @Test
    public void testGetAllUsers() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        List<User> users = objectMapper.readValue(Paths.get("src/test/resources/users.json").toFile(), new TypeReference<List<User>>(){});
        mockMvc.perform(get("/api/users")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Arun kumar"))
                .andExpect(jsonPath("$.content[1].name").value("Dhoni mahendra"))
                .andExpect(jsonPath("$.totalElements").value(4))
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    public void testGetUserById() throws Exception {
        mockMvc.perform(get("/api/users/67946e818552692fae90b169")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$.id").value("67946e818552692fae90b169"))
                .andExpect(jsonPath("$.name").value("Dhoni mahendra"))
                .andExpect(jsonPath("$.email").value("dhoni@gmail.com"))
                .andExpect(jsonPath("$.age").value(29))
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    public void testCreateUser() throws Exception {
        mockMvc.perform(post("/api/users")
                        .content("{\"name\": \"Sai\", \"email\": \"sai@gmail.com\", \"age\": 25}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$.name").value("Sai"))
                .andExpect(jsonPath("$.email").value("sai@gmail.com"))
                .andExpect(jsonPath("$.age").value(25))
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));;
    }

    @Test
    public void testDeleteUserById() throws Exception {
        mockMvc.perform(delete("/api/users/67946e718552692fae90b168")
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isNoContent())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));;
    }


    @Test
    void testGetEndpoint() {
        // Arrange: Add dummy data to MongoDB
        // Use your repository or MongoTemplate to save test data
        User user = new User("1234","Sai","sai@gmail.com",25);
        userRepository.save(user);

        // Act: Call your API endpoint
        String baseUrl = "http://localhost:" + port; // Construct the absolute URL
        ResponseEntity<UserResponseDTO> response = testRestTemplate.getForEntity(baseUrl+"/api/users/1234", UserResponseDTO.class);

        System.out.println(response.toString());
        // Assert: Validate the API response and database interaction
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}
