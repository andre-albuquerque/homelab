# Homelab Kubernetes Cluster

This project automates the deployment of a production-ready Kubernetes cluster on a homelab environment using Proxmox, Terraform, and Kubespray.

## Project Overview

The project is structured into three main parts:

1.  **Infrastructure Provisioning:** [Terraform](https://www.terraform.io/) is used to provision the virtual machines for the Kubernetes cluster on a [Proxmox](https://www.proxmox.com/en/) server.
2.  **Kubernetes Deployment:** [Kubespray](https://kubespray.io/) (an Ansible-based tool) is used to deploy a highly available and configurable Kubernetes cluster onto the provisioned VMs.
3.  **Automation:** A `Jenkinsfile` is included to automate the entire process, from infrastructure provisioning to Kubernetes deployment.

## Prerequisites

Before you begin, ensure you have the following:

*   A Proxmox server with a `cloud-init` enabled VM template ready for cloning.
*   Terraform installed.
*   Ansible installed.
*   Access to an S3-compatible object storage for Terraform state (e.g., MinIO).
*   A Jenkins instance for automated deployments (optional).

## How to Use

### 1. Configure the Infrastructure

Navigate to the `terraform/` directory and customize the following files:

*   `provider.tf`: Configure the Proxmox provider and the S3 backend for Terraform state.
*   `locals.tf`: Adjust the number of master and worker nodes, their resources (CPU, memory, disk), network settings, and other VM parameters.

Once configured, initialize Terraform and apply the configuration:

```bash
cd terraform
terraform init
terraform apply
```

This will create the master and worker VMs on your Proxmox server.

### 2. Prepare the Kubespray Inventory

After the VMs are created, Terraform will generate an Ansible inventory file named `inventory.ini` in the `terraform/` directory.

Copy this inventory file to the `kubespray/inventory/homelab/` directory:

```bash
cp terraform/inventory.ini kubespray/inventory/homelab/
```

### 3. Deploy Kubernetes with Kubespray

Navigate to the `kubespray/` directory and run the Ansible playbook to deploy the Kubernetes cluster. It's recommended to use the Docker container as described in the official Kubespray documentation:

```bash
cd kubespray
docker run --rm -it \
  --mount type=bind,source="$(pwd)"/inventory/homelab,dst=/inventory \
  --mount type=bind,source="${HOME}"/.ssh/id_rsa,dst=/root/.ssh/id_rsa \
  quay.io/kubespray/kubespray:v2.29.0 bash
```

Inside the container, run the playbook:

```bash
ansible-playbook -i /inventory/inventory.ini --private-key /root/.ssh/id_rsa cluster.yml
```

### 4. (Optional) Automated Deployment with Jenkins

The included `Jenkinsfile` provides a pipeline for automating the entire process. You will need to configure a Jenkins job and customize the pipeline script to match your environment.

The `jenkins/` directory also contains a `docker-compose.yml` file to easily spin up a Jenkins and MinIO (for Terraform state) instance.

## Customization

*   **Terraform:** The `terraform/` directory contains all the infrastructure-as-code definitions. You can modify the `.tf` files to change the VM specifications, network configuration, etc.
*   **Kubespray:** The `kubespray/` directory is a git submodule pointing to the official Kubespray repository. You can customize the Kubernetes cluster configuration by modifying the files in `kubespray/inventory/homelab/group_vars/`. For example, you can change the network plugin, enable/disable addons, and configure other cluster-wide settings.
