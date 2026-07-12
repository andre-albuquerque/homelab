# AGENTS.md – OpenCode Quick Reference

## Commands
- **helmfile apply** – Deploy all Helm releases.
- **kubectl apply -f manifests/metallb/** – LoadBalancer IPs.
- **kubectl apply -f manifests/minio/** – Object storage.
- **kubectl apply -f manifests/clusterIssuer/** – Cert‑Manager CRDs.
- Apply manifests first, then run helmfile.

## Namespaces
| Component        | Namespace          |
|------------------|--------------------|
| ingress‑nginx    | ingress-nginx      |
| longhorn         | longhorn-system    |
| jenkins          | jenkins            |
| harbor           | harbor             |
| cert‑manager     | cert-manager       |
| prometheus       | prometheus         |
| alloy            | alloy              |

## Ingress Hosts
- `jenkins.andrealbuquerque.me`
- `harbor.andrealbuquerque.me`
- Ensure DNS or `/etc/hosts` resolves to the LoadBalancer IP.

## Defaults & Secrets
- MinIO default credentials: `minioadmin:minioadmin` (change ASAP).

## Verification
- `kubectl get pods -A` – Confirm all pods are \`Running\`.
- Access apps via configured Ingress hosts.

## Gotchas
- Run manifest applies before `helmfile apply`; otherwise Helmfile fails.
- DNS must resolve Ingress hostnames.
- Update MinIO credentials after first login.