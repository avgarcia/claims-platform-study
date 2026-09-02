# 0001 · Adoptar Spring Boot 4 en lugar de la versión 3 planificada

- **Estado:** aceptado
- **Fecha:** 2026-09-02
- **Contexto del plan:** Módulo 1, hito M1-H1

## Contexto y problema

El proyecto actual es un proyecto de aprendizaje, por lo que se decidió utilizar la última versión
de las tecnologías seleccionadas. En principio, cuando se tomó esa decisión se pensó que Spring
Boot 3 era la última versión disponible, pero cuando se creó el proyecto con el generador, éste
propuso Spring Boot 4 y por eso se decidió cambiar de versión. Una revisión del cambio generó dudas
de si era la versión adecuada y por qué se había realizado el cambio, así que se revisó la matriz de
compatibilidades de Spring Boot 4 y se comprobó que era compatible con Java 25. Además, a
posteriori se pudo comprobar que Spring Boot 3 no es compatible con Gradle 9.

## Opciones consideradas

1. **Spring Boot 3** — la versión que decía el plan inicial. Se pensó inicialmente que era la
   última versión disponible de Spring Boot; al comprobar que no lo era y que además no es
   compatible con la versión de Gradle seleccionada, se descartó.
2. **Spring Boot 4** — la versión propuesta por el generador del proyecto. Se seleccionó por ser la
   última versión del framework y por ser compatible con Gradle 9. Se quería la última versión para
   poder investigar y utilizar las últimas funcionalidades del framework.
3. **Bajar a Gradle 8 para poder quedarse en Spring Boot 3** — no se consideró, porque el criterio
   del proyecto era trabajar con la última versión de las tecnologías seleccionadas, y bajar de
   versión de Gradle iba en contra de ese criterio.

## Decisión

Se elige Spring Boot 4 porque es la última versión del framework y es compatible con la versión
actual de Gradle del proyecto.

## Consecuencias

### Positivas

- Última versión de un framework muy maduro, con las últimas mejoras añadidas en esta versión.

### Negativas

- No hay garantía de soporte en librerías futuras hasta comprobarlo: al elegir la versión más
  reciente del framework, ninguna librería de terceros que se añada más adelante (Debezium, clientes
  de Kafka, Testcontainers, Spring Data JPA...) tiene garantizado el soporte en el momento de
  decidir esto — hay que comprobarlo activamente cada vez que se seleccione una tecnología nueva.
- La documentación y los ejemplos disponibles son menores para una versión tan reciente, aunque la
  mayor parte del framework se mantiene respecto a versiones anteriores.
- Estar en la última versión implica asumir posibles bugs de una versión que acaba de salir.

## Qué invalidaría esta decisión

Si Debezium, los clientes de Kafka, Testcontainers, o alguna de las tecnologías que deban
seleccionarse en M1-H2 en adelante no soportan Spring Boot 4.

## Referencias

- [Spring Boot 3.5 System Requirements](https://docs.spring.io/spring-boot/3.5/system-requirements.html) — confirma que Spring Boot 3.5 solo soporta hasta Gradle 8, no Gradle 9.
- [Spring Boot System Requirements](https://docs.spring.io/spring-boot/system-requirements.html) — confirma la compatibilidad de Spring Boot 4 con Java 25 y Gradle 9.
