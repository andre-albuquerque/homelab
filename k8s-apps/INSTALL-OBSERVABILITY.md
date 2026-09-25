# 🚀 Instalação Rápida - Stack de Observabilidade

## Pré-requisitos
- Cluster Kubernetes com pelo menos **2GB RAM livres**
- StorageClass `longhorn` configurado
- MinIO instalado e acessível (para o Loki)
- Ingress Controller (nginx) configurado

---

## 📦 Passo 1: Criar Buckets no MinIO

Antes de instalar o Loki, crie os buckets necessários:

```bash
# Port-forward para o MinIO
kubectl port-forward -n minio svc/minio 9000:9000

# Acessar no browser: http://localhost:9000
# Login: minioadmin / minioadmin

# Criar 3 buckets:
# 1. loki-chunks
# 2. loki-ruler
# 3. loki-admin
```

Ou via CLI do MinIO:
```bash
mc alias set myminio http://localhost:9000 minioadmin minioadmin
mc mb myminio/loki-chunks
mc mb myminio/loki-ruler
mc mb myminio/loki-admin
```

---

## 📦 Passo 2: Configurar DNS

Adicione ao `/etc/hosts` (ou DNS local):

```bash
<LOAD_BALANCER_IP> prometheus.andrealbuquerque.me
<LOAD_BALANCER_IP> grafana.andrealbuquerque.me
<LOAD_BALANCER_IP> loki.andrealbuquerque.me
```

Para descobrir o Load Balancer IP:
```bash
kubectl get svc -n ingress-nginx
```

---

## 📦 Passo 3: Instalar Stack

```bash
# Aplicar manifests de infraestrutura (se necessário)
kubectl apply -f manifests/metallb/
kubectl apply -f manifests/minio/
kubectl apply -f manifests/clusterIssuer/

# Instalar stack de observabilidade
helmfile apply
```

---

## 📦 Passo 4: Verificar Instalação

```bash
# Verificar pods
kubectl get pods -n prometheus
kubectl get pods -n grafana
kubectl get pods -n alloy
kubectl get pods -n loki

# Verificar se todos estão Running
kubectl get pods -A | grep -E 'prometheus|grafana|alloy|loki'
```

---

## 📦 Passo 5: Acessar Interfaces

### Prometheus
- URL: http://prometheus.andrealbuquerque.me
- Query Explorer: `/graph`
- Targets: `/targets`
- Rules: `/rules`

### Grafana
- URL: http://grafana.andrealbuquerque.me
- **Login**: `admin`
- **Senha**: `Grafana123`
- Datasources já configurados: Prometheus e Loki

### Alloy UI
```bash
kubectl port-forward -n alloy ds/alloy 12345:12345
```
- URL: http://localhost:12345

### Loki (via Grafana)
- Explore logs: http://grafana.andrealbuquerque.me/explore

---

## 🔧 Troubleshooting

### Pods não estão iniciando
```bash
# Verificar logs
kubectl logs -n prometheus deploy/prometheus-server
kubectl logs -n grafana deploy/grafana
kubectl logs -n loki deploy/loki
kubectl logs -n alloy ds/alloy

# Verificar eventos
kubectl get events -n prometheus --sort-by='.lastTimestamp'
kubectl get events -n grafana --sort-by='.lastTimestamp'
kubectl get events -n loki --sort-by='.lastTimestamp'
kubectl get events -n alloy --sort-by='.lastTimestamp'
```

### Loki não conecta ao MinIO
```bash
# Verificar se buckets existem
kubectl port-forward -n minio svc/minio 9000:9000
# Acessar http://localhost:9000 e verificar buckets

# Verificar secret do MinIO
kubectl get secret -n longhorn-system longhorn-backup-minio -o yaml
```

### Prometheus não scrapeia targets
```bash
# Verificar targets
kubectl port-forward -n prometheus deploy/prometheus-server 9090:80
# Acessar http://localhost:9090/targets

# Verificar service discovery
kubectl port-forward -n prometheus deploy/prometheus-server 9090:80
# Acessar http://localhost:9090/service-discovery
```

### Grafana não mostra dados
```bash
# Verificar datasources
kubectl port-forward -n grafana deploy/grafana 3000:80
# Acessar http://localhost:3000/connections/datasources

# Testar conexão Prometheus e Loki
```

---

## 📊 Consumo de Recursos

### Verificar consumo em tempo real
```bash
kubectl top pods -n prometheus
kubectl top pods -n grafana
kubectl top pods -n alloy
kubectl top pods -n loki
```

### Consumo esperado (média)
| Componente | CPU | Memória |
|------------|-----|---------|
| Prometheus | 100m | 300Mi |
| Grafana | 100m | 256Mi |
| Alloy (por node) | 50m | 128Mi |
| Loki | 200m | 512Mi |
| **Total** | **~450m** | **~1.2GB** |

---

## 🔐 Segurança - Próximos Passos

1. **Mudar senha do Grafana**:
   - Acessar Profile → Change Password
   - Ou: `kubectl edit secret -n grafana grafana`

2. **Mudar credenciais do MinIO**:
   ```bash
   kubectl edit secret -n minio minio-secret
   ```

3. **Habilitar TLS**:
   - Configurar cert-manager para cadastrar certificados
   - Atualizar ingress de cada componente

4. **Configurar autenticação no Prometheus**:
   - Adicionar nginx auth ou oauth2-proxy

---

## 📚 Dashboards Recomendados

Importar no Grafana (IDs do grafana.com):

- **Kubernetes Cluster**: `6417`
- **Kubernetes Pods**: `6336`
- **Node Exporter**: `1860`
- **Prometheus**: `2`
- **Loki Stats**: `13434`

---

## ✅ Checklist de Verificação

- [ ] Buckets do MinIO criados
- [ ] DNS configurado
- [ ] Todos os pods Running
- [ ] Prometheus scrapeando targets
- [ ] Grafana acessível
- [ ] Datasources configurados no Grafana
- [ ] Alloy coletando métricas
- [ ] Loki recebendo logs (se configurado)

---

## 🆘 Suporte

Se encontrar problemas:
1. Verificar logs dos pods
2. Verificar eventos do Kubernetes
3. Checar conectividade entre serviços
4. Validar configurações de storage
5. Verificar se MinIO está acessível

Documentação completa: `OBSERVABILITY.md`