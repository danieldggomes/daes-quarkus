# demo-crud

## Uso do projeto

Execução em modo dev (com live reload):

```bash
./mvnw quarkus:dev
```

Build JVM (gera `target/quarkus-app/`):

```bash
./mvnw package
```

Executar o JAR:

```bash
java -jar target/quarkus-app/quarkus-run.jar
```

Build nativo:

```bash
./mvnw package -Dnative
```

Build nativo em container (sem Mandrel/GraalVM local):

```bash
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

Executar o binário nativo:

```bash
./target/demo-crud-1.0.0-SNAPSHOT-runner
```


## Modelo de domínio (POC Previdência Social)

Entidades principais:
- Cidadão
- Benefício
- Contribuição
- Pagamento de Benefício

Relacionamentos:

```text
Cidadão
  ├──< Contribuição
  └──< Benefício
          └──< Pagamento de Benefício
```

## Carga inicial (seed) para testes

Os dados iniciais ficam em `src/main/resources/import.sql` e são carregados no startup.

Observação: o POC está com `quarkus.hibernate-orm.database.generation=drop-and-create` para evitar duplicidades. Ajuste para `update` em ambientes persistentes.

## Exemplos de payloads JSON (POST/PUT)

### POST /cidadaos

```json
{
  "cpf": "12345678901",
  "nome": "Ana Oliveira",
  "dataNascimento": "1988-02-10",
  "sexo": "FEMININO",
  "dataCadastro": "2024-01-20"
}
```

### POST /beneficios

```json
{
  "tipoBeneficio": "APOSENTADORIA_IDADE",
  "dataConcessao": "2020-01-10",
  "valorMensal": 3200.00,
  "status": "ATIVO",
  "cidadao": { "id": 1 }
}
```

### POST /contribuicoes

```json
{
  "competencia": "2024-01",
  "valor": 700.00,
  "tipoContribuicao": "EMPREGADO",
  "cidadao": { "id": 1 }
}
```

### POST /pagamentos

```json
{
  "competencia": "2024-12",
  "valorPago": 3200.00,
  "dataPagamento": "2024-12-05",
  "beneficio": { "id": 1 }
}
```

## Docker Compose (local)

```bash
./mvnw -B package

docker compose up --build
```

## Build nativo com Mandrel

### Opção A: build local (Mandrel/GraalVM instalado)

```bash
./mvnw -B package -Dnative
```

### Opção B: build nativo em container (sem Mandrel local)

```bash
./mvnw -B package -Dnative -Dquarkus.native.container-build=true
```

Se você tiver uma imagem Mandrel corporativa, pode apontar via:

```bash
./mvnw -B package -Dnative -Dquarkus.native.container-build=true \
  -Dquarkus.native.builder-image=<MANDREL_BUILDER_IMAGE>
```

## Containerfile para nativo

Já existe o `Containerfile.native` no root do projeto. Build da imagem native:

```bash
docker build -f Containerfile.native -t demo-crud:native .
```

Executar:

```bash
docker run --rm -p 8080:8080 demo-crud:native
```

## Dockerfiles nativos com Mandrel (multi-stage)

Build da imagem native (runtime UBI minimal):

```bash
docker build -f src/main/docker/Dockerfile.native-mandrel -t demo-crud:native-mandrel .
```

Build da imagem native micro (runtime menor):

```bash
docker build -f src/main/docker/Dockerfile.native-micro-mandrel -t demo-crud:native-micro-mandrel .
```

## Exemplos de curl (teste rápido)

Criar Cidadão:

```bash
curl -X POST http://localhost:8080/cidadaos \
  -H "Content-Type: application/json" \
  -d '{
    "cpf": "12345678901",
    "nome": "Ana Oliveira",
    "dataNascimento": "1988-02-10",
    "sexo": "FEMININO",
    "dataCadastro": "2024-01-20"
  }'
```

Listar Cidadãos:

```bash
curl http://localhost:8080/cidadaos
```

Criar Benefício:

```bash
curl -X POST http://localhost:8080/beneficios \
  -H "Content-Type: application/json" \
  -d '{
    "tipoBeneficio": "APOSENTADORIA_IDADE",
    "dataConcessao": "2020-01-10",
    "valorMensal": 3200.00,
    "status": "ATIVO",
    "cidadao": { "id": 1 }
  }'
```

Listar pagamentos por benefício:

```bash
curl http://localhost:8080/beneficios/1/pagamentos
```

Criar Contribuição:

```bash
curl -X POST http://localhost:8080/contribuicoes \
  -H "Content-Type: application/json" \
  -d '{
    "competencia": "2024-01",
    "valor": 700.00,
    "tipoContribuicao": "EMPREGADO",
    "cidadao": { "id": 1 }
  }'
```

Listar contribuições por cidadão:

```bash
curl http://localhost:8080/cidadaos/1/contribuicoes
```

Criar pagamento de benefício:

```bash
curl -X POST http://localhost:8080/pagamentos \
  -H "Content-Type: application/json" \
  -d '{
    "competencia": "2024-12",
    "valorPago": 3200.00,
    "dataPagamento": "2024-12-05",
    "beneficio": { "id": 1 }
  }'
```
# Tutorial: preparar ambiente e criar o projeto Quarkus demo

Este guia instala o Maven 3.9.x via binário (opção 2) e cria o projeto **Quarkus demo CRUD** com PostgreSQL.

## 1) Instalar Maven 3.9.x (binário manual)

```bash
cd /tmp
curl -LO https://archive.apache.org/dist/maven/maven-3/3.9.8/binaries/apache-maven-3.9.8-bin.tar.gz
tar -xzf apache-maven-3.9.8-bin.tar.gz
mkdir -p "$HOME/tools"
mv /tmp/apache-maven-3.9.8 "$HOME/tools/"
```

Ativar na sessão atual:

```bash
export PATH="$HOME/tools/apache-maven-3.9.8/bin:$PATH"
mvn -v
```

Tornar permanente:

```bash
echo 'export PATH="$HOME/tools/apache-maven-3.9.8/bin:$PATH"' >> ~/.bashrc
```

## 1.1) Instalar JDK 21 (Ubuntu/Debian)

```bash
sudo apt-get update
sudo apt-get install -y openjdk-21-jdk
```

Configurar na sessão atual:

```bash
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
export PATH="$JAVA_HOME/bin:$PATH"
java -version
javac -version
```

## 1.2) Instalar Docker (script oficial)

```bash
curl -fsSL https://get.docker.com -o install-docker.sh
sudo sh install-docker.sh
```

Pós-instalação:

```bash
sudo usermod -aG docker "$USER"
newgrp docker
docker version
docker compose version
```

Se o daemon não estiver rodando:

```bash
sudo service docker start
```

## 2) Criar o projeto Quarkus demo

No diretório do workspace:

```bash
cd /home/danielgomes/workspace/quarkus-poc
mvn -B io.quarkus.platform:quarkus-maven-plugin:create \
  -DprojectGroupId=com.example.previdencia \
  -DprojectArtifactId=demo-crud \
  -DprojectVersion=1.0.0-SNAPSHOT \
  -Dextensions="rest-jackson,hibernate-orm-panache,hibernate-validator,jdbc-postgresql" \
  -DnoCode
```

Isso cria o projeto em:

```
/home/danielgomes/workspace/quarkus-poc/demo-crud
```

## 3) Rodar local com Docker Compose (backend + PostgreSQL)

A ideia é subir **2 containers**: `app` (Quarkus) e `db` (PostgreSQL).

### 3.1) Ajustar `application.properties`

Edite `demo-crud/src/main/resources/application.properties` e configure:

```properties
quarkus.datasource.db-kind=postgresql
quarkus.datasource.username=postgres
quarkus.datasource.password=postgres
quarkus.datasource.jdbc.url=jdbc:postgresql://db:5432/previdencia
quarkus.hibernate-orm.database.generation=update
quarkus.hibernate-orm.log.sql=true
```

### 3.2) Build do app (JVM)

```bash
cd /home/danielgomes/workspace/quarkus-poc/demo-crud
mvn -B package
```

### 3.2.1) Rebuild limpo (garantir novo pacote Java no container)

Use este fluxo quando alterar `import.sql`, entidades ou qualquer código e quiser garantir que o container use o **novo JAR**:

```bash
cd /home/danielgomes/workspace/quarkus-poc/demo-crud
./mvnw -B package
docker compose down -v
docker compose build --no-cache app
docker compose up
```

### 3.3) docker-compose.yml

Crie um `docker-compose.yml` em `demo-crud/`:

```yaml
services:
  db:
    image: postgres:16
    environment:
      POSTGRES_DB: previdencia
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U postgres"]
      interval: 10s
      timeout: 5s
      retries: 5

  app:
    build:
      context: .
      dockerfile: src/main/docker/Dockerfile.jvm
    depends_on:
      db:
        condition: service_healthy
    ports:
      - "8080:8080"
    environment:
      QUARKUS_DATASOURCE_USERNAME: postgres
      QUARKUS_DATASOURCE_PASSWORD: postgres
      QUARKUS_DATASOURCE_JDBC_URL: jdbc:postgresql://db:5432/previdencia
```

Subir tudo:

```bash
docker compose up
```

Parar:

```bash
docker compose down
```

## 4) Próximos passos (resumo rápido)

- Ajustar `application.properties` para o PostgreSQL
- Criar as entidades JPA (Cidadão, Benefício, Contribuição, PagamentoBenefício)
- Criar os recursos REST (CRUD)
- Build JVM: `./mvnw package`
- Build nativo (Mandrel/GraalVM): `./mvnw package -Dnative`

Se quiser, posso complementar este tutorial com:

- configuração completa do PostgreSQL (local e OpenShift)
- entidades e endpoints prontos
- Dockerfile/Containerfile
- passos para build native com Mandrel

## 5) Exemplos de curl (teste rápido)

Criar Cidadão:

```bash
curl -X POST http://localhost:8080/cidadaos \
  -H "Content-Type: application/json" \
  -d '{
    "cpf": "12345678901",
    "nome": "Ana Oliveira",
    "dataNascimento": "1988-02-10",
    "sexo": "FEMININO",
    "dataCadastro": "2024-01-20"
  }'
```

Listar Cidadãos:

```bash
curl http://localhost:8080/cidadaos
```

Criar Benefício:

```bash
curl -X POST http://localhost:8080/beneficios \
  -H "Content-Type: application/json" \
  -d '{
    "tipoBeneficio": "APOSENTADORIA_IDADE",
    "dataConcessao": "2020-01-10",
    "valorMensal": 3200.00,
    "status": "ATIVO",
    "cidadao": { "id": 1 }
  }'
```

Listar pagamentos por benefício:

```bash
curl http://localhost:8080/beneficios/1/pagamentos
```

Criar Contribuição:

```bash
curl -X POST http://localhost:8080/contribuicoes \
  -H "Content-Type: application/json" \
  -d '{
    "competencia": "2024-01",
    "valor": 700.00,
    "tipoContribuicao": "EMPREGADO",
    "cidadao": { "id": 1 }
  }'
```

Listar contribuições por cidadão:

```bash
curl http://localhost:8080/cidadaos/1/contribuicoes
```

Criar pagamento de benefício:

```bash
curl -X POST http://localhost:8080/pagamentos \
  -H "Content-Type: application/json" \
  -d '{
    "competencia": "2024-12",
    "valorPago": 3200.00,
    "dataPagamento": "2024-12-05",
    "beneficio": { "id": 1 }
  }'
```

---

## Documentação da API (OpenAPI / Swagger / Redoc)

### OpenAPI (obrigatório)

- JSON: `http://localhost:8080/openapi`
- YAML: `http://localhost:8080/openapi?format=yaml`

### Swagger UI (dev/test)

- `http://localhost:8080/swagger-ui`

### Redoc (apresentação executiva)

- `http://localhost:8080/redoc.html`

Observação: o Redoc é servido como arquivo estático em `src/main/resources/META-INF/resources/redoc.html`.

---

## OpenAPI, Swagger UI e Redoc (local, homologação e produção)

### O que foi configurado neste projeto

- **SmallRye OpenAPI** (obrigatório): gera o contrato em `/openapi`
- **Swagger UI**: interface interativa em `/swagger-ui`
- **Redoc**: página estática em `/redoc.html`
  - O bundle foi **embutido localmente** em `src/main/resources/META-INF/resources/redoc.standalone.js` para evitar bloqueio de CDN

Arquivos envolvidos:

- `pom.xml` (dependências)
- `src/main/resources/application.properties` (paths)
- `src/main/resources/META-INF/resources/redoc.html`
- `src/main/resources/META-INF/resources/redoc.standalone.js`

### Como funciona em cada ambiente

**Local (dev/test)**

- **Swagger UI** e **Redoc** são úteis para testes e apresentação
- OpenAPI fica acessível em `/openapi`

**Homologação / Produção**

- **OpenAPI** normalmente continua ativo, pois é útil para integração e governança
- **Swagger UI** e **Redoc** podem ser:
  - **mantidos** (se for política interna), ou
  - **desabilitados** por segurança

### Recomendações de configuração por ambiente

Sugestão de propriedades (exemplo):

```properties
# Sempre manter OpenAPI
quarkus.smallrye-openapi.path=/openapi

# Swagger UI apenas em dev/test
%dev.quarkus.swagger-ui.always-include=true
%test.quarkus.swagger-ui.always-include=true
%prod.quarkus.swagger-ui.always-include=false

# Redoc pode ser mantido ou removido em prod
# Se quiser ocultar em prod, não copie o redoc.html/redoc.standalone.js
```

### Vou ter que mudar algo quando sair do local?

Depende da política do ambiente:

- **Se segurança exigir**: mantenha apenas `/openapi` e remova/oculte Swagger UI e Redoc.
- **Se o objetivo for facilitar integração**: mantenha `/openapi` e, opcionalmente, Redoc.

### Como desabilitar Swagger UI em produção

No `application.properties`:

```properties
%prod.quarkus.swagger-ui.always-include=false
```

Se preferir desabilitar totalmente, remova a dependência:

```xml
<dependency>
  <groupId>io.quarkus</groupId>
  <artifactId>quarkus-swagger-ui</artifactId>
</dependency>
```

### Redoc em produção

- Se quiser manter, basta deixar os arquivos estáticos em `META-INF/resources`
- Se quiser remover, apague os arquivos:
  - `redoc.html`
  - `redoc.standalone.js`

Assim você controla o que fica exposto em cada ambiente.

### Política corporativa sugerida 

| Ambiente             | SmallRye OpenAPI | Swagger UI | Redoc |
|----------------------|------------------|------------|-------|
| Dev                  | Sim              | Sim        | Sim   |
| Homologação          | Sim              | Sim        | Sim   |
| Produção (interna)   | Sim              | Não        | Sim (opcional) |
| Produção (externa)   | Sim              | Não        | Protegido |

### Como refletir isso na configuração

Sugestao (mantem OpenAPI sempre ativo, desliga Swagger em prod):

```properties
# OpenAPI sempre ativo
quarkus.smallrye-openapi.path=/openapi

# Swagger UI so em dev e homologacao
%dev.quarkus.swagger-ui.always-include=true
%test.quarkus.swagger-ui.always-include=true
%prod.quarkus.swagger-ui.always-include=false
```

### Redoc em producao

- **Produção interna**: pode manter `redoc.html` e `redoc.standalone.js` se o acesso for interno.
- **Produção externa**: manter apenas se estiver protegido (ex.: autenticacao, WAF, VPN ou roteamento interno).
- Para remover completamente, apague:
  - `src/main/resources/META-INF/resources/redoc.html`
  - `src/main/resources/META-INF/resources/redoc.standalone.js`

