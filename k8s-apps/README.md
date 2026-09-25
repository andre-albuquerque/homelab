# Homelab Kubernetes Apps

This repository contains the configuration for a Kubernetes-based homelab, managing applications using [Helm](https://helm.sh/) and [Helmfile](https://github.com/helmfile/helmfile), along with supporting manifests for infrastructure components.

## Applications

The following applications are deployed and managed:

### Core Infrastructure
*   **Ingress-Nginx:** Ingress controller using NGINX as a reverse proxy and load balancer.
*   **Cert-Manager:** Native Kubernetes certificate management.
*   **MetalLB:** Load-balancer implementation for bare metal Kubernetes clusters.
*   **Longhorn:** Distributed block storage system for Kubernetes.
*   **Minio:** High-performance, S3 compatible object storage.

### DevOps & Tools
*   **Jenkins:** Open-source automation server for CI/CD.
*   **Harbor:** Open-source registry for container images.

## Repository Structure

```
├── helmfile.yaml          # Main Helmfile configuration
├── manifests/             # Raw Kubernetes manifests (applied via kubectl)
│   ├── clusterIssuer/     # Cert-Manager ClusterIssuer
│   ├── metallb/           # MetalLB IPAddressPool and L2Advertisement
│   └── minio/             # Minio Deployment and Service
└── values/                # Helm chart values
    ├── cert-manager/
    ├── harbor/
    ├── ingress-nginx/
    ├── jenkins/
    └── longhorn/
```

## Prerequisites

Before deploying, ensure you have the following installed:

*   [Kubernetes Cluster](https://kubernetes.io/) (Accessible via `kubectl`)
*   [Helm](https://helm.sh/docs/intro/install/) (v3+)
*   [Helmfile](https://github.com/helmfile/helmfile#installation)
*   [kubectl](https://kubernetes.io/docs/tasks/tools/)

## Installation

### 1. Apply Infrastructure Manifests
Apply the raw manifests for MetalLB, Minio, and Cert-Manager configuration:

```bash
kubectl apply -f manifests/metallb/
kubectl apply -f manifests/minio/
kubectl apply -f manifests/clusterIssuer/
```

### 2. Deploy Helm Releases
Use Helmfile to deploy the main applications:

```bash
helmfile apply
```

## Configuration Details

### Networking & Ingress
*   **MetalLB IP Range:** `192.168.1.150 - 192.168.1.170`
*   **Ingress Hosts:**
    *   **Jenkins:** `jenkins.andrealbuquerque.me`
    *   **Harbor:** `harbor.andrealbuquerque.me`

### Storage
*   **Longhorn:** Default storage class.
*   **Minio:** Deployed as a standalone deployment in namespace `minio` (Service type: `ClusterIP`).

### Credentials
Check the `values.yaml` files in `values/` or your Kubernetes Secrets for initial credentials.
*   **Minio:** See `manifests/minio/minio.yaml` env vars (Default: `minioadmin` / `minioadmin`).

## Verification

After deployment, check the status of the pods:

```bash
kubectl get pods -A
```

Access the applications via their configured Ingress hosts (ensure your DNS or `/etc/hosts` resolves them to your LoadBalancer IP).
