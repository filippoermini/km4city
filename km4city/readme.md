# km4city
Documentazione per il file xml di generazione delle triple.

Utilizzo:
```shell
java -jar sensorGenerator.jar -x sensor.xml -n simulazione_parcheggi
```
1. -x indica il file per la genrazione delle triple 
2. -n nome della simulazione 

## tree 
nodo radice

propriertà:
- typeId: valore dell'URI dell' *rdf-syntax-ns#type*
- simulationName: nome della simulazione

elementi:
- istanceIterationQuery: elemento di tipo *queryInfo* contenete la query rdf con cui fare iterare le istanze definite
- fileInfo: elemento contenente i campi per la definizione del file .n3 da generare
- iterationelement: contiene una lista di istanze e di attributi da generare ad ogni iterazione

## fileInfo
contiene i campi per la definizione del file .n3 da generare.

proprietà: nessuna

elementi:
- fileName: nome del file .n3 da generare
- startDirectory: path della cartella in cui creare il file .n3
## queryInfo
elemento per la definizione di una query SPARQL

proprietà: nessuna

elementi:
- query: query SPARQL
- servar: end point a cui sottoporre la query
- bindingValue: campo su cui andare a rcuperare il valore (dafault = id)
## iterationElement
contiene gli elementi che devono essere generati ad ogni iterazione. Contiene una lista (opzionale) 
di attributi il cui valore sarà necessario ai fini del calcolo dei valori degli attibuti delle istanze, ed una lista di istanze 
rappresentanti gli oggetti rdf e di loro attributi che andranno a comporre il le triple rdf del file .n3 .

proprietà: nessuna

elementi:
- attributes: lista di elementi di tipo *properties* contnente una lista di attributi 
- list\<istance\>: lista di istanze rdf da generare

## instance
elemnto che definisce la struttura e gli attributi di un'istanza rdf.

proprietà:
- type: valore dell'uri *rdf-syntax-ns#type* dell'istanza
- name: nome dell'istanza usato per referenziare l'istanza 
- isRoot: se *true* indica se questa istanza contiene l'emento su cui inserire il valore generato dalla query
- baseUri: valore del base URI dell'istanza

elementi:
- properties: elemento contenente una lista di attributi

## properties
contiene la lista delle proprietà dell'istanza. questo elemento è usato anche per definire gli attributi generali dell'iterazione.

proprietà: nessuna

elementi:
- list\<prop\>: contiene una lista di proprietà che definiscono ognua una tripla rdf

## prop
definisce le singole proprietà dell'istanza 

proprietà: 
- key: valore dell'URI della proprietà
- name: nome della proprietà, utilizzato per referenziare la proprietà
- isHidden: questa proprietà darà origine e nessuna tripla, da utilizzare come proprietà di appoggio per la generazione di valori complessi

elementi:
- type: indica il tipo di valore che deve essere generato (ogni tipologia può necessitare alementi aggiuntivi).
- uri: valore dell'*oggetto* della tropla rdf

type value:

tip | Descrizione | Campi aggiuntivi |
------------ | ------------- | --------------
id | definische che la proprietà dovrà contenere il valore proveniente dall query di iterazione, questa proprietà deve essere attribuita all'istanza con isRoot = true | nessuno
integer | l'attributo restituirà un valore intero generato casualmente tra due valori | **maxValue**,**minValue** estremi dell'intervallo 
uuid | genera un identificativo univoco | nessuno
float | l'attributo restituirà un valore float generato casualmente tra due valori | **maxValue**,**minValue** estremi dell'intervallo 
md5  |  genera il valore attraverso la funzione md5 di una stringa | **md5String** contiene la stringa da crittografare 
datetime  | genera una data in formato ISO 8601 se non specificati gli estremi la date assume il valore dell'istante in cui è generata |  **maxValue**,**minValue** estremi dell'intervallo per la generazione di una data casuale
hourdependent |  serve a generare dei valori diversi per ogni ora della giornata in base a valori orari di riferimento |  **hourValue**: contiene i valori di riferimento per ogni ora, devono essere inseriti 24 valori separati da ';' . **range**: contiene il range di variazione all'interno del quale possiamo far variare il valore di riferimento, può essere un valore unico per tutti e 24 i valori oppure può essere definito in modo analogo a **hourValue** indicando i 24 valori del range
fromset  |  estrae un valore a caso da un set di elementi (se il set contiene un solo elemento verrà estratto sempre quello) | **set** deve contenere la lista di elementi separati da';'
query  | il valore dell'attributo è il risultato dell'esecuzione di una query, la query deve restituire un solo valore |  *elemento di tipo queryInfo rifarsi alla definizione dello stesso*
valueexpression | il valore dell'attributo è ottenuto dall'esecuzione di una espressione che può contenere variabili, le variabili si riferiscono ai valori assunti da altre propiertà | **valueExpression* contiene l'espressione algebrica o logica da eseguire, le variabile devono essere dichiarate con la seguente sintassi ${nome} dove nome è il valore inserito nelle proprietà dell'attributo *prop*

le espressione logiche ed algebriche oltre che nelle proprietà in cui sono definite possono essere inserite nei campi aggiuntivi di altre proprietà
ad esemprio nei campi maxValue o minValue oppure all'interno di una query.


Esemipo di file xml:

```xml
<?xml version="1.0"?>
<tree typeId="http://www.w3.org/1999/02/22-rdf-syntax-ns#type" isStateful="false">
    <instanceIterationQuery>
    	<server>http://servicemap.disit.org/WebAppGrafo/sparql</server>
    	<query>SELECT DISTINCT (substr(str(?s),39) as ?id) WHERE {?s a km4c:Charging_stations.} limit 10</query>
    </instanceIterationQuery>
    <fileInfo>
    	<fileName>recharge.n3</fileName>
    	<startDirectory>./recharge/</startDirectory>
    </fileInfo>
    <iterationElement>
    	<instance type="http://www.disit.org/km4city/schema#Charging_Stations" name="Charging_Station" isRoot="true" baseUri="http://www.disit.org/km4city/resource/">
	        <properties>
	            <prop key="http://purl.org/dc/terms/identifier">
	                <type>id</type>
	                <name>Charging_Stations</name>
	            </prop>
	            <prop key="http://www.w3.org/ns/ssn/hasSensor">
	                <type>Sensor1</type>
	                <uri>http://www.w3.org/ns/ssn/Sensor</uri>
	            </prop>
	        </properties>
	    </instance>
	    <instance type="http://www.w3.org/ns/ssn/Sensor" name="Sensor1" isRoot="false" baseUri="http://www.disit.org/km4city/resource/">
	        <properties>
	            <prop key="http://purl.org/dc/terms/identifier">
	                <type>valueExpression</type>
	                <valueExpression>${Charging_Stations} + "_sensor1"</valueExpression>
	            </prop>
	            <prop key="http://www.w3.org/ns/ssn/MadeObservation">
	                <type>Observation1</type>
	            </prop>
	        </properties>
	    </instance>
	    <instance type="http://www.w3.org/ns/ssn/Observation" name="Observation1" isRoot="false" baseUri="http://www.disit.org/km4city/resource/">
	        <properties>
	            <prop key="http://purl.org/dc/terms/identifier">
	                <type>md5</type>
	                <md5String>"obs1"+${Charging_Stations}+${dateTime}</md5String>
	            </prop>
	            <prop key="http://www.w3.org/ns/ssn/dateTime" isHidden="true">
	                <name>dateTime</name>
	                <type>DateTime</type>
	            </prop>
	            <prop key="http://www.w3.org/ns/ssn/observedProperty" isUri="true">
					<type>FromSet</type>
	                <set>http://www.disit.org/km4city/schema#chargeState</set>
	            </prop>
	        </properties>
	    </instance>
    </iterationElement>
</tree>
```
