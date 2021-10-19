package reflection.api;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ReflectionExplorer implements Investigator {

    private Object suspect;
    //private Class suspectClass;

    @Override
    public void load(Object anInstanceOfSomething) {
        this.suspect = anInstanceOfSomething;
        //this.suspectClass = suspect.getClass();
    }

    @Override
    public int getTotalNumberOfMethods() {
        return suspect.getClass().getDeclaredMethods().length;
    }

    @Override
    public int getTotalNumberOfConstructors() {
        return suspect.getClass().getConstructors().length;
    }

    @Override
    public int getTotalNumberOfFields() {
        return this.suspect.getClass().getDeclaredFields().length;
    }

    @Override
    public Set<String> getAllImplementedInterfaces() {
        Class<?>[] Interfaces = this.suspect.getClass().getInterfaces();
        Set<String> interfacesNames = new HashSet<>();

        for(Class<?> inter : Interfaces){
            interfacesNames.add(inter.getName());
        }

        return interfacesNames;
    }

    @Override
    public int getCountOfConstantFields() {
        Field[] fields = this.getClass().getDeclaredFields();
        int finalMembersCounter = 0;

        for(Field f : fields){
            if(Modifier.FINAL == f.getModifiers()) {
                finalMembersCounter++;
            }
        }

        return finalMembersCounter;
    }

    @Override
    public int getCountOfStaticMethods() {
        Method[] methods = this.suspect.getClass().getDeclaredMethods();
        int staticMethodsCounter = 0;

        for(Method m : methods){
            if(m.getModifiers() == Modifier.STATIC){
                staticMethodsCounter++;
            }
        }
        return staticMethodsCounter;
    }

    @Override
    public boolean isExtending() {
        return !(this.suspect.getClass().getSuperclass() == null);
    }

    @Override
    public String getParentClassSimpleName() {
        if(this.isExtending()) {
            return this.suspect.getClass().getSuperclass().getName();
        }

        return null;
    }

    @Override
    public boolean isParentClassAbstract() {
        if(!(this.getParentClassSimpleName() == null)){
            return this.suspect.getClass().getSuperclass().getModifiers() == Modifier.ABSTRACT;
        }

        return false;
    }

    @Override
    public Set<String> getNamesOfAllFieldsIncludingInheritanceChain() {

        Set<String> allFields = new HashSet<>();
        if(this.isExtending()) {
           ReflectionExplorer superClass = new ReflectionExplorer();
           superClass.load(this.suspect.getClass().getSuperclass());
           allFields.addAll(superClass.getNamesOfAllFieldsIncludingInheritanceChain());
        }

        Arrays.stream(this.suspect.getClass().getDeclaredFields()).map(s -> s.getName()).forEach(s -> allFields.add(s));
        return allFields;
    }

    @Override
    public int invokeMethodThatReturnsInt(String methodName, Object... args) {
         return 0;
    }

    @Override
    public Object createInstance(int numberOfArgs, Object... args) {

        // is there a problem in using Constructor instead of Class ?
        Constructor[] constructors = this.suspect.getClass().getConstructors();
        for(Constructor constructor : constructors){
            if(constructor.getParameterCount() == numberOfArgs){
                try {
                    // not supposed to use any exception according to aviad
                    return constructor.newInstance(args);
                }
                catch (Exception e){
                    return  null;
                }
            }
        }

        return null;
    }


    @Override
    public Object elevateMethodAndInvoke(String name, Class<?>[] parametersTypes, Object... args) {
        return null;
    }

    @Override
    public String getInheritanceChain(String delimiter) {
        return null;
    }
}
