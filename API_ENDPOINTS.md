# TechFlow API - Endpoints para o Frontend

Documentação simples para integração do frontend com a API do projeto **Sistema de Gestão do Conhecimento para Suporte de TI**.

## Base URL local

```txt
http://localhost:8080
```

## Observações gerais

- A API retorna dados em JSON.
- Para requisições `POST` e `PUT`, usar o header:

```http
Content-Type: application/json
```

- O endpoint de IA depende da variável de ambiente `GEMINI_API_KEY` configurada no backend.
- A senha do técnico é enviada no cadastro, mas não aparece nas respostas da API.

---

# Endpoint raiz

## Informações da API

```http
GET /
```

Resposta esperada:

```json
{
  "nome": "TechFlow API",
  "descricao": "API do Sistema de Gestão do Conhecimento para Suporte de TI",
  "status": "online",
  "versao": "1.0.0",
  "endpoints": {
    "categorias": "/categorias",
    "tecnicos": "/tecnicos",
    "problemas": "/problemas",
    "solucoes": "/solucoes",
    "chamados": "/chamados",
    "dashboard": "/dashboard/resumo",
    "ia": "/ia/analisar-problema"
  }
}
```

---

# Categorias

## Listar categorias

```http
GET /categorias
```

## Buscar categoria por ID

```http
GET /categorias/{id}
```

## Criar categoria

```http
POST /categorias
```

Body:

```json
{
  "nome": "Rede",
  "descricao": "Problemas relacionados à internet, Wi-Fi, DNS e conexão."
}
```

## Atualizar categoria

```http
PUT /categorias/{id}
```

Body:

```json
{
  "nome": "Rede e Conectividade",
  "descricao": "Problemas relacionados à internet, Wi-Fi, DNS, IP e conexão."
}
```

## Deletar categoria

```http
DELETE /categorias/{id}
```

---

# Técnicos

## Listar técnicos

```http
GET /tecnicos
```

## Buscar técnico por ID

```http
GET /tecnicos/{id}
```

## Criar técnico

```http
POST /tecnicos
```

Body:

```json
{
  "nome": "Higor Valuar",
  "email": "higor@techflow.com",
  "senha": "123456",
  "nivelAcesso": "ADMIN"
}
```

Resposta esperada:

```json
{
  "id": 1,
  "nome": "Higor Valuar",
  "email": "higor@techflow.com",
  "nivelAcesso": "ADMIN"
}
```

## Atualizar técnico

```http
PUT /tecnicos/{id}
```

Body:

```json
{
  "nome": "Higor Valuar",
  "email": "higor@techflow.com",
  "senha": "123456",
  "nivelAcesso": "ADMIN"
}
```

## Deletar técnico

```http
DELETE /tecnicos/{id}
```

---

# Problemas

## Listar problemas

```http
GET /problemas
```

## Buscar problema por ID

```http
GET /problemas/{id}
```

## Criar problema

```http
POST /problemas
```

Body:

```json
{
  "titulo": "Computador sem acesso à internet",
  "descricao": "O computador está conectado ao Wi-Fi, mas não consegue acessar sites.",
  "categoria": {
    "id": 1
  },
  "tecnico": {
    "id": 1
  }
}
```

Observação:

- `categoria.id` deve ser o ID de uma categoria já cadastrada.
- `tecnico.id` deve ser o ID de um técnico já cadastrado.

## Atualizar problema

```http
PUT /problemas/{id}
```

Body:

```json
{
  "titulo": "Computador sem acesso à internet",
  "descricao": "O computador conecta ao Wi-Fi, mas não acessa nenhum site.",
  "categoria": {
    "id": 1
  },
  "tecnico": {
    "id": 1
  }
}
```

## Deletar problema

```http
DELETE /problemas/{id}
```

---

# Soluções

## Listar soluções

```http
GET /solucoes
```

## Buscar solução por ID

```http
GET /solucoes/{id}
```

## Criar solução

```http
POST /solucoes
```

Body:

```json
{
  "descricaoSolucao": "Foi verificado que o DNS estava configurado incorretamente. A solução foi alterar o DNS para 8.8.8.8 e 1.1.1.1, reiniciar o adaptador de rede e testar novamente o acesso.",
  "resumoIa": "Correção de DNS e reinicialização do adaptador de rede.",
  "problema": {
    "id": 1
  }
}
```

Observação:

- `problema.id` deve ser o ID de um problema já cadastrado.

## Atualizar solução

```http
PUT /solucoes/{id}
```

Body:

```json
{
  "descricaoSolucao": "Foi corrigida a configuração de DNS e reiniciado o adaptador de rede.",
  "resumoIa": "Ajuste de DNS.",
  "problema": {
    "id": 1
  }
}
```

## Deletar solução

```http
DELETE /solucoes/{id}
```

---

# Chamados

## Listar chamados

```http
GET /chamados
```

## Buscar chamado por ID

```http
GET /chamados/{id}
```

## Criar chamado

```http
POST /chamados
```

Body:

```json
{
  "titulo": "Chamado de internet indisponível",
  "descricao": "Usuário informou que o computador está conectado ao Wi-Fi, mas não acessa sites.",
  "status": "ABERTO",
  "prioridade": "MEDIA",
  "tecnico": {
    "id": 1
  },
  "problema": {
    "id": 1
  }
}
```

Observações:

- `tecnico.id` deve ser o ID de um técnico já cadastrado.
- `problema.id` deve ser o ID de um problema já cadastrado.

Status sugeridos:

```txt
ABERTO
EM_ANDAMENTO
FECHADO
```

Prioridades sugeridas:

```txt
BAIXA
MEDIA
ALTA
```

## Atualizar chamado

```http
PUT /chamados/{id}
```

Body:

```json
{
  "titulo": "Chamado de internet indisponível",
  "descricao": "Usuário informou que o computador está conectado ao Wi-Fi, mas não acessa sites.",
  "status": "EM_ANDAMENTO",
  "prioridade": "MEDIA",
  "tecnico": {
    "id": 1
  },
  "problema": {
    "id": 1
  }
}
```

## Deletar chamado

```http
DELETE /chamados/{id}
```

---

# Dashboard

## Resumo geral

```http
GET /dashboard/resumo
```

Resposta esperada:

```json
{
  "totalCategorias": 1,
  "totalTecnicos": 1,
  "totalProblemas": 1,
  "totalSolucoes": 1,
  "totalChamados": 1,
  "chamadosAbertos": 1,
  "chamadosEmAndamento": 0,
  "chamadosFechados": 0
}
```

Esse endpoint pode ser usado no frontend para montar cards ou gráficos simples.

---

# IA

## Analisar problema técnico

```http
POST /ia/analisar-problema
```

Body:

```json
{
  "descricaoProblema": "O computador está conectado ao Wi-Fi, mas não consegue abrir nenhum site. Outros computadores da sala funcionam normalmente."
}
```

Resposta esperada:

```json
{
  "categoria_sugerida": "Rede",
  "prioridade_sugerida": "Média",
  "palavras_chave": ["internet", "Wi-Fi", "DNS", "navegação"],
  "resumo_problema": "Computador conectado ao Wi-Fi, mas incapaz de abrir sites, enquanto outros dispositivos na mesma rede funcionam normalmente.",
  "solucao_sugerida": "Verificar a configuração de DNS do adaptador de rede do computador. Alterar o DNS para 8.8.8.8 e 1.1.1.1, reiniciar o adaptador de rede e testar novamente o acesso à internet."
}
```

Observações:

- Esse endpoint deve ser chamado via `POST`.
- Se abrir diretamente no navegador, vai dar erro `405 Method Not Allowed`, porque o navegador usa `GET`.
- A resposta é gerada pela Gemini API.
- O técnico deve revisar a sugestão antes de aplicar ou salvar no sistema.

---

# Exemplo com JavaScript fetch

```js
async function analisarProblema() {
  const resposta = await fetch("http://localhost:8080/ia/analisar-problema", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify({
      descricaoProblema: "Computador conectado ao Wi-Fi, mas sem acesso à internet."
    })
  });

  const dados = await resposta.json();
  console.log(dados);
}
```

---

# Ordem recomendada para popular dados de teste

Para evitar erro de relacionamento, cadastre nesta ordem:

1. Categoria
2. Técnico
3. Problema
4. Solução
5. Chamado

Exemplo:

```txt
Categoria -> Técnico -> Problema -> Solução -> Chamado
```

---

# Variáveis importantes do backend

No backend, a chave da Gemini deve ser configurada como variável de ambiente:

```txt
GEMINI_API_KEY=sua_chave_aqui
```

No `application.properties`, deve ficar assim:

```properties
gemini.api.key=${GEMINI_API_KEY}
```

Nunca colocar a chave diretamente no Git.
