# Estado del plan de estudio

> Última sesión: 2026-09-02 · Módulo 1, semana 1 de 48

## Ahora

**Módulo:** 1 — Fundamentos formales y plataforma de datos distribuida
**Hito abierto:** ninguno.
**Falta para cerrarlo:** N/A.

**Estado del repositorio ahora mismo:** `main` al día, pusheado a GitHub. `docs/adr/0001-adoptar-spring-boot-4.md`
comiteado. Working tree limpio.

## Siguiente tarea

1. ADR `0002` — estructura de paquetes DDD táctico + hexagonal (feature-first, Command/Query en los
   puertos de entrada, entity/mapper separados del dominio) — quedó pendiente hoy, sin posponerse
   una tercera vez la próxima sesión.
2. Empezar M1-H2 — Outbox transaccional con Debezium publicando a Kafka (entregable 2 del módulo).

## Hitos cerrados

| Hito | Fecha | Medición | Fallo provocado |
|---|---|---|---|
| M1-H1 · Andamiaje hexagonal de `claims-core` | 2026-09-01 | Arranque en frío: media 3.76 s (mín 3.13, máx 4.56, 5 runs) — `evidencias/M1-H1/README.md` | `@Component` de Spring colado en `ClaimStatus` (dominio) → `no_framework_in_domain` lo detecta correctamente, hipótesis escrita antes acertó — `evidencias/M1-H1/fallo.md` |

## Deuda del plan

*(ninguna todavía)*

## Bitácora

- 2026-08-27 · Bootstrap del monorepo: estructura de carpetas, git init, remoto público en GitHub (`avgarcia/claims-platform-study`).
- 2026-08-27 · 1 h · Teoría: taxonomía de consistencia (linealizabilidad, causal, read-your-writes, lecturas monótonas, prefijo consistente) con ejemplos de siniestros. Profundizado el mecanismo de read-your-writes vía token atado a identidad frente a sticky routing. Sin código tocado. Nota nueva en la KB: `consistencia-modelos-sistemas-distribuidos`.
- 2026-08-28 · ~4 h (de 12:44 a bien pasadas las 15h que se habían marcado) · Repaso de ArchUnit (teoría corta). Andamiaje hexagonal + DDD táctico de `claims-core` escrito por Antonio: Spring Boot 4 (decisión informada tras comprobar compatibilidad, diverge del plan escrito que decía Boot 3), Java 25, estructura por feature con capas hexagonales dentro, puertos de entrada divididos en command/query, un caso de uso por clase, entity/mapper separados del dominio. Regla de capas de ArchUnit con un bug real de dirección en la negación, detectado y corregido por Antonio tras revisión con preguntas; validado provocando la violación a propósito. Package-info con Javadoc + JSpecify (`@NullMarked`) escritos por Claude a petición explícita de Antonio, reconfirmando "quién escribe" para esa pieza. Añadidas 6 reglas de ArchUnit más (Antonio) y una regla de ubicación de implementaciones de puertos (Claude, a petición explícita) — esta última se perdió dos veces por guardados concurrentes con el IDE y quedó fuera al cerrar. Trabajo comiteado en 4 commits estructurados en la rama `feature/claims-core-hexagonal-scaffold` (sin mergear). Sesión cerrada con `Claim`/`ClaimStatus` a medio construir, sin comitear — tarea registrada como abierta, no como terminada. ADR de las decisiones de hoy pospuesto a la próxima sesión.
- 2026-09-01 · ~4,5 h (14:40–19:19) · Cierre de M1-H1. `Claim`/`ClaimStatus` terminados, `SubmitClaim` y `UserClaimQuery` implementados de extremo a extremo por Antonio con repositorio en memoria — varias rondas de revisión con preguntas encontraron bugs reales (overwrite por `policyId` en vez de `claim.id()`, lista con `null` en vez de vacía cuando no hay resultados, invariante de `Claim.changeStatus` con la negación invertida) y decisiones de diseño discutidas a fondo (`Clock` inyectado vs. llamado a pelo, campo `static` vs. bean singleton) — Antonio distinguió correctamente qué cambios arreglaban algo roto hoy frente a cuáles invertían en verificabilidad futura, concepto que generó nota nueva en la KB. Medición (arranque en frío, 5 runs, media 3.76 s) y fallo provocado (violación de `no_framework_in_domain` con hipótesis previa, acertada) ejecutados por Claude a petición explícita; interpretación de la medición escrita por Antonio y discutida antes de fijarla. M1-H1 cerrado. ADR pospuesto una segunda vez — para mañana sin falta.
- 2026-09-02 · ~8,5 h (8:46–17:21) · ADR `0001` (Spring Boot 4 sobre la 3 planificada), primer ADR de Antonio en este plan. Escrito por Claude a petición explícita ("para tener una primera referencia"), tras una fase larga de reconstrucción honesta del contexto real en el chat: la primera versión que propuso Antonio atribuía el cambio a una incompatibilidad técnica con Gradle 9 conocida de antemano, que resultó ser una reconstrucción más limpia de lo que pasó — el motivo real fue el default del generador de IntelliJ, verificado después al ser cuestionado. Interrogado tras escribirlo: Antonio defendió con criterio sólido tanto dejar abierta la respuesta a una futura incompatibilidad (no decidir sin datos) como mantener la opción de bajar a Gradle 8 descartada pese a las negativas ya documentadas (trade-off relativo al contexto de un proyecto de aprendizaje, no universal). ADR `0002` (estructura de paquetes) queda pendiente — no se pospone una tercera vez. Sin código tocado. Sin nota nueva de KB.
