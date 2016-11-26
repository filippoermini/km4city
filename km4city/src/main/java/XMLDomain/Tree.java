//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.10.14 alle 11:59:24 AM CEST 
//


package XMLDomain;

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

import genericClass.AttributeParam;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
	"fileIterationQuery",
	"classIterationQuery",
    "fileInfo",
    "iterationElement"
})
@XmlRootElement(name = "tree")
public class Tree {

	@XmlAttribute(name = "isStateful")
	protected String isStateful;
	@XmlAttribute(name = "typeId")
	protected String typeId;
	@XmlAttribute(name = "simulationName")
	protected String simulationName;
    @XmlElement(name = "fileIterationQuery")
    protected Tree.QueryInfo fileIterationQuery;
    @XmlElement(name = "classIterationQuery")
    protected Tree.QueryInfo classIterationQuery;
    @XmlElement(name = "fileInfo")
    protected Tree.FileInfo fileInfo;
    @XmlElement(name = "iterationElement")
    protected Tree.iterationElement iterationElement;
    
    public Tree.iterationElement getIterationElement(){
    	return this.iterationElement;
    }
    
    public void setIterationElement(Tree.iterationElement it){
    	this.iterationElement = it;
    }
    
	public Tree.FileInfo getFileInfo() {
		return fileInfo;
	}
	public void setFileInfo(Tree.FileInfo fileInfo) {
		this.fileInfo = fileInfo;
	}
	
	
	public Tree.QueryInfo getFileIterationQuery() {
		return fileIterationQuery==null?new QueryInfo():fileIterationQuery;
	}
	public void setFileIterationQuery(Tree.QueryInfo fileIterationQuery) {
		this.fileIterationQuery = fileIterationQuery;
	}
	public Tree.QueryInfo getClassIterationQuery() {
		return classIterationQuery==null?new QueryInfo():classIterationQuery;
	}
	public void setClassIterationQuery(Tree.QueryInfo classIterationQuery) {
		this.classIterationQuery = classIterationQuery;
	}
	
    public void setQueryInfo(Tree.QueryInfo fileIterationquery){
    	this.fileIterationQuery = fileIterationquery;
    }
    
    public String getIsStateful() {
		return isStateful;
	}
	public void setIsStateful(String isStateful) {
		this.isStateful = isStateful;
	}
	public String getSimulationName() {
		return simulationName;
	}
	public void setSimulationName(String simulationName) {
		this.simulationName = simulationName;
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
	
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "",propOrder = {
    	"clazz",
    	"attributes"
    })
    public static class iterationElement{
    	@XmlElement(name = "attributes")
        protected Tree.Class.Properties attributes;
    	@XmlElement(name = "class")
        protected List<Tree.Class> clazz;
    	
    	public Tree.Class.Properties getAttributes() {
    		return attributes;
    	}
    	public void seAttributes(Tree.Class.Properties hiddenProperties) {
    		this.attributes = hiddenProperties;
    	}
    	
    	public List<Tree.Class> getClazz() {
            if (clazz == null) {
                clazz = new ArrayList<Tree.Class>();
            }
            return this.clazz;
        }

    	
    }
    
    
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "name",
        "valueExpression"
    })
    public static class Attribute{
    	@XmlAttribute(name= "name")
    	protected String name;
    	@XmlAttribute(name= "valueExpression")
    	protected String valueExpression;
		
    	public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getValueExpression() {
			return valueExpression;
		}
		public void setValueExpression(String valueExpression) {
			this.valueExpression = valueExpression;
		}
    }
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "linkedTree"
    })
    public static class LinkedTree{
    	
    	@XmlElement(name = "linkedTree")
    	protected List<String> linkedTree;

		public List<String> getLinkedTree() {
			return linkedTree;
		}

		public void setLinkedTree(List<String> linkedTree) {
			this.linkedTree = linkedTree;
		}
    	
    }
    public static class LinkedAttribute{
    	
    }
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "fileName",
        "startDirectory"
    })
    public static class FileInfo{
 
    	protected String fileName;
    	protected String startDirectory;
		
    	public String getFileName() {
			return fileName;
		}
		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
		public String getStartDirectory() {
			return startDirectory;
		}
		public void setStartDirectory(String startDirectory) {
			this.startDirectory = startDirectory;
		}
    }
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "query",
        "server",
        "bindingValue"
    })
    public static class QueryInfo{
    	
    	protected String query;
    	protected String server;
    	protected String bindingValue;
    	    	
    	public String getBindingValue() {
			return bindingValue;
		}

		public void setBindingValue(String bindingValue) {
			this.bindingValue = bindingValue;
		}

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


      
       
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "prop"
        })
        public static class Properties {

            protected List<Tree.Class.Properties.Prop> prop;

          
            public List<Tree.Class.Properties.Prop> getProp() {
                if (prop == null) {
                    prop = new ArrayList<Tree.Class.Properties.Prop>();
                }
                return this.prop;
            }


            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "type",
                "maxValue",
                "minValue",
                "uri",
                "valueExpression",
                "hourValue",
                "range",
                "set",
                "name",
                "queryInfo",
                "md5String"
            })
            public static class Prop {

                @XmlElement(required = true)
                protected String type;
                protected String maxValue;
                protected String minValue;
                protected String name;
                @XmlSchemaType(name = "anyURI")
                protected String uri;
                @XmlAttribute(name = "key")
                protected String key;
                protected String valueExpression;
                protected String hourValue;
                protected String range;
                protected String set;
                @XmlAttribute(name = "isUri")
                protected String isUri;
                @XmlAttribute(name = "isHidden")
                protected String isHidden;
                @XmlElement(name = "queryInfo")
                protected Tree.QueryInfo queryInfo;
                protected String md5String;
         
                public String getMd5String() {
					return md5String;
                }
				public void setMd5String(String md5) {
					this.md5String = md5;
				}

				public boolean isHidden(){
                	return isHidden==null?false:isHidden.contains("true");
                }
                
                public String getIsHidden() {
					return isHidden;
				}

				public void setIsHidden(String isHidden) {
					this.isHidden = isHidden;
				}

				public Tree.QueryInfo getQueryInfo() {
					return queryInfo;
				}

				public void setQueryInfo(Tree.QueryInfo query) {
					this.queryInfo = query;
				}

				public HashMap<String,AttributeParam> getAttributeList(){
                	HashMap<String,AttributeParam> map = new HashMap<>();
                	Field[] fields = this.getClass().getDeclaredFields();
                	for(Field f:fields){
                		try {
							Object v = f.get(this);
							AttributeParam param = new AttributeParam(v);
							map.put(f.getName(),param);
						} catch (IllegalArgumentException | IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                	}
                	return map;
                }
                
                public String getName() {
					return name;
				}

				public void setName(String name) {
					this.name = name;
				}

				public boolean isUri(){
                	return isUri==null?false:isUri.contains("true");
                }
                public String getIsUri() {
					return isUri;
				}


				public void setIsUri(String isUri) {
					this.isUri = isUri;
				}


				public String geSet() {
					return set;
				}


				public void setSet(String set) {
					this.set = set;
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
                    return type==null?"":type;
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
                    return key==null?"":key;
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
