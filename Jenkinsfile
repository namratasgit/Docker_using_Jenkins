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
