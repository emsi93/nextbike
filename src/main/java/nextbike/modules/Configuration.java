package nextbike.modules;


import javax.servlet.ServletContext;

import org.springframework.web.context.ServletContextAware;

public class Configuration implements ServletContextAware {

	private static String rootPath;
	private static String rootContext;

	private Configuration(String rootContext) {
		Configuration.rootContext = rootContext;
	}

	public void setServletContext(ServletContext servletContext) {
		rootPath = servletContext.getRealPath("/");
	}

	public static String getRootPath() {
		return rootPath;
	}

	public static String getRootContext() {
		return rootContext;
	}
}
