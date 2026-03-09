FROM openjdk:17

WORKDIR /app

COPY . .

RUN javac dsa/*.java

CMD ["java", "dsa.Main"]
