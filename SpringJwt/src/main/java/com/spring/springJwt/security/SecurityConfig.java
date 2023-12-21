package com.spring.springJwt.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;


@Configuration
public class SecurityConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //avatarUser
//        Path userAvatarUploadDir = Paths.get("./avatar");
//        String userAvatarUploadPath = userAvatarUploadDir.toFile().getAbsolutePath();
//        registry.addResourceHandler("/avatar/**").addResourceLocations("file:/" + userAvatarUploadPath + "/");

//        LogoCompany
        Path userAvatarUploadDir1 = Paths.get("logo");
        String userAvatarUploadPath1 = userAvatarUploadDir1.toFile().getAbsolutePath();
//        registry.addResourceHandler("/logo/**").addResourceLocations("file:/" + userAvatarUploadPath1 + "/");
        registry.addResourceHandler("/logo/**").addResourceLocations("file:/" + "D:\\VueJsProject\\vue-3-authentication-jwt\\src\\assets\\images_company"+ "/");
     registry.addResourceHandler("/avatar/**").addResourceLocations("file:/" + "D:\\VueJsProject\\vue-3-authentication-jwt\\src\\assets\\images_user"+ "/");

        registry
                .addResourceHandler("/src/main/resources/**")
                .addResourceLocations("/resources/");

    }

//    @PostConstruct
//    public void afterPropertiesSet() throws Exception{
//        Resource resource = new ClassPathResource("static");
//        File file = resource.getFile();
//    }
}

