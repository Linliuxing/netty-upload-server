package com.haiyu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@ComponentScan(basePackages = { "com.haiyu" })
public class StartApplication {

	@RequestMapping("/")
	String home() {
		return "Hello File Uploder Server";
	}

	/**
	 * 多个文件中间用;分隔
	 * @param paths
	 * @return
	 */
	@RequestMapping("/uploadFile")
	String uploadFile(@RequestParam("filePath") String paths) {
		return  paths;
	}


	public static void main(String[] args) {
		SpringApplication.run(StartApplication.class, args);
	}


	@Bean
	public NettyFileUploadServer fileUploadServer() throws Exception {
		NettyFileUploadServer fileUploadServer = new NettyFileUploadServer();
		fileUploadServer.init();
		return fileUploadServer;
	}

}
