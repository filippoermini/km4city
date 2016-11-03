package Application;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import genericClass.GenericAttribute;
import genericClass.GenericObject;
import jsonDomain.Attribute;
import jsonDomain.State;



public class TripleContainer {
	public class Pair<L,R> {

		  private final L left;
		  private final R right;

		  public Pair(L left, R right) {
		    this.left = left;
		    this.right = right;
		  }

		  public L getLeft() { return left; }
		  public R getRight() { return right; }

		  @Override
		  public int hashCode() { return left.hashCode() ^ right.hashCode(); }

		  @Override
		  public boolean equals(Object o) {
		    if (!(o instanceof Pair)) return false;
		    Pair pairo = (Pair) o;
		    return this.left.equals(pairo.getLeft()) &&
		           this.right.equals(pairo.getRight());
		  }

		}
	private String type; 
	private ArrayList<GenericObject> tripleObject;
	private String tripleRDF;
	private State state;
	
	public TripleContainer(String type){
		this.tripleObject = new ArrayList<>();
		this.type = type;
	}
	
	public TripleContainer(TripleContainer t){
		this.type = t.type;
		this.tripleRDF = t.tripleRDF;
		this.state = t.state;
		this.tripleObject = new ArrayList<>();
		Iterator<GenericObject> it = t.tripleObject.iterator();
		while(it.hasNext()){
			this.tripleObject.add(new GenericObject(it.next()));
		}
	}
	
	public GenericObject getObjectClassByName(String name){
		for(GenericObject g:getTripleObject()){
			if(g.getClassName().contains(name)){
				return g;
			}
		}
		return null;
	}
	
	public void add(GenericObject obj){
		this.tripleObject.add(obj);
	}
	public ArrayList<GenericObject> getTripleObject(){
		return this.tripleObject;
	}
	public void generateTriple(){
		tripleRDF = "";
		state = new State();
		for(GenericObject go:tripleObject){
			//per ogni classe genero il type
			Pair<String,String> baseUriParam = getBaseUriParam(go.getBaseUri());
			String pre = baseUriParam.getLeft();
			String suff = baseUriParam.getRight();
			String baseUri = "<"+pre+"/"+go.getIdentifier().getAttribute().gettAttributeValue()+suff+"> ";
			tripleRDF += baseUri+"<"+type+"> "+"<"+go.getType().toString()+"> .\n";
			state.add(new Attribute("id",go.getIdentifier().getAttribute().gettAttributeValue()));
			for(GenericAttribute ga:go.getAttributeList()){
				//genero per ogni attributo delle classi che compongono l'oggetto la lista delle triple
				String object = ga.isExternalKey()?"<"+ga.getExternalClassObject().getBaseUri()+"/"+ga.getAttribute().gettAttributeValue()+">":"\""+ga.getAttribute().gettAttributeValue()+"\""+(ga.getUri()!=null?"^^<"+getUriParam(ga.getUri())+">":"");
				tripleRDF += baseUri+"<"+ga.getAttributeKey()+"> "+object+" .\n";
				state.add(new Attribute(ga.getAttributeName(),ga.getAttribute().gettAttributeValue()));
			}
		}		
	}
	
	public String getRDFTriple(){
		return tripleRDF;
	}
	public State getState(){
		return state;
	}
	
	public void setID(String id){
		for(int i=0;i<getTripleObject().size();i++)
		{
			GenericObject g = getTripleObject().get(i);
			if (g.isRoot()){
				g.setID(id);
			}	
		}
	}
	public String getValueByAttributeName(String name){
		GenericAttribute value = null;
		for(GenericObject go:tripleObject){
			if((value = go.getAttributeByName(name))!= null )
				return value.getAttribute().gettAttributeValue()!=null?value.getAttribute().gettAttributeValue():null;
		}
		return null;
	}
	private String getUriParam(String uri){
		String resUri = uri;
		if(resUri.contains("$")){
			Matcher m = Pattern.compile("\\{.*?\\}").matcher(resUri);
			while(m.find()){
				String pattern = m.group();
				Matcher m1 = Pattern.compile("[$]+[a-zA-Z]+").matcher(pattern);
				m1.find();
				String var = m1.group().replace("$", "");
				String value = getIdentifierFromClassName(var);
				resUri = resUri.replace(pattern, pattern.replace(var, value).replace("{", "").replace("}", ""));
			}
		}
		return resUri;
	}
	private Pair<String,String> getBaseUriParam(String baseUri){
		String suff = "";
		String pre  = baseUri;
		if(baseUri.contains("$")){
			Matcher m = Pattern.compile("\\{.*?\\}").matcher(baseUri);
			while(m.find()){
				String pattern = m.group();
				Matcher m1 = Pattern.compile("[$]+[a-zA-Z]+").matcher(pattern);
				m1.find();
				String var = m1.group().replace("$", "");
				String value = getIdentifierFromClassName(var);
				pre = pre.replace(pattern, "");
				suff+=pattern.replace("$"+var, value).replace("{", "").replace("}", "");
			}
		}
		return new Pair<String,String>(pre,suff);
	}
	private String getIdentifierFromClassName(String className){
		for(GenericObject go:tripleObject){
			if(go.getClassName().toLowerCase().equals(className.toLowerCase()))
				return go.getIdentifier().getAttribute().gettAttributeValue();
		}
		return "";
	}
}