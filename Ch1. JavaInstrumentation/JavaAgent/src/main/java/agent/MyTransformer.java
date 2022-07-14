package agent;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class MyTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        byte[] byteCode = classfileBuffer;

        if (className.equals("myapp/Sample")){
          try{
              ClassPool classPool = ClassPool.getDefault();
              CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));

              CtMethod[] methods = ctClass.getDeclaredMethods();
              for (CtMethod method : methods){
                  method.insertAfter("System.out.println(\"adding end line...\");");
              }
              byteCode = ctClass.toBytecode();
              ctClass.detach();
          } catch (Throwable ex) {
              System.out.println("Exception: "+ex);
              ex.printStackTrace();
          }
        }
        return byteCode;
    }
}
