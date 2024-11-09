pipeline {
    agent any
    stages {
        stage('Compile') {
            steps {
                script {
                    def rootDir = "application/backend/business/order-hub-v1"
                    echo "Changing directory to: ${rootDir}"
                    dir(rootDir) {
                        sh "ls -la"
                        def mvnCmd = 'mvn clean package'
                        def mvnOutput = sh(script: mvnCmd, returnStdout: true).trim()
                        if (mvnOutput.contains('BUILD SUCCESS')) {
                            echo 'Maven build successful'
                        } else {
                            error 'Maven build failed'
                        }
                    }
                }
            }
        }
        stage('Deploy') {
            steps {
                withCredentials(bindings: [ string(credentialsId: 'k8s-cluster-token', variable: 'api_token') ]) {
                    sh 'kubectl apply -f devops/k8s-manifests/order-hub-v1.yaml --server https://d244-179-6-4-43.ngrok-free.app --token $api_token --insecure-skip-tls-verify=true'
                }
            }
        }
    }
}
