pipeline {
    agent any

    parameters {
        choice(
            name: 'CONTAINER_NAME',
            choices: ['ecommerce-dev', 'ecommerce-test', 'ecommerce-prod'],
            description: 'Select container name'
        )
    }

    environment {
        IMAGE_NAME = "lohar45/jen_ansi_pro"
        IMAGE_TAG = "latest"
        DOCKERHUB_CREDENTIALS = "dockerhub-creds"
    }

    stages {

        stage('Checkout Code') {
            steps {
                git 'https://github.com/GauravLohar45/e-commerceWebApplication-DevOps.git'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
            }
        }

        stage('DockerHub Login') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: "${DOCKERHUB_CREDENTIALS}",
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )
                ]) {
                    sh "echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin"
                }
            }
        }

        stage('Push Image') {
            steps {
                sh "docker push ${IMAGE_NAME}:${IMAGE_TAG}"
            }
        }

        stage('Cleanup Local Image') {
            steps {
                sh "docker rmi ${IMAGE_NAME}:${IMAGE_TAG} || true"
            }
        }

        stage('Deploy via Ansible') {
            steps {
                sh """
                ansible-playbook deploy.yml \
                --extra-vars "container_name=${params.CONTAINER_NAME} image_name=${IMAGE_NAME} image_tag=${IMAGE_TAG}"
                """
            }
        }
    }

    post {
        success {
            echo "✅ Deployment Successful 🚀"
        }
        failure {
            echo "❌ Pipeline Failed"
        }
        always {
            echo "Pipeline Finished"
        }
    }
}
