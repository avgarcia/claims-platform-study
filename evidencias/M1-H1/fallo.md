# Fallo provocado — anotación de Spring en el dominio

- **Hito:** M1-H1
- **Fecha:** 2026-09-01
- **Componente objetivo:** regla de ArchUnit `no_framework_in_domain`, sobre la clase
  `com.codecriticon.claimscore.claim.domain.model.ClaimStatus`.
- **Cómo se provoca:** añadir `import org.springframework.stereotype.Component;` y anotar el enum
  `ClaimStatus` con `@Component`. Reproducible ejecutando después:
  ```bash
  ./gradlew test --tests "com.codecriticon.claimscore.architecture.LayeredArchitecturePackageTest"
  ```

## Hipótesis (escrita ANTES de ejecutar)

Saltará la regla `no_framework_in_domain`. Señalará la clase `ClaimStatus`, y el mensaje indicará
que `ClaimStatus` no puede depender de Spring.

## Resultado real

El test falla. Mensaje completo:

```
Architecture Violation [Priority: MEDIUM] - Rule 'no classes that reside in a package '..domain..'
should depend on classes that reside in any package ['org.springframework..', 'jakarta.persistence..',
'com.fasterxml.jackson..']' was violated (1 times):
Class <com.codecriticon.claimscore.claim.domain.model.ClaimStatus> is annotated with
<org.springframework.stereotype.Component> in (ClaimStatus.java:0)
```

## Diferencia

Ninguna — la hipótesis acierta en la regla, la clase señalada y la sustancia del mensaje
(dependencia de Spring detectada sobre `ClaimStatus`). Un experimento sin sorpresas: es justo lo que
se esperaría de una regla de ArchUnit escrita correctamente y probada de antes contra este mismo tipo
de violación (ver la sesión anterior, donde la regla de capas se corrigió y se validó con el mismo
procedimiento).

## Qué se cambió a raíz de esto

Nada en el código — el import y la anotación se revirtieron inmediatamente después de confirmar el
resultado, y la suite completa vuelve a estar en verde. El valor de este experimento no es haber
encontrado un bug, sino tener la certeza demostrada (no solo escrita) de que la regla protege
exactamente el escenario para el que se diseñó: el mismo que abrió la sesión de andamiaje — un
`@Entity` o cualquier anotación de framework colándose en el dominio.
