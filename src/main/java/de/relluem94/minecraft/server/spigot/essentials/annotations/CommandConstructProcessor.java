package de.relluem94.minecraft.server.spigot.essentials.annotations;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.util.Set;

@SupportedAnnotationTypes("de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class CommandConstructProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        TypeElement ifaceElement = processingEnv.getElementUtils()
                .getTypeElement("de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct");

        if (ifaceElement == null) {
            processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.ERROR,
                    "Interface CommandConstruct not found on classpath!"
            );
            return false;
        }

        TypeMirror iface = ifaceElement.asType();

        for (Element element : roundEnv.getRootElements()) {
            if (element.getKind() != ElementKind.CLASS) continue;

            TypeElement typeElement = (TypeElement) element;

            boolean implementsIface = processingEnv.getTypeUtils().isAssignable(typeElement.asType(), iface);
            boolean hasAnnotation = typeElement.getAnnotation(CommandName.class) != null;

            if (implementsIface && !hasAnnotation) {
                processingEnv.getMessager().printMessage(
                        Diagnostic.Kind.ERROR,
                        "Class " + typeElement.getQualifiedName()
                                + " implements CommandConstruct, but does not have a @CommandName Annotation.",
                        element
                );
            }
        }

        return false;
    }
}

