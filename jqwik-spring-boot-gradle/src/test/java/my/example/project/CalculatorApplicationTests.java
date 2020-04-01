package my.example.project;

import net.jqwik.api.*;
import net.jqwik.api.lifecycle.*;
import net.jqwik.spring.*;
import net.jqwik.spring.boot.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.http.*;
import org.springframework.test.web.servlet.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@JqwikSpringBootTest(
//		webEnvironment = SpringBootTest.WebEnvironment.MOCK,
//		classes = CalculatorApplication.class
//)
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		classes = CalculatorApplication.class
)
@AutoConfigureMockMvc
@AddLifecycleHook(JqwikSpringExtension.class)
class CalculatorApplicationTests {

	@Autowired
	private MockMvc mvc;

	@Example
	void helloWorldWorks() throws Exception {
		mvc.perform(get("/hello").contentType(MediaType.APPLICATION_JSON))
		   .andExpect(status().isOk())
		   .andExpect(content().string("world"));
	}

}
