# Lab02 - MongoDB 

Aluno: Bernardo Pinto

NMec: 105926



## 2.1 MongoDB – Instalação e exploração por linha de comandos
### Principais comandos:

Para efetuar a conexão com o Mongo através do terminal linux foram utilizados os seguintes comandos:

```bash
sudo service mongod start
mongosh
```

Para criar uma base de dados foi utilizado o comando:

```shell
test> use minha_bd
```

Para inserir documentos:

```shell
minha_bd> db.colecao.insertOne({nome: "ex", idade: 100})
```

Para consultar:

```shell
minha_bd> db.colecao.find({nome: "ex"})
```

Para remover:

```shell
minha_bd> db.colecao.deleteOne({nome: "ex"})
```

Consultar todas as bases de dados:

```shell
minha_bd> show dbs
```

Apagar base de dados atual:
    
```shell
minha_bd> db.dropDatabase()
```

## 2.2 MongoDB – Construção de queries

### Notas relevantes:

- No final de uma query posso selecionar os campos que desejo que sejam apresentados, por exemplo:

```shell
db.colecao.find({nome: "ex"}, {nome: 1, idade: 1}) # 1 para apresentar, 0 para não apresentar
```

- Para ordenar de forma crescente ou decrescente a partir de um campo específico:

```shell
db.colecao.find().sort({nome: 1}) # 1 para crescente, -1 para decrescente
```

- Tags de comparação:

```shell
db.colecao.find({idade: {$gt: 20}}) # $gt: maior que
db.colecao.find({idade: {$gte: 20}}) # $gte: maior ou igual que
db.colecao.find({idade: {$lt: 20}}) # $lt: menor que
db.colecao.find({idade: {$lte: 20}}) # $lte: menor ou igual que
```

- A "profundidade" dos campos segue a mesma logica que em ficheros JSON, por exemplo:

```shell
adress.coord # para aceder ao campo cidade dentro do campo endereço
```

- O acúmulo de condições segue a mesma lógica de ANDs, por exemplo:

```shell
db.colecao.find({idade: {$gt: 20}, nome: "ex"}) # idade maior que 20 E nome igual a "ex"
``` 

- Posso utilizar o operador OR passando um conjunto das possibilidades para um MESMO CAMPO e utilizar a tag $in

```shell
db.restaurants.find({ localidade: "Bronx", gastronomia: {$in: ["American","Chinese"]}}) # localidade igual a "Bronx" E gastronomia igual a "American" OU "Chinese"
```

- Para filtrar nomes iniciados ou terminados por ...   
    
```shell
db.colecao.find({nome: /^ex/}) # nome iniciado por "ex"
db.colecao.find({nome: /ex$/}) # nome terminado por "ex"
``` 

- Agregação no MongoDB usando `.aggregate()`

```shell
db.colecao.aggregate([
  { $match: { campo: valor } },     # Estágio 1: Filtragem
  { $group: { _id: "$campo", ... } } # Estágio 2: Agrupamento
])
```


## 2.3 MongoDB – Driver (Java)

Primeiramente foi criado o projeto Maven e inseridas as dependências do Mongo no ficheiro `pom.xml`.

```xml
<dependency>
 <groupId>org.mongodb</groupId>
 <artifactId>mongodb-driver-sync</artifactId>
 <version>4.11.0</version>
</dependency>
```

### Conexão à base de dados:

```java
MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
MongoDatabase database = mongoClient.getDatabase("cbd"); // cbd é o nome da base de dados
MongoCollection<Document> db = database.getCollection("restaurants"); // restaurants é o nome da coleção
```

### CRUD:

Ao utilizar os drivers do Mongo para Java sempre utiliza-se o objeto `Document` para representar os documentos da base de dados.

- Por exemplo para inserir um documento na base de dados:
```java
Document document = new Document("nome", nome)
                .append("building", building)
                .append("rua", rua)
                .append("zipcode", zipcode)
                .append("localidade", localidade)
                .append("gastronomia", gastronomia);

        db.insertOne(document);
```

- Para consultar:
```java
Document search = new Document(campo, valor);

                    for (Document resultado : db.find(search)) {
                        System.out.println(resultado.toJson());
                    }
```
