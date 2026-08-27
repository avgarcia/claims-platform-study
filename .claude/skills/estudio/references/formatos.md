# Plantillas

Todas en español salvo el código y los mensajes de commit, que van en inglés.

---

## `ESTUDIO.md` — estado del plan

Vive en la raíz del monorepo. Se actualiza al cerrar cada sesión. Es corto a propósito: si crece
más de una pantalla, es que está acumulando historia que ya cuenta el git log.

```markdown
# Estado del plan de estudio

> Última sesión: AAAA-MM-DD · Módulo N, semana N de 48

## Ahora

**Módulo:** 1 — Fundamentos formales y plataforma de datos
**Hito abierto:** M1-H2 — CDC con Debezium publicando a Kafka
**Falta para cerrarlo:**
- [ ] Medición: throughput sostenido del conector, guardada en `evidencias/M1-H2/`
- [ ] Fallo provocado: parar Postgres a media carga y medir el hueco de replicación

## Siguiente tarea

Configurar el conector de Debezium contra la tabla de outbox y verificar el orden de
los eventos por clave de agregado.

## Hitos cerrados

| Hito | Fecha | Medición | Fallo provocado |
|---|---|---|---|
| M1-H1 · Outbox transaccional | 2026-09-12 | 1.850 ev/s, p99 22 ms | Kill del publicador a media transacción → sin pérdida, 3 duplicados absorbidos |

## Deuda del plan

Temas tocados por encima que hay que revisitar en el mes 12.

- Watermarks en Kafka Streams: entendido a nivel de API, no de garantías.

## Bitácora

Una línea por sesión. Sin adornos.

- 2026-09-12 · 4 h · Outbox cerrado. Escribí yo el publicador; Claude el docker-compose.
```

---

## ADR — formato MADR

En `docs/adr/NNNN-titulo-en-kebab-case.md`. Numeración correlativa de cuatro dígitos.

```markdown
# NNNN · Título en una frase que diga la decisión, no el tema

- **Estado:** propuesto | aceptado | sustituido por [NNNN](NNNN-....md) | obsoleto
- **Fecha:** AAAA-MM-DD
- **Contexto del plan:** Módulo N, hito MN-HN

## Contexto y problema

Qué situación obliga a decidir. Qué restricciones existen. Qué se sabía y qué no se sabía
en este momento — esto es lo que hace valioso un ADR escrito a tiempo.

## Opciones consideradas

1. **Opción A** — descripción en una o dos frases.
2. **Opción B** — ídem.
3. **No hacer nada** — casi siempre es una opción real y casi nunca se escribe.

## Decisión

Se elige **X**, porque ...

## Consecuencias

### Positivas
- ...

### Negativas
- ...

> **Esta sección no puede estar vacía ni ser cosmética.** Si no hay ninguna consecuencia
> negativa, o no se ha entendido la decisión o no había decisión que tomar. Un ADR sin
> trade-offs incómodos es documentación decorativa y hay que reescribirlo.

## Qué invalidaría esta decisión

Qué tendría que cambiar en el sistema o en el contexto para volver aquí y revisarla.

## Referencias

- Enlaces a la medición, al fallo provocado o a la fuente que sustenta la decisión.
```

---

## `fallo.md` — experimento de caos

En `evidencias/<id-hito>/fallo.md`. **La hipótesis se escribe antes de ejecutar nada.**

```markdown
# Fallo provocado — <qué se rompe>

- **Hito:** MN-HN
- **Fecha:** AAAA-MM-DD
- **Componente objetivo:** ...
- **Cómo se provoca:** comando o procedimiento exacto, reproducible.

## Hipótesis (escrita ANTES de ejecutar)

Qué se espera que pase, con números cuando se pueda: cuánto sube la latencia, cuántos
mensajes se pierden o duplican, cuánto tarda en recuperarse, qué ve el usuario.

## Resultado real

Qué pasó de verdad. Datos, no impresiones. Adjuntar gráfica o salida.

## Diferencia

En qué se falló la predicción y por qué.

> Los experimentos donde la hipótesis se equivoca son los valiosos. **No los maquilles ni
> reescribas la hipótesis a posteriori.** Si acertaste en todo, probablemente el experimento
> era demasiado fácil y conviene subir la apuesta.

## Qué se cambió a raíz de esto

Cambio de código, de configuración, de diseño — o nada, justificado.
```

---

## Medición

En `evidencias/<id-hito>/README.md`.

```markdown
# Medición — MN-HN

- **Qué se mide y por qué importa:** ...
- **Cómo reproducirlo:** `comando exacto`
- **Entorno:** versiones, recursos, tamaño del dataset.

## Resultados

| Métrica | Valor |
|---|---|
| Throughput sostenido | ... |
| Latencia p50 / p95 / p99 | ... |

## Interpretación

Qué dice este número. Dónde está el cuello de botella. Qué se probó y no funcionó.
```

---

## Nota para la base de conocimiento

Ruta: `C:\Users\avidal\Documents\Claude\Projects\Ingeniería del Software\notes\`

**Antes de crear una nota nueva, obligatorio:** listar `notes/`, leer el frontmatter (`title`,
`tags`) de las existentes y comparar. Si hay solape → **enriquecer la existente**, no duplicar.

```markdown
---
title: "Nombre del concepto"
tags: [arquitectura, java, devops, testing, patrones, ...]
first_captured: AAAA-MM-DD
last_updated: AAAA-MM-DD
---

## Idea central

Una o dos frases con la esencia del concepto.

## Evidencia y matices

Ejemplos concretos, casos de uso, benchmarks, datos. Sin obviedades.
Cuando la nota nazca de una sesión de estudio, **enlaza la medición o el fallo provocado**:
es la diferencia entre una nota leída y una nota vivida.

## Implicaciones / cómo aplicarlo

Trade-offs de diseño. Cuándo usar este enfoque frente a otros. Decisiones concretas.

## Notas relacionadas

- [[nombre-de-otra-nota]] — por qué se relaciona

## Referencias

- [Fuente](URL) — descripción breve
```

Además hay que **proponer** qué `[[wikilinks]]` añadir en las notas existentes relacionadas.
Antonio decide cuáles se aplican; no se tocan las notas existentes por iniciativa propia.
Y añadir la entrada correspondiente en `INDEX.md` bajo su categoría.

---

## Commits

En inglés, formato convencional. Un commit por unidad de trabajo con sentido.

```
feat(claims-core): add transactional outbox publisher
test(claims-core): property-based test for claim aggregate invariants
docs(adr): 0004 choose Debezium over polling for CDC
chore(ci): add ArchUnit fitness functions to pipeline
```
