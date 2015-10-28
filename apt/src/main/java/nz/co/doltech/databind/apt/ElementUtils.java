package nz.co.doltech.databind.apt;

import com.github.misberner.apcommons.util.Visibility;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ErrorType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.NullType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.UnionType;
import javax.lang.model.type.WildcardType;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.SimpleTypeVisitor7;
import java.util.Set;

public class ElementUtils extends com.github.misberner.apcommons.util.ElementUtils {

    public static boolean hasAtleastOneModifier(Element element, Modifier modifier, Modifier... modifiers) {
        Set<Modifier> modifierSet = element.getModifiers();
        if(modifierSet.contains(modifier)) {
            return true;
        } else {
            for (Modifier mod : modifiers) {
                if (modifierSet.contains(mod)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasModifier(Element element, Modifier modifier) {
        for(Modifier mod : element.getModifiers()) {
            if(mod.equals(modifier)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasDefaultCtor(Element element) {
        for(ExecutableElement ctor : ElementFilter.constructorsIn(element.getEnclosedElements())) {
            if (ctor.getParameters().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public static Visibility getVisibilityFromPackage(Element element) {
        Visibility vis = Visibility.of(element);

        Element enclosing = element.getEnclosingElement();

        while(enclosing != null) {
            vis = vis.meet(Visibility.of(enclosing));

            if(enclosing.getKind() == ElementKind.PACKAGE) {
                break;
            }
            enclosing = enclosing.getEnclosingElement();
        }

        return vis;
    }

    public static Visibility getVisibility(Element element) {
        return new ElementUtils().getEffectiveVisibility(element);
    }

    public static TypeElement getTypeElementFromPackage(PackageElement pkgElement, String enclosedType) {
        for(Element element : pkgElement.getEnclosedElements()) {
            if(element instanceof TypeElement) {
                TypeElement enclosedElem = (TypeElement) element;
                if(enclosedElem.getQualifiedName().toString().equals(enclosedType)) {
                    return enclosedElem;
                }
            }
        }
        return null;
    }
}
