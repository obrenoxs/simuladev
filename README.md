# SimulaDev

> Simulador do dia a dia de um desenvolvedor de software — tarefas, projetos e um "emprego" fictício, personalizados pela sua stack, nível e área de interesse.

**Status do projeto:** 🚧 Em desenvolvimento (pré-MVP) — construção do backend (V1)

---

## 📖 Sobre o projeto

O SimulaDev é um simulador onde o usuário "trabalha" como desenvolvedor, recebendo tarefas de um gerente fictício dentro de uma empresa também fictícia. Mas nada disso é genérico: a stack, o nível de habilidade e o domínio da empresa são moldados pelo perfil que o próprio usuário escolhe.

O projeto nasce de uma lacuna real no mercado de portfólio para devs: falta uma forma de treinar com tarefas que sejam realistas e coerentes com o cotidiano de uma empresa de verdade (não exercícios soltos), personalizadas pela stack e nível de quem está estudando, e que deem a sensação de estar efetivamente empregado e progredindo — não apenas fazendo mais um curso.

## ⚙️ Como funciona

- **Motor de decisão de tarefas** — um algoritmo em Java decide a próxima tarefa considerando o perfil do usuário, o histórico recente e os conceitos técnicos ainda não exercitados. A IA entra só na etapa final, redigindo o ticket em linguagem natural, no tom de um gerente.
- **Grade de conceitos** — cada stack/nível tem uma checklist de conceitos técnicos que o motor tenta cobrir ao longo das tarefas antes de liberar a transição de nível.
- **Empresas fictícias e múltiplos vínculos** — o usuário escolhe entre domínios de empresa pré-definidos (banco, startup, prestadora de serviço) e pode manter vários vínculos simultâneos ativos, como um freelancer alternando entre contextos.
- **Nível agregado** — combina a cobertura da grade de conceitos com o volume de tarefas entregues.

## 🧱 Escopo do MVP (V1)

- Stack simulada travada em **Java + Spring**.
- Interação inicial: apenas o gerente/superior atribuindo tarefas.
- Cadência **pull** — o usuário pede a próxima tarefa quando quiser.
- Aceite de entrega manual (o usuário marca como concluído).
- Catálogo de empresas pré-definido e curado.

## 🛠️ Stack tecnológica

- **Java 25** + **Spring Boot**
- **Spring Data JPA** + **Hibernate**
- **Spring Security** + **JWT** (autenticação própria via `OAuth2 Resource Server`)
- **PostgreSQL** + **Flyway** (versionamento de schema)
- **SpringDoc OpenAPI** (documentação da API / Swagger)
- **Bean Validation** (Hibernate Validator)
- **JUnit** + **Mockito** (testes automatizados)
- **Maven**
- **Docker**

## 🗺️ Roadmap

- [ ] Setup inicial do projeto e configuração de banco/migrations
- [ ] Autenticação (Access Token) e cadastro de usuário
- [ ] Modelagem das entidades principais (`Usuario`, `EmpresaTipo`, `VinculoEmpresa`, `EstadoProjeto`, `Conceito`, `Tarefa`)
- [ ] Motor de regras de priorização de tarefas
- [ ] Integração com provedor de IA para redação dos tickets
- [ ] Refresh Token
- [ ] Frontend (após o backend da V1 estar concluído)

## 👤 Autor

Desenvolvido por **Breno Oliveira de Souza**, como projeto de estudo e portfólio no processo de conquista da primeira vaga como desenvolvedor.

- GitHub: [@obrenoxs](https://github.com/obrenoxs)
- LinkedIn: [linkedin.com/in/breno-oliveira-souza](https://linkedin.com/in/breno-oliveira-souza)
