FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

RUN javac dsa/*.java

CMD ["java", "dsa.Main"]
