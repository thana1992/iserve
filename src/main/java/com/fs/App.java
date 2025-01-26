package com.fs;

import com.fs.dev.Arguments;
import com.fs.dev.Console;
import com.fs.serve.ServiceApplication;
import services.moleculer.web.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import services.moleculer.ServiceBroker;
import services.moleculer.config.SpringRegistrator;

@SpringBootApplication
public class App {

	@Autowired
	private Environment env;
	
	public static void main(String[] args) {
		try {
    		if(Arguments.getBoolean(args, "-?","-h","-help")) {
    			ServiceApplication.usage();
    			return;
    		}
    		if(Arguments.getBoolean(args, "-boot","-springboot")) {
    			SpringApplication.run(App.class, args);
    			return;
    		}
			App app = new App();
    		ServiceApplication serve = app.createServiceApplication();
    		serve.readArguments(args);
    		serve.startupApplication(true);
    		if(Arguments.getBoolean(args, "-env")) {
    			printEnv();
    		}
		} catch(Exception ex) {
			Console.err.println(ex);
		}
	}
	
	public static void printEnv() {
		Console.out.println("-------- properties --------");
		java.util.Properties prop = System.getProperties();	
		for(java.util.Iterator<Object> it = prop.keySet().iterator(); it.hasNext();) {
			String key = (String)it.next();
			String value = prop.getProperty(key);
			Console.out.println(key+"="+value);
		}
		Console.out.println("-------- environments --------");
		java.util.Map<String,String> map = System.getenv();
		for(java.util.Iterator<String> it = map.keySet().iterator(); it.hasNext();) {
			String key = it.next();
			String value = map.get(key);
			Console.out.println(key+"="+value);			
		}
	}
	
	public ServiceApplication createServiceApplication() {
		ServiceApplication app = new ServiceApplication();
		if(env != null) {
			app.setPort(env.getProperty("netty.port", Integer.class, app.getPort()));
			app.setGateWay(env.getProperty("gate.way", Boolean.class, app.isGateWay()));
			app.setTransporter(env.getProperty("transporter", String.class, app.getTransporter()));
			app.setPreferLocal(env.getProperty("prefer.local", Boolean.class, app.isPreferLocal()));
			app.setProviderPackages(env.getProperty("provider.packages", String.class, app.getProviderPackages()));
			Console.out.println("netty.port="+env.getProperty("netty.port")+", gate.way="+env.getProperty("gate.way")+", transporter="+env.getProperty("transporter")+", prefer.local="+env.getProperty("prefer.local")+", provider.packages="+env.getProperty("provider.packages"));
		}
		settingRouter(app);
		Console.out.println("service application: "+app);
		return app;
	}
	
	@Bean(initMethod = "start", destroyMethod = "stop")
	public ServiceBroker getServiceBroker() throws Exception {
		ServiceApplication app = createServiceApplication();
		ServiceBroker broker = app.createServiceBroker();
		broker.getConfig().setJsonReaders("jackson");
		broker.getConfig().setJsonWriters("jackson");
		return broker;
	}

	@Bean
	public SpringRegistrator getSpringRegistrator() {
		return new SpringRegistrator();
	}
	
    public void settingRouter(ServiceApplication app) {
		app.settingGateway(gw -> {
			Route route = gw.addRoute();
			route.addAlias("GET", "api/fetch/hi/:name", "fetch.hi");
			route.addAlias("GET", "api/fetch/time/:name", "fetch.time");
		});		
	}
	
}
