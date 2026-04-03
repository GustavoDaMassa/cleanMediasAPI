# Deploy — cleanMediasAPI

Guia de implantação no home server Ubuntu 24.04 via Cloudflare Tunnel.
Fluxo: `git push main → GitHub Actions → Docker Hub → Watchtower → restart automático`

---

## 1. GitHub — Secrets do repositório

Em **Settings → Secrets and variables → Actions**, adicionar:

| Secret | Valor |
|---|---|
| `DOCKER_USERNAME` | `gustavodamassa` |
| `DOCKER_PASSWORD` | Access Token do Docker Hub (não a senha da conta) |

Gerar o token em: Docker Hub → Account Settings → Personal access tokens → New token (permissão: Read & Write).

---

## 2. Cloudflare — DNS

No painel da Cloudflare, zona `financeapi.com.br`:

- Adicionar registro CNAME:
  - **Name:** `cleanmediasapi`
  - **Target:** `<tunnel-id>.cfargotunnel.com` (mesmo tunnel do financeApi)
  - **Proxy:** ativado (nuvem laranja)

---

## 3. Cloudflare Tunnel — rota

No servidor, editar a configuração do tunnel (normalmente em `~/.cloudflared/config.yml`):

```yaml
ingress:
  # entradas existentes do financeApi...
  - hostname: cleanmediasapi.financeapi.com.br
    service: http://localhost:80
  - service: http_status:404
```

Reiniciar o tunnel:

```bash
sudo systemctl restart cloudflared
```

> O Nginx do cleanMediasAPI escuta na porta 80. Se o financeApi também usa a porta 80,
> ver seção "Conflito de porta" no final deste arquivo.

---

## 4. Servidor — primeira vez

```bash
# 1. Criar diretório do projeto
mkdir -p ~/servidor/cleanmediasapi
cd ~/servidor/cleanmediasapi

# 2. Clonar o repositório
git clone https://github.com/gustavodamassa/cleanMediasAPI.git .

# 3. Criar o .env com os valores reais
cp .env.example .env
nano .env
```

Preencher o `.env` com:

```env
# Banco de dados
MYSQL_ROOT_PASSWORD=<senha-root-forte>
DATABASE_URL=jdbc:mysql://mysql:3306/cleanmediasapi
DATABASE_USERNAME=cleanmediasapi
DATABASE_PASSWORD=<senha-app-forte>

# JWT — gerar par RSA 2048:
#   openssl genrsa -out private.pem 2048
#   openssl rsa -in private.pem -pubout -out public.pem
#   cat private.pem | base64 -w 0   → JWT_PRIVATE_KEY_CONTENT
#   cat public.pem  | base64 -w 0   → JWT_PUBLIC_KEY_CONTENT
JWT_PUBLIC_KEY_CONTENT=<base64 do public.pem>
JWT_PRIVATE_KEY_CONTENT=<base64 do private.pem>

# CORS
CORS_ALLOWED_ORIGINS=https://cleanmediasapi.financeapi.com.br
```

```bash
# 4. Subir os containers
docker compose -f docker-compose.prod.yml up -d

# 5. Verificar que tudo subiu
docker compose -f docker-compose.prod.yml ps
docker compose -f docker-compose.prod.yml logs -f cleanmediasapi
```

A aplicação estará acessível em `https://cleanmediasapi.financeapi.com.br` assim que o tunnel estiver configurado.

---

## 5. Comandos úteis no servidor

```bash
cd ~/servidor/cleanmediasapi

# Ver status dos containers
docker compose -f docker-compose.prod.yml ps

# Ver logs da aplicação
docker compose -f docker-compose.prod.yml logs -f cleanmediasapi

# Ver logs do MySQL
docker compose -f docker-compose.prod.yml logs -f mysql

# Reiniciar apenas a aplicação
docker compose -f docker-compose.prod.yml restart cleanmediasapi

# Parar tudo
docker compose -f docker-compose.prod.yml down

# Forçar atualização manual da imagem (sem esperar o Watchtower)
docker compose -f docker-compose.prod.yml pull cleanmediasapi
docker compose -f docker-compose.prod.yml up -d cleanmediasapi
```

---

## 6. Fluxo de deploy contínuo

Após o setup inicial, o deploy acontece automaticamente:

```
git push main
  → GitHub Actions: mvn test (131 testes)
  → GitHub Actions: docker build + push gustavodamassa/clean-medias-api:latest
  → Watchtower (a cada 30s): detecta nova imagem → restart do container
```

Tempo estimado do push até o container atualizado: ~5 minutos.

---

## Conflito de porta 80

Se o financeApi já ocupa a porta 80 no servidor, **não é possível** que o cleanMediasAPI também escute na porta 80 diretamente.

**Solução:** usar portas distintas e apontar cada rota do tunnel para a porta correta.

Exemplo:

| Serviço | Porta no servidor | Tunnel aponta para |
|---|---|---|
| financeApi nginx | 80 | `http://localhost:80` |
| cleanmediasapi nginx | 8081 | `http://localhost:8081` |

Para isso, alterar no `docker-compose.prod.yml`:

```yaml
nginx:
  ports:
    - "8081:80"   # host:container
```

E no `~/.cloudflared/config.yml`:

```yaml
- hostname: cleanmediasapi.financeapi.com.br
  service: http://localhost:8081
```
