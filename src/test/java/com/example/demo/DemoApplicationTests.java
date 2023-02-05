package com.example.demo;

import com.example.demo.Repository.UserRepository;
import com.example.demo.controller.UserController;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = { DemoApplicationTests.class })
class DemoApplicationTests {

	@InjectMocks
	private UserService service;

	@Mock
	private UserRepository repository;

	@Test
	public void saveUserTest() {
		User user = new User(1,"kk", "K", "a1100@dddfgii.com", "sdsdssscdD@2", LocalDateTime.now(), LocalDateTime.now());
		String username = "a1100@dddfgii.com";
		when(repository.findByUsername(username)).thenReturn(user);
		assertEquals(user.getUsername(), service.loadUserByUsername(username).getUsername());
	}
}
