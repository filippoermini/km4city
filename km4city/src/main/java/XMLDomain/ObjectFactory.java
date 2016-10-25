//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.10.14 alle 11:59:24 AM CEST 
//


package XMLDomain;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the XMLDomain package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: XMLDomain
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Tree }
     * 
     */
    public Tree createTree() {
        return new Tree();
    }

    /**
     * Create an instance of {@link Tree.Class }
     * 
     */
    public Tree.Class createTreeClass() {
        return new Tree.Class();
    }

    /**
     * Create an instance of {@link Tree.Class.Properties }
     * 
     */
    public Tree.Class.Properties createTreeClassProperties() {
        return new Tree.Class.Properties();
    }

    /**
     * Create an instance of {@link Tree.Class.Properties.Prop }
     * 
     */
    public Tree.Class.Properties.Prop createTreeClassPropertiesProp() {
        return new Tree.Class.Properties.Prop();
    }

}
