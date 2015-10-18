package nz.co.doltech.databind.apt.reflect.gwt.util;

import com.github.javaparser.ast.body.ModifierSet;

import javax.lang.model.element.Modifier;
import java.util.HashSet;
import java.util.Set;

public class ModifierUtil {

    public static Set<Modifier> asSet(int modifiers) {
        Set<Modifier> modSet = new HashSet<>();
        if(ModifierSet.hasModifier(modifiers, ModifierSet.FINAL)) {
            modSet.add(Modifier.FINAL);
        }
        if(ModifierSet.hasModifier(modifiers, ModifierSet.NATIVE)) {
            modSet.add(Modifier.NATIVE);
        }
        if(ModifierSet.hasModifier(modifiers, ModifierSet.ABSTRACT)) {
            modSet.add(Modifier.ABSTRACT);
        }
        if(ModifierSet.hasModifier(modifiers, ModifierSet.PRIVATE)) {
            modSet.add(Modifier.PRIVATE);
        }
        if(ModifierSet.hasModifier(modifiers, ModifierSet.PROTECTED)) {
            modSet.add(Modifier.PROTECTED);
        }
        if(ModifierSet.hasModifier(modifiers, ModifierSet.PUBLIC)) {
            modSet.add(Modifier.PUBLIC);
        }
        if(ModifierSet.hasModifier(modifiers, ModifierSet.STATIC)) {
            modSet.add(Modifier.STATIC);
        }
        if(ModifierSet.hasModifier(modifiers, ModifierSet.STRICTFP)) {
            modSet.add(Modifier.STRICTFP);
        }
        if(ModifierSet.hasModifier(modifiers, ModifierSet.SYNCHRONIZED)) {
            modSet.add(Modifier.SYNCHRONIZED);
        }
        if(ModifierSet.hasModifier(modifiers, ModifierSet.TRANSIENT)) {
            modSet.add(Modifier.TRANSIENT);
        }
        return modSet;
    }
}
