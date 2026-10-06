def call (body) {

  def settings = [:]
  if (body instanceof Map) {
    settings << body
  } else {
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = settings
    body()
  }

  def appDir = settings.appDir ?: ''
  if (appDir && !appDir.endsWith('/')) {
    appDir += '/'
  }

  container('kaniko') {
    sh """
      REGISTRY="harbor.andrealbuquerque.me/andrealbuquerqueme"
      REPOSITORY=\${JOB_NAME%/*}
      IMAGE_TAG=\${GIT_COMMIT:0:10}
      ENVIRONMENT="prod"

      DESTINATION="\${REGISTRY}/\${REPOSITORY}:\${IMAGE_TAG}"

      /kaniko/executor \\
        --dockerfile \$(pwd)/${appDir}docker/Dockerfile \\
        --insecure \\
        --skip-tls-verify \\
        --destination "\${DESTINATION}" \\
        --context \$(pwd)/${appDir}

      echo "\${IMAGE_TAG}" > /artifacts/\${ENVIRONMENT}.artifact
    """
  }

}
