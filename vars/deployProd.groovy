def call (body) {
	def settings = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = settings
  body()
	
	container('alpine') {
    sh '''
			apk add git
			
			GITEA_URL=http://gitea.andrealbuquerque.me
			REPO=andrealbuquerqueme/flux-cluster
			APP_NAME=real-world-api
			IMAGE_TAG="$(cat /artifacts/prod.artifact)"

			git clone ${GITEA_URL}/${REPO}.git

			cd flux-cluster

			# Update image tag
			sed -i "/image: .*${APP_NAME}:/ s|:[^[:space:]]*|:${IMAGE_TAG}|" clusters/homelab/apps/real-world-api/deployment.yaml

			git config user.name "jenkins"
			git config user.email "jenkins@ci.local"

			git add .
			git commit -m "Deploy to production - build ${IMAGE_TAG}"
			git push origin main
    '''
}
}