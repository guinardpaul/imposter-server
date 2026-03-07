package com.guinardpouard.imposteur.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Test;

class ArchitectureTest {

    private final JavaClasses classes = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.guinardpouard.imposteur");

    @Test
    void domain_should_not_depend_on_other_packages() {
        ArchRuleDefinition.noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        "com.guinardpouard.imposteur.application..",
                        "com.guinardpouard.imposteur.infrastructure..",
                        "com.guinardpouard.imposteur.websocket.."
                )
                .check(classes);
    }

    @Test
    void application_should_only_depend_on_domain_and_infrastructure() {
        ArchRuleDefinition.classes()
                .that().resideInAPackage("..application..")
                .should().onlyDependOnClassesThat().resideInAnyPackage(
                        "java..",
                        "javax..",
                        "org.springframework..",
                        "com.guinardpouard.imposteur.domain..",
                        "com.guinardpouard.imposteur.infrastructure..",
                        "com.guinardpouard.imposteur.application.."
                )
                .check(classes);
    }

    @Test
    void websocket_should_not_depend_on_infrastructure_or_domain_directly() {
        ArchRuleDefinition.noClasses()
                .that().resideInAPackage("..websocket..")
                .and().resideOutsideOfPackage("..websocket.mapper..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        "com.guinardpouard.imposteur.domain..",
                        "com.guinardpouard.imposteur.infrastructure.."
                )
                .check(classes);
    }

    @Test
    void mapper_should_only_depend_on_domain() {
        ArchRuleDefinition.classes()
                .that().resideInAPackage("..websocket.mapper..")
                .should().onlyDependOnClassesThat().resideInAnyPackage(
                        "java..",
                        "javax..",
                        "org.springframework..",
                        "com.guinardpouard.imposteur.domain..",
                        "com.guinardpouard.imposteur.websocket.dto..",
                        "com.guinardpouard.imposteur.websocket.mapper.."
                )
                .check(classes);
    }
}
