# Docker_using_Jenkins

1.	Create a new pipeline job
2.	Configure  General  github project(provide repo url), build triggers git hook trigger for scm polling, pipeline follow below steps
3.	Go to Pipeline
Pipeline syntax-
Steps to be followed-
checkout: checkout from version control
a. SCM – Git, Repo URL- …., Branches to build  */master
b. Generate the pipeline and copy paste in the script

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

4.	Build


// Build Docker Image
5.	Download and install Docker
    visit link (https://docs.docker.com/desktop/install/ubuntu/)
e.	Download DEB package
f.	Set up Docker's package repository[using Apt repository] visit link -  https://docs.docker.com/engine/install/ubuntu/#install-using-the-repository
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

6.	Now go to vscode and add dockerfile directly inside calculator_app directory and add the following-
FROM openjdk:11
EXPOSE 8080
ADD target/CalculatorApp-1.0-SNAPSHOT.jar CalculatorApp-1.0-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/CalculatorApp-1.0-SNAPSHOT.jar"]
7.	Edit pom.xml file with  <finalName> CalculatorApp-1.0-SNAPSHOT</finalName>  add it after <plugins>,</plugins>
8.	Run mvn clean install, git commit and push updated repo.
9.	Start Docker Desktop
10.	Go to Jenkins and make necessary updations in pipeline now-
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

11.	Grant permission to Jenkin to access Dockerfile-
sudo chmod +r /home/namrata/Desktop/calculator_app/Dockerfile
sudo chown -R namrata:namrata /home/namrata/Desktop/calculator_app
12.	Build 

//Push Docker Image into Dockerhub
13.	Login to Dockerhub account and edit the pipeline
[got to pipeline sysntax—select “with credentials-…’’ 
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
14.	Build
15.	We can even create a Jenkins file in our project repo and copy paste the entire groovy script there. We then clean install, commit and push to git. In Jenkins configure, we need to select pipeline from scm and provide git credentials.
