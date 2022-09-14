package com.ntg.sadmin;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.DispatcherServlet;

import com.ntg.common.NTGEncryptor;
import com.ntg.common.STAGESystemOut;
import com.ntg.sadmin.common.NTGMessageOperation;
import com.ntg.sadmin.config.tenant.CurrentTenantIdentifierResolverImpl;

@ComponentScan(basePackages = "com.ntg.*")
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@EnableAspectJAutoProxy
public class Application {
	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	/*
	 * Zero not started 1 starting 2 end
	 */
	static int ApplicaitonIsIntializing = 0;

	public static void main(String[] args) {

		Application.ApplicaitonIsIntializing = 1;
		TenantContext.setCurrentTenantByCompanyName(CurrentTenantIdentifierResolverImpl.DEFAULT_TENANT_ID, "main",
				"Application.class");

		SpringApplication.run(Application.class, args);
		NTGInit();
	}
	static String Version;
	public static void NTGInit() {

		Application.ApplicaitonIsIntializing = 2;
		System.out.println("*********************************************************");
		System.out.println("   _____                      __ ___   ______    \n" +
				"  / ___/____ ___  ____ ______/ /|__ \\ / ____/___ \n" +
				"  \\__ \\/ __ `__ \\/ __ `/ ___/ __/_/ // / __/ __ \\\n" +
				" ___/ / / / / / / /_/ / /  / /_/ __// /_/ / /_/ /\n" +
				"/____/_/ /_/ /_/\\__,_/_/   \\__/____/\\____/\\____/ \n" +
				"                                                 ");
		System.out.println("   _____                      _ __           ___       __          _      \n" +
				"  / ___/___  _______  _______(_) /___  __   /   | ____/ /___ ___  (_)___  \n" +
				"  \\__ \\/ _ \\/ ___/ / / / ___/ / __/ / / /  / /| |/ __  / __ `__ \\/ / __ \\ \n" +
				" ___/ /  __/ /__/ /_/ / /  / / /_/ /_/ /  / ___ / /_/ / / / / / / / / / / \n" +
				"/____/\\___/\\___/\\__,_/_/  /_/\\__/\\__, /  /_/  |_\\__,_/_/ /_/ /_/_/_/ /_(_)\n" +
				"                                /____/                                    ");
		System.out.println(":: Security Adminstration Back End Started(Smart2GoAdmin):: (v"+Version+")");
		System.out.println("*********************************************************");
		STAGESystemOut.OverrideSystemOutput();

		STAGESystemOut.OverrideSystemErrorOutput();

	}

	@Bean
	public PropertySourcesPlaceholderConfigurer properties() throws Exception {
		final PropertySourcesPlaceholderConfigurer ppc = new PropertySourcesPlaceholderConfigurer();
		logger.info("creating External Config");
		CreateExternalPropertyFile();
		Properties prop = intializeSettings("application_sadmin.properties", false, false);
		ppc.setIgnoreResourceNotFound(true);
		final List<Resource> resourceLst = new ArrayList<Resource>();
		String homePath = System.getProperty("user.home");
 		resourceLst.add(new ClassPathResource("UDASSqlCommands.properties"));
		// resourceLst.add(new ClassPathResource("Paging.properties"));
		ppc.setProperties(prop);

		ppc.setLocations(resourceLst.toArray(new Resource[] {}));
		return ppc;
	}

	public static void CreateExternalPropertyFile() {
		try {
			String path = System.getProperty("user.home") + "/.Smart2GoConfig";
			File file = new File(path);
			if (!file.exists()) {
				Path pathToCreate = Paths.get(path);
				Files.createDirectories(pathToCreate);
			}
			path = System.getProperty("user.home") + "/.Smart2GoConfig/.application_sadmin.properties";
			file = new File(path);
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (IOException e) {
			NTGMessageOperation.PrintErrorTrace(e);
		}
	}



	@Bean
	javax.servlet.MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize(DataSize.parse("5120MB"));
		factory.setMaxRequestSize(DataSize.parse("5120MB"));
		return factory.createMultipartConfig();
	}

	@Bean
	public HttpMessageConverters customConverters() {
		ByteArrayHttpMessageConverter arrayHttpMessageConverter = new ByteArrayHttpMessageConverter();
		return new HttpMessageConverters(arrayHttpMessageConverter);
	}

	@Bean
	public DispatcherServlet dispatcherServlet() {
		return new DispatcherServlet();
	}

	@Bean
	public ServletRegistrationBean<DispatcherServlet> restServlet() {
		ServletRegistrationBean<DispatcherServlet> servlet = new ServletRegistrationBean<>(dispatcherServlet(), "/*");

		servlet.setLoadOnStartup(1);
		return servlet;
	}

	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver createMultipartResolver() {
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		resolver.setDefaultEncoding("utf-8");
		return resolver;
	}


	/**
	 * Password Encoder for hashing using Bcrypt
	 * for more info https://www.baeldung.com/spring-security-registration-password-encoding-bcrypt
	 * @return
	 * @author babdelaziz
	 */

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}



	public static Properties intializeSettings(String PropertyFileName, boolean EscapeCopyOut, boolean IgnoreHomeResourceFile) throws Exception {


		Properties props = new Properties();
		Properties propsToWrite = null;
		InputStream input = Application.class.getClassLoader().getResourceAsStream(PropertyFileName);
		if (input == null) {
			System.out.println("Resource File " + PropertyFileName + " can't be Loaded");
			throw new Exception( "Resource File " + PropertyFileName + " can't be Loaded");
		}

		if (input != null) {
			props.load(input);
			propsToWrite = new Properties();
			Properties oldProps = new Properties();
			File f = null;
			if (IgnoreHomeResourceFile == false) {
				String path = System.getProperty("user.home") + "/.Smart2GoConfig/." + PropertyFileName;
				f = new File(path);
				if (f.exists()) {
					oldProps.load(new FileInputStream(f));
				}
			}

			for (Object property : props.keySet()) {

				String properyName = property.toString().replaceAll("\\[", "").replaceAll("\\]", "");
				Object properyValue;
				if (System.getenv(properyName) != null) {
					properyValue = System.getenv(properyName);

				} else {
					if (oldProps.get(property) != null
							&& property.toString().startsWith("pom.") == false
							&& property.toString().startsWith("ImpExp.version") == false
					) {
						properyValue = oldProps.get(property);
					} else {
						properyValue = props.get(property);
						if (property.toString().equals("pom.version")) {
							Version = (String) props.get(property);
						}
					}
				}
				////Dev-00003395 : extract the encrypted value for the password
				String properyValuestr = properyValue.toString();
				if (property.toString().toLowerCase().contains("password")) {
					int start = properyValuestr.toLowerCase().indexOf("enc(");
					int end = properyValuestr.lastIndexOf(")");
					if (start > -1 && end > 0) {

						if (properyValuestr.trim().startsWith("${") && properyValuestr.trim().endsWith("}") && properyValuestr.contains(":")) {
							int index = properyValuestr.indexOf(":");
							String FirstPart = properyValuestr.substring(0, index + 1);

							properyValuestr = properyValuestr.substring(start + 4, end);

							properyValue = FirstPart + NTGEncryptor.decrypt(properyValuestr) + "}";
						} else {

							properyValuestr = properyValuestr.substring(start + 4, end);
							properyValue = NTGEncryptor.decrypt(properyValuestr);
						}
					}
				}
				propsToWrite.put(property, properyValue);

				if (properyName.equals("app.instance-id")) {
					if (propsToWrite.get(properyName).equals("${random.uuid}")) {
						propsToWrite.put(properyName, "BE-" + java.util.UUID.randomUUID());
					}
				}

			}

			// write into it
			if (EscapeCopyOut == false) {
				StoreTheNewPropertiyFile(propsToWrite, PropertyFileName, f);
			}

			Object[] keys = propsToWrite.keySet().toArray();
			for (Object k : keys) {
				String v = (String) propsToWrite.get(k);
				System.setProperty((String) k, v);
			}

		}

		return propsToWrite;
	}



	public static void StoreTheNewPropertiyFile(Properties propsToWrite, String PropertyFileName, File f) throws Exception {

		InputStream in = Application.class.getClassLoader().getResourceAsStream(PropertyFileName);
		Scanner inp = new Scanner(in);
		FileWriter wr = new FileWriter(f);
		while (inp.hasNextLine()) {
			String line = inp.nextLine().trim();

			if (line.indexOf("=") > 0 && line.startsWith("#") == false) {
				String[] list = line.split("=");
				//find value from the propert
				String Key = list[0].trim();
				Object newValue = propsToWrite.get(Key);

				String ValueStr = (newValue == null || newValue.equals("null") ? "" : newValue.toString());

				////Dev-00003395 : extract the encrypted value for the password

				if (Key.toLowerCase().contains("password")) {
					int start = ValueStr.toLowerCase().indexOf("enc(");
					int end = ValueStr.lastIndexOf(")");
					if (start < 0 || end < 0) {
						if(ValueStr.trim().startsWith("${") && ValueStr.trim().endsWith("}") &&  ValueStr.contains(":")){
							int index = ValueStr.indexOf(":");
							int LastIndex = ValueStr.indexOf("}");
							String FirstPart = ValueStr.substring(0,index +1);
							String SecondPart = ValueStr.substring(index+1,LastIndex );
							ValueStr = FirstPart + "Enc(" + NTGEncryptor.encrypt(SecondPart) + ")}";
						}else {
							ValueStr = "Enc(" + NTGEncryptor.encrypt(ValueStr) + ")";
						}
					}
				}

				line = Key + "=" + ValueStr;
			}
			wr.write(line);
			wr.write("\r\n");
		}
		wr.close();
		inp.close();
		in.close();
		System.out.println("ReWrite Property File --> " + PropertyFileName);
	}

}
