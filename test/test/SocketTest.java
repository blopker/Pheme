package test;

import models.Socket;

import org.junit.*;

import play.mvc.*;
import play.test.*;
import play.libs.*;
import play.libs.F.Callback;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

public class SocketTest {
	@Test
	public void testSocket() {
		running(testServer(3333), HTMLUNIT, new Callback<TestBrowser>() {
	        public void invoke(TestBrowser browser) {
	           browser.goTo("http://localhost:3333"); 
	           assertThat(browser.$("#title").getTexts().get(0)).isEqualTo("Hello Guest");
	           browser.$("a").click();
	           assertThat(browser.url()).isEqualTo("http://localhost:3333/Coco");
	           assertThat(browser.$("#title", 0).getText()).isEqualTo("Hello Coco");
	        }
	    });

	}
}
