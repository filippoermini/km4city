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

tipo | Descrizione | Campi aggiuntivi |
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
valueexpression | il valore dell'attributo è ottenuto dall'esecuzione di una espressione che può contenere variabili, le variabili si riferiscono ai valori assunti da altre propiertà | **valueExpression** contiene l'espressione algebrica o logica da eseguire, le variabile devono essere dichiarate con la seguente sintassi ${nome} dove nome è il valore inserito nelle proprietà dell'attributo *prop*
foregoing  |  attraverso questo tipo di variabile si può andare a referenziare il valore di una variabile generata in iterazioni precedenti all'interno dell'iterazione corrente  |  **name** è il nome assunto dalla variabile nell'iterazione corrente, **defaultValue** è il valore della variabile nel caso in cui il valore di riferimento non esistesse o non sia ancora stato generato, **refValue** contiene la coppia [iterazione]{nomeVaribile} da cui prendere il valore

i tipi foregoing possono essere dichiarati solo all'interno degli attributes e non delle instances
inoltre, i campi che lo definiscono non devono contenere espressioni o riferimenti a variabili.

le iterazioni precedenti possono essere referenziate attraverso un indice @[4]{...} oppure specificando l'id dell'iterazione @['FI055ZTL00801']{...} necessariamente tra apici

le espressione logiche ed algebriche oltre che nelle proprietà in cui sono definite possono essere inserite nei campi aggiuntivi di altre proprietà
ad esemprio nei campi maxValue o minValue oppure all'interno di una query.


Esemipo di file xml:

```xml
<?xml version="1.0"?>
<tree 	typeId="http://www.w3.org/1999/02/22-rdf-syntax-ns#type" 
	baseUri="http://www.disit.org/km4city/resource/" isStateful="false">
	<instanceIterationQuery>
		<server>http://servicemap.disit.org/WebAppGrafo/sparql</server>
		<query>SELECT DISTINCT ?id WHERE 
					{?s a km4c:CarParkSensor. 
					 ?s dcterms:identifier ?id. 
					 ?s km4c:capacity ?o}
					 limit 100
		</query>
	</instanceIterationQuery>
	<fileInfo>
		<fileName>CarParkSensor.n3</fileName>
		<startDirectory>./parking</startDirectory>
	</fileInfo>
	<iterationElement>
		<instance type="http://www.disit.org/km4city/schema#CarParkSensor" name="CarParkSensor" isRoot="true">
			<properties>
				<prop key="http://purl.org/dc/terms/identifier" isPrimaryKey="true">
					<type>id</type>
				</prop>
				<prop key="http://www.disit.org/km4city/schema#capacity">
					<type>query</type>
					<name>capacity</name>
					<queryInfo>
						<server>http://servicemap.disit.org/WebAppGrafo/sparql</server>
						<query>	
								SELECT ?capacity WHERE {
								?s a km4c:CarParkSensor. 
								?s dcterms:identifier '${id}'.
								?s km4c:capacity ?capacity.
								} LIMIT 1
						</query>
						<bindingValue>capacity</bindingValue>
					</queryInfo>
				</prop>
				<prop key="http://www.disit.org/km4city/schema#hasRecord">
					<type>SituationRecord</type>
				</prop>
			</properties>
		</instance>
		<instance type="http://www.disit.org/km4city/schema#SitutatioRecord" name="SituationRecord">
			<properties>
				<prop key="http://purl.org/dc/terms/identifier" isPrimaryKey="true">
					<type>UUID</type>
				</prop>
				<prop key="http://purl.org/dc/terms/date">
					<type>DateTime</type>
					<uri>http://www.w3.org/2001/XMLSchema#dateTime</uri>
				</prop>
				<prop key="http://www.disit.org/km4city/schema#relatedToSensor">
					<type>CarParkSensor</type>
				</prop>
				<prop key="http://www.disit.org/km4city/schema#observationTime">
					<type>Instant</type>
				</prop>
				<prop key="http://www.disit.org/km4city/schema#validityStatus">
					<type>fromset</type>
					<name>validityStatus</name>
					<set>active</set>
				</prop>
				<prop key="http://www.disit.org/km4city/schema#occupied">
					<type>integer</type>
					<name>occupied</name>
					<uri>http://www.w3.org/2001/XMLSchema#integer</uri>
					<maxValue>${capacity}</maxValue>
					<minValue>0</minValue>
				</prop>
				<prop key="http://www.disit.org/km4city/schema#free">
					<type>valueExpression</type>
					<name>free</name>
					<uri>http://www.w3.org/2001/XMLSchema#integer</uri>
					<valueExpression>${capacity} - ${occupied}</valueExpression>
					<format>%.0f</format>
				</prop>
				<prop key="http://www.disit.org/km4city/schema#parkOccupancy">
					<type>valueExpression</type>
					<valueExpression>( ${occupied} / ${capacity} ) * 100 </valueExpression>
					<format>%.2f</format>
				</prop>
				<prop key="http://www.disit.org/km4city/schema#carParkStatus">
					<type>valueExpression</type>
					<valueExpression>
						<![CDATA[
							function checkStatus(free){
								if(free == 0) return 'carParkFull';
								else return 'enoughSpacesAvailable';
							}
							checkStatus(${free});
						]]>
					</valueExpression>	
				</prop>
			</properties>
		</instance>
		<instance type="http://www.w3.org/2006/time#Instant" name="Instant">
			<properties>
				<prop key="http://purl.org/dc/terms/identifier" isPrimaryKey="true">
					<type>DateTime</type>
					<uri>http://www.w3.org/2001/XMLSchema#dateTime</uri>
				</prop>
				<prop key="http://www.disit.org/km4city/schema#instantParking">
					<type>SituationRecord</type>
				</prop>
			</properties>
		</instance>
	</iterationElement>
</tree>
```
