# DevOps Practice Lab

This repository serves as a practice lab for DevOps, demonstrating CI pipeline with a GoLang Gin Realworld application, Docker, Jenkins, and Kubernetes.

## Repository Structure
```
├── api/                                  # Application source (RealWorld API)
│   ├── articles/
│   ├── common/
│   ├── users/
│   ├── hello.go
│   ├── go.mod
│   ├── go.sum
│   └── .env.example
│
├── docker/
│   └── Dockerfile
│
├── ci/                                   # CI (Jenkins)
│   ├── Jenkinsfile
│   └── scripts/
│       ├── build.sh                     # build + test
│       ├── image.sh                     # docker build + push
│       └── update-image-tag.sh          # update k8s manifests
│
├── scripts/                              # Local helpers
│   ├── local-run.sh
│   └── migrate.sh
│
├── Makefile
├── .gitignore
└── README.md
```


