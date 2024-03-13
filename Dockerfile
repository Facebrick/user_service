FROM tomcat:10.1.19-jre17
COPY target/user_service.war /usr/local/tomcat/webapps/
EXPOSE 8081