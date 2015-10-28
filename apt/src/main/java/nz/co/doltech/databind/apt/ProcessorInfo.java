/*
 * Copyright 2015 Doltech Systems Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package nz.co.doltech.databind.apt;

import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;

public class ProcessorInfo {
    TypeElement typeElement;
    Annotation annotation;
    String implName;
    String packageName;

    public ProcessorInfo(TypeElement typeElement, Annotation annotation,
                         String implName, String packageName) {
        this.typeElement = typeElement;
        this.annotation = annotation;
        this.implName = implName;
        this.packageName = packageName;
    }

    public TypeElement getTypeElement() {
        return typeElement;
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public String getImplName() {
        return implName;
    }

    public String getPackageName() {
        return packageName;
    }
}