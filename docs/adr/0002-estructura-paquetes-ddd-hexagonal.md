# 0002 · Estructura de paquetes por feature con capas hexagonales dentro

- **Estado:** aceptado
- **Fecha:** 2026-09-03
- **Contexto del plan:** Módulo 1, hito M1-H1

## Contexto y problema

Había que diseñar una aplicación que permitiera gestionar los siniestros — un dominio complejo que
se puede complicar mucho. Para organizar correctamente los casos de uso de la aplicación y que no se
mezclaran conceptos, la aplicación de DDD ayuda a dejar el dominio limpio y bien definido; la
arquitectura hexagonal, junto con el DDD, hace más sencillo centrarse en los casos de uso de la
aplicación dejando aparte todo lo relacionado con el framework de implementación. Ambos ya venían
dados por el plan del módulo 1, no eran la decisión a tomar aquí.

Lo que sí había que decidir era la estructura concreta de paquetes: ninguna de las decisiones finas
viene dada por "usar hexagonal". Además, la estructura donde se dividen las capas por
responsabilidad hace que sea más sencillo comprobar los límites mediante tests de ArchUnit — el
entregable exigía límites verificados por tests, no por disciplina. La separación por dominios
también simplifica quién y cuándo puede llamar a cada uno de los dominios, convirtiéndolo en algo
verificable en tiempo de test.

## Opciones consideradas

1. **Paquetes por capa a nivel raíz** (`domain/`, `application/`, `infrastructure/` en la raíz del
   proyecto, sin noción de feature) — el hexagonal "de libro de texto". Se descartó porque impide una
   separación entre cada uno de los dominios: no existe forma de evitar que se mezcle la lógica entre
   ellos, y no permite separar los dominios en servicios independientes de forma sencilla si en el
   futuro aparece un segundo dominio.
2. **Módulos Gradle separados por capa** — domain sin Spring ni nada de infraestructura en su
   classpath, límites verificados en compilación. Se descartó por complejidad: cada módulo necesita
   su propia configuración, y con un único dominio hoy esa ceremonia no se justifica.
3. **Paquetes por feature con capas hexagonales dentro** (`claim/domain`, `claim/application`,
   `claim/infrastructure`) — la elegida. Puertos de entrada divididos en command/query dentro de
   `application`, un caso de uso por clase, y entity/mapper separados del dominio en la capa de
   infraestructura.

## Decisión

Se decide la opción de un paquete por dominio y arquitectura hexagonal dentro de cada uno, por
simplicidad, separación de conceptos, facilidad para la gestión de límites de cada dominio y por
sencillez para una futura separación en servicios independientes.

## Consecuencias

### Positivas

- Separación clara entre dominios.
- Separación de la lógica de negocio del framework.
- Facilidad para una separación futura en servicios independientes.

### Negativas

- Utilizar una clase por caso de uso requiere una definición muy cuidadosa de la parte común que
  puede haber entre casos de uso, como validaciones o gestión de permisos.
- La separación por feature da fronteras visibles entre dominios, pero la comunicación entre
  dominios y cómo gestionar la relación entre ellos todavía no está resuelta: cuando aparezca un
  segundo dominio, hay que evitar que se creen dependencias fuertes entre ellos. Esa separación no
  está verificada todavía — a diferencia de domain/application/infrastructure, que sí tiene su
  `layeredArchitecture()` de ArchUnit, hoy nada falla si un dominio importa código de otro sin
  querer. Se resolverá con Spring Modulith cuando sea necesario.

## Qué invalidaría esta decisión

Cuando aparezca un segundo dominio y haya que gestionar su separación.

## Referencias

- [buckpal](https://github.com/thombergs/buckpal) — Tom Hombergs, referencia de la convención
  `application/port/in` / `application/port/out` seguida para la ubicación de los puertos.
