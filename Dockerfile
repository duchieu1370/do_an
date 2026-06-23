# Bước 1: Dùng Maven để build code thành file .jar
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Bước 2: Tạo môi trường chạy Java siêu nhẹ
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy file jar đã build từ Bước 1 sang
COPY --from=build /app/target/*.jar app.jar

# Copy thư mục wallet ra một đường dẫn vật lý cố định trong server
COPY src/main/resources/wallet /app/wallet

# Khởi chạy ứng dụng
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]