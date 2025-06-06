## Запуск клиента
### Собрать рантайм образ
    ./gradlew :Task_6:Client:jlink
### Запустить 
По пути из корневой папки проекта 

cd Task_6/Client/build/chat-client/bin
    
    ./chat-client - для Linux
    ./chat-client.bat - для винды

## Запуск сервера
### Собрать jar'ник
    ./gradlew :Task_6:Server:shadowjar
### Запустить
По пути из корневой папки проекта

cd Task_6/Server/build/libs

    java -jar chat-server-1.0-SNAPSHOT-all.jar
