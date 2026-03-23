def call (body) {
 
  def settings = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = settings
  body()
 
  container('kaniko') {
    sh '''
      REGISTRY="harbor.andrealbuquerque.me/andrealbuquerqueme"
      REPOSITORY=${JOB_NAME%/*}
      IMAGE_TAG=${GIT_COMMIT:0:10}
      ENVIRONMENT="prod"

      DESTINATION="${REGISTRY}/${REPOSITORY}:${IMAGE_TAG}"

      /kaniko/executor \
        --dockerfile $(pwd)/docker/Dockerfile \
        --insecure \
        --skip-tls-verify \
        --destination "${DESTINATION}" \
        --context $(pwd)

      echo "${IMAGE_TAG}" > /artifacts/${ENVIRONMENT}.artifact
    '''
  }
 
}