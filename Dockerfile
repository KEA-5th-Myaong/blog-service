FROM openjdk:17
COPY build/libs/popolog-blog-service.jar popolog-blog-service.jar
ENTRYPOINT ["java", "-jar", "/popolog-blog-service.jar"]