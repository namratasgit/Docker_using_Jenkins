# Docker_using_Jenkins-pipeline

1.	install vs code in Ubuntu
https://itslinuxfoss.com/how-to-install-visual-studio-code-on-ubuntu-22-04/#1

Create a New Maven Project

a.	cd ~/Desktop [Open a terminal and navigate to the directory where you want to create your project.]
b.	Use Maven to create a new Maven project with the following command:
mvn archetype:generate -DgroupId=com.example.calculator -DartifactId=calculator-app -DarchetypeArtifactId=maven-archetype-webapp -DinteractiveMode=false
c.	Change into your project directory:

Step 1: cd calculator_app

Step 2: Create the Calculator.java Class inside java/com/example/calculator
[[[[[[[Directory structure should look like this -
my-web-app/
   src/
      main/
         java/               <-- Create this directory if it's missing
         webapp/
            WEB-INF/
               web.xml
   pom.xml  ]]]]]]]

    package main.java. com.example.calculator;

    public class Calculator {
        public static double calculate(double number1, double number2, char operator) {
            switch (operator) {
                case '+':
                    return number1 + number2;
                case '-':
                    return number1 - number2;
                case '*':
                    return number1 * number2;
                case '/':
                    if (number2 != 0) {
                        return number1 / number2;
                    } else {
                        throw new ArithmeticException("Division by zero is not allowed");
                    }
                default:
                    throw new IllegalArgumentException("Invalid operator");
            }
        }
    }

Step 3: Create a Servlet

    Create a new Java class named CalculatorServlet.java in the src/main/java/com/example/calculator directory. This servlet will handle the web requests.

    package main.java.com.example.calculator;

    import java.io.IOException;
    import javax.servlet.ServletException;
    import javax.servlet.annotation.WebServlet;
    import javax.servlet.http.HttpServlet;
    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;

    @WebServlet("/calculator")
    public class CalculatorServlet extends HttpServlet {
        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            double number1 = Double.parseDouble(request.getParameter("number1"));
            double number2 = Double.parseDouble(request.getParameter("number2"));
            char operator = request.getParameter("operator").charAt(0);

            double result = Calculator.calculate(number1, number2, operator);

            request.setAttribute("result", result);
            request.getRequestDispatcher("calculator.jsp").forward(request, response);
        }
    }

Step 4: Create a JSP Page

    Create a directory named WEB-INF in the src/main/webapp directory.

    Inside the WEB-INF directory, create a JSP file named calculator.jsp. This file will be used to display the calculator form and results.

    <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    <!DOCTYPE html>
    <html>
    <head>
        <meta charset="UTF-8">
        <title>Calculator</title>
    </head>
    <body>
        <h1>Simple Calculator</h1>
        <form action="calculator" method="post">
            Enter first number: <input type="text" name="number1"><br>
            Enter second number: <input type="text" name="number2"><br>
            Choose an operator: <input type="text" name="operator"><br>
            <input type="submit" value="Calculate">
        </form>
        <br>
        <c:if test="${not empty requestScope.result}">
            Result: ${requestScope.result}
        </c:if>
    </body>
    </html>

Step 5: Configure Web Deployment Descriptor (web.xml)

    Inside the WEB-INF directory, create a web.xml file with the following content:

    <?xml version="1.0" encoding="UTF-8"?>
    <web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
        version="4.0">
        <servlet>
            <servlet-name>CalculatorServlet</servlet-name>
            <servlet-class>com.calculator.CalculatorServlet</servlet-class>
        </servlet>
        <servlet-mapping>
            <servlet-name>CalculatorServlet</servlet-name>
            <url-pattern>/calculator</url-pattern>
        </servlet-mapping>
        <welcome-file-list>
            <welcome-file>calculator.jsp</welcome-file>
        </welcome-file-list>
    </web-app>



Step 6: Create the pom.xml File --->In the root directory of your project, create a pom.xml file if it doesn't already exist.
   Add the following content to your pom.xml file to specify your project's dependencies and plugins:


         <?xml version="1.0" encoding="UTF-8"?>
         <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
           <modelVersion>4.0.0</modelVersion>
           
           <groupId>com.calculator</groupId>
           <artifactId>CalculatorApp</artifactId>
           <version>1.0-SNAPSHOT</version>
           <packaging>jar</packaging> <!-- Change this to JAR -->
           
           <properties>
             <maven.compiler.source>11</maven.compiler.source>
             <maven.compiler.target>11</maven.compiler.target>
           </properties>
         
           <dependencies>
             <!-- Your dependencies here -->
           </dependencies>
         
           <build>
             <plugins>
               <plugin>
                 <groupId>org.apache.maven.plugins</groupId>
                 <artifactId>maven-compiler-plugin</artifactId>
                 <version>3.8.1</version>
                 <configuration>
                   <source>11</source>
                   <target>11</target>
                 </configuration>
               </plugin>
             </plugins>
           </build>
         </project>


Step 7: Build and Run Your Web App

    Open a terminal in VSCode and navigate to your project directory:

Build your project using Maven:
namrata@namrata-vm:~/Desktop/calculator_app$ mvn clean install

OPTIONAL

Deploy the generated WAR file (located in the target directory) to a Servlet container like Apache Tomcat.

Access the application by navigating to http://localhost:8080/CalculatorWebApp (assuming your servlet container is running on port 8080).

2.	Create a new pipeline job
3.	Configure  General  github project(provide repo url), build triggers git hook trigger for scm polling, pipeline follow below steps
4.	Pipeline


Pipeline syntax-

Steps checkout: chexkout from version control
SCM – Git, Repo URL- …., Branches to build --> */master
Generate the pipeline and copy paste in the script

               pipeline {
                   agent any
                   tools {
                       maven '3.6.3'
                   }
                   stages {
                       stage("Build Maven") {
                           steps {
                               script {
                                   // Specify the path to your POM file
                                   def pomPath = '/home/namrata/.jenkins/workspace/docker_using_jenkins/calculator_app/pom.xml'
               
                                   // Checkout your source code from your repository
                                   checkout scmGit(
                                       branches: [[name: '*/main']],
                                       extensions: [],
                                       userRemoteConfigs: [[credentialsId: 'git1', url: 'https://github.com/namratasgit/Docker_using_Jenkins.git']]
                                   )
               
                                   // Build your project using Maven with the specified POM path
                                   sh "mvn clean install -f ${pomPath}"
                               }
                           }
                       }
                   }
               }

5.	Build
// Build Docker Image

6.	Download and install Docker
https://docs.docker.com/desktop/install/ubuntu/
4.	Download DEB package
5.	Set up Docker's package repository[using Apt repository].[visit link] https://docs.docker.com/engine/install/ubuntu/#install-using-the-repository
•	sudo apt-get update
•	sudo apt-get install ca-certificates curl gnupg
•	sudo install -m 0755 -d /etc/apt/keyrings
•	curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
•	sudo chmod a+r /etc/apt/keyrings/docker.gpg
•	echo \
  "deb [arch="$(dpkg --print-architecture)" signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  "$(. /etc/os-release && echo "$VERSION_CODENAME")" stable" | \sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
•	sudo apt-get update
•	sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin [install docker packages]
•	[install and launch docker desktop]
sudo apt-get install ./docker-desktop-4.23.0-amd64.deb
•	systemctl --user start docker-desktop
•	gpg --generate-key
passphrase –dockerkey123
pg: /home/namrata/.gnupg/trustdb.gpg: trustdb created
gpg: key 2060F75D9468D9DC marked as ultimately trusted
gpg: directory '/home/namrata/.gnupg/openpgp-revocs.d' created
gpg: revocation certificate stored as '/home/namrata/.gnupg/openpgp-revocs.d/5074F1A18A5F83D265A6859C2060F75D9468D9DC.rev'
public and secret key created and signed.

pub   rsa3072 2023-09-15 [SC] [expires: 2025-09-14]
      5074F1A18A5F83D265A6859C2060F75D9468D9DC
uid                      Namrata <dasnamrata795@gmail.com>
sub   rsa3072 2023-09-15 [E] [expires: 2025-09-14]
•	pass init 5074F1A18A5F83D265A6859C2060F75D9468D9DC

7.	Now go to vscode and add dockerfile directly inside calculator_app directory
         
         FROM openjdk:11
         EXPOSE 8080
         ADD target/CalculatorApp-1.0-SNAPSHOT.jar CalculatorApp-1.0-SNAPSHOT.jar
         ENTRYPOINT ["java", "-jar", "/CalculatorApp-1.0-SNAPSHOT.jar"]
  	
8.	Edit pom.xml file with  <finalName> CalculatorApp-1.0-SNAPSHOT</finalName>  add it after <plugins>,</plugins>

11.	Run mvn clean install, git commit and push updated repo.
	
12.	Start Docker Desktop
    
13.	Go to Jenkins and make necessary updations in pipeline now-
    
            pipeline {
                agent any
                tools {
                    maven '3.6.3'
                }
                stages {
                    stage("Build Maven") {
                        steps {
                            script {
                                // Specify the path to your POM file
                                def pomPath = '/home/namrata/.jenkins/workspace/docker_using_jenkins/calculator_app/pom.xml'
            
                                // Checkout your source code from your repository
                                checkout scmGit(
                                    branches: [[name: '*/master']],
                                    extensions: [],
                                    userRemoteConfigs: [[credentialsId: 'git1', url: 'https://github.com/namratasgit/Docker_using_Jenkins.git']]
                                )
            
                                // Build your project using Maven with the specified POM path
                                sh "mvn clean install -f ${pomPath}"
                            }
                        }
                    }
                    stage("Build Docker Image") {
                        steps {
                            script {
                                // Navigate to the directory containing the Dockerfile
                                dir('/home/namrata/Desktop/calculator_app') {
                                    // Build the Docker image
                                    sh 'docker build -t calculator_app_image .'
                                }
                            }
                        }
                    }
                }
            }

14.	Grant permission to Jenkin to access Dockerfile-

sudo chmod +r /home/namrata/Desktop/calculator_app/Dockerfile
sudo chown -R namrata:namrata /home/namrata/Desktop/calculator_app

15.	Build 

//Push Docker Image into Dockerhub
16.	Login to Dockerhub account and edit the pipeline
[get to pipeline sysntax—select “with credentials-…’’ 
In bindings add secret text – variable(dockerhubpwd) and add credentials
In add credentials--- kind(secret text), secret –Namrata@123 and add id -secret
And generate pipeline

            pipeline {
                agent any
                tools {
                    maven '3.6.3'
                }
                stages {
                    stage("Build Maven") {
                        steps {
                            script {
                                // Specify the path to your POM file
                                def pomPath = '/home/namrata/.jenkins/workspace/docker_using_jenkins/calculator_app/pom.xml'
            
                                // Checkout your source code from your repository
                                checkout scmGit(
                                    branches: [[name: '*/master']],
                                    extensions: [],
                                    userRemoteConfigs: [[credentialsId: 'git1', url: 'https://github.com/namratasgit/Docker_using_Jenkins.git']]
                                )
            
                                // Build your project using Maven with the specified POM path
                                sh "mvn clean install -f ${pomPath}"
                            }
                        }
                    }
                    stage("Build Docker Image") {
                        steps {
                            script {
                                // Navigate to the directory containing the Dockerfile
                                dir('/home/namrata/Desktop/calculator_app') {
                                    // Build the Docker image
                                    sh 'docker build -t calculator_app_image .'
                                }
                            }
                        }
                    }
                    stage("Push image to hub") {
                        steps {
                            script {
                                withCredentials([string(credentialsId: 'secret', variable: 'dockerhubpwd')]) {
                                    sh "docker login -u namratasdocker -p ${dockerhubpwd}"
                                    sh "docker tag image_id namratasdocker/docker_using_jenkins_pipeline:latest"
            
             sh "docker push namratasdocker/docker_using_jenkins_pipeline"
                                }
                            }
                        }
                    }
                    
                }
            }
            
17.	Build
    
18.	We can even create a Jenkins file in our project repo and copy paste the entire groovy script there. We then clean install, commit and push to git. In Jenkins configure, we need to select pipeline from scm and provide git credentials.




