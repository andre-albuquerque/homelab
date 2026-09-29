def call (body) {
  
  def settings = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = settings
  body()

  def appDir = settings.appDir ?: ''
  if (appDir && !appDir.endsWith('/')) {
    appDir += '/'
  }

  container('go') {
    sh """
      cd ${appDir}api
      go test ./... -buildvcs=false -p=1
    """
  } 
}