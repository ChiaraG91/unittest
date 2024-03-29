package com.unittest.unittest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unittest.unittest.controllers.UserController;
import com.unittest.unittest.entities.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTests {

	@Autowired
	private UserController userController;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;



	@Test
	@Order(1)
	void createUser() throws Exception {
		User user = new User();
		user.setId(1L);
		user.setName("Filippo");
		user.setSurname("Rossi");
		user.setDetails("hjdljigrugnugu");

		String userJSON = objectMapper.writeValueAsString(user);

		MvcResult resultActions = this.mockMvc.perform(post("/v2/adduser")
						.contentType(MediaType.APPLICATION_JSON)
						.content(userJSON)).andDo(print())
				.andExpect(status().isOk()).andReturn();
	}

	@Test
	@Order(2)
	void updateStudentById() throws Exception {
		Long userId = 1L;
		User updatedUser = new User(userId, "Updated", "Name", "ahahahahah");
		String userJSON = objectMapper.writeValueAsString(updatedUser);

		MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.put("/v2/updateuser/{id}", userId)
						.contentType(MediaType.APPLICATION_JSON).content(userJSON))
				.andDo(print()).andExpect(status().isOk()).andReturn();

		String content = result.getResponse().getContentAsString();
		Assertions.assertNotNull(content);
	}

	@Test
	@Order(3)
	void readUserList() throws Exception {
		MvcResult result = this.mockMvc.perform(get("/v2/getlist"))
				.andDo(print()).andReturn();

		List<User> userFromResponseList = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
		assertThat(userFromResponseList.size()).isNotZero();
	}

	@Test
	@Order(4)
	void getUser() throws Exception {
		Long studentId = 1L;
		MvcResult resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/v2/getuser/{id}", studentId))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(studentId)).andReturn();
	}



	@Test
	@Order(5)
	void deleteUser() throws Exception {
		MvcResult result = mockMvc.perform(delete("/v2/delete")
						.param("id", String.valueOf(1L))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();


	}

	@Test
	@Order(6)
	void contextLoads() {
		assertThat(userController).isNotNull();
	}
}
