pipeline {
    agent any

    environment {
        IMAGE_NAME = "lohar45/jen_ansi_dev"
        TAG = "latest"
        REPO_URL = "https://github.com/GauravLohar45/e-commerceWebApplication-DevOps.git"
        BRANCH = "main"
    }

    stages {

        stage('Checkout Code') {
            steps {
                git branch: "${BRANCH}", url: "${REPO_URL}"
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t $IMAGE_NAME:$TAG .'
            }
        }

        stage('Push to DockerHub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub', usernameVariable: 'USER', passwordVariable: 'PASS')]) {
                    sh '''
                    echo $PASS | docker login -u $USER --password-stdin
                    docker push $IMAGE_NAME:$TAG
                    '''
                }
            }
        }

        stage('Deploy via Ansible') {
            steps {
                sh 'ansible-playbook deploy.yml'
            }
        }
    }
}
