def call (body) {
 
  def settings = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = settings
  body()

  def podYaml = libraryResource('jenkinsPod.yaml')

  echo "Resource contents:\n${libraryResource('jenkinsPod.yaml')}"
 
  pipeline {
    agent {
      kubernetes {
        yamlFile podYaml
      }
    }
    stages {
      stage('Test') {
        steps {
          container('go') {
            sh '''
              echo $USER
            '''
          }
        }
      }
    }
  }
}