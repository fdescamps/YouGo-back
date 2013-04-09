package com.ineatconseil.yougo.util;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ineatconseil.yougo.ws.rs.impl.provider.ContextResolver;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.client.filter.LoggingFilter;

public abstract class AbstractWebRunnerTestCase {

	private static int port = 9797;
	private String url;
	private static Server server;
	private static Client client;
	
	private Logger logger = LoggerFactory.getLogger(AbstractWebRunnerTestCase.class);
	
	protected AbstractWebRunnerTestCase() {
		this.url = String.format("http://localhost:%s/api/", port);
	}

	protected WebResource getResource(String relativeUrl) {
		String realUrl = url + relativeUrl;
		logger.info("Resource URL : {}", realUrl);
		return client.resource(realUrl);
	}

	@BeforeClass
	public static void setUp() throws Exception {
		server = new Server(port);
		WebAppContext webAppContext = new WebAppContext("src/main/webapp", "/");
		webAppContext.setConfigurationClasses(new String[] {
				"org.mortbay.jetty.webapp.WebInfConfiguration",
				"org.mortbay.jetty.webapp.WebXmlConfiguration", });
		server.addHandler(webAppContext);
		server.start();

		ClientConfig cc = new DefaultClientConfig();
		cc.getClasses().add(ContextResolver.class);

		client = Client.create(cc);
		client.addFilter(new LoggingFilter());
		client.addFilter(new HTTPBasicAuthFilter("kristina.chung@company.com", "password"));
	}

	@AfterClass
	public static void tearDown() throws Exception {
		if (server != null)
			server.stop();
	}
}
