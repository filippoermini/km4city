//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.10.14 alle 11:59:24 AM CEST 
//


package XMLDomain;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;



/**
 * <p>Classe Java per anonymous complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="class" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="properties">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="prop" maxOccurs="unbounded" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="maxValue" type="{http://www.w3.org/2001/XMLSchema}byte" minOccurs="0"/>
 *                                       &lt;element name="minValue" type="{http://www.w3.org/2001/XMLSchema}byte" minOccurs="0"/>
 *                                       &lt;element name="uri" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *                                     &lt;/sequence>
 *                                     &lt;attribute name="key" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *                 &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="isRoot" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="baseUri" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "clazz",
    "queryInfo"
})
@XmlRootElement(name = "tree")
public class Tree {

	@XmlAttribute(name = "isStateful")
	protected String isStateful;
	@XmlAttribute(name = "typeId")
	protected String typeId;
	@XmlAttribute(name = "fileName")
	protected String fileName;
    @XmlElement(name = "class")
    protected List<Tree.Class> clazz;
    protected Tree.QueryInfo queryInfo;

    public Tree.QueryInfo getQueryInfo(){
    	
    	return queryInfo==null?new QueryInfo():queryInfo;
    }
    public void setQueryInfo(Tree.QueryInfo queryInfo){
    	this.queryInfo = queryInfo;
    }
    
    public boolean isStatefull() {
		return isStateful.contains("true");
	}
	public void setStatefull(String stateful) {
		this.isStateful = stateful;
	}
	public String getTypeId(){
    	return this.typeId;
    }
    public void setTypeId(String type){
    	this.typeId = type;
    }
    public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
     * Gets the value of the clazz property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the clazz property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getClazz().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Tree.Class }
     * 
     * 
     */
    public List<Tree.Class> getClazz() {
        if (clazz == null) {
            clazz = new ArrayList<Tree.Class>();
        }
        return this.clazz;
    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="properties">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="prop" maxOccurs="unbounded" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="maxValue" type="{http://www.w3.org/2001/XMLSchema}byte" minOccurs="0"/>
     *                             &lt;element name="minValue" type="{http://www.w3.org/2001/XMLSchema}byte" minOccurs="0"/>
     *                             &lt;element name="uri" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
     *                           &lt;/sequence>
     *                           &lt;attribute name="key" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
     *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="isRoot" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="baseUri" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "query",
        "server"
    })
    public static class QueryInfo{
    	
    	protected String query;
    	protected String server;
    	
    	public void setQuery(String query){
    		this.query = query;
    	}
    	
    	public String getQuery(){
    		return query;
    	}
    	
    	public void setServer(String server){
    		this.server = server;
    	}
    	
    	public String getServer(){
    		return server;
    	}	
    }
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "properties"
    })
    public static class Class {

        @XmlElement(required = true)
        protected Tree.Class.Properties properties;
        @XmlAttribute(name = "type")
        @XmlSchemaType(name = "anyURI")
        protected String type;
        @XmlAttribute(name = "name")
        protected String name;
        @XmlAttribute(name = "isRoot")
        protected String isRoot;
        @XmlAttribute(name = "baseUri")
        protected String baseUri;

        /**
         * Recupera il valore della proprietà properties.
         * 
         * @return
         *     possible object is
         *     {@link Tree.Class.Properties }
         *     
         */
        public Tree.Class.Properties getProperties() {
            return properties;
        }
        
        
        /**
         * Imposta il valore della proprietà properties.
         * 
         * @param value
         *     allowed object is
         *     {@link Tree.Class.Properties }
         *     
         */
        public void setProperties(Tree.Class.Properties value) {
        	this.properties = value;
        }

        /**
         * Recupera il valore della proprietà type.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getType() {
            return type;
        }

        /**
         * Imposta il valore della proprietà type.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setType(String value) {
            this.type = value;
        }

        /**
         * Recupera il valore della proprietà name.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getName() {
            return name;
        }

        /**
         * Imposta il valore della proprietà name.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setName(String value) {
            this.name = value;
        }

        /**
         * Recupera il valore della proprietà isRoot.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIsRoot() {
            return isRoot;
        }

        /**
         * Imposta il valore della proprietà isRoot.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIsRoot(String value) {
            this.isRoot = value;
        }

        /**
         * Recupera il valore della proprietà baseUri.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getBaseUri() {
            return baseUri;
        }

        /**
         * Imposta il valore della proprietà baseUri.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setBaseUri(String value) {
            this.baseUri = value;
        }


        /**
         * <p>Classe Java per anonymous complex type.
         * 
         * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="prop" maxOccurs="unbounded" minOccurs="0">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="maxValue" type="{http://www.w3.org/2001/XMLSchema}byte" minOccurs="0"/>
         *                   &lt;element name="minValue" type="{http://www.w3.org/2001/XMLSchema}byte" minOccurs="0"/>
         *                   &lt;element name="uri" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
         *                 &lt;/sequence>
         *                 &lt;attribute name="key" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
       
        
       
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "prop"
        })
        public static class Properties {

            protected List<Tree.Class.Properties.Prop> prop;

            /**
             * Gets the value of the prop property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the prop property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getProp().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link Tree.Class.Properties.Prop }
             * 
             * 
             */
            public List<Tree.Class.Properties.Prop> getProp() {
                if (prop == null) {
                    prop = new ArrayList<Tree.Class.Properties.Prop>();
                }
                return this.prop;
            }


            /**
             * <p>Classe Java per anonymous complex type.
             * 
             * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="maxValue" type="{http://www.w3.org/2001/XMLSchema}byte" minOccurs="0"/>
             *         &lt;element name="minValue" type="{http://www.w3.org/2001/XMLSchema}byte" minOccurs="0"/>
             *         &lt;element name="uri" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
             *       &lt;/sequence>
             *       &lt;attribute name="key" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "type",
                "maxValue",
                "minValue",
                "uri",
                "valueExpression",
                "hourValue",
                "range",
                "fromSet"
            })
            public static class Prop {

                @XmlElement(required = true)
                protected String type;
                protected String maxValue;
                protected String minValue;
                @XmlSchemaType(name = "anyURI")
                protected String uri;
                @XmlAttribute(name = "key")
                @XmlSchemaType(name = "anyURI")
                protected String key;
                protected String valueExpression;
                protected String hourValue;
                protected String range;
                protected String fromSet;

                
                public HashMap<String,String> getAttributeList(){
                	HashMap<String,String> map = new HashMap<>();
                	Field[] fields = this.getClass().getDeclaredFields();
                	for(Field f:fields){
                		try {
							Object v = f.get(this);
							map.put(f.getName(),(String) v);
						} catch (IllegalArgumentException | IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                	}
                	return map;
                }
                
                
                public String getFromSet() {
					return fromSet;
				}


				public void setFromSet(String fromSet) {
					this.fromSet = fromSet;
				}


				public String getHourValue() {
					return hourValue;
				}
				public void setHourValue(String hourValue) {
					this.hourValue = hourValue;
				}
				public String getRange() {
					return range;
				}
				public void setRange(String range) {
					this.range = range;
				}
				/**
                 * Recupera il valore della proprietà type.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getValueExpression(){
                	return valueExpression;
                }
                public void setValueExpression(String valueExpression){
                	this.valueExpression = valueExpression;
                }
                public String getType() {
                    return type;
                }

                /**
                 * Imposta il valore della proprietà type.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setType(String value) {
                    this.type = value;
                }

                /**
                 * Recupera il valore della proprietà maxValue.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Byte }
                 *     
                 */
                public String getMaxValue() {
                    return maxValue;
                }

                /**
                 * Imposta il valore della proprietà maxValue.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Byte }
                 *     
                 */
                public void setMaxValue(String value) {
                    this.maxValue = value;
                }

                /**
                 * Recupera il valore della proprietà minValue.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Byte }
                 *     
                 */
                public String getMinValue() {
                    return minValue;
                }

                /**
                 * Imposta il valore della proprietà minValue.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Byte }
                 *     
                 */
                public void setMinValue(String value) {
                    this.minValue = value;
                }

                /**
                 * Recupera il valore della proprietà uri.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getUri() {
                    return uri;
                }

                /**
                 * Imposta il valore della proprietà uri.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setUri(String value) {
                    this.uri = value;
                }

                /**
                 * Recupera il valore della proprietà key.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getKey() {
                    return key;
                }

                /**
                 * Imposta il valore della proprietà key.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setKey(String value) {
                    this.key = value;
                }

            }

        }

    }

}
