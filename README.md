# Ingeniería de Software - Tarea 8

Este es un monorepo con 2 microservicios: **restaurant** y **rewards**. Ambos microservicios están implementados con Spring Boot, JOOQ, y se comunican mediante una cola de RabbitMQ.

## Uso

Las 2 instancias de PostgreSQL requeridas se pueden levantar con Docker Compose:

```bash
docker compose up -d
```

Luego de configurar archivos `.env` tanto en `restaurant` como en `rewards` (según los `.env.example`), se puede levantar cada servicio con Gradle de la siguiente forma:

```bash
./gradlew clean bootRun
```

Esto levantará `restaurant` en el puerto 8080 y `rewards` en el puerto 8081.
