pipeline {
    agent any

    parameters {
        choice(
            name: 'CONTAINER_NAME',
            choices: ['static-webapp-dev', 'static-webapp-test', 'static-webapp-prod'],
            description: 'Select container name to run'
        )
    }

    environment {
        IMAGE_NAME = "venkateshjaggaraju/static-webapp"
        IMAGE_TAG = "v1"
        DOCKERHUB_CREDENTIALS = "dockerhub-creds"
    }

    stages {

        stage('Stage-1: Build Docker Image') {
            steps {
                sh "docker build -t ${IMAGE_NAME}:latest ."
            }
        }

        stage('Stage-2: Tag Docker Image') {
            steps {
                sh "docker tag ${IMAGE_NAME}:latest ${IMAGE_NAME}:${IMAGE_TAG}"
            }
        }

        stage('Stage-3: Login to DockerHub') {
            steps {
                withCredentials([usernamePassword(credentialsId: "${DOCKERHUB_CREDENTIALS}", usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh "echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin"
                }
            }
        }

        stage('Stage-4: Push Image to DockerHub') {
            steps {
                sh "docker push ${IMAGE_NAME}:${IMAGE_TAG}"
            }
        }

        stage('Stage-5: Remove Image Locally') {
            steps {
                sh "docker rmi ${IMAGE_NAME}:${IMAGE_TAG} || true"
            }
        }

        stage('Stage-6: Trigger Ansible Playbook') {
            steps {
                sh "ansible-playbook deploy.yml --extra-vars \"container_name=${params.CONTAINER_NAME}\""
            }
        }
    }

    post {
        success {
            echo "Pipeline completed successfully 🎉"
        }
        failure {
            echo "Pipeline failed ❌"
        }
        always {
            echo "Pipeline execution finished"
        }
    }
}
