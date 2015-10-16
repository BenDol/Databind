package nz.co.doltech.databind.apt;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.util.ElementFilter;
import java.util.Set;

public class ElementUtils {

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
}
