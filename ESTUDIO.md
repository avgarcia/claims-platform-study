# Estado del plan de estudio

> Última sesión: 2026-08-28 · Módulo 1, semana 1 de 48

## Ahora

**Módulo:** 1 — Fundamentos formales y plataforma de datos distribuida
**Hito abierto (propuesto, a confirmar):** M1-H1 — Andamiaje hexagonal de `claims-core` con límites verificados por ArchUnit
**Falta para cerrarlo:**
- [ ] Contenido real del agregado `Claim` — en curso: `ClaimStatus` añadido, `Claim.java` en edición activa a media sesión (cambios sin comitear en la rama).
- [ ] Un caso de uso completo de extremo a extremo — `SubmitClaim` (comando: alta de un siniestro nuevo, invariante: solo transiciona a `APPROVED`/`REJECTED` desde `SUBMITTED`) y la consulta `UserClaimQuery` (siniestros de un usuario por `policyId`) con su implementación real.
- [ ] Las reglas de ArchUnit de "quien implementa un puerto vive donde toca" (in→usecase, out→infrastructure) reincorporadas — se escribieron y se perdieron dos veces por guardados concurrentes durante la sesión; están fuera del fichero ahora mismo tras la limpieza de última hora.
- [ ] Medición y fallo provocado — pendiente de decidir qué tiene sentido medir en un hito de andamiaje puro; a discutir la próxima sesión.

**Estado del repositorio ahora mismo:** rama `feature/claims-core-hexagonal-scaffold`, 4 commits limpios y comprobados en verde (proyecto Spring Boot 4 + Java 25, esqueleto hexagonal, package-info con JSpecify, regla de capas de ArchUnit). Por encima de esos 4 commits hay cambios sin comitear: se borraron `UserClaimQuery`, `ClaimRepository`, `UserClaim` y `GenericClaimRepository` para reescribirlos con contenido real, y `Claim.java`/`ClaimStatus.java` están a medio construir. **Sin mergear a `main` todavía** — no tiene sentido mergear con el agregado a medias.

## Siguiente tarea

Terminar `Claim` + `ClaimStatus`, implementar `SubmitClaim` (comando) y `UserClaimQuery` (consulta) de
extremo a extremo con un repositorio en memoria, y recuperar las dos reglas de ArchUnit de ubicación
de adaptadores. Cuando esté verde, decidir medición + fallo provocado para cerrar M1-H1.

## Hitos cerrados

| Hito | Fecha | Medición | Fallo provocado |
|---|---|---|---|

*(ninguno todavía)*

## Deuda del plan

*(ninguna todavía)*

## Bitácora

- 2026-08-27 · Bootstrap del monorepo: estructura de carpetas, git init, remoto público en GitHub (`avgarcia/claims-platform-study`).
- 2026-08-27 · 1 h · Teoría: taxonomía de consistencia (linealizabilidad, causal, read-your-writes, lecturas monótonas, prefijo consistente) con ejemplos de siniestros. Profundizado el mecanismo de read-your-writes vía token atado a identidad frente a sticky routing. Sin código tocado. Nota nueva en la KB: `consistencia-modelos-sistemas-distribuidos`.
- 2026-08-28 · ~4 h (de 12:44 a bien pasadas las 15h que se habían marcado) · Repaso de ArchUnit (teoría corta). Andamiaje hexagonal + DDD táctico de `claims-core` escrito por Antonio: Spring Boot 4 (decisión informada tras comprobar compatibilidad, diverge del plan escrito que decía Boot 3), Java 25, estructura por feature con capas hexagonales dentro, puertos de entrada divididos en command/query, un caso de uso por clase, entity/mapper separados del dominio. Regla de capas de ArchUnit con un bug real de dirección en la negación, detectado y corregido por Antonio tras revisión con preguntas; validado provocando la violación a propósito. Package-info con Javadoc + JSpecify (`@NullMarked`) escritos por Claude a petición explícita de Antonio, reconfirmando "quién escribe" para esa pieza. Añadidas 6 reglas de ArchUnit más (Antonio) y una regla de ubicación de implementaciones de puertos (Claude, a petición explícita) — esta última se perdió dos veces por guardados concurrentes con el IDE y quedó fuera al cerrar. Trabajo comiteado en 4 commits estructurados en la rama `feature/claims-core-hexagonal-scaffold` (sin mergear). Sesión cerrada con `Claim`/`ClaimStatus` a medio construir, sin comitear — tarea registrada como abierta, no como terminada. ADR de las decisiones de hoy pospuesto a la próxima sesión.
