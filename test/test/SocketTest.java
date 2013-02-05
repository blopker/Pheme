package test;

import org.junit.*;

import play.mvc.*;
import play.test.*;
import play.libs.*;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

public class SocketTest {
	@Test
	public void testSocket() {
		running(testServer(3333), new Runnable() {
			@Override
			public void run() {
				assertThat(
						WS.url("http://localhost:3333/socket").get().get().getStatus())
						.isEqualTo(OK);
			}
		});
	}

	@Test
	public void callIndex() {
		Result result = callAction(controllers.routes.ref.Application.index());
		assertThat(status(result)).isEqualTo(OK);
		assertThat(contentType(result)).isEqualTo("text/html");
		assertThat(charset(result)).isEqualTo("utf-8");
		assertThat(contentAsString(result)).contains("Welcome to Pheme");
	}
}
