/*
 * Copyright (C) 2016 The Dagger Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dagger.grpc.server.processor;

import static com.google.auto.common.AnnotationMirrors.getAnnotationValue;
import static com.google.auto.common.GeneratedAnnotationSpecs.generatedAnnotationSpec;
import static com.google.auto.common.MoreElements.getAnnotationMirror;
import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;

import com.google.auto.common.MoreTypes;
import com.google.common.base.Joiner;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;

import dagger.grpc.server.ForGrpcService;
import dagger.grpc.server.GrpcService;
import dagger.grpc.server.processor.SourceGenerator.IoGrpc;

import java.util.Optional;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.AnnotationValueVisitor;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.SimpleAnnotationValueVisitor7;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;

class GrpcServiceModel {

    private static final String GRPC_SERVICE_PARAMETER_NAME = "grpcClass";

    private final Types types;
    private final Elements elements;
    private final SourceVersion sourceVersion;
    private final Messager messager;
    final TypeElement serviceImplementation;
    final ClassName serviceImplementationClassName;
    final ClassName serviceDefinitionTypeName;
    final ClassName proxyModuleName;
    final ClassName serviceDefinitionTypeFactoryName;
    final ClassName serviceModuleName;
    final ClassName unscopedServiceModuleName;

    GrpcServiceModel(ProcessingEnvironment processingEnv, TypeElement serviceImplementation) {
        this.types = processingEnv.getTypeUtils();
        this.elements = processingEnv.getElementUtils();
        this.sourceVersion = processingEnv.getSourceVersion();
        this.messager = processingEnv.getMessager();
        this.serviceImplementation = serviceImplementation;
        this.serviceImplementationClassName = ClassName.get(serviceImplementation);
        this.serviceDefinitionTypeName = peerClassWithSuffix("ServiceDefinition");
        this.serviceDefinitionTypeFactoryName = serviceDefinitionTypeName.nestedClass("Factory");
        this.proxyModuleName = peerClassWithSuffix("GrpcProxyModule");
        this.serviceModuleName = peerClassWithSuffix("GrpcServiceModule");
        this.unscopedServiceModuleName = peerClassWithSuffix("UnscopedGrpcServiceModule");
    }

    /**
     * Returns the name of a top-level class in the same package as the service implementation
     * class, whose name is the simple name of the service implementation class and its enclosing
     * classes, joined with underscores, and appended with {@code suffix}.
     */
    private ClassName peerClassWithSuffix(String suffix) {
        return serviceImplementationClassName.peerClass(
                Joiner.on('_').join(serviceImplementationClassName.simpleNames()) + suffix);
    }

    String packageName() {
        return serviceImplementationClassName.packageName();
    }

    public boolean validate() {
        AnnotationValue argument =
                getAnnotationValue(grpcServiceAnnotation(), GRPC_SERVICE_PARAMETER_NAME);
        return argument.accept(
                new SimpleAnnotationValueVisitor7<Boolean, AnnotationValue>(false) {
                    @Override
                    public Boolean visitType(TypeMirror type, AnnotationValue value) {
                        return validateGrpcClass(type, value);
                    }
                },
                argument);
    }

    private AnnotationMirror grpcServiceAnnotation() {
        return getAnnotationMirror(serviceImplementation, GrpcService.class).get();
    }

    /**
     * Returns the gRPC service class declared by {@link GrpcService#grpcClass()}.
     */
    protected final TypeElement grpcClass() {
        AnnotationValue argument =
                getAnnotationValue(grpcServiceAnnotation(), GRPC_SERVICE_PARAMETER_NAME);
        return GET_TYPE_ELEMENT_FROM_VALUE.visit(argument, argument);
    }

    /**
     * Returns the annotation spec for the {@code @Generated} annotation to add to any
     * type generated by this processor.
     */
    protected final Optional<AnnotationSpec> generatedAnnotation() {
        return generatedAnnotationSpec(
                elements,
                sourceVersion,
                GrpcService.class,
                String.format(
                        "@%s annotation on %s",
                        GrpcService.class.getCanonicalName(), serviceImplementationClassName));
    }

    /**
     * Returns the annotation spec for a {@link ForGrpcService} annotation whose value is the
     * gRPC-generated service class.
     */
    protected final AnnotationSpec forGrpcService() {
        return AnnotationSpec.builder(ForGrpcService.class)
                .addMember("value", "$T.class", grpcClass())
                .build();
    }

    protected final String subcomponentServiceDefinitionMethodName() {
        return UPPER_CAMEL.to(LOWER_CAMEL, simpleServiceName()) + "ServiceDefinition";
    }

    private String simpleServiceName() {
        return grpcClass().getSimpleName().toString().replaceFirst("Grpc$", "");
    }

    private TypeElement serviceImplBase(TypeMirror service) {
        ClassName serviceClassName = ClassName.get(MoreTypes.asTypeElement(service));
        ClassName serviceImplBaseName = serviceClassName.nestedClass(simpleServiceName() + "ImplBase");
        return elements.getTypeElement(serviceImplBaseName.toString());
    }

    private boolean validateGrpcClass(TypeMirror type, AnnotationValue value) {
        TypeElement serviceImplBase = serviceImplBase(type);
        if (serviceImplBase == null || !types.isSubtype(serviceImplBase.asType(), bindableService())) {
            messager.printMessage(
                    Kind.ERROR,
                    String.format("%s is not a gRPC service class", type),
                    serviceImplementation,
                    grpcServiceAnnotation(),
                    value);
            return false;
        }
        if (!(types.isSubtype(serviceImplementation.asType(), serviceImplBase.asType()))) {
            messager.printMessage(
                    Kind.ERROR,
                    String.format(
                            "%s must extend %s", serviceImplementation, serviceImplBase.getQualifiedName()),
                    serviceImplementation,
                    grpcServiceAnnotation(),
                    value);
            return false;
        }
        return true;
    }

    private TypeMirror bindableService() {
        return elements.getTypeElement(IoGrpc.BINDABLE_SERVICE.toString()).asType();
    }

    static final AnnotationValueVisitor<TypeElement, AnnotationValue> GET_TYPE_ELEMENT_FROM_VALUE =
            new SimpleAnnotationValueVisitor7<TypeElement, AnnotationValue>() {
                @Override
                public TypeElement visitType(TypeMirror t, AnnotationValue p) {
                    return MoreTypes.asTypeElement(t);
                }

                @Override
                protected TypeElement defaultAction(Object o, AnnotationValue p) {
                    throw new IllegalArgumentException("Expected " + p + " to be a class");
                }
            };
}
