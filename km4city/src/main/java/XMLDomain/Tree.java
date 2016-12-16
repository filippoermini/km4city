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

import genericInstance.AttributeParam;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
	"fileIterationQuery",
	"instanceIterationQuery",
    "fileInfo",
    "iterationElement"
})
@XmlRootElement(name = "tree")
public class Tree {

	@XmlAttribute(name = "isStateful")
	protected String isStateful;
	@XmlAttribute(name = "typeId")
	protected String typeId;
	@XmlAttribute(name = "identifierUri")
	protected String identifierUri;
	@XmlAttribute(name = "simulationName")
	protected String simulationName;
    @XmlElement(name = "fileIterationQuery")
    protected Tree.QueryInfo fileIterationQuery;
    @XmlElement(name = "instanceIterationQuery")
    protected Tree.QueryInfo instanceIterationQuery;
    @XmlElement(name = "fileInfo")
    protected Tree.FileInfo fileInfo;
    @XmlElement(name = "iterationElement")
    protected Tree.iterationElement iterationElement;
    @XmlAttribute(name = "baseUri")
    protected String baseUri;
    
    public String getBaseUri() {
		return baseUri;
	}

	public void setBaseUri(String baseUri) {
		this.baseUri = baseUri;
	}

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
	
	public String getIdentifierUri() {
		return identifierUri;
	}

	public void setIdentifierUri(String identifierUri) {
		this.identifierUri = identifierUri;
	}

	public Tree.QueryInfo getFileIterationQuery() {
		return fileIterationQuery==null?new QueryInfo():fileIterationQuery;
	}
	public void setFileIterationQuery(Tree.QueryInfo fileIterationQuery) {
		this.fileIterationQuery = fileIterationQuery;
	}
	public Tree.QueryInfo getInstanceIterationQuery() {
		return instanceIterationQuery==null?new QueryInfo():instanceIterationQuery;
	}
	public void setInstanceIterationQuery(Tree.QueryInfo instanceIterationQuery) {
		this.instanceIterationQuery = instanceIterationQuery;
	}
	
    public void setQueryInfo(Tree.QueryInfo fileIterationquery){
    	this.fileIterationQuery = fileIterationquery;
    }
    
    public String getIsStateful() {
		return isStateful==null?"false":isStateful;
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
		return isStateful==null?false:isStateful.contains("true");
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
    	"instance",
    	"attributes"
    })
    public static class iterationElement{
    	@XmlElement(name = "attributes")
        protected Tree.Instance.Properties attributes;
    	@XmlElement(name = "instance")
        protected List<Tree.Instance> instance;
    	
    	public Tree.Instance.Properties getAttributes() {
    		return attributes;
    	}
    	public void seAttributes(Tree.Instance.Properties hiddenProperties) {
    		this.attributes = hiddenProperties;
    	}
    	
    	public List<Tree.Instance> getInstance() {
            if (instance == null) {
            	instance = new ArrayList<Tree.Instance>();
            }
            return this.instance;
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
			return bindingValue!=null?bindingValue:"";
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
    	
    	@Override
    	public String toString(){
    		return this.query;
    	}
    	
    	public void setStringValue(String val){
    		this.query = val;
    	}
    }
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "properties"
    })
    public static class Instance {

        @XmlElement(required = true)
        protected Tree.Instance.Properties properties;
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
        public Tree.Instance.Properties getProperties() {
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
        public void setProperties(Tree.Instance.Properties value) {
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
            return isRoot==null?"false":isRoot;
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

            protected List<Tree.Instance.Properties.Prop> prop;

          
            public List<Tree.Instance.Properties.Prop> getProp() {
                if (prop == null) {
                    prop = new ArrayList<Tree.Instance.Properties.Prop>();
                }
                return this.prop;
            }


            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "type",
                "maxValue",
                "minValue",
                "datatype",
                "valueExpression",
                "hourValue",
                "range",
                "set",
                "name",
                "queryInfo",
                "md5String",
                "profilesFile",
                "profileId",
                "variance"
            })
            public static class Prop {

                @XmlElement(required = true)
                protected String type;
                protected String maxValue;
                protected String minValue;
                protected String name;
                @XmlSchemaType(name = "anyURI")
                protected String datatype;
                @XmlAttribute(name = "key")
                protected String key;
                protected String valueExpression;
                protected String hourValue;
                protected String range;
                protected String set;
                @XmlAttribute(name = "isPrimaryKey")
                protected String isPrimaryKey;
                @XmlAttribute(name = "isUri")
                protected String isUri;
                @XmlAttribute(name = "isHidden")
                protected String isHidden;
                @XmlElement(name = "queryInfo")
                protected Tree.QueryInfo queryInfo;
                protected String md5String;
                protected String profilesFile;
                protected String profileId;
                protected String variance;
                @XmlAttribute(name = "isPersistent")
                protected String isPersistent;
                
				public String getProfilesFile() {
					return profilesFile;
				}
				public void setProfilesFile(String profilesFile) {
					this.profilesFile = profilesFile;
				}
				public String getProfileId() {
					return profileId;
				}
				public void setProfileId(String profileId) {
					this.profileId = profileId;
				}
				public String getVariance() {
					return variance;
				}
				public void setVariance(String variance) {
					this.variance = variance;
				}
				public String getSet() {
					return set;
				}
				public String getMd5String() {
					return md5String;
                }
				public void setMd5String(String md5) {
					this.md5String = md5;
				}

				public String getIsPrimaryKey() {
					return isPrimaryKey;
				}
				public void setIsPrimaryKey(String isPrimaryKey) {
					this.isPrimaryKey = isPrimaryKey;
				}
				public boolean isPersistent(){
					return isPersistent==null?false:isPersistent.contains("true");
				}
				
				public String getIsPersistent() {
					return isPersistent;
				}
				public void setIsPersistent(String isPersistent) {
					this.isPersistent = isPersistent;
				}
				public boolean isPrimaryKey(){
					return isPrimaryKey==null?false:isPrimaryKey.contains("true");
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
                public String getDatatype() {
                    return datatype;
                }

                /**
                 * Imposta il valore della proprietà uri.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setDatatype(String value) {
                    this.datatype = value;
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
