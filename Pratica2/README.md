# Lab02 - MongoDB 

Aluno: Bernardo Pinto
N° Mec: 105926



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