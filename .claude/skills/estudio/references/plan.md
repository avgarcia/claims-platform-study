# Plan de estudio — 12 meses, ~430 h, 8–10 h/semana

Reparto aproximado: 40% teoría, 60% práctica. Cuatro semanas de holgura ya descontadas
(una por trimestre). Si se gastan, se gastan; si no, se convierten en profundización.

## Forma de la semana

| Momento | Horas | Contenido |
|---|---|---|
| Martes y jueves noche | 3 h | Teoría, lectura y vídeo. Cabeza cansada, contenido pasivo. |
| Sábado mañana | 4–5 h | Bloque hands-on. El único con contexto continuo para construir. |
| Domingo | 1 h | Módulo 4: ADR de lo decidido, actualizar C4, cerrar registro. |
| Hueco suelto | ~1 h | Notas a la base de conocimiento con su formato habitual. |

---

## Módulo 1 — Fundamentos formales y plataforma de datos distribuida
**Meses 1–3 · 100 h (40 teoría / 60 práctica) · repo `claims-core`**

Cierra la distancia entre aplicar patrones y razonar desde primeros principios, y sustituye
el stack de datos de la era Hadoop.

### Conceptos
- **Taxonomía de consistencia** — linealizabilidad, causal, read-your-writes, lecturas monótonas,
  prefijo consistente. Cinco garantías con cinco costes, no una categoría.
- **Consenso** — Raft completo: elección de líder, replicación de log, seguridad, cambios de
  membresía. Qué pasa en una partición de red asimétrica.
- **Transacciones distribuidas** — por qué 2PC falla (bloqueo ante caída del coordinador), saga
  orquestada frente a coreografiada, TCC, y cuándo la respuesta es rediseñar para no necesitarlas.
- **Effectively-once** — exactly-once no existe. Claves de idempotencia, ventanas de deduplicación,
  fencing tokens, transacciones de Kafka y qué garantizan exactamente.
- **Tiempo y orden** — relojes lógicos, Lamport, vector clocks, hybrid logical clocks, deriva y
  salto de reloj. Por qué el timestamp de pared no ordena eventos entre máquinas.
- **Particionado** — hashing consistente, particiones calientes, la clave de partición como
  decisión prácticamente irreversible.
- **CDC y contratos de datos** — Debezium y el log de la base como fuente de verdad, schema
  registry, evolución Avro/Protobuf, compatibilidad backward, forward y full transitive.
- **Streams con estado** — Kafka Streams frente a Flink, ventanas, watermarks, eventos tardíos,
  state stores y su recuperación tras fallo.
- **Lakehouse** — Iceberg: snapshots, time travel, evolución de esquema y de partición.
- **Verificación** — testing basado en propiedades sobre invariantes, simulación determinista,
  cómo se lee un informe de Jepsen.

### Entregables de `claims-core`
1. Java 25 y Spring Boot 3, hexagonal, con los límites verificados por tests y no por disciplina.
2. PostgreSQL con Outbox transaccional y Debezium publicando CDC a Kafka.
3. Schema Registry con Protobuf y **una migración incompatible ejecutada con consumidores vivos**,
   documentada paso a paso. Este ejercicio enseña más que tres capítulos de libro.
4. Job de Kafka Streams con estado: agregados por región y severidad, incumplimiento de SLA,
   ventanas y tratamiento explícito de eventos tardíos.
5. Sink a Iceberg sobre MinIO, con una consulta de time travel que demuestre para qué sirve.
6. Testcontainers, tests de propiedades sobre los invariantes del agregado de siniestro, e inyección
   sistemática de duplicados y desorden que **demuestre** la idempotencia.
7. Benchmark reproducible con un comando, con gráficas de throughput y percentiles.

### Cómo saber que se domina
- Explicar sin mirar qué garantía se pierde al añadir una réplica de lectura y qué vería el usuario.
- Justificar la clave de partición y describir el escenario que la volvería caliente.
- Tener la gráfica de qué le pasa al p99 cuando cae un consumidor y se rebalancea el grupo.
- Describir la migración incompatible como una secuencia de pasos reversibles.

### Recursos
- **DDIA** (Kleppmann) — capítulos 5, 7, 8, 9 y 11. No releer entero.
- **Database Internals** (Petrov) — parte II completa.
- **Distributed Systems** — 8 clases de Kleppmann (Cambridge), gratis en YouTube.
- **jepsen.io** — tres informes completos: Kafka, PostgreSQL, MongoDB.
- **jack-vanlightly.com** — interioridades de Kafka, replicación y streaming.
- **brooker.co.za/blog** — Marc Brooker, trade-offs de sistemas distribuidos.
- **Designing Event-Driven Systems** (Stopford) — gratis desde Confluent.
- Especificación de formato de tabla de **Apache Iceberg** — corta, léela directa.

---

## Módulo 2 — Cloud, plataforma, resiliencia y cadena de suministro
**Meses 4–7 · 150 h (50 teoría / 100 práctica) · repo `claims-platform`**

La brecha más profunda y el único dominio donde se parte casi de cero.

### Conceptos
- **Kubernetes de verdad** — planificador, requests/limits y clases QoS, HPA/VPA/KEDA,
  PodDisruptionBudgets durante un drenado, topology spread, y **operadores y CRDs**.
- **Red y aislamiento** — VPC, subredes públicas y privadas, endpoints de servicio,
  NetworkPolicies, y cómo la topología de red condiciona la de servicios.
- **Identidad y permisos** — IAM en profundidad: lógica de evaluación de políticas, basadas en
  identidad frente a recurso, roles asumidos, federación de cargas. El sistema más subestimado.
- **Servicios gestionados y su precio arquitectónico** — EKS, RDS, MSK, S3, SQS/SNS/EventBridge,
  Lambda y arranque en frío, DynamoDB con single-table design.
- **IaC** — Terraform: módulos, estado y bloqueo, drift, workspaces. OPA/Conftest en CI.
- **GitOps y entrega progresiva** — ArgoCD, Argo Rollouts o Flagger, canary con análisis automático,
  compatibilidad N/N+1, separación de deploy y release con feature flags.
- **Resiliencia a escala** — celdas y blast radius, shuffle sharding, load shedding, concurrencia
  adaptativa, backpressure, presupuestos de timeout propagados, tormentas de reintentos y jitter,
  degradación funcional planificada.
- **SRE cuantitativo** — SLIs de experiencia de usuario, SLOs calculados, error budget como criterio
  de decisión, alertas por burn rate multiventana.
- **Observabilidad avanzada** — OTEL extremo a extremo, wide events, exemplars, perfilado continuo
  con Pyroscope, nociones de eBPF.
- **Cadena de suministro** — SBOM con Syft/CycloneDX y su uso real ante un CVE, firma con
  Sigstore/cosign, niveles SLSA, admisión con Kyverno, secretos con Vault o External Secrets,
  SAST y SCA en pipeline, modelado STRIDE, mTLS y service mesh.
- **Coste como diseño** — economía unitaria, coste por transacción, right-sizing, spot,
  Infracost en el PR, Kubecost en el clúster.
- **Arquitectura bajo regulación** — DORA (UE 2022/2554): pruebas de resiliencia operativa,
  registro de proveedores TIC críticos, gestión de incidentes.

### Entregables de `claims-platform`
1. Módulos Terraform propios (no copiados) para VPC, EKS, RDS y MSK, con estado remoto, bloqueo
   y políticas OPA rechazando configuraciones inseguras en CI.
2. **Un operador Kubernetes propio con CRD** — por ejemplo `ClaimsPipeline`, que provisione topics,
   consumidores y configuración asociada. El ejercicio más formativo del módulo.
3. GitOps con ArgoCD y canary con Argo Rollouts gobernado por análisis automático de SLOs, con
   rollback sin intervención humana demostrado.
4. OTEL completo con exemplars enlazando la métrica de latencia con la traza que la causó.
5. SLIs y SLOs por servicio, error budget calculado, alertas por burn rate y **política escrita**
   de qué pasa cuando se agota.
6. **Experimentos de caos con hipótesis previa** — caída de zona, latencia inyectada, saturación
   del pool de conexiones, pérdida de un broker. Escribir la hipótesis antes y comparar.
7. SBOM CycloneDX, imágenes firmadas con cosign, Kyverno rechazando lo no firmado. Objetivo SLSA 2.
8. Infracost en cada PR y modelo de coste en euros por mil siniestros, con una iteración medida.
9. **Informe de pruebas de resiliencia** estructurado según los requisitos de DORA.

### Cómo saber que se domina
- Destruir el entorno entero y recrearlo con Terraform sin pasos manuales ni apuntes.
- Predecir, antes de ejecutarlo, qué le pasa al SLO cuando cae una zona — y acertar.
- Saber el coste por transacción y qué palanca lo bajaría un 30%.
- Que el operador reconcilie cuando alguien toca a mano un recurso que gestiona.

### Recursos
- **Amazon Builders' Library** — gratis, ~30 artículos. Léela entera; no hay sustituto.
- **Implementing Service Level Objectives** (Hidalgo) — el único que enseña a *calcular* SLOs.
- **Site Reliability Engineering** y **The SRE Workbook** (Google, gratis).
- **Building Secure and Reliable Systems** (Google, gratis).
- **Terraform: Up & Running**, 3ª ed. (Brikman).
- **Kubernetes Patterns** (Ibryam, Huß) y **Programming Kubernetes** (Hausenblas, Schimanski).
- **slsa.dev** y documentación de **Sigstore**.
- **Guía del examen AWS SAP-C02** — solo como mapa de cobertura. No pagar el examen.
- **Reglamento UE 2022/2554 (DORA)** — capítulos de riesgo TIC y pruebas de resiliencia.

---

## Módulo 3 — Sistemas con LLMs, agentes y EDA avanzado
**Meses 8–10 · 130 h (45 teoría / 85 práctica) · repo `claims-intelligence`**

No es reconversión a ingeniería de ML: es aplicar veinte años de sistemas distribuidos a una
dependencia no determinista, cara y lenta. El dominio donde la experiencia previa más rinde.

### Conceptos
- **LLMs para arquitectos** — tokens, ventana de contexto, embeddings, temperatura y muestreo.
  Lo justo para razonar sobre coste, latencia y modos de fallo. Nada de entrenar ni afinar.
- **RAG bien hecho** — fragmentación, búsqueda híbrida (BM25 más vectorial), reranking, y sobre
  todo evaluación: recall@k, precisión, fidelidad a las fuentes. Empezar por pgvector.
- **Agentes como sistemas distribuidos** — tool calling, orquestación frente a coreografía, estado
  conversacional, human-in-the-loop, sagas y compensación para acciones irreversibles, idempotencia
  de herramientas, presupuestos de tokens y de tiempo.
- **MCP** — el estándar de integración entre modelos y sistemas. Leerlo como gobierno de contratos.
- **Gateway de inferencia** — enrutamiento por coste y capacidad, cadenas de fallback, caché
  semántica, rate limiting por tenant, atribución de coste.
- **Verificar lo no determinista** — datasets dorados versionados, LLM-as-judge y sus sesgos, suites
  de regresión con umbral bloqueante en CI, trazas con convenciones semánticas GenAI de OTEL, deriva.
- **Seguridad** — OWASP Top 10 for LLM Applications, inyección de prompt directa e indirecta (los
  documentos subidos son el vector realista), exfiltración vía herramientas, sandboxing,
  validación de salida, redacción de PII.
- **Gobierno de IA** — EU AI Act, Anexo III: la evaluación de riesgo y la tarificación en seguros de
  vida y salud son alto riesgo. Trazabilidad, documentación y supervisión humana como diseño.
- **Event sourcing avanzado** — snapshots, reconstrucción de proyecciones, versionado de eventos, y
  el derecho al olvido del RGPD sobre un log inmutable mediante crypto-shredding.
- **Gobierno de eventos** — AsyncAPI como equivalente de OpenAPI, catálogos, versionado de contratos.

### Entregables de `claims-intelligence`
1. Extracción documental: documento → extracción estructurada con LLM → validación de esquema →
   confianza → **escalado a revisión humana bajo umbral**. El escalado es la decisión central.
2. RAG sobre condicionados de póliza que responda "¿esto está cubierto?" **con citas verificables**.
3. Flujo agéntico sobre las APIs de `claims-core`, con sagas, compensaciones, idempotencia real en
   cada herramienta y presupuesto de pasos acotado.
4. Gateway de inferencia: enrutamiento, fallback, caché semántica, presupuesto por tenant,
   atribución de coste, OTEL con convenciones GenAI.
5. **Arnés de evaluación**: dataset dorado de 100 siniestros versionado, regresión en CI, métricas
   de exactitud y fidelidad, con **umbral que bloquea el merge**.
6. Suite de seguridad: inyección indirecta vía documentos subidos ejecutándose en CI, redacción de
   PII, validación estructural de salida antes de tocar ningún sistema.
7. AsyncAPI de todos los eventos y catálogo publicado.
8. Migración de un agregado a event sourcing puro, con reconstrucción de proyecciones y
   crypto-shredding.

### Cómo saber que se domina
- Cambiar de modelo y saber por la suite si se ha mejorado o empeorado, en números.
- Tener al menos un caso de inyección indirecta que **funcionó** antes de mitigarlo, documentado.
- Explicar y demostrar con un test qué pasa si el agente llama dos veces a la misma herramienta.
- Saber el coste de procesar un siniestro y qué proporción es inferencia.

### Recursos
- **AI Engineering** (Chip Huyen, O'Reilly 2025) — si solo se lee uno, éste.
- **hamel.dev** — evaluación de LLMs. La fuente práctica de referencia.
- **modelcontextprotocol.io** — especificación de MCP.
- **"Building effective agents"** — blog de ingeniería de Anthropic. Corto y anti-hype.
- **OWASP Top 10 for LLM Applications** — gratis.
- **Spring AI** y **LangChain4j** — mantenerse en la JVM.
- **event-driven.io** (Oskar Dudycz) — event sourcing práctico, incluido el RGPD.
- **AsyncAPI** y **EventCatalog**.
- **EU AI Act, Anexo III** — artificialintelligenceact.eu.

---

## Módulo 4 — Arquitectura como código y gobierno ejecutable
**Transversal, meses 1–12 · 50 h · ~1 h/semana · carpeta `docs/`**

No se estudia aparte: se produce mientras se hacen los otros tres. Es lo que convierte tres
subproyectos en un cuerpo de trabajo con criterio visible.

### Conceptos
- **ADRs** — MADR, cuándo una decisión merece uno, y escribir las consecuencias negativas con
  honestidad. Un ADR sin trade-offs incómodos es documentación decorativa.
- **C4 como código** — Structurizr DSL versionado junto al código, para que el diagrama envejezca
  con el sistema y no en una wiki.
- **arc42** — útil sobre todo por las secciones que obliga a rellenar: escenarios de calidad,
  riesgos, deuda conocida.
- **Fitness functions** — ArchUnit imponiendo límites hexagonales y reglas de dependencia en CI.
- **Escenarios de atributos de calidad** — ATAM ligero: convertir "debe ser resiliente" en un
  escenario medible con estímulo, contexto y respuesta esperada. Prerrequisito de un SLO honesto.
- **Architecture advice process** (Harmel-Law) — quien decide consulta, pero decide.
- **Team Topologies** — para razonar sobre dónde poner los límites de servicio.
- **Wardley Mapping** — construir frente a comprar, y evolución de componentes.
- **Escritura de diseño** — RFCs en prosa. La narrativa de seis páginas obliga a un rigor que las
  diapositivas permiten evitar.

### Entregables en `docs/`
- **18–20 ADRs** en MADR, escritos **en el momento** de decidir. Un ADR retrospectivo pierde lo
  que lo hace valioso: la incertidumbre del momento.
- **Modelo C4 en Structurizr DSL**, cuatro niveles, regenerado en cada cambio estructural.
- **Documento arc42** con riesgos y deuda conocida rellenados de verdad.
- **9–10 fitness functions ArchUnit** en CI.
- **Escenarios de atributos de calidad** enlazados con los SLOs del módulo 2.
- **Un design doc en prosa** de seis páginas por subproyecto.
- **Un Wardley map** del sistema, justificando qué se construye y qué se consume gestionado.
- **Modelos de amenazas** STRIDE: uno de plataforma, otro de la capa agéntica.

### Recursos
- **Facilitating Software Architecture** (Harmel-Law, 2024).
- **Software Architecture: The Hard Parts** (Ford, Richards, Sadalage, Dehghani).
- **The Software Architect Elevator** (Hohpe) y architectelevator.com.
- **Team Topologies** (Skelton, Pais).
- **Software Architecture for Developers** (Simon Brown) y documentación de Structurizr DSL.
- **Wardley Maps** (Simon Wardley, gratis online).
- Herramientas: adr.github.io (MADR), ArchUnit, Structurizr DSL, arc42.

---

## Calendario

| Periodo | Contenido | Hito |
|---|---|---|
| Mes 1 | Clases de Kleppmann, DDIA 5 y 7. Esqueleto de `claims-core`: hexagonal, agregado de siniestro, Outbox. Primer ADR la primera semana. | Outbox funcionando + 3 ADRs |
| Meses 2–3 | DDIA 8, 9 y 11; Petrov II; tres Jepsen. Debezium y CDC, Schema Registry con migración incompatible, Kafka Streams, sink a Iceberg. Suite de propiedades, duplicados, benchmark. | `claims-core` con mediciones |
| Meses 4–5 | Kubernetes hasta el operador con CRD. AWS: VPC, IAM, EKS, RDS, MSK, DynamoDB. Terraform modular con OPA en CI. | En EKS, 100% IaC, operador propio |
| Meses 6–7 | Builders' Library. GitOps y canary con análisis de SLO. SLIs, SLOs, error budget, burn rate. Caos con hipótesis previa e informe DORA. SBOM, cosign, Kyverno. Infracost y una optimización medida. | `claims-platform` + informe de resiliencia |
| Mes 8 | AI Engineering. RAG con pgvector, híbrida y reranking. Spring AI o LangChain4j. Extracción documental con validación y escalado humano. | Extracción y RAG con citas |
| Meses 9–10 | MCP, tool calling, sagas y compensación. Gateway de inferencia. Arnés de evaluación con umbral bloqueante. OWASP LLM e inyección indirecta. AsyncAPI. Event sourcing con crypto-shredding. | `claims-intelligence` + informe de evaluación |
| Mes 11 | Cierre documental: arc42, C4 publicado, ADRs revisados, fitness functions en verde, escenarios de calidad, Wardley map, modelos de amenazas. | `docs/` completo |
| Mes 12 | Profundización y deuda del plan. Re-ejecutar los benchmarks del mes 3 con lo que se sabe ahora. | Comparativa mes 3 vs mes 12 |
| Holgura | 4 semanas, una por trimestre. No son descanso: son el seguro contra el pico de trabajo, la enfermedad y el viaje que van a ocurrir. | — |
