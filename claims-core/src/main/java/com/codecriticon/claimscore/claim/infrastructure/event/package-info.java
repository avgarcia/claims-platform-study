/**
 * Adaptador de mensajería: conecta los eventos de dominio con sistemas externos (p.ej. Kafka).
 * Agrupa listener (reacción a eventos internos), sender (publicación externa), mapper (evento de
 * dominio↔evento de integración) y model (contrato serializable publicado).
 */
@NullMarked
package com.codecriticon.claimscore.claim.infrastructure.event;

import org.jspecify.annotations.NullMarked;
