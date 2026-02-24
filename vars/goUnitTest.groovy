def call (body) {
 
  def settings = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = settings
  body()
 
  container('go') {
    sh '''
      cd api
      for pkg in $(go list ./...); do
        echo ">>> TESTING $pkg"
        if ! go test -v -buildvcs=false -p=1 $pkg; then
          echo "‼️ FIRST FAIL IN PACKAGE $pkg ‼️"
          break
        fi
      done
    '''
  }
 
}