package com.example.demo.controller;
import com.example.demo.dto.request.UserRequestDTO;
import com.example.demo.dto.response.PaginatedResponseDTO;
import com.example.demo.dto.response.UserResponseDTO;
import com.example.demo.persistance.entity.User;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/*
* Note : here you should not use MockBean as it is depricated in spring 3.4 +.
*  Little bit complicated look afterwards
* */

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private UserService userService;

    @InjectMocks
    private UserController userController;  // Inject the mock service into the controller




    @Test
    public void testGetAllUsers_validRequest() throws Exception {
        // Given: mock data
        PaginatedResponseDTO<UserResponseDTO> pagedResponseDTO = mockPaginatedResponse();

        when(userService.getAllUsers(any(PageRequest.class)))
                .thenReturn(pagedResponseDTO);

        // When & Then: Perform the GET request and check the response
        mockMvc.perform(get("/api/users")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("John Doe"))
                .andExpect(jsonPath("$.content[1].name").value("Jane Smith"))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.currentPage").value(0))
                .andExpect(jsonPath("$.pageSize").value(10));

        verify(userService).getAllUsers(any(PageRequest.class));

    }

    @Test
    public void testGetAllUsers_useDefaultValues() throws Exception {
        // Given: mock data
        PaginatedResponseDTO<UserResponseDTO> pagedResponseDTO = mockPaginatedResponse();

        when(userService.getAllUsers(any(PageRequest.class)))
                .thenReturn(pagedResponseDTO);

        // When & Then: Perform the GET request and check the response
        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("John Doe"))
                .andExpect(jsonPath("$.content[1].name").value("Jane Smith"))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.currentPage").value(0))
                .andExpect(jsonPath("$.pageSize").value(10));

        verify(userService).getAllUsers(any(PageRequest.class));

    }

    @Test
    public void testGetAllUsers_invalidsize() throws Exception {
        String expectedResponse = "{\"getAllUsers.size\": \"Min page size should be greater than 5\"}";
        mockMvc.perform(get("/api/users")
                        .param("size", "3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    public void testGetAllUsers_invalidPage() throws Exception {
        String expectedResponse = "{\"getAllUsers.page\": \"Page number should not be negative\"}";
        mockMvc.perform(get("/api/users")
                        .param("page", "-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    public void testGetAllUsers_invalidPageandsize() throws Exception {
        String expectedResponse = "{"
                + "\"getAllUsers.page\": \"Page number should not be negative\","
                + "\"getAllUsers.size\": \"Min page size should be greater than 5\""
                + "}";
        mockMvc.perform(get("/api/users")
                        .param("page", "-1")
                        .param("size", "3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedResponse));
    }


    @Test
    public void testGetUserById_validRequest() throws Exception {
        // Given: mock data
        UserResponseDTO userResponseDTO = new UserResponseDTO("123","Sai","sai@gmail.com",25);

        when(userService.getUserById(any(String.class)))
                .thenReturn(userResponseDTO);

        // When & Then: Perform the GET request and check the response
        mockMvc.perform(get("/api/users/123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.name").value("Sai"))
                .andExpect(jsonPath("$.email").value("sai@gmail.com"))
                .andExpect(jsonPath("$.age").value(25));


        verify(userService).getUserById(any(String.class));

    }

    @Test
    public void testGetUserById_userNotFound() throws Exception {

        when(userService.getUserById(any(String.class)))
                .thenReturn(null);

        // When & Then: Perform the GET request and check the response
        mockMvc.perform(get("/api/users/12")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());


    }


    @Test
    public void testCreateUser_validRequest() throws Exception {
        // Given: mock data
        UserResponseDTO userResponseDTO = new UserResponseDTO("123","Sai","sai@gmail.com",25);

        when(userService.createUser(any(UserRequestDTO.class)))
                .thenReturn(userResponseDTO);

        // When & Then: Perform the GET request and check the response
        mockMvc.perform(post("/api/users")
                        .content("{\"name\": \"Sai\", \"email\": \"sai@gmail.com\", \"age\": 25}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.name").value("Sai"))
                .andExpect(jsonPath("$.email").value("sai@gmail.com"))
                .andExpect(jsonPath("$.age").value(25));


        verify(userService).createUser(any(UserRequestDTO.class));

    }


    @Test
    public void testCreateUser_invalidRequest() throws Exception {

        String expectedResponse = "{\"name\": \"Name is mandatory\"}";

        // When & Then: Perform the GET request and check the response
        mockMvc.perform(post("/api/users")
                        .content("{\"name\": \"\", \"email\": \"sai@gmail.com\", \"age\": 25}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedResponse));

    }



    @Test
    public void testDeleteUserById_validRequest() throws Exception {
        // Given: mock data
        doNothing().when(userService).deleteUserById(any(String.class));


        // When & Then: Perform the GET request and check the response
        mockMvc.perform(delete("/api/users/123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());


        verify(userService).deleteUserById(any(String.class));

    }







    private PaginatedResponseDTO<UserResponseDTO> mockPaginatedResponse(){
        List<UserResponseDTO> userList = List.of(
                new UserResponseDTO("1", "John Doe", "john.doe@example.com", 25),
                new UserResponseDTO("2", "Jane Smith", "jane.smith@example.com", 30)
        );
        PaginatedResponseDTO<UserResponseDTO> pagedResponseDTO = new PaginatedResponseDTO<>(
                userList,
                1,  // totalPages
                2,  // totalElements
                0,  // currentPage
                10, // pageSize
                true,  // last
                true   // first
        );

        return pagedResponseDTO;
    }

}
