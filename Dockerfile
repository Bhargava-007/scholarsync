FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

RUN find dsa -name "*.java" > sources.txt && javac -d . @sources.txt

CMD ["java", "Main"]
