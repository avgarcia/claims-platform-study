# Medición — M1-H1

- **Qué se mide y por qué importa:** tiempo de arranque en frío de `claims-core` (desde `java -jar`
  hasta la línea `Started ClaimsCoreApplication`), con solo el andamiaje hexagonal y
  `spring-boot-starter-actuator` — sin persistencia ni mensajería todavía. Sirve como línea base para
  comparar, en los próximos hitos, cuánto añade cada pieza de infraestructura real (Postgres,
  Debezium, Kafka) al arranque.
- **Cómo reproducirlo:**
  ```bash
  ./gradlew bootJar
  java -jar build/libs/claims-core-0.0.1-SNAPSHOT.jar --server.port=0
  ```
  (leer la línea `Started ClaimsCoreApplication in X.XXX seconds` del log de arranque; `--server.port=0`
  evita choques de puerto entre ejecuciones repetidas)
- **Entorno:** OpenJDK 25.0.3 LTS (build Microsoft), Spring Boot 4.1.1, Windows 11. Jar empaquetado
  con `bootJar`, ejecutado 5 veces en frío (proceso nuevo cada vez, sin JIT calentado de una ejecución
  anterior).

## Resultados

| Run | Tiempo de arranque |
|---|---|
| 1 | 3.915 s |
| 2 | 3.765 s |
| 3 | 3.133 s |
| 4 | 4.560 s |
| 5 | 3.428 s |

Media ≈ 3.76 s · mín 3.13 s · máx 4.56 s

## Interpretación

3.76 s de media no es un número atípico para una aplicación JVM sin AOT ni compilación nativa que
incluye `spring-boot-starter-actuator` — el propio actuator obliga a evaluar bastante
auto-configuración (health, metrics, endpoints) antes de estar listo. Además, la línea de log que se
mide mezcla dos cosas que no se pueden separar con este dato: el arranque en frío de la propia JVM
(carga de clases, sin el JIT calentado todavía) y el refresh del contexto de Spring en sí. No hay
forma de saber con esta medición sola qué parte del tiempo corresponde a cada una.

Dicho esto, para Antonio este tiempo **sí es lento**, en términos subjetivos: la expectativa es un
arranque casi inmediato, sin esperar varios segundos, para una aplicación que hoy no hace nada más
que exponer el andamiaje. No hay un baseline formal de comparación (un SLA, otro framework, un
entorno de despliegue concreto) — es una valoración basada en experiencia previa, no en un número de
referencia externo, y se registra como tal.

Lo que sí es una predicción con fundamento: el tiempo va a subir según se añadan dependencias reales,
especialmente JPA/Hibernate (más que Kafka, cuyo coste de arranque suele ser menor). Queda pendiente
de contrastar con datos, no solo predecir: **repetir esta misma medición en M1-H2** (Postgres +
Debezium) y comparar el delta real frente a la línea base de hoy.
