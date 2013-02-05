package test;

import org.junit.*;

import play.mvc.*;
import play.test.*;
import play.libs.F.*;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

public class RouteTest {

	@Test
	public void index() {
		Result result = callAction(controllers.routes.ref.Application.index());
		assertThat(status(result)).isEqualTo(OK);
		assertThat(contentType(result)).isEqualTo("text/html");
		assertThat(charset(result)).isEqualTo("utf-8");
		assertThat(contentAsString(result)).contains("Welcome to Pheme!");
	}
	
	@Test
	public void logs() {
		Result result = callAction(controllers.routes.ref.Application.logs());
		assertThat(status(result)).isEqualTo(OK);
		assertThat(contentType(result)).isEqualTo("text/html");
		assertThat(charset(result)).isEqualTo("utf-8");
		assertThat(contentAsString(result)).contains("Log Viewer");
	}
}