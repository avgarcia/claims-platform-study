package com.codecriticon.claimscore.architecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.implement;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@AnalyzeClasses(packages = "com.codecriticon.claimscore")
public class LayeredArchitecturePackageTest {

    @ArchTest
    static final ArchRule layered_architecture_are_respected = layeredArchitecture()
        .consideringAllDependencies()
        .layer("infrastructure").definedBy("..infrastructure..")
        .layer("application").definedBy("..application..")
        .layer("domain").definedBy("..domain..")
        .whereLayer("infrastructure").mayNotBeAccessedByAnyLayer()
        .whereLayer("application").mayOnlyBeAccessedByLayers("infrastructure")
        .whereLayer("domain").mayOnlyBeAccessedByLayers("infrastructure", "application");

    @ArchTest
    static final ArchRule no_framework_in_domain = noClasses().that()
        .resideInAPackage("..domain..")
        .should()
        .dependOnClassesThat()
        .resideInAnyPackage(
            "org.springframework..",
            "jakarta.persistence..",
            "com.fasterxml.jackson.."
        );

    @ArchTest
    static final ArchRule ports_must_be_interfaces = classes().that()
        .resideInAPackage("..application.port..")
        .should()
        .beInterfaces();

    @ArchTest
    static final ArchRule api_cannot_call_other_adapter = noClasses().that()
        .resideInAPackage("..infrastructure.api..")
        .should()
        .dependOnClassesThat()
        .resideInAnyPackage("..infrastructure.database..", "..infrastructure.event..");

    @ArchTest
    static final ArchRule database_cannot_call_other_adapter = noClasses().that()
        .resideInAPackage("..infrastructure.database..")
        .should()
        .dependOnClassesThat()
        .resideInAnyPackage("..infrastructure.api..", "..infrastructure.event..");

    @ArchTest
    static final ArchRule event_cannot_call_other_adapter = noClasses().that()
        .resideInAPackage("..infrastructure.event..")
        .should()
        .dependOnClassesThat()
        .resideInAnyPackage("..infrastructure.api..", "..infrastructure.database..");

    @ArchTest
    static final ArchRule output_port_implementations_live_in_infrastructure = classes()
        .that(implement(resideInAPackage("..application.port.out..")))
        .should()
        .resideInAPackage("..infrastructure..");

    @ArchTest
    static final ArchRule input_port_implementations_live_in_usecase = classes()
        .that(implement(resideInAPackage("..application.port.in..")))
        .should()
        .resideInAPackage("..application.usecase..");
}
