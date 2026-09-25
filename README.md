# SaleSys: Sistema de Vendas com Spring Boot + gRPC + JavaFX

O objetivo deste exercício é aplicar comunicação por gRPC entre um servidor Spring Boot e um cliente JavaFX, utilizando Protocol Buffers.

**Por simplificação, vamos utilizar um [Blocking Stub](https://grpc.io/docs/languages/java/basics/) diretamente na Main Thread. Isso é um anti-padrão e deve ser evitado**

**O Seu Trabalho:**
*   Implementar a funcionalidade completa para **Product** na camada de apresentação
*   Completar os métodos relacionados com Product no `SaleSysGrpcService` e no `ApiClient`
*   Completar os templates FXML relacinados ao **Product**
*   **Use as implementações de Customer e o seu uso do Sale como referência**

**Entidades do Sistema:**
1.  **Customer**: Representa um cliente do sistema.
2.  **Product**: Representa um produto disponível para venda.
3.  **Sale**: Representa uma venda realizada (Pode estar OPEN ou CLOSED).
4.  **SaleProduct**: Representa a relação entre uma venda e os produtos vendidos.

### **Contrato gRPC (`salesys.proto`):**

| RPC | Request | Response | Descrição |
|---|---|---|---|
| `CreateCustomer` | `CustomerRequest` | `CustomerResponse` | Cria um novo cliente |
| `GetAllCustomers` | `Empty` | `CustomerListResponse` | Lista todos os clientes |
| `GetProductsByVat` | `VatRequest` | `SaleProductListResponse` | Produtos da venda em aberto |
| `CreateSale` | `VatRequest` | `SaleResponse` | Cria venda para um cliente |
| `AddProduct` | — | — | **TODO – ver abaixo** |
| `?` | — | — | **TODO – outras funcionalidades necessárias** |


---

# Implementação

## O Que Está Pronto

As seguintes funcionalidades já estão parcialmente implementadas: 
1. Controllers REST 
2. Camada de Negócio
3. Camada de Dados 

Dentro dessas camadas, estão implementadas os seguintes componentes:

1. **Proto** – `salesys.proto` idêntico em ambos as aplicações, com `java_package` diferente
2. **Servidor gRPC** – `SaleSysGrpcService` implementa os quatro RPCs acima; Usa porto 9090
3. **Cliente gRPC** – `ApiClient` utiliza um `SaleSysServiceBlockingStub` para invocar o servidor
4. **Controladores JavaFX** – `AddCustomerController` e `ListCustomerController`

A sua tarefa é implementar as funcionalidades na:

1. Implementar tudo relacionado ao Produto (lógica e interface gráfica)
2. Garantir que não é possível remover ou alterar um produto de uma sale que já se encontra fechada

## O Que Deve Implementar

### `AddProduct` via gRPC

O sistema já tem `ProductService` e `ProductController` REST mas falta realizar a gestão via gRPC:

#### Passo 1 – Defina as mensagens no proto

Descomente as mensagens e adicione o RPC ao serviço em `salesys.proto`:

```proto
service SaleSysService {
  // outros rpcs
  // adicionar rpc do AddProduct
}

message ProductRequest {
  // adicionar atributos
}

message ProductResponse {
  // adicionar atributos
}
```

#### Passo 2 – Implemente o RPC no servidor (`SaleSysGrpcService`)

```java
@Override
public void addProduct(ProductRequest request,
                       StreamObserver<ProductResponse> responseObserver) {
    // complete aqui
}
```

#### Passo 3 – Implemente o método no cliente (`ApiClient`)

```java
public static ProductResponse addProduct(int code, String description,
                                          double faceValue, int stockQuantity) {
    // complete aqui
}
```

Use os RPCs de Customer já implementados como exemplo.

---

## Como Correr

### Pré-requisitos

É necessário ter o Java 21+, o Maven e o Docker + Docker compose. **O JDK precisa ser headfull**. A versão headless não tem acesso a interface gráfica. Certifique-se de que a variável de sistema `JAVA_HOME` aponta para ele.

Certifique-se de que tem o Docker Engine a correr na sua máquina. Depois, dentro da pasta do projeto, execute o seguinte comando no terminal:

```bash
cd SaleSys
docker-compose up --build
```

Depois deve executar o JavaFX. **Corre nativamente e não em container**.

```bash
cd JavaFX
mvn javafx:run
```

## Visualizar a base de dados

Use uma ferramenta com interface gráfica, como o **DBeaver**, para inspecionar a base de dados.

### Detalhes da ligação:

*   **Host:** `localhost`
*   **Porta:** `5432`
*   **Base de Dados:** `postgres`
*   **Utilizador:** `user`
*   **Palavra-passe:** `password`


## Extra: 

Remova o anti-padrão do Blocking Stub e substituia por algo melhor (e.g., Threads geridas manualmente). 