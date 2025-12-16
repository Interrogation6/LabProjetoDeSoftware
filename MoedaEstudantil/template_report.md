# 📘 Relatório de Análise Crítica do Projeto 👨‍💻

## 1. Informações do grupo
- **🎓 Curso:** Engenharia de Software
- **📘 Disciplina:** Laboratório de Desenvolvimento de Software
- **🗓 Período:** 4° Período
- **👨‍🏫 Professor(a):** Prof. Dr. João Paulo Carneiro Aramuni
- **👥 Membros do Grupo:** Kelvyn Dantas Leal

---

## 📌 2. Identificação do Projeto
- **Nome do projeto:** Sistema de Moeda Estudantil
- **Integrantes do outro grupo:** 
- Áulus Batista
- João Gabriel
- Lucas Ferreira

- **Link do repositório:** _https://github.com/AulusHZP/LabProjetoDeSoftware/tree/main/MoedaEstudantil_  
- **Pull requests submetidos pelo seu grupo:**
  
  | 👤 Integrante | 🔧 Refatoração | 🔗 Link do PR |
  |--------------|---------------|----------------|
  | :octocat: <a href="https://github.com/Interrogation6">Kelvyn Dantas</a> | Configuração de Permissão CORS | https://github.com/AulusHZP/LabProjetoDeSoftware/pull/21 |
  | :octocat: <a href="https://github.com/Interrogation6">Kelvyn Dantas</a> | Remoção de funções não utilizadas | https://github.com/AulusHZP/LabProjetoDeSoftware/pull/23 |
  | :octocat: <a href="https://github.com/Interrogation6">Kelvyn Dantas</a> | Adicionar criptografia de senha | https://github.com/AulusHZP/LabProjetoDeSoftware/pull/22 |

## 🧱 3. Arquitetura e Tecnologias Utilizadas

O projeto utiliza uma arquitetura dividida entre backend e frontend, promovendo modularidade, separação de responsabilidades e facilidade de manutenção.

### 🏗️ Backend — Spring Boot
O backend foi desenvolvido utilizando **Spring Boot**, seguindo um padrão próximo ao **MVC**, com camadas bem definidas:

- **Controllers:** recebem requisições HTTP e encaminham para a lógica apropriada.  
- **Services:** concentram as regras de negócio e interações entre camadas.  
- **Repositories:** utilizam Spring Data JPA para acesso aos dados e persistência.  
- **Entities/Models:** representam as estruturas de dados do domínio.
- **DTOs:** representam views limitadas para serem consumidas de forma segura pelo front-end.

Tecnologias empregadas:
- Spring Boot  
- Spring Data JPA  
- PostgreSQL
- Spring Web

### 🌐 Frontend — React
O projeto complementa o backend com um frontend moderno baseado em **React.tsx**, utilizado para criar interfaces reativas e componentes reutilizáveis.

Principais características:
- Projeto Front-end compatível com telas Mobile (Responsível);
- Componentização com React.  
- Roteamento através de React-Router-DOM.
- Estilização modular através do TailWindCSS. 
- Comunicação com o backend via APIs REST.

## 🗂️ 4. Organização do GitHub e Fluxo de Trabalho Colaborativo

Avalie as práticas de Engenharia de Software Colaborativa do projeto, focando na clareza, padronização e rastreabilidade.

### 4.1. Estrutura do Repositório e Documentação
* **Estrutura de Pastas:** Segue o esperado de padrões de desenvolvimento de software, único detalhe mínimo seria que o repositório é composto de todas atividades desta disciplina no semestre, não somente o Sistema de Moeda Estudantil.
* **Documentação Essencial:** O arquivo `README.md` possui as informações mais triviais, porém falta alguns pontos que não estão condizendo com o código do projeto final, provavelmente porque estas seções foram escritas durante a etapa de planejamento, e não verificadas após o final do desenvolvimento.
* Um exemplo deste detalhe seria a especificação da utilização do BCRYPT, que apesar de estar escrito que as senhas são criptografadas, elas estão sendo manipuladas como texto simples dentro do back-end.

### 4.2. Fluxo de Trabalho (Pull Requests e Branches)
* **Branches:** O projeto utiliza, como deveria, principalmente a branch 'main'. Outras branches foram criadas para momentos específicos e tornaram-se obsoletas, mas isto não está presente em uma escala grande o suficiente para tornar-se um problema.
* **Pull Requests (PRs):** Pull Requests foram brevemente utilizados pelo time em commits grandes que precisavam de um planejamento maior, como esperado. Logo, apesar de ter alguns PRs sobre mudanças drásticas, maioria das mudanças que o time efetuou foram através de commits na main.
* Estes PRs não possuiam nomes formais e apontavam uma forte comunicação (ou necessidade de comunicação) entre o time, já que apesar de não possui descrição, o projeto não houve necessidade de desfazer trabalho devido a nomenclatura incompetente.

### 4.3. Padrões de Commits e Versionamento
* **Padrão de Commits:** Alguns integrantes seguiam um padrão estrutural de commit enquanto outros inseriam nomes breves ou anotações vagas, provavelmente porque não esperavam outras pessoas/desenvolvedores exceto o time abrir o histórico e tentar compreendê-los.
* **Versionamento (Releases/Tags):** O projeto não utiliza nenhuma forma de versionamento, exceto brevemente por baselines informais dentro da equipe, como um commit chamado 'vantagens pronto'. Onde o time terminou a funcionalidade de Vantagens e o projeto encontrava-se em um estado funcional.

---

## 🖥️ 5. Dificuldade para Configuração do Ambiente

Não houve dificuldades para a configuração do ambiente. Todas as etapas levam para o funcionamento do sistema com o cache limpo.
---

## 🔎 6. Análise de Qualidade do Código e Testes

### 6.1. Design e Princípios SOLID
* **Coesão e Acoplamento:** Todas as classes seguem os princípios SOLID. Há alguns detalhes específicos onde pode-se considerar uma forma melhor para estruturar o código.
* Este detalhe refere-se ao arquivo Codigo\star-exchange-platform-main\src\utils\heartRemover.ts, um componente visual de 'coração' que faria mais sentido residir na pasta \components.

### 6.2. Testabilidade e Cobertura
* **Presença de Testes:** O projeto não possui testes, logo não é possível avaliá-los.
---

## 🚀 7. Sugestões de Melhorias

Além dos Pull Requests mencionados, pode-se considerar a continuação do desenvolvimento de algumas partes do código. Estas partes são:

1. **Limitar acesso ADMIN ao CORS:** As origens foram configuradas corretamente, porém poderia implementar outro nível de segurança de forma que somente um ADMINISTRADOR autenticado pode efetuar chamadas no back-end relacionadas ao controle administrativo.
2. **Centralizar processo build dev:** Quando em processo de desenvolvimento, é necessário abrir 2 terminais para inicializar separadamente o front-end e o back-end. Mas tipo poderia ser centralizado através de outro comando _node_ que inicializa ambos para aumentar a conveniência do desenvolvimento.
3. **Implementar testes:** Não há nenhum tipo de testes unitários no projeto, a sua ausência pode levar a comportamentos inesperados em seções do projeto que seriam muito inconvenientes para testar (Por exemplo o Professor enviar moedas para aluno, precisaria de 1 Professor, 1 instituicao, 1 aluno e 1 curso configurado previamente para efetuar o teste manual). Se houver uma configuração para estes testes, todo este processo poderia ser automatizado.
4. **Padronização de Commits e PRs:** Apesar do grupo ser fortemente acoplado com alta comunicação, isto não torna a falta de padronização dos commits e dos pull requests aceitável. Pois virá o momento em que o projeto encontra um bug de um commit antigo que não possui nomenclatura compreensível que por consequência irá ser necessário a re-verificação das mudanças desse commit para chegar a um plano de ação. A falta de pull requests implica na necessidade do grupo desenvolver de forma individual em cada parte do código, em nenhum momento 2 integrantes irão poder desenvolver 2 funcionalidades no mesmo setor porque ambos estarão aplicando commit na branch 'main', não uma branch temporária para preparar um merge através de pull request.

---

## 🔧 8. Refatorações Propostas (3 partes do código)

### 1️⃣ Refatoração 1 – Configuração de Permissão CORS (removido CrossOrigin)

**Pull Request:** https://github.com/AulusHZP/LabProjetoDeSoftware/pull/21

**Arquivos:** `Backend\src\main\java\com\moedaestudantil\controller\XXXXController.java`

#### 🔴 Antes
```java
//imports......

@RestController
@RequestMapping("/api/company")
@CrossOrigin(origins = "*") // CrossOrigin *?
public class CompanyController {
//.....}
}
```

#### 🟢 Depois
```java
//imports......

@RestController
@RequestMapping("/api/company")
public class CompanyController {
//.....}
}
```

#### ✔ Tipo de refatoração aplicada
- **Remoção de redundância**  

#### 📝 Justificativa
CORSConfig.java não aplicava suas regras para os controllers.

---

### 2️⃣ Refatoração 2 – Remoção de funções não utilizadas

**Pull Request:** https://github.com/AulusHZP/LabProjetoDeSoftware/pull/23 

**Arquivo:** `Backend\src\main\java\com\moedaestudantil\controller\XXXXXController.java`
#### 🔴 Antes
```java
@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {

	@PatchMapping("/{id}/coins")
    public ResponseEntity<?> updateCoinBalance(@PathVariable Long id, 
                                               @RequestParam Integer amount) {
        try {
            StudentResponse response = studentService.updateCoinBalance(id, amount);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}

```

#### 🟢 Depois
```java
@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {

	/* @PatchMapping("/{id}/coins")
    public ResponseEntity<?> updateCoinBalance(@PathVariable Long id, 
                                               @RequestParam Integer amount) {
        try {
            StudentResponse response = studentService.updateCoinBalance(id, amount);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    } */
}

```

OBS: também foram removidos integrações inutilizadas dentro de `Codigo\star-exchange-platform-main\src\services\api.ts`.


#### ✔ Tipo de refatoração aplicada
- **Remover funções inutilizadas**

#### 📝 Justificativa
Minimiza código supérfluo.

---

### 3️⃣ Refatoração 3 – Adicionar criptografia de senha

**Pull Request:** https://github.com/AulusHZP/LabProjetoDeSoftware/pull/22  

**Arquivo:** `Backend\src\main\java\com\moedaestudantil\config\SecurityConfig.java`

#### 🔴 Antes
Arquivo inexistente.

#### 🟢 Depois
```java
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(@Value("${security.bcrypt.strength:8}") int bcryptStrength) {
        return new BCryptPasswordEncoder(bcryptStrength);
    }    
}
```

OBS: Todos os services que possuem manipulação com senha agora utilizam este `PasswordEncoder`.

#### ✔ Tipo de refatoração aplicada
- **Adição de API**

#### 📝 Justificativa
Seguir os requisitos propostos na documentação.

---

## 9. 📄 Conclusão

A análise do projeto Sistema de Moeda Estudantil permitiu observar, de forma prática, como o grupo avaliado estruturou sua aplicação, organizou o repositório e conduziu o desenvolvimento ao longo da disciplina. O projeto apresenta uma base bem definida, com separação entre frontend e backend, uso adequado do Spring Boot e React, além de uma arquitetura que segue padrões comuns de desenvolvimento de software.

Apesar disso, foram identificados pontos que impactam diretamente a manutenibilidade e a colaboração, principalmente no que diz respeito ao uso limitado de pull requests, à falta de padronização nos commits e à ausência de versionamento formal. Embora a comunicação interna do grupo aparentemente tenha funcionado, essas práticas dificultam a rastreabilidade das mudanças e tornam a evolução do projeto mais arriscada, especialmente em um contexto com mais desenvolvedores ou maior tempo de manutenção.

As refatorações realizadas tiveram como objetivo corrigir problemas pontuais observados durante a análise. A remoção de anotações redundantes de CORS contribuiu para uma configuração mais centralizada e segura, a exclusão de métodos e integrações não utilizados reduziu código desnecessário, e a adição de criptografia de senha com BCrypt alinhou a implementação ao que já estava descrito na documentação do projeto. Essas alterações melhoram a clareza do código e reduzem potenciais falhas futuras.

Outro ponto relevante foi a ausência de testes automatizados. A falta de testes dificulta a validação de regras de negócio mais complexas e torna o processo de verificação manual mais trabalhoso e suscetível a erros, principalmente em funcionalidades que dependem de múltiplas entidades e estados do sistema.

De forma geral, o projeto cumpre seu papel funcional, mas poderia se beneficiar significativamente da adoção de práticas mais consistentes de engenharia de software, como padronização de commits, maior uso de pull requests, implementação de testes e revisão final da documentação. A atividade de análise e refatoração reforçou a importância dessas práticas no desenvolvimento de sistemas mais organizados, seguros e fáceis de manter, além de aproximar a experiência acadêmica de um cenário real de desenvolvimento profissional.

---

## 10. 📚 Referências
- Revisando alterações em Pull Requests:  
  https://docs.github.com/pt/pull-requests/collaborating-with-pull-requests/reviewing-changes-in-pull-requests/commenting-on-a-pull-request
  
---
